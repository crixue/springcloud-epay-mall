package org.micro.epay.mall.order.service;

import org.micro.epay.mall.order.pojo.Order;

public interface OrderService {

	Order getOrderByOrderNo(String orderNo);

	void sendOrderlyMessage4Pkg(String userId, String orderId);

}
