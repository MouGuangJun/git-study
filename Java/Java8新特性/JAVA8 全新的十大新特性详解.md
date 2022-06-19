# JAVA8 全新的十大新特性详解

## 一、接口的默认方法



Java 8允许我们给接口添加一个非抽象的方法实现，只需要使用 default关键字即可，这个特征又叫做扩展方法，示例如下：

代码如下:

```java
interface Formula {
    double calculate(int a);

    default double sqrt(int a) {
        return Math.sqrt(a);
    }
}
```

Formula接口在拥有calculate方法之外同时还定义了sqrt方法，实现了Formula接口的子类只需要实现一个calculate方法，默认方法sqrt将在子类上可以直接使用。
代码如下:

```java
 Formula formula = new Formula() {
     @Override
     public double calculate(int a) {
         return sqrt(a * 100);
     }
 };

System.out.println(formula.calculate(100));// 100.0
System.out.println(formula.sqrt(16));// 4.0

```

文中的formula被实现为一个匿名类的实例，该代码非常容易理解，6行代码实现了计算 sqrt(a * 100)。在下一节中，我们将会看到实现单方法接口的更简单的做法。

译者注： 在Java中只有单继承，如果要让一个类赋予新的特性，通常是使用接口来实现，在C++中支持多继承，允许一个子类同时具有多个父类的接口与功能，在其他语言中，让一个类同时具有其他的可复用代码的方法叫做mixin。新的Java 8 的这个特新在编译器实现的角度上来说更加接近Scala的trait。 在C#中也有名为扩展方法的概念，允许给已存在的类型扩展方法，和Java 8的这个在语义上有差别。



## 二、Lambda 表达式

首先看看在老版本的Java中是如何排列字符串的：

代码如下:

```java
List<String> names = Arrays.asList("peter", "anna", "mike", "xenia");
Collections.sort(names, new Comparator<String>() {
    @Override
    public int compare(String a, String b) {
        return a.compareTo(b);
    }
});
```

只需要给静态方法 Collections.sort 传入一个List对象以及一个比较器来按指定顺序排列。通常做法都是创建一个匿名的比较器对象然后将其传递给sort方法。

在Java 8 中你就没必要使用这种传统的匿名对象的方式了，Java 8提供了更简洁的语法，lambda表达式：

代码如下:

```java
Collections.sort(names, (String a, String b) -> {
    return b.compareTo(a);
});
```

看到了吧，代码变得更段且更具有可读性，但是实际上还可以写得更短：
代码如下:

```java
Collections.sort(names, (String a, String b) -> a.compareTo(b));
```

对于函数体只有一行代码的，你可以去掉大括号{}以及return关键字，但是你还可以写得更短点：
代码如下:

```java
Collections.sort(names, (a, b) -> b.compareTo(a));
```

Java编译器可以自动推导出参数类型，所以你可以不用再写一次类型。接下来我们看看lambda表达式还能作出什么更方便的东西来：



## 三、函数式接口

Lambda表达式是如何在java的类型系统中表示的呢？每一个lambda表达式都对应一个类型，通常是接口类型。而“函数式接口”是指仅仅只包含一个抽象方法的接口，每一个该类型的lambda表达式都会被匹配到这个抽象方法。因为 默认方法 不算抽象方法，所以你也可以给你的函数式接口添加默认方法。
我们可以将lambda表达式当作任意只包含一个抽象方法的接口类型，确保你的接口一定达到这个要求，你只需要给你的接口添加 @FunctionalInterface 注解，编译器如果发现你标注了这个注解的接口有多于一个抽象方法的时候会报错的。

示例如下：

代码如下:

```java
@FunctionalInterface
interface Converter<F, T> {
    T convert(F from);
}

Converter<String, Integer> converter = (from) -> Integer.valueOf(from);
Integer converted = converter.convert("123");
System.out.println(converted);// 123
```



