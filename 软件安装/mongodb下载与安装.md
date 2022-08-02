# Mongodb下载与安装

## 下载

[MongoDB 源码下载地址](https://www.mongodb.com/download-center#community)

![img](../../md-photo/0D72BC20-1D77-437E-972C-286EB5EFB183.jpg)

![img](../../md-photo/558D36F2-01AF-49C3-BA07-F2728B216C87.jpg)



这里我们选择 tgz 下载，下载完安装包，并解压 **tgz**（以下演示的是 64 位 Linux上的安装） 。

```bash
tar -xvf mongodb-linux-x86_64-rhel70-5.0.9.tgz #解压

mv mongodb-linux-x86_64-rhel70-5.0.9  /gjmou/sofeware #移动到软件存放位置

mv mongodb-linux-x86_64-rhel70-5.0.9 mongodb #重命名

 vi /etc/profile #修改环境变量
```

修改环境变量：

![image-20220714090235083](../../md-photo/image-20220714090235083.png)

生效环境变量：

```bash
source /etc/profile
```



## 创建数据库目录

- 数据存储目录：/gjmou/software/mongodb/data
- 日志文件目录：/gjmou/software/mongodb/data

**创建这两个目录**：

```bash
sudo mkdir -p /gjmou/software/mongodb/data
sudo mkdir -p /gjmou/software/mongodb/logs
```

在bin目录下添加mongodb.conf文件，内容如下：

```bash
# 配置信息
port=27017 #端口
bind_ip=0.0.0.0 #默认是127.0.0.1
dbpath=/gjmou/software/mongodb/data #数据库存放
logpath=/gjmou/software/mongodb/logs/mongod.log #日志文件
fork=true #设置后台运行
#auth=true #开启认证
```

<font color="red">**启动 Mongodb 服务**</font>

**配置文件**的启动方式：

```bash
cd /gjmou/software/mongodb/bin/
mongod --config mongodb.conf #通过配置文件的方式进行启动
```



使用普通命令启动：（<font color="green">*不建议使用*</font>）

```bash
# 普通命令启动
$ mongod --dbpath /gjmou/software/mongodb/data --logpath /gjmou/software/mongodb/logs/mongod.log

# 使用配置方式启动的命令
$ mongod --config XXX.config
```



## MongoDB 后台管理 Shell

命令格式：

```bash
$ mongo -u 用户名 -p 密码 --port 端口号 --host IP数据库名
```

如果你需要进入 mongodb 后台管理，你需要先打开 mongodb 装目录的下的 **bin 目录**，然后执行**mongo 命令**文件。

MongoDB Shell 是 MongoDB 自带的交互式 Javascript shell，用来对 MongoDB 进行操作和管理的交互式环境。

当你进入 mongoDB 后台后，它默认会链接到 test 文档（数据库）：

```bash
$ cd /gjmou/software/mongodb/bin/
$ mongo
MongoDB shell version v5.0.9
connecting to: mongodb://127.0.0.1:27017/?compressors=disabled&gssapiServiceName=mongodb
Implicit session: session { "id" : UUID("c61e803b-b654-456c-a24e-e58e8666b9d9") }
MongoDB server version: 5.0.9
```



由于它是一个JavaScript shell，可以运行一些**简单的算术运算**:

```bash
> 2 + 2
4
> 3 * 3
9
> 
```

现在让我们插入一些简单的数据，并对插入的**数据进行检索**：

```bash
> db.runoob.insert({x:10}) #将数字 10 插入到 runoob 集合的 x 字段中
WriteResult({ "nInserted" : 1 })
> db.runoob.find()
{ "_id" : ObjectId("62cf9bf5e680d70811c9a5a2"), "x" : 10 }
> 
```



**停止 mongodb**可以使用以下命令：

```bash
mongod --dbpath /gjmou/software/mongodb/data --logpath /gjmou/software/mongodb/logs/mongod.log --shutdown
```

也可以在 mongo 的命令出口中实现：

```bash
> use admin
switched to db admin
> db.shutdownServer()
```



## 图形化管理界面

下载地址：

[Try MongoDB Atlas Products | MongoDB](https://www.mongodb.com/try/download/compass)



<font color="red">**防火墙规则添加端口号：27017**</font>

```bash
# 查看开放的端口
sudo firewall-cmd --zone=public --list-ports

# 添加端口
#27017改成要添加的端口（--permanent永久生效，没有此参数重启后失效）
firewall-cmd --zone=public --add-port=27017/tcp --permanent   

# 更新防火墙
sudo firewall-cmd --reload
```



启动compass，添加配置信息：

![image-20220714234629730](../../md-photo/image-20220714234629730.png)



连接成功：

![image-20220714234703990](../../md-photo/image-20220714234703990.png)
