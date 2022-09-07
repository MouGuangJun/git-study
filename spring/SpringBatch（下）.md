# SpringBatch

## ItemWriter

ItemReader是一个数据一个数据的读，而ItemWirter是一批一批的输出。

### 初识ItemWriter

Job任务配置：

```java
@Configuration
public class ItemWriterConfig {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private MyWriter myWriter;

    @Bean
    public Job itemWriterDemoJob() {
        return jobBuilderFactory.get("itemWriterDemoJob")
                .start(itemWriterDemoStep())
                .build();
    }

    @Bean
    public Step itemWriterDemoStep() {
        return stepBuilderFactory.get("itemWriterDemoStep")
                // 每一次读取的数据量
                .<String, String>chunk(2)
                .reader(new ListItemReader<>(Arrays.asList("cat", "dog", "pig", "duck")))
                .writer(myWriter)
                .build();
    }

}
```



MyWriter：

```java
@Component
public class MyWriter implements ItemWriter<String> {
    @Override
    public void write(List<? extends String> items) throws Exception {
        items.forEach(System.out::println);
    }
}
```



运行结果：

![image-20220906220952851](../../md-photo/image-20220906220952851.png)



### 输出数据到数据库

完整代码：

```java
@Configuration
public class ItemWriterDBConfig {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private HikariDataSource dataSource;

    @Bean
    public Job itemWriterDBDemoJob() {
        return jobBuilderFactory.get("itemWriterDBDemoJob")
                .start(itemWriterDBDemoStep())
                .build();
    }

    @Bean
    public Step itemWriterDBDemoStep() {
        return stepBuilderFactory.get("itemWriterDBDemoStep")
                // 每一次读取的数据量
                .<User, User>chunk(2)
                .reader(userFileReader())
                .writer(userDBWriter())
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemReader<User> userFileReader() {
        FlatFileItemReader<User> reader = new FlatFileItemReader<>();
        // 设置读取数据的路径
        reader.setResource(new ClassPathResource("writer/user.txt"));
        reader.setLinesToSkip(1);// 跳过第一行

        // 解析数据
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        // 设置表头
        tokenizer.setNames("id", "name", "sex", "age");
        // 把解析出来的数据跟对象进行映射
        DefaultLineMapper<User> lineMapper = new DefaultLineMapper<>();
        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(fieldSet -> {
            User user = new User();
            user.setId(fieldSet.readInt("id"));
            user.setName(fieldSet.readString("name"));
            user.setSex(fieldSet.readString("sex"));
            user.setAge(fieldSet.readInt("age"));

            return user;
        });

        lineMapper.afterPropertiesSet();
        // 将映射对象赋值给Reader对象
        reader.setLineMapper(lineMapper);

        return reader;
    }

    @Bean
    @StepScope
    public JdbcBatchItemWriter<User> userDBWriter() {
        JdbcBatchItemWriter<User> writer = new JdbcBatchItemWriter<>();
        // 设置数据源
        writer.setDataSource(dataSource);
        // 设置sql语句
        writer.setSql("insert into user_back (id, name, sex, age) values(:id, :name, :sex, :age)");
        // 将java属性与JDBC占位符进行映射
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());

        return writer;
    }
}
```



User实体类：

```java
@Getter
@Setter
@ToString
@TableName("user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("name")
    private String name;

    @TableField("sex")
    private String sex;

    @TableField("age")
    private Integer age;
}
```



数据文件：

```txt
id,name,sex,age
1,张三,男,18
2,李四,1,19
3,拉拉,2,17
4,王五,MALE,18
5,赵六,MALE,22
6,李白,MALE,20
7,赵信  ,1,21
```



运行结果：

![image-20220906233122888](../../md-photo/image-20220906233122888.png)



成功插入数据库数据：

![image-20220906233155686](../../md-photo/image-20220906233155686.png)





### 输出数据到普通文件

从数据库中读取数据并写入到普通的文件中。

源代码：

