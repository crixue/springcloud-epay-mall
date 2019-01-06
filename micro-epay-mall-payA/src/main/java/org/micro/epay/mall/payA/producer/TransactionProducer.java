package org.micro.epay.mall.payA.producer;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.common.message.Message;
import org.micro.common.constant.Const;
import org.micro.common.properties.CustomServer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class TransactionProducer implements InitializingBean{
	
	@Autowired
	private CustomServer customServer;

	private static final String PRODUCER_GROUP_NAME = "tx_pay_producer_group_name";

	private TransactionMQProducer producer;

	private ExecutorService executorService;
	
	@Autowired
	private TransactionListenerImpl transactionListenerImpl;

	public TransactionProducer() {
		producer = new TransactionMQProducer(PRODUCER_GROUP_NAME);
		producer.setNamesrvAddr(Const.ROCKETMQ_NAMESRV);
		executorService = new ThreadPoolExecutor(2, 8, 100, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(2000),
				new ThreadFactory() {
					@Override
					public Thread newThread(Runnable r) {
						Thread t = new Thread(r);
						t.setName(PRODUCER_GROUP_NAME + "-check-thread");
						return t;
					}
				});
		producer.setExecutorService(executorService);
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		// 处理事务监听处理器
		producer.setTransactionListener(transactionListenerImpl);
		this.start();
	}
	
	public TransactionSendResult sendMessage(Message message, Object argument) {
		TransactionSendResult sendResult = null;
		
		try {
			sendResult = producer.sendMessageInTransaction(message, argument);
		} catch (MQClientException e) {
			e.printStackTrace();
		}
		return sendResult;
	}
	
	private void start() {
		try {
			producer.start();
		} catch (MQClientException e) {
			e.printStackTrace();
		}
	}
	
	public void shutdown() {
		this.producer.shutdown();
	}

}
