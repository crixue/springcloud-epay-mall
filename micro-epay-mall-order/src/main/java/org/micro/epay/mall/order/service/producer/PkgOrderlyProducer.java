package org.micro.epay.mall.order.service.producer;

import java.util.List;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.micro.common.constant.Const;
import org.springframework.stereotype.Component;

/**
 * 顺序地向包裹处理系统投递消息
 * @author crixue
 */
@Component
public class PkgOrderlyProducer {
	
	private DefaultMQProducer producer;
	
	public static final String PRODUCER_GROUP_NAME = "orderly_producer_group_name";
	
	private PkgOrderlyProducer() {
		this.producer = new DefaultMQProducer(PRODUCER_GROUP_NAME);
		this.producer.setNamesrvAddr(Const.ROCKETMQ_NAMESRV);
		this.producer.setSendMsgTimeout(3000);
		start();
	}

	public void start() {
		try {
			this.producer.start();
		} catch (MQClientException e) {
			e.printStackTrace();
		}
	}
	
	public void shutdown() {
		this.producer.shutdown();
	}
	
	public void sendOrderlyMessages(List<Message> messageList, int supplyId) {
		for (Message message : messageList) {
			
			try {
				this.producer.send(message, new MessageQueueSelector() {
					@Override
					public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
						//为同一supperId的消费者选择同一消费队列，以便消费者端对同一队列的消息进行处理
						Integer supperId = (Integer)arg;
						int index = supplyId % 16;
						return mqs.get(index);
					}
				}, supplyId);
			} catch (MQClientException | RemotingException | MQBrokerException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	
}