需要注意如果@FunctionalInterface如果没有指定，上面的代码也是对的。
译者注 将lambda表达式映射到一个单方法的接口上，这种做法在Java 8之前就有别的语言实现，比如Rhino JavaScript解释器，如果一个函数参数接收一个单方法的接口而你传递的是一个function，Rhino 解释器会自动做一个单接口的实例到function的适配器，典型的应用场景有 org.w3c.dom.events.EventTarget 的addEventListener 第二个参数 EventListener。



## 四、方法与构造函数引用

前一节中的代码还可以通过静态方法引用来表示：

代码如下:

```java
Converter<String, Integer> converter = Integer::valueOf;
Integer converted = converter.convert("123");
System.out.println(converted);// 123
```

Java 8 允许你使用 :: 关键字来传递方法或者构造函数引用，上面的代码展示了如何引用一个静态方法，我们也可以引用一个对象的方法：
代码如下:

```java
static class Something {
    public String startsWith(String str) {
        if (null == str || str.length() == 0) return str;
        return str.substring(0, 1);
    }
}

Something something = new Something();
Converter<String, String> strConverter = something::startsWith;
String strConverted = strConverter.convert("Java");
System.out.println(strConverted);// "J"

```

接下来看看构造函数是如何使用::关键字来引用的，首先我们定义一个包含多个构造函数的简单类：
代码如下:

```java
static class Person {
    String firstName;
    String lastName;

    Person() {
    }

    Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return "Person{" +
            "firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            '}';
    }
}
```

接下来我们指定一个用来创建Person对象的对象工厂接口：
代码如下:

```java
interface PersonFactory<P extends Person> {
    P create(String firstName, String lastName);
}
```

这里我们使用构造函数引用来将他们关联起来，而不是实现一个完整的工厂：
代码如下:

```java
PersonFactory<Person> personFactory = Person::new;
Person person = personFactory.create("Peter", "Parker");
System.out.println(person);
```

我们只需要使用 Person::new 来获取Person类构造函数的引用，Java编译器会自动根据PersonFactory.create方法的签名来选择合适的构造函数。



## 五、Lambda 作用域

在lambda表达式中访问外层作用域和老版本的匿名对象中的方式很相似。你可以直接访问标记了final的外层局部变量，或者实例的字段以及静态变量。



## 六、访问局部变量

我们可以直接在lambda表达式中访问外层的局部变量：

代码如下:

```java
final int finalNum = 1;
Converter<Integer, String> finalConverter =
    (from) -> String.valueOf(from + finalNum);
System.out.println(finalConverter.convert(2));// 3
```

但是和匿名对象不同的是，这里的变量num可以不用声明为final，该代码同样正确：
代码如下:

```java
int noFinalNum = 1;
Converter<Integer, String> noFinalConverter =
    (from) -> String.valueOf(from + noFinalNum);
System.out.println(noFinalConverter.convert(2));// 3
```

不过这里的num必须不可被后面的代码修改（即隐性的具有final的语义），例如下面的就无法编译：
代码如下:

```java
int num = 1;
Converter<Integer, String> stringConverter =
        (from) -> String.valueOf(from + num);
num = 3;
```

在lambda表达式中试图修改num同样是不允许的。



## 七、访问对象字段与静态变量

和本地变量不同的是，lambda内部对于实例的字段以及静态变量是即可读又可写。该行为和匿名对象是一致的：

代码如下:

```java
static class LambdaScope {
    static int outerStaticNum;
    int outerNum;

    void scopes() {
        Converter<Integer, String> strConverter1 = (from) -> {
            outerNum = 23;// 成员变量可以被重新赋值
            return String.valueOf(from);
        };

        Converter<Integer, String> strConverter2 = (from) -> {
            outerStaticNum = 72;// 静态变量可以被重新赋值
            return String.valueOf(from);
        };

        strConverter1.convert(52);
        strConverter2.convert(25);

        System.out.println(outerNum);
        System.out.println(outerStaticNum);
    }
}
```



## 八、访问接口的默认方法

还记得第一节中的formula例子么，接口Formula定义了一个默认方法sqrt可以直接被formula的实例包括匿名对象访问到，但是在lambda表达式中这个是不行的。
Lambda表达式中是无法访问到默认方法的，以下代码将无法编译：
代码如下:

