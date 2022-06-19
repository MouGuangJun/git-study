package enumtest;

public class TrafficLight {
	public static void main(String[] args) {
		Signal color = Signal.RED;
		change(color);
	}

	private static void change(Signal color) {
		switch(color) {
			case RED:
				color=Signal.GREEN;
				break;
			case YELLOW:
				color=Signal.RED;
				break;
			case GREEN:
				color=Signal.YELLOW;
				break;
		}
		
		System.out.println(color);
	}
}
