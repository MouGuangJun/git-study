# CAS自旋锁



## 背景

- CAS？
- 自旋是什么意思？
- CAS是怎么保证原子性操作的？
- CAS带来的ABA问题，及解决方案？



## 过程

- **CAS** compareAndSet，compareAndExchange，compareAndSwap

  这个函数会先进行比较，如果相比较的两个值是相等的，那么就进行更新操作。

- **CAS使用场景**

```java
AtomicInteger atomicInteger = new AtomicInteger(1);
atomicInteger.compareAndSet(1, 2);
```

1.初始值是1
2.compareAndSet(expect, update), 要想更新成功的话，那么这个expect的值一定是1，否则是不会被更新的。

```java
AtomicInteger atomicInteger = new AtomicInteger(1);
atomicInteger.compareAndSet(2, 3);
```

上面的语句，就永远无法把值更新为3。

- **自旋操作**

  线程一直在执行逻辑，也就是一直让CPU进行计算操作。

- **CAS保证原子性操作**

![在这里插入图片描述](../../../md-photo/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L291dHNhbmRpbmc=,size_16,color_FFFFFF,t_70.png)

![在这里插入图片描述](../../../md-photo/20201213163858588.png)

是一个unsafe类下面的native方法。
最终保证原子性是通过，硬件层面的指令，comxchg完成的。如果是多核的情况下，就会上锁。LOCK_IF_MP（mutil Processors）

- **ABA问题及解决方案**

![image](../../../md-photo/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L291dHNhbmRpbmc=,size_16,color_FFFFFF,t_70-16591000687343.png)

1.Thread1和Thread2都拿到值，都为A
2.Thread2把A值更新为B
3.此时，Thread1还在处理自己的业务逻辑，然后Thread3拿到的值是B，然后又把值更新为A
4.此时，Thread1进行更新操作，发现更新成功了。出现了ABA问题，按理说，Thread1是不能更新值的。
5.更新的时候添加一个version版本号就可以了，解决这个问题。在理解上，可以理解为乐观锁。其实，加这个版本号，相当于又是一个CAS过程。
		

```java
Student student = new Student();
student.setStatus(1);
AtomicReference<Student> studentAtomicReference = new AtomicReference<>(student);
if (studentAtomicReference.get().getStatus() == 1) {
    Student updateStudent = new Student();
    updateStudent.setStatus(2);
    studentAtomicReference.compareAndSet(student, updateStudent);
}
```



## 小结

1.理解CAS的概念。
2.理解原子类底层实现都是CAS。
3.理解CAS是通过CPU指令来达到原子性的。
4.理解CAS存在的ABA问题，以及相应的解决方案。