package reflection;

public class Test {
	public static void main(String[] args) {
		String name = "dog";
		Animal animal = null;
		if ("dog".equals(name)) {
			animal = new Dog();
		} else {
			animal = new Cat();
		}
		
		/**
		 * 
		 * Âß¼­ 
		 * 
		 */
		
		animal.eat();
		
		Dog dogson = null;
		Cat catson = null;
		if ("dog".equals(name)) {
			dogson = (Dog) animal;
		} else {
			catson = (Cat) animal;
		}
		
		if (null != dogson) {
			dogson.eat();
		} else {
			catson.eat();
		}
	}
}
