package org.micro.epay.mall.pkg.service.consumer;

import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.micro.common.constant.Const;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

@Component
public class PkgOrderlyConsumer {

	private DefaultMQPushConsumer consumer;
	
	public static final String CONSUMER_GROUP_NAME = "orderly_consumer_group_name"; 

	private PkgOrderlyConsumer() throws MQClientException {
		this.consumer = new DefaultMQPushConsumer(CONSUMER_GROUP_NAME);
		this.consumer.setNamesrvAddr(Const.ROCKETMQ_NAMESRV);
		this.consumer.setConsumeThreadMin(10);
		this.consumer.setConsumeThreadMax(30);
		this.consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
		this.consumer.subscribe(Const.PKG_TOPIC, Const.PKG_TAGS);
		this.consumer.setMessageListener(new PkgOrderlyListener());
		this.consumer.start();
	}
	
	class PkgOrderlyListener implements MessageListenerOrderly {

		@Override
		public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
			Iterator<MessageExt> iterator = msgs.iterator();
			int count = 0;
			while (iterator.hasNext()) {
				try {
					MessageExt msg = (MessageExt) iterator.next();
					count ++;
					String topic = msg.getTopic();
					String msgBody = new String(msg.getBody(), "utf-8");
					String tags = msg.getTags();
					String keys = msg.getKeys();	
					System.err.println("当前是第" + count + "条消息，收到消息为：" + "  topic :" + topic + "  ,tags : " + tags + "keys :" + keys + ", msg : " + msgBody);
					
					Map<String, Object> body = JSONObject.parseObject(msgBody, Map.class);
					String orderId = (String) body.get("orderId");
					String userId = (String) body.get("userId");
					String text = (String)body.get("text");
					
					Thread.sleep(3000);  //模拟业务操作,PS: 创建包裹信息  、对物流的服务调用（异步调用
					System.err.println("当前正在操作业务是："+text);
				} catch (UnsupportedEncodingException | InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
				}
			}
			
			return ConsumeOrderlyStatus.SUCCESS;
		}
		
	}
}
