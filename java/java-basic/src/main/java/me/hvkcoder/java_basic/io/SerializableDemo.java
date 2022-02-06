package me.hvkcoder.java_basic.io;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.*;

/**
 * 序列化与反序列化
 *
 * @author h_vk
 * @since 2022/2/6
 */
@Slf4j
public class SerializableDemo {

	@Data
	@AllArgsConstructor
	private static class Student implements Serializable {
		private static final long serialVersionUID = -116040035351685563L;

		private String name;
		private Integer age;
		// transient 修改临时属性，不会参与序列化
		private transient String password;
	}

	public static void main(String[] args) {
		// 序列化对象
		try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("student"))) {
			Student student = new Student("hvkcoder", 18, "123456");
			outputStream.writeObject(student);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 反序列化
		try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("student"))) {
			Student student = (Student) inputStream.readObject();
			log.info("{}", student);
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
