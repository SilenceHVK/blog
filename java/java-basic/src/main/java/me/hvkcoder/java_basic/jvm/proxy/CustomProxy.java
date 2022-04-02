package me.hvkcoder.java_basic.jvm.proxy;


import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.Arrays;

/**
 * @author h_vk
 * @since 2022/4/2
 */
public class CustomProxy {
	private static final String ProxyName = "$Proxy0";
	private static final String ln = "\r\n";

	/**
	 * 实例化代理对象
	 *
	 * @param classLoader
	 * @param interfaces
	 * @param h
	 * @return
	 */
	public static Object newInstanceProxy(CustomClassLoader classLoader, Class<?>[] interfaces, CustomInvocationHandler h) {
		// 生成代理类源码
		File classFile = generatorProxyJava(interfaces);

		// 将 java 文件编译成 字节码
		try {
			JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
			StandardJavaFileManager manager = compiler.getStandardFileManager(null, null, null);
			Iterable<? extends JavaFileObject> fileObjects = manager.getJavaFileObjects(classFile);
			JavaCompiler.CompilationTask task = compiler.getTask(null, manager, null, null, null, fileObjects);
			task.call();
			manager.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			classFile.delete();
		}

		// 加载代理类的字节码
		try {
			return classLoader.findClass(ProxyName).getConstructor(CustomInvocationHandler.class).newInstance(h);
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 生成代理类源码
	 *
	 * @param interfaces
	 * @return
	 */
	private static File generatorProxyJava(Class<?>[] interfaces) {
		File classFile = new File(CustomProxy.class.getResource("").getPath() + ProxyName + ".java");
		StringBuffer sb = new StringBuffer();
		sb.append("package me.hvkcoder.java_basic.jvm.proxy;" + ln)
			.append("import java.lang.reflect.Method;" + ln)
			.append("import java.lang.reflect.Parameter;" + ln)
			.append("public final class $Proxy0 implements ");

		// 获取接口类名称
		for (int i = 0; i < interfaces.length; i++) {
			sb.append(interfaces[i].getName());
			if (i != interfaces.length - 1) {
				sb.append(", ");
			} else {
				sb.append(" { " + ln);
			}
		}
		sb.append("  private final CustomInvocationHandler h;" + ln)
			.append("  public $Proxy0(CustomInvocationHandler h) { " + ln)
			.append("    this.h = h;" + ln)
			.append("  }" + ln)
		;

		// 生成接口中的方法
		Arrays.stream(interfaces).forEach(aClass -> {
			Arrays.stream(aClass.getMethods()).forEach(method -> {
				String returnType = method.getReturnType().getName();
				sb.append("  @Override" + ln)
					.append(String.format("  public %s %s(", method.getReturnType().getName(), method.getName()));

				// 设置方法中的参数
				Parameter[] parameters = method.getParameters();
				StringBuilder parametersType = new StringBuilder();
				StringBuilder parametersName = new StringBuilder();

				for (int i = 0; i < parameters.length; i++) {
					Parameter parameter = parameters[i];
					parametersType.append(parameter.getType().getName()).append(".class");
					parametersName.append(parameter.getName());
					sb.append(parameter);
					if (i != parameters.length - 1) {
						sb.append(", ");
						parametersType.append(", ");
						parametersName.append(", ");
					}
				}
				sb.append(") {" + ln)
					.append("    try {" + ln)
					.append(String.format("      Method m = %s.class.getMethod(\"%s\"%s);" + ln, aClass.getName(), method.getName(), parameters.length > 0 ? ", " + parametersType : ""))
					.append(parameters.length > 0 ? "      Object[] args = new Object[]{ " + parametersName + " };" + ln : "")
					.append(String.format("      %sthis.h.invoke(this, m, %s);" + ln, "void".equals(returnType) ? "" : "return (" + returnType + ")", parameters.length > 0 ? "args" : "(Object[])null"))
					.append("    } catch(Throwable e){" + ln)
					.append("       e.printStackTrace();" + ln)
					.append("    }" + ln);
				if (!"void".equals(returnType)) {
					sb.append("    return null;" + ln);
				}
				sb.append("  }" + ln);
			});
		});
		sb.append("}");

		// 生成 .java 文件
		try (FileWriter fileWriter = new FileWriter(classFile)) {
			fileWriter.write(sb.toString());
			fileWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return classFile;
	}
}
