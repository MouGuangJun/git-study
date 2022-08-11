# RabbitMQ高级

## 消息的可靠性传递

在使用rabbitmq的时候，消息的发送方会杜绝消息的丢失或者投递失败的场景，所以rabbitmq为我们提供了两种解决方式：

- **confirm 确认模式**
- **return 退回模式**

rabbitmq整个消息投递的路径为：
producer—>rabbitmq broker—>exchange—>queue—>consumer

- 当消息从producer—>exchange 会返回一个confirmReturn;
- 消息从producer—>queue 会返回一个returnCallback

我们利用这两个来保证消息的可靠性投递



### confirm 确认模式

**<font color='red'>当消息从producer—>exchange 会返回一个confirmReturn。只有当到达交换机失败了才会执行</font>**

案例：

SpringBoot核心配置文件：

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
    # 开启确认模式
    publisher-confirm-type: correlated
```



队列、交换机配置：

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



测试类：

```java
@Test
public void testConfirms() {
    // 定义确定回调函数
    rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
        System.out.println("执行了confirm确认回调函数......");
        if (ack) {
            System.out.println("消息发送成功......" + cause);
        } else {
            System.out.println("消息发送失败......" + cause);
        }
    });

    rabbitTemplate.convertAndSend("confirms-exchange1", "confirms-route", "hello, confirms......");
}
```



测试结果：

成功发送了消息执行了回调函数、交换机确定收到了消息：

![image-20220807170648463](../../md-photo/image-20220807170648463.png)



成功发送了消息执行了回调函数、交换机没有收到了消息：

![image-20220807170831102](../../md-photo/image-20220807170831102.png)



### return 退回模式

**<font color='red'>当消息发送给Exchange后，Exchange路由到Queue失败时，才会执行ReturnCallBack。只有当到达交换机了，但是路由失败的时候才会执行</font>**

案例：

Spring核心配置文件：

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
    # 开启回退模式
    publisher-returns: true
```



*队列、交换机配置借用confirm确认模式的配置*



测试类：

```java
/**
 * 回退模式：当消息发送给Exchange后，Exchange路由到Queue失败时，才会执行ReturnCallBack
 * 步骤：
 * 1.开启回退模式
 * 2.设置ReturnCallBack
 */
@Test
public void testReturn() {
    // 设置ReturnCallBack
    rabbitTemplate.setReturnsCallback(returned -> {
        System.out.println("return执行了......");
        // 消息
        System.out.println(returned.getMessage());
        // 错误码
        System.out.println(returned.getReplyCode());
        // 错误提示
        System.out.println(returned.getReplyText());
        // 交换机
        System.out.println(returned.getExchange());
        // 路由key
        System.out.println(returned.getRoutingKey());
    });

    rabbitTemplate.convertAndSend("confirms-exchange", "confirms-route1", "hello, confirms......");
}
```



测试结果：

消息路由到队列失败，返回对应的错误处理信息及生产者发送的相关消息信息

![image-20220807172427173](../../md-photo/image-20220807172427173.png)



## Consumer Ack

ack值Acknowledge，确认。表示消费端收到消息后的确认方式。

消费者有三种接收消息的方式：

- ackknowledge=none 自动确认
- ackknowledge=manual 手动确认
- ackknowledge=auto 根据异常情况确认



**默认自动确认**，消息被consumer取到后，会自动确认，同时将队列中的该消息移除

我们也可以修改消息为手动确认，**手动ack需要调用channel.basicAck()**

如果出现异常，可以调用**channel.basicNack()/channel.basicReject()，进行重复发送**

一般**推荐手动确认**，保证消息的可靠性



案例：

SpringBoot核心配置文件：

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
    # consumer设置手动签收
    listener:
      direct:
        acknowledge-mode: manual
```



测试类：

```java
@Component
public class AckListener {

