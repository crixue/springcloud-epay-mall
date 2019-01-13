package org.micro.epay.mall.order.service.consumer;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageConst;
import org.apache.rocketmq.common.message.MessageExt;
import org.micro.common.constant.Const;
import org.micro.epay.mall.order.mapper.OrderMapper;
import org.micro.epay.mall.order.pojo.Order;
import org.micro.epay.mall.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class OrderConsumer {
	
	private DefaultMQPushConsumer consumer;
	
	public static final String CONUSMER_GROUP = "callback_pay_consumer_group";
	
	@Autowired
	private OrderMapper orderMapper;
	
	@Autowired
	private OrderService orderService;
	
	public OrderConsumer() throws MQClientException {
		consumer = new DefaultMQPushConsumer(CONUSMER_GROUP);
		consumer.setConsumeThreadMin(10);
		consumer.setConsumeThreadMax(50);
		consumer.setNamesrvAddr(Const.ROCKETMQ_NAMESRV);
		consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
		consumer.subscribe(Const.CALLBACK_PAY_TOPIC, Const.CALLBACK_PAY_TAGS);
		consumer.registerMessageListener(new MessageListenerConcurrently4Pay());
		consumer.start();
	}
	
	class MessageListenerConcurrently4Pay implements MessageListenerConcurrently {

		@Override
		public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
			MessageExt msg = msgs.get(0);
			
			try {
				String topic = msg.getTopic();
				String msgBody = new String(msg.getBody(), "utf-8");
				String tags = msg.getTags();
				String keys = msg.getKeys();	
				log.debug("收到消息需要更新订单状态的消息：" + "  topic :" + topic + "  ,tags : " + tags + "keys :" + keys + ", msg : " + msgBody);
				String orignMsgId = msg.getProperties().get(MessageConst.PROPERTY_ORIGIN_MESSAGE_ID);
				log.debug("orignMsgId: " + orignMsgId);
				
				Map<String, Object> body = JSONObject.parseObject(msgBody, Map.class);
				String orderId = (String) body.get("orderId");
				String userId = (String) body.get("userId");
				String status = (String)body.get("status");
				
				Order changedOrder = new Order();
				changedOrder.setOrderNo(Long.valueOf(orderId));
				changedOrder.setUserId(Integer.valueOf(userId));
				changedOrder = orderMapper.selectOne(changedOrder);
				if (changedOrder == null || changedOrder.getId() == null) {
					log.error("不存在创建的订单");
					return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
				}
				
				changedOrder.setStatus(Integer.valueOf(status));
				int ret = orderMapper.updateByPrimaryKeySelective(changedOrder);
				if(ret == 1) {
					log.info("已成功将投递订单{}的状态更新为已付款，下一步将信息投递到包裹处理服务", orderId);
					//订单包裹进行投递
					orderService.sendOrderlyMessage4Pkg(userId, orderId);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				return ConsumeConcurrentlyStatus.RECONSUME_LATER;
			}
			return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
		}
		
	}

}
