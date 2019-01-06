package org.micro.epay.mall.payA.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.micro.common.constant.Const;
import org.micro.epay.mall.payA.producer.SyncProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;


@Service
public class CallbackService {

	@Autowired
	private SyncProducer syncProducer;
	
	public void sendOKMessage(String orderId, String userId) {
		
		Map<String, Object> params = new HashMap<>();
		params.put("userId", userId);
		params.put("orderId", orderId);
		params.put("status", "20");	//ok
		
		String keys = orderId;
		Message message = new Message(Const.CALLBACK_PAY_TOPIC, Const.CALLBACK_PAY_TAGS, keys,
				JSONObject.toJSONString(params).getBytes());
		
		SendResult ret = syncProducer.sendMessage(message);	
	}
	

	
}
