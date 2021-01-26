package me.hvkcoder.vertx.simple;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Vertx 实现 Http服务
 *
 * <p>在 Vert.x 中一切皆是 Verticle，每个 Verticle 都是独立的单元，每个 Verticle 都需要通过 deployVerticle 方法部署
 *
 * @author h-vk
 * @since 2020/12/9
 */
public class SimpleHttpServerVerticle extends AbstractVerticle {

	public static void main(String[] args) {
		Vertx.vertx().deployVerticle(new SimpleHttpServerVerticle());
	}

	@Override
	public void start() throws Exception {
		HttpServer server = vertx.createHttpServer();

		// 监听所有 http 请求的路由
		//		server.requestHandler(
		//			request -> {
		//				HttpServerResponse response = request.response();
		//				response.putHeader("content-type", "text/plain");
		//				response.end("Hello World");
		//			});

		// 路由配置
		Router router = Router.router(vertx);
		// 添加 Body 解析中间件
		router.route().handler(BodyHandler.create());
		// 添加 静态资源
		router.route().handler(StaticHandler.create());

		router.route("/login").handler(this::login);
		router.post("/user/add").handler(this::addUser);
		router.delete("/user/delete").handler(this::deleteUser);
		router.get("/user/get/:id").handler(this::getUserById);
		router.route("/event/stream").handler(this::eventStream);

		server.requestHandler(router);
		server.listen(3000);
	}

	private void login(RoutingContext ctx) {

		out(ctx, "Login" + ctx.request().getParam("username"));
	}

	private void eventStream(RoutingContext ctx) {
		ctx.response()
			.putHeader("Content-Type", "text/event-stream;charset=utf-8")
			.end("data:" + Math.random() + " \n\n");
	}

	private void addUser(RoutingContext ctx) {
		Map<String, Object> result = new HashMap<>();
		result.put("code", 0);
		result.put("msg", "success");
		result.put("data", ctx.getBodyAsJson());
		out(ctx, Json.encodePrettily(result));
	}

	private void deleteUser(RoutingContext ctx) {
		out(ctx, "DeleteUser");
	}

	private void getUserById(RoutingContext ctx) {
		out(ctx, ctx.pathParam("id"));
	}

	private void out(RoutingContext ctx, String msg) {
		ctx.response().putHeader("Content-Type", "text/plain; charset=utf-8").end(msg);
	}
}
