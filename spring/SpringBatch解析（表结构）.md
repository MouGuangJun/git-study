# SpringBatch解析

## SpringBatch核心概念介绍

**<font color='red'>带* 的属于核心概念，否则属于高级扩展的功能概念</font>**

### *Job

​	Job是封装整个批处理过程的实体。与其他Spring项目一样，Job与XML配置文件或基于Java的配置连接在一起，该配置可以被称为“Job configuration”。但是， Job这只是整个层次结构的顶部，如下图所示：

![Job Hierarchy](../../md-photo/c3a914a1e7eccb5ff18d25236f4eb2bd.png)



​	在Spring Batch中，Job只是Step实例的容器。它组合了逻辑上属于流程的多个Step，并允许配置所有Step全局的属性，例如可重新启动性。

​	Job只是SpringBatch提供的一个Job接口，另外，SpringBatch还提供了一个Job的简单实现类SimpleJob，在此类上创建了一些标准功能。SimpleJob继承于AbstractJob，而AbstractJob是Job的简单实现类。使用基于Java的配置时，可使用一组构建器来实例化一个Job，如以下示例所示：

```java
@Bean
public Job footballJob() {
    return this.jobBuilderFactory.get("footballJob")
                     .start(playerLoad())
                     .next(gameLoad())
                     .next(playerSummarization())
                     .end()
                     .build();
}
```



### *JobInstance

​	JobInstance是指逻辑作业运行的概念，相当于Job的实例。假设一个批处理任务，每天需要执行一遍，每天的日期作为参数（即后面说到的JobParameters），那么，昨天和今天的批处理任务虽然是同一个Job，但是因为JobParameters不同，分属于两个JobInstance，而我们必须记录job的每次运行的情况。其中SpringBatch会通过表BATCH_JOB_INSTANCE记录了JobInstance的详细情况。

<center>BATCH_JOB_INSTANCE</center>
| 属性            | 定义说明                                                     |
| --------------- | ------------------------------------------------------------ |
| JOB_INSTANCE_ID | 主键                                                         |
| VERSION         | 版本号                                                       |
| JOB_NAME        | 从Job对象获取的作业的名称。                                  |
| JOB_KEY         | 它的序列化JobParameters唯一地识别相同作业的不同实例。（JobInstances具有相同的作业名称必须具有JobParameters不同的JOB_KEY值，因此具有不同的值）。 |



### *JobParameters

​	上面已经大致描述了一下，相同Job下，每个JobInstance有可能不同，而引起不同的就是JobParameters。字面上理解，JobParameters就是Job的参数集合，其内部也仅仅是一个简单的Map集合。一个JobParameters对象拥有一组用于启动批处理作业的参数。它们可以在运行期间用于标识甚至用作参考数据，如下图所示：

![工作参数](../../md-photo/05c9800bb9dfdc49c4b72c74b8af6f2f.png)



​	因此，JobInstance = Job + JobParameters。这允许开发人员有效地控制如何定义JobInstance，因为它们控制传入的参数。<font color='blue'>但是，并非所有作业参数都需要用于标识作业实例。默认情况下，它们会这样做。不过，该框架还允许提交带有不构成JobInstance标识的参数的作业。</font>其中SpringBatch会通过表BATCH_JOB_EXECUTION_PARAMS记录了JobParameters的详细情况。虽然JobParameters和Job组成了JobInstance ，并用于JobInstance 的标识，但是SpringBatch在做持久化的时候，将其和下面会讲到的JobExecution进行关联。

<center>BATCH_JOB_EXECUTION_PARAMS</center>

