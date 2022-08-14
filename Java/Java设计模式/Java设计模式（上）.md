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



## 创建型模式

### 简单工厂模式

<font color='red'>**建立一个工厂类，并定义一个接口对实现了同一接口的产品类进行创建**</font>

#### 示例

![image](../../../md-photo/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBAc2hpbmUgc3Vu,size_20,color_FFFFFF,t_70,g_se,x_16.png)

工厂：

```java
public class TVFactory {

    public static TV produceTV(String brand) throws Exception {
        if (brand.equalsIgnoreCase("Haier")) {
            System.out.println("电视机工厂生产海尔电视机！");
            return new HaierTV();
        } else if (brand.equalsIgnoreCase("Hisense")) {
            System.out.println("电视机工厂生产海信电视机！");
            return new HisenseTV();
        } else {
            throw new Exception("对不起，暂不能生产该品牌电视机！");
        }
    }
}
```



不同品牌电视机：

```java
public class HisenseTV implements TV {
    public void play() {
        System.out.println("海信电视机播放中......");
    }
}

public class HaierTV implements TV {
    public void play() {
        System.out.println("海尔电视机播放中......");
    }
}
```



电视机：

```java
public interface TV {
    void play();
}
```



#### 优点

1. 对象创建和使用的分离
2. 客户端无须知道所创建的具体产品类的类名，只需要知道具体产品类所对应的参数即可
3. 通过引入配置文件，可以在丌修改任何客户端代码的情况下更换和增加新的具体产品类，在一定程度上提高了系统的灵活性



#### 缺点

1. 工厂类职责过重；
2. 增加系统中类的个数
3. 系统扩展困难，一旦添加新产品不得不修改工厂逻辑 （违背开闭原则）
4. 由于使用了静态工厂方法，造成工厂角色无法形成基于继承的等级结构，工厂类丌能得到很好地扩展



#### 适用场景

1. 工厂类负责创建的对象比较少，由于创建对象比较少，不会造成工厂方法中的逻辑太过复杂。
2. 客户端只知道传入工厂类的参数，对于如何创建对象不关心：客户端既不需要关心创建细节，甚至于连类名都不需要记住，只需要知道类型所对应的参数即可。



### 工厂模式

解决简单工厂模式的不足，工厂类职责过重，不利于扩展的缺点。

工厂父类负责定义创建产品对象的公共接口， 而工厂子类则负责生成具体的产品对象。
是将产品类的实例化操作延迟到工厂子类中完成，即<font color='red'>**通过工厂子类来确定究竟应该实例化哪一个具体产品类**</font>。

#### 模式结构

![image](../../../md-photo/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBAc2hpbmUgc3Vu,size_20,color_FFFFFF,t_70,g_se,x_16-16594557307083.png)



#### 示例

工厂：

```java
public interface Factory {
    Product createProduct();
}

public class FoodFactory implements Factory {
    public Product createProduct() {
        return new Food();
    }
}

public class ProgramFactory implements Factory {
    public Product createProduct() {
        return new Program();
    }
}
```



产品:

```java
public interface Product {
    void show();
}

public class Food implements Product {
    public void show() {
        System.out.println("食物产品......");
    }
}

public class Program implements Product {
    public void show() {
        System.out.println("程序产品......");
    }
}

```



#### 模式分析

- 工厂方法模式是简单工厂模式的迚一步抽象和推广
- 工厂方法模式保持了简单工厂模式的优点，并克服了它的缺点
- 核心的工厂类不再负责所有产品的创建，而是将具体创建工作交 给其子类去完成
- 可以允许系统在丌修改工厂角色的情况下引迚新产品
- 增加具体产品–>增加具体工厂，符合“开闭原则”



#### 优点

1. 向客户隐藏了哪种具体产品类将被实例化这一细节
2. 让工厂自主确定创建何种产品对象
3. 加入新产品时，完全符合开闭原则



#### 缺点

1. 类的个数将成对增加
2. 增加了系统的抽象性和理解难度



#### 适用场景

1. 客户端不知道它所需要的对象的类
2. 抽象工厂类通过其子类来指定创建哪个对象



### 抽象工厂模式

<font color='red'>**抽象工厂模式可以看成是工厂方法模式的一种推广**</font>

需要一个工厂可以生产多个产品对象。
**产品等级结构**：产品等级结构即产品的继承结构
**产品族**：在抽象工厂模式中，产品族是指由同一个工厂生产的，
位于不同产品等级结构中的一组产品。

