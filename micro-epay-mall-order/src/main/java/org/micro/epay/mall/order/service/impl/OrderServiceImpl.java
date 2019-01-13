package org.micro.epay.mall.order.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.rocketmq.common.message.Message;
import org.micro.common.constant.Const;
import org.micro.epay.mall.order.mapper.OrderMapper;
import org.micro.epay.mall.order.pojo.Order;
import org.micro.epay.mall.order.service.OrderService;
import org.micro.epay.mall.order.service.producer.PkgOrderlyProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderMapper orderMapper;
	
	@Autowired
	private PkgOrderlyProducer pkgOrderlyProducer;
	
	@Override
	public Order getOrderByOrderNo(String orderNo) {
		Long orderNoLong = Long.valueOf(orderNo);
		
		Order record = new Order();
		record.setOrderNo(orderNoLong);
		record = orderMapper.selectOne(record);
		return record;
	}
	
	/* 
	 * 对成功更改状态的订单发送两条顺序的消息：1.创建包裹的操作 --》 2.发送物流通知
	 * (non-Javadoc)
	 * @see org.micro.epay.mall.order.service.OrderService#sendOrderlyMessage4Pkg(java.lang.String, java.lang.String)
	 */
	@Override
	public void sendOrderlyMessage4Pkg(String userId, String orderId) {
		List<Message> messages = new ArrayList<>();
		
		Map<String, Object> param1 = new HashMap<String, Object>();
		param1.put("userId", userId);
		param1.put("orderId", orderId);
		param1.put("text", "创建包裹操作---1");
		String key1 = orderId + "$" + UUID.randomUUID().toString() ;
		Message message1 = new Message(Const.PKG_TOPIC, Const.PKG_TAGS, key1, JSONObject.toJSONString(param1).getBytes());
		
		Map<String, Object> param2 = new HashMap<String, Object>();
		param2.put("userId", userId);
		param2.put("orderId", orderId);
		param2.put("text", "发送物流通知操作---2");
		String key2 = orderId + "$" + UUID.randomUUID().toString() ;
		Message message2 = new Message(Const.PKG_TOPIC, Const.PKG_TAGS, key2, JSONObject.toJSONString(param2).getBytes());
		messages.add(message1);
		messages.add(message2);
		
		Order order = this.getOrderByOrderNo(orderId);
		
		//这一步是使用顺序消费的关键，这里定义了supplyId作为topic下队列的id
		int supplyId = Integer.valueOf(order.getSupplierId());  
		pkgOrderlyProducer.sendOrderlyMessages(messages, supplyId);
	}
	
}
