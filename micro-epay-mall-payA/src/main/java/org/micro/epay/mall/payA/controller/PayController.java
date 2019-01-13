package org.micro.epay.mall.payA.controller;

import org.micro.common.response.ServerResponse;
import org.micro.epay.mall.payA.service.PayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pay")
public class PayController {

	@Autowired
	private PayService payService;
	
	@RequestMapping("/mockPay")
	public ServerResponse<String> mockPay(String userId, String orderId, String accountId, double money) {
		return ServerResponse.createBySucessResReturnData(
				payService.payment(userId, orderId, accountId, money));
		
	}
	
}
