# SpringBatch

## 简介

Spring Batch是Spring提供的一个数据处理框架。企业域中的许多应用程序需要批量处理才能在关键任务环境中执行业务操作。 这些业务运营包括：

- 无需用户交互即可最有效地处理大量信息的自动化，复杂处理。 这些操作通常包括基于时间的事件（例如月末计算，通知或通信）。
- 在非常大的数据集中重复处理复杂业务规则的定期应用（例如，保险利益确定或费率调整）。
- 集成从内部和外部系统接收的信息，这些信息通常需要以事务方式格式化，验证和处理到记录系统中。 批处理用于每天为企业处理数十亿的交易。

Spring Batch是一个轻量级，全面的批处理框架，旨在开发对企业系统日常运营至关重要的强大批处理应用程序。 Spring Batch构建了人们期望的Spring Framework特性（生产力，基于POJO的开发方法和一般易用性），同时使开发人员可以在必要时轻松访问和利用更高级的企业服务。 Spring Batch不是一个Schuedling的框架。

Spring Batch提供了可重用的功能，这些功能对于处理大量的数据至关重要，包括记录/跟踪，事务管理，作业处理统计，作业重启，跳过和资源管理。 它还提供更高级的技术服务和功能，通过优化和分区技术实现极高容量和高性能的批处理作业。 Spring Batch可用于两种简单的用例（例如将文件读入数据库或运行存储过程）以及复杂的大量用例（例如在数据库之间移动大量数据，转换它等等） 上）。 大批量批处理作业可以高度可扩展的方式利用该框架来处理大量信息。

Spring Batch架构介绍
一个典型的批处理应用程序大致如下：

- 从数据库，文件或队列中读取大量记录。
- 以某种方式处理数据。
- 以修改之后的形式写回数据。

其对应的示意图如下：

![img](../../md-photo/20190109164353485.png)

Spring Batch的一个总体的架构如下：

![image](../../md-photo/format,png.png)

在Spring Batch中一个Job可以定义很多的步骤Step，在每一个Step里面可以定义其专属的ItemReader用于读取数据，ItemProcesseor用于处理数据，ItemWriter用于写数据，而每一个定义的Job则都在JobRepository里面，我们可以通过JobLauncher来启动某一个Job。


## 搭建Spring Batch项目

引入依赖：

**<font color='red'>Spring Batch依赖于数据库，所以必须配置数据库的相关信息</font>**

```xml
<!--Spring Batch依赖-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-batch</artifactId>
</dependency>

<!--jdbc-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-jdbc</artifactId>
</dependency>

<!--mysql驱动-->
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
</dependency>

<!--mybatis-plus-->
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-boot-starter</artifactId>
</dependency>

<!-- Spring-Batch 4.3.0版本以上时，Jackson需要在2.11.x版本以上 -->
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
</dependency>

<!--spring-web starter-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
</dependency>
```



yml相关配置：

**<font color='red'>Spring Batch依赖的数据库表结构在/org/springframework/batch/core/schema-mysql.sql目录下，直接手动到数据库中创建对应的表即可</font>**

```yml
server:
  port: 8098

spring:
  application:
    name: spring-batch

  # 数据库相关配置
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost/spring?serverTimezone=GMT%2B8&useUnicode=true&characterEncoding=utf8&autoReconnect=true&allowMultiQueries=true
    username: root
    password: 123456

  #指定初始化数据库表的位置，手动执行对应的sql脚本即可
  #  sql:
  #    init:
  #      schema-locations: classpath:/org/springframework/batch/core/schema-mysql.sql
  #  batch:
  #    # Spring Batch需要将job相关内容保存到不同的数据表中，此时需要保证数据库中存在此内容，设置always来创建对应表结构
  #    jdbc:
  #      initialize-schema: always
  batch:
    # 配置用于启动时是否创建JobLauncherCommandLineRunner, true则会创建JobLauncherCommandLineRunner实例并且执行，否则不会创建实例
    job:
      enabled: true

#slf4j
logging:
  config: D:/IDEAWorkSpace/gmall-web/spring-batch/src/main/resources/logback.xml
```



主启动类添加<font color='bold'>@EnableBatchProcessing</font>注解：

![image-20220905200909140](../../md-photo/image-20220905200909140.png)



## HelloWorld