| 属性             | 定义说明                                                     |
| ---------------- | ------------------------------------------------------------ |
| JOB_EXECUTION_ID | BATCH_JOB_EXECUTION表中的外键，指示参数条目所属的作业执行。  |
| TYPE_CD          | 存储值类型的字符串表示形式，可以是字符串，日期，长整数或双精度。由于该类型必须是已知的，因此它不能为空。 |
| KEY_NAME         | 参数键。                                                     |
| STRING_VAL       | 参数值，如果类型是字符串。                                   |
| DATE_VAL         | 参数值，如果类型是日期。                                     |
| LONG_VAL         | 参数值，如果类型很长。                                       |
| DOUBLE_VAL       | 参数值，如果类型是双倍的。                                   |
| IDENTIFYING      | 指示参数是否有助于相关身份的标志JobInstance。                |

​	

### *JobExecution

​	JobExecution是指一次尝试运行一个作业的技术概念。执行可能以失败或成功结束，但是，除非执行成功完成，否则对应于给定执行的JobInstance不会被认为是完成的。上面已经说了，SpringBatch具有重启批处理任务的功能。假设一个JobInstance执行失败，当第二次重启的时候，会重新创建一个JobExecution，和第一次执行的JobExecution不同，但是，仍然只有一个JobInstance。

​	Job定义了Job是什么以及如何执行Job，而JobInstance是一个纯粹的组织对象，用于将执行分组在一起，主要是为了支持正确的重启语义。然而，JobExecution是运行过程中实际发生的事情的主要存储机制，并包含许多必须控制和持久化的属性，这些属性对于Job的执行是非常长重要的，SpringBatch会通过JobRepository将这些属性持久化到数据库中，而SpringBatch也会创建表用于存储这些属性（BATCH_JOB_EXECUTION）

<center>BATCH_JOB_EXECUTION</center>

| 属性             | 定义说明                                                     |
| ---------------- | ------------------------------------------------------------ |
| JOB_EXECUTION_ID | 主键                                                         |
| VERSION          | 版本号                                                       |
| JOB_INSTANCE_ID  | BATCH_JOB_INSTANCE表中的外键。它表示此执行所属的实例。每个实例可能有多个执行。 |
| CREATE_TIME      | 当工作方案第一次被持续的当前系统的时间。作业可能还没有启动(因此没有启动时间)，但它总是有一个createTime，这是框架用于管理作业级别ExecutionContexts所必需的。 |
| START_TIME       | 代表当执行开始时的当前系统时间。如果作业尚未开始，则此字段为空。 |
| END_TIME         | 代表当执行完成后，无论它是否是成功的当前系统时间。如果作业尚未完成，则该字段为空。 |
| STATUS           | 指示执行的状态。在运行时，它是 `BatchStatus#STARTED`。如果失败，则为`BatchStatus#FAILED`。如果成功完成，那就是`BatchStatus#COMPLETED。`见BatchStatus常量类。 |
| EXIT_CODE        | 说明运行的结果。这是最重要的，因为它包含返回给调用方的退出代码。如果作业尚未完成，则该字段为空。见ExitStatus常量类。 |
| EXIT_MESSAGE     | 运行结果的信息。                                             |
| LAST_UPDATED     | 代表最后一次的罢工继续进行。如果作业尚未开始，则此字段为空。 |



### *Step

​	Step是一个域对象，封装了批处理作业的一个独立的顺序阶段。因此，每个Job完全由一个或多个Step组成。一个Step包含了所有的定义和控制实际的批量处理所需的信息。这是一个必要的模糊描述，因为任何给定Step的内容都是由编写Job的开发人员自行决定的。与Job一样，Step具有与JobExecution类似的StepExecution，如下图所示：

![带步骤的作业层次结构](../../md-photo/af3ddff081e3fabd3703fe3e6ecbd173.png)



### *StepExecution

​	StepExecution表示一次执行Step, 每次运行一个Step时都会创建一个新的StepExecution，类似于JobExecution。 但是，某个步骤可能由于其之前的步骤失败而无法执行。 且仅当Step实际启动时才会创建StepExecution。