    /**
     * 1.consumer设置手动签收
     * 2.给监听器添加channel参数
     * 3.如果消息处理成功，则调用channel.basicAck()签收
     * 3.如果消息处理失败，则调用channel.basicNack()拒绝签收，broker重新发送给consumer
     */
    @SneakyThrows
    @RabbitListener(queues = {"confirms-queue"})
    public void ackListener(Message message, Channel channel) {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try { // 1.接收消息
            System.out.println(new String(message.getBody()));

            // 2.业务逻辑处理
            System.out.println("处理业务逻辑......");

            int i = 1 / 0;
            // 3.手动签收
            // deliveryTag： 消息投递序号，每个channel对应一个(long类型)，从1开始到9223372036854775807范围
            // multiple：一次签收多条消息
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            // e.printStackTrace();

            // 4.拒绝签收，requeue：true->重新回到队列，false-> 不重新回到队列
            channel.basicNack(deliveryTag, false, true);
        }
    }
}
```



测试结果，当存在int i = 1 / 0（**故意抛出错误**）的逻辑代码时：

会一直进行重试的操作：

![image-20220807180431950](../../md-photo/image-20220807180431950.png)



如果业务逻辑处理正常，消费消息成功：

![image-20220807180511127](../../md-photo/image-20220807180511127.png)



## 消费端限流

![image](../../md-photo/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA5byC5pe256m6,size_20,color_FFFFFF,t_70,g_se,x_16.png)



案例：

SpringBoot核心配置文件：

<font color='red'>注意：这里的手动签收配置的是：spring.rabbitmq.listener.**simple**.acknowledge-mode=manual，而不是spring.rabbitmq.listener.**direct**.acknowledge-mode=manual</font>

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
    listener:
      direct:
        acknowledge-mode: manual
      simple:
        # 设置签收的方式为手动签收
        acknowledge-mode: manual
        # 每次抓取一条数据
        prefetch: 1
```



发送端代码（使用**消息的可靠性传递**的发送端进行信息发送）：

```java
@Test
public void testSend() {
    for (int i = 0; i < 10; i++) {
        rabbitTemplate.convertAndSend("confirms-exchange", "confirms-route", "hello, confirms......");
    }
}
```



消费端代码：

```java
/**
 * Consumer限流
 * 1.确保ack机制为手动确认
 * 2.spring.rabbitmq.listener.simple配置属性
 * acknowledge-mode: manual， 表示设置签收模式为手动签收
 * prefetch = 1000，表示消费端每次从mq拉去1000条消息，直到手动确认消费完毕之后，才会继续拉取下一批次消息
 */
@Component
public class QosListener {

    @SneakyThrows
    @RabbitListener(queues = "confirms-queue")
    public void printMsg(String msg, Channel channel, Message message) {

        //设置1秒的睡眠好看变化情况
        // TimeUnit.SECONDS.sleep(1L);
        System.out.println("消息处理成功：" + msg);
        // 手工ack
        // channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

    }
}
```

测试结果：

如果spring.rabbitmq.listener.simple.prefetch=1，且不进行手动签收时，只能拿到一条数据：

![image-20220807231912537](../../md-photo/image-20220807231912537.png)



如果spring.rabbitmq.listener.simple.prefetch=1000，可以看到消息是一次性拿出来的：

![image-20220807231954980](../../md-photo/image-20220807231954980.png)



但是此时的数据仍然未被签收：

![image-20220807232036963](../../md-photo/image-20220807232036963.png)



## TTL（存活时间/过期时间）

### 定义

