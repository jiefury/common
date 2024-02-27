
package com.twin.demos.web;

import com.twin.config.RedisStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/base")
public class BasicController {

    @Autowired
    @Lazy
    private StringRedisTemplate redisTemplate;

    @Autowired
    private RedisStream redisStream;

    @RequestMapping("/publish")
    public String publish() {
        // 发布消息
        redisTemplate.convertAndSend("test", "xxx001");
        redisTemplate.convertAndSend("test2", "aaa");
        redisTemplate.convertAndSend("test3", "cccc");
        Map<String, Object> map = new HashMap<>();
        map.put("aaa", "12321");
        String test001 = redisStream.add("test001", map);
        System.out.println(test001);
        return "success";
    }

    public static void main(String[] args) {
        int a = 1;
        int x = 2;
        int y = 3;
        boolean b = x == y ? false : true;
        System.out.println(b);
    }
}
