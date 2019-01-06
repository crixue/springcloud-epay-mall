package org.micro.epay.mall.payA.mapper;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.micro.common.mapper.MyBaseMapper;
import org.micro.epay.mall.payA.pojo.User;

@Mapper
public interface UserMapper extends MyBaseMapper<User> {
	
	int updateBalance(@Param("accountId")int accountId, @Param("newBalance")BigDecimal newBalance,
			@Param("version")int currentVersion, @Param("updateTime")Date currentTime);
}