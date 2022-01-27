package me.hvkcoder.spring.webflux.repository;

import me.hvkcoder.spring.webflux.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author h_vk
 * @since 2022/1/27
 */
@Repository
public interface UserRepository extends ReactiveMongoRepository<User, String> {
}
