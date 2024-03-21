package me.hvkcoder.java_basic.docker;


import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.PullImageResultCallback;
import com.github.dockerjava.api.model.*;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author h_vk
 * @since 2024/3/20
 */
@Slf4j
public class DockerJava {
	private DockerClient dockerClient;

	@Before
	public void initDockerClient() {
		DefaultDockerClientConfig clientConfig = DefaultDockerClientConfig.createDefaultConfigBuilder().withDockerHost("tcp://master.cluster.local:2375").build();
		dockerClient = DockerClientBuilder.getInstance(clientConfig).build();
	}

	/**
	 * docker image list
	 */
	@Test
	public void listImage() {
		dockerClient.listImagesCmd().exec().forEach(image -> log.info("imageName: {}", (Object) image.getRepoTags()));
	}

	/**
	 * docker run
	 */
	@Test
	public void runContainer() {

		// 端口映射
		Map<Integer, Integer> portMap = new HashMap<>();
		portMap.put(80, 8080);

		List<PortBinding> portBindingList = new ArrayList<>();
		List<ExposedPort> exposedPortList = new ArrayList<>();

		portMap.forEach((exposedPort, bindPort) -> {
			ExposedPort tcp = ExposedPort.tcp(exposedPort);
			Ports.Binding binding = Ports.Binding.bindPort(bindPort);
			PortBinding ports = new PortBinding(binding, tcp);
			portBindingList.add(ports);
			exposedPortList.add(tcp);
		});

		// 创建容器配置
		CreateContainerResponse response = dockerClient.createContainerCmd("nginx:latest")
			.withExposedPorts(exposedPortList)
			.withPortBindings(portBindingList)
			.withName("my-container-test")
			.exec();

		if (StringUtils.isNotEmpty(response.getId())) {
			// 启动容器
			dockerClient.startContainerCmd(response.getId()).exec();
			log.info("容器已启动，容器ID：{}", response.getId());
		}
	}

	/**
	 * docker ps -a
	 */
	@Test
	public void listContainer() {
		dockerClient.listContainersCmd().withShowAll(true).exec()
			.forEach(container -> log.info("containerId:{}, containerName: {}, containerState: {}", container.getId(), (Object) container.getNames(), container.getState()));
	}

	/**
	 * docker pause
	 */
	@Test
	public void pauseContainer() {
		dockerClient.pauseContainerCmd("3d22e8ab9e85aa4c5fd203624f0d89b1f83a4f702945e360a3066414e0657c87").exec();
	}

	/**
	 * docker stop
	 */
	@Test
	public void stopContainer() {
		dockerClient.stopContainerCmd("3d22e8ab9e85aa4c5fd203624f0d89b1f83a4f702945e360a3066414e0657c87").exec();
	}

	/**
	 * docker rm
	 */
	@Test
	public void deleteContainer() {
		dockerClient.removeContainerCmd("3d22e8ab9e85aa4c5fd203624f0d89b1f83a4f702945e360a3066414e0657c87").exec();
	}

	/**
	 * docker pull
	 */
	@Test
	public void pullImage() {
		dockerClient.pullImageCmd("nginx:latest").exec(new PullImageResultCallback() {
			@Override
			public void onComplete() {
				log.info("完成镜像拉取");
			}
		});
	}

	@After
	public void closeDockerClient() throws IOException {
		if (dockerClient != null) {
			dockerClient.close();
		}
	}
}
