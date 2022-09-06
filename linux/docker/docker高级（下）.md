# docker高级（下）

## Compose

### 定义

Compose是Docker公司推出的一个工具软件，可以管理多个Docker容器组成一个应用。你需要定义一个YAML格式的配置文件docker-compose.yml，**<font color='red'>写好多个容器之间的调用关系</font>**。然后，只要一个命令，就能同时启动/关闭这些容器。

> docker建议我们每一个容器中只运行一个服务，因为docker容器本身占用资源极少，所以最好是将每个服务单独的分割开来但是这样我们又面临了一个问题？
>
> 如果我需要同时部署好多个服务,难道要每个服务单独写Dockerfie然后在构建镜像，构建容器，这样累都累死了，所以docker官方给我们提供了docker-compose多服务部署的工具
>
> 例如要实现一个Web微服务项目，除了Web服务容器本身，往往还需要再加上后端的数据库mysql服务容器，redis服务器，注册中心eureka，甚至还包括负载均衡容器等等......
>
> Compose允许用户通过一个单独的<font color='blue'>docker-compose.yml</font>模板文件(YAML格式）来定义<font color='red'>一组相关联的应用容器为一个项目（ project）</font>。
>
> 可以很容易地用一个配置文件定义一个多容器的应用，然后使用一条指令安装这个应用的所有依赖，完成构建。Docker-Compose解决了容器与容器之间如何管理编排的问题。



### 下载安装

参见：../../软件安装/docker-compose下载与安装.md



### Compose核心概念

一个文件：

**<font color='red'>	docker-compose.yml</font>**

两要素：

​	服务（service）：一个个应用容器实例，比如订单微服务、库存微服务、mysql容器、nginx容器或者redis容器

​	工程（project）：由一组关联的应用容器组成的一个**<font color='red'>完整业务单元</font>**，在docker-compose.yml文件中定义。



### Compose使用步骤

- 编写Dockerfile定义各个微服务应用并构建出对应的镜像文件。
- 使用docker-compose.yml定义一个完整业务单元，安排好整体应用中的各个容器服务。
- 最后，执行docker-compose up命令来启动并运行整个应用程序，完成一键部署上线。



### Compose常用命令

| 命令                                          | 作用                                         |
| --------------------------------------------- | -------------------------------------------- |
| docker-compose -h                             | 查看帮助                                     |
| docker-compose up                             | 启动所有docker-compose服务                   |
| docker-compose up -d                          | 启动所有docker-compose服务并后台运行         |
| docker-compose down                           | 停止并删除容器、网络、卷、镜像。             |
| docker-compose exec yml里面的服务id /bin/bash | 进入容器实例内部启动单独的服务               |
| docker-compose ps                             | 展示当前docker-compose编排过的运行的所有容器 |
| docker-compose top                            | 展示当前docker-compose编排过的容器进程       |
| docker-compose logs yml里面的服务id           | 查看容器输出日志                             |
| docker-compose config                         | 检查配置                                     |
| docker-compose config -q                      | 检查配置，有问题才有输出                     |
| docker-compose restart                        | 重启服务                                     |
| docker-compose start                          | 启动服务                                     |
| docker-compose stop                           | 停止服务                                     |



### 创建微服务工程并生成镜像

微服务中使用redis和mysql进行简单的插入和查询操作。

```bash
# 将微服务放到执行目录下
$ mkdir -p /gjmou/docker/compose/
$ mv docker-compose-server-1.0-SNAPSHOT.jar /gjmou/docker/compose/

# 编写Dockfile文件
$ cd /gjmou/docker/compose/
$ vi Dockfile
# 录入以下的内容
```

```dockerfile
# 基础镜像使用Java
FROM java:8
# 作者
MAINTAINER gjmou
# VOLUME 指定临时文件目录为/tmp，将宿主机的/gjmou/docker/compose/tmp目录作为其容器卷
VOLUME  /local/docker/compose/tmp /tmp
# 将jar包添加到容器中并进行重命名的操作
ADD docker-compose-server-1.0-SNAPSHOT.jar web_app.jar
# 运行jar包
ENTRYPOINT ["java", "-jar", "/web_app.jar"]
# 暴露8097端口作为服务
EXPOSE 8097
```



```bash
# 构建docker镜像
$ docker build -t docker-compose:1.0.0 .

# 启动docker容器
$ docker run -d -p 9099:9099 --name=docker-compose docker-compose:1.0.0
```



### 镜像启动生成容器

#### 不使用compose

##### 安装mysql

```bash
# 启动mysql
$ docker run -d --name=mysql -p 3306:3306 --privileged=true -v /local/mysql/log:/var/log/mysql -v /local/mysql/data:/var/lib/mysql -v /local/mysql/conf:/etc/mysql/conf.d -e MYSQL_ROOT_PASSWORD=123456 mysql

# 进入docker容器 ！！注意修改数据库字符集！！
$ docker exec -it mysql bash

# 连接mysql服务
$docker mysql -uroot -p123456

# 创建数据库
$docker>mysql create database Spring;

# 创建对应的表结构
$docker>mysql
略
```



##### 安装redis

```bash
# 启动redis实例即可
$ docker run -d -p 6379:6379 --name=redis --privileged=true -v /local/redis/conf/redis.conf:/etc/redis/redis.conf -v /local/redis/data:/data redis:6.0.8 redis-server /etc/redis/redis.conf
```



##### 测试结果

![image-20220831202511294](../../../md-photo/image-20220831202511294.png)

#### 使用compose

##### 编写docker-compose.yml文件

```bash
# 编写yml配置文件
$ vi /gjmou/docker/compose/docker-compose.yml
# 录入以下的内容
```

配置信息参见注释：

```yml
# compose的版本号
version: "3"
# 所有的docker容器
services:
  ##### 自己的微服务 #####
  docker-compose:
    # 镜像名称
    image: docker-compose:1.0.0
    # 容器名称
    container_name: docker-compose-file
    # 端口映射
    ports:
      - "9099:9099"
    # 容器卷
    volumes:
      - /local/compose/app/data:/data
    # 网络配置
    networks:
      - compose-net
    # 依赖于哪些容器
    depends_on:
      - redis
      - mysql

  ##### redis #####
  # 没有配置容器名称的话，会自动加前后缀，如在compose目录下，那么就是compose_redis_1
  redis:
    image: redis:6.0.8
    container_name: redis-compose
    ports:
      - "6379:6379"
    volumes:
      - /local/compose/redis/redis.conf:/etc/redis/redis.conf
      - /local/compose/redis/data:/data
    networks:
      - compose-net
    # 启动命令
    command: redis-server /etc/redis/redis.conf

  ##### mysql #####
  mysql:
    image: mysql
    container_name: mysql-compose
    # MYSQL环境信息，参见官网
    environment:
      MYSQL_ROOT_PASSWORD: '123456'
      MYSQL_ALLOW_EMPTY_PASSWORD: 'no'
      MYSQL_DATABASE: 'Spring'
      MYSQL_USER: 'remotes'
      MYSQL_PASSWORD: '123456'
    ports:
      - "3306:3306"
    volumes:
      - /local/compose/mysql/log:/var/log/mysql
      - /local/compose/mysql/data:/var/lib/mysql
      - /local/compose/mysql/conf:/etc/mysql/conf.d
    networks:
      - compose-net
    command: --default-authentication-plugin=mysql_native_password #解决外部无法访问问题
    
# 网络配置
networks:
  # 启动时会自动加linux前缀，如在compose目录下，那么网络名称为compose_compose-net
  compose-net:
```



**<font color='red'>将微服务中的redis地址改为'redis'，mysql地址改为'mysql'，用的就是docker-compose.yml中的services名称</font>**



##### 开始构建容器

```bash
# 进入docker-compose.yml所在文件目录
$ cd /gjmou/docker/compose
# 检查docker-compose.yml是否有语法错误
$ docker-compose config -q

# 后台启动所有容器
$ docker-compose up -d
```

启动结果：

![image-20220831210721331](../../../md-photo/image-20220831210721331.png)



**<font color='blue'>进入mysql中创建对应的数据库和表结构。</font>**



##### 结果测试

![image-20220831211733912](../../../md-photo/image-20220831211733912.png)





##### 停止容器

```bash
# 进入docker-compose.yml所在文件目录
$ cd /gjmou/docker/compose
# 关闭所有容器
$ docker-compose stop
```

![image-20220831212135989](../../../md-photo/image-20220831212135989.png)





## 可视化工具Portainer

### 下载与安装

```bash
# 启动命令 --restart=always 伴随着docker的启动而启动
$ docker run -d -p 8000:8000 -p 9443:9443 --name portainer --restart=always -v /var/run/docker.sock:/var/run/docker.sock -v portainer_data:/data portainer/portainer-ce
```



### 前端登录网址

主机名 + 9443端口号即可：

https://docker:9443/



设置账号和登录密码：admin 12345678

![image-20220831220456885](../../../md-photo/image-20220831220456885.png)



使用方式：

![image-20220831221534743](../../../md-photo/image-20220831221534743.png)

![image-20220831221620195](../../../md-photo/image-20220831221620195.png)



### 可视化安装nginx

![image-20220831221804443](../../../md-photo/image-20220831221804443.png)



![image-20220831221936520](../../../md-photo/image-20220831221936520.png)



![image-20220831222052409](../../../md-photo/image-20220831222052409.png)



发布容器：

![image-20220831222115829](../../../md-photo/image-20220831222115829.png)



![image-20220831222224481](../../../md-photo/image-20220831222224481.png)

![image-20220831222355770](../../../md-photo/image-20220831222355770.png)



## 容器监控CIG

### 定义

通过docker stats命令可以很方便的看到当前宿主机上所有容器的CPU,内存以及网络流量等数据，一般小公司够用了。但是，docker stats统计结果只能是<font color='red'>当前宿主机的全部容器</font>，数据资料是实时的，<font color='red'>没有地方存储，没有健康指标过线预警等功能</font>。

由此提供以下的解决方案：

CAdvisor监控收集+lnfluxDB存储数据+Granfana展示图表

![image-20220831223642439](../../../md-photo/image-20220831223642439.png)

### CAdvisor

CAdvisor是一个容器资源监控工具，包括容器的内存，CPU，网络IO，磁盘IO等监控，同时提供了一个WEB页面用于查看容器的实时运行状态。CAdvisor默认存储2分钟的数据，而且只是针对单物理机。不过，CAdvisor提供了很多数据集成接口，支持InfluxDB，Redis，Kafka，Elasticsearch等集成,可以加上对应配置将监控数据发往这些数据库存储起来。
CAdvisor功能主要有两点:

- 展示Host和容器两个层次的监控数据。
- 展示历史变化数据。



### InfluxDB

InfluxDB是用Go语言编写的一个开源分布式时序、事件和指标数据库,无需外部依赖。
CAdvisor默认只在本机保存最近2分钟的数据，为了持久化存储数据和统—收集展示监控数据，需要将数据存储到InfluxDB中。InfluxDB是一个时序数据库,专门用于存储时序相关数据，很适合存储CAdvisor的数据。而且，CAdvisor本身已经提供了InfluxDB的集成方法，丰启动容器时指定配置即可。
lnfluxDB主要功能:

- 基于时间序列,支持与时间有关的相关函数(如最大、最小、求和等)。
- 可度量性：你可以实时对大量数据进行计算。
- 基于事件：它支持任意的事件数据。



### Granfana

Grafana是一个开源的数据监控分析可视化平台,支持多种数据源配置(支持的数据源包括
InfluxDB，MySQLElasticsearch，OpenTSDB，Graphite等)和丰富的插件及模板功能，支持图表权限控制和报警。
Grafan主要特性：

- 灵活丰富的图形化选项。
- 可以混合多种风格。
- 支持白天和夜间模式。
- 多个数据源。



### compose容器编排

```bash
# 创建使用的目录
$ mkdir -p /gjmou/docker/cig
# 创建docker-compose.yml文件
$ vi docker-compose.yml
# 录入以下部分的内容
```

```yml
version: '3.1'

volumes:
  grafana_data: {}

services:
  influxdb:
    image: tutum/influxdb:0.9
    #image: tutum/influxdb
    #image: influxdb
    restart: always
    #user:
    environment:
      - PRE_CREATE_DB=cadvisor
    ports:
      - "8083:8083"
      - "8086:8086"
    expose:
      - "8090"
      - "8099"
    volumes:
      - ./data/influxdb:/data

  cadvisor:
    #image: google/cadvisor:v0.29.0
    image: google/cadvisor
    links:
      - influxdb:influxsrv
    command: -storage_driver=influxdb -storage_driver_db=cadvisor -storage_driver_host=influxsrv:8086
    restart: always
    ports:
      - "8080:8080"
    volumes:
      - /:/rootfs:ro
      - /var/run:/var/run:rw
      - /sys:/sys:ro
      - /var/lib/docker/:/var/lib/docker:ro

  grafana:
    #image: grafana/grafana:2.6.0
    image: grafana/grafana
    user: "104"
    #user: "472"
    restart: always
    links:
      - influxdb:influxsrv
    ports:
      - "3000:3000"
    volumes:
      # 这里使用的是docker系统的容器卷
      - grafana_data:/var/lib/grafana
    environment:
      - HTTP_USER=admin
      - HTTP_PASS=admin
      - INFLUXDB_HOST=influxsrv
      - INFLUXDB_PORT=8086
      - INFLUXDB_NAME=cadvisor
      - INFLUXDB_USER=root
      - INFLUXDB_PASS=root
```

```bash
# 编译看是否通过
$ docker-compose config -q
# 启动docker-compose文件
$ docker-compose up -d
```

![image-20220831230747380](../../../md-photo/image-20220831230747380.png)



测试容器是否启动成功：

CAdvisor：

[cAdvisor - /](http://docker:8080/containers/)

![image-20220831232743452](../../../md-photo/image-20220831232743452.png)



influxdb：

[InfluxDB - Admin Interface](http://docker:8083/)

![image-20220831232758424](../../../md-photo/image-20220831232758424.png)



grafana：

[Grafana](http://docker:3000/login)

默认登录账号密码为：admin admin

![image-20220831232815405](../../../md-photo/image-20220831232815405.png)



### cig添加panel

![image-20220901085819489](../../../md-photo/image-20220901085819489.png)

![image-20220901085854757](../../../md-photo/image-20220901085854757.png)

配置数据源：

![image-20220901090029627](../../../md-photo/image-20220901090029627.png)



配置数据库信息，其中默认用户名为root，密码为root：

![image-20220901090103824](../../../md-photo/image-20220901090103824.png)



连接数据库成功：

![image-20220901090212904](../../../md-photo/image-20220901090212904.png)



创建新的panel：

![image-20220901090337383](../../../md-photo/image-20220901090337383.png)



选择曲线和柱状图：

![image-20220901090543143](../../../md-photo/image-20220901090543143.png)



保存panel：

![image-20220901090620529](../../../md-photo/image-20220901090620529.png)

![image-20220901090722064](../../../md-photo/image-20220901090722064.png)



### cig配置监控业务规则

![image-20220901221827141](../../../md-photo/image-20220901221827141.png)

进行相关的配置：

![image-20220901222028836](../../../md-photo/image-20220901222028836.png)

成功监控到对应的数据：

![image-20220901222152104](../../../md-photo/image-20220901222152104.png)

