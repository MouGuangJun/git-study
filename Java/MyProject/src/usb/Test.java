package usb;

public class Test {
	public static void main(String[] args) {
		MyUsb myUsb = new MyUsb();
		myUsb.width(10, 12);
		
		Usb usb = new Usb() {
			
			@Override
			public void width(int a, int b) {
				System.out.println("����" + a + ",��" + b);				
			}
		};
		
		usb.width(20, 30);
		
		
		Usb usbL = (a, b) -> System.out.println("����" + a + ",��" + b);
		usbL.width(30, 40);
	}
}
