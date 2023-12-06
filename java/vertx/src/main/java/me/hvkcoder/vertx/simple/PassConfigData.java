package me.hvkcoder.vertx.simple;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;

/**
 * 传递配置数据
 *
 * @author h_vk
 * @since 2023/12/4
 */
@Slf4j
public class PassConfigData extends AbstractVerticle {
	@Override
	public void start() throws Exception {
		log.info("n => {}", config().getInteger("n", -1));
	}

	public static void main(String[] args) {
		Vertx vertx = Vertx.vertx();
		for (int i = 0; i < 4; i++) {
			JsonObject json = new JsonObject().put("n", i);
			DeploymentOptions options = new DeploymentOptions()
				.setConfig(json)
				.setInstances(1);
			vertx.deployVerticle("me.hvkcoder.vertx.simple.PassConfigData", options);
		}
	}
}
