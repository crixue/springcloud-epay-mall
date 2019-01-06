package org.micro.common.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@ConfigurationProperties(prefix="custom")
//@Component
@Data
public class CustomProperties {
	
//	@Value("${custom.rocketmqNamesrv}")
	private String rocketmqNamesrv;

}
