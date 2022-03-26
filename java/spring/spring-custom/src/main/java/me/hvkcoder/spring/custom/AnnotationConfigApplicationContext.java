package me.hvkcoder.spring.custom;

import me.hvkcoder.spring.custom.annotation.Component;
import me.hvkcoder.spring.custom.annotation.ComponentScan;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author h_vk
 * @since 2022/3/26
 */
public class AnnotationConfigApplicationContext implements ApplicationContext {

  public AnnotationConfigApplicationContext(Class<?> configClass) {
    if (!configClass.isAnnotationPresent(ComponentScan.class)) {
      throw new RuntimeException("未在配置类中找到 ComponentScan 注解");
    }

    // 获取配置类中的扫描路径
    ComponentScan componentScan = configClass.getDeclaredAnnotation(ComponentScan.class);
    final String scanPath = componentScan.value();

    // 通过 ClassLoader 获取当前程序指定扫描路径的绝对路径
    ClassLoader classLoader = AnnotationConfigApplicationContext.class.getClassLoader();
    URL url = classLoader.getResource(scanPath.replace(".", "/"));

    // 获取扫描路径下所有的类文件
    File files = new File(Objects.requireNonNull(url).getPath());
    Arrays.stream(Objects.requireNonNull(files.listFiles())).forEach(file -> {
			String className = file.getName();
			if (className.endsWith(".class")){
				try {
					// 加载类文件
					Class<?> aClass = classLoader.loadClass(scanPath + "." + className.substring(0, className.indexOf(".")));
					// 判断加载的类文件是否有 Component 注解
					if (aClass.isAnnotationPresent(Component.class)){
						System.out.println(aClass);
					}
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		});
  }

  @Override
  public <T> T getBean(Class<T> beanType) {
    return null;
  }
}
