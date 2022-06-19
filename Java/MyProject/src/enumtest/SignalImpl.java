package enumtest;

public class SignalImpl {
	public static void main(String[] args) {
		//foreach();
		
		//compare(Sex.valueOf("MALE"));
		
		ordinal();
	}

	private static void ordinal() {
		for (int i = 0; i < Signal.values().length; i++) {
			System.out.println("索引" + Signal.values()[i].ordinal() + "，值：" + Signal.values()[i]);
		}
	}

	private static void foreach() {
		for (int i = 0; i < Signal.values().length; i++) {
			System.out.println("枚举成员：" + Signal.values()[i]);
		}
	}

	private static void compare(Sex s) {
		for(int i = 0; i < Sex.values().length; i++) {
			System.out.println(s + "与" + Sex.values()[i] + "的比较结果是：" + s.compareTo(Sex.values()[i]));
		}
	}
}
