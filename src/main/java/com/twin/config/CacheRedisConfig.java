package com.twin.config;

import com.twin.constant.RedisCacheNameConstants;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableCaching
public class CacheRedisConfig {

  @Value("${spring.application.name}")
  private String applicationName;

  @Bean
  public RedisTemplate<String, Object> redisTemplate(
      RedisConnectionFactory redisConnectionFactory) {

    RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
    redisTemplate.setConnectionFactory(redisConnectionFactory);

    StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
    GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer =
            new GenericJackson2JsonRedisSerializer();

    redisTemplate.setKeySerializer(stringRedisSerializer); // key

    redisTemplate.setValueSerializer(genericJackson2JsonRedisSerializer);

    redisTemplate.setHashKeySerializer(stringRedisSerializer);
    redisTemplate.setHashValueSerializer(genericJackson2JsonRedisSerializer);

    redisTemplate.afterPropertiesSet();
    return redisTemplate;
  }

  /**
   * @author liukx
   * @date 2021/8/16
   */
  @SneakyThrows
  @Bean
  public RedisCacheManager redisCacheManager(@Qualifier("redisTemplate") RedisTemplate redisTemplate) {
    RedisConnectionFactory redisConnectionFactory = redisTemplate.getConnectionFactory();

    Map<String, RedisCacheConfiguration> initialCacheMap = new HashMap<>();

    for (Field field : RedisCacheNameConstants.class.getDeclaredFields()) {
      String fieldName = field.getName();
      String fieldValue = (String) field.get(null);
      if (fieldValue.equals(RedisCacheNameConstants.CACHE_DEFAULT)) {
        continue;
      }
      if (!fieldName.equals(fieldValue)) {
        throw new RuntimeException("缓存字段名和值定义不一致,容易产生歧义:RedisCacheNameConstants." + fieldName);
      }

      long cacheSeconds = RedisCacheNameConstants.getSeconds(fieldValue);
      RedisCacheConfiguration redisCacheConfiguration =
          RedisCacheConfiguration.defaultCacheConfig()
                  .entryTtl(Duration.ofSeconds(cacheSeconds));
      initialCacheMap.put(fieldValue, redisCacheConfiguration);
    }

    //这里值序列化不能使用json,序列化之后,反序列化回来的时候变成了map,类型就转不回来了,还是使用默认的jdk序列化
//    RedisSerializationContext.SerializationPair<?> valueSerializationPair = RedisSerializationContext
//            .fromSerializer(redisTemplate.getValueSerializer())
//            .getValueSerializationPair();

    long defaultCacheSeconds =
        RedisCacheNameConstants.getSeconds(RedisCacheNameConstants.CACHE_DEFAULT);
    // 默认配置
    RedisCacheConfiguration defaultRedisCacheConfiguration =
        RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofSeconds(defaultCacheSeconds));
//                .serializeValuesWith();

    RedisCacheManager.RedisCacheManagerBuilder builder =
        RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(redisConnectionFactory)
            .cacheDefaults(defaultRedisCacheConfiguration)
            .withInitialCacheConfigurations(initialCacheMap);
    return builder.build();
  }

  @Bean
  public KeyGenerator diyKeyGenerator() {
    return new KeyGenerator() {
      @Override
      public Object generate(Object target, Method method, Object... params) {
        StringBuilder stb = new StringBuilder();
        stb.append(applicationName)
                .append(":")
                .append(target.getClass().getSimpleName())
                .append(":")
                .append(method.getName())
                .append("_");
        for (Object param : params) {
          if(param == null){
            stb.append("null").append("|");
          }else {
            stb.append(param.toString()).append("|");
          }
        }
        return stb.toString();
      }
    };

  }

}
