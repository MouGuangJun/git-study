# Java设计模式

## 什么是设计模式

​        设计模式是一套经过反复使用的代码设计经验，目的是为了重用代码、让代码更容易被他人理解、保证代码可靠性。 设计模式于己于人于系统都是多赢的，它使得代码编写真正工程化，它是软件工程的基石，如同大厦的一块块砖石一样。项目中合理的运用设计模式可以完美的解决很多问题，每种模式在现实中都有相应的原理来与之对应，每种模式描述了一个在我们周围不断重复发生的问题，以及该问题的核心解决方案，这也是它能被广泛应用的原因。总体来说，设计模式分为三大类：

> - 创建型模式：共5种：工厂方法模式、抽象工厂模式、单例模式、建造者模式、原型模式
> - 结构型模式：共7种：适配器模式、装饰器模式、代理模式、桥接模式、外观模式、组合模式、享元模式
> - 行为型模式：共11种：策略模式、模板方法模式、观察者模式、责任链模式、访问者模式、中介者模式、迭代器模式、命令模式、状态模式、备忘录模式、解释器模式



## 设计模式的六大原则

**（1）开闭原则 (Open Close Principle) **：

​    开闭原则指的是对扩展开放，对修改关闭。在对程序进行扩展的时候，不能去修改原有的代码，想要达到这样的效果，我们就需要使用接口或者抽象类

**（2）依赖倒转原则 (Dependence Inversion Principle)**：

​    依赖倒置原则是开闭原则的基础，指的是针对接口编程，依赖于抽象而不依赖于具体

**（3）里氏替换原则 (Liskov Substitution Principle) **：

​    里氏替换原则是继承与复用的基石，只有当子类可以替换掉基类，且系统的功能不受影响时，基类才能被复用，而子类也能够在基础类上增加新的行为。所以里氏替换原则指的是任何基类可以出现的地方，子类一定可以出现。

​    里氏替换原则是对 “开闭原则” 的补充，实现 “开闭原则” 的关键步骤就是抽象化，而基类与子类的继承关系就是抽象化的具体实现，所以里氏替换原则是对实现抽象化的具体步骤的规范。

**（4）接口隔离原则 (Interface Segregation Principle)**：

​    使用多个隔离的接口，比使用单个接口要好，降低接口之间的耦合度与依赖，方便升级和维护方便

**（5）迪米特原则 (Demeter Principle)**：

​    迪米特原则，也叫最少知道原则，指的是一个类应当尽量减少与其他实体进行相互作用，使得系统功能模块相对独立，降低耦合关系。该原则的初衷是降低类的耦合，虽然可以避免与非直接的类通信，但是要通信，就必然会通过一个“中介”来发生关系，过分的使用迪米特原则，会产生大量的中介和传递类，导致系统复杂度变大，所以采用迪米特法则时要反复权衡，既要做到结构清晰，又要高内聚低耦合。

**（6）合成复用原则 (Composite Reuse Principle)**：

​    尽量使用组合/聚合的方式，而不是使用继承。



## 结构型模式

### 适配器模式

#### 定义

​	<font color='red'>适配器模式主要用于将一个类的接口转化成客户端希望的目标类格式，使得原本不兼容的类可以在一起工作，将目标类和适配者类解耦；同时也符合“开闭原则”，可以在不修改原代码的基础上增加新的适配器类；将具体的实现封装在适配者类中，对于客户端类来说是透明的，而且提高了适配者的复用性</font>，但是缺点在于更换适配器的实现过程比较复杂。



所以，适配器模式比较适合以下场景：

- （1）系统需要使用现有的类，而这些类的接口不符合系统的接口。
- （2）使用第三方组件，组件接口定义和自己定义的不同，不希望修改自己的接口，但是要使用第三方组件接口的功能。



下面两个非常形象的例子很好地说明了什么是适配器模式：

![img](../../md-photo/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2E3NDUyMzM3MDA=,size_16,color_FFFFFF,t_70-16596705422836.jpeg)