```java
package com.spring.batch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HelloWorldConfig {
    // 注入负责创建Job（任务）对象的工场
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    // 任务的执行由Step决定
    // 注入创建Step（步骤）对象的工厂
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    // 创建任务（Job）对象
    @Bean
    public Job helloWorld() {
        return jobBuilderFactory.get("helloWorldJob")
                .start(stepHello()).build();
    }

    // 创建步骤（Step）对象
    @Bean
    public Step stepHello() {
        return stepBuilderFactory.get("stepHello")
                .tasklet(((contribution, chunkContext) -> {
                    System.out.println("Hello, World！");
                    // 步骤正常结束
                    return RepeatStatus.FINISHED;
                })).build();
    }
}
```

启动Spring Boot，得到以下结果：

![image-20220905202517374](../../md-photo/image-20220905202517374.png)



## Job创建使用

JobBuilderFactory负责创建Job任务：

- jobBuilderFactory.get("jobName") get里面是任务的名称我们先使用我们自己的这个Job名称
- start是执行任务的最终step，一个任务是可以由多个step组成的
- 多个step的执行我们可以使用next()继续执行下一个step

```java
return jobBuilderFactory.get( "hellowordJob")

.start(hellostep())

.next(wordStep()).build();
```

也可以使用on. . .to. . .from的方式实现：

- 其中on表示的是一个条件，是你前面step执行成功的一个条件，只有满足了才可以开始下一个，
- to是你下一个需要执行的是哪一个step，from表示你多个step的时候，是从那一个from继续开始往下面走
- end是表示没有下一个step了
- 在on后面可以调用to的方法也可以调用fail()方法，表示失败了，不执行下一个
- stopAndRestart()停止前面一个方法并重新启动

```java
@Configuration
public class JobConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job jobDemoJob() {
        return jobBuilderFactory.get("jobDemoJob")
//                .start(step1())
//                .next(step2())
//                .next(step3())
                .start(step1()).on(BatchStatus.COMPLETED.name()).to(step2())
                .from(step2()).on(BatchStatus.COMPLETED.name()).to(step3())
                .from(step3()).end()
                // 可以start多个step
                //.start(step2()).build()
                .build();

    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .tasklet(((contribution, chunkContext) -> {
                    System.out.println("step1，执行了！");
                    return RepeatStatus.FINISHED;
                })).build();
    }

    @Bean
    public Step step2() {
        return stepBuilderFactory.get("step2")
                .tasklet(((contribution, chunkContext) -> {
                    System.out.println("step2，执行了！");
                    return RepeatStatus.FINISHED;
                })).build();
    }

    @Bean
    public Step step3() {
        return stepBuilderFactory.get("step3")
                .tasklet(((contribution, chunkContext) -> {
                    System.out.println("step3，执行了！");
                    return RepeatStatus.FINISHED;
                })).build();
    }
}
```

结果：

![image-20220905214621643](../../md-photo/image-20220905214621643.png)



## Flow的创建和使用

可以将多个step放到一起执行：

```java
@Configuration
public class FlowConfig {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    // 创建Job对象，使用flow可以将多个step放到一块执行
    @Bean
    public Job flowDemoJob() {
        return jobBuilderFactory.get("flowDemoFlow")
                .start(flowDemoFlow())
                // 还可以继续执行step
                .next(flowDemoStep3()).end()
                .build();
    }


    // 创建Flow对象，指明Flow对象包含哪些step
    @Bean
    public Flow flowDemoFlow() {
        return new FlowBuilder<Flow>("flowDemoFlow")
                .start(flowDemoStep1())
                .next(flowDemoStep2())
                .build();
    }

    @Bean
    public Step flowDemoStep1() {
        return stepBuilderFactory.get("flowDemoStep1")
                .tasklet(((contribution, chunkContext) -> {
                    System.out.println("flowDemoStep1，执行了！");
                    return RepeatStatus.FINISHED;
                })).build();
    }

    @Bean
    public Step flowDemoStep2() {
        return stepBuilderFactory.get("flowDemoStep2")
                .tasklet(((contribution, chunkContext) -> {
                    System.out.println("flowDemoStep1，执行了！");
                    return RepeatStatus.FINISHED;
                })).build();
    }


    @Bean
    public Step flowDemoStep3() {
        return stepBuilderFactory.get("flowDemoStep3")
                .tasklet(((contribution, chunkContext) -> {
                    System.out.println("flowDemoStep3，执行了！");
                    return RepeatStatus.FINISHED;
                })).build();
    }
}
```

结果：

![image-20220905222001797](../../md-photo/image-20220905222001797.png)



## split并发执行

可以使用split方法，让多个flow/step并发执行。