```java
Formula formula = (a) -> sqrt( a * 100);
Built-in Functional Interfaces
```

JDK 1.8 API包含了很多内建的函数式接口，在老Java中常用到的比如Comparator或者Runnable接口，这些接口都增加了@FunctionalInterface注解以便能用在lambda上。

Java 8 API同样还提供了很多全新的函数式接口来让工作更加方便，有一些接口是来自Google Guava库里的，即便你对这些很熟悉了，还是有必要看看这些是如何扩展到lambda上使用的。

### Predicate接口

Predicate 接口只有一个参数，返回boolean类型。该接口包含多种默认方法来将Predicate组合成其他复杂的逻辑（比如：与，或，非）：

代码如下:

```java
Predicate<String> notNull = s -> s != null;
Predicate<String> notEmpty = s -> s.length() > 0;
System.out.println(String.format("常规操作：%s", notEmpty.test("foo")));
// 取反
System.out.println(String.format("取反操作：%s", notEmpty.negate().test("foo")));
// 与
System.out.println(String.format("与操作：%s", notNull.and(notEmpty).test(null)));
// 或
//System.out.println(String.format("或操作：%s", notNull.or(notEmpty).test(null)));

// 相等操作
Predicate<Object> isEqual = Predicate.isEqual("Hello, Predicate");
System.out.println(String.format("相等操作：%s", isEqual.test("Hello, Function")));

// 对象引用
Predicate<String> isEmpty = String::isEmpty;// 字符串为空
System.out.println(String.format("对象引用：%s", isEmpty.test("")));
```

### Function 接口

```java
Function<String, Integer> toInteger = Integer::valueOf;
// andThen
Function<String, String> back2String = toInteger.andThen(String::valueOf);
System.out.println(String.format("andThen测试：%s", back2String.apply("123")));

// compose
Function<char[], String> char2String = String::new;
System.out.println(String.format("compose测试：%s", toInteger.compose(char2String).apply(new char[]{'3', '2', '1'})));

Function<Object, Object> identity = Function.identity();
System.out.println(String.format("identity：%s", identity.apply("我返回我自己！")));

```

### Supplier 接口

Supplier 接口返回一个任意范型的值，和Function接口不同的是该接口没有任何参数
代码如下:

```java
Supplier<Person> personSupplier = Person::new;
Person person = personSupplier.get();
```

### Consumer 接口

Consumer 接口表示执行在单个参数上的操作。
代码如下:

```java
Supplier<Person> personSupplier = () -> new Person("Jack", "Rose");

// Consumer 操作
Consumer<Person> greeter = p ->
    System.out.println(String.format("Hello, %s", p.lastName));
greeter.accept(personSupplier.get());
```

### Comparator 接口

Comparator 是老Java中的经典接口， Java 8在此之上添加了多种默认方法：
代码如下:

```java
Comparator<Person> comparator = (p1, p2) -> p1.firstName.compareTo(p2.firstName);
Person p1 = new Person("John", "Doe");
Person p2 = new Person("Alice", "Wonderland");
System.out.println("John".compareTo("Alice"));

System.out.println(comparator.compare(p1, p2));// > 0
System.out.println(comparator.reversed().compare(p1, p2));// < 0

```

### Optional 类

Optional 不是函数是接口，这是个用来防止NullPointerException异常的辅助类型，这是下一届中将要用到的重要概念，现在先简单的看看这个接口能干什么：

Optional 被定义为一个简单的容器，其值可能是null或者不是null。在Java 8之前一般某个函数应该返回非空对象但是偶尔却可能返回了null，而在Java 8中，不推荐你返回null而是返回Optional。

```java
// Optional<Object> optional1 = Optional.of(null);// java.lang.NullPointerException
Optional<String> optional2 = Optional.of("are you good Malaysia");
Optional<Object> optional3 = Optional.ofNullable(null);// 正常运行

System.out.println(optional2.isPresent());// true
System.out.println(optional3.isPresent());// false

System.out.println(optional2.orElse("歪比巴卜"));// are you good Malaysia
System.out.println(optional3.orElse("肉蛋葱鸡"));// 肉蛋葱鸡
```