![img](../../md-photo/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2E3NDUyMzM3MDA=,size_16,color_FFFFFF,t_70-16596705492569.jpeg)



#### 实现方式

适配器模式主要分成三类：类的适配器模式、对象的适配器模式、接口的适配器模式。

##### 类的适配器模式

![img](../../md-photo/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2E3NDUyMzM3MDA=,size_16,color_FFFFFF,t_70-165967109259512.jpeg)

- 目标接口（Target）：客户所期待的接口。目标可以是具体的或抽象的类，也可以是接口。
- 需要适配的类（Adaptee）：需要适配的类或适配者类。
- 适配器（Adapter）：通过包装一个需要适配的对象，把原接口转换成目标接口。



```java
// 已存在的、具有特殊功能、但不符合我们既有的标准接口的类
class Adaptee {
    void specificRequest() {
        System.out.println("被适配类具有 特殊功能...");
    }
}
```



```java
// 目标接口，或称为标准接口
interface Target {
    void request();
}
```



```java
// 具体目标类，只提供普通功能
class ConcreteTarget implements Target {
    public void request() {
        System.out.println("普通类 具有 普通功能...");
    }
}
```



```java
// 适配器类，继承了被适配类，同时实现标准接口
class Adapter extends Adaptee implements Target {
    public void request() {
        super.specificRequest();
    }
}
```



测试：

```java
public static void main(String[] args) {
    // 请求自身普通功能
    ConcreteTarget concreteTarget = new ConcreteTarget();
    concreteTarget.request();

    // 请求目标特殊功能
    Adapter adapter = new Adapter();
    adapter.request();
}
```



结果：

```java
普通类 具有 普通功能...
被适配类具有 特殊功能...
```



##### 对象的适配器模式

![img](../../md-photo/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2E3NDUyMzM3MDA=,size_16,color_FFFFFF,t_70-165967152679115.jpeg)



Adaptee、ConcreteTarget、Target与类的适配器保持一致

修改Adapter为以下内容：

```java
// 适配器类，直接关联被适配类，同时实现标准接口
@Data
class Adapter implements Target {
    // 直接关联被适配类
    private Adaptee adaptee;

    // 可以通过构造函数传入具体需要适配的被适配类对象
    Adapter(Adaptee adaptee) {
        this.adaptee = adaptee;
    }

    // 这里是使用委托的方式完成特殊功能
    public void request() {
        adaptee.specificRequest();
    }
}
```



测试：

```java
public static void main(String[] args) {
    // 请求自身普通功能
    ConcreteTarget concreteTarget = new ConcreteTarget();
    concreteTarget.request();

    // 请求目标特殊功能
    Adapter adapter = new Adapter(new Adaptee());
    adapter.request();
}
```



结果：

```java
普通类 具有 普通功能...
被适配类具有 特殊功能...
```

​	测试结果与类的适配模型一致。从类图中我们也知道需要修改的只不过就是 Adapter 类的内部结构，即 Adapter 自身必须先拥有一个被适配类的对象，再把具体的特殊功能委托给这个对象来实现。使用对象适配器模式，可以使得 Adapter 类（适配类）根据传入的 Adaptee 对象达到适配多个不同被适配类的功能，当然，此时我们可以为多个被适配类提取出一个接口或抽象类。<font color='red'>这样看起来的话，似乎对象适配器模式更加灵活一点</font>。



##### 接口的适配器模式

​	有时我们写的一个接口中有多个抽象方法，<font color='red'>当我们写该接口的实现类时，必须实现该接口的所有方法，这明显有时比较浪费</font>，因为并不是所有的方法都是我们需要的，有时只需要某一些，此处为了解决这个问题，我们引入了接口的适配器模式，借助于一个抽象类，该抽象类实现了该接口，实现了所有的方法，而我们不和原始的接口打交道，只和该抽象类取得联系，所以我们写一个类，继承该抽象类，重写我们需要的方法就行。看一下类图：