- TTL全称Time To Live(存活时间/过期时间)。
- 当消息到达存活时间后，还没有被消费，会被自动清除。
- RabbitMQ可以对消息设置过期时间，也可以对整个队列(Queue）设置过期时间。



![img](../../md-photo/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA5oKg54S25LqI5aSP,size_20,color_FFFFFF,t_70,g_se,x_16.png)

### 控制台演示

新建队列：

![image-20220808090614779](../../md-photo/image-20220808090614779.png)



新建交换机：

![image-20220808085831344](../../md-photo/image-20220808085831344.png)



建立交换机与队列的关联关系：

![image-20220808090111207](../../md-photo/image-20220808090111207.png)



绑定路由和队列的关系：

![image-20220808091034299](../../md-photo/image-20220808091034299.png)



发布消息：

![image-20220808091121120](../../md-photo/image-20220808091121120.png)



结果：

![image-20220808091221825](../../md-photo/image-20220808091221825.png)



10秒之后，消息丢失：

![image-20220808091245868](../../md-photo/image-20220808091245868.png)



### 程序演示

#### 队列统一过期

交换机与路由的配置：

![image-20220808201309577](../../md-photo/image-20220808201309577.png)

源码：

```java
@Configuration
public class RabbitTTLConfig {

    // 队列
    @Bean
    public Queue ttlQueue() {
        return QueueBuilder.durable("boot-ttl-queue").ttl(10000).build();
    }

    // 交换机
    @Bean
    public Exchange ttlExchange() {
        return ExchangeBuilder.topicExchange("boot-ttl-exchange").build();
    }

    // 绑定交换机和队列
    @Bean
    public Binding ttlBinding(@Qualifier("ttlQueue") Queue queue,
                              @Qualifier("ttlExchange") Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("ttl.#").noargs();
    }
}
```



发送消息：

```java
@Test
public void testTTL() {
    rabbitTemplate.convertAndSend("boot-ttl-exchange", "ttl.test", "hello, ttl......");
}
```



测试结果：

![image-20220808201424740](../../md-photo/image-20220808201424740.png)



10秒过后，消息被移除：

![image-20220808201449304](../../md-photo/image-20220808201449304.png)



#### 消息单独过期

**<font color='red'>测试前将队列的ttl配置从10秒改为100秒（注意删除队列后再设置），便于观察结果</font>**

在发送消息的时候进行过期时间的设置：

发送消息：

```java
@Test
public void testMessageTTL() {
    MessagePostProcessor messagePostProcessor = message -> {
        // 设置Message的相关信息，这里设置的是过期时间
        message.getMessageProperties().setExpiration("5000");
        // 返回该消息
        return message;
    };


    rabbitTemplate.convertAndSend("boot-ttl-exchange", "ttl.test", "hello, message-ttl......", messagePostProcessor);
}
```



测试结果：

![image-20220808202247048](../../md-photo/image-20220808202247048.png)



可以看到5秒之后，消息失效：

![image-20220808202306009](../../md-photo/image-20220808202306009.png)



#### 结论

- 设置队列过期时间使用参数:**x-message-ttl，单位: ms(毫秒)**，会对整个队列消息统一过期。
- 设置消息过期时间使用参数: **expiration。单位: ms(毫秒)**，当该消息在队列头部时（消费时)，会单独判断这—消息是否过期。
- 如果设置的消息的过期时间，也设置了队列的过期时间，**以时间短的为准**。
- 队列过期后，会将队列中的全部信息移除。
- 消息过期后，**只有消息在队列的顶端，才会判断其是否过期**（如果过期就移除掉，类比redis移除内存的方式）



## 死信队列

### 定义

​	死信队列，英文缩写:DLX 。Dead Letter Exchange(死信交换机)，当消息成为Dead message后，可以被重新发送到另一个交换机，这个交换机就是DLX。

![image-20220808203320787](../../md-photo/image-20220808203320787.png)



### 消息如何成为死信

- 队列消息长度到达限制；
- 消费者拒接消费消息，basicNack/basicReject,并且不把消息重新放入原目标队列,requeue=false；
- 原队列存在消息过期设置，消息到达超时时间未被消费；





### 队列绑定死信交换机

给队列设置参数: 

​	x-dead-letter-exchange：死信交换机名称。

​	x-dead-letter-routing-key：死信交换机和死信队列绑定的路由key

![image-20220808203510998](../../md-photo/image-20220808203510998.png)



### 程序演示

#### 程序步骤

1. 声明正常队列（owner-queue-dlx）和交换机（owner-exchange-dlx）
2. 声明死信队列（dead-queue-dlx）和死信交换机（dead-exchange-dlx）
3. 正常队列绑定到死信交换机
   1. 设置参数：x-dead-letter-exchange：死信交换机名称
   2. 设置参数：x-dead-letter-routing-key：死信交换机发动到死信队列的routingkey
4. 让消息成为死信
   1. 设置队列的过期时间：x-message-ttl
   2. 设置队列的最大长度：x-max-length
   3. 拒绝接收消息并且不发送回原队列



交换机和路由配置：