### Stream 接口

java.util.Stream 表示能应用在一组元素上一次执行的操作序列。Stream 操作分为中间操作或者最终操作两种，最终操作返回一特定类型的计算结果，而中间操作返回Stream本身，这样你就可以将多个操作依次串起来。Stream 的创建需要指定一个数据源，比如 java.util.Collection的子类，List或者Set， Map不支持。Stream的操作可以串行执行或者并行执行。

首先看看Stream是怎么用，首先创建实例代码的用到的数据List：

代码如下:

```java
List<String> stringCollection = new ArrayList<>();
stringCollection.add("a");
stringCollection.add("b");
stringCollection.add("c");
stringCollection.add("d");
stringCollection.add("e");
stringCollection.add("f");
stringCollection.add("g");
stringCollection.add("h");

// 通过 Collection.stream()创建
Stream<String> stream = stringCollection.stream();
System.out.println(stream);
// 通过Collection.parallelStream()创建
Stream<String> parallelStream = stringCollection.parallelStream();
System.out.println(parallelStream);
```

Java 8扩展了集合类，可以通过 Collection.stream() 或者 Collection.parallelStream() 来创建一个Stream。下面几节将详细解释常用的Stream操作：

#### Filter 过滤

过滤通过一个predicate接口来过滤并只保留符合条件的元素，该操作属于中间操作，所以我们可以在过滤后的结果来应用其他Stream操作（比如forEach）。forEach需要一个函数来对过滤后的元素依次执行。forEach是一个最终操作，所以我们不能在forEach之后来执行其他Stream操作。

代码如下:

```java
stringCollection.stream()
        .filter(c -> c.startsWith("t"))
        .forEach(System.out::println);
```

#### Sort 排序

排序是一个中间操作，返回的是排序好后的Stream。如果你不指定一个自定义的Comparator则会使用默认排序。

代码如下:

```java
stringCollection.stream()
        .sorted()
        .forEach(System.out::println);
// 排序创建了一个排好序的Stream，不影响原来的数据源
// [the, spider, is, on, the, top, road, ,, what, are, you, doing, ?]
System.out.println(stringCollection);
```

需要注意的是，排序只创建了一个排列好后的Stream，而不会影响原有的数据源，排序之后原数据stringCollection是不会被修改的。

#### Map 映射

中间操作map会将元素根据指定的Function接口来依次将元素转成另外的对象，下面的示例展示了将字符串转换为大写字符串。你也可以通过map来将对象转换成其他类型，map返回的Stream类型是根据你map传递进去的函数的返回值决定的。
代码如下:

```java
 stringCollection.stream()
     .map(String::toUpperCase)
     .forEach(System.out::println);
```

#### Match 匹配

Stream提供了多种匹配操作，允许检测指定的Predicate是否匹配整个Stream。所有的匹配操作都是最终操作，并返回一个boolean类型的值。

代码如下:

```java
boolean anyStartsWithT =
		stringCollection
        		.stream()
        		.anyMatch(s -> s.startsWith("t"));
System.out.println(anyStartsWithT);// true

boolean allStartsWithT =
    	stringCollection
    		.stream()
    		.allMatch(s -> s.startsWith("t"));

System.out.println(allStartsWithT);// false

boolean noneStartsWithZ =
    	stringCollection
    		.stream()
    		.noneMatch(s -> s.startsWith("z"));

System.out.println(noneStartsWithZ);// true
```

#### Count 计数

计数是一个最终操作，返回Stream中元素的个数，返回值类型是long。
代码如下:

```java
 long startsWithB =
     	stringCollection
     		.stream()
     		.filter(s -> s.startsWith("t"))
     		.count();
System.out.println(startsWithB);// 3
```

#### Reduce 规约

这是一个最终操作，允许通过指定的函数来讲stream中的多个元素规约为一个元素，规约后的结果是通过Optional接口表示的：

代码如下:

```java
Optional<String> reduced =
        stringCollection
    		.stream()
    		.reduce((s1, s2) -> s1 + " " + s2);
// the spider is on the top road , what are you doing ?
reduced.ifPresent(System.out::println);
```

