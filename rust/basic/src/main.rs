use std::io; // 引入标准库
use std::cmp::Ordering;
use rand::Rng; // trait

fn main() {
	// guess_the_number();
	// let result = usage_control_flow(20);
	// println!("{}", result)

	usage_loop();
}

// 定义一个元组
fn defined_tuple() {
	let tuple: (u32, f64) = (10, 3.14);
	// 使用解构解析元组内的元素
	let (x, y) = tuple;
	println!("x: {}, y: {}", x, y);
}

// 定义一个数组
fn defined_array() {
	// 声明数组 [类型;长度]
	let array = [1, 2, 3, 4, 5];
}

// 实现一个猜数字
fn guess_the_number() {
	// 生成随机数
	let secret_number = rand::thread_rng().gen_range(1..100);
	println!("生成的数字 => {}", secret_number);

	loop {
		println!("请输入一个数字：");
		// 声明一个变量，rust 中所有变量都是不可变的，只有增加了 mut 关键字才能使其可变
		let mut number = String::new();
		io::stdin().read_line(&mut number).expect("无法读取内容");

		// 对输入的内容进行类型转换，显式声明变量类型
		let number:u32 = match number.trim().parse() {
			Ok(number) => number,
			Err(_) => continue
		};

		// 对比对值进行枚举匹配
		match number.cmp(&secret_number) {
			Ordering::Less => println!("猜小了"),
			Ordering::Greater => println!("猜大了"),
			Ordering::Equal => {
				println!("猜对了，游戏结束");
				break;
			}
		}

	}
}


// 定义一个带参数、带返回值的函数
fn defined_function(a: u32, b:u32) -> u32 {
	return a + b;
}

// 控制流使用
fn usage_control_flow(age: u32) -> bool {
	if age >= 18 {
		println!("年龄通过");
		return true;
	} else {
		println!("年龄必须 >= 18");
		return false;
	}
}

// 循环的使用
fn usage_loop() {

	// loop 关键字告诉 Rust 反复执行一块代码，直到你喊停
	let mut counter = 0;
	loop {
		if counter >= 10 {
			println!("loop 循环结束，counter = {}", counter);
			break;
		}
		counter += 1;
	}

	// while 条件循环，每次执行循环时都进行条件判断
	let mut counter = 0;
	while counter < 10 {
		counter += 1;
	}
	println!("while 循环结束，counter = {}", counter);

	// for 循环变量数组
	let languages = ["Java", "Golang", "Python", "Rust"];
	for language in languages.iter()  {
		println!("{}", language);
	}

	// Range 指定开始与结束
	for number in (1..4).rev() {
		println!("the number {}", number);
	}

}