​	Step执行的实例由StepExecution类的对象表示。 每个StepExecution都包含对其相应步骤的引用以及JobExecution和事务相关的数据，例如提交和回滚计数以及开始和结束时间。 此外，每个步骤执行都包含一个ExecutionContext，其中包含开发人员需要在批处理运行中保留的任何数据，例如重新启动所需的统计信息或状态信息。此外，SpringBatch也会将StepExecution持久化到数据库中，其对应的表是BATCH_STEP_EXECUTION。

<center>BATCH_STEP_EXECUTION</center>

| 属性               | 定义说明                                                     |
| ------------------ | ------------------------------------------------------------ |
| STEP_EXECUTION_ID  | 唯一标识此执行的主键。该列的值应该可以通过调用 对象的getId方法来获得StepExecution。 |
| VERSION            | 版本号。                                                     |
| STEP_NAME          | 此执行程序所属步骤的名称。                                   |
| JOB_EXECUTION_ID   | BATCH_JOB_EXECUTION表中的外键。它表明这StepExecution属于哪个JobExecution。 |
| START_TIME         | 代表执行开始时间的时间戳。                                   |
| END_TIME           | 表示执行完成时的时间戳，无论成功或失败。即使作业当前未运行，此列中的空值也表示存在某种类型的错误，并且框架无法在失败之前执行上次保存。 |
| STATUS             | 表示执行状态的字符串。在运行时，状态为`BatchStatus.STARTED`。如果失败，则状态为`BatchStatus.FAILED`。如果成功完成，则状态为`BatchStatus.COMPLETED`。见BatchStatus类。 |
| COMMIT_COUNT       | 此执行期间步骤已提交事务的次数。                             |
| READ_COUNT         | 执行过程中读取的项目数量。                                   |
| FILTER_COUNT       | 从此执行过滤出的项目数量。                                   |
| WRITE_COUNT        | 在执行期间写入和提交的项目数量。                             |
| READ_SKIP_COUNT    | 在执行过程中跳过的项目数量。                                 |
| WRITE_SKIP_COUNT   | 在写入时跳过的项目数量。                                     |
| PROCESS_SKIP_COUNT | 在执行过程中跳过的项目数量。                                 |
| ROLLBACK_COUNT     | 执行期间的回滚次数。请注意，此计数包括每次发生回滚时，包括重试回滚和跳过恢复过程中的回滚。 |
| EXIT_CODE          | 表示执行退出代码的字符串。对于命令行作业，可能会将其转换为数字。见ExitStatus类。 |
| EXIT_MESSAGE       | 表示作业如何退出的更详细描述的字符串。在失败的情况下，这可能包括尽可能多的堆栈跟踪。 |
| LAST_UPDATED       | 代表上次执行持续时间的时间戳。                               |



### *ExecutionContext

​	ExecutionContext表示一个由框架持久化和控制的键/值对集合，以便让开发人员有地方存储作用域为StepExecution对象或JobExecution对象的持久化状态。同样重要的是要注意，每个JobExecution或者StepExecution至少有一个ExecutionContext。例如，下面的代码片段:

```java
ExecutionContext ecStep = stepExecution.getExecutionContext();
ExecutionContext ecJob = jobExecution.getExecutionContext();
//ecStep does not equal ecJob
```

​	同样的，SpringBatch也会将ExecutionContext的内容持久化到数据库中，以方便后续重启的时候，直接从数据库中读取数据，并且让批处理任务从失败的地方继续执行。其对应的表为BATCH_STEP_EXECUTION_CONTEXT和BATCH_JOB_EXECUTION_CONTEXT。

<center>BATCH_STEP_EXECUTION_CONTEXT</center>

| 属性               | 定义说明                                                     |
| ------------------ | ------------------------------------------------------------ |
| STEP_EXECUTION_ID  | 表示StepExecution上下文所属的外键。可能和多个StepExecution关联。 |
| SHORT_CONTEXT      | 一个字符串版本的SERIALIZED_CONTEXT。                         |
| SERIALIZED_CONTEXT | 整个上下文序列化。                                           |