```java
@Configuration
public class SplitConfig {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job splitJobDemo() {
        return jobBuilderFactory.get("splitJobDemo")
                .start(splitDemoFlow1())
                // 使用split方式实现两个Flow并发执行
                .split(new SimpleAsyncTaskExecutor())
                .add(splitDemoFlow2())
                .end()
                .build();
    }

    @Bean
    public Flow splitDemoFlow1() {
        return new FlowBuilder<Flow>("splitDemoFlow1")
                .start(splitDemoStep1())
                .build();
    }

    @Bean
    public Flow splitDemoFlow2() {
        return new FlowBuilder<Flow>("splitDemoFlow2")
                .start(splitDemoStep2())
                .next(splitDemoStep3())
                .build();
    }

    @Bean
    public Step splitDemoStep1() {
        return stepBuilderFactory.get("splitDemoStep1")
                .tasklet(((contribution, chunkContext) -> {
                    System.out.println("splitDemoStep1，执行了！");
                    return RepeatStatus.FINISHED;
                })).build();
    }

    @Bean
    public Step splitDemoStep2() {
        return stepBuilderFactory.get("splitDemoStep2")
                .tasklet(((contribution, chunkContext) -> {
                    System.out.println("splitDemoStep2，执行了！");
                    return RepeatStatus.FINISHED;
                })).build();
    }

    @Bean
    public Step splitDemoStep3() {
        return stepBuilderFactory.get("splitDemoStep3")
                .tasklet(((contribution, chunkContext) -> {
                    System.out.println("splitDemoStep3，执行了！");
                    return RepeatStatus.FINISHED;
                })).build();
    }
}
```

结果，flow并发执行：

![image-20220905222656967](../../md-photo/image-20220905222656967.png)



## 决策器使用

使用决策器可以根据条件判断Step的执行走向：

```java
@Configuration
public class DeciderConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private MyDecider myDecider;


    @Bean
    public Job deciderDemoJob() {
        return jobBuilderFactory.get("deciderDemoJob")
                .start(deciderDemoStep1())
                .next(myDecider)
                .from(myDecider).on("even").to(deciderDemoStep2())
                .from(myDecider).on("odd").to(deciderDemoStep3())
                // step3执行后，无论返回什么都回到myDecider重新开始执行
                // 执行顺序为step1 -> step3 -> step2
                .from(deciderDemoStep3()).on("*").to(myDecider)
                .end()
                .build();
    }


    @Bean
    public Step deciderDemoStep1() {
        return stepBuilderFactory.get("deciderDemoStep1")
                .tasklet(((contribution, chunkContext) -> {
                    System.out.println("deciderDemoStep1，执行了！");
                    return RepeatStatus.FINISHED;
                })).build();
    }

    @Bean
    public Step deciderDemoStep2() {
        return stepBuilderFactory.get("deciderDemoStep2")
                .tasklet(((contribution, chunkContext) -> {
                    System.out.println("deciderDemoStep2，执行了！");
                    return RepeatStatus.FINISHED;
                })).build();
    }

    @Bean
    public Step deciderDemoStep3() {
        return stepBuilderFactory.get("deciderDemoStep3")
                .tasklet(((contribution, chunkContext) -> {
                    System.out.println("deciderDemoStep3，执行了！");
                    return RepeatStatus.FINISHED;
                })).build();
    }
}
```



MyDecider：

```java
@Component
public class MyDecider implements JobExecutionDecider {
    private int count;

    @Override
    public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
        count++;
        if (count % 2 == 0) {
            return new FlowExecutionStatus("even");
        }

        return new FlowExecutionStatus("odd");
    }
}
```



结果：

![image-20220905224058864](../../md-photo/image-20220905224058864.png)



## Job的嵌套

一个Job可以嵌套在另一个Job中，被嵌套的Job称为子Job，外部Job称为父Job。子Job不能单独执行，需要由父Job来启动。

子Job1：

```java
@Configuration
public class ChildJob1Config {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job childJob1() {
        return jobBuilderFactory.get("childJob1")
                .start(childJob1Step1())
                .build();
    }

    @Bean
    public Step childJob1Step1() {
        return stepBuilderFactory.get("childJob1Step1")
                .tasklet(((contribution, chunkContext) -> {
                    System.out.println("childJob1Step1，执行了！");
                    return RepeatStatus.FINISHED;
                })).build();
    }
}
```



子Job2：

