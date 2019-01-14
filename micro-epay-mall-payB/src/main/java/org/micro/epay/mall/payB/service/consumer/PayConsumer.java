package org.micro.epay.mall.payB.service.consumer;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.micro.common.constant.Const;
import org.micro.epay.mall.payB.mapper.UserMapper;
import org.micro.epay.mall.payB.pojo.Order;
import org.micro.epay.mall.payB.pojo.User;
import org.micro.epay.mall.payB.service.IOrderFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class PayConsumer {
	
	private static final String CONSUMER_GROUP_NAME = "tx_pay_consumer_group_name";
	
	@Autowired
	UserMapper userMapper;
	
	@Autowired
	IOrderFeignClient iOrderFeignClient;
	
	private DefaultMQPushConsumer consumer;
	
	private PayConsumer() throws MQClientException {
		this.consumer = new DefaultMQPushConsumer(CONSUMER_GROUP_NAME);
		this.consumer.setNamesrvAddr(Const.ROCKETMQ_NAMESRV);
		this.consumer.setConsumeThreadMin(10);
		this.consumer.setConsumeThreadMax(30);
		this.consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
		this.consumer.subscribe(Const.TX_PAY_TOPIC, Const.TX_PAY_TAGS);
		this.consumer.registerMessageListener(new MessageListenerConcurrently4Pay());
		this.consumer.start();
	}
	
	class MessageListenerConcurrently4Pay implements MessageListenerConcurrently {
		
		@Override
		public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
			MessageExt msg = msgs.get(0);
			
			try {
				String topic = msg.getTopic();
				String tags = msg.getTags();
				String keys = msg.getKeys();
				String body = new String(msg.getBody(), RemotingHelper.DEFAULT_CHARSET);
				System.err.println("收到事务消息, topic: " + topic + ", tags: " + tags + ", keys: " + keys + ", body: " + body);
				
				Map<String, Object> paramsBody = JSONObject.parseObject(body, Map.class);
				String userId = (String)paramsBody.get("userId");	// customer userId
				String accountId = (String)paramsBody.get("accountId");	//customer accountId
				String orderId = (String)paramsBody.get("orderId");	// 	统一的订单
				BigDecimal money = (BigDecimal)paramsBody.get("money");	//	当前的收益款
				
				//消息过来后首先通过keys（唯一的）做数据库对同一主键去重，保证幂等性
				Order order = iOrderFeignClient.getOrderByOrderNo(orderId).getData();
				if (order != null && order.getStatus() == 30) {
					log.warn("当前订单{}已收款成功，请勿重复下单", orderId);
					return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
				}
				
				User user = userMapper.selectByPrimaryKey(Integer.valueOf(accountId));
				user.setCurrentBalance(user.getCurrentBalance().add(money));
				user.setUpdateTime(new Date());
				user.setVersion(user.getVersion() + 1);
				int ret = userMapper.updateByPrimaryKeySelective(user);
				if (ret != 1) {
					log.error("数据库更新收款人信息失败，稍后重试...");
					return ConsumeConcurrentlyStatus.RECONSUME_LATER;
				}
				
				
				//mq将order表status状态改为30-已收款
			} catch (Exception e) {
				e.printStackTrace();
				if (context.getDelayLevelWhenNextConsume() == 3) {
					log.error("多次处理依然失败，请人工处理做补偿处理，详细信息：{}", JSONObject.toJSONString(msg));
				}
				return ConsumeConcurrentlyStatus.RECONSUME_LATER;
			}
			return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
		}
		
	}

}