#### 并行Streams

前面提到过Stream有串行和并行两种，串行Stream上的操作是在一个线程中依次完成，而并行Stream则是在多个线程上同时执行。

下面的例子展示了是如何通过并行Stream来提升性能：

首先我们创建一个没有重复元素的大表：

代码如下:

```java
private List<String> values = new ArrayList<String>() {
    private static final long serialVersionUID = -222577029175058858L;

    {
        for (int i = 0; i < 1000000; i++) {
            UUID uuid = UUID.randomUUID();
            values.add(uuid.toString());
        }
    }
};
```

然后我们计算一下排序这个Stream要耗时多久，

串行排序，代码如下:

```java
// 排序完成前的时间
long t0 = System.nanoTime();
long count = values.stream().sorted().count();
// 排序完成后的时间
long t1 = System.nanoTime();

long millis = TimeUnit.NANOSECONDS.toMillis(t1 - t0);
System.out.println(String.format("sequential sort took: %d ms", millis));
```

// 串行耗时: 899 ms

并行排序，代码如下:

```java
// 排序完成前的时间
long t0 = System.nanoTime();
long count = values.parallelStream().sorted().count();
// 排序完成后的时间
long t1 = System.nanoTime();

long millis = TimeUnit.NANOSECONDS.toMillis(t1 - t0);
System.out.println(String.format("parallel sort took: %d ms", millis));
```

// 并行排序耗时: 472 ms
上面两个代码几乎是一样的，但是并行版的快了50%之多，唯一需要做的改动就是将stream()改为parallelStream()。



### Map

前面提到过，Map类型不支持stream，不过Map提供了一些新的有用的方法来处理一些日常任务。

遍历Map，代码如下:

```java
Map<Integer, String> map = new HashMap<>();
for (int i = 0; i < 10; i++) {
    map.putIfAbsent(i, "val" + i);
}

map.forEach((k, v) -> System.out.println(v));
```

map.forEach((id, val) -> System.out.println(val));
以上代码很容易理解， putIfAbsent 不需要我们做额外的存在性检查，而forEach则接收一个Consumer接口来对map里的每一个键值对进行操作。

下面的例子展示了map上的其他有用的函数：

代码如下:

```java
// 如果key存在，对其进行操作，重新赋值
// 如果key不存在，没有实际意义
map.computeIfPresent(3, (k, v) -> v + k);
System.out.println(map.get(3));// val33

// 移除key
map.computeIfPresent(9, (num, val) -> null);
map.containsKey(9);// false


// 如果key不存在，添加一个元素，其中num是key的值
map.computeIfAbsent(23, num -> "val" + num);
System.out.println(map.get(23));

// 如果key存在，不做任何操作
map.computeIfAbsent(3, num -> "bam");
map.get(3);// val33
```

接下来展示如何在Map里删除一个键值全都匹配的项：
代码如下:

```java
// 当仅有在key和value都满足的条件下才进行删除
map.remove(3, "val33");
System.out.println(map.get(3));// val3
map.remove(3, "val3");
System.out.println(map.get(3));// null
```

如果Map中不存在对应的key值，返回默认值，相当于数据库的nvl方法

代码如下:

```java
System.out.println(map.getOrDefault("42", "not found"));// found
```

对Map的元素做合并也变得很容易了：
代码如下:

```java
// 如果key在map中的值为空，那么将"val18"放到map中
map.merge(18, "val18", (value, newValue) -> value.concat(newValue));
System.out.println(map.get(18));// val18

// 如果key在map中的值不为空，那么将key对应map里的值和"concat"进行合并
// value map里的老元素值，"concat"：合并值
map.merge(18, "concat", (value, newValue) -> value.concat(newValue));
System.out.println(map.get(18));// val18concat
```

Merge做的事情是如果键名不存在则插入，否则则对原键对应的值做合并操作并重新插入到map中。



## 九、Date API

Java 8 在包java.time下包含了一组全新的时间日期API。新的日期API和开源的Joda-Time库差不多，但又不完全一样，下面的例子展示了这组新API里最重要的一些部分：