```java
@Configuration
public class ItemWriterFileConfig {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private HikariDataSource dataSource;

    @Bean
    public Job itemWriterFileDemoJob() {
        return jobBuilderFactory.get("itemWriterFileDemoJob")
                .start(itemWriterFileDemoStep())
                .build();
    }

    @Bean
    public Step itemWriterFileDemoStep() {
        return stepBuilderFactory.get("itemWriterFileDemoStep")
                // 每一次读取的数据量
                .<User, User>chunk(2)
                .reader(userDBReader())
                .writer(userFileWriter())
                .build();
    }

    @Bean
    @StepScope
    public JdbcPagingItemReader<User> userDBReader() {
        JdbcPagingItemReader<User> reader = new JdbcPagingItemReader<>();
        reader.setDataSource(dataSource);
        // 每次从ResultSet中抓取两条数据
        reader.setFetchSize(2);
        // 把读取到的记录转换为实体类对象
        reader.setRowMapper((rs, rowNum) -> {
            User user = new User();
            // 进行数据库字段和对象的属性映射
            user.setId(rs.getInt("id"));
            user.setName(rs.getString("name"));
            user.setSex(rs.getString("sex"));
            user.setAge(rs.getInt("age"));

            return user;
        });

        // 设置查询的sql语句
        MySqlPagingQueryProvider provider = new MySqlPagingQueryProvider();
        provider.setSelectClause("id, name, sex, age");
        provider.setFromClause("from user_back");

        // 指定通过哪个字段进行排序
        HashMap<String, Order> sort = new HashMap<>(1);
        sort.put("id", Order.ASCENDING);
        provider.setSortKeys(sort);

        // 将查询语句赋值给reader
        reader.setQueryProvider(provider);

        return reader;
    }

    @SneakyThrows
    @Bean
    @StepScope
    public FlatFileItemWriter<User> userFileWriter() {
        FlatFileItemWriter<User> writer = new FlatFileItemWriter<>();
        // 把user对象转换为字符串，并输出到文件
        // 这里不输出到classpath下，直接输出到文件系统
        writer.setResource(new FileSystemResource("D:/user.txt"));
        writer.setLineAggregator(new LineAggregator<User>() {
            @SneakyThrows
            @Override
            public String aggregate(User item) {
                // 将对象转化为Json字符串
                ObjectMapper mapper = new ObjectMapper();
                return mapper.writeValueAsString(item);
            }
        });

        writer.afterPropertiesSet();

        return writer;
    }
}
```



运行结果：

![image-20220907091023704](../../md-photo/image-20220907091023704.png)

数据成功转为Json文件进行存储；

![image-20220907091055829](../../md-photo/image-20220907091055829.png)





### 输出数据到XML文件

还是将数据库的数据输出到XML文件中，这里的数据库读取Reader对象复用上一节的对象。

源代码：

```java
@Configuration
public class ItemWriterXMLConfig {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Resource(name = "userDBReader")
    private ItemReader<? extends User> userDBReader;

    @Bean
    public Job itemWriterXMLDemoJob() {
        return jobBuilderFactory.get("itemWriterXMLDemoJob")
                .start(itemWriterXMLDemoStep())
                .build();
    }

    @Bean
    public Step itemWriterXMLDemoStep() {
        return stepBuilderFactory.get("itemWriterXMLDemoStep")
                // 每一次读取的数据量
                .<User, User>chunk(2)
                .reader(userDBReader)
                .writer(userXMLWriter())
                .build();
    }

    @Bean
    @StepScope
    public StaxEventItemWriter<User> userXMLWriter() {
        StaxEventItemWriter<User> writer = new StaxEventItemWriter<>();
        // 指定输出的文件
        writer.setResource(new FileSystemResource("D:/user.xml"));
        // 指定根标签
        writer.setRootTagName("data");
        // 使用marshaller进行xml转换操作
        XStreamMarshaller marshaller = new XStreamMarshaller();
        HashMap<String, Class<?>> map = new HashMap<>();
        map.put("user", User.class);
        marshaller.setAliases(map);

        // 设置marshaller
        writer.setMarshaller(marshaller);

        return writer;
    }
}
```



执行结果：

![image-20220907092421821](../../md-photo/image-20220907092421821.png)



数据成功生成：

![image-20220907092454059](../../md-photo/image-20220907092454059.png)