![image](../../../md-photo/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBAc2hpbmUgc3Vu,size_20,color_FFFFFF,t_70,g_se,x_16.png)

#### 模式结构

![image](../../../md-photo/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBAc2hpbmUgc3Vu,size_20,color_FFFFFF,t_70,g_se,x_16-16594881427703.png)



#### 模式实例

计算机包含内存（RAM），CPU等硬件设备，根据如图所示的“产品等级结构-产品族示意图”，使用抽象工厂模式实现计算机设备创建过程并绘制类图。

![image](../../../md-photo/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBAc2hpbmUgc3Vu,size_20,color_FFFFFF,t_70,g_se,x_16-16594881656926.png)

根据题意，明白相应的产品族与产品等级结构，建立相应的抽象类，同一产品族的实现共同工厂类，同一产品等级结构继承共同的产品类。画出相应的类图，写出相应的代码。

![image](../../../md-photo/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBAc2hpbmUgc3Vu,size_20,color_FFFFFF,t_70,g_se,x_16-16594881890589.png)



工厂：

```java
public interface Factory {
    CPU produceCPU();

    RAM produceRAM();
}

public class MacFactory implements Factory {
    public CPU produceCPU() {
        return new MacCPU();
    }

    public RAM produceRAM() {
        return new MacRAM();
    }
}

public class PcFactory implements Factory {
    public CPU produceCPU() {
        return new PcCPU();
    }

    public RAM produceRAM() {
        return new PcRAM();
    }
}
```



CPU：

```java
public interface CPU {
    void use();
}

public class MacCPU implements CPU {
    public void use() {
        System.out.println("正在使用MacCPU......");
    }
}

public class PcCPU implements CPU {
    public void use() {
        System.out.println("正在使用PcCPU......");
    }
}
```



RAM：

```java
public interface RAM {
    void stroe();
}

public class MacRAM implements RAM {
    public void stroe() {
        System.out.println("使用MacRAM存储......");
    }
}

public class PcRAM implements RAM {
    public void stroe() {
        System.out.println("使用PcRAM存储......");
    }
}
```



测试：

```java
Factory macFactory = new MacFactory();
CPU macCPU = macFactory.produceCPU();
macCPU.use();
RAM macRAM = macFactory.produceRAM();
macRAM.stroe();


PcFactory pcFactory = new PcFactory();
CPU pcCPU = pcFactory.produceCPU();
pcCPU.use();
RAM pcRAM = pcFactory.produceRAM();
pcRAM.stroe();
```



#### 优点

1. 隔离了具体类的生成
2. 能够保证客户端始终只使用同一个产品族中的对象
3. 增加新的产品族很方便，无须修改已有系统，符合开闭原则



#### 缺点

增加新的**产品等级结构**麻烦，需要对原有系统进行较大的修改，甚至需要修改抽象层代码，这显然会带来较大的不便，**违背了开闭原则**



#### 适用场景

1. 一个系统不应当依赖于产品类实例如何被创建、组合和表达的细节。
2. 系统中有多于一个的产品族，但每次只使用其中某一产品族。
3. 属于同一个产品族的产品将在一起使用，这一约束必须在系统的设计中体现出来。
4. 产品等级结构稳定，在设计完成之后不会向系统中增加新的产品等级结构或者删除已有的产品等级结构。



### 三种工厂模式的比较

**相同：**
三个设计模式名字中都含有“工厂”二字，其含义是使用工厂（一个或一系列方法）去生产产品（一个或一系列类的实例）。



**不同：**

