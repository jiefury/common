package com.twin.config;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * redis Stream 监听消息
 *
 * @author cheneq
 */
@Slf4j
//@Component
public class RedisStreamListenerMessage implements StreamListener<String, MapRecord<String, String, String>> {


    /**
     * redis Stream 工具类
     */
    @Autowired
    private RedisStream redisStream;

    @Value("${spring.redis-stream.key}")
    private String redisStreamKey;

    @Value("${spring.redis-stream.group}")
    private String redisStreamGroup;

    /**
     * 消息监听
     *
     * @param message e
     */
//    @SneakyThrows
//    @Override
//    public void onMessage(ObjectRecord<String, Object> message) {
    @Override
    public void onMessage(MapRecord<String, String, String> message) {
        log.info("接受redisStream: {},监听到消息：{}", message.getStream(), message);
        try {
            if (redisStreamKey.equals(message.getStream())) {
                Object value = message.getValue();
                Map<String, String> map = message.getValue();
                String jsonString = JSONObject.toJSONString(value);

                log.info(jsonString);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        redisStream.ack(String.valueOf(message.getStream()), redisStreamGroup, String.valueOf(message.getId()));
        log.info("确认消费key: {}, 群组：{}, 消息Id：{}", message.getStream(), redisStreamGroup, message.getId());
    }
}
