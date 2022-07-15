# maven常见异常

## build不需要在父类定义

```
Unable to find main class
```

以下内容不需要在parent 父工程中进行定义，而是哪个模块使用到后再自行进行定义

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
        </plugin>
    </plugins>
</build>
```