```java
@Configuration
public class ChildJob2Config {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job childJob2() {
        return jobBuilderFactory.get("childJob2")
                .start(childJob2Step1())
                .next(childJob2Step2())
                .build();
    }

    @Bean
    public Step childJob2Step1() {
        return stepBuilderFactory.get("childJob2Step1")
                .tasklet(((contribution, chunkContext) -> {
                    System.out.println("childJob2Step1，执行了！");
                    return RepeatStatus.FINISHED;
                })).build();
    }

    @Bean
    public Step childJob2Step2() {
        return stepBuilderFactory.get("childJob2Step2")
                .tasklet(((contribution, chunkContext) -> {
                    System.out.println("childJob2Step2，执行了！");
                    return RepeatStatus.FINISHED;
                })).build();
    }
}
```



父Job：

配置子Job的时候需要注意：

- 需要使用launcher指定为通过父Job启动。
- 需要指定Job的持久化存储对象。
- 需要指定对应的事务管理器。

```java
@Configuration
public class NestedJobConfig {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Resource(name = "childJob1")
    private Job childJob1;

    @Resource(name = "childJob2")
    private Job childJob2;

    @Autowired
    private JobLauncher jobLauncher;

    @Bean
    public Job parentJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return jobBuilderFactory.get("parentJob")
                .start(childJob1(jobRepository, transactionManager))
                .next(childJob2(jobRepository, transactionManager))
                .build();
    }

    // 返回的是Job类型的Step，是特殊类型的Step
    public Step childJob1(JobRepository jobRepository, PlatformTransactionManager transactionManager) {

        return new JobStepBuilder(new StepBuilder("childJob1"))
                .job(childJob1)
                .launcher(jobLauncher)// 使用启动父Job的启动对象
                .repository(jobRepository)// 指定Job持久化存储对象
                .transactionManager(transactionManager)// 指定事务管理器
                .build();
    }

    public Step childJob2(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobStepBuilder(new StepBuilder("childJob2"))
                .job(childJob2)
                .launcher(jobLauncher)// 使用启动父Job的启动对象
                .repository(jobRepository)// 指定Job持久化存储对象
                .transactionManager(transactionManager)// 指定事务管理器
                .build();
    }
}
```



子Job不能单独启动，因此，spring核心配置文件需要指定运行Job的名称：

applicaiton.yml中添加配置文件：

```yaml
# 指定需要运行的Job
spring:
  batch:
    job:
      names: parentJob
```



结果：

![image-20220905230817174](../../md-photo/image-20220905230817174.png)



## 监听器使用

可以使用监听器对Job/Step进行监听，实现对应的接口，或者通过注解的方式实现：

实现接口的Job监听：

```java
@Component
public class MyJobListener implements JobExecutionListener {
    @Override
    public void beforeJob(JobExecution jobExecution) {
        System.out.println(jobExecution.getJobInstance().getJobName() + "before......");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        System.out.println(jobExecution.getJobInstance().getJobName() + "after......");
    }
}
```



通过注解方式的Step监听：

```java
@Component
public class MyChunkListener {

    @BeforeChunk
    public void beforeChunk(ChunkContext chunkContext) {
        System.out.println(chunkContext.getStepContext().getStepName() + "before......");
    }


    @AfterChunk
    public void afterChunk(ChunkContext chunkContext) {
        System.out.println(chunkContext.getStepContext().getStepName() + "after......");
    }
}
```



配置对应的Job任务：

```java
@Configuration
public class JobListenerConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private MyJobListener myJobListener;

    @Autowired
    private MyChunkListener myChunkListener;

    @Bean
    public Job listenerJob() {
        return jobBuilderFactory.get("listenerJob")
                .start(listenerStep()).listener(myJobListener)
                .build();
    }

    @Bean
    public Step listenerStep() {
        return stepBuilderFactory.get("listenerStep1")
                // 每读取两个数据进行一次输出处理
                .chunk(2)
                .faultTolerant()
                .listener(myChunkListener)
                // 读取数据
                .reader(new ListItemReader<>(Arrays.asList("java", "spring", "mybatis")))
                // 写数据
                .writer(items -> items.forEach(System.out::println))
                .build();
    }
}
```



执行结果：

![image-20220906101430285](../../md-photo/image-20220906101430285.png)





## Job参数

Jvm运行参数会被Spring Batch进行解析，从而传递给每一个Job任务。

![image-20220906105214433](../../md-photo/image-20220906105214433.png)



获取任务的参数：

```java
contribution.getStepExecution().getJobParameters().getParameters();
```



完整代码：

