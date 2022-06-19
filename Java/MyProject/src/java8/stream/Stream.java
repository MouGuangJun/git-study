package java8.stream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Stream {
	public static void main(String[] args) {
		List<Student> students = init();
		List<Integer> numbers = initNum();
		
		/************************* ���� start *************************/
		/*
		 * ����
		 * ���ˣ�����˼����ǰ��ո�����Ҫ��Լ��Ͻ���ɸѡ����������Ԫ�أ�java8�ṩ�ĵ�ɸѡ����������filter��distinct��limit��skip.
		 */
		//filter(students);
		//distinct(numbers);
		//limit(students);
		//skip(students);
		/************************* ���� end *************************/
		
		/************************* ӳ�� start *************************/
		/*
		 * sql�У�������select���������Ҫ���ֶ�
		 */
		//map(students);
		//flatMap();
		/************************* ӳ�� end *************************/
		
		/************************ �ն˲��� start ************************/
		/************************* ���� start *************************/
		//allMatch(students);
		//anyMatch(students);
		//noneMatch(students);
		//findFirst(students);
		//findAny(students);
		/************************* ���� end *************************/
		
		/************************* ��Լ start *************************/
		//reduce(students);
		//count(students);
		
		/************************* ��Լ end *************************/
		
		/************************* �ռ� start *************************/
		//toList(students);
		//toMap(students);
		//toSet(students);
		
		/************************* �ռ� end *************************/
		
		/************************* ���� start *************************/
		//group(students);
		/************************* ���� end *************************/
		
		/************************* ���� start *************************/
		//partition(students, numbers);
		/************************* ���� end *************************/
		
		/************************* ���� start *************************/
		//forEach(students);
		//maxMin(students);
		counting(students);
		/************************* ���� end *************************/
		
		/************************ �ն˲��� end ************************/
	}
	
	private static List<Student> init() {
		// ��ʼ��
		List<Student> students = new ArrayList<Student>() {
			private static final long serialVersionUID = -6262796848539237179L;

			{
				add(new Student(20160001, "����", 20, 1, "��ľ����", "�人��ѧ"));
				add(new Student(20160002, "��Լ", 21, 2, "��Ϣ��ȫ", "�人��ѧ"));
				add(new Student(20160003, "����", 22, 3, "���ù���", "�人��ѧ"));
				add(new Student(20160004, "�Ƴ�", 21, 2, "��Ϣ��ȫ", "�人��ѧ"));
				add(new Student(20161001, "���", 21, 2, "��е���Զ���", "���пƼ���ѧ"));
				add(new Student(20161002, "Ԫֱ", 23, 4, "��ľ����", "���пƼ���ѧ"));
				add(new Student(20161003, "��Т", 23, 4, "�������ѧ", "���пƼ���ѧ"));
				add(new Student(20162001, "��ı", 22, 3, "��ľ����", "�㽭��ѧ"));
				add(new Student(20162002, "³��", 23, 4, "�������ѧ", "�㽭��ѧ"));
				add(new Student(20163001, "����", 24, 5, "��ľ����", "�Ͼ���ѧ"));
			}
		};

		return students;
	}

	private static List<Integer> initNum() {
		// ��ʼ��
		List<Integer> students = new ArrayList<Integer>() {

			private static final long serialVersionUID = 4591633775871834220L;

			{
				add(20);
				add(21);
				add(22);
				add(21);
				add(21);
				add(23);
				add(23);
				add(22);
				add(23);
				add(24);
			}
		};
		
		return students;
	}

	/**
	 * filter
	 */
	private static void filter(List<Student> students) {
		List<Student> aStudents = students.stream().filter(s -> "�人��ѧ".equals(s.getSchool())).collect(Collectors.toList());
		System.out.println(aStudents);
	}
	
	/**
	 * distinct
	 */
	private static void distinct(List<Integer> numbers) {
		//���������ȥ�صĲ���
		List<Integer> number = numbers.stream().distinct().collect(Collectors.toList());
		System.out.println(number);
		
		//��ȡ�����е�ż����������ȥ�ز���
		number = numbers.stream().filter(n -> n % 2 == 0).distinct().collect(Collectors.toList());
		System.out.println(number);
	}
	
	/**
	 * limit ���ذ���ǰn��Ԫ�ص����������ϳ���С��nʱ������ʵ�ʳ���
	 */
	private static void limit(List<Student> students) {
		List<Student> aStudents = students.stream().filter(s -> "��ľ����".equals(s.getMajor())).limit(2).collect(Collectors.toList());
		System.out.println(aStudents);
		
		//ɸѡ��רҵΪ��ľ���̵�ѧ�������������С��������ɸѡ��������С������ѧ��
		aStudents = students.stream().filter(s -> "��ľ����".equals(s.getMajor())).sorted((s1, s2) -> s1.getAge() - s2.getAge()).limit(2).collect(Collectors.toList());
		System.out.println(aStudents);
	}
	
	/**
	 * skip ����ǰn��Ԫ�أ���limit�÷��෴
	 */
	private static void skip(List<Student> students) {
		List<Student> aStudents = students.stream().filter(s -> "��ľ����".equals(s.getMajor())).skip(2).collect(Collectors.toList());
		System.out.println(aStudents);
		//ɸѡ��רҵΪ��ľ���̵�ѧ�������������С��������ɸѡ��������С������ѧ��
		aStudents = students.stream().filter(s -> "��ľ����".equals(s.getMajor())).sorted((s1, s2) -> s2.getAge() - s1.getAge()).skip(2).collect(Collectors.toList());
		System.out.println(aStudents);
	}
	
	/**
	 * map
	 */
	private static void map(List<Student> students) {
		List<String> aStudents = students.stream().filter(s -> "�������ѧ".equals(s.getMajor())).map(Student :: getName).collect(Collectors.toList());
		System.out.println(aStudents);
		
		//��������רҵΪ�������ѧѧ��������֮��
		//mapToInt, mapToDouble,mapToLong����ָ�����͵ķ���ֵ
		int sum = students.stream().filter(s -> "�������ѧ".equals(s.getMajor())).mapToInt(Student :: getAge).sum();
		System.out.println(sum);
		
	}

	/**
	 * flatMap
	 */
	private static void flatMap() {
		//���ֶδ�strs�����е������ַ�����ȥ�ز���
		String[] strs = {"java8", "is", "easy", "to", "use"};
		List<String[]> strLists = Arrays.stream(strs).map(s -> s.split("")).distinct().collect(Collectors.toList());
		for (String[] strList : strLists) {
			System.out.println(Arrays.toString(strList));
		}
		
		/**
		 * ��ִ��map�����󣬵õ�����һ����������ַ������������£������Դﲻ��ȥ�ص�Ч��
		 * [j, a, v, a, 8]
		 * [i, s]
		 * [e, a, s, y]
		 * [t, o]
		 * [u, s, e]
		 * 
		 * ʹ��flatMap���Խ�����ַ������ϲ�Ϊһ���ַ�����
		 */
		
		List<String> distinctStrs = Arrays.stream(strs).map(s -> s.split("")).flatMap(Arrays :: stream).distinct().collect(Collectors.toList());
		System.out.println(distinctStrs);
		
		//������[java8, is]��[easy, to, use]���кϲ��Ĳ���
		ArrayList<List<String>> arrs = new ArrayList<List<String>>();
		ArrayList<String> arr1 = new ArrayList<String>();
		arr1.add("java8");
		arr1.add("is");
		arrs.add(arr1);
		
		ArrayList<String> arr2 = new ArrayList<String>();
		arr2.add("easy");
		arr2.add("to");
		arr2.add("use");
		arrs.add(arr2);
		
		//[java8, is, easy, to, use]
		System.out.println(arrs.stream().flatMap(Collection :: stream).collect(Collectors.toList()));
	}
	
	/**
	 * allMatch ���ڼ����Ƿ�ȫ��������ָ��������������������㷵��true�����򷵻�false
	 */
	private static void allMatch(List<Student> students) {
		boolean allMatch = students.stream().allMatch(s -> s.getAge() >= 18);
		System.out.println(allMatch);
	}
	
	/**
	 * anyMatch ���ڼ����Ƿ����һ�����߶������ָ��������������������㷵��true�����򷵻�false
	 */
	private static void anyMatch(List<Student> students) {
		boolean anyMatch = students.stream().anyMatch(s -> "�人��ѧ".equals(s.getSchool()));
		System.out.println(anyMatch);
	}
	
	/**
	 * anyMatch ���ڼ���Ƿ񲻴���ָ����Ϊ��Ԫ�أ���������ڷ���true�����򷵻�false
	 */
	private static void noneMatch(List<Student> students) {
		boolean noneMatch = students.stream().noneMatch(s -> "�������ѧ".equals(s.getMajor()));
		System.out.println(noneMatch);
	}
	
	/**
	 * findFirst ���ڷ������������ĵ�һ��Ԫ��
	 */
	private static void findFirst(List<Student> students) {
		//ѡ��רҵΪ��ľ�������ڵ�һλ��ѧ��
		Optional<Student> findFirst = students.stream().filter(s -> "��ľ����".equals(s.getMajor())).findFirst();
		if(findFirst.isPresent()) {
			System.out.println(findFirst.get());
		}
	}
	
	/**
	 * findAny ���ڷ�����������������һ��Ԫ��
	 */
	private static void findAny(List<Student> students) {
		Optional<Student> findAny = students.stream().filter(s -> "��ľ����".equals(s.getMajor())).findAny();
		if(findAny.isPresent()) {
			System.out.println(findAny.get());
		}
	}
	
	/**
	 * reduce �ù�Լ���������Դﵽ֮ǰmapToInt��Ч��
	 */
	private static void reduce(List<Student> students) {
		//mapToInt�ķ���
		int sum = 0;
		sum = students.stream().filter(s -> "�������ѧ".equals(s.getMajor())).mapToInt(Student :: getAge).sum();
		System.out.println("mapToInt's result : " + sum);
		
		//��Լ�ķ���
		sum = students.stream().filter(s -> "�������ѧ".equals(s.getMajor())).map(Student :: getAge).reduce(0, (a, b) -> a + b);
		System.out.println("reduce1's result : " + sum);
		
		//��һ����
		sum = students.stream().filter(s -> "�������ѧ".equals(s.getMajor())).map(Student :: getAge).reduce(0, Integer :: sum);
		System.out.println("reduce2's result : " + sum);
		
		//ʡ�Գ�ʼֵ
		Optional<Integer> totalAge = students.stream().filter(s -> "�������ѧ".equals(s.getMajor())).map(Student :: getAge).reduce(Integer :: sum);
		if(totalAge.isPresent()) {
			System.out.println("reduce3's result : " + totalAge.get());
		}
	}
	
	/**
	 * ��Լ����
	 */
	private static void count(List<Student> students) {
		//1.ѧ��������
		Long count = students.stream().collect(Collectors.counting());
		//��һ���Ż�
		count = students.stream().count();
		System.out.println(count);
		
		//2.����������ֵ����Сֵ
		//���ֵ
		Optional<Student> maxAge = students.stream().collect(Collectors.maxBy((s1, s2) -> s1.getAge() - s2.getAge()));
		//��һ����
		maxAge = students.stream().collect(Collectors.maxBy(Comparator.comparing(Student :: getAge)));
		System.out.println(maxAge.orElse(new Student()));
		//��Сֵ
		Optional<Student> minAge = students.stream().collect(Collectors.minBy((s1, s2) -> s1.getAge() - s2.getAge()));
		//��һ����
		minAge = students.stream().collect(Collectors.minBy(Comparator.comparing(Student :: getAge)));
		System.out.println(minAge.orElse(new Student()));
		
		//3.�������ܺ�
		Integer sum = students.stream().collect(Collectors.summingInt(Student :: getAge));
		System.out.println(sum);
		
		//4.�������ƽ��ֵ
		Double average = students.stream().collect(Collectors.averagingInt(Student :: getAge));
		System.out.println(average);
		
		//һ���Եõ�Ԫ�ظ������ܺ͡���ֵ�����ֵ����Сֵ
		IntSummaryStatistics statistics = students.stream().collect(Collectors.summarizingInt(Student :: getAge));
		System.out.println(statistics.toString());
		
		//�ַ���ƴ��
		String names = students.stream().map(s -> s.getName()).collect(Collectors.joining());
		System.out.println(names);
		names = students.stream().map(Student :: getName).collect(Collectors.joining("','", "'", "'"));
		System.out.println(names);
	}
	
	/**
	 * toList ��stream��ת��ΪList���Ϸ���
	 */
	private static void toList(List<Student> students) {
		List<Student> aStudents = students.stream().filter(s -> "�人��ѧ".equals(s.getSchool())).collect(Collectors.toList());
		System.out.println(aStudents);
	}
	
	/**
	 * toMap ���ڽ�ListתΪMap
	 */
	private static void toMap(List<Student> students) {
		//(key=ID, value = student)��list����studentsת��ΪMap
		Map<Long, Student> map = students.stream().collect(Collectors.toMap(Student :: getId, Function.identity()));
		System.out.println(map.get(20160001L));
		
		//(key=ID,value=name)list����(students)�е�id��nameת��Ϊmap
		Map<Long, String> nameMap = students.stream().collect(Collectors.toMap(Student :: getId, Student :: getName));
		System.out.println(nameMap);
	}
	
	/**
	 * toSet stream��ת��ΪSet���Ϸ���
	 */
	private static void toSet(List<Student> students) {
		Set<Student> set = students.stream().collect(Collectors.toSet());
		System.out.println(set);
	}
	
	/**
	 * group ����
	 */
	private static void group(List<Student> students) {
		//��ѧУ��ѧ�����з���
		Map<String, List<Student>> groups = students.stream().collect(Collectors.groupingBy(Student :: getSchool));
		System.out.println(groups);
	
		//���ط���
		Map<String, Map<String, List<Student>>> groupes = students.stream().collect(Collectors.groupingBy(Student :: getSchool, Collectors.groupingBy(Student :: getMajor)));
		System.out.println(groupes);
		
		//�ڶ���������ֹ�ܴ���groupingBy�������Դ�������Collector����
		//����һ��Collectors.counting��ͳ��ÿ����ĸ���
		Map<String, Long> maps = students.stream().collect(Collectors.groupingBy(Student :: getSchool, Collectors.counting()));
		System.out.println(maps);
	}
	
	/**
	 * ����
	 * @param numbers 
	 */
	private static void partition(List<Student> students, List<Integer> numbers) {
		//��ѧ����Ϊ�人��ѧѧ���ͷ��人��ѧѧ��
		Map<Boolean, List<Student>> map = students.stream().collect(Collectors.partitioningBy(s -> "�人��ѧ".equals(s.getSchool())));
		System.out.println(map);
		
		//�������Ϊ������ż��
		Map<Boolean, List<Integer>> maps = numbers.stream().distinct().collect(Collectors.partitioningBy(n -> n % 2 == 0));
		System.out.println(maps);
	}
	
	/**
	 * forEach ����
	 */
	private static void forEach(List<Student> students) {
		//������ѧ������������һ��
		students.stream().forEach(s -> {
			s.setAge(s.getAge() * 2);
		});
		
		//�������е�ѧ��
		students.stream().forEach(System.out :: println);
		
		//�������е�ѧ��������
		students.stream().forEach(s -> {
			System.out.println(s.getAge());
		});
	}
	
	/**
	 * max min �������е����ֵ����Сֵ
	 */
	private static void maxMin(List<Student> students) {
		//��������ѧ��
		Student max = students.stream().max((s1, s2) -> s1.getAge() - s2.getAge()).orElse(new Student());
		System.out.println(max);
		
		//������С��ѧ��
		Student min = students.stream().min(Comparator.comparing(Student :: getAge)).orElse(new Student());
		System.out.println(min);
		
		//ѧУ�������ѧ��
		Student maxName = students.stream().max((s1, s2) -> s1.getSchool().length() - s2.getSchool().length()).orElse(new Student());
		System.out.println(maxName);
		
		//ѧУ������̵�ѧ��
		Student minName = students.stream().min((s1, s2) -> s1.getSchool().length() - s2.getSchool().length()).orElse(new Student());
		System.out.println(minName);
	}
	
	/**
	 * count �������е�Ԫ������
	 */
	private static void counting(List<Student> students) {
		//ѧ��������
		long totalCount = students.stream().count();
		System.out.println(totalCount);
		
		//�人��ѧѧ������
		long whCount = students.stream().filter(s -> "�人��ѧ".equals(s.getSchool())).count();
		System.out.println(whCount);
	}
}
