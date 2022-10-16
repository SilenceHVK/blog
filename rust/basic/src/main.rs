use std::io; // 引入标准库
use std::cmp::Ordering;
use rand::Rng; // trait

fn main() {
		// 生成随机数
		let secret_number = rand::thread_rng().gen_range(1..101);

		// 死循环
		loop {
			println!("请输入一个数字：");
			// 声明一个变量，rust 中所有变量都是不可变的，只有增加了 mut 关键字才能使其可变
			let mut number = String::new();
			io::stdin().read_line(&mut number).expect("无法读取内容");

			// 对输入的内容进行类型转换，显式声明变量类型
			let number :u32 = match number.trim().parse() {
				Ok(num) => num,
				Err(_) => continue,
			};

			// 匹配
			match number.cmp(&secret_number) {
				Ordering::Less => println!("小于随机数"),
				Ordering::Greater => println!("大于随机数"),
				Ordering::Equal => {
					println!("猜对了");
					break; // 跳出循环
				}
			}
		}
}
