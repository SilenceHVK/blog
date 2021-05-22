package me.hvkcoder.spring_security.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author h-vk
 * @since 2021/5/21
 */
@Data
@Entity
public class UserInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private String userId;
	
	private String userName;

	private String encryptedPassword;

	private String role;
}
