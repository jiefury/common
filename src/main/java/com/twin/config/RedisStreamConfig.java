package com.twin.config;

import com.twin.utils.SpringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.data.redis.stream.Subscription;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Slf4j
//@RequiredArgsConstructor
//@Configuration
public class RedisStreamConfig {


    @Value("${spring.redis-stream.key}")
    private String redisStreamKey;

    @Value("${spring.redis-stream.group}")
    private String redisStreamGroup;

    @Autowired
    private RedisStreamListenerMessage streamListener;


//    private final RedisStream redisStream = SpringUtils.getBean(RedisStream.class);


    /**
     * 收到消息后不自动确认，需要用户选择合适的时机确认
     * <p>
     * 当某个消息被ACK，PEL列表就会减少
     * 如果忘记确认（ACK），则PEL列表会不断增长占用内存
     * 如果服务器发生意外，重启连接后将再次收到PEL中的消息ID列表
     *
     * @return
     */
    @Bean
    public Subscription subscription(StreamMessageListenerContainer listenerContainer) {
//        checkGroup(redisStreamKey, redisStreamGroup);
        // 创建Stream消息监听容器配置
        checkGroup(redisStreamKey, redisStreamGroup);
        // 设置消费手动提交配置
        Subscription subscription = listenerContainer.receive(
                // 设置消费者分组和名称
                Consumer.from(redisStreamGroup, "consumer-1"),
                // 设置订阅Stream的key和获取偏移量，以及消费处理类
                StreamOffset.create(redisStreamKey, ReadOffset.lastConsumed()),
                streamListener);
        // 监听容器启动
        listenerContainer.start();
        return subscription;
    }

    @Bean
    public StreamMessageListenerContainer<String, MapRecord<String, String, String>> streamMessageListenerContainer(RedisConnectionFactory factory) {
        // 创建Stream消息监听容器配置
//        StreamMessageListenerContainer.StreamMessageListenerContainerOptions<String, ObjectRecord<String, String>> options = StreamMessageListenerContainer
//                .StreamMessageListenerContainerOptions
//                .builder()
//                // 设置阻塞时间
//                .pollTimeout(Duration.ofSeconds(1))
//                // 配置消息类型
//                .targetType(String.class)
//                .build();
        // 创建Stream消息监听容器


//        StreamMessageListenerContainer<String, ObjectRecord<String, String>> listenerContainer = StreamMessageListenerContainer.create(factory, options);
        StreamMessageListenerContainer.StreamMessageListenerContainerOptions<String, MapRecord<String, String, String>> options =
                StreamMessageListenerContainer.StreamMessageListenerContainerOptions
                        .builder()
                        // 一次最多获取多少条消息
                        .batchSize(3)
                        // 运行 Stream 的 poll task
//                        .executor(executor)
                        // Stream 中没有消息时，阻塞多长时间，需要比 `spring.redis.timeout` 的时间小
                        .pollTimeout(Duration.ofSeconds(3))
                        // 获取消息的过程或获取到消息给具体的消息者处理的过程中，发生了异常的处理
                        .build();



        StreamMessageListenerContainer<String, MapRecord<String, String, String>> streamMessageListenerContainer =
                StreamMessageListenerContainer.create(factory, options);

        // 设置消费手动提交配置
        return streamMessageListenerContainer;
    }

    /**
     * 由于订阅需要先有stream，先做下检查
     */
    private void checkGroup(String key, String group) {
        // 创建需要校验的分组List
        List<String> consumers = new ArrayList<>();
        consumers.add(group);
        StreamInfo.XInfoConsumers infoGroups = null;
        RedisStream redisStream = SpringUtils.getBean(RedisStream.class);
        try {
            // 获取Stream的所有组信息
            infoGroups = redisStream.consumers(key, group);
        } catch (RedisSystemException | InvalidDataAccessApiUsageException ex) {
            log.error("group key not exist or commend error", ex);
        }

        // 遍历校验分组是否存在
        for (String consumer : consumers) {
            boolean consumerExist = Objects.nonNull(infoGroups);
            // 创建不存在的分组
            if (!consumerExist) {
                redisStream.creartGroup(key, consumer);
            }
        }
    }

}