![img](../../md-photo/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2E3NDUyMzM3MDA=,size_16,color_FFFFFF,t_70-165968019470618.png)



这个很好理解，在实际开发中，我们也常会遇到这种接口中定义了太多的方法，以致于有时我们在一些实现类中并不是都需要。看代码：

```java
public interface Sourceable {
    void method1();

    void method2();
}
```



抽象类Wrapper2：

```java
public abstract class Wrapper2 implements Sourceable {
    public void method1() {
    }

    public void method2() {
    }
}
```



```java
// 只需要实现method1接口
public class SourceSub1 extends Wrapper2 {
    @Override
    public void method1() {
        System.out.println("the sourceable interface's first Sub1!");
    }
}

// 只需要实现method2接口
public class SourceSub2 extends Wrapper2 {
    @Override
    public void method2() {
        System.out.println("the sourceable interface's first Sub2!");
    }
}
```



测试：

```java
public static void main(String[] args) {
    SourceSub1 sourceSub1 = new SourceSub1();
    SourceSub2 sourceSub2 = new SourceSub2();
    sourceSub1.method1();
    sourceSub1.method2();

    sourceSub2.method1();
    sourceSub2.method2();
}
```

结果：

```java
the sourceable interface's first Sub1!
the sourceable interface's first Sub2!
```



### 装饰者模式

#### 定义

​	当需要对类的功能进行拓展时，一般可以使用继承，但如果需要拓展的功能种类很繁多，那势必会生成很多子类，增加系统的复杂性，并且使用继承实现功能拓展时，我们必须能够预见这些拓展功能，也就是这些功能在编译时就需要确定了。那么有什么更好的方式实现功能的拓展吗？答案就是装饰器模式。

​	<font color='red'>装饰器模式可以动态给对象添加一些额外的职责从而实现功能的拓展，在运行时选择不同的装饰器，从而实现不同的行为；比使用继承更加灵活，通过对不同的装饰类进行排列组合，创造出很多不同行为，得到功能更为强大的对象；符合“开闭原则”，被装饰类与装饰类独立变化，用户可以根据需要增加新的装饰类和被装饰类，在使用时再对其进行组合，原有代码无须改变</font>。

​    但是装饰器模式也存在缺点，首先会产生很多的小对象，增加了系统的复杂性，第二是排错比较困难，对于多次装饰的对象，调试时寻找错误可能需要逐级排查，较为烦琐。

![img](../../md-photo/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2E3NDUyMzM3MDA=,size_16,color_FFFFFF,t_70-165968201560921.jpeg)





>- Component：抽象构件，是定义一个对象接口，可以给这个对象动态地添加职责。
>- ConcreteComponent：具体构件，是定义了一个具体的对象，也可以给这个对象添加一些职责。
>- Decorator：抽象装饰类，继承自 Component，从外类来扩展 Component 类的功能，但对于 Component 来说，是无需知道 Decorator 存在的。
>- ConcreteDecorator：具体装饰类，起到给 Component 添加职责的功能。



<font color='red'>**装饰者与被装饰者都拥有共同的超类，但这里继承的目的是继承类型，而不是行为。**</font>

#### 代码实现一

```java
// 定义被装饰者
public interface Human {
    void wearClothes();
}
```



```java
// 定时装饰者
public class Decorator implements Human {
    private Human human;

    public Decorator(Human human) {
        this.human = human;
    }

    public void wearClothes() {
        human.wearClothes();
    }
}
```



```java
// 下面定义三种装饰，这是第一个，第二个第三个功能依次细化，即装饰者的功能越来越多
public class Decorator_zero extends Decorator {

    public Decorator_zero(Human human) {
        super(human);
    }

    public void goHome() {
        System.out.println("进房子。。");
    }

    @Override
    public void wearClothes() {
        super.wearClothes();
        goHome();
    }
}

public class Decorator_first extends Decorator {
    public Decorator_first(Human human) {
        super(human);
    }

    public void goClothespress() {
        System.out.println("去衣柜找找看。。");
    }

    @Override
    public void wearClothes() {
        super.wearClothes();
        goClothespress();
    }
}

public class Decorator_two extends Decorator {

    public Decorator_two(Human human) {
        super(human);
    }

    public void findClothes() {
        System.out.println("找到一件D&G。。");
    }

    @Override
    public void wearClothes() {
        super.wearClothes();
        findClothes();
    }
}
```