```java
@Configuration
public class RabbitDeadConfig {

    //------------------------正常队列------------------------
    // 队列
    @Bean
    public Queue ownerQueue() {
        return QueueBuilder.durable("owner-queue-dlx")
                // 设置队列过期时间
                .ttl(10000)
                // 设置队列的最大长度
                .maxLength(10)
                .deadLetterExchange("dead-exchange-dlx")
                .deadLetterRoutingKey("dead.test")
                .build();
    }

    // 交换机
    @Bean
    public Exchange ownerExchange() {
        return ExchangeBuilder.topicExchange("owner-exchange-dlx").build();
    }

    // 绑定交换机和队列
    @Bean
    public Binding ownerBinding(@Qualifier("ownerQueue") Queue queue,
                                @Qualifier("ownerExchange") Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("owner.#").noargs();
    }

    //------------------------死信队列------------------------
    // 队列
    @Bean
    public Queue deadQueue() {
        return QueueBuilder.durable("dead-queue-dlx").build();
    }

    // 交换机
    @Bean
    public Exchange deadExchange() {
        return ExchangeBuilder.topicExchange("dead-exchange-dlx").build();
    }

    // 绑定交换机和队列
    @Bean
    public Binding deadBinding(@Qualifier("deadQueue") Queue queue,
                               @Qualifier("deadExchange") Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("dead.#").noargs();
    }
```



#### 队列过期

生产者

```java
@Test
public void testDeadTTL() {
    rabbitTemplate.convertAndSend("owner-exchange-dlx", "owner.test", "发送消息到队列，但是它会死......");
}
```



测试结果：

![image-20220808210655187](../../md-photo/image-20220808210655187.png)



10秒钟后，由于队列失效，导致消息被推送至死信队列：

![image-20220808210707925](../../md-photo/image-20220808210707925.png)



#### 超出队列最大长度

生产者：

```java
@Test
public void testDeadMaxLen() {
    for (int i = 0; i < 20; i++) {
        rabbitTemplate.convertAndSend("owner-exchange-dlx", "owner.test", "发送消息到队列，但是超出了长度......");
    }
}
```



测试结果：

![image-20220808211632098](../../md-photo/image-20220808211632098.png)



消费者拒收消息：

消费者：

```java
@Component
public class DeadListener {

    @SneakyThrows
    @RabbitListener(queues = {"owner-queue-dlx"})
    public void refuseMsg(Message message, Channel channel) {
        System.out.println(new String(message.getBody()));

        channel.basicNack(message.getMessageProperties().getDeliveryTag(), true, false);
    }
}
```



测试结果：

可以看到拒收的消息全部在死信队列中：

![image-20220808212348661](../../md-photo/image-20220808212348661.png)



#### 总结

1. 死信交换机和死信队列和普通的没有区别
2. 当消息成为死信后，如果该队列绑定了死信交换机，则消息会被死信交换机重新路由到死信队列



## 延迟队列

### 定义

延迟队列，即消息进入队列后不会立即被消费，只有到达指定时间后，才会被消费。

需求：

1. 下单后，30分钟未支付，取消订单，回滚库存。
2. 新用户注册成功7天后，发送短信问候。

实现方式：

1. 定时器
2. 延迟队列

![img](../../md-photo/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA5oKg54S25LqI5aSP,size_20,color_FFFFFF,t_70,g_se,x_16-16599664241943.png)

很可惜，在**RabbitMQ中并未提供延迟队列功能**。

但是可以使用：<font color='red'>TTL</font>+<font color='red'>死信队列</font> 组合实现延迟队列的效果

![img](../../md-photo/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA5oKg54S25LqI5aSP,size_20,color_FFFFFF,t_70,g_se,x_16-16599664763766.png)



### 程序演示

#### 程序步骤

1. 定义正常交换机（order_exchange）和队列（order_queue）
2. 定义死信交换机（order_exchange_dlx）和队列（order_queue_dlx）
3. 绑定，设置正常队列的时间为30分钟
4. 对死信队列进行消费，查询订单是否支付，如果未支付，执行回滚操作



#### 生产者

队列和交换机的配置：

```java
@Configuration
public class RabbitDelayConfig {
    //------------------------正常队列------------------------
    // 队列
    @Bean
    public Queue orderQueue() {
        return QueueBuilder.durable("order_queue")
                .ttl(10000)
                .deadLetterExchange("order_exchange_dlx")
                .deadLetterRoutingKey("order.dlx.cancell")
                .build();
    }

    // 交换机
    @Bean
    public Exchange orderExchange() {
        return ExchangeBuilder.topicExchange("order_exchange").build();
    }

    // 绑定交换机和队列
    @Bean
    public Binding orderBinding(@Qualifier("orderQueue") Queue queue,
                                @Qualifier("orderExchange") Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("order.#").noargs();
    }

    //------------------------死信队列------------------------
    @Bean
    public Queue orderDlxQueue() {
        return QueueBuilder.durable("order_queue_dlx").build();
    }

    // 交换机
    @Bean
    public Exchange orderDlxExchange() {
        return ExchangeBuilder.topicExchange("order_exchange_dlx").build();
    }

    // 绑定交换机和队列
    @Bean
    public Binding orderDlxBinding(@Qualifier("orderDlxQueue") Queue queue,
                                   @Qualifier("orderDlxExchange") Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("order.dlx.#").noargs();
    }
}
```



