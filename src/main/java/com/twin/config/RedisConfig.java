package com.twin.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.Topic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.ArrayList;
import java.util.List;


//@Configuration
public class RedisConfig {

    /**
     * 自定义序列化方式
     */
    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        // 使用Jackson2JsonRedisSerialize 替换默认序列化
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 将类名称序列化到json串中，去掉会导致得出来的是LinkedHashMap对象，直接转换实体对象会失败
        objectMapper.activateDefaultTyping(
                LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.WRAPPER_ARRAY);
        // 解决jackson2无法反序列化LocalDateTime的问题,因为LocalDateTime没有constructor
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.registerModule(new JavaTimeModule());

        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

        // 初始化string的序列化方式
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        // key采用String的序列化方式
        redisTemplate.setKeySerializer(stringRedisSerializer);
        // hash的key也采用String的序列化方式
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        // value序列化方式采用jackson
        redisTemplate.setValueSerializer(stringRedisSerializer);
        // hash的value序列化方式采用jackson
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);

        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    //    @Bean
//    public RedisMessageListenerContainer container(RedisConnectionFactory factory, RedisMessageListener listener) {
//        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
//        container.setConnectionFactory(factory);
//        // 订阅频道dog 和 cat ,添加多个频道
//        container.addMessageListener(listener, new ChannelTopic("test"));
//        return container;
//    }
//    **
//            * MessageListenerAdapter内部使用反射机制处理传入的类及指定的方法，默认方法名称为handleMessage
//     */
//    @Bean
//    public MessageListenerAdapter listenerAdapter(RedisMessageReceiver receiver) {
//        return new MessageListenerAdapter(receiver, "receiveMessage");
//    }
//
//    @Bean
//    public RedisMessageListenerContainer container(RedisConnectionFactory factory, MessageListenerAdapter listener) {
//        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
//        container.setConnectionFactory(factory);
//        // 订阅频道dog 和 cat ,添加多个频道
//        ChannelTopic test = new ChannelTopic("test");
//        ChannelTopic test2 = new ChannelTopic("test2");
//        ChannelTopic test3 = new ChannelTopic("test3");
//        List<Topic> list = new ArrayList<>();
//        list.add(test);
//        list.add(test2);
//        list.add(test3);
//        container.addMessageListener(listener, list);
//
//        return container;
//    }

}
