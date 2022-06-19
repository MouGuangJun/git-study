package java8.lambda;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Lambda���ʽ��Ҳ�ɳ�Ϊ�հ��������ƶ�java8����������Ҫ������
 * Lambda����Ѻ�����Ϊһ�������Ĳ�����������Ϊ�������ݽ������У�
 * ʹ��Lambda���ʽ����ʹ�����ø��Ӽ�����
 * 
 * �﷨
 * Lambda���ʽ���﷨��ʽ���£�
 * (parameters) -> expression
 * ����
 * (parameters) -> {statements;}
 * 
 * ������lambda���ʽ����Ҫ����
 * ��ѡ��������������Ҫ�����������ͣ�����������ͳһʶ�����ֵ��
 * ��ѡ�Ĳ���Բ���ţ�һ���������붨��Բ���ţ������������Ҫ����Բ����
 * ��ѡ�����ţ�������������һ����䣬�Ͳ���Ҫʹ�ô�����
 * ��ѡ�ķ��عؼ��֣��������ֻ��һ�����ʽ����ֵ����������Զ�����ֵ����������Ҫָ�����ʽ������һ����ֵ
 * 
 * ʾ����
 * 1.����Ҫ����������ֵΪ5
 * () -> 5;
 * 
 * 2.����һ������(��������)��������2����ֵ
 * x -> 2 * x;
 * 
 * 3.����2������(����)�����������ǵĲ�ֵ
 * (x, y) -> x - y;
 * 
 * 4.����2��int���������������ǵĺ�
 * (int x, int y) -> x + y;
 * 
 * 5.����һ��String���󣬲��ڿ���̨��ӡ���������κ�ֵ�����������Ƿ���void��
 * (String s) -> System.out.println(s);
 * 
 * ע�����
 * Lambda���ʽ��Ҫ����������ִ�еķ������ͽӿڣ����磬һ���򵥷����ӿڡ������������У�����ʹ�ø������͵�Lambda���ʽ������MathOperation�ӿڵķ�����
 * Ȼ�����Ƕ�����sayMessage��ִ��
 * 
 * Lambda���ʽ��ȥ��ʹ�������������鷳�����Ҹ���Java�򵥵���ǿ��ĺ������ı������
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
		//��������
		MathOperation addOper = (int a, int b) -> a + b;
		//����������
		MathOperation subOper = (a, b) -> a - b;
		
		//�������еķ������
		MathOperation mulOper = (int a, int b) -> {  return a * b; };
		
		//û�д����ż��������
		MathOperation divOper = (int a, int b) -> a / b;
		
		System.out.println("10 + 5 = " + operate(10, 5, addOper));
		System.out.println("10 - 5 = " + operate(10, 5, subOper));
		System.out.println("10 * 5 = " + operate(10, 5, mulOper));
		System.out.println("10 / 5 = " + operate(10, 5, divOper));
		
		//��������
		GreetingSerive greet1 = (msg) -> System.out.println("Hello " + msg);
		GreetingSerive greet2 = (msg) -> {
			System.out.println("Hello " + msg);
		};
		
		greet1.sayMessage("Runoob");
		greet2.sayMessage("Google");
	}
	
	/**
	 * Lambda���ʽֻ�����ñ����final�����ֲ������������˵��������lambda�ڲ��޸Ķ���������ľֲ������������������
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
	 * ����Ҳ����ֱ����lambda���ʽ�з������ľֲ�����
	 * lambda���ʽ�ľֲ��������Բ�������Ϊfinal�����Ǳ��벻�ɱ�����Ĵ����޸ģ������Եľ���final�����壩
	 */
	private static void third() {
		final int num = 1;
		Converter<Integer, String> s = (param) -> {
			System.out.println(String.valueOf(param + num));
		};
		
		s.convert(2);
	}
	
	/**
	 * Lambdaʵ��Runnable�ӿ�
	 */
	private static void runnable() {
		//1.1 ʹ�������ڲ���
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				System.out.println("Hello, World��");
			}
		}).start();
		
		//1.2 ʹ��lambda���ʽ
		new Thread(() -> System.out.println("Hello, Lambda��")).start();
		
		//2.1 ʹ�������ڲ���
		Runnable race1 = new Runnable() {
			
			@Override
			public void run() {
				System.out.println("Hello, World��");
			}
		};
		
		//2.2ʹ��lambda���ʽ
		Runnable race2 = () -> System.out.println("Hello, Lambda��");
		
		//ֱ�ӵ���run������û�����̣߳�
		race1.run();
		race2.run();
	}
	
	/**
	 * ʹ��Comparator�������򼯺�
	 */
	private static void comparator() {
		String[] players = {"Rafael Nadal", "Novaj Djokovic", "Stanislas Wawrinka", "David Ferrer", "Andy Muray", "Tomas Berdych", "Juan Marin Del Potro", "Tichard Gasquet", "John Isner"};
		
		//ʹ�������ڲ������name����players
		Arrays.sort(players, new Comparator<String>() {
			@Override
			public int compare(String s1, String s2) {
				return s1.compareTo(s2);
			}
		});
		System.out.println(Arrays.toString(players));
		
		//ʹ��lambda���ʽ
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
