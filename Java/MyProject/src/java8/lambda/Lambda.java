package java8.lambda;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Lambda表达式，也可称为闭包，它是推动java8发布的最重要新特性
 * Lambda允许把函数作为一个方法的参数（函数作为参数传递进方法中）
 * 使用Lambda表达式可以使代码变得更加简洁紧凑
 * 
 * 语法
 * Lambda表达式的语法格式如下：
 * (parameters) -> expression
 * 或者
 * (parameters) -> {statements;}
 * 
 * 以下是lambda表达式的重要特征
 * 可选类型声明：不需要声明参数类型，编译器可以统一识别参数值。
 * 可选的参数圆括号：一个参数无须定义圆括号，但多个参数需要定义圆括号
 * 可选大括号：如果主体包含了一个语句，就不需要使用大括号
 * 可选的返回关键字：如果主体只有一个表达式返回值则编译器会自动返回值，大括号需要指定表达式返回了一个数值
 * 
 * 示例：
 * 1.不需要参数，返回值为5
 * () -> 5;
 * 
 * 2.接收一个参数(数字类型)，返回其2倍的值
 * x -> 2 * x;
 * 
 * 3.接收2个参数(数字)，并返回他们的差值
 * (x, y) -> x - y;
 * 
 * 4.接收2个int型整数，返回他们的和
 * (int x, int y) -> x + y;
 * 
 * 5.接受一个String对象，并在控制台打印，不返回任何值（看起来像是返回void）
 * (String s) -> System.out.println(s);
 * 
 * 注意事项：
 * Lambda表达式主要用来定义行执行的方法类型接口，例如，一个简单方法接口。在下面例子中，我们使用各种类型的Lambda表达式来定义MathOperation接口的方法。
 * 然后我们定义了sayMessage的执行
 * 
 * Lambda表达式免去了使用匿名方法的麻烦，并且给予Java简单但是强大的函数化的编程能力
 * 
 */
public class Lambda {
	public static void main(String[] args) {
		//first();
		//second();
		//third();
		//runnable();
		comparator();
	}

	private static void first() {
		//类型声明
		MathOperation addOper = (int a, int b) -> a + b;
		//不声明类型
		MathOperation subOper = (a, b) -> a - b;
		
		//大括号中的返回语句
		MathOperation mulOper = (int a, int b) -> {  return a * b; };
		
		//没有大括号及返回语句
		MathOperation divOper = (int a, int b) -> a / b;
		
		System.out.println("10 + 5 = " + operate(10, 5, addOper));
		System.out.println("10 - 5 = " + operate(10, 5, subOper));
		System.out.println("10 * 5 = " + operate(10, 5, mulOper));
		System.out.println("10 / 5 = " + operate(10, 5, divOper));
		
		//不用括号
		GreetingSerive greet1 = (msg) -> System.out.println("Hello " + msg);
		GreetingSerive greet2 = (msg) -> {
			System.out.println("Hello " + msg);
		};
		
		greet1.sayMessage("Runoob");
		greet2.sayMessage("Google");
	}
	
	/**
	 * Lambda表达式只能引用标记了final的外层局部变量，这就是说明不能在lambda内部修改定义在域外的局部变量，否则会编译错误
	 */
	private static void second() {
		String salutation = "Hello ";
		GreetingSerive greet = msg -> {
			System.out.println(salutation + msg);
			//salutation = "boy next door";
		};
		
		greet.sayMessage("Runoob");
	}
	
	/**
	 * 我们也可以直接在lambda表达式中访问外层的局部变量
	 * lambda表达式的局部变量可以不用声明为final，但是必须不可被后面的代码修改（即隐性的具有final的语义）
	 */
	private static void third() {
		final int num = 1;
		Converter<Integer, String> s = (param) -> {
			System.out.println(String.valueOf(param + num));
		};
		
		s.convert(2);
	}
	
	/**
	 * Lambda实现Runnable接口
	 */
	private static void runnable() {
		//1.1 使用匿名内部类
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				System.out.println("Hello, World！");
			}
		}).start();
		
		//1.2 使用lambda表达式
		new Thread(() -> System.out.println("Hello, Lambda！")).start();
		
		//2.1 使用匿名内部类
		Runnable race1 = new Runnable() {
			
			@Override
			public void run() {
				System.out.println("Hello, World！");
			}
		};
		
		//2.2使用lambda表达式
		Runnable race2 = () -> System.out.println("Hello, Lambda！");
		
		//直接调用run方法（没开新线程）
		race1.run();
		race2.run();
	}
	
	/**
	 * 使用Comparator类来排序集合
	 */
	private static void comparator() {
		String[] players = {"Rafael Nadal", "Novaj Djokovic", "Stanislas Wawrinka", "David Ferrer", "Andy Muray", "Tomas Berdych", "Juan Marin Del Potro", "Tichard Gasquet", "John Isner"};
		
		//使用匿名内部类根据name排序players
		Arrays.sort(players, new Comparator<String>() {
			@Override
			public int compare(String s1, String s2) {
				return s1.compareTo(s2);
			}
		});
		System.out.println(Arrays.toString(players));
		
		//使用lambda表达式
		Arrays.sort(players, (s1, s2) -> s2.compareTo(s1));
		System.out.println(Arrays.toString(players));
		
		Comparator<String> c = (s1, s2) -> s1.compareTo(s2);
		Arrays.sort(players, c);
		System.out.println(Arrays.toString(players));
	}
	
	private static int operate(int a, int b, MathOperation mOper) {
		return mOper.operation(a, b);
	}
}