### 数据输出到多个文件

还是将数据库的数据输出到多个文件中，这里的数据库读取Reader对象复用上一节的对象，写入到普通文件和xml的Writer对象复用前两节的对象。

#### 不进行分类

源代码：

```java
@Configuration
public class ItemWriterMultiFileConfig {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Resource(name = "userDBReader")
    private ItemReader<? extends User> userDBReader;

    @Resource(name = "userFileWriter")
    private ItemWriter<User> userFileWriter;

    @Resource(name = "userXMLWriter")
    private ItemWriter<User> userXMLWriter;


    @Bean
    public Job itemWriterMultiFileDemoJob() {
        return jobBuilderFactory.get("itemWriterMultiFileDemoJob")
                .start(itemWriterMultiFileDemoStep())
                .build();
    }

    @Bean
    public Step itemWriterMultiFileDemoStep() {
        return stepBuilderFactory.get("itemWriterMultiFileDemoStep")
                // 每一次读取的数据量
                .<User, User>chunk(2)
                .reader(userDBReader)
                .writer(userMultiFileWriter())
                .build();
    }

    @SneakyThrows
    @Bean
    public CompositeItemWriter<User> userMultiFileWriter() {
        CompositeItemWriter<User> writer = new CompositeItemWriter<>();
        // 指定真正输出文件的writer
        writer.setDelegates(Arrays.asList(userFileWriter, userXMLWriter));
        writer.afterPropertiesSet();

        return writer;
    }
}
```



执行结果：

![image-20220907093904322](../../md-photo/image-20220907093904322.png)

同时写了xml和普通文件：

![image-20220907093933394](../../md-photo/image-20220907093933394.png)





每一个文件都有完整的数据：

![image-20220907094057285](../../md-photo/image-20220907094057285.png)

![image-20220907094106122](../../md-photo/image-20220907094106122.png)





#### 进行分类

源代码：

**<font color='red'>注意：使用分类的方式输出时，需要手动指定Stream，否则会发生报错情况！</font>**

```java
@Configuration
public class ItemWriterMultiFileConfig {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Resource(name = "userDBReader")
    private ItemReader<? extends User> userDBReader;

    @Resource(name = "userFileWriter")
    private FlatFileItemWriter<User> userFileWriter;

    @Resource(name = "userXMLWriter")
    private StaxEventItemWriter<User> userXMLWriter;


    @Bean
    public Job itemWriterMultiFileDemoJob() {
        return jobBuilderFactory.get("itemWriterMultiFileDemoJob")
                .start(itemWriterMultiFileDemoStep())
                .build();
    }

    @Bean
    public Step itemWriterMultiFileDemoStep() {
        return stepBuilderFactory.get("itemWriterMultiFileDemoStep")
                // 每一次读取的数据量
                .<User, User>chunk(2)
                .reader(userDBReader)
                .writer(userMultiFileClassifierWriter())
                // 分类写出的Writer没有实现流接口，需要手动指定stream流，否则会报错：
                // Writer must be open before it can be written to
                .stream(userFileWriter)
                .stream(userXMLWriter)
                .build();
    }

    /**
     * 进行分类，将id 1 - 4输出到普通文件，其他输出到xml
     */
    @Bean
    public ClassifierCompositeItemWriter<User> userMultiFileClassifierWriter() {
        ClassifierCompositeItemWriter<User> writer = new ClassifierCompositeItemWriter<>();
        writer.setClassifier((Classifier<User, ItemWriter<? super User>>) user -> {
            if (user.getId() < 5) {
                return userFileWriter;
            }

            return userXMLWriter;
        });

        return writer;
    }
}
```



执行结果：

![image-20220907095507337](../../md-photo/image-20220907095507337.png)



数据成功的进行了分类：

![image-20220907095539388](../../md-photo/image-20220907095539388.png)



![image-20220907095617098](../../md-photo/image-20220907095617098.png)

## ItemProcessor

ItemProcessor<I,O>用于处理业务逻辑，验证，过滤等功能。

从数据库中读取数据，然后对数据进行处理，最后输出到普通文件。读取和输出复用上一章的Reader和Writer。

源代码：