```java
@Configuration
public class JobParametersConfig {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job parameterJob() {
        return jobBuilderFactory.get("parameterJob")
                .start(parameterStep())
                .build();
    }

    // Job执行的是Step，Job的参数最终传递给Step进行使用
    // 只需要给Step传递参数
    // 使用监听，使用Step级别的监听来传递数据
    @Bean
    public Step parameterStep() {
        return stepBuilderFactory.get("parameterStep")
                .tasklet(((contribution, chunkContext) -> {
                    // 将Job/Step监听器传入的参数进行打印
                    System.out.println(contribution.getStepExecution().getJobParameters().getParameters());
                    return RepeatStatus.FINISHED;
                }))
                .build();
    }
}
```



运行结果：

![image-20220906105049137](../../md-photo/image-20220906105049137.png)



## ItemReader概述

### 初识ItemReader

ItemReader进行数据的读取，每个批次读取量依据chunk(2)中定义的数量，**<font color='red'>但是每一次只能读取一个数据</font>**

```java
@Configuration
public class ItemReaderConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job itemReaderDemoJob() {
        return jobBuilderFactory.get("itemReaderDemoJob")
                .start(itemReaderDemoStep())
                .build();
    }

    @Bean
    public Step itemReaderDemoStep() {
        return stepBuilderFactory.get("itemReaderDemoStep")
                // 每一次读取的数据量
                .<String, String>chunk(2)
                .reader(new ItemReader<String>() {
                    private Iterator<String> iterator = Arrays.asList("cat", "dog", "pig", "duck").iterator();

                    @Override
                    public String read() {
                        // 一个一个数据的读
                        if (iterator.hasNext()) {
                            return iterator.next();
                        }
                        // 没有数据时，返回null
                        return null;
                    }
                })
                .writer(items -> items.forEach(System.out::println))
                .build();
    }
}
```



运行结果：

![image-20220906110704949](../../md-photo/image-20220906110704949.png)



### 从数据库中的读取数据

可以使用Spring提供的JDBC分页查询进行数据库的查询的操作：

```java
@Configuration
public class ItemReaderDBConfig {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private HikariDataSource dataSource;

    @Bean
    public Job itemReaderDBDemoJob() {
        return jobBuilderFactory.get("itemReaderDBDemoJob")
                .start(itemReaderDBDemoStep())
                .build();
    }

    @Bean
    public Step itemReaderDBDemoStep() {
        return stepBuilderFactory.get("itemReaderDBDemoStep")
                // 每一次读取的数据量
                .<Students, Students>chunk(2)
                .reader(studentsReader())
                .writer(items -> items.forEach(System.out::println))
                .build();
    }

    @Bean
    @StepScope
    public JdbcPagingItemReader<Students> studentsReader() {
        JdbcPagingItemReader<Students> reader = new JdbcPagingItemReader<>();
        reader.setDataSource(dataSource);
        // 每次读取两条数据
        reader.setFetchSize(2);
        // 把读取到的记录转换为实体类对象
        reader.setRowMapper((rs, rowNum) -> {
            Students students = new Students();
            // 进行数据库字段和对象的属性映射
            students.setSid(rs.getString("Sid"));
            students.setName(rs.getString("name"));
            students.setSex(rs.getString("sex"));
            students.setAge(rs.getInt("age"));
            students.setClassZ(rs.getString("class"));
            students.setCard(rs.getString("card"));
            students.setCity(rs.getString("city"));

            return students;
        });

        // 设置查询的sql语句
        MySqlPagingQueryProvider provider = new MySqlPagingQueryProvider();
        provider.setSelectClause("Sid, name, sex, age, class, card, city");
        provider.setFromClause("from students");

        // 指定通过哪个字段进行排序
        HashMap<String, Order> sort = new HashMap<>(1);
        sort.put("Sid", Order.ASCENDING);
        provider.setSortKeys(sort);

        // 将查询语句赋值给reader
        reader.setQueryProvider(provider);

        return reader;
    }
}
```



Students：

```java
@Getter
@Setter
@ToString
@TableName("students")
public class Students implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("Sid")
    private String sid;

    @TableField("name")
    private String name;

    @TableField("sex")
    private String sex;

    @TableField("age")
    private Integer age;

    @TableField("class")
    private String classZ;

    @TableField("card")
    private String card;

    @TableField("city")
    private String city;


}
```



查询结果：

![image-20220906141216063](../../md-photo/image-20220906141216063.png)



### 从普通文件中读取数据