```java
// 被装饰者person
public class Person implements Human {
    public void wearClothes() {
        System.out.println("穿什么呢。。");
    }
}
```



测试结果：

```java
public static void main(String[] args) {
    Person person = new Person();
    Decorator decorator = new Decorator_two(new Decorator_first(new Decorator_zero(person)));
    decorator.wearClothes();
}
```



结果：

```java
穿什么呢。。
进房子。。
去衣柜找找看。。
找到一件D&G。。
```

 其实就是进房子找衣服，通过装饰者的三层装饰，把细节变得丰富，该Demo的关键点：

（1）Decorator 抽象类持有Human接口，方法全部委托给该接口调用，目的是交给该接口的实现类进行调用。

（2）Decorator 抽象类的子类，即具体装饰者，里面都有一个构造方法调用 super(human)，这里就体现了抽象类依赖于子类实现，即抽象依赖于实现的原则。因为构造函数的参数都是 Human 类型，只要是该 Human 的实现类都可以传递进去，即表现出 Decorator dt = new Decorator_second(new Decorator_first(new Decorator_zero(human))) 这种结构的样子，所以当调用 dt.wearClothes() 的时候，又因为每个具体装饰者类中，都先调用 super.wearClothes() 方法，<font color='red'>而该 super 已经由构造函数传递并指向了具体的某一个装饰者类，那么最终调用的就是装饰类的方法，然后才调用自身的装饰方法，即表现出一种装饰、链式的类似于过滤的行为</font>。

（3）具体被装饰者类，可以定义初始的状态或者初始的自己的装饰，后面的装饰行为都在此基础上一步一步进行点缀、装饰。

（4）装饰者模式的设计原则为：<font color='red'>对扩展开放、对修改关闭，这句话体现在如果想扩展被装饰者类的行为，无须修改装饰者抽象类，只需继承装饰者抽象类，实现额外的一些装饰或者叫行为即可对被装饰者进行包装</font>。所以：扩展体现在继承、修改体现在子类中，而不是具体的抽象类，这充分体现了依赖倒置原则，这是自己理解的装饰者模式。



#### 代码实现二

现在需要一个汉堡，主体是鸡腿堡，可以选择添加生菜、酱、辣椒等等许多其他的配料，这种情况下就可以使用装饰者模式。

```java
// 汉堡基类 被装饰者
public interface Humburger {
    String getName();// 名称

    double getPrice();// 价格
}
```



```java
// 鸡腿堡类（被装饰者的初始状态，有些自己的简单装饰）
public class ChickenBurger implements Humburger {
    public String getName() {
        return "鸡腿堡";
    }

    public double getPrice() {
        return 10;
    }
}
```



```java
// 配料的基类（装饰者，用来对汉堡进行多层装饰，每层装饰增加一些配料）
public class Condiment implements Humburger {
    private Humburger humburger;

    public Condiment(Humburger humburger) {
        this.humburger = humburger;
    }

    public String getName() {
        return humburger.getName();
    }

    public double getPrice() {
        return humburger.getPrice();
    }
}
```



```java
// 生菜（装饰者的第一层）
public class Lettuce extends Condiment {
    public Lettuce(Humburger humburger) {
        super(humburger);
    }

    public String getName() {
        return super.getName() + " 加生菜";
    }

    public double getPrice() {
        return super.getPrice() + 1.5;
    }
}
```