```java
@Configuration
public class ItemProcessorConfig {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Resource(name = "userDBReader")
    private ItemReader<? extends User> userDBReader;

    @Resource(name = "userFileWriter")
    private ItemWriter<User> userFileWriter;

    @Autowired
    private SexConvertProcessor sexConvertProcessor;

    @Autowired
    private IdFilterProcessor idFilterProcessor;

    @Bean
    public Job itemProcessorDemoJob() {
        return jobBuilderFactory.get("itemProcessorDemoJob")
                .start(itemProcessorDemoStep())
                .build();
    }

    @Bean
    public Step itemProcessorDemoStep() {
        return stepBuilderFactory.get("itemProcessorDemoStep")
                // 每一次读取的数据量
                .<User, User>chunk(2)
                .reader(userDBReader)
                // 单个处理过程
                // .processor(sexConvertProcessor)
                // 多个处理过程
                .processor(process())
                .writer(userFileWriter)
                .build();
    }


    // 有多个数据处理的方式
    @Bean
    public CompositeItemProcessor<User, User> process() {
        CompositeItemProcessor<User, User> processor = new CompositeItemProcessor<>();
        // 将多个处理过程都给CompositeItemProcessor
        processor.setDelegates(Arrays.asList(sexConvertProcessor, idFilterProcessor));

        return processor;
    }
}
```



执行结果：

![image-20220907101807562](../../md-photo/image-20220907101807562.png)

![image-20220907101717175](../../md-photo/image-20220907101717175.png)





## 错误处理

默认情况下当任务出现异常时，SpringBatch会结束任务，当使用相同的参数时，SpringBatch会

去执行未执行的剩余任务。



### 错误概述

源代码：

```java
@Configuration
public class ErrorJobConfig {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job errorsDemoJob() {
        return jobBuilderFactory.get("errorsDemoJob")
                .start(errorStep1())
                .next(errorStep2())
                .build();
    }

    @Bean
    public Step errorStep1() {
        return stepBuilderFactory.get("errorStep1")
                .tasklet(errorHandling())
                .build();
    }

    @Bean
    public Step errorStep2() {
        return stepBuilderFactory.get("errorStep2")
                .tasklet(errorHandling())
                .build();
    }

    @Bean
    @StepScope
    public Tasklet errorHandling() {
        return (contribution, chunkContext) -> {
            // 获取任务运行的上下文，每一个Step的执行上下文都不同
            Map<String, Object> context = chunkContext.getStepContext().getStepExecutionContext();
            String stepName = chunkContext.getStepContext().getStepName();
            // 第一次运行的时候没有该key，执行错误。第二次有该key，成功执行。
            if (context.containsKey("qianfen")) {
                System.out.println(stepName + "：the second run will success");
                return RepeatStatus.FINISHED;
            } else {
                System.out.println(stepName + "：the first run will fail");
                // 将参数放到Step执行上下文中
                chunkContext.getStepContext().getStepExecution().getExecutionContext().put("qianfen", true);
                // 手动模拟的异常
                throw new RuntimeException("error......");
            }

        };
    }
}
```



执行结果：

第一次Step1执行错误：

![image-20220907103716899](../../md-photo/image-20220907103716899.png)



第二次Step1成功，step2第一次执行失败：

![image-20220907104132960](../../md-photo/image-20220907104132960.png)



第三次任务成功执行完成：

![image-20220907104220586](../../md-photo/image-20220907104220586.png)



### 错误重试

有些错误我们能够预料到，可以让Spring Batch进行相应的重试。

源代码：

```java
@Configuration
public class ErrorRetryConfig {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private ErrorRetryProcessor errorRetryProcessor;

    @Bean
    public Job errorRetryJob() {
        return jobBuilderFactory.get("errorRetryJob")
                .start(errorRetryStep())
                .build();
    }

    @Bean
    public Step errorRetryStep() {
        return stepBuilderFactory.get("errorRetryStep")
                .<String, String>chunk(10)
                .reader(errorRetryReader())
                .processor(errorRetryProcessor)
                .writer(items -> items.forEach(System.out::println))
                .faultTolerant()// 容错
                // 发生运行时异常时，进行重试
                .retry(RuntimeException.class)
                // 指定重试的次数（reader、processor、writer里面出现的总异常次数不能超过五次）
                .retryLimit(5)
                .build();
    }

    @Bean
    @StepScope
    public ItemReader<? extends String> errorRetryReader() {
        ArrayList<String> items = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            items.add(String.valueOf(i));
        }

        return new ListItemReader<>(items);
    }
}
```



