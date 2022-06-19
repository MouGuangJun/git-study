package java8.predicate;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * Predicate接口只有一个参数，返回boolean类型。该接口包含多种默认方法来将Predicate组合成其他复杂的逻辑（比如：与，或，非）
 */
public class PredicateT {
	public static void main(String[] args) {
		Predicate<String> predicate = (s) -> s.length() > 0;
		System.out.println(predicate.test("foo"));
		
		// 非
		System.out.println(predicate.negate().test("foo"));
		
		Predicate<Boolean> nonNull = Objects :: nonNull;
		Predicate<Boolean> isNull = Objects :: isNull;
		
		Predicate<String> isEmpty = String :: isEmpty;
		Predicate<String> isNotEmpty = isEmpty.negate();
		
		// 或
		System.out.println(predicate.or(isEmpty).test("foo"));
		
		// 与
		System.out.println(predicate.and(isEmpty).test("foo"));
	}
}