### Clock 时钟

Clock类提供了访问当前日期和时间的方法，Clock是时区敏感的，可以用来取代 System.currentTimeMillis() 来获取当前的微秒数。某一个特定的时间点也可以使用Instant类来表示，Instant类也可以用来创建老的java.util.Date对象。

代码如下:

```java
// 让 Clock 使用默认时区返回当前时刻
Clock clock = Clock.systemDefaultZone();
System.out.println(clock);// SystemClock[Asia/Shanghai]
// 返回从 1970-01-01T00:00Z (UTC) 到开始测量的时钟的当前毫秒时刻。
System.out.println(clock.millis());// 1653922961066
// 返回时间线上的一个瞬时点
System.out.println(clock.instant());// 2022-05-30T15:07:18.337Z

// 从指定的基本时钟返回计时，并添加了指定的持续时间。
// 2022-05-31T15:10:43.915Z
System.out.println(Clock.offset(clock, Duration.ofHours(24L)).instant());

// 如果持续时间为负，则获得的时钟时刻将早于给定的基本时钟
// 2022-05-29T15:10:43.915Z
System.out.println(Clock.offset(clock, Duration.ofHours(-24L)).instant());

// 如果我们传递零持续时间，那么我们将获得与给定基本时钟相同的时钟。
// 2022-05-30T15:10:43.915Z
System.out.println(Clock.offset(clock, Duration.ZERO).instant());
```

### Timezones 时区

在新API中时区使用ZoneId来表示。时区可以很方便的使用静态方法of来获取到。 时区定义了到UTS时间的时间差，在Instant时间点对象到本地日期对象之间转换的时候是极其重要的。
代码如下:

```java
// prints all available timezone ids
System.out.println(ZoneId.getAvailableZoneIds());
ZoneId zone1 = ZoneId.of("Europe/Berlin");
ZoneId zone2 = ZoneId.of("Brazil/East");
// ZoneRules[currentStandardOffset=+01:00]
System.out.println(zone1.getRules());
// ZoneRules[currentStandardOffset=-03:00]
System.out.println(zone2.getRules());
```

### LocalTime 本地时间

LocalTime 定义了一个没有时区信息的时间，例如 晚上10点，或者 17:30:15。下面的例子使用前面代码创建的时区创建了两个本地时间。之后比较时间并以小时和分钟为单位计算两个时间的时间差：

代码如下:

```java
ZoneId zone1 = ZoneId.of("Europe/Berlin");
ZoneId zone2 = ZoneId.of("Brazil/East");
// 没有时区信息的时间
LocalTime now1 = LocalTime.now(zone1);
LocalTime now2 = LocalTime.now(zone2);
System.out.println(now1.isBefore(now2));// false

// 两个时间的时间差[小时]
long hoursBetween = ChronoUnit.HOURS.between(now1, now2);
// 两个时间的时间差[分钟]
long minutesBetween = ChronoUnit.MINUTES.between(now1, now2);

System.out.println(hoursBetween);// -4
System.out.println(minutesBetween);// -299
```

LocalTime 提供了多种工厂方法来简化对象的创建，包括解析时间字符串。
代码如下:

```java
LocalTime late = LocalTime.of(23, 59, 59);
System.out.println(late);// 23:59:59
DateTimeFormatter germanFormatter =
		DateTimeFormatter
				.ofLocalizedTime(FormatStyle.SHORT)
				.withLocale(Locale.GERMAN);

LocalTime leetTime = LocalTime.parse("13:37", germanFormatter);
System.out.println(leetTime);// 13:37
```

### LocalDate 本地日期

LocalDate 表示了一个确切的日期，比如 2014-03-11。该对象值是不可变的，用起来和LocalTime基本一致。下面的例子展示了如何给Date对象加减天/月/年。另外要注意的是这些对象是不可变的，操作返回的总是一个新实例。
代码如下:

