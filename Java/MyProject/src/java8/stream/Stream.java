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
		
		/************************* 过滤 start *************************/
		/*
		 * 过滤
		 * 过滤，顾名思义就是按照给定的要求对集合进行筛选满足条件的元素，java8提供的的筛选操作包括：filter、distinct、limit、skip.
		 */
		//filter(students);
		//distinct(numbers);
		//limit(students);
		//skip(students);
		/************************* 过滤 end *************************/
		
		/************************* 映射 start *************************/
		/*
		 * sql中，类似于select后面添加需要的字段
		 */
		//map(students);
		//flatMap();
		/************************* 映射 end *************************/
		
		/************************ 终端操作 start ************************/
		/************************* 查找 start *************************/
		//allMatch(students);
		//anyMatch(students);
		//noneMatch(students);
		//findFirst(students);
		//findAny(students);
		/************************* 查找 end *************************/
		
		/************************* 归约 start *************************/
		//reduce(students);
		//count(students);
		
		/************************* 归约 end *************************/
		
		/************************* 收集 start *************************/
		//toList(students);
		//toMap(students);
		//toSet(students);
		
		/************************* 收集 end *************************/
		
		/************************* 分组 start *************************/
		//group(students);
		/************************* 分组 end *************************/
		
		/************************* 分区 start *************************/
		//partition(students, numbers);
		/************************* 分区 end *************************/
		
		/************************* 其他 start *************************/
		//forEach(students);
		//maxMin(students);
		counting(students);
		/************************* 其他 end *************************/
		
		/************************ 终端操作 end ************************/
	}
	
	private static List<Student> init() {
		// 初始化
		List<Student> students = new ArrayList<Student>() {
			private static final long serialVersionUID = -6262796848539237179L;

			{
				add(new Student(20160001, "孔明", 20, 1, "土木工程", "武汉大学"));
				add(new Student(20160002, "伯约", 21, 2, "信息安全", "武汉大学"));
				add(new Student(20160003, "玄德", 22, 3, "经济管理", "武汉大学"));
				add(new Student(20160004, "云长", 21, 2, "信息安全", "武汉大学"));
				add(new Student(20161001, "翼德", 21, 2, "机械与自动化", "华中科技大学"));
				add(new Student(20161002, "元直", 23, 4, "土木工程", "华中科技大学"));
				add(new Student(20161003, "奉孝", 23, 4, "计算机科学", "华中科技大学"));
				add(new Student(20162001, "仲谋", 22, 3, "土木工程", "浙江大学"));
				add(new Student(20162002, "鲁肃", 23, 4, "计算机科学", "浙江大学"));
				add(new Student(20163001, "丁奉", 24, 5, "土木工程", "南京大学"));
			}
		};

		return students;
	}

	private static List<Integer> initNum() {
		// 初始化
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
		List<Student> aStudents = students.stream().filter(s -> "武汉大学".equals(s.getSchool())).collect(Collectors.toList());
		System.out.println(aStudents);
	}
	
	/**
	 * distinct
	 */
	private static void distinct(List<Integer> numbers) {
		//对数组进行去重的操作
		List<Integer> number = numbers.stream().distinct().collect(Collectors.toList());
		System.out.println(number);
		
		//获取数组中的偶数，并进行去重操作
		number = numbers.stream().filter(n -> n % 2 == 0).distinct().collect(Collectors.toList());
		System.out.println(number);
	}
	
	/**
	 * limit 返回包含前n个元素的流，当集合长度小于n时，返回实际长度
	 */
	private static void limit(List<Student> students) {
		List<Student> aStudents = students.stream().filter(s -> "土木工程".equals(s.getMajor())).limit(2).collect(Collectors.toList());
		System.out.println(aStudents);
		
		//筛选出专业为土木工程的学生，并按年龄从小到大排序，筛选出年龄最小的两个学生
		aStudents = students.stream().filter(s -> "土木工程".equals(s.getMajor())).sorted((s1, s2) -> s1.getAge() - s2.getAge()).limit(2).collect(Collectors.toList());
		System.out.println(aStudents);
	}
	
	/**
	 * skip 跳过前n个元素，与limit用法相反
	 */
	private static void skip(List<Student> students) {
		List<Student> aStudents = students.stream().filter(s -> "土木工程".equals(s.getMajor())).skip(2).collect(Collectors.toList());
		System.out.println(aStudents);
		//筛选出专业为土木工程的学生，并按年龄从小到大排序，筛选出年龄最小的两个学生
		aStudents = students.stream().filter(s -> "土木工程".equals(s.getMajor())).sorted((s1, s2) -> s2.getAge() - s1.getAge()).skip(2).collect(Collectors.toList());
		System.out.println(aStudents);
	}
	
	/**
	 * map
	 */
	private static void map(List<Student> students) {
		List<String> aStudents = students.stream().filter(s -> "计算机科学".equals(s.getMajor())).map(Student :: getName).collect(Collectors.toList());
		System.out.println(aStudents);
		
		//计算所有专业为计算机科学学生的年龄之和
		//mapToInt, mapToDouble,mapToLong返回指定类型的返回值
		int sum = students.stream().filter(s -> "计算机科学".equals(s.getMajor())).mapToInt(Student :: getAge).sum();
		System.out.println(sum);
		
	}

	/**
	 * flatMap
	 */
	private static void flatMap() {
		//对字段串strs集合中的所有字符进行去重操作
		String[] strs = {"java8", "is", "easy", "to", "use"};
		List<String[]> strLists = Arrays.stream(strs).map(s -> s.split("")).distinct().collect(Collectors.toList());
		for (String[] strList : strLists) {
			System.out.println(Arrays.toString(strList));
		}
		
		/**
		 * 在执行map操作后，得到的是一个包含多个字符串的流（如下），所以达不到去重的效果
		 * [j, a, v, a, 8]
		 * [i, s]
		 * [e, a, s, y]
		 * [t, o]
		 * [u, s, e]
		 * 
		 * 使用flatMap可以将多个字符串流合并为一个字符串流
		 */
		
		List<String> distinctStrs = Arrays.stream(strs).map(s -> s.split("")).flatMap(Arrays :: stream).distinct().collect(Collectors.toList());
		System.out.println(distinctStrs);
		
		//将集合[java8, is]、[easy, to, use]进行合并的操作
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
	 * allMatch 用于检验是否全部都满足指定参数的条件，如果满足返回true，否则返回false
	 */
	private static void allMatch(List<Student> students) {
		boolean allMatch = students.stream().allMatch(s -> s.getAge() >= 18);
		System.out.println(allMatch);
	}
	
	/**
	 * anyMatch 用于检验是否存在一个或者多个满足指定参数的条件，如果满足返回true，否则返回false
	 */
	private static void anyMatch(List<Student> students) {
		boolean anyMatch = students.stream().anyMatch(s -> "武汉大学".equals(s.getSchool()));
		System.out.println(anyMatch);
	}
	
	/**
	 * anyMatch 用于检测是否不存在指定行为的元素，如果不存在返回true，否则返回false
	 */
	private static void noneMatch(List<Student> students) {
		boolean noneMatch = students.stream().noneMatch(s -> "计算机科学".equals(s.getMajor()));
		System.out.println(noneMatch);
	}
	
	/**
	 * findFirst 用于返回满足条件的第一个元素
	 */
	private static void findFirst(List<Student> students) {
		//选出专业为土木工程排在第一位的学生
		Optional<Student> findFirst = students.stream().filter(s -> "土木工程".equals(s.getMajor())).findFirst();
		if(findFirst.isPresent()) {
			System.out.println(findFirst.get());
		}
	}
	
	/**
	 * findAny 用于返回满足条件的任意一个元素
	 */
	private static void findAny(List<Student> students) {
		Optional<Student> findAny = students.stream().filter(s -> "土木工程".equals(s.getMajor())).findAny();
		if(findAny.isPresent()) {
			System.out.println(findAny.get());
		}
	}
	
	/**
	 * reduce 用归约操作，可以达到之前mapToInt的效果
	 */
	private static void reduce(List<Student> students) {
		//mapToInt的方法
		int sum = 0;
		sum = students.stream().filter(s -> "计算机科学".equals(s.getMajor())).mapToInt(Student :: getAge).sum();
		System.out.println("mapToInt's result : " + sum);
		
		//归约的方法
		sum = students.stream().filter(s -> "计算机科学".equals(s.getMajor())).map(Student :: getAge).reduce(0, (a, b) -> a + b);
		System.out.println("reduce1's result : " + sum);
		
		//进一步简化
		sum = students.stream().filter(s -> "计算机科学".equals(s.getMajor())).map(Student :: getAge).reduce(0, Integer :: sum);
		System.out.println("reduce2's result : " + sum);
		
		//省略初始值
		Optional<Integer> totalAge = students.stream().filter(s -> "计算机科学".equals(s.getMajor())).map(Student :: getAge).reduce(Integer :: sum);
		if(totalAge.isPresent()) {
			System.out.println("reduce3's result : " + totalAge.get());
		}
	}
	
	/**
	 * 归约操作
	 */
	private static void count(List<Student> students) {
		//1.学生总人数
		Long count = students.stream().collect(Collectors.counting());
		//进一步优化
		count = students.stream().count();
		System.out.println(count);
		
		//2.求年龄的最大值和最小值
		//最大值
		Optional<Student> maxAge = students.stream().collect(Collectors.maxBy((s1, s2) -> s1.getAge() - s2.getAge()));
		//进一步简化
		maxAge = students.stream().collect(Collectors.maxBy(Comparator.comparing(Student :: getAge)));
		System.out.println(maxAge.orElse(new Student()));
		//最小值
		Optional<Student> minAge = students.stream().collect(Collectors.minBy((s1, s2) -> s1.getAge() - s2.getAge()));
		//进一步简化
		minAge = students.stream().collect(Collectors.minBy(Comparator.comparing(Student :: getAge)));
		System.out.println(minAge.orElse(new Student()));
		
		//3.求年龄总和
		Integer sum = students.stream().collect(Collectors.summingInt(Student :: getAge));
		System.out.println(sum);
		
		//4.求年龄的平均值
		Double average = students.stream().collect(Collectors.averagingInt(Student :: getAge));
		System.out.println(average);
		
		//一次性得到元素个数、总和、均值、最大值、最小值
		IntSummaryStatistics statistics = students.stream().collect(Collectors.summarizingInt(Student :: getAge));
		System.out.println(statistics.toString());
		
		//字符串拼接
		String names = students.stream().map(s -> s.getName()).collect(Collectors.joining());
		System.out.println(names);
		names = students.stream().map(Student :: getName).collect(Collectors.joining("','", "'", "'"));
		System.out.println(names);
	}
	
	/**
	 * toList 将stream流转换为List集合返回
	 */
	private static void toList(List<Student> students) {
		List<Student> aStudents = students.stream().filter(s -> "武汉大学".equals(s.getSchool())).collect(Collectors.toList());
		System.out.println(aStudents);
	}
	
	/**
	 * toMap 用于将List转为Map
	 */
	private static void toMap(List<Student> students) {
		//(key=ID, value = student)将list集合students转换为Map
		Map<Long, Student> map = students.stream().collect(Collectors.toMap(Student :: getId, Function.identity()));
		System.out.println(map.get(20160001L));
		
		//(key=ID,value=name)list集合(students)中的id和name转换为map
		Map<Long, String> nameMap = students.stream().collect(Collectors.toMap(Student :: getId, Student :: getName));
		System.out.println(nameMap);
	}
	
	/**
	 * toSet stream流转换为Set集合返回
	 */
	private static void toSet(List<Student> students) {
		Set<Student> set = students.stream().collect(Collectors.toSet());
		System.out.println(set);
	}
	
	/**
	 * group 分组
	 */
	private static void group(List<Student> students) {
		//按学校对学生进行分组
		Map<String, List<Student>> groups = students.stream().collect(Collectors.groupingBy(Student :: getSchool));
		System.out.println(groups);
	
		//多重分组
		Map<String, Map<String, List<Student>>> groupes = students.stream().collect(Collectors.groupingBy(Student :: getSchool, Collectors.groupingBy(Student :: getMajor)));
		System.out.println(groupes);
		
		//第二个参数不止能传递groupingBy，还可以传递任意Collector类型
		//传递一个Collectors.counting，统计每个组的个数
		Map<String, Long> maps = students.stream().collect(Collectors.groupingBy(Student :: getSchool, Collectors.counting()));
		System.out.println(maps);
	}
	
	/**
	 * 分区
	 * @param numbers 
	 */
	private static void partition(List<Student> students, List<Integer> numbers) {
		//将学生分为武汉大学学生和非武汉大学学生
		Map<Boolean, List<Student>> map = students.stream().collect(Collectors.partitioningBy(s -> "武汉大学".equals(s.getSchool())));
		System.out.println(map);
		
		//将数组分为奇数和偶数
		Map<Boolean, List<Integer>> maps = numbers.stream().distinct().collect(Collectors.partitioningBy(n -> n % 2 == 0));
		System.out.println(maps);
	}
	
	/**
	 * forEach 遍历
	 */
	private static void forEach(List<Student> students) {
		//将所有学生的年龄增长一倍
		students.stream().forEach(s -> {
			s.setAge(s.getAge() * 2);
		});
		
		//遍历所有的学生
		students.stream().forEach(System.out :: println);
		
		//遍历所有的学生的年龄
		students.stream().forEach(s -> {
			System.out.println(s.getAge());
		});
	}
	
	/**
	 * max min 返回流中的最大值、最小值
	 */
	private static void maxMin(List<Student> students) {
		//年龄最大的学生
		Student max = students.stream().max((s1, s2) -> s1.getAge() - s2.getAge()).orElse(new Student());
		System.out.println(max);
		
		//年龄最小的学生
		Student min = students.stream().min(Comparator.comparing(Student :: getAge)).orElse(new Student());
		System.out.println(min);
		
		//学校名字最长的学生
		Student maxName = students.stream().max((s1, s2) -> s1.getSchool().length() - s2.getSchool().length()).orElse(new Student());
		System.out.println(maxName);
		
		//学校名字最短的学生
		Student minName = students.stream().min((s1, s2) -> s1.getSchool().length() - s2.getSchool().length()).orElse(new Student());
		System.out.println(minName);
	}
	
	/**
	 * count 返回流中的元素总数
	 */
	private static void counting(List<Student> students) {
		//学生总人数
		long totalCount = students.stream().count();
		System.out.println(totalCount);
		
		//武汉大学学生个数
		long whCount = students.stream().filter(s -> "武汉大学".equals(s.getSchool())).count();
		System.out.println(whCount);
	}
}