同样，使用ItemReader可以实现从文件中读取数据，并将文件中读取到的数据映射到对象中。

其中文件数据students.txt，放到resources路径下：

```txt
Sid,name,sex,age,class,card,city
001,王昭君,女,30,3班,634101199003157654,北京
002,诸葛亮,男,29,2班,110102199104262354,上海
003,鲁班大师,男,30,1班,820102199003047654,南京
004,白起,男,35,4班,840202198505177654,安徽
005,大乔,女,28,3班,215301199204067654,天津
006,孙尚香,女,25,1班,130502199506137654,河北
007,百里玄策,男,39,2班,140102198107277654,山西
008,小乔,女,25,3班,,河南
009,百里守约,男,31,1班,,湖南
010,妲己,女,24,2班,440701199607147654,广东
011,廉颇,男,30,1班,110202199005017754,北京
012,孙膑,男,36,3班,650102198401297655,新疆
```



完整代码：

```java
@Configuration
public class ItemReaderFileConfig {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;


    @Bean
    public Job itemReaderFileDemoJob() {
        return jobBuilderFactory.get("itemReaderFileDemoJob")
                .start(itemReaderFileDemoStep())
                .build();
    }

    @Bean
    public Step itemReaderFileDemoStep() {
        return stepBuilderFactory.get("itemReaderFileDemoStep")
                // 每一次读取的数据量
                .<Students, Students>chunk(2)
                .reader(studentsFileReader())
                .writer(items -> items.forEach(System.out::println))
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemReader<Students> studentsFileReader() {
        FlatFileItemReader<Students> reader = new FlatFileItemReader<>();
        // 设置读取数据的路径
        reader.setResource(new ClassPathResource("students.txt"));
        reader.setLinesToSkip(1);// 跳过第一行

        // 解析数据
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        // 设置表头
        tokenizer.setNames("Sid", "name", "sex", "age", "class", "card", "city");
        // 把解析出来的数据跟对象进行映射
        DefaultLineMapper<Students> lineMapper = new DefaultLineMapper<>();
        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(fieldSet -> {
            Students students = new Students();
            students.setSid(fieldSet.readString("Sid"));
            students.setName(fieldSet.readString("name"));
            students.setSex(fieldSet.readString("sex"));
            students.setAge(fieldSet.readInt("age"));
            students.setClassZ(fieldSet.readString("class"));
            students.setCard(fieldSet.readString("card"));
            students.setCity(fieldSet.readString("city"));

            return students;
        });

        lineMapper.afterPropertiesSet();
        // 将映射对象赋值给Reader对象
        reader.setLineMapper(lineMapper);

        return reader;
    }

}
```



读取结果：

![image-20220906190247590](../../md-photo/image-20220906190247590.png)



### 从XML文件中读取数据

同样，使用ItemReader可以实现从XML文件中读取数据，并将文件中读取到的数据映射到对象中。

其中文件数据students.xml，放到resources路径下：

```xml
<data>
    <row>
        <sid>001</sid>
        <name>王昭君</name>
        <sex>女</sex>
        <age>30</age>
        <classZ>3班</classZ>
        <card>634101199003157654</card>
        <city>北京</city>
    </row>
    <row>
        <sid>002</sid>
        <name>诸葛亮</name>
        <sex>男</sex>
        <age>29</age>
        <classZ>2班</classZ>
        <card>110102199104262354</card>
        <city>上海</city>
    </row>
    <row>
        <sid>003</sid>
        <name>鲁班大师</name>
        <sex>男</sex>
        <age>30</age>
        <classZ>1班</classZ>
        <card>820102199003047654</card>
        <city>南京</city>
    </row>
    <row>
        <sid>004</sid>
        <name>白起</name>
        <sex>男</sex>
        <age>35</age>
        <classZ>4班</classZ>
        <card>840202198505177654</card>
        <city>安徽</city>
    </row>
    <row>
        <sid>005</sid>
        <name>大乔</name>
        <sex>女</sex>
        <age>28</age>
        <classZ>3班</classZ>
        <card>215301199204067654</card>
        <city>天津</city>
    </row>
    <row>
        <sid>006</sid>
        <name>孙尚香</name>
        <sex>女</sex>
        <age>25</age>
        <classZ>1班</classZ>
        <card>130502199506137654</card>
        <city>河北</city>
    </row>
    <row>
        <sid>007</sid>
        <name>百里玄策</name>
        <sex>男</sex>
        <age>39</age>
        <classZ>2班</classZ>
        <card>140102198107277654</card>
        <city>山西</city>
    </row>
    <row>
        <sid>008</sid>
        <name>小乔</name>
        <sex>女</sex>
        <age>25</age>
        <classZ>3班</classZ>
        <card>(NULL)</card>
        <city>河南</city>
    </row>
    <row>
        <sid>009</sid>
        <name>百里守约</name>
        <sex>男</sex>
        <age>31</age>
        <classZ>1班</classZ>
        <card/>
        <city>湖南</city>
    </row>
    <row>
        <sid>010</sid>
        <name>妲己</name>
        <sex>女</sex>
        <age>24</age>
        <classZ>2班</classZ>
        <card>440701199607147654</card>
        <city>广东</city>
    </row>
    <row>
        <sid>011</sid>
        <name>廉颇</name>
        <sex>男</sex>
        <age>30</age>
        <classZ>1班</classZ>
        <card>110202199005017754</card>
        <city>北京</city>
    </row>
    <row>
        <sid>012</sid>
        <name>孙膑</name>
        <sex>男</sex>
        <age>36</age>
        <classZ>3班</classZ>
        <card>650102198401297655</card>
        <city>新疆</city>
    </row>
</data>
```