发送订单消息：

```java
@Test
public void testDelay() {
    rabbitTemplate.convertAndSend("order_exchange", "order.msg", "下单成功，请及时支付，30分钟自动取消......");
}
```



#### 消费者

```java
@Component
public class OrderListener {

    @SneakyThrows
    @RabbitListener(queues = {"order_queue_dlx"})
    public void confirmOrder(Message message, Channel channel) {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        System.out.println(new String(message.getBody()));

        // 执行相关的业务逻辑操作
        try {
            System.out.println("查询当前订单是否支付......");
            System.out.println("当前订单超时未支付......");
            System.out.println("执行取消订单的操作......");
            channel.basicAck(deliveryTag, true);
        } catch (Exception e) {
            channel.basicNack(deliveryTag, true, true);
        }
    }
}
```



测试结果：

订单超过30分钟未支付，被放到了死信队列：

![image-20220808220815462](../../md-photo/image-20220808220815462.png)



对死信队列中的订单判断是否支付，如果未支付，将库存回滚：

![image-20220808220958382](../../md-photo/image-20220808220958382.png)



### 总结

1. 延迟队列 指消息进入队列后，可以被延迟一定时间，再进行消费。
2. RabbitMQ没有提供延迟队列功能，但是可以使用 ： TTL + DLX 来实现延迟队列效果



## 日志与监控

### RabbitMQ日志

RabbitMQ默认日志存放路径:/var/log/rabbitmq/rabbit@xxx.log



### 常用监控命令

| 命令                                                 | 作用                   |
| ---------------------------------------------------- | ---------------------- |
| rabbitmqctl list_queues                              | 查看队列               |
| rabbitmqctl environment                              | 查看环境变量           |
| rabbitmqctl list_exchanges                           | 查看exchanges          |
| rabbitmqctl list_queues name messages_unacknowledged | 查看未被确认的队列     |
| rabbitmqctl list_users                               | 查看用户               |
| rabbitmqctl list_queues name memory                  | 查看单个队列的内存使用 |
| rabbitmqctl list_connections                         | 查看连接               |
| rabbitmqctl list_queues name messages_ready          | 查看准备就绪的队列     |
| rabbitmqctl list_consumers                           | 查看消费者信息         |



## 消息追踪

​	在使用任何消息中间件的过程中，难免会出现某条消息异常丢失的情况。对于RabbitMQ而言，可能是因为生产者或消费者与RabbitMQ断开了连接，而它们与RabbitMQ又采用了不同的确认机制;也有可能是因为交换器与队列之间不同的转发策略;甚至是交换器并没有与任何队列进行绑定，生产者又不感知或者没有采取相应的措施;另外RabbitMQ本身的集群策略也可能导致消息的丢失。这个时候就需要有一个较好的机制跟踪记录消息的投递过程，以此协助开发和运维人员进行问题的定位。在RabbitMQ中可以使用Firehose和rabbitmq_tracing插件功能来实现消息追踪。



### firehose

​	firehose的机制是将生产者投递给rabbitmq的消息，rabbitmq投递给消费者的消息按照指定的格式发送到默认的exchange上。这个默认的exchange的名称为**amq.rabbitmq.trace**，它是一个topic类型的exchange。发送到这个exchange上的消息的routing key为 **publish.exchangename和deliver.queuename**。其中exchangename和queuename为实际exchange和queue的名称，分别对应生产者投递到exchange的消息，和消费者从queue上获取的消息。

注意：**<font color='red'>打开trace 会影响消息写入功能，适当打开后请关闭</font>**。
	rabbitmqctl trace_on:开启Firehose命令
	rabbitmqctl trace_off:关闭Firehose命令



测试使用：

新建交换机：

![image-20220808223925552](../../md-photo/image-20220808223925552.png)



绑定到记录日志的交换机上：

![image-20220808230258363](../../md-photo/image-20220808230258363.png)



启用插件功能：