| 项目         | 简单工厂                                                     | 工厂模式                                                     | 抽象工厂模式                                                 |
| ------------ | ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ |
| 工厂类的实现 | 拥有一个工厂方法（create），接受了一个参数，通过不同的参数实例化不同的产品类 | 工厂方法针对每一种产品提供一个工厂类。通过不同的工厂实例来创建不同的产品实例 | 抽象工厂对应的是产品族概念。                                 |
| 优点         | 很明显，简单工厂的特点就是“简单粗暴”，通过一个含参的工厂方法，我们可以实例化任何产品类，上至飞机火箭，下至土豆面条，无所不能。所以简单工厂有一个别名：上帝类。 | 1.工厂方法模式就很好的减轻了工厂类的负担，把某一类/某一种东西交由一个工厂生产；（对应简单工厂的缺点1）<br /> 2.同时增加某一类”东西“并不需要修改工厂类，只需要添加生产这类”东西“的工厂即可，使得工厂类符合开放-封闭原则。 | 针对产品族场景，实现比较工整                                 |
| 缺点         | 1任何“东西”的子类都可以被生产，负担太重。当所要生产产品种类非常多时，工厂方法的代码量可能会很庞大。<br />2.在遵循开闭原则（对拓展开放，对修改关闭）的条件下，简单工厂对于增加新的产品，无能为力。因为增加新产品只能通过修改工厂方法来实现。 | 1.相比简单工厂，实现略复杂。<br />2.对于某些可以形成产品族的情况处理比较复杂 | 比较复杂，只适用于产品族场景                                 |
| 适用的场景   | 用于生产同一等级结构中的任意产品（不支持拓展增加产品）       | 用于生产同一等级结构中的固定产品（支持拓展增加产品）         | 用于生产不同产品族的全部产品（不支持拓展增加产品；支持增加产品族） |



### 建造者模式

#### 定义

​	建造者模式<font color='red'>将复杂产品的创建步骤分解在在不同的方法中</font>，使得创建过程更加清晰，从而更精确控制复杂对象的产生过程；通过隔离复杂对象的构建与使用，也就是将产品的创建与产品本身分离开来，使得<font color='red'>同样的构建过程可以创建不同的对象</font>；并且每个具体建造者都相互独立，因此可以很方便地替换具体建造者或增加新的具体建造者，<font color='red'>用户使用不同的具体建造者即可得到不同的产品对象</font>。

![img](../../../md-photo/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2E3NDUyMzM3MDA=,size_16,color_FFFFFF,t_70.jpeg)



#### 示例

套餐类：

```java
@Data
public class Meal {
    private String food;
    private String drink;
}
```



套餐构造器：

```java
public abstract class MealBuilder {
    protected Meal meal = new Meal();

    public abstract void buildFood();

    public abstract void buildDrink();

    public Meal getMeal() {
        return meal;
    }
}
```



套餐A、套餐B。这个两个套餐都是实现抽象套餐类：

```java
public class MealA extends MealBuilder {
    public void buildFood() {
        meal.setFood("一盒薯条");
    }

    public void buildDrink() {
        meal.setDrink("一杯可乐");
    }
}
```



```java
public class MealB extends MealBuilder {
    public void buildFood() {
        meal.setFood("三个鸡翅");
    }

    public void buildDrink() {
        meal.setDrink("一杯柠檬果汁");
    }
}
```



 最后是KFC的服务员，它相当于一个指挥者，它决定了套餐是的实现过程，然后给你一个完美的套餐：

```java
@Data
public class KFCWaiter {
    private MealBuilder mealBuilder;

    public Meal construct() {
        //准备食物
        mealBuilder.buildFood();
        //准备饮料
        mealBuilder.buildDrink();

        //准备完毕，返回一个完整的套餐给客户
        return mealBuilder.getMeal();
    }
}
```



测试类：

```java
public static void main(String[] args) {
    //服务员
    KFCWaiter waiter = new KFCWaiter();
    //套餐A
    MealA a = new MealA();
    //服务员准备套餐A
    waiter.setMealBuilder(a);
    //获得套餐
    Meal mealA = waiter.construct();

    System.out.print("套餐A的组成部分:");
    System.out.println(mealA.getFood() + "---" + mealA.getDrink());
}
```



#### 抽象工厂模式和建造者模式的区别

​    **两者都是创建型模式，并且最终都是得到一个产品**，但两者的区别在于：

<font color='red'>1、抽象工厂模式实现对产品族的创建，产品族指的是不同分类维度的产品组合，用抽象工厂模式不需要关心具体构建过程，只关心产品由什么工厂生产即可。而建造者模式则更关心的是对象的构建过程，要求按照指定的蓝图建造产品，主要目的是通过组装零配件而产生一个新产品。</font>

<font color='red'>2、在抽象工厂模式中使用“工厂”来描述构建者，而在建造者模式中使用“车间”来描述构建者。</font>

*（1）抽象工厂模式就好比是一个一个的工厂，宝马车工厂生产宝马SUV和宝马VAN，奔驰车工厂生产奔驰车SUV和奔驰VAN，它是从一个更高层次去看对象的构建，具体到工厂内部还有很多的车间，如制造引擎的车间、装配引擎的车间等，但这些都是隐藏在工厂内部的细节，对外不公布。也就是对领导者来说，他只要关心一个工厂到底是生产什么产品的，不用关心具体怎么生产。*

