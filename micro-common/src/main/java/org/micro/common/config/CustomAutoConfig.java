package org.micro.common.config;

import org.micro.common.properties.CustomProperties;
import org.micro.common.properties.CustomServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass({CustomServer.class})
@EnableConfigurationProperties(CustomProperties.class)
public class CustomAutoConfig {

	@Autowired
	private CustomProperties customProperties;
	
	@Bean
	@ConditionalOnMissingBean(CustomServer.class)
	public CustomServer customResolver() {
		CustomServer customServer = new CustomServer();
		customServer.setRocketmqNamesrv(customProperties.getRocketmqNamesrv());
		return customServer;
	}
	
	
}