```java
// 辣椒（装饰者的第二层）
public class Chilli extends Condiment {
    public Chilli(Humburger humburger) {
        super(humburger);
    }

    @Override
    public String getName() {
        return super.getName() + " 加辣椒";
    }

    @Override
    public double getPrice() {
        return super.getPrice();// 辣椒是免费的哦
    }
}
```



测试：

```java
public static void main(String[] args) {
    // 普通的汉堡
    Humburger humburger = new ChickenBurger();
    System.out.println(humburger.getName() + "  价钱：" + humburger.getPrice());

    // 加生菜
    Lettuce lettuce = new Lettuce(humburger);
    System.out.println(lettuce.getName() + "  价钱：" + lettuce.getPrice());

    // 加辣椒
    Chilli chilli = new Chilli(humburger);
    System.out.println(chilli.getName() + "  价钱：" + chilli.getPrice());

    // 先加生菜再加辣椒
    Chilli chilli1 = new Chilli(lettuce);
    System.out.println(chilli1.getName() + "  价钱：" + chilli1.getPrice());
}
```



结果：

```java
鸡腿堡  价钱：10.0
鸡腿堡 加生菜  价钱：11.5
鸡腿堡 加辣椒  价钱：10.0
鸡腿堡 加生菜 加辣椒  价钱：11.5
```



#### 缺点

但是装饰器模式也存在缺点，首先会产生很多的小对象，增加了系统的复杂性，第二是排错比较困难，对于多次装饰的对象，调试时寻找错误可能需要逐级排查，较为烦琐。



### 代理模式

​	*我们一般在租房子时会去找中介，为什么呢？因为你对该地区房屋的信息掌握的不够全面，希望找一个更熟悉的人去帮你做；再比如我们打官司需要请律师，因为律师在法律方面有专长，可以替我们进行操作，表达我们的想法；再比如在淘宝上面买东西，你使用支付宝平台支付，卖家请物流公司发货，在这个过程汇总支付宝、物流公司都扮演者“第三者”的角色在帮你完成物品的购买，这里的第三者我们可以将其称之为代理者，在我们实际生活中这种代理情况无处不在！*

#### 定义

通过上面的例子，我们可以很清楚地理解什么是代理模式：

​    <font color='red'>代理模式的设计动机是通过代理对象来访问真实对象，通过建立一个对象代理类，由代理对象控制原对象的引用，从而实现对真实对象的操作。在代理模式中，代理对象主要起到一个中介的作用，用于协调与连接调用者(即客户端)和被调用者(即目标对象)，在一定程度上降低了系统的耦合度，同时也保护了目标对象。但缺点是在调用者与被调用者之间增加了代理对象，可能会造成请求的处理速度变慢，</font>



![img](../../../md-photo/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2E3NDUyMzM3MDA=,size_16,color_FFFFFF,t_70.jpeg)



>- Subject：抽象角色，声明了真实对象和代理对象的共同接口；
>- Proxy：代理角色，实现了与真实对象相同的接口，所以在任何时刻都能够代理真实对象，并且代理对象内部包含了真实对象的引用，所以它可以操作真实对象，同时也可以附加其他的操作，相当于对真实对象进行封装。
>- RealSubject：真实对象，是我们最终要引用的对象。



#### 代码实现

某天你看到一位女生，一见钟情，心里发誓要她做你女朋友，但是你想这样直接上去可能会唐突了。于是你采用迂回政策，先和她室友搞好关系，然后通过她室友给她礼物，然后……。

首先出现的就是美女一枚：

```java
// 首先出现的就是美女一枚
@Data
@AllArgsConstructor
public class BeautifulGirl {
    private String name;
}
```



 然后是抽象主题，送礼物接口：

```java
// 然后是抽象主题，送礼物接口
public interface GiveGift {
    /**
     * 送花
     */
    void giveFlowers();

    /**
     * 送巧克力
     */
    void giveChocolate();

    /**
     * 送书
     */
    void giveBook();
}
```



你小子：

