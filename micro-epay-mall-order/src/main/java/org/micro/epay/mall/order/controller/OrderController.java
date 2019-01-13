package org.micro.epay.mall.order.controller;

import org.apache.commons.lang3.StringUtils;
import org.micro.common.response.ServerResponse;
import org.micro.epay.mall.order.pojo.Order;
import org.micro.epay.mall.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {
	
	@Autowired
	private OrderService orderService;
	
	@GetMapping("/getOrderByOrderNo/{orderNo}")
	public ServerResponse<Order> getOrderByOrderNo(@PathVariable("orderNo")String orderNo) {
		if (StringUtils.isBlank(orderNo)) {
			return ServerResponse.createBySucessResReturnData(null);
		}
		
		return ServerResponse.createBySucessResReturnData(orderService.getOrderByOrderNo(orderNo));
	}

}
