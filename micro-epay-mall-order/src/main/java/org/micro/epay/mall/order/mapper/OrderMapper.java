package org.micro.epay.mall.order.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.micro.common.mapper.MyBaseMapper;
import org.micro.epay.mall.order.pojo.Order;

@Mapper
public interface OrderMapper extends MyBaseMapper<Order> {
}