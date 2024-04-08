package com.twin.constant;

import org.springframework.cache.annotation.Cacheable;

import java.util.concurrent.TimeUnit;

/**
 * redis缓存名时间定义,每个缓存的缓存时间不一样,字段名和值定义成一样的,方便显式的设置每个缓存的时间,有需要的时间在这里加 Created by liukx on 2021/8/16.
 */
public interface RedisCacheNameConstants {

  String CACHE_DEFAULT = "CACHE_DEFAULT_1_HOURS";
  String CACHE_1_SECONDS = "CACHE_1_SECONDS";
  String CACHE_1_MINUTES = "CACHE_1_MINUTES";
  String CACHE_30_MINUTES = "CACHE_30_MINUTES";
  String CACHE_1_HOURS = "CACHE_1_HOURS";
  String CACHE_6_HOURS = "CACHE_6_HOURS";
  String CACHE_1_DAYS = "CACHE_1_DAYS";

  String SYS_DEPT_QUERY_CACHE_1_DAYS = "SYS_DEPT_QUERY_CACHE_1_DAYS";

  /**
   * 把字符串的时间表达式转换成数值
   *
   * @author liukx
   * @date 2021/8/16
   */
  static long getSeconds(String cacheString) {
    String[] array = cacheString.split("_");
    //数组的最后一个是时间单位,倒数第二个是值
    String timeStr = array[array.length-2];
    String timeUnitStr = array[array.length-1];
    TimeUnit timeUnit = TimeUnit.valueOf(timeUnitStr);
    return timeUnit.toSeconds(Integer.parseInt(timeStr));
  }

  /**
   * 这是spring @Cacheable的cacheKey的格式,统一起来,方便调用 @CacheEvict,
   * 比如 {@link net.hiim.system.service.resource.impl.ResInstrumentServiceImpl#updateInstrument}
   * @author liukx
   * @date 2022/5/3
   */
  static String getRedisCacheKey(String cacheName,String key){
    return cacheName+"::"+key;
  }

  /**
   * 用法样例
   *
   * @author liukx
   * @date 2021/8/16
   */
  @Cacheable(cacheNames = CACHE_30_MINUTES, key = "'cacheExample'+#example")
  Object cacheExample(String example);
}
