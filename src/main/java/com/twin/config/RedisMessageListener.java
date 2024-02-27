package com.twin.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

//@Component
//public class RedisMessageListener implements MessageListener {
//    @Autowired
//    private RedisTemplate redisTemplate;
//
//    @Override
//    public void onMessage(Message message, byte[] bytes) {
//
//        // 获取value定义的序列化方式反序列化
//        Object messageBody = redisTemplate.getValueSerializer().deserialize(message.getBody());
//        byte[] channelByte = message.getChannel();
//        // 使用string的序列化方式反序列化
//        Object channel = redisTemplate.getStringSerializer().deserialize(channelByte);
//
//        System.out.println("RedisMessageListener---channel---: " + channel);
//        System.out.println("RedisMessageListener---message---: " + messageBody);
//
//    }
//}