```java
LocalDate today = LocalDate.now();
LocalDate tomorrow = today.plus(1, ChronoUnit.DAYS);
// 明天 2022-05-31
System.out.println(tomorrow);

LocalDate yesterday = tomorrow.minusDays(2);
// 前两天 2022-05-29
System.out.println(yesterday);

LocalDate independenceDay = LocalDate.of(2014, Month.JULY, 4);
// 获取相应的星期几 FRIDAY
DayOfWeek dayOfWeek = independenceDay.getDayOfWeek();
System.out.println(dayOfWeek);
```

从字符串解析一个LocalDate类型和解析LocalTime一样简单：
代码如下:

```java
DateTimeFormatter germanFormatter =
		DateTimeFormatter
    			.ofLocalizedDate(FormatStyle.MEDIUM)
				.withLocale(Locale.CHINESE);
LocalDate xmas = LocalDate.parse("2014-12-24", germanFormatter);
System.out.println(xmas); // 2014-12-24
```

### LocalDateTime 本地日期时间

LocalDateTime 同时表示了时间和日期，相当于前两节内容合并到一个对象上了。LocalDateTime和LocalTime还有LocalDate一样，都是不可变的。LocalDateTime提供了一些能访问具体字段的方法。
代码如下:

```java
LocalDateTime sylvester = LocalDateTime.of(2014, Month.DECEMBER, 31, 23, 59, 59);
DayOfWeek dayOfWeek = sylvester.getDayOfWeek();
System.out.println(dayOfWeek);// WEDNESDAY

Month month = sylvester.getMonth();
System.out.println(month);// DECEMBER

long minuteOfDay = sylvester.getLong(ChronoField.MINUTE_OF_DAY);
System.out.println(minuteOfDay);// 1439
```

只要附加上时区信息，就可以将其转换为一个时间点Instant对象，Instant时间点对象可以很容易的转换为老式的java.util.Date。
代码如下:

```java
Instant instant = sylvester
        .atZone(ZoneId.systemDefault())
        .toInstant();
Date legacyDate = Date.from(instant);
System.out.println(legacyDate);// Wed Dec 31 23:59:59 CET 2014
```

格式化LocalDateTime和格式化时间和日期一样的，除了使用预定义好的格式外，我们也可以自己定义格式：
代码如下:

```java
DateTimeFormatter formatter =
		DateTimeFormatter
				.ofPattern("yyyy/MM/dd HH:mm:ss");
LocalDateTime parsed = LocalDateTime.parse("2022/05/31 18:28:34", formatter);
System.out.println(parsed);// 2022-05-31T18:28:34
System.out.println(formatter.format(parsed));// 2022/05/31 18:28:34
```

## 十、Annotation 注解

在Java 8中支持多重注解了，先看个例子来理解一下是什么意思。
首先定义一个包装类Hints注解用来放置一组具体的Hint注解：
代码如下:

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Hints {
    Hint[] value();
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
// 可重复的注解
@Repeatable(Hints.class)
public @interface Hint {
    String value();
}
```

Java 8允许我们把同一个类型的注解使用多次，只需要给该注解标注一下@Repeatable即可。

### 例 1: 使用包装类当容器来存多个注解（老方法）

代码如下:

```java
@Hints({@Hint("one"), @Hint("two")})
class Bank {}
```

测试：

```java
Class<Bank> clazz = Bank.class;
Hint[] hints = clazz.getAnnotation(Hints.class).value();
for (Hint hint : hints) {
System.out.println(hint.value());// one two
}
```



### 例 2：使用多重注解（新方法）

代码如下:

```java
@Hint("three")
@Hint("four")
public class Building {}
```

测试：

```java
Class<Building> clazz = Building.class;
// Building类上没有Hints注解，也可以获取对应的注解
Hint[] hints = clazz.getAnnotation(Hints.class).value();
for (Hint hint : hints) {
    System.out.println(hint.value());// three four
}

// 通过getAnnotationsByType获取Hint注解
for (Hint hint : clazz.getAnnotationsByType(Hint.class)) {
    System.out.println(hint.value());// three four
}
```

即便我们没有在Person类上定义@Hints注解，我们还是可以通过 getAnnotation(Hints.class) 来获取 @Hints注解，更加方便的方法是使用 getAnnotationsByType 可以直接获取到所有的@Hint注解。
