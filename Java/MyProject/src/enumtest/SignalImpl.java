package enumtest;

public class SignalImpl {
	public static void main(String[] args) {
		//foreach();
		
		//compare(Sex.valueOf("MALE"));
		
		ordinal();
	}

	private static void ordinal() {
		for (int i = 0; i < Signal.values().length; i++) {
			System.out.println("����" + Signal.values()[i].ordinal() + "��ֵ��" + Signal.values()[i]);
		}
	}

	private static void foreach() {
		for (int i = 0; i < Signal.values().length; i++) {
			System.out.println("ö�ٳ�Ա��" + Signal.values()[i]);
		}
	}

	private static void compare(Sex s) {
		for(int i = 0; i < Sex.values().length; i++) {
			System.out.println(s + "��" + Sex.values()[i] + "�ıȽϽ���ǣ�" + s.compareTo(Sex.values()[i]));
		}
	}
}
