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
	
	@GetMapping("/mockPay")
	public ServerResponse<String> mockPay() {
		return ServerResponse.createBySucessResReturnData(
				payService.payment("1", "1492091141269", "22", new Double(22.00)));
		
	}
	
}