ErrorRetryProcessor：

```java
@Component
public class ErrorRetryProcessor implements ItemProcessor<String, String> {
    private int attemptCount = 0;

    @Override
    public String process(String item) throws Exception {
        System.out.println("processing item ：" + item);
        // 当值为26的时候，故意抛出异常
        if (item.equals("26")) {
            attemptCount++;
            // 重试三次，任务执行成功
            if (attemptCount >= 3) {
                System.out.println("Retried " + attemptCount + " times success.");
                return String.valueOf(Integer.valueOf(item) * -1);
            } else {
                System.out.println("Processed the：" + attemptCount + " times fail.");
                throw new RuntimeException("Processed failed. AttemptCount：" + attemptCount);
            }
        } else {
            return String.valueOf(Integer.valueOf(item) * -1);
        }
    }
}
```



运行结果：

![image-20220907110318583](../../md-photo/image-20220907110318583.png)



### 错误跳过

同样的，我们可以把错误的部分给跳过：

源代码：

```java
@Configuration
public class ErrorSkipConfig {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private ErrorSkipProcessor errorSkipProcessor;

    @Bean
    public Job errorSkipJob() {
        return jobBuilderFactory.get("errorSkipJob")
                .start(errorSkipStep())
                .build();
    }

    @Bean
    public Step errorSkipStep() {
        return stepBuilderFactory.get("errorSkipStep")
                .<String, String>chunk(10)
                .reader(errorSkipReader())
                .processor(errorSkipProcessor)
                .writer(items -> items.forEach(System.out::println))
                // 容错
                .faultTolerant()
                .skip(RuntimeException.class)
                // 指定跳过的次数（reader、processor、writer里面出现的总异常次数不能超过五次）
                .skipLimit(5)
                .build();
    }

    @Bean
    @StepScope
    public ItemReader<? extends String> errorSkipReader() {
        ArrayList<String> items = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            items.add(String.valueOf(i));
        }

        return new ListItemReader<>(items);
    }
}
```



ErrorSkipProcessor：

```java
@Component
public class ErrorSkipProcessor implements ItemProcessor<String, String> {

    @Override
    public String process(String item) throws Exception {
        System.out.println("processing item ：" + item);
        // 当值为26的时候，故意抛出异常
        if (item.equals("26")) {
            throw new RuntimeException("Processed failed. ");
        } else {
            return String.valueOf(Integer.valueOf(item) * -1);
        }
    }
}
```



执行结果：

![image-20220907111143115](../../md-photo/image-20220907111143115.png)



### 错误跳过监听器

上面一节把错误跳过了，如果我们需要记录被跳过的数据，可以使用监听器进行监听。

复用上一节的代码，添加一个监听器即可：

![image-20220907111743654](../../md-photo/image-20220907111743654.png)



MySkipListener：

```java
@Component
public class MySkipListener implements SkipListener<String, String> {
    @Override
    public void onSkipInRead(Throwable t) {

    }

    @Override
    public void onSkipInWrite(String item, Throwable t) {
        
    }

    @Override
    public void onSkipInProcess(String item, Throwable t) {
        // 记录异常的信息
        System.out.println(item + " occur exception " + t);
    }
}
```



执行结果：

![image-20220907112229665](../../md-photo/image-20220907112229665.png)





## JobLauncher实现作业调度

JobLauncher用于控制任务什么时候启动，之前的都是程序启动是自动启动。

关闭程序启动时自动执行批量任务配置：

```yml
spring:
  batch:
    # 配置用于启动时是否创建JobLauncherCommandLineRunner, true则会创建JobLauncherCommandLineRunner实例并且执行，否则不会创建实例
    job:
      enabled: false
```



源代码：
controller：