```bash
$ rabbitmqctl trace_on [-p vhost（指定虚拟机）]
```

 ![image-20220808230104684](../../md-photo/image-20220808230104684.png)



向test-trace队列中发送一条消息：

![image-20220808224440343](../../md-photo/image-20220808224440343.png)



可以看到amq.rabbitmq.trace交换机也会发送一条记录日志的消息：

![image-20220808230549354](../../md-photo/image-20220808230549354.png)



![image-20220808230744646](../../md-photo/image-20220808230744646.png)





### rabbitmq_tracing

**<font color='red'>注意：需要给guest相应虚拟机的权限，否则会添加trace时会报错，这里需要给/itcast的权限</font>**

rabbitmq_tracing和Firehose在实现上如出一辙，只不过rabbitmq_tracing的方式比Firehose多了层GUI的包装，更容易使用和管理。

**<font color='red'>开启该插件会影响rabbitmq的性能，需要谨慎开启</font>**

启用插件:

```bash
# 查看插件,其中e*代表的是已经启用的插件
$ rabbitmq-plugins list
Listing plugins with pattern ".*" ...
 Configured: E = explicitly enabled; e = implicitly enabled
 | Status: * = running on rabbit@rabbit1
 |/
[  ] rabbitmq_amqp1_0                  3.9.21
[  ] rabbitmq_auth_backend_cache       3.9.21
[  ] rabbitmq_auth_backend_http        3.9.21
[  ] rabbitmq_auth_backend_ldap        3.9.21
[  ] rabbitmq_auth_backend_oauth2      3.9.21
[  ] rabbitmq_auth_mechanism_ssl       3.9.21
[  ] rabbitmq_consistent_hash_exchange 3.9.21
[  ] rabbitmq_event_exchange           3.9.21
[  ] rabbitmq_federation               3.9.21
[  ] rabbitmq_federation_management    3.9.21
[  ] rabbitmq_jms_topic_exchange       3.9.21
[E*] rabbitmq_management               3.9.21
[e*] rabbitmq_management_agent         3.9.21
[  ] rabbitmq_mqtt                     3.9.21
[  ] rabbitmq_peer_discovery_aws       3.9.21
[  ] rabbitmq_peer_discovery_common    3.9.21
[  ] rabbitmq_peer_discovery_consul    3.9.21
[  ] rabbitmq_peer_discovery_etcd      3.9.21
[  ] rabbitmq_peer_discovery_k8s       3.9.21
[  ] rabbitmq_prometheus               3.9.21
[  ] rabbitmq_random_exchange          3.9.21
[  ] rabbitmq_recent_history_exchange  3.9.21
[  ] rabbitmq_sharding                 3.9.21
[  ] rabbitmq_shovel                   3.9.21
[  ] rabbitmq_shovel_management        3.9.21
[  ] rabbitmq_stomp                    3.9.21
[  ] rabbitmq_stream                   3.9.21
[  ] rabbitmq_stream_management        3.9.21
[  ] rabbitmq_top                      3.9.21
[  ] rabbitmq_tracing                  3.9.21
[  ] rabbitmq_trust_store              3.9.21
[e*] rabbitmq_web_dispatch             3.9.21
[  ] rabbitmq_web_mqtt                 3.9.21
[  ] rabbitmq_web_mqtt_examples        3.9.21
[  ] rabbitmq_web_stomp                3.9.21
[  ] rabbitmq_web_stomp_examples       3.9.21


# 启用rabbitmq_tracing插件
$ rabbitmq-plugins enable rabbitmq_tracing
```



![image-20220808231216306](../../md-photo/image-20220808231216306.png)

添加日志记录trace：

![image-20220808231650103](../../md-photo/image-20220808231650103.png)



随便对一个队列发送消息：

![image-20220808231529244](../../md-photo/image-20220808231529244.png)



可以看到日志成功被记录：

![image-20220808232637421](../../md-photo/image-20220808232637421.png)



插件自动帮我们创建了记录日志的队列：

![image-20220808232850554](../../md-photo/image-20220808232850554.png)



该队列自动与**amq.rabbitmq.trace**交换机进行绑定：

![image-20220808232930188](../../md-photo/image-20220808232930188.png)



## 应用问题

### 消息补偿

![image-20220809230459607](../../md-photo/image-20220809230459607.png)



### 幂等性保障

![image-20220809230800067](../../md-photo/image-20220809230800067.png)