*（2）建造者模式就不同了，它是由车间组成，不同的车间完成不同的创建和装配任务，一个完整的汽车生产过程需要引擎制造车间、引擎装配车间的配合才能完成，它们配合的基础就是设计蓝图，而这个蓝图是掌握在车间主任（Director类）手中，它给建造车间什么蓝图就能生产什么产品，建造者模式更关心建造过程。虽然从外界看来一个车间还是生产车辆，但是这个车间的转型是非常快的，只要重新设计一个蓝图，即可产生不同的产品，这有赖于建造者模式的功劳。*

*（3）相对来说，抽象工厂模式比建造者模式的粒度要大，它关注产品整体，而建造者模式关注构建过程，所以建造者模式可以很容易地构建出一个崭新的产品，只要指挥类 Director 能够提供具体的工艺流程。也正因为如此，两者的应用场景截然不同，如果希望屏蔽对象的创建过程，只提供一个封装良好的对象，则可以选择抽象工厂方法模式。而建造者模式可以用在构件的装配方面，如通过装配不同的组件或者相同组件的不同顺序，可以产生出一个新的对象，它可以产生一个非常灵活的架构，方便地扩展和维护系统。*



#### 缺点

​	但建造者模式的缺陷是要求创建的<font color='red'>产品具有较多的共同点、组成部分相似，如果产品之间的差异性很大，则不适合使用建造者模式</font>。同时如果产品的内部变化复杂，可能会导致需要定义很多具体建造者类来实现这种变化，导致系统变得很庞大。



### 单例模式

#### 定义

<font color='red'>单例模式可以确保系统中某个类只有一个实例，该类自行实例化并向整个系统提供这个实例的公共访问点，除了该公共访问点，不能通过其他途径访问该实例。单例模式的优点在于：</font>

- <font color='red'>系统中只存在一个共用的实例对象，无需频繁创建和销毁对象，节约了系统资源，提高系统的性能。</font>
- <font color='red'>可以严格控制客户怎么样以及何时访问单例对象。</font>



单例模式有以下特点：

- （1）单例类只能有一个实例；
- （2）单例类必须自己创建自己的唯一实例；
- （3）单例类必须给所有其他对象提供这一实例。

*在计算机系统中，线程池、缓存、日志对象、对话框、打印机、显卡的驱动程序对象常被设计成单例，这些应用都或多或少具有资源管理器的功能。每台计算机可以有若干个打印机，但只能有一个Printer Spooler，以避免两个打印作业同时输出到打印机中。每台计算机可以有若干通信端口，系统应当集中管理这些通信端口，以避免一个通信端口同时被两个请求同时调用。总之，选择单例模式就是为了避免不一致状态，避免政出多头。*



#### 示例

##### 懒汉式单例

```java
public class Singleton1 {
    private Singleton1() {
    }
	
	//懒汉式单例类.在第一次调用的时候实例化自己 
    private static Singleton1 singleton1 = null;
    //静态工厂方法 
    public static Singleton1 getInstance() {
        if (singleton1 == null) {
            singleton1 = new Singleton1();
        }

        return singleton1;
    }
}
```

Singleton 通过私有化构造函数，避免类在外部被实例化，而且只能通过 getInstance() 方法获取 Singleton 的唯一实例。但是以上懒汉式单例的实现是线程不安全的，在并发环境下可能出现多个 Singleton 实例的问题。要实现线程安全，有以下三种方式，都是对 getInstance() 这个方法改造，保证了懒汉式单例的线程安全：

> 线程安全：如果程序每次运行结果都和单线程运行的结果是一样的，而且其他的变量的值也和预期的是一样的，就是线程安全的。

1、在 getInstance() 方法上加同步机制：

```java
public synchronized static Singleton1 getInstance() {
    if (singleton1 == null) {
        singleton1 = new Singleton1();
    }

    return singleton1;
}
```

在方法调用上加了同步，虽然线程安全了，但是每次都要同步，会影响性能，毕竟99%的情况下是不需要同步的。



##### 双重检查锁定

```java
// 懒汉式单例类.在第一次调用的时候实例化自己
public class Singleton2 {
    private Singleton2() {
    }

    // volatile防止指令重排
    // 先执行singleton2 = new Singleton2()的操作导致另一个线程获取到了singleton2但值为空
    private volatile static Singleton2 singleton2 = null;

    public static Singleton2 getInstance() {
        if (singleton2 == null) {
            synchronized (Singleton2.class) {
                if (singleton2 == null) {
                    singleton2 = new Singleton2();
                }
            }
        }

        return singleton2;
    }
}
```

