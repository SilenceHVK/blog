package me.hvkcoder.spring.custom;

import me.hvkcoder.spring.custom.annotation.Autowired;
import me.hvkcoder.spring.custom.annotation.Component;
import me.hvkcoder.spring.custom.annotation.ComponentScan;
import me.hvkcoder.spring.custom.annotation.Scope;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author h_vk
 * @since 2022/3/26
 */
public class AnnotationConfigApplicationContext implements ApplicationContext {

	/**
	 * 存储完整的单例 Bean
	 */
	private ConcurrentHashMap<String, Object> singletonObjects = new ConcurrentHashMap<>();

	/**
	 * 存储 BeanDefinition
	 */
	private ConcurrentHashMap<String, BeanDefinition> beanDefinitionMap  = new ConcurrentHashMap<>();

	/**
	 * 存储半成品 Bean
	 */
	private ConcurrentHashMap<String, Object> earlySingletonObjects = new ConcurrentHashMap<>();

  public AnnotationConfigApplicationContext(Class<?> configClass) {
		scan(configClass);

		// 完成 Bean 对象实例化
		beanDefinitionMap.forEach((beanName, beanDefinition) -> {
			if (BeanDefinition.SCOPE_SINGLETON.equals(beanDefinition.getScope())){
				Object bean = createBean(beanDefinition);
				// 将实例化后的 bean 放入半成品 bean 缓存中
				earlySingletonObjects.put(beanName, bean);
			}
		});

		// 完成 Bean 的属性填充
		earlySingletonObjects.forEach((beanName, beanInstance) -> {
			Class<?> beanClass = beanInstance.getClass();
			Arrays.stream(beanClass.getDeclaredFields()).forEach(field -> {
				if (field.isAnnotationPresent(Autowired.class)){
					Class<?> fieldType = field.getType();
					try {
						// 获取完整的 Bean 对象，如果没有完整的 Bean 对象则从 半成品 Bean 缓存中取 Bean
						Object bean = getBean(fieldType.getSimpleName());
						if (bean == null){
							bean = earlySingletonObjects.get(fieldType.getSimpleName());
						}
						field.setAccessible(true);
						field.set(beanInstance, bean);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			earlySingletonObjects.remove(beanName);
			singletonObjects.put(beanName, beanInstance);
		});
	}

	/**
	 * 扫描配置类中指定的注解，解析为 BeanDefinition
	 * @param configClass
	 */
	private void scan(Class<?> configClass) {
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
						Component component = aClass.getDeclaredAnnotation(Component.class);
						String beanName = "".equals(component.value()) ? aClass.getSimpleName() : component.value();

						// 将解析的 Class 文件信息存放到 BeanDefinition 中
						BeanDefinition beanDefinition = new BeanDefinition();
						beanDefinition.setBeanClass(aClass);

						// 判断加载类文件中是否有 Scope 注解，如果没有则默认为 单例 Bean
						if (aClass.isAnnotationPresent(Scope.class)){
							// 设置注解上的 Bean 的作用域
							Scope scope = aClass.getDeclaredAnnotation(Scope.class);
							beanDefinition.setScope(scope.value());
						}else {
							beanDefinition.setScope(BeanDefinition.SCOPE_SINGLETON);
						}
						// 将 BeanDefinition 对象存放到 beanDefinitionMap 缓存中
						beanDefinitionMap.put(beanName, beanDefinition);
					}
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * 创建 Bean 对象
	 * @param beanDefinition
	 * @return
	 */
	private Object createBean(BeanDefinition beanDefinition){
		try {
			return beanDefinition.getBeanClass().getDeclaredConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			throw new RuntimeException("创建 Bean 对象失败");
		}
	}

	@Override
	public Object getBean(String beanName) throws Exception{
		if (beanDefinitionMap.containsKey(beanName)){
			// 判断指定 Bean 是否为单例 Bean
			BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
			if (BeanDefinition.SCOPE_SINGLETON.equals(beanDefinition.getScope())){
				return singletonObjects.get(beanName);
			}else {
				return createBean(beanDefinition);
			}
		}else{
			throw new Exception("未找到 "+ beanName+"  Bean");
		}
	}

	@Override
  public <T> T getBean(Class<T> beanType) throws Exception{
    return (T)getBean(beanType.getSimpleName());
  }
}