```java
@RestController
public class JobLauncherController {
    @Autowired
    private JobLauncher jobLauncher;
    @Resource(name = "jobLauncherDemoJob")
    private Job jobLauncherDemoJob;

    @SneakyThrows
    @PostMapping("/job")
    public String jobRun(@RequestBody String msg) {
        // 把接收到的参数值传给任务
        JobParameters jobParameters = new JobParametersBuilder().addString("msg", msg).toJobParameters();

        // 启动任务，并把参数传给任务
        jobLauncher.run(jobLauncherDemoJob, jobParameters);

        return "job execute success";
    }
}
```



Job配置：

```java
@Configuration
public class JobLauncherConfig {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job jobLauncherDemoJob() {
        return jobBuilderFactory.get("jobLauncherDemoJob")
                .start(jobLauncherDemoStep())
                .build();
    }

    @Bean
    public Step jobLauncherDemoStep() {
        return stepBuilderFactory.get("jobLauncherDemoStep")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("jobLauncherDemoStep 执行了......");
                    // 输出调度时，前端传入的参数
                    System.out.println("job的参数：" + chunkContext.getStepContext().getJobParameters());
                    return RepeatStatus.FINISHED;
                }).build();
    }
}
```



测试结果：

前端访问controller：

![image-20220907113949767](../../md-photo/image-20220907113949767.png)



后端Step，成功获取到参数：

![image-20220907114034306](../../md-photo/image-20220907114034306.png)



## JobOperator的使用

可以使用JobOperator，使用字符串的方式执行Job和参数来执行，不过需要自己配置JobOperator。

源代码：
JobOperatorConfig：

```java
@Configuration
public class JobOperatorConfig {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private JobExplorer jobExplorer;

    @Autowired
    private JobRegistry jobRegistry;

    @Autowired
    private ApplicationContext context;

    @Bean
    public Job jobOperatorDemoJob() {
        return jobBuilderFactory.get("jobOperatorDemoJob")
                .start(jobOperatorDemoStep())
                .build();
    }

    @Bean
    public Step jobOperatorDemoStep() {
        return stepBuilderFactory.get("jobOperatorDemoStep")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("jobOperatorDemoStep 执行了......");
                    // 输出调度时，前端传入的参数
                    System.out.println("job的参数：" + chunkContext.getStepContext().getJobParameters());
                    return RepeatStatus.FINISHED;
                }).build();
    }


    /**
     * 配置JobOperater对象
     */
    @Bean
    public JobOperator jobOperator() {
        SimpleJobOperator operator = new SimpleJobOperator();
        operator.setJobLauncher(jobLauncher);
        // 设置默认的参数转换形式
        operator.setJobParametersConverter(new DefaultJobParametersConverter());
        // 设置任务仓库
        operator.setJobRepository(jobRepository);
        // 获取和任务相关的信息
        operator.setJobExplorer(jobExplorer);
        // 注册需要执行的任务对象
        operator.setJobRegistry(jobRegistry);

        return operator;
    }


    /**
     * 注册任务对象
     */
    @Bean
    public JobRegistryBeanPostProcessor jobRegistrar() throws Exception {
        JobRegistryBeanPostProcessor processor = new JobRegistryBeanPostProcessor();
        processor.setJobRegistry(jobRegistry);
        processor.setBeanFactory(context.getAutowireCapableBeanFactory());
        processor.afterPropertiesSet();

        return processor;
    }
}
```



JobOperatorController：

```java
@RestController
public class JobOperatorController {
    @Autowired
    private JobOperator jobOperator;


    @SneakyThrows
    @PostMapping("/jobOperator")
    public String jobRun(@RequestBody String msg) {
        // 把接收到的参数值传给任务
        JobParameters jobParameters = new JobParametersBuilder().addString("msg", msg).toJobParameters();

        // 启动任务，并把参数传给任务
        jobOperator.start("jobOperatorDemoJob", "msg=" + msg);

        return "job execute success";
    }
}
```



前端请求参数：

![image-20220907141914919](../../md-photo/image-20220907141914919.png)



执行结果：

![image-20220907141827659](../../md-photo/image-20220907141827659.png)
