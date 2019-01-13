package org.micro.epay.mall.payB;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.ComponentScan;

@MapperScan(basePackages="org.micro.epay.mall.payB.mapper.*")
@SpringCloudApplication
@EnableFeignClients
@EnableHystrix
@EnableAutoConfiguration
@ComponentScan(basePackages="org.micro.epay.mall.payB")
public class App {
	public static void main(String[] args) {
		 SpringApplication.run(App.class, args);
	}
}
