package me.hvkcoder.custom.spring.boot.starter.callback;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author h_vk
 * @since 2024/3/28
 */
@Data
public class CallBackRegistry {
	private final List<CallBack> callbacks = new ArrayList<>();

	public void register(CallBack callback) {
		callbacks.add(callback);
	}
	
	public Boolean isEmpty() {
		return callbacks.isEmpty();
	}
}