（1）为什么 getInstance() 方法内需要使用两个 if (singleton == null) 进行判断呢？

假设高并发下，线程A、B 都通过了第一个 if 条件。若A先抢到锁，new 了一个对象，释放锁，然后线程B再抢到锁，此时如果不做第二个 if 判断，B线程将会再 new 一个对象。使用两个 if 判断，确保了只有第一次调用单例的时候才会做同步，这样也是线程安全的，同时避免了每次都同步的性能损耗。

（2）volatile 关键字的作用？

volatile 的作用主要是禁止指定重排序。假设在不使用 volatile 的情况下，两个线程A、B，都是第一次调用该单例方法，线程A先执行 singleton = new Singleton()，但由于构造方法不是一个原子操作，编译后会生成多条字节码指令，由于 JAVA的 指令重排序，可能会先执行 singleton 的赋值操作，该操作实际只是在内存中开辟一片存储对象的区域后直接返回内存的引用，之后 singleton 便不为空了，但是实际的初始化操作却还没有执行。如果此时线程B进入，就会拿到一个不为空的但是没有完成初始化的singleton 对象，所以需要加入volatile关键字，禁止指令重排序优化，从而安全的实现单例。



##### 静态内部类

```java
public class Singleton3 {
    private Singleton3() {
    }

    public static class LazyHolder {
        private static final Singleton3 INSTANCE = new Singleton3();
    }

    public static Singleton3 getInstance() {
        return LazyHolder.INSTANCE;
    }
}
```

利用了类加载机制来保证初始化 instance 时只有一个线程，所以也是线程安全的，同时没有性能损耗，这种比上面1、2都好一些，既实现了线程安全，又避免了同步带来的性能影响。



##### 饿汉式单例

```java
// 饿汉式单例类.在类初始化时，已经自行实例化
public class Singleton4 {
    private Singleton4() {
    }

    private static final Singleton4 singleton4 = new Singleton4();

    // 静态工厂方法
    public static Singleton4 getInstance() {
        return singleton4;
    }
}
```

饿汉式在类创建的同时就已经创建好一个静态的对象供系统使用，以后不再改变，所以天生是线程安全的。



> <font color='blue'>**饿汉式和懒汉式区别：**</font>
>
> （1）初始化时机与首次调用：
>
> - 饿汉式是在类加载时，就将单例初始化完成，保证获取实例的时候，单例是已经存在的了。所以在第一次调用时速度也会更快，因为其资源已经初始化完成。
> - 懒汉式会延迟加载，只有在首次调用时才会实例化单例，如果初始化所需要的工作比较多，那么首次访问性能上会有些延迟，不过之后就和饿汉式一样了。
>
> （2）线程安全方面：饿汉式天生就是线程安全的，可以直接用于多线程而不会出现问题，懒汉式本身是非线程安全的，需要通过额外的机制保证线程安全



##### 登记式单例

```java
// 类似Spring里面的方法，将类名注册，下次从里面直接获取。
public class Singleton5 {
    private static Map<String, Singleton5> map = new HashMap<String, Singleton5>();

    static {
        Singleton5 singleton5 = new Singleton5();
        map.put(singleton5.getClass().getName(), singleton5);
    }


    //保护的默认构造子
    protected Singleton5() {
    }

    // 静态工厂方法,返还此类惟一的实例
    public static Singleton5 getInstance(Class<?> clazz) {
        String name;
        if (clazz == null) {
            name = Singleton5.class.getName();
            clazz = Singleton5.class;
        } else {
            name = clazz.getName();
        }

        if (map.get(name) == null) {
            try {
                map.put(name, (Singleton5) clazz.newInstance());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return map.get(name);
    }

    //一个示意性的商业方法
    public String about() {
        return "Hello, I am RegSingleton.";
    }
}
```

登记式单例实际上维护了一组单例类的实例，将这些实例存放在一个Map（登记薄）中，对于已经登记过的实例，则从Map直接返回，对于没有登记的，则先登记，然后返回。



### 原型模式

#### 定义

![img](../../../md-photo/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2E3NDUyMzM3MDA=,size_16,color_FFFFFF,t_70-16596680506063.jpeg)

