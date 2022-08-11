# MQ

## 简介

MQ全称<font color='red'>M</font>essage <font color='red'>Q</font>ueue（消息队列）,是在消息传输过程中保存消息的容器。多用于分布式系统之间进行通信。



## 优势

### 应用解耦

提升了系统容错性和可维护性

![image](../../md-photo/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80MDE0OTU1Nw==,size_16,color_FFFFFF,t_70#pic_center.png)



使用MQ后：

![image](../../md-photo/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80MDE0OTU1Nw==,size_16,color_FFFFFF,t_70#pic_center-16596272031293.png)



### 异步提速

提升用户体验和系统吞吐量

![image](../../md-photo/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80MDE0OTU1Nw==,size_16,color_FFFFFF,t_70#pic_center-16596272458126.png)



使用MQ后：

![image](../../md-photo/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80MDE0OTU1Nw==,size_16,color_FFFFFF,t_70#pic_center-16596272643089.png)



### 削峰填谷

提高系统稳定性

![image](https://img-blog.csdnimg.cn/2021032119265844.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80MDE0OTU1Nw==,size_16,color_FFFFFF,t_70#pic_center)



使用MQ后：

![image](../../md-photo/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80MDE0OTU1Nw==,size_16,color_FFFFFF,t_70#pic_center-165962735375512.png)



## 劣势

### 系统可靠性降低

![img](../../md-photo/10b65392f1d3879c05e729442da538a2.png)



### 系统复杂度提高

![img](../../md-photo/419eea8e3d69a81d70d32398d49058f0.png)



### 处理结果的最终一致性问题

![img](../../md-photo/dce59c4ce1aca42b60b90502315b7983.png)

## 使用MQ需要满足条件

*小红希望小明多读书，常寻找好书给小明看，之前的方式是这样：小红问小明什么时候有空，把书给小明送去，并亲眼监督小明读完书才走。久而久之，两人都觉得麻烦。*

*后来的方式改成了：小红对小明说「我放到书架上的书你都要看」，然后小红每次发现不错的书都放到书架上，小明则看到书架上有书就拿下来看。*

*书架就是一个消息队列，小红是生产者，小明是消费者。*

>1. 生产者不需要从消费者处获得反馈
>
>引入消息队列之前的直接调用，其接口的返回值应该为空，这才让明明下层的动作还没做，上层却当成动作做完了继续往后走——即所谓异步——成为了可能。
>
>小红放完书之后小明到底看了没有，小红根本不问，她默认他是看了，否则就只能用原来的方法监督到看完了。
>
>2. 容许短暂的不一致性
>
>妈妈可能会发现「有时候据说小明看了某书，但事实上他还没看」，只要妈妈满意于「反正他最后看了就行」，异步处理就没问题。
>
>如果妈妈对这情况不能容忍，对小红大发雷霆，小红也就不敢用书架方式了。
>
>3. 确实是用了有效果即解耦、提速、广播、削峰这些方面的收益，超过放置书架、监控书架这些成本。
>
>否则如果是盲目照搬，「听说老赵家买了书架，咱们家也买一个」，买回来却没什么用，只是让步骤变多了，还不如直接把书递给对方呢，那就不对了。



## MQ产品

![image-20220804235443939](../../md-photo/image-20220804235443939.png)



# RabbitMQ

## 简介

RabbitMQ是一个实现了AMQP（<font color='red'>**Advanced Message Queuing Protocol**</font>）高级消息队列协议的消息队列服务，用Erlang语言。是面向消息的中间件。

## 主要流程

​	生产者（Producer）与消费者（Consumer）和 RabbitMQ 服务（Broker）建立连接， 然后生产者发布消息（Message）同时需要携带交换机（Exchange） 名称以及路由规则（Routing Key），这样消息会到达指定的交换机，然后交换机根据路由规则匹配对应的 Binding，最终将消息发送到匹配的消息队列（Quene），最后 RabbitMQ 服务将队列中的消息投递给订阅了该队列的消费者（消费者也可以主动拉取消息）。


![image](../../md-photo/63a098525b8c47408d9d93ccebb926c8.png)



## 下载与安装

参见

../软件安装/rabbitmq下载与安装.md



启动rabbitmq命令：

```bash
$ systemctl start rabbitmq-server
```



控制台访问地址：

http://rabbitmq1:15672/



## Rabbit控制台

### 创建用户

![image-20220806164625963](../../md-photo/image-20220806164625963.png)



### 创建虚拟机

![image-20220806164724033](../../md-photo/image-20220806164724033.png)



对虚拟机进行授权



![image-20220806164806144](../../md-photo/image-20220806164806144.png)

对bobo授权：

![image-20220806164902871](../../md-photo/image-20220806164902871.png)





## 程序实现工作模式

需要引入依赖：

```xml
<!--rabbitmq java 客户端-->
<dependency>
    <groupId>com.rabbitmq</groupId>
    <artifactId>amqp-client</artifactId>
</dependency>
```



### simple简单模式

#### 定义

![660cfa5db41d45298debdcd6b9a08732.png](../../md-photo/660cfa5db41d45298debdcd6b9a08732.png)

- P：消息产生者将消息放入队列
- C：消息的消费者(consumer) 监听(while) 消息队列,如果队列中有消息,就消费掉,消息被拿走后,自动从队列中删除(隐患 消息可能没有被消费者正确处理,已经从队列中消失了,造成消息的丢失)
- 应用场景:聊天(中间有一个过度的服务器;p端,c端)



#### 生产者

```java
public static void main(String[] args) throws IOException, TimeoutException {
    // 1.创建连接工厂
    ConnectionFactory factory = new ConnectionFactory();

    // 2.设置参数
    factory.setHost("rabbitmq1");// IP地址 默认localhost
    factory.setPort(5672);// 端口，默认5672
    factory.setVirtualHost("/itcast");// 虚拟机，默认值/
    factory.setUsername("bobo");// 用户名，默认guest
    factory.setPassword("123456");// 密码， 默认guest

    // 3.创建连接 Connection
    Connection connection = factory.newConnection();

    // 4.创建 Channel
    Channel channel = connection.createChannel();

    // 5.创建一个队列Queue
    /**
     * queueDeclare(String queue, boolean durable, boolean exclusive, boolean autoDelete,
     *                                  Map<String, Object> arguments)
     * queue：队列名称
     * durable：是否持久化，当mq重启后，数据还在
     * exclusive：是否独占，只有一个消费者监听这个队列 && 当Connection关闭时，是否删除该队列
     * autoDelete：是否自动删除。当没有consumer时，自动删除掉
     * arguments：参数信息
     */
    // 如果没有一个名字叫做hello_world队列，则会进行创建的操作
    channel.queueDeclare("hello_world", true, false, false, null);

    /**
     * basicPublish(String exchange, String routingKey, BasicProperties props, byte[] body)
     * exchange：交换机名称。简单模式下的交换机会使用默认的""
     * routingKey：路由名称
     * props：配置信息
     * body：发送的消息数据
     */
    String body = "hello, rabbitmq...";
    // 使用默认的交换机时，路由的名称要和队列的名字一致
    // 6.发送消息到Queue
    channel.basicPublish("", "hello_world", null, body.getBytes());

    // 7.释放资源
    channel.close();
    connection.close();
}
```



运行程序成功后，在控制台能够看到对应的队列信息：

![image-20220806171825212](../../md-photo/image-20220806171825212.png)



#### 消费者

```java
public static void main(String[] args) throws IOException, TimeoutException {
    // 1.创建连接工厂
    ConnectionFactory factory = new ConnectionFactory();

    // 2.设置参数
    factory.setHost("rabbitmq1");// IP地址 默认localhost
    factory.setPort(5672);// 端口，默认5672
    factory.setVirtualHost("/itcast");// 虚拟机，默认值/
    factory.setUsername("bobo");// 用户名，默认guest
    factory.setPassword("123456");// 密码， 默认guest

    // 3.创建连接 Connection
    Connection connection = factory.newConnection();

    // 4.创建 Channel
    Channel channel = connection.createChannel();

    // 5.创建一个队列Queue
    /**
     * queueDeclare(String queue, boolean durable, boolean exclusive, boolean autoDelete,
     *                                  Map<String, Object> arguments)
     * queue：队列名称
     * durable：是否持久化，当mq重启后，数据还在
     * exclusive：是否独占，只有一个消费者监听这个队列 && 当Connection关闭时，是否删除该队列
     * autoDelete：是否自动删除。当没有consumer时，自动删除掉
     * arguments：参数信息
     */
    // 如果没有一个名字叫做hello_world队列，则会进行创建的操作
    channel.queueDeclare("hello_world", true, false, false, null);

    /**
     * basicConsume(String queue, boolean autoAck, Consumer callback)
     * queue：队列名称
     * autoAck：是否自动确认
     * callback：回调对象
     */
    String body = "hello, rabbitmq...";
    // 使用默认的交换机时，路由的名称要和队列的名字一致
    Consumer consumer = new DefaultConsumer(channel) {
        // 回调方法，当收到消息后，会自动执行该方法

        /**
         * 参数解释
         *
         * @param consumerTag 标识
         * @param envelope 获取一些信息，交换机，路由key...
         * @param properties 配置信息
         * @param body 数据
         */
        @Override
        public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
            System.out.println("consumerTag：" + consumerTag);
            System.out.println("exchange：" + envelope.getExchange());
            System.out.println("routingKey：" + envelope.getRoutingKey());
            System.out.println("properties：" + properties);
            System.out.println("body：" + new String(body));
        }
    };
    // 6.发送消息到Queue
    channel.basicConsume("hello_world", true, consumer);

    // 消费者不需要关闭资源，它需要一直监听
    // 7.释放资源
    channel.close();
    connection.close();
}
```



消费者消费完后，队列中的消息清零：

![image-20220806173031623](../../md-photo/image-20220806173031623.png)





###  work工作模式(资源的竞争)

#### 定义

![e6ed585f24c7ab026ea9c70b5ad8c5ce.png](../../md-photo/e6ed585f24c7ab026ea9c70b5ad8c5ce.png)



> - 消息产生者将消息放入队列消费者可以有多个,消费者1,消费者2,同时监听同一个队列,消息被消费?C1 C2共同<font color='red'>争抢</font>当前的消息队列内容,谁先拿到谁负责消费消息
>
> - (隐患,高并发情况下,默认会产生某一个消息被多个消费者共同使用,可以设置一个开关(syncronize,与同步锁的性能不一样) 保证一条消息只能被一个消费者使用)
>
> - 应用场景:红包;大项目中的资源调度(任务分配系统不需知道哪一个任务执行系统在空闲,直接将任务扔到消息队列中,空闲的系统自动争抢)



#### 生产者

```java
@SneakyThrows
public static void main(String[] args) {
    // 1.创建连接工厂
    // 2.设置参数
    // 3.创建连接 Connection
    // 4.创建 Channel
    Channel channel = RabbitMQUtils.createChannel();

    // 5.创建一个队列Queue
    /**
     * queueDeclare(String queue, boolean durable, boolean exclusive, boolean autoDelete,
     *                                  Map<String, Object> arguments)
     * queue：队列名称
     * durable：是否持久化，当mq重启后，数据还在
     * exclusive：是否独占，只有一个消费者监听这个队列 && 当Connection关闭时，是否删除该队列
     * autoDelete：是否自动删除。当没有consumer时，自动删除掉
     * arguments：参数信息
     */
    // 如果没有一个名字叫做hello_world队列，则会进行创建的操作
    channel.queueDeclare("work-queues", true, false, false, null);

    /**
     * basicPublish(String exchange, String routingKey, BasicProperties props, byte[] body)
     * exchange：交换机名称。简单模式下的交换机会使用默认的""
     * routingKey：路由名称
     * props：配置信息
     * body：发送的消息数据
     */
    for (int i = 0; i < 10; i++) {
        String body = i + " hello, rabbitmq...";
        // 使用默认的交换机时，路由的名称要和队列的名字一致
        // 6.发送消息到Queue
        channel.basicPublish("", "work-queues", null, body.getBytes());
    }

    // 7.释放资源
    RabbitMQUtils.clear(channel);
}
```

#### 消费者

添加2个消费者，程序内容都一样：

```java
@SneakyThrows
public static void main(String[] args) {
    // 1.创建连接工厂
    // 2.设置参数
    // 3.创建连接 Connection
    // 4.创建 Channel
    Channel channel = RabbitMQUtils.createChannel();

    // 5.创建一个队列Queue
    /**
     * queueDeclare(String queue, boolean durable, boolean exclusive, boolean autoDelete,
     *                                  Map<String, Object> arguments)
     * queue：队列名称
     * durable：是否持久化，当mq重启后，数据还在
     * exclusive：是否独占，只有一个消费者监听这个队列 && 当Connection关闭时，是否删除该队列
     * autoDelete：是否自动删除。当没有consumer时，自动删除掉
     * arguments：参数信息
     */
    // 如果没有一个名字叫做hello_world队列，则会进行创建的操作
    channel.queueDeclare("work-queues", true, false, false, null);

    /**
     * basicConsume(String queue, boolean autoAck, Consumer callback)
     * queue：队列名称
     * autoAck：是否自动确认
     * callback：回调对象
     */
    // 使用默认的交换机时，路由的名称要和队列的名字一致
    Consumer consumer = new DefaultConsumer(channel) {
        // 回调方法，当收到消息后，会自动执行该方法

        /**
         * 参数解释
         *
         * @param consumerTag 标识
         * @param envelope 获取一些信息，交换机，路由key...
         * @param properties 配置信息
         * @param body 数据
         */
        @Override
        public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
            System.out.println("body：" + new String(body));
        }
    };
    // 6.发送消息到Queue
    channel.basicConsume("work-queues", true, consumer);
}
```



先启动两个消费者，再启动生产者，得到以下的结果：

![image-20220806174639559](../../md-photo/image-20220806174639559.png)

![image-20220806174657146](../../md-photo/image-20220806174657146.png)

两个队列互相竞争进行消费。



### publish/subscribe发布订阅(共享资源)

#### 定义

![0aff369c26ef9dcbdca2f1e4dd319308.png](../../md-photo/0aff369c26ef9dcbdca2f1e4dd319308.png)

>- X代表交换机rabbitMQ内部组件,erlang 消息产生者是代码完成,代码的执行效率不高,消息产生者将消息放入交换机,交换机发布订阅把消息发送到所有消息队列中,对应消息队列的消费者拿到消息进行消费，交换机
>
>- 相关场景:邮件群发,群聊天,广播(广告)



Exchange:交换机(X)。一方面，接收生产者发送的消息。另一方面，知道如何处理消息，例如递交给某个特别队列、递交给所有队列、或是将消息丢弃。到底如何操作，取澳于Exchange的类型。Exchange有常见以下3种类型:

- Fanout:广播，将消息交给所有绑定到交换机的队列
- Direct:定向，把消息交给符合指定routing key 的队列
- Topic:通配符，把消息交给符合routing pattern(路由模式)的队列



Exchange(交换机)只负责转发消息，不具备存储消息的能力，因此<font color='red'>**如果没有任何队列与Exchange绑定，或者没有符合路由规则的队列，那么消息会丢失!**</font>



#### 生产者

```java
@SneakyThrows
public static void main(String[] args) {
    // 1.创建连接工厂
    // 2.设置参数
    // 3.创建连接 Connection
    // 4.创建 Channel
    Channel channel = RabbitMQUtils.createChannel();

    /**
     * exchangeDeclare(String exchange,
     *         BuiltinExchangeType type,
     *         boolean durable,
     *         boolean autoDelete,
     *         boolean internal,
     *         Map<String, Object> arguments)
     *
     * exchange：交换机名称
     * type：交换机类型 {@link com.rabbitmq.client.BuiltinExchangeType}
     *   DIRECT("direct")：定向
     *   FANOUT("fanout")：扇形（广播），发送消息到每一个与之绑定队列
     *   TOPIC("topic")：通配符的方式
     *   HEADERS("headers")：参数匹配
     * durable：是否持久化
     * autoDelete：是否自动删除
     * internal；内部使用，一般都为false
     * arguments：参数
     */
    // 5.创建交换机
    String exangeName = "test-fanout";
    channel.exchangeDeclare(exangeName, BuiltinExchangeType.FANOUT, true, false, false, null);

    // 6.创建队列
    String queueName1 = "test-fanout-queue1";
    String queueName2 = "test-fanout-queue2";
    channel.queueDeclare(queueName1, true, false, false, null);
    channel.queueDeclare(queueName2, true, false, false, null);

    /**
     * queueBind(String queue, String exchange, String routingKey, Map<String, Object> arguments)
     * queue：队列名称
     * exchange：交换机名称
     * routingKey：路由key，绑定规则
     *  如果交换机类型为fanout，routingKey设置为""，将消息路由到每一个队列
     * arguments：参数
     */
    // 7.绑定队列和交换机
    channel.queueBind(queueName1, exangeName, "");
    channel.queueBind(queueName2, exangeName, "");

    String message = "hello, pubsub rabbitmq...";
    // 8.发送消息
    channel.basicPublish(exangeName, "", null, message.getBytes());

    // 9.释放资源
    RabbitMQUtils.clear(channel);
}
```



生产者运行成功后，多了一个交换机：

![image-20220806181711496](../../md-photo/image-20220806181711496.png)



交换机中有绑定的规则，可以看是没有路由key的绑定方式：

![image-20220806181756750](../../md-photo/image-20220806181756750.png)



通过广播模式，将消息通过交换机发送到两个队列中：

![image-20220806181912868](../../md-photo/image-20220806181912868.png)

队列1：

![image-20220806181940518](../../md-photo/image-20220806181940518.png)



队列2：

![image-20220806182016185](../../md-photo/image-20220806182016185.png)



#### 消费者

消费者1：

```java
@SneakyThrows
public static void main(String[] args) {
    // 1.创建连接工厂
    // 2.设置参数
    // 3.创建连接 Connection
    // 4.创建 Channel
    Channel channel = RabbitMQUtils.createChannel();

    // 5.创建一个队列Queue
    // 队列只需要创建一次就行了，生产者已经创建队列，这里不需要再创建队列

    // 使用默认的交换机时，路由的名称要和队列的名字一致
    Consumer consumer = new DefaultConsumer(channel) {
        // 回调方法，当收到消息后，会自动执行该方法

        /**
         * 参数解释
         *
         * @param consumerTag 标识
         * @param envelope 获取一些信息，交换机，路由key...
         * @param properties 配置信息
         * @param body 数据
         */
        @Override
        public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) {
            System.out.println("body：" + new String(body));
            System.out.println("日志打印到控制台......");
        }
    };
    // 6.发送消息到Queue
    channel.basicConsume("test-fanout-queue1", true, consumer);
}
```



消费者2：

```java
@SneakyThrows
public static void main(String[] args) {
    // 1.创建连接工厂
    // 2.设置参数
    // 3.创建连接 Connection
    // 4.创建 Channel
    Channel channel = RabbitMQUtils.createChannel();

    // 5.创建一个队列Queue
    // 队列只需要创建一次就行了，生产者已经创建队列，这里不需要再创建队列

    // 使用默认的交换机时，路由的名称要和队列的名字一致
    Consumer consumer = new DefaultConsumer(channel) {
        // 回调方法，当收到消息后，会自动执行该方法

        /**
         * 参数解释
         *
         * @param consumerTag 标识
         * @param envelope 获取一些信息，交换机，路由key...
         * @param properties 配置信息
         * @param body 数据
         */
        @Override
        public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
            System.out.println("body：" + new String(body));
            System.out.println("日志保存到数据库......");
        }
    };
    // 6.发送消息到Queue
    channel.basicConsume("test-fanout-queue2", true, consumer);
}
```



运行结果：

![image-20220806185344941](../../md-photo/image-20220806185344941.png)



![image-20220806185358926](../../md-photo/image-20220806185358926.png)



可以看到，都消费了消息，而且后续还可以执行不同的操作，同时队列中的消息被清空：

![image-20220806185452617](../../md-photo/image-20220806185452617.png)

### RabbitMQ之Routing模式

#### 定义

![img](../../md-photo/cc9c5d8c975c4ad08d4653216817c35b.png)

队列与交换机的绑定，<font color='red'>**不能是任意绑定了，而是要指定一个 RoutingKey（路由key）**</font>

消息的发送方在<font color='red'>**向 Exchange 发送消息时，也必须指定消息的 RoutingKey**</font>

Exchange 不再把消息交给每一个绑定的队列，而是<font color='red'>**根据消息的 Routing Key 进行判断**</font>，只有队列的 Routingkey 与消息的 Routing key 完全一致，才会接收到消息



#### 需求

**将error级别日志保存到数据库，error、info、warning级别日志打印到控制台**



#### 生产者

```java
@SneakyThrows
public static void main(String[] args) {
    // 1.创建连接工厂
    // 2.设置参数
    // 3.创建连接 Connection
    // 4.创建 Channel
    Channel channel = RabbitMQUtils.createChannel();

    /**
     * exchangeDeclare(String exchange,
     *         BuiltinExchangeType type,
     *         boolean durable,
     *         boolean autoDelete,
     *         boolean internal,
     *         Map<String, Object> arguments)
     *
     * exchange：交换机名称
     * type：交换机类型 {@link BuiltinExchangeType}
     *   DIRECT("direct")：定向
     *   FANOUT("fanout")：扇形（广播），发送消息到每一个与之绑定队列
     *   TOPIC("topic")：通配符的方式
     *   HEADERS("headers")：参数匹配
     * durable：是否持久化
     * autoDelete：是否自动删除
     * internal；内部使用，一般都为false
     * arguments：参数
     */
    // 5.创建交换机
    String exangeName = "test-direct";
    channel.exchangeDeclare(exangeName, BuiltinExchangeType.DIRECT, true, false, false, null);

    // 6.创建队列
    String queueName1 = "test-direct-queue1";
    String queueName2 = "test-direct-queue2";
    channel.queueDeclare(queueName1, true, false, false, null);
    channel.queueDeclare(queueName2, true, false, false, null);

    /**
     * queueBind(String queue, String exchange, String routingKey, Map<String, Object> arguments)
     * queue：队列名称
     * exchange：交换机名称
     * routingKey：路由key，绑定规则
     *  如果交换机类型为fanout，routingKey设置为""，将消息路由到每一个队列
     * arguments：参数
     */
    // 7.绑定队列和交换机
    // 队列1绑定error
    channel.queueBind(queueName1, exangeName, "error");

    // 队列2绑定error、info、warning
    channel.queueBind(queueName2, exangeName, "error");
    channel.queueBind(queueName2, exangeName, "info");
    channel.queueBind(queueName2, exangeName, "warning");

    String message = "hello, pubsub rabbitmq...";
    // 8.发送消息
    channel.basicPublish(exangeName, "error", null, message.getBytes());

    // 9.释放资源
    RabbitMQUtils.clear(channel);
}
```

#### 消费者

消费者1：

```java
@SneakyThrows
public static void main(String[] args) {
    // 1.创建连接工厂
    // 2.设置参数
    // 3.创建连接 Connection
    // 4.创建 Channel
    Channel channel = RabbitMQUtils.createChannel();

    // 5.创建一个队列Queue
    // 队列只需要创建一次就行了，生产者已经创建队列，这里不需要再创建队列

    // 使用默认的交换机时，路由的名称要和队列的名字一致
    Consumer consumer = new DefaultConsumer(channel) {
        // 回调方法，当收到消息后，会自动执行该方法

        /**
         * 参数解释
         *
         * @param consumerTag 标识
         * @param envelope 获取一些信息，交换机，路由key...
         * @param properties 配置信息
         * @param body 数据
         */
        @Override
        public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) {
            System.out.println("body：" + new String(body));
            System.out.println("日志保存到数据库......");
        }
    };
    // 6.发送消息到Queue
    channel.basicConsume("test-direct-queue1", true, consumer);
}
```



消费者2：

```java
@SneakyThrows
public static void main(String[] args) {
    // 1.创建连接工厂
    // 2.设置参数
    // 3.创建连接 Connection
    // 4.创建 Channel
    Channel channel = RabbitMQUtils.createChannel();

    // 5.创建一个队列Queue
    // 队列只需要创建一次就行了，生产者已经创建队列，这里不需要再创建队列

    // 使用默认的交换机时，路由的名称要和队列的名字一致
    Consumer consumer = new DefaultConsumer(channel) {
        // 回调方法，当收到消息后，会自动执行该方法

        /**
         * 参数解释
         *
         * @param consumerTag 标识
         * @param envelope 获取一些信息，交换机，路由key...
         * @param properties 配置信息
         * @param body 数据
         */
        @Override
        public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) {
            System.out.println("body：" + new String(body));
            System.out.println("日志打印到控制台......");
        }
    };
    // 6.发送消息到Queue
    channel.basicConsume("test-direct-queue2", true, consumer);
}
```



#### 结论

当发送发送error的路由时：

![image-20220806222945218](../../md-photo/image-20220806222945218.png)



两个消费者都能收到消息：

![image-20220806223040178](../../md-photo/image-20220806223040178.png)



![image-20220806223057848](../../md-photo/image-20220806223057848.png)



当发送发送info的路由时：

![image-20220806223236844](../../md-photo/image-20220806223236844.png)



只有打印到控制台的收到了消息：

![image-20220806223206089](../../md-photo/image-20220806223206089.png)



![image-20220806223321082](../../md-photo/image-20220806223321082.png)



### RabbitMQ之Topics模式

#### 定义

**<font color='red'>在Routing模式的基础上多了一个模糊匹配</font>**

![img](../../md-photo/d0ca2117006c4e6b813d995f5fcaab35.png)

**<font color='red'>通配符规则：# 匹配一个或多个词，* 匹配有且仅有1个词</font>**，例如：item.# 能够匹配 item.insert.abc 或者 item.insert，item.* 只能匹配 item.insert

![img](../../md-photo/2e75f56ae7e94c71ba9c3c03ace5a70a.png)

红色 Queue：绑定的是**usa.#** ，因此凡是**以 usa.开头的** routing key 都会被匹配到

黄色 Queue：绑定的是**#.news**，因此凡是**以 .news 结尾**的 routing key 都会被匹配 

Topic 类型与 Direct 相比，都是可以根据 RoutingKey 把消息路由到不同的队列。只不过 Topic 类型 Exchange 可以让队列在绑定 Routing key 的时候使用通配符！ 

Routingkey 一般都是有一个或**多个单词组成**，多个单词之间以”.”分割，例如： item.insert



#### 需求

**routing key 系统名称.日志的级别**
**将所有error级别的日志保存到数据库，所有order系统的日志保存到数据库**

#### 生产者

```java
@SneakyThrows
public static void main(String[] args) {
    // 1.创建连接工厂
    // 2.设置参数
    // 3.创建连接 Connection
    // 4.创建 Channel
    Channel channel = RabbitMQUtils.createChannel();

    /**
     * exchangeDeclare(String exchange,
     *         BuiltinExchangeType type,
     *         boolean durable,
     *         boolean autoDelete,
     *         boolean internal,
     *         Map<String, Object> arguments)
     *
     * exchange：交换机名称
     * type：交换机类型 {@link BuiltinExchangeType}
     *   DIRECT("direct")：定向
     *   FANOUT("fanout")：扇形（广播），发送消息到每一个与之绑定队列
     *   TOPIC("topic")：通配符的方式
     *   HEADERS("headers")：参数匹配
     * durable：是否持久化
     * autoDelete：是否自动删除
     * internal；内部使用，一般都为false
     * arguments：参数
     */
    // 5.创建交换机
    String exangeName = "test-topics";
    channel.exchangeDeclare(exangeName, BuiltinExchangeType.TOPIC, true, false, false, null);

    // 6.创建队列
    String queueName1 = "test-topics-queue1";
    String queueName2 = "test-topics-queue2";
    channel.queueDeclare(queueName1, true, false, false, null);
    channel.queueDeclare(queueName2, true, false, false, null);

    /**
     * queueBind(String queue, String exchange, String routingKey, Map<String, Object> arguments)
     * queue：队列名称
     * exchange：交换机名称
     * routingKey：路由key，绑定规则
     *  如果交换机类型为fanout，routingKey设置为""，将消息路由到每一个队列
     * arguments：参数
     */
    // 7.绑定队列和交换机
    // routing key 系统名称.日志的级别
    // 将所有error级别的日志保存到数据库，所有order系统的日志保存到数据库
    channel.queueBind(queueName1, exangeName, "#.error");
    channel.queueBind(queueName1, exangeName, "order.*");
    channel.queueBind(queueName2, exangeName, "*.*");

    String message = "hello, pubsub rabbitmq...";
    // 8.发送消息
    channel.basicPublish(exangeName, "order.info", null, message.getBytes());

    // 9.释放资源
    RabbitMQUtils.clear(channel);
}
```



#### 消费者

消费者1：

```java
@SneakyThrows
public static void main(String[] args) {
    // 1.创建连接工厂
    // 2.设置参数
    // 3.创建连接 Connection
    // 4.创建 Channel
    Channel channel = RabbitMQUtils.createChannel();

    // 5.创建一个队列Queue
    // 队列只需要创建一次就行了，生产者已经创建队列，这里不需要再创建队列

    // 使用默认的交换机时，路由的名称要和队列的名字一致
    Consumer consumer = new DefaultConsumer(channel) {
        // 回调方法，当收到消息后，会自动执行该方法

        /**
         * 参数解释
         *
         * @param consumerTag 标识
         * @param envelope 获取一些信息，交换机，路由key...
         * @param properties 配置信息
         * @param body 数据
         */
        @Override
        public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) {
            System.out.println("body：" + new String(body));
            System.out.println("日志保存到数据库......");
        }
    };
    // 6.发送消息到Queue
    channel.basicConsume("test-topics-queue1", true, consumer);
}
```



消费者2：

```java
@SneakyThrows
public static void main(String[] args) {
    // 1.创建连接工厂
    // 2.设置参数
    // 3.创建连接 Connection
    // 4.创建 Channel
    Channel channel = RabbitMQUtils.createChannel();

    // 5.创建一个队列Queue
    // 队列只需要创建一次就行了，生产者已经创建队列，这里不需要再创建队列

    // 使用默认的交换机时，路由的名称要和队列的名字一致
    Consumer consumer = new DefaultConsumer(channel) {
        // 回调方法，当收到消息后，会自动执行该方法

        /**
         * 参数解释
         *
         * @param consumerTag 标识
         * @param envelope 获取一些信息，交换机，路由key...
         * @param properties 配置信息
         * @param body 数据
         */
        @Override
        public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) {
            System.out.println("body：" + new String(body));
            System.out.println("日志打印到控制台......");
        }
    };
    // 6.发送消息到Queue
    channel.basicConsume("test-topics-queue2", true, consumer);
}
```



#### 结论

当订单系统order的info级别日志时，日志需要打印到控制台并保存到数据库

![image-20220806225400615](../../md-photo/image-20220806225400615.png)



打印到控制台：

![image-20220806225851747](../../md-photo/image-20220806225851747.png)



保存到数据库：

![image-20220806225916968](../../md-photo/image-20220806225916968.png)



当商品系统goods的info级别日志时，仅仅打印到控制台：

![image-20220806225953614](../../md-photo/image-20220806225953614.png)

![image-20220806230027816](../../md-photo/image-20220806230027816.png)





## Spring整合RabbitMQ

### 生产者

#### 依赖

```xml
<!--spring 上下文-->
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-context</artifactId>
    <version>LATEST</version>
</dependency>

<!--spring-rabbit-->
<dependency>
    <groupId>org.springframework.amqp</groupId>
    <artifactId>spring-rabbit</artifactId>
</dependency>

<!--单元测试-->
<dependency>
    <groupId>junit</groupId>
    <artifactId>junit</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-test</artifactId>
    <version>LATEST</version>
</dependency>
```



rabbitmq连接配置信息：

```properties
rabbitmq.host=rabbitmq1
rabbitmq.port=5672
rabbitmq.username=bobo
rabbitmq.password=123456
rabbitmq.virtual-host=/itcast
```



#### spring核心配置文件

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:rabbit="http://www.springframework.org/schema/rabbit"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/context
       http://www.springframework.org/schema/context/spring-context.xsd
       http://www.springframework.org/schema/rabbit
       http://www.springframework.org/schema/rabbit/spring-rabbit.xsd">

    <!--加载配置文件-->
    <context:property-placeholder location="rabbitmq.properties"/>

    <!--定义rabbitmq connectionFactory-->
    <rabbit:connection-factory id="connectionFactory" host="${rabbitmq.host}"
                               port="${rabbitmq.port}"
                               username="${rabbitmq.username}"
                               password="${rabbitmq.password}"
                               virtual-host="${rabbitmq.virtual-host}"/>

    <!--定义管理交换机、队列-->
    <rabbit:admin connection-factory="connectionFactory"/>

    <!--定义rabbitmqTemplate模板方便在代码中进行发送消息的操作-->
    <rabbit:template id="rabbitTemplate" connection-factory="connectionFactory"/>

    <!--定义持久化队列，不存在则自动创建；不绑定到交换机则绑定到默认交换机
    默认交换机类型为direct，名字为""，路由键为队列名称-->
    <!--helloworld示例，没有使用交换机-->
    <rabbit:queue id="spring_queue" name="spring_queue" auto-declare="true"/>

    <!--================广播：所有队列都能收到消息================-->
    <!--队列-->
    <rabbit:queue id="spring_fanout_queue1" name="spring_fanout_queue1" auto-declare="true"/>
    <rabbit:queue id="spring_fanout_queue2" name="spring_fanout_queue2" auto-declare="true"/>
    <!--广播类型交换机-->
    <rabbit:fanout-exchange id="spring_fanout_exchange" name="spring_fanout_exchange" auto-declare="true">
        <rabbit:bindings>
            <rabbit:binding queue="spring_fanout_queue1"/>
            <rabbit:binding queue="spring_fanout_queue2"/>
        </rabbit:bindings>
    </rabbit:fanout-exchange>

    <!--================通配符：*匹配一个单词，#匹配多个单词================-->
    <!--队列-->
    <rabbit:queue id="spring_topics_queue1" name="spring_topics_queue1" auto-declare="true"/>
    <rabbit:queue id="spring_topics_queue2" name="spring_topics_queue2" auto-declare="true"/>
    <rabbit:queue id="spring_topics_queue3" name="spring_topics_queue3" auto-declare="true"/>

    <!--交换机-->
    <rabbit:topic-exchange name="spring_topics_exchange" id="spring_topics_exchange" auto-declare="true">
        <rabbit:bindings>
            <rabbit:binding pattern="heima.*" queue="spring_topics_queue1"/>
            <rabbit:binding pattern="heima.#" queue="spring_topics_queue2"/>
            <rabbit:binding pattern="itcast.#" queue="spring_topics_queue3"/>
        </rabbit:bindings>
    </rabbit:topic-exchange>
</beans>
```



#### 案例

**Test类**：

```java
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-rabbitmq-producer.xml"})
public class ProducerTest {
    // 1. 注入RabbitTemplate
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testHelloWorld() {
        rabbitTemplate.convertAndSend("spring_queue", "helloworld.spring");
    }

}
```



##### HelloSpring

```java
@Test
public void testHelloWorld() {
    // 不需要交换机发送消息
    rabbitTemplate.convertAndSend("spring_queue", "helloworld.spring");
}
```

运行结果：

![image-20220807114110125](../../md-photo/image-20220807114110125.png)



##### Fanout

```java
@Test
public void testFanout() {
    // 通过交换机广播发送消息，此时的路由key给空值即可
    rabbitTemplate.convertAndSend("spring_fanout_exchange", "", "helloworld.spring.fanout");
}
```

运行结果：

![image-20220807114326521](../../md-photo/image-20220807114326521.png)



##### Topics

```java
@Test
public void testTopics() {
    // 通过交换机发送消息，通过模糊匹配路由进行发送
    rabbitTemplate.convertAndSend("spring_topics_exchange", "heima.test.bobo", "helloworld.spring.topics");
}
```

运行结果：

![image-20220807115103534](../../md-photo/image-20220807115103534.png)



### 消费者

#### 依赖

同生产者

#### spring核心配置文件

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:rabbit="http://www.springframework.org/schema/rabbit"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/context
       http://www.springframework.org/schema/context/spring-context.xsd
       http://www.springframework.org/schema/rabbit
       http://www.springframework.org/schema/rabbit/spring-rabbit.xsd">

    <!--加载配置文件-->
    <context:property-placeholder location="rabbitmq.properties"/>

    <!--定义rabbitmq connectionFactory-->
    <rabbit:connection-factory id="connectionFactory" host="${rabbitmq.host}"
                               port="${rabbitmq.port}"
                               username="${rabbitmq.username}"
                               password="${rabbitmq.password}"
                               virtual-host="${rabbitmq.virtual-host}"/>

    <!--定义rabbitmq相关队列的监听器-->
    <bean id="springQueueListener" class="com.consumer.SpringQueueListener"/>
    <bean id="fanoutListener1" class="com.consumer.FanoutListener1"/>
    <bean id="fanoutListener2" class="com.consumer.FanoutListener2"/>
    <bean id="topicsListener1" class="com.consumer.TopicsListener1"/>
    <bean id="topicsListener2" class="com.consumer.TopicsListener2"/>
    <bean id="topicsListener3" class="com.consumer.TopicsListener3"/>

    <!--配置监听器与连接工厂的关系-->
    <rabbit:listener-container connection-factory="connectionFactory">
        <rabbit:listener ref="springQueueListener" queue-names="spring_queue"/>
        <rabbit:listener ref="fanoutListener1" queue-names="spring_fanout_queue1"/>
        <rabbit:listener ref="fanoutListener2" queue-names="spring_fanout_queue2"/>
        <rabbit:listener ref="topicsListener1" queue-names="spring_topics_queue1"/>
        <rabbit:listener ref="topicsListener2" queue-names="spring_topics_queue2"/>
        <rabbit:listener ref="topicsListener3" queue-names="spring_topics_queue3"/>
    </rabbit:listener-container>
</beans>
```



#### 案例

##### HelloSpring

```java
public class SpringQueueListener implements MessageListener {

    @Override
    public void onMessage(Message message) {
        // 打印消息
        System.out.println(this.getClass().getName() + "：" + new String(message.getBody()));
    }
}
```



##### Fanout

```java
public class FanoutListener1 implements MessageListener {

    @Override
    public void onMessage(Message message) {
        // 打印消息
        System.out.println(this.getClass().getName() + "：" + new String(message.getBody()));
    }
}

public class FanoutListener2 implements MessageListener {

    @Override
    public void onMessage(Message message) {
        // 打印消息
        System.out.println(this.getClass().getName() + "：" + new String(message.getBody()));
    }
}
```



##### Topics

```java
public class TopicsListener1 implements MessageListener {

    @Override
    public void onMessage(Message message) {
        // 打印消息
        System.out.println(this.getClass().getName() + "：" + new String(message.getBody()));
    }
}

public class TopicsListener2 implements MessageListener {

    @Override
    public void onMessage(Message message) {
        // 打印消息
        System.out.println(this.getClass().getName() + "：" + new String(message.getBody()));
    }
}

public class TopicsListener3 implements MessageListener {

    @Override
    public void onMessage(Message message) {
        // 打印消息
        System.out.println(this.getClass().getName() + "：" + new String(message.getBody()));
    }
}
```



##### 开始消费

对生产者生产的消息进行消费

```java
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-rabbitmq-consumer.xml")
public class ConsumerTest {

    @Test
    public void testConsumer() {
        while (true) {
            
        }
    }
}
```



运行结果：

![image-20220807121114447](../../md-photo/image-20220807121114447.png)



## SpringBoot整合RabbitMQ

### 生产者

#### 依赖

```xml
<!--springboot rabbitmq启动器-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-amqp</artifactId>
</dependency>
```



#### spring核心配置文件

```yml
server:
  port: 8095

# spring-rabbit配置信息
spring:
  rabbitmq:
    host: rabbitmq1
    port: 5672
    username: bobo
    password: 123456
    virtual-host: /itcast
```



#### 案例

**测试Test：**

```java
@SpringBootTest
public class ProducerTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;
}
```



##### HelloSpringBoot

添加RabbitMQ的队列配置：

```java
@Configuration
public class RabbitSimleConfig {

    @Bean
    public Queue simpleQueue() {
        return QueueBuilder.durable("boot_simple_queue").build();
    }
}
```



进行测试：

```java
@Test
public void testSimple() {
    rabbitTemplate.convertAndSend("boot_simple_queue", "boot.test.simple");
}
```

测试结果：

![image-20220807134923743](../../md-photo/image-20220807134923743.png)



##### Direct

添加RabbitMQ的队列配置：

```java
@Configuration
public class RabbitRoutingConfig {

    // =======================交换机=======================
    @Bean
    public Exchange routingExchange() {
        return ExchangeBuilder.directExchange("boot_routing_exchange").build();
    }

    // =====================Queue队列=====================
    @Bean
    public Queue routingQueue1() {
        return QueueBuilder.durable("boot_routing_queue1").build();
    }

    @Bean
    public Queue routingQueue2() {
        return QueueBuilder.durable("boot_routing_queue2").build();
    }

    // ============队列和交换机的绑定关系 Binding============
    @Bean
    public Binding routingBindings1(@Qualifier("routingQueue1") Queue queue,
                                    @Qualifier("routingExchange") Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("error").noargs();
    }

    @Bean
    public Binding routingBindings2(@Qualifier("routingQueue2") Queue queue,
                                    @Qualifier("routingExchange") Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("error").noargs();
    }

    @Bean
    public Binding routingBindings3(@Qualifier("routingQueue2") Queue queue,
                                    @Qualifier("routingExchange") Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("info").noargs();
    }

}
```



进行测试：

```java
@Test
public void testRouting() {
    rabbitTemplate.convertAndSend("boot_routing_exchange", "info", "boot.test.routing.info");
    rabbitTemplate.convertAndSend("boot_routing_exchange", "error", "boot.test.routing.error");
}
```



测试结果：

![image-20220807141215049](../../md-photo/image-20220807141215049.png)



![image-20220807141503276](../../md-photo/image-20220807141503276.png)



![image-20220807141416022](../../md-photo/image-20220807141416022.png)





##### Topics

添加RabbitMQ的队列配置：

```java
@Configuration
public class RabbitTopicsConfig {
    // =======================交换机=======================

    @Bean
    public Exchange topicsExchange() {
        // 可以在build之前声明参数
        return ExchangeBuilder.topicExchange("boot_topics_exchange").durable(true).build();
    }

    // =====================Queue队列=====================
    @Bean
    public Queue topicsQueue1() {
        // 可以在build之前声明参数
        return QueueBuilder.durable("boot_topics_queue1").build();
    }

    @Bean
    public Queue topicsQueue2() {
        // 可以在build之前声明参数
        return QueueBuilder.durable("boot_topics_queue2").build();
    }

    @Bean
    public Queue topicsQueue3() {
        // 可以在build之前声明参数
        return QueueBuilder.durable("boot_topics_queue3").build();
    }

    // ============队列和交换机的绑定关系 Binding============
    @Bean
    public Binding topicsBindings1(@Qualifier("topicsQueue1") Queue queue,
                                   @Qualifier("topicsExchange") Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("boot.#").noargs();
    }

    @Bean
    public Binding topicsBindings2(@Qualifier("topicsQueue2") Queue queue,
                                   @Qualifier("topicsExchange") Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("boot.*").noargs();
    }

    @Bean
    public Binding topicsBindings3(@Qualifier("topicsQueue3") Queue queue,
                                   @Qualifier("topicsExchange") Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("cloud.#").noargs();
    }
}
```



进行测试：

```java
@Test
public void testTopics() {
    rabbitTemplate.convertAndSend("boot_topics_exchange", "boot.test", "hello.boot.topics");
}
```



测试结果：

![image-20220807141746295](../../md-photo/image-20220807141746295.png)



### 消费者

#### 依赖

同生产者



#### spring核心配置文件

```yml
server:
  port: 8096

# spring-rabbit配置信息
spring:
  rabbitmq:
    host: rabbitmq1
    port: 5672
    username: bobo
    password: 123456
    virtual-host: /itcast
```



#### 案例

监听器：

```java
@Component
public class SimpleListener {

    @RabbitListener(queues = {"boot_simple_queue"})
    public void listenerQueue(Message message) {
        System.out.println(new String(message.getBody()));
    }
}
```



在有消息的时候自动会消费，这里只演示simple的消息，其他的(direct、topics)下来自行测试即可：

启动主启动类，自动完成消息的消费：

![image-20220807163724230](../../md-photo/image-20220807163724230.png)



### 对象（Object）进行消息的发送和消费

#### 对象

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RabbitBean {
    private String name;
    private String description;
}
```



#### 生产者

添加消息序列化配置：

```java
@Configuration
@Slf4j
public class RabbitMQConfig {
    // 提供自定义RabbitTemplate,将对象序列化为json串
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setConnectionFactory(connectionFactory);
        // 设置Jackson2Json高性能序列化工具
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        // 开启发送失败退回
        rabbitTemplate.setMandatory(true);
        // 设置确定回调函数 消息发送到交换机时执行
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            log.info("confirm执行了......");
        });

        // 设置回调函数 交换机路由到队列失败时执行
        rabbitTemplate.setReturnsCallback(returned -> {
            log.info("return执行了......");
        });

        return rabbitTemplate;
    }
}
```



添加队列和交换机：

```java
@Configuration
public class RabbitConfirmsConfig {

    // 定义队列
    @Bean
    public Queue confirmsQueue() {
        return QueueBuilder.durable("confirms-queue").build();
    }

    // 定义交换机
    @Bean
    public Exchange confirmsExchange() {
        return ExchangeBuilder.directExchange("confirms-exchange").build();
    }

    // 绑定交换机和队列
    @Bean
    public Binding confirmsBinding(@Qualifier("confirmsQueue") Queue queue,
                                   @Qualifier("confirmsExchange") Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("confirms-route").noargs();
    }
}
```



进行消息发送：

```java
@Test
public void testSendObj() {
    rabbitTemplate.convertAndSend("confirms-exchange", "confirms-route", new RabbitBean("rabbit", "小兔子"));
}
```



#### 消费者

添加消息反序列化配置：

```java
@Configuration
public class RabbitMQConfig implements RabbitListenerConfigurer {
    // 可以将json串反序列化为对象
    @Override
    public void configureRabbitListeners(RabbitListenerEndpointRegistrar registrar) {
        registrar.setMessageHandlerMethodFactory(messageHandlerMethodFactory());
    }

    @Bean
    public MessageHandlerMethodFactory messageHandlerMethodFactory() {
        DefaultMessageHandlerMethodFactory factory = new DefaultMessageHandlerMethodFactory();
        // 这里的转换器设置实现了 通过 @Payload 注解 自动反序列化message body
        factory.setMessageConverter(new MappingJackson2MessageConverter());

        return factory;
    }

    // 提供自定义RabbitTemplate,将对象序列化为json串
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        // 这里的转换器设置实现了发送消息时自动序列化消息对象为message body
        return rabbitTemplate;
    }

    /*@Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setConcurrentConsumers(3);
        factory.setMaxConcurrentConsumers(10);
        // 设置手动签收
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        // 每次抓取一条数据
        factory.setPrefetchCount(1);

        return factory;
    }*/
}
```



添加消息消费者：

```java
@Component
public class ObjListener {
    @SneakyThrows
    @RabbitListener(queues = "confirms-queue")
    public void getObj(@Payload RabbitBean rabbitBean, Message message) {
        System.out.println(new String(message.getBody()));
        System.out.println(rabbitBean);
    }
```



测试结果：

可以看到发送出来的消息是Json格式的：

![image-20220808141452674](../../md-photo/image-20220808141452674.png)



消费端进行消费：

![image-20220808141553191](../../md-photo/image-20220808141553191.png)



## RabbitMQUtils

```java
package com.base.utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.SneakyThrows;

/**
 * RabbitMQ的工具操作类
 */
public class RabbitMQUtils {

    /**
     * RabbitMQ连接工厂
     */
    private static class Factory {
        private static ConnectionFactory factory;

        static {
            factory = new ConnectionFactory();
            factory.setHost("rabbitmq1");// IP地址 默认localhost
            factory.setPort(5672);// 端口，默认5672
            factory.setVirtualHost("/itcast");// 虚拟机，默认值/
            factory.setUsername("bobo");// 用户名，默认guest
            factory.setPassword("123456");// 密码， 默认guest
        }
    }

    /**
     * 获取连接工厂
     */
    public static ConnectionFactory getFactory() {
        return Factory.factory;
    }

    /**
     * 创建连接
     */
    @SneakyThrows
    public static Connection newConnection() {
        return getFactory().newConnection();
    }

    /**
     * 创建Channel
     */
    @SneakyThrows
    public static Channel createChannel() {
        return newConnection().createChannel();
    }


    /**
     * 释放资源
     */
    @SneakyThrows
    public static void clear(Channel channel) {
        channel.close();
        channel.getConnection().close();
    }
}
```

