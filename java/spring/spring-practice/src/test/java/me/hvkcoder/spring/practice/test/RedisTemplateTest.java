package me.hvkcoder.spring.practice.test;

import lombok.extern.slf4j.Slf4j;
import me.hvkcoder.spring.practice.PracticeApplication;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

import java.util.Arrays;
import java.util.List;

/**
 * @author h_vk
 * @since 2022/9/2
 */
@Slf4j
@SpringBootTest(classes = {PracticeApplication.class})
public class RedisTemplateTest {
	@Autowired
	private RedisTemplate redisTemplate;

	@Test
	public void testRedisStrings() throws Exception {
		System.out.println(redisTemplate.opsForValue().get("name"));
	}

	/**
	 * Redis 事务操作
	 *
	 * @throws Exception
	 */
	@Test
	public void testRedisTransaction() throws Exception {
		Object result = redisTemplate.execute(new SessionCallback<String>() {
			String inventoryKey = "inventory";

			@Override
			public String execute(RedisOperations operations) throws DataAccessException {
				// 监视指定 库存 Key 的变化
				operations.watch(inventoryKey);

				// 判断库存是否为空
				Object inventory = operations.opsForValue().get(inventoryKey);
				if (inventory == null || Integer.parseInt(inventory.toString()) <= 0) {
					return "秒杀商品已结束";
				}

				// 开启 Redis 事务
				redisTemplate.multi();
				redisTemplate.opsForValue().decrement(inventoryKey);
				// 执行 Redis 事务
				List result = redisTemplate.exec();
				return result.size() == 0 ? "秒杀商品失败" : "秒杀商品成功";
			}
		});
		log.info("{}", result);
	}

	/**
	 * Redis 执行 Lua 脚本
	 *
	 * @throws Exception
	 */
	@Test
	public void testRedisScriptLua() throws Exception {
		DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
		// 设置脚本返回类型
		redisScript.setResultType(Long.class);
		// 加载 Lua 脚本
		redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("redis/seckill.lua")));
		// 执行 Lua 脚本  // KEYS, ARGV
		Long result = (Long) redisTemplate.execute(redisScript, List.of("inventory"));
		log.info("-----{}", result > 0 ? "商品秒杀成功" : "商品秒杀失败");

	}
}