完整代码：

```java
@Configuration
public class ItemReaderXMLConfig {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;


    @Bean
    public Job itemReaderXMLDemoJob() {
        return jobBuilderFactory.get("itemReaderXMLDemoJob")
                .start(itemReaderXMLDemoStep())
                .build();
    }

    @Bean
    public Step itemReaderXMLDemoStep() {
        return stepBuilderFactory.get("itemReaderXMLDemoStep")
                // 每一次读取的数据量
                .<Students, Students>chunk(2)
                .reader(studentsXMLReader())
                .writer(items -> items.forEach(System.out::println))
                .build();
    }

    @Bean
    @StepScope
    public StaxEventItemReader<Students> studentsXMLReader() {
        StaxEventItemReader<Students> reader = new StaxEventItemReader<>();
        reader.setResource(new ClassPathResource("students.xml"));
        // 指定需要处理的根标签，这里指的是每行数据的row标签
        reader.setFragmentRootElementName("row");
        // 将XML映射为对象，需要引入spring-oxm和xstream依赖
        XStreamMarshaller unMarshaller = new XStreamMarshaller();
        HashMap<String, Class<?>> map = new HashMap<>();
        // 设置XML需要映射的对象
        map.put("row", Students.class);
        unMarshaller.setAliases(map);

        // 设置XML的映射关系
        reader.setUnmarshaller(unMarshaller);

        return reader;
    }
}
```



读取结果：

![image-20220906193953140](../../md-photo/image-20220906193953140.png)



### 从多个文件中读取数据

将前面的students.txt文件拆分为3个文件，分别为：students_1.txt、students_2.txt、students_3.txt，进行读取测试。

完整代码：

```java
@Configuration
public class ItemReaderMultiFileConfig {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Value("classpath:/students_*.txt")
    private Resource[] fileResources;


    @Bean
    public Job itemReaderMultiFileDemoJob() {
        return jobBuilderFactory.get("itemReaderMultiFileDemoJob")
                .start(itemReaderMultiFileDemoStep())
                .build();
    }

    @Bean
    public Step itemReaderMultiFileDemoStep() {
        return stepBuilderFactory.get("itemReaderMultiFileDemoStep")
                // 每一次读取的数据量
                .<Students, Students>chunk(2)
                .reader(studentsMultiFileReader())
                .writer(items -> items.forEach(System.out::println))
                .build();
    }

    @Bean
    @StepScope
    public MultiResourceItemReader<Students> studentsMultiFileReader() {
        MultiResourceItemReader<Students> reader = new MultiResourceItemReader<>();
        // 设置单个文件的读取方式
        reader.setDelegate(studentsSubFileReader());
        // 设置读取的文件位置
        reader.setResources(fileResources);

        return reader;
    }


    @Bean
    @StepScope
    public FlatFileItemReader<Students> studentsSubFileReader() {
        // 创建读取单个文件的对象
        FlatFileItemReader<Students> reader = new FlatFileItemReader<>();
        reader.setLinesToSkip(1);// 跳过第一行

        // 解析数据
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        // 设置表头
        tokenizer.setNames("Sid", "name", "sex", "age", "class", "card", "city");
        // 把解析出来的数据跟对象进行映射
        DefaultLineMapper<Students> lineMapper = new DefaultLineMapper<>();
        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(fieldSet -> {
            Students students = new Students();
            students.setSid(fieldSet.readString("Sid"));
            students.setName(fieldSet.readString("name"));
            students.setSex(fieldSet.readString("sex"));
            students.setAge(fieldSet.readInt("age"));
            students.setClassZ(fieldSet.readString("class"));
            students.setCard(fieldSet.readString("card"));
            students.setCity(fieldSet.readString("city"));

            return students;
        });

        lineMapper.afterPropertiesSet();
        // 将映射对象赋值给Reader对象
        reader.setLineMapper(lineMapper);

        return reader;
    }
}
```



