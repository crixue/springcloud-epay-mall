package org.micro.epay.mall.payA.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.common.message.Message;
import org.micro.common.constant.Const;
import org.micro.epay.mall.payA.mapper.UserMapper;
import org.micro.epay.mall.payA.pojo.User;
import org.micro.epay.mall.payA.producer.TransactionProducer;
import org.micro.epay.mall.payA.service.PayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

@Service
public class PayServiceImpl implements PayService {

	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private TransactionProducer producer;
	
	@Autowired
	private CallbackService callbackService;

	@Override
	public String payment(String userId, String orderId, String accountId, double money) {
		String paymentRet = "";

		try {
			//	最开始有一步 token验证操作（重复提单问题）
			BigDecimal payMoney = new BigDecimal(money);
			
			// 采用redis分布式锁获取数据库锁，避免集群环境下的同时操纵数据库
			//redis 加锁
			User user = userMapper.selectByPrimaryKey(Integer.valueOf(userId));
			BigDecimal currentBalance = user.getCurrentBalance();
			int currentVersion = user.getVersion();
			//		要对大概率事件进行提前预判（小概率事件我们做放过,但是最后保障数据的一致性即可）
			//业务出发:
			//当前一个用户账户 只允许一个线程（一个应用端访问）
			//技术出发：
			//1 redis去重 分布式锁
			//2 数据库乐观锁去重
			//	做扣款操作的时候：获得分布式锁，看一下能否获得
			BigDecimal newBalance = currentBalance.subtract(payMoney);

			//redis 加锁 結束，已經获得应该有的version了，释放锁
			
			if (newBalance.doubleValue() > 0) {
				//1.组装发送消息并异步发送
				String keys = orderId;
				Map<String, Object> params = new HashMap<>();
				params.put("userId", userId);
				params.put("orderId", orderId);
				params.put("accountId", accountId);  //收款人id
				params.put("money", money);	//本次消费金额
				params.put("currentVersion", currentVersion);
				params.put("newBalance", newBalance);
				
				CountDownLatch countDownLatch = new CountDownLatch(1);  //同步阻塞需要让发消息和执行本地事务单元都操作完
				params.put("currentCountDown", countDownLatch);
				Message message = new Message(Const.TX_PAY_TOPIC, Const.TX_PAY_TAGS, keys, JSONObject.toJSONString(params).getBytes());
				TransactionSendResult result = producer.sendMessage(message, params);  //在trasactionListenerImpl中异步执行用户扣款的操作
				countDownLatch.await();
				
				if (result.getSendStatus() == SendStatus.SEND_OK 
						&& result.getLocalTransactionState() == LocalTransactionState.COMMIT_MESSAGE) {
					paymentRet = "支付成功";
					callbackService.sendOKMessage(orderId, userId);
					return paymentRet;
				}
			}
			
			paymentRet = "支付失败";
			return paymentRet;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			paymentRet = "支付失败";
			return paymentRet;
		}
		
	}

}
