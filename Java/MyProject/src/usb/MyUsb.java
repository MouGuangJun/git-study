package usb;

public class MyUsb implements Usb{

	@Override
	public void width(int a, int b) {
		System.out.println("����" + a + ",��" + b);
	}

}
