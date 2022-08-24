# Nacos下载和安装

## 下载

下载地址：

[Tags · alibaba/nacos (github.com)](https://github.com/alibaba/nacos/tags)

选择版本：

![image-20220815202751329](../../md-photo/image-20220815202751329.png)



下载linux操作系统上的server：

![image-20220815202825613](../../md-photo/image-20220815202825613.png)





## 安装

```bash
# 执行解压的操作
$ tar -xvf nacos-server-2.1.1.tar.gz 

# 将文件夹移动到工作文件夹下
$ mv nacos/ /gjmou/software/
```



## 支持MySQL

​	在nacos的本机安装mysql数据库，安装步骤参见：

**<font color='blue'>../软件安装/mysql下载与安装.md</font>**



​	在0.7版本之前，在单机模式时nacos使用嵌入式数据库实现数据的存储，不方便观察数据存储的基本情况。0.7版本增加了支持mysql数据源能力，具体的操作步骤：

1. 安装数据库，版本要求：5.6.5+
2. 初始化mysql数据库
3. 修改conf/application.properties文件，增加支持mysql数据源配置（目前只支持mysql），添加mysql数据源的url、用户名和密码。



```bash
# 修改数据连接的相关配置
$ vi /gjmou/software/nacos/conf/application.properties
# 修改为以下的配置
db.num=1
### Connect URL of DB:
db.url.0=jdbc:mysql://localhost:3306/nacos?characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true&useUnicode=true&useSSL=false&serverTimezone=UTC
db.user.0=remotes
db.password.0=123456
```

修改配置后的效果：

![image-20220815224022839](../../md-photo/image-20220815224022839.png)



mysql对应的配置信息：

![image-20220815224258779](../../md-photo/image-20220815224258779.png)



执行conf配置文件下的nacos-mysql.sql文件，初始化对应的表结构信息：

![image-20220815224332243](../../md-photo/image-20220815224332243.png)



执行sql后的结果：

![image-20220815225222660](../../md-photo/image-20220815225222660.png)



## 启动Nacos

```bash
# 启动Nacos命令
$ sh /gjmou/software/nacos/bin/startup.sh -m standalone

# 开放8848端口号
# 查看开放的端口
sudo firewall-cmd --zone=public --list-ports
# 添加端口
#27017改成要添加的端口（--permanent永久生效，没有此参数重启后失效）
firewall-cmd --zone=public --add-port=8848/tcp --permanent   
# 更新防火墙
sudo firewall-cmd --reload

# 查看nacos启动的日志
$ less /gjmou/software/nacos/logs/start.out 
```



添加启动单例Nacos的shell脚本nacos.sh：

```bash
#!/bin/bash
# 启动mysql
# mysql是启动集群的时候使用的
# `systemctl start mysqld`;

wait;
# 启动nacos
`nohup sh /gjmou/software/nacos/bin/startup.sh -m standalone > nohup.out 2>&1 &`;
```



nacos启动日志：

![image-20220815225956205](../../md-photo/image-20220815225956205.png)



访问nacos默认登录网址：

[Nacos默认登录网址](http://nacos1:8848/nacos/#/login)

默认登录用户：nacos/nacos





## 集群

修改集群时的jvm虚拟机内存大小：

```bash
$ vi /gjmou/software/nacos-cluster/nacos1/bin/startup.sh
```

![image-20220816124453108](../../md-photo/image-20220816124453108.png)



```bash
# 创建nacos集群文件夹
$ mkdir -p /gjmou/software/nacos-cluster


# 复制三个实例
$ cp -r /gjmou/software/nacos /gjmou/software/nacos-cluster/nacos1

# 先修改集群的配置信息，再复制另外两个实例
# 进入conf配置文件目录
$ cd /gjmou/software/nacos-cluster/nacos1/conf
# 复制集群配置文件
$ cp cluster.conf.example cluster.conf

$ vi cluster.conf
# 修改cluster.conf为以下内容
#it is ip
#example
nacos1:8858
nacos1:8868
nacos1:8878

# 修改application.properties的端口号为8858
$ vi application.properties
# 修改以下内容
server.port=8858


# 使用nacos1实例复制出nacos2和nacos3，其对应端口号分别为8868和8878，这里不再过多描述

# 开放端口8858 8868 8878
```



**<font color='red'>注意：在Nacos2.0以后，相对于之前的版本增加了gRPC的通信方式，简单来说 8801端口占用的偏移量是9801端口和9802端口、8802端口占用的偏移量是9802端口和9803端口、8803端口占用的偏移量是9803端口和9804端口，端口冲突了。所以伪集群的三个实例端口不能连续。</font>**



编写启动集群的shell脚本：

```bash
#!/bin/bash
echo "需要手动的启动每一个节点";
#####需要手动的启动每一个节点#####
# 启动mysql
# systemctl start mysqld
# 启动nacos1
# sh /gjmou/software/nacos-cluster/nacos1/bin/startup.sh;
# 启动nacos2
# sh /gjmou/software/nacos-cluster/nacos2/bin/startup.sh;
# 启动nacos3
# sh /gjmou/software/nacos-cluster/nacos3/bin/startup.sh;
```



启动后的结果：

![image-20220816124026096](../../md-photo/image-20220816124026096.png)



**<font color='red'>但是伪集群速度较慢，这里还是使用单例的nacos进行开发测试</font>**