```java
// 你小子
public class You implements GiveGift {
    private BeautifulGirl mm;

    public You(BeautifulGirl mm) {
        this.mm = mm;
    }

    public void giveBook() {
        System.out.println(mm.getName() + ",送你一本书....");
    }

    public void giveChocolate() {
        System.out.println(mm.getName() + ",送你一盒巧克力....");
    }

    public void giveFlowers() {
        System.out.println(mm.getName() + ",送你一束花....");
    }
}
```



 她闺蜜室友：

```java
// 她闺蜜室友
public class HerChum implements GiveGift {
    private You you;

    public HerChum(You you) {
        this.you = you;
    }

    public void giveFlowers() {
        you.giveFlowers();
        System.out.println("分我一朵先！");
    }

    public void giveChocolate() {
        you.giveChocolate();
        System.out.println("巧克力？拿来吧你！");
    }

    public void giveBook() {
        you.giveBook();
        System.out.println("学习？学个屁！");
    }
}
```



测试：

```java
public static void main(String[] args) {
    BeautifulGirl beautifulGirl = new BeautifulGirl("漂亮妹妹");
    HerChum herChum = new HerChum(new You(beautifulGirl));
    herChum.giveFlowers();
    herChum.giveChocolate();
    herChum.giveBook();
}
```



结果：

```java
漂亮妹妹,送你一束花....
分我一朵先！
漂亮妹妹,送你一盒巧克力....
巧克力？拿来吧你！
漂亮妹妹,送你一本书....
学习？学个屁！
```



#### 代理模式与装饰器模式的区别

代理模式跟装饰器模式比较类似，有些读者可能容易将他们混淆，这里我们简单介绍下两者的区别：

- （1）装饰器模式关注于在对象上动态添加新行为，而代理模式虽然也可以增加新的行为，但关注于控制对象的访问。
- （2）装饰器模式执行主体是原类；代理模式是代理原类进行操作，执行主体是代理类。
- （3）代理模式中，代理类可以对客户端隐藏真实对象的具体信息，因此使用代理模式时，我们常常在代理类中创建真实对象的实例。而装饰器模式的通常做法是将原始对象作为参数传给装饰者的构造器。也就是说。代理模式的代理和真实对象之间的对象通常在编译时就已经确定了，而装饰者能够在运行时递归地被构造。



### 桥接模式

#### 定义

​	桥接，顾名思义，就是用来连接两个部分，使得两个部分可以互相通讯，桥接模式的作用就是为被分离的抽象部分和实现部分搭桥。在现实生活中一个物品在搭配不同的配件时会产生不同的动作和结果，例如一辆赛车搭配的是硬胎或者是软胎就能够在干燥的马路上行驶，而如果要在下雨的路面行驶，就需要搭配雨胎了，这种根据行驶的路面不同，需要搭配不同的轮胎的变化的情况，我们从软件设计的角度来分析，就是一个系统由于自身的逻辑，会有两个或多个维度的变化，而为了应对这种变化，我们就可以使用桥接模式来进行系统的解耦。 桥接模式将一个系统的抽象部分和实现部分分离，使它们都可以独立地进行变化，对应到上面就是赛车的种类可以相对变化，轮胎的种类可以相对变化，形成一种交叉的关系，最后的结果就是一种赛车对应一种轮胎就能够成功产生一种结果和行为。 

​	<font color='red'>桥接模式将系统的抽象部分与实现部分分离解耦，使他们可以独立的变化。为了达到让抽象部分和实现部分独立变化的目的，桥接模式使用组合关系来代替继承关系，抽象部分拥有实现部分的接口对象，从而能够通过这个接口对象来调用具体实现部分的功能。也就是说，桥接模式中的桥接是一个单方向的关系，只能够抽象部分去使用实现部分的对象，而不能反过来。 </font>

​    <font color='red'>桥接模式符合“开闭原则”，提高了系统的可拓展性，在两个变化维度中任意扩展一个维度，都不需要修改原来的系统；并且实现细节对客户不透明，可以隐藏实现细节。但是由于聚合关系建立在抽象层，要求开发者针对抽象进行编程，这增加系统的理解和设计难度。</font>

