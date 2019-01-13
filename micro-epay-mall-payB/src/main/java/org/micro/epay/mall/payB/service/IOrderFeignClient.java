package org.micro.epay.mall.payB.service;

import org.micro.common.response.ServerResponse;
import org.micro.epay.mall.payB.pojo.Order;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import feign.Param;

@FeignClient(name="micro-epay-mall-order")
public interface IOrderFeignClient {

	@GetMapping("/order/getOrderByOrderNo/{orderNo}")
	public ServerResponse<Order> getOrderByOrderNo(@PathVariable("orderNo")String orderNo);
	
}