<center>BATCH_JOB_EXECUTION_CONTEXT</center>

| 属性               | 定义说明                                                     |
| ------------------ | ------------------------------------------------------------ |
| JOB_EXECUTION_ID   | 表示JobExecution上下文所属的外键。可能和多个JobExecution相关联。 |
| SHORT_CONTEXT      | 一个字符串版本的SERIALIZED_CONTEXT。                         |
| SERIALIZED_CONTEXT | 整个上下文序列化。                                           |



### *JobRepository

​	JobRepository是上述所有构造型的持久性机制。它提供了JobLauncher，Job以及Step的CRUD操作实现。当一个Job首次启动时，从存储库中获得一个JobExecution，并且在执行过程中，通过将它们传递到存储库来持久化StepExecution和JobExecution实现。

​	@EnableBatchProcessing注解可以为JobRepository提供自动配置。



### *JobLauncher

​	JobLauncher表示一个简单的接口，用于启动一组给定JobParameters的Job，为什么这里要强调指定了JobParameter，原因其实我们在前面已经提到了，jobparameter和job一起才能组成一次JobInstance。如下面的示例所示:

```java
public interface JobLauncher {

public JobExecution run(Job job, JobParameters jobParameters)
            throws JobExecutionAlreadyRunningException, JobRestartException,
                   JobInstanceAlreadyCompleteException, JobParametersInvalidException;
}
```



### *Item Reader

​	ItemReader是一个抽象，表示对步骤的输入检索，每次检索一个条目。当ItemReader耗尽了它可以提供的项时，它通过返回null来表示。



### *Item Writer

​	ItemWriter是一种抽象，它表示步骤、一次批处理或项块的输出。通常，ItemWriter不知道它接下来应该接收的输入，只知道在其当前调用中传递的项。



### *Item Processor

​	ItemProcessor是一种抽象，表示项目的业务处理。当ItemReader读取一个项目，ItemWriter写入这些项目时，ItemProcessor提供一个访问点来转换或应用其他业务处理。如果在处理该项时，确定该项无效，则返回null表示不应将该项写入。



### JobExplorer

​	JobExplorer是查询存储库中现有执行情况的高级组件，是JobRepository的只读版本。以下示例显示了如何JobExplorer在Java中配置：

```java
// This would reside in your BatchConfigurer implementation
@Override
public JobExplorer getJobExplorer() throws Exception {
	JobExplorerFactoryBean factoryBean = new JobExplorerFactoryBean();
	factoryBean.setDataSource(this.dataSource);
	return factoryBean.getObject();
}
```



### JobRegistry

​	JobRegistry 的父类是JobLocator，其主要功能是跟踪Job执行情况。当Job在其他地方(例如，在子上下文中)创建时，它还可用于在应用程序上下文中集中收集Job。自定义的JobRegistry实现还可以用于操作已注册作业的名称和其他属性。框架只提供了一个实现，它基于从作业名称到作业实例的简单映射。

​	使用时@EnableBatchProcessing，JobRegistry是开箱即用的。如果要配置自己的：

```java
@Override
@Bean
public JobRegistry jobRegistry() throws Exception {
	return new MapJobRegistry();
}
```

​	有两种JobRegistry自动填充方法：使用bean后处理器和使用注册商生命周期组件。

​	1、JobRegistryBeanPostProcessor

​	这是一个bean后处理器，可以在创建所有作业时注册它们。

​	以下示例显示了如何包括JobRegistryBeanPostProcessor中定义的作业的：

```java
@Bean
public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor() {
    JobRegistryBeanPostProcessor postProcessor = new JobRegistryBeanPostProcessor();
    postProcessor.setJobRegistry(jobRegistry());
    return postProcessor;
}
```

​	虽然这不是严格必要的，但是示例中的后处理器已经被赋予了一个id，这样它就可以包含在子上下文中(例如，作为一个父bean定义)，并导致在那里创建的所有作业也被自动注册。

