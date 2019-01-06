package org.micro.epay.mall.payA.controller;

import org.micro.epay.mall.payA.mapper.UserMapper;
import org.micro.epay.mall.payA.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.conf.HystrixPropertiesManager;

@RestController
public class IndexController {
	
	@Autowired
	private UserMapper UserMapper;
	
	@GetMapping("/hello")
	@HystrixCommand(commandKey="threadKey1",
								   commandProperties= {
									@HystrixProperty(name=HystrixPropertiesManager.EXECUTION_ISOLATION_STRATEGY, value="THREAD")
								   },
								  threadPoolKey="threadPoolKey1",
								  threadPoolProperties= {
										  @HystrixProperty(name=HystrixPropertiesManager.CORE_SIZE, value="10"),
										  @HystrixProperty(name=HystrixPropertiesManager.MAX_QUEUE_SIZE, value="2000"),  //不可变
										  @HystrixProperty(name=HystrixPropertiesManager.QUEUE_SIZE_REJECTION_THRESHOLD, value="30") //动态可变
								  },
								  fallbackMethod="helloFailBack")
	public String hello() {
		User user = UserMapper.selectByPrimaryKey(23);
		return JSONObject.toJSONString(user);
	}
	
	public String helloFailBack() {
		System.err.println("--------服务降级，限流策略--------");
		return "--------服务降级，限流策略--------";
	}
	
	@GetMapping("/hello2")
	@HystrixCommand(commandKey="semaphoreKey1",
	   commandProperties= {
		@HystrixProperty(name=HystrixPropertiesManager.EXECUTION_ISOLATION_STRATEGY, value="SEMAPHORE"),
		@HystrixProperty(name=HystrixPropertiesManager.EXECUTION_ISOLATION_SEMAPHORE_MAX_CONCURRENT_REQUESTS, value="10")
	   },
	  fallbackMethod="helloFailBack")
	public String hello2() {
		User user = UserMapper.selectByPrimaryKey(23);
		return JSONObject.toJSONString(user);
	}

}
