package database;

public class Syso {
	public static void main(String[] args) {
		while (true) {
			try {
				Thread.sleep(3000);
				System.out.println("1");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
