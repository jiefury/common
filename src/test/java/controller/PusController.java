package controller;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

//@RunWith(SpringRunner.class)
@SpringBootTest
public class PusController {

    @Autowired
    RedisTemplate redisTemplate;

    @Test
    public void publish() {
        // 发布消息
        redisTemplate.convertAndSend("dog", "wangwang");
        redisTemplate.convertAndSend("cat", "miaomiao");
    }
}
