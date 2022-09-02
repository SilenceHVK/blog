package me.hvkcoder.spring.practice.test;

import me.hvkcoder.spring.practice.PracticeApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author h_vk
 * @since 2022/9/2
 */
@SpringBootTest(classes = {PracticeApplication.class})
public class RedisTemplateTest {
	@Autowired
	private RedisTemplate redisTemplate;

	@Test
	public void testRedisStrings() throws Exception {
		System.out.println(redisTemplate.opsForValue().get("name"));
	}
}