读取结果：

![image-20220906195438542](../../md-photo/image-20220906195438542.png)



### 异常处理以及重启

复制students.txt为restart.txt，用于测试程序重启时，跳过已经成功执行的任务。

完整的代码：

Job任务配置：

```java
@Configuration
public class ItemRestartReaderConfig {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private RestartReader restartReader;

    @Bean
    public Job itemRestartReaderDemoJob() {
        return jobBuilderFactory.get("itemRestartReaderDemoJob")
                .start(itemRestartReaderDemoStep())
                .build();
    }

    @Bean
    public Step itemRestartReaderDemoStep() {
        return stepBuilderFactory.get("itemRestartReaderDemoStep")
                // 每一次读取的数据量
                .<Students, Students>chunk(2)
                .reader(restartReader)
                .writer(items -> items.forEach(System.out::println))
                .build();
    }
}
```



ItemStreamReader接口：

```java
@Component
@StepScope
public class RestartReader implements ItemStreamReader<Students> {
    private FlatFileItemReader<Students> reader = new FlatFileItemReader<>();
    private Long curLine = 0L;
    private boolean restart = false;
    private ExecutionContext executionContext;

    public RestartReader() {
        reader.setResource(new ClassPathResource("restart.txt"));
        reader.setLinesToSkip(1);// 跳到第一行

        // 解析数据
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        // 设置表头
        tokenizer.setNames("Sid", "name", "sex", "age", "class", "card", "city");
        // 把解析出来的数据跟对象进行映射
        DefaultLineMapper<Students> lineMapper = new DefaultLineMapper<>();
        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(fieldSet -> {
            Students students = new Students();
            students.setSid(fieldSet.readString("Sid"));
            students.setName(fieldSet.readString("name"));
            students.setSex(fieldSet.readString("sex"));
            students.setAge(fieldSet.readInt("age"));
            students.setClassZ(fieldSet.readString("class"));
            students.setCard(fieldSet.readString("card"));
            students.setCity(fieldSet.readString("city"));

            return students;
        });

        lineMapper.afterPropertiesSet();
        // 将映射对象赋值给Reader对象
        reader.setLineMapper(lineMapper);
    }

    @Override
    public Students read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        Students students;

        this.curLine++;

        // 如果是重启的任务，跳过上次已经成功执行的数据
        if (restart) {
            reader.setLinesToSkip(this.curLine.intValue());
            restart = false;
            System.out.println("Start reading from line: " + this.curLine);
        }

        reader.open(this.executionContext);
        students = reader.read();

        // 故意模拟的异常
        if (students != null && students.getSid().equals("005")) {
            throw new RuntimeException("Something wrong. Student Sid is:" + students.getSid());
        }

        return students;
    }

    // 在未执行读取操作前，获取任务上下文，判断当前任务是否为重启的任务
    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        this.executionContext = executionContext;
        // 上下文已经存在对应的信息，是重启的任务
        if (executionContext.containsKey("curLine")) {
            this.curLine = executionContext.getLong("curLine");
            this.restart = true;
            // 新的任务，赋予程序的上下文
        } else {
            this.curLine = 0L;
            executionContext.put("curLine", this.curLine);
            System.out.println("Start reading from line: " + (this.curLine + 1));
        }
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        // 将程序的上下文更新到数据库中
        executionContext.put("curLine", this.curLine);
        System.out.println("currentLine:" + this.curLine);
    }

    @Override
    public void close() throws ItemStreamException {

    }
}
```



测试结果：

第一次读取了四条数据，第五条时发生错误：

![image-20220906203842035](../../md-photo/image-20220906203842035.png)



去除模拟的错误之后，程序从第五条开始读取：

![image-20220906204415787](../../md-photo/image-20220906204415787.png)



整体结构示意图：

![image-20220906213502026](../../md-photo/image-20220906213502026.png)
