package reflection;

public class Cat extends Animal {
	public void eatjr() {
		System.out.println("Ã¨³ÔÓã£¡");
	}
	
	@Override
	public void eat() {
		System.out.println("Ã¨µÄ³Ô·¨£¡");
	}
}
