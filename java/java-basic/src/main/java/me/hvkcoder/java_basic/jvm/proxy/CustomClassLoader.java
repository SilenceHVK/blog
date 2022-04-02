package me.hvkcoder.java_basic.jvm.proxy;

import java.io.*;

/**
 * @author h_vk
 * @since 2022/4/2
 */
public class CustomClassLoader extends ClassLoader {

	private final File baseDir;

	public CustomClassLoader() {
		String path = CustomClassLoader.class.getResource("").getPath();
		this.baseDir = new File(path);
	}

	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		String className = CustomClassLoader.class.getPackageName() + "." + name;
		if (baseDir != null) {
			File classFile = new File(baseDir, name + ".class");
			if (classFile.exists()) {
				try (
					FileInputStream fileInputStream = new FileInputStream(classFile);
					ByteArrayOutputStream outputStream = new ByteArrayOutputStream()
				) {
					byte[] bytes = new byte[1024];
					int length;
					while ((length = fileInputStream.read(bytes)) > 0) {
						outputStream.write(bytes, 0, length);
					}
					return defineClass(className, outputStream.toByteArray(), 0, outputStream.size());
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					classFile.delete();
				}
			}
		}
		return super.findClass(name);
	}
}