​	2、AutomaticJobRegistrar

​	这是一个生命周期组件，它创建子上下文，并在创建Job时从这些上下文注册Job。防止不同模块的Job具有相同名称的时候，在进行注册的时候不会发生冲突并相互替代。这使得集成应用程序的不同模块提供的作业变得更加容易。以下示例显示了如何包括AutomaticJobRegistrarJava中定义的作业的：

```java
@Bean
public AutomaticJobRegistrar registrar() {
 
    AutomaticJobRegistrar registrar = new AutomaticJobRegistrar();
    registrar.setJobLoader(jobLoader());
    registrar.setApplicationContextFactories(applicationContextFactories());
    registrar.afterPropertiesSet();
    return registrar;
 
}
```

​	JobRegistry有两个强制属性，一个是ApplicationContextFactory数组(这里是从一个工厂bean创建的)，另一个是JobLoader。JobLoader负责管理子上下文的生命周期，并在JobRegistry中注册作业。ApplicationContextFactory负责创建子上下文，最常见的用法是使用ClassPathXmlApplicationContextFactory。此工厂的特性之一是，默认情况下，它将一些配置从父上下文复制到子上下文。因此，如果子程序应该与父程序相同，则不必在子程序中重新定义PropertyPlaceholderConfigurer或AOP配置。

​	如果需要，AutomaticJobRegistrar可以与JobRegistryBeanPostProcessor一起使用(只要还使用DefaultJobLoader)。例如，如果在主父上下文和子位置都定义了作业，那么这是可取的。

### JobOperator

​	JobOperator提供了JobLauncher、JobRepository、JobExplorer和JobRegistry等不同组件的方法，相当于方法集合。



### JobParametersIncrementer

​	用于生成一个新的JobParameters。注意不是复制操作。

```java
public interface JobParametersIncrementer {
 
    JobParameters getNext(JobParameters parameters);
 
}
```

​	JobParametersIncrementer的约定是，给定一个JobParameters对象，它将递增该对象可能包含的任何必要值，从而返回“下一个”JobParameters对象。
因为框架无法知道对JobParameters的哪些更改使其成为“下一个”实例。对于任何帮助识别工作的数值都可以按照下面示例处理，如下所示:

```java
public class SampleIncrementer implements JobParametersIncrementer {

    public JobParameters getNext(JobParameters parameters) {
        if (parameters==null || parameters.isEmpty()) {
            return new JobParametersBuilder().addLong("run.id", 1L).toJobParameters();
        }
        long id = parameters.getLong("run.id",1L) + 1;
        return new JobParametersBuilder().addLong("run.id", id).toJobParameters();
    }

}
```

​	增量程序可以通过构建器中提供的增量程序方法与“Job”关联，如下所示:

```java
@Bean
public Job footballJob() {
    return this.jobBuilderFactory.get("footballJob")
    				 .incrementer(sampleIncrementer())
    				 ...
                     .build();
}
```



## SpringBatch表结构UML

![img](../../md-photo/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2h1YW55dW1pbmhhbw==,size_16,color_FFFFFF,t_70.png)



除此之外，还提供三个自增序列，分别是BATCH_STEP_EXECUTION_SEQ、BATCH_JOB_EXECUTION_SEQ、BATCH_JOB_SEQ。以下是定义语句：

```sql
CREATE SEQUENCE BATCH_STEP_EXECUTION_SEQ START WITH 0 MINVALUE 0 MAXVALUE 9223372036854775807 NOCYCLE;
CREATE SEQUENCE BATCH_JOB_EXECUTION_SEQ START WITH 0 MINVALUE 0 MAXVALUE 9223372036854775807 NOCYCLE;
CREATE SEQUENCE BATCH_JOB_SEQ START WITH 0 MINVALUE 0 MAXVALUE 9223372036854775807 NOCYCLE;
```

