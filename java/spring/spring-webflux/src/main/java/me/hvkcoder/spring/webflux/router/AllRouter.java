package me.hvkcoder.spring.webflux.router;

import me.hvkcoder.spring.webflux.handler.UserHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.*;

/**
 * RouterFunction 添加路由
 *
 * @author h_vk
 * @since 2022/1/28
 */
@Configuration
public class AllRouter {

	/**
	 * 设置 Handler 路由
	 *
	 * @param userHandler
	 * @return
	 */
	@Bean
	public RouterFunction<ServerResponse> userRouter(UserHandler userHandler) {
		return RouterFunctions.nest(
			RequestPredicates.path("/user/router/function"), // 设置路由前缀
			RouterFunctions.route(RequestPredicates.GET("/all"), userHandler::getAllUser)// 设置获取全部用户信息路由
				// 设置删除用户信息路由
				.andRoute(RequestPredicates.DELETE("/delete/{id}"), userHandler::deleteUser)
				// 设置创建用户信息路由
				.andRoute(RequestPredicates.POST("/create").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), userHandler::createUser)
				// 设置修改用户信息路由
				.andRoute(RequestPredicates.PUT("/update/{id}").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), userHandler::updateUser)
		);
	}
}

