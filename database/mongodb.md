# mongodb常用命令

## 创建数据库

### 创建数据库

```bash
# use DATABASE_NAME
$ use runoob #创建数据库runoob
switched to db runoob
```

如果数据库不存在，则创建数据库，否则**切换到指定数据库**。



### 查看所有数据库

```bash
$ show dbs
admin   0.000GB
config  0.000GB
local   0.000GB
spring  0.000GB
```

<font color="red">*可以看到，我们刚创建的数据库 runoob 并不在数据库的列表中， 要显示它，我们需要向 runoob 数据库插入一些数据。*</font>

```bash
$ db.runoob.insert({"name":"菜鸟教程"})
WriteResult({ "nInserted" : 1 })

$ show dbs
admin   0.000GB
config  0.000GB
local   0.000GB
spring  0.000GB
test    0.000GB
```

<font color="red">*MongoDB 中默认的数据库为 test，如果你没有创建新的数据库，集合将存放在 test 数据库中。*</font>

**注意:** *在 MongoDB 中，集合只有在内容插入后才会创建! 就是说，创建集合(数据表)后要再插入一个文档(记录)，集合才会真正创建。*



## MongoDB 删除数据库

### 语法

MongoDB 删除数据库的语法格式如下：

```bash
$ db.dropDatabase()
```

删除**当前数据库**，默认为 test，你可以使用 db 命令查看当前数据库名。

### 实例

以下实例我们删除了数据库 runoob。

首先，查看所有数据库：

```bash
$ show dbs
admin   0.000GB
config  0.000GB
local   0.000GB
runoob  0.000GB
spring  0.000GB
```

接下来我们切换到数据库 runoob，执行删除的操作

```bash
$ use runoob
switched to db runoob

# 执行删除命令
$ db.dropDatabase()
{ "ok" : 1 }

# 数据库runoob已经被删除
$ show dbs
admin   0.000GB
config  0.000GB
local   0.000GB
spring  0.000GB


```

### 删除集合

集合删除语法格式如下：

```bash
$ db.collection.drop()
```

以下实例删除了 runoob 数据库中的集合 site：

```bash
$ use runoob #使用runoob数据库
switched to db runoob

$ db.createCollection("site") #创建site集合
{ "ok" : 1 }

$ show collections #查看所有的集合,show collections 命令会更加准确点
runoob
site

$ db.site.drop() #删除选中的集合/表
true

$ show tables #查看所有的表
runoob
```



## SpringBoot整合mongodb

pom引入依赖：

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-mongodb</artifactId>
</dependency>

```

