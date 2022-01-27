package me.hvkcoder.spring.webflux.controller;

import lombok.AllArgsConstructor;
import me.hvkcoder.spring.webflux.domain.User;
import me.hvkcoder.spring.webflux.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * WebFlux CURD 练习
 *
 * @author h_vk
 * @since 2022/1/27
 */
@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {

	private final UserRepository userRepository;

	/**
	 * 创建用户
	 *
	 * @param user
	 * @return
	 */
	@PostMapping("/post/create")
	public Mono<User> createUser(@RequestBody User user) {
		return userRepository.save(user);
	}

	/**
	 * 获取所有用户
	 *
	 * @return
	 */
	@GetMapping("/get/all")
	public Flux<User> getAll() {
		return userRepository.findAll();
	}

	/**
	 * 使用 SSE 的方式返回所有用户
	 *
	 * @return
	 */
	@GetMapping(value = "/get/stream/all", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<User> getStreamAll() {
		return userRepository.findAll();
	}

	/**
	 * 根据ID 删除用户信息
	 *
	 * @param id
	 * @return
	 */
	@DeleteMapping("/delete/{id}")
	public Mono<ResponseEntity<Void>> deleteUserById(@PathVariable String id) {
		return userRepository.findById(id)
			// flatMap 用于处理数据
			.flatMap(user -> userRepository.delete(user).then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK))))
			.defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	/**
	 * 根据ID 修改用户信息
	 *
	 * @param id
	 * @param user
	 * @return
	 */
	@PutMapping("/put/update/{id}")
	public Mono<ResponseEntity<User>> updateUserById(@PathVariable String id, @RequestBody User user) {
		return userRepository.findById(id)
			.flatMap(o -> userRepository.save(user).then(Mono.just(new ResponseEntity<>(user, HttpStatus.OK))))
			.defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	/**
	 * 根据ID 获取用户信息
	 *
	 * @param id
	 * @return
	 */
	@GetMapping("/get/{id}")
	public Mono<ResponseEntity<User>> getUserById(@PathVariable String id) {
		return userRepository.findById(id)
			.map(user -> new ResponseEntity<>(user, HttpStatus.OK))
			.defaultIfEmpty((new ResponseEntity<>(HttpStatus.NOT_FOUND)));
	}
}
