package org.micro.epay.mall.payA.service;

public interface PayService {

	String payment(String userId, String orderId, String accountId, double money);

}
