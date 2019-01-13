package org.micro.epay.mall.payA.service;

public interface PayService {

	/**
	 * @param userId  付款人id
	 * @param orderId
	 * @param accountId  收款人id
	 * @param money  付款金额
	 * @return
	 */
	String payment(String userId, String orderId, String accountId, double money); 
}