​    所以，桥接模式一般适用于以下几种应用场景：

> （1）系统需要在构件的抽象化角色和具体化角色之间增加更多的灵活性，避免在两个层次之间建立静态的继承联系，则可以通过桥接模式使他们在抽象层建立一个关联关系；
> （2）系统不希望使用继承或因为多层次继承导致系统类的个数急剧增加时
> （3）一个类存在两个独立变化的维度，而这两个维度都需要进行扩展。



![img](../../../md-photo/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2E3NDUyMzM3MDA=,size_16,color_FFFFFF,t_70-16596875765123.png)



> - 抽象化角色 Abstraction：定义抽象的接口，包含一个对实现化角色的引用，抽象角色的方法需要调用实现化角色；
> - 扩展抽象化角色 RefinedAbstraction：抽象化角色的子类，一般对抽象部分的方法进行完善和扩展，实现父类中的业务方法，并通过组合/聚合关系调用实现化角色中的业务方法
> - 实现化角色 Implementor：定义具体行为、具体特征的应用接口，供扩展抽象化角色使用，一般情况下是由实现化角色提供基本的操作，而抽象化角色定义基于实现部分基本操作的业务方法；
> - 具体实现化角色 ConcreteImplementor：完善实现化角色中定义的具体逻辑。



#### 代码实现

Implementor 接口类：

```java
public interface Implementor {
    void operationImpl();
}
```



ConcreteImplementor 接口实现类：

```java
public class ConcreteImplementorA implements Implementor {
    public void operationImpl() {
        System.out.println("ConcreteImplementorA的业务逻辑实现...");
    }
}

public class ConcreteImplementorB implements Implementor {
    public void operationImpl() {
        System.out.println("ConcreteImplementorB的业务逻辑实现...");
    }
}
```



Abstraction 抽象类：

```java
public class Abstraction {
    private Implementor implementor;

    public Abstraction(Implementor implementor) {
        this.implementor = implementor;
    }

    public void operation() {
        implementor.operationImpl();
    }
}
```



RefinedAbstraction 抽象类的具体实现：

```java
public class RefinedAbstractionA extends Abstraction {
    public RefinedAbstractionA(Implementor implementor) {
        super(implementor);
    }

    public void refinedOperation() {
        super.operation();
        System.out.println("RefinedAbstractionA 对 Abstraction 中的 operation 方法进行扩展");
    }
}

public class RefinedAbstractionB extends Abstraction {
    public RefinedAbstractionB(Implementor implementor) {
        super(implementor);
    }

    public void refinedOperation() {
        super.operation();
        System.out.println("RefinedAbstractionB 对 Abstraction 中的 operation 方法进行扩展");
    }
}
```



测试：

```java
public static void main(String[] args) {
    ConcreteImplementorA implementorA = new ConcreteImplementorA();
    RefinedAbstractionA refinedAbstractionA = new RefinedAbstractionA(implementorA);
    refinedAbstractionA.refinedOperation();

    RefinedAbstractionB refinedAbstractionB = new RefinedAbstractionB(implementorA);
    refinedAbstractionB.refinedOperation();
}
```



结果：

```java
ConcreteImplementorA的业务逻辑实现...
RefinedAbstractionA 对 Abstraction 中的 operation 方法进行扩展
ConcreteImplementorA的业务逻辑实现...
RefinedAbstractionB 对 Abstraction 中的 operation 方法进行扩展
```

​	

​	看了这段通用代码之后，桥接模式的结构应该就很清楚了，需要注意的是 RefinedAbstraction 根据实际情况是可以有多个的。 当然上面的 UML 类图和通用代码只是最常用的实现方式而已，在实际使用中可能会有其他的情况，比如 Implementor 只有一个类的情况，虽然这时候可以不去创建 Implementor 接口，精简类的层次，但是我建议还是需要抽象出实现部分的接口。