原型模式的核心是就是原型类 Prototype，Prototype 类需要具备以下两个条件：

- （1）实现 Cloneable 接口：在 Java 中 Cloneable 接口的作用就是在运行时通知虚拟机可以安全地在实现了 Cloneable 接口的类上使用 clone() 方法，只有在实现了 Cloneable 的类才可以被拷贝，否则在运行时会抛出 CloneNotSupportedException 异常。
- （2）重写 Object 类中的 clone() 方法：Java 中所有类的父类都是 Object，Object 中有一个clone() 方法用于返回对象的拷贝，但是其作用域 protected，一般的类无法调用，因此，Prototype 类需要将 clone() 方法的作用域修改为 public。

​    原型模式是一种比较简单的模式，也非常容易理解，实现一个接口，重写一个方法即完成了原型模式。在实际应用中，原型模式很少单独出现。经常与其他模式混用，他的原型类Prototype也常用抽象类来替代。    



#### 原型模式的优点与适用场景

<font color='red'>（1）原型模式比 new 方式创建对象的性能要好的多，因为 Object 类的 clone() 方法是一个本地方法，直接操作内存中的二进制流，特别是复制大对象时，性能的差别非常明显；</font>

<font color='red'>（2）简化对象的创建；</font>

​    所以原型模式适合在重复地创建相似对象的场景使用，比如在一个循环体内创建对象，假如对象创建过程比较复杂或者循环次数很多的话，使用原型模式不但可以简化创建过程，而且也可以提供系统的整体性能。



#### 注意事项

（1）使用原型模式复制对象不会调用类的构造函数，对象是通过调用 Object 类的 clone() 方法来完成的，它直接在内存中复制数据。不但构造函数不会执行，甚至连访问权限都对原型模式无效。单例模式中，需要将构造函数的访问权限设置为 private，但是 clone() 方法直接无视构造方法的权限，所以单例模式与原型模式是冲突的，在使用时需要注意。

（2）深拷贝与浅拷贝。Object 类的 clone() 方法只会拷贝对象中的基本的数据类型（8种基本数据类型 byte,char,short,int,long,float,double,boolean 和对应的封装类），对于数组、容器对象、引用对象等都不会拷贝，这就是浅拷贝。如果要实现深拷贝，必须将原型模式中的数组、容器对象、引用对象等另行拷贝。

- 浅拷贝：只克隆对象中的基本数据类型，而不会克隆数组、容器、引用对象等。换言之，浅复制仅仅复制所考虑的对象，而不复制它所引用的对象。如果变量为String字符串，则拷贝其引用地址，但是在修改的时候，它会从字符串池中重新生成一个新的字符串，原有的字符串对象保持不变。
- 深拷贝：把要克隆的对象所引用的对象都克隆了一遍。



#### 示例

```java
public abstract class Prototype implements Cloneable {
    protected ArrayList<String> list = new ArrayList<String>();

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public abstract void show();
}

// 浅拷贝
public class ShallowClone extends Prototype {

    @Override
    public Prototype clone() throws CloneNotSupportedException {
        return (Prototype) super.clone();
    }

    @Override
    public void show() {
        System.out.println("浅克隆");
    }
}

// 深拷贝
public class DeepClone extends Prototype {
    @Override
    public Object clone() throws CloneNotSupportedException {
        Prototype prototype = (Prototype) super.clone();
        // 拷贝非基础类型数据
        prototype.list = (ArrayList<String>) this.list.clone();
        return prototype;
    }

    @Override
    public void show() {
        System.out.println("深克隆");
    }
}
```



> 由于ArrayList不是基本类型，所以成员变量list，不会被拷贝，需要我们自己实现深拷贝，幸运的是Java提供的大部分的容器类都实现了Cloneable接口。所以实现深拷贝并不是特别困难。



```java
public static void main(String[] args) throws CloneNotSupportedException {
        ShallowClone cp = new ShallowClone();
        ShallowClone clonecp = (ShallowClone) cp.clone();
        clonecp.show();
        // true 还是引用的同一个地址
        System.out.println(clonecp.list == cp.list);

        DeepClone cp2 = new DeepClone();
        DeepClone clonecp2 = (DeepClone) cp2.clone();
        clonecp2.show();
        // false 不是相同的内存地址，已经完成了copy的操作
        System.out.println(clonecp2.list == cp2.list);
    }
```

运行结果：

```java
浅克隆
true
深克隆
false
```
