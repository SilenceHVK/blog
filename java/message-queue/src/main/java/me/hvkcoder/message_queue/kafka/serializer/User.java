package me.hvkcoder.message_queue.kafka.serializer;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author h_vk
 * @since 2021/8/5
 */
@Data
@AllArgsConstructor
public class User implements Serializable {
	private int id;
	private String name;
}
