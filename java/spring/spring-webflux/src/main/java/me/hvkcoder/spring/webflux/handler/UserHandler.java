package me.hvkcoder.spring.webflux.handler;

import lombok.AllArgsConstructor;
import me.hvkcoder.spring.webflux.domain.User;
import me.hvkcoder.spring.webflux.repository.UserRepository;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * 使用 Handler Function 实现 CURD
 * ServerRequest <-> HttpServletRequest
 * ServerResponse <-> HttpServletResponse
 *
 * @author h_vk
 * @since 2022/1/28
 */
@Component
@AllArgsConstructor
public class UserHandler {

	private final UserRepository userRepository;

	/**
	 * 获取所有用户信息
	 *
	 * @param request
	 * @return
	 */
	public Mono<ServerResponse> getAllUser(ServerRequest request) {
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(userRepository.findAll(), User.class);
	}

	/**
	 * 创建用户信息
	 *
	 * @param request
	 * @return
	 */
	public Mono<ServerResponse> createUser(ServerRequest request) {
		// 通过 Request 获取用户信息
		Mono<User> userMono = request.bodyToMono(User.class);
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(userRepository.saveAll(userMono), User.class);
	}

	/**
	 * 根据ID 删除用户信息
	 *
	 * @param request
	 * @return
	 */
	public Mono<ServerResponse> deleteUser(ServerRequest request) {
		// 通过 Request 获取用户ID
		String id = request.pathVariable("id");
		return userRepository.findById(id)
			.flatMap(user -> userRepository.delete(user).then(ServerResponse.ok().build()))
			.switchIfEmpty(ServerResponse.notFound().build());
	}

	/**
	 * 根据ID 修改用户信息
	 *
	 * @param request
	 * @return
	 */
	public Mono<ServerResponse> updateUser(ServerRequest request) {
		// 在 Request 获取用户ID
		String id = request.pathVariable("id");
		// 通过 Request 获取用户信息
		Mono<User> userMono = request.bodyToMono(User.class);

		return userRepository.findById(id)
			.map(user -> userRepository.saveAll(userMono))
			.flatMap(user -> ServerResponse.ok().body(userMono, User.class))
			.switchIfEmpty(ServerResponse.notFound().build());
	}

}
