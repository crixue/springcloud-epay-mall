package org.micro.common.constant;

public class Const {

	public static final String ROCKETMQ_NAMESRV = "192.168.2.110:9876";
	
	//支付事务消息的topic
	public static final String TX_PAY_TOPIC = "tx_pay_topic";
	
	public static final String TX_PAY_TAGS = "pay";
	
	//支付完成后的订单消费回调的topic
	public static final String CALLBACK_PAY_TOPIC = "callback_pay_topic";
	
	public static final String CALLBACK_PAY_TAGS = "callback_pay";
	
}
