package org.micro.epay.mall.payA.producer;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.micro.epay.mall.payA.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class TransactionListenerImpl implements TransactionListener{
	
	@Autowired
	private UserMapper userMapper;

	/* 
	 * 发往mq的同时，异步的执行事务单元
	 * (non-Javadoc)
	 * @see org.apache.rocketmq.client.producer.TransactionListener#executeLocalTransaction(org.apache.rocketmq.common.message.Message, java.lang.Object)
	 */
	@Override
	public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
		System.err.println("----执行本地事务单元----");
		try {
			Map<String, Object> params = (Map<String, Object>) arg;
			String userId = (String)params.get("userId");  //付款人id
			String accountId = (String)params.get("accountId");  //收款人id
			String orderId = (String)params.get("orderId");  //orderNo
			BigDecimal payMoney = (BigDecimal)params.get("payMoney");	//	当前的支付款
			BigDecimal newBalance = (BigDecimal)params.get("newBalance");	//	前置扣款成功的余额
			int currentVersion = (int)params.get("currentVersion");
			CountDownLatch currentCountDown = (CountDownLatch)params.get("currentCountDown");
			
			int ret = userMapper.updateBalance(Integer.valueOf(userId), newBalance, currentVersion, new Date());
			if (ret == 1) {
				currentCountDown.countDown();
				return LocalTransactionState.COMMIT_MESSAGE;
			} else {
				currentCountDown.countDown();
				return LocalTransactionState.ROLLBACK_MESSAGE;
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return LocalTransactionState.ROLLBACK_MESSAGE;
		}
	}

	@Override
	public LocalTransactionState checkLocalTransaction(MessageExt msg) {
		// TODO Auto-generated method stub
		return null;
	}

}
