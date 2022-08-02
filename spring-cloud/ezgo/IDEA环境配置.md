# IDEA环境配置

## Maven选取

![image-20220626164306480](../../../md-photo/image-20220626164306480.png)



## 字符集设置

![image-20220626164551359](../../../md-photo/image-20220626164551359.png)



## 注解激活生效

![image-20220626164644189](../../../md-photo/image-20220626164644189.png)



## 编译器选择8版本

![image-20220626164811412](../../../md-photo/image-20220626164811412.png)



## FileType过滤

过滤多余的idea、maven的配置文件

![image-20220626164941023](../../../md-photo/image-20220626164941023.png)



## Spring热部署

引入依赖：

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-devtools</artifactId>
    <scope>runtime</scope>
    <optional>true</optional>
</dependency>
```



添加插件：

```xml
<plugin>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-maven-plugin</artifactId>
</plugin>
```



修改idea设置：

![image-20220703202911585](../../../md-photo/image-20220703202911585.png)



快捷键：ctrl + alt + shift + /

![image-20220703202958746](../../../md-photo/image-20220703202958746.png)

![image-20220703203041517](../../../md-photo/image-20220703203041517.png)



## Run Dashboard

![image-20220703214237600](../../../md-photo/image-20220703214237600.png)



![image-20220703214332208](../../../md-photo/image-20220703214332208.png)



在RunDashboard配置下添加如下配置：

```xml
<component name="RunDashboard">
      <!-- 1、配置显示 -->
    <option name="configurationTypes">
      <set>
        <option value="SpringBootApplicationConfigurationType" />
      </set>
    </option>
</component>
```

重启IDEA