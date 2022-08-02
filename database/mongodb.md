# mongodb常用命令

## 数据库

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

$ db #查看当前数据库
spring
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



### 删除数据库

MongoDB 删除数据库的语法格式如下：

```bash
$ db.dropDatabase()
```

删除**当前数据库**，默认为 test，你可以使用 db 命令查看当前数据库名。



以下*实例*我们删除了数据库 runoob。

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



## 集合collections

### 显式创建集合

创建集合的语法格式如下：

```bash
$ db.createCollection(collection)
```

创建site集合：

```bash
$ use runoob #使用runoob数据库
switched to db runoob

$ db.createCollection("site") #创建site集合
{ "ok" : 1 }
```



### 隐式创建集合

在插入文档的时候，如果集合不存在，则会默认创建集合



### 删除集合

集合删除语法格式如下：

```bash
$ db.collection.drop()
```

以下实例删除了 runoob 数据库中的集合 site：

```bash
$ show collections #查看所有的集合,show collections 命令会更加准确点
runoob
site

$ db.site.drop() #删除选中的集合/表
true

$ show tables #查看所有的表
runoob
```



## 文档的CRUD

### 文档的插入

（1）单个文档插入

使用insert()或者save()方法向集合中插入文档，格式如下：

```bash
$ db.collection.insert(
	<document or array of documents>,
	{
		writeConcern: <document>,
		ordered: <boolean>
	}
)
```



参数：

| Parameter    | Type              | Description                                                  |
| ------------ | ----------------- | ------------------------------------------------------------ |
| document     | document or array | 要插入到集合中的文档或文档数组（json格式）                   |
| writeConcern | document          | Write Concern，简称MongoDB写入安全机制，是一种客户端设置，用于控制写入安全的级别（了解） |
| ordered      | boolean           | true -> 按顺序插入数组中的文档，如果一个文档错误，直接返回不处理其他文档，false -> 执行无顺插入，如果一个文档错误，继续处理数组中的主文档 |

示例：

向comment的集合（表）中插入一条测试数据：

```bash
$ db.comment.insert(
{
    "articleid": "100000",
    "content": "今天天气真好，阳光明媚",
    "userId": "1001",
    "nickName": "rose",
    "createDateTime":new Date(),
    "likeNum":NumberInt(10),
    "status": null
})
```

**Tips**：

- comment集合如果不存在，则会隐式创建
- mongo中的数字，默认情况下是double类型，如果要存入整型，必须使用函数NumberInt（整形数字）
- 插入当前日期使用new Date()
- 插入的数据没有指定_id，会自动生成主键值
- 如果某字段没有值，可以赋值为null，或者不写该字段

出现以下结果代表插入成功：

```bash
WriteResult({ "nInserted" : 1 })
```



（2）批量插入

语法：

```bash
$ db.collection.insertMany(
	[<document1>, <document2>,...],
	{
		writeConcern: <document>,
		ordered: <boolean>
	}
)
```



示例：

批量插入多条文章评论：

```bash
$ db.comment.insertMany([
	{
    "_id": "1",
    "articleid": "100001",
    "content": "不应该浪费时间在学习上",
    "userId": "1002",
    "nickName": "江湖之人",
    "createDateTime":new Date("2019-08-05T22:08:15.522Z"),
    "likeNum":NumberInt(1000),
    "status": "1"},{
    "_id": "2",
    "articleid": "100001",
    "content": "夏天喝开水，冬天喝冰水",
    "userId": "1003",
    "nickName": "伊人憔悴",
    "createDateTime":new Date(),
    "likeNum":NumberInt(8888),
    "status": "1"},{
    "_id": "3",
    "articleid": "100001",
    "content": "专家说不能空腹吃饭，影响健康",
    "userId": "1004",
    "nickName": "健康砖家",
    "createDateTime":new Date("2019-09-05T22:06:15.523Z"),
    "likeNum":NumberInt(777),
    "status": "1"}
])

{ "acknowledged" : true, "insertedIds" : [ "1", "2", "3" ] } #插入成功的提示
```



### 文档的查询

语法：

```bash
$ db.collection.find(<query>, [projection])
```

如果你需要以易读的方式来读取数据，可以使用 pretty() 方法，语法格式如下：

```bash
$ db.collection.find(<query>, [projection]).pretty()
```



参数：

| Parameter  | Type     | Description                                                  |
| ---------- | -------- | ------------------------------------------------------------ |
| query      | document | 可选。使用查询运算符指定选择筛选器。                         |
| projection | document | 可选。执行要在与查询筛选器匹配的文档中要返回的字段（映射）。如果返回所有字段，忽略该参数 |

示例：

<font color="green">**（1）查询所有字段：**</font>

查询所有

```bash
$ db.comment.find()
$ db.comment.find({})
```



查询userId为1003的数据：**按照条件进行查询**

```bash
$ db.comment.find({userId:'1003'}).pretty()

#查询结果
{
        "_id" : "2",
        "articleid" : "100001",
        "content" : "夏天喝开水，冬天喝冰水",
        "userId" : "1003",
        "nickName" : "伊人憔悴",
        "createDateTime" : ISODate("2022-07-15T13:33:37.299Z"),
        "likeNum" : 8888,
        "status" : "1"
}
```



只查询第一条数据：

```bash
$ db.comment.findOne({articleid:"100001"})
```

<font color="green">**（2）查询部分字段：**</font>

如果要查询部分字段，则使用投影查询：

示例：

查询结果只展示_id, userId, nickName：

```bash
$ db.comment.find({articleid:"100001"}, {userId:1,nickName:1})
{ "_id" : "1", "userId" : "1002", "nickName" : "江湖之人" }
{ "_id" : "2", "userId" : "1003", "nickName" : "伊人憔悴" }
{ "_id" : "3", "userId" : "1004", "nickName" : "健康砖家" }

# 发现id默认会展示出来，如果不想展示出来：
$ db.comment.find({articleid:"100001"},{userId:1,nickName:1,_id:0})
{ "userId" : "1002", "nickName" : "江湖之人" }
{ "userId" : "1003", "nickName" : "伊人憔悴" }
{ "userId" : "1004", "nickName" : "健康砖家" }

# 得出结论，后面的1/0是空值字段是否展示
```



### 文档的更新

语法：

```bash
$ db.collection.update(query, update, options)
# 或者
$ db.collection.update(
	<query>,
	<udpate>,
	{
		upsert:<boolean>,
		multi:<boolean>,
		writeConcern:<document>,
		collation:<document>,
		arrayFilters:[<filterdocument1>,...],
		hint:<document | string>
	}
)
```



参数：

| Parameter    | Type                 | Description                                                  |
| ------------ | -------------------- | ------------------------------------------------------------ |
| query        | document             | 更新的选择条件                                               |
| update       | document or pipeline | 要应用的更改                                                 |
| upsert       | boolean              | true -> 如果没有与查询条件匹配的时候创建新文档               |
| multi        | boolean              | true -> 更新符合条件查询的多个文档                           |
| writeConcern | document             | 表示写问题的文档，抛出异常的级别                             |
| collation    | document             | 指定要用与操作的校对规则<br/>校对规则允许用户为字符串比较指定特定的语言的规则，例如字母大小写和重音标记的规则。<br/>校对规则选项具有以下语法：<br/>校对规则:{<br/>区域设置:<string><br/>caseLevel:<boolean>,<br/>caseFirst:<string>,<br/>强度:<int>,<br/>numbericordering:<boolean>,<br/>替代:<string>,<br/>最大变量:<string>,<br/>向后:<boolean><br/>}<br/>指定校对规则时，区域设置字段是必须的 |
| arrayFilters | array                | 一个筛选文档数组，用于确定要为数组字段上的更新操作修改哪些数组元素 |
| hint         | document or string   | 指定用于支持查询谓词的索引的文档或字符串                     |



示例：

（1）覆盖的修改：

修改id为1记录的点赞量为1001，输入以下的语句：

```bash
$ db.comment.update({_id:"1"}, {likeNum:NumberInt(1001)})
```

执行后发现，这条文档除了likeNum字段，其他字段都不见了

（2）局部修改（使用修改器**$set**来实现）

```bash
$ db.comment.update({_id:"1"}, {$set:{likeNum:NumberInt(1001)}})
# 更新成功
WriteResult({ "nMatched" : 1, "nUpserted" : 0, "nModified" : 1 })
```

（3）批量修改

更新所有用户为1004的昵称为网王砖家

```bash
# 默认只修改第一条数据
$ db.comment.update({userId:"1004"}, {$set:{nickName:"王砖家"}})

# 修改所有符合条件的数据
$ db.comment.update({userId:"1004"}, {$set:{nickName:"王砖家"}}, {multi:true})
```



（4）列值增长的修改

如果想实现对某列值在原有的基础上进行增加或者减少，可以使用**$inc**运算符来实现

对3号数据的点赞数，每次递增1

```bash
$ db.comment.update({_id:"3"},{$inc:{likeNum:NumberInt(1)}})
```



### 删除文档

删除文档的语法结构：

```bash
$ db.collection.remove(query)
```

将数据全部删除：

```bash
$ db.comment.remove({})
```

如果删除_id=5的记录，输入以下语句：

```bash
$ db.comment.remove({_id:"5"})
```



## 文档的分页查询

### 统计查询

统计查询使用count()方法，语法如下：

```bash
$ db.collection.count(query, options)
```

参数：

| Parameter | Type     | Description            |
| --------- | -------- | ---------------------- |
| query     | document | 查询选择的条件         |
| options   | document | 用于修改计数的额外选项 |

示例：

（1）统计所有记录数：

```bash
$ db.comment.count()
5
```

（2）按条件统计记录数

统计userId为1003的记录数

```bash
$ db.comment.count({userId:"1003"})
```

<font color="red">*默认情况下count()方法返回符合条件的全部记录条数*</font>



### 分页列表查询

可以使用limit()方法来读取指定数量的数据，使用skip()方法来跳过指定数量的数据

基本语法格式如下所示：**与Java的limit和skip方法想过一样**

```bash
# 其中Numer是需要限定/跳过的数量
$ db.collection.find().limit(Numer).skip(Number)
```



分页查询，需求：每页两条记录

```bash
# 第一页
$ db.comment.find().skip(0).limit(2)
# 第二页
$ db.comment.find().skip(2).limit(2)
# 第三页
$ db.comment.find().skip(4).limit(2)
```



### 排序查询

sort()方法对数据进行排序，通过参数指定排序字段排序，1 -> 升序；-1 -> 降序

语法：

```bash
# 其中KEY是需要排序的字段
$ db.collection.find().sort({KEY:1})
```

例子：

对userId进行降序排序，并对点赞量进行升序排序

```bash
$ db.comment.find().sort({userId:1, likeNum:-1})
```

<font color="red">**Tips：sort(),limit(),skip()三个放到一起执行时，sort() > skip() > limit()，跟命令的顺序无关**</font>



## 文档的复杂查询

### 正则表达式查询

MongoDB的模糊查询通过正则表达式的方式实现，格式：

```bash
# db.集合.find(字段:/正则表达式/)
$ db.collection.find({KEY:/正则表达式/})
```

<font color='red'>**Tips：正则表达式是JS的语法**</font>

查询评论内容中包含“水“的所有文档：

```bash
$ db.comment.find({content:/水/})
```

查询评论内容中以“专家“开头的所有文档：

```bash
$ db.comment.find({content:/^专家/})
```

查询评论内容中以“健康“结束的所有文档：

```bash
$ db.comment.find({content:/健康$/})
```



### 比较查询

<,	<=,	>,	>=,	!=

```bash
# KEY -> 字段，VALUE -> 值
db.collection.find({KEY:{$gt : VALUE}}) # KEY > VALUE
db.collection.find({KEY:{$lt : VALUE}}) # KEY < VALUE
db.collection.find({KEY:{$gte : VALUE}}) # KEY >= VALUE
db.collection.find({KEY:{$lte : VALUE}}) # KEY <= VALUE
db.collection.find({KEY:{$ne : VALUE}}) # KEY != VALUE
```

查询点赞数量大于700的记录

```bash
$ db.comment.find({likeNum:{$gt:NumberInt(700)}})
```



### 包含查询

包含使用**$in**操作符

查询用户userId：1003和1004评论的文档

```bash
$ db.comment.find({userId:{$in:["1003", "1004"]}})
```

不包含使用**$nin**操作符

查询非用户userId：1003和1004评论的文档

```bash
$ db.comment.find({userId:{$nin:["1003", "1004"]}})
```



### 条件连接查询

如果需要查询同时满足两个以上的条件，需要使用**$and**操作符进行关联

语法：

```bash
$ db.comment.find({$and:[{KEY1:VALUE1}, {KEY2:VALUE2},...]})
```

查询评论集合中点赞数超过700并且小于2000的文档

```bash
$ db.comment.find({$and:[{likeNum:{$gte:700}}, {likeNum:{$lt:2000}}]})
```



如果两个以上的条件是或者的关系，那我们使用操操作符**$or**进行关联

语法：

```bash
$ db.comment.find({$or:[{KEY1:VALUE1}, {KEY2:VALUE2},...]})
```

查询评论集合中userId为1003，或者点赞数小于1000的文档记录

```bash
$ db.comment.find({$or:[{userId:"1003"}, {likeNum:{$lt:1000}}]})
```





## 索引-index

### 查看索引

返回一个集合的所有索引，语法：

```bash
$ db.collection.getIndexes()
# mongodb默认创建的索引
[
    {
        "v": 2, # 索引版本号
        "key": {
            "_id": 1 # 索引字段，1 -> 升序，-1 -> 降序
        },
        "name": "_id_" # 索引的名称
    }
]
```



### 创建索引

语法：

```bash
$ db.collection.createIndex(keys, options)
```

| Parameter | Type     | Description                           |
| --------- | -------- | ------------------------------------- |
| keys      | document | 字段和值对，字段：1（升序）/2（降序） |
| options   | document | 包含一组创建索引的可选项文档          |

options（可选项列表）:

| Parameter                       | Type          | Description                                                  |
| :------------------------------ | :------------ | :----------------------------------------------------------- |
| background                      | Boolean       | 建索引过程会阻塞其它数据库操作，background可指定以后台方式创建索引，即增加 "background" 可选参数。 "background" 默认值为**false**。 |
| <font color="red">unique</font> | Boolean       | 建立的索引是否唯一。指定为true创建唯一索引。默认值为**false**. |
| <font color="red">name</font>   | string        | 索引的名称。如果未指定，MongoDB的通过连接索引的字段名和排序顺序生成一个索引名称。 |
| dropDups                        | Boolean       | **3.0+版本已废弃。**在建立唯一索引时是否删除重复记录,指定 true 创建唯一索引。默认值为 **false**. |
| sparse                          | Boolean       | 对文档中不存在的字段数据不启用索引；这个参数需要特别注意，如果设置为true的话，在索引字段中不会查询出不包含对应字段的文档.。默认值为 **false**. |
| expireAfterSeconds              | integer       | 指定一个以秒为单位的数值，完成 TTL设定，设定集合的生存时间。 |
| v                               | index version | 索引的版本号。默认的索引版本取决于mongod创建索引时运行的版本。 |
| weights                         | document      | 索引权重值，数值在 1 到 99,999 之间，表示该索引相对于其他索引字段的得分权重。 |
| default_language                | string        | 对于文本索引，该参数决定了停用词及词干和词器的规则的列表。 默认为英语 |
| language_override               | string        | 对于文本索引，该参数指定了包含在文档中的字段名，语言覆盖默认的language，默认值为 language. |



示例：

对userId创建索引：<font color="red">**单字段索引**</font>

```bash
$ db.comment.createIndex({userId:1})
[
    {
        "v": 2,
        "key": {
            "userId": 1
        },
        "name": "userId_1"
    }
]
```



对userId和nickName创建索引：<font color="red">**复合索引**</font>

```bash
$ db.comment.createIndex({userId:1, nickName:-1})
[
    {
        "v": 2,
        "key": {
            "userId": 1,
            "nickName": -1
        },
        "name": "userId_1_nickName_-1"
    }
]
```



### 删除索引

语法：

```bash
# index：索引名称/索引规范文档指定索引
$ db.collection.dropIndex(index)
```



示例：

删除comment集合中userId上的升序索引：

```bash
$ db.comment.dropIndex({userId:1})
{ "nIndexesWas" : 3, "ok" : 1 }
```



删除所有索引：

```bash
$ db.comment.dropIndexes()
```



## 文章评论案例

![image-20220716203327427](../../md-photo/image-20220716203327427.png)



文章示例参考：早晨空腹喝水，是对还是错？https://www.toutiao.com/a6721476546088927748/.

程序设计参见：[mongodb实现文章评论展示](https://blog.csdn.net/weixin_56219549/article/details/122195557)

### 一、需求分析

需要实现以下功能：
1）基本增删改查
2）根据文章id查询评论
3）评论点赞

### 二、表结构分析

存放文章评论的数据存放到MongoDB中，数据结构参考如下：
数据库: articledb
专栏文章评论: comment

| **字段名称**   | 字段含义       | **字段类型**     | **备注**                  |
| -------------- | -------------- | ---------------- | ------------------------- |
| _id            | ID             | ObjectId或String | Mongo的主键的字段         |
| articleid      | 文章ID         | String           |                           |
| content        | 评论内容       | String           |                           |
| userid         | 评论人ID       | String           |                           |
| nickname       | 评论人昵称     | String           |                           |
| createdatetime | 评论的日期时间 | Date             |                           |
| likenum        | 点赞数         | Int32            |                           |
| replynum       | 回复数         | Int32            |                           |
| state          | 状态           | String           | 0：不可见；1：可见；      |
| parentid       | 上级ID         | String           | 如果为0表示文章的顶级评论 |

### 三、项目搭建

参见：<font color="red">**SpringBoot整合mongodb**</font>



根据上级ID查询文章评论的分页列表：

<font color="red">**方法名必须按照mongodb的规范，其中findByX是查询的操作，ParentId必须是Java实体类中的字段，如果属性在java实体类中不存在，会报出： No property 'parentId11' found for type 'Comment'! Did you mean ''parentId''?**</font>

```java
// 通过父节点Id查询，并且进行分页操作
// 方法名必须按照mongodb的规范，其中find是查询的操作
Page<Comment> findByParentId(String parentId, Pageable pageable);
```



实现评论点赞的操作：<font color="red">**使用mongoTemplate进行操作**</font>

```java
mongoTemplate.updateFirst(
    new Query(Criteria.where("_id").is(id)), // 查询条件
    new Update().inc("likeNum", 1),// 更新条件，（默认添加1，但是方法返回为void -^-
    Comment.class
);
```



### 四、文章评论

参见：**gamll-web** | **spring-mongodb** | *<font color="bold">CommentController.java</font>*



## 副本集-Replica Sets

一主一副本一仲裁

![在这里插入图片描述](../../md-photo/f7850feeffa5406cb7c93d448c566070.png)

副本集名称：<font color="blue">**myrs**</font>

Primary：主 -> 27017

Secondary ：副本集 -> 27018

Arbtier：仲裁者 -> 27019



### 创建主节点

```bash
# 创建副本集数据/日志目录
$ mkdir /gjmou/software/mongodb/replica_sets

# 创建config文件
$ mkdir -p /gjmou/software/mongodb/replica_sets/config

# 创建主节点的数据/日志目录
$ mkdir -p /gjmou/software/mongodb/replica_sets/myrs_27017/data
$ mkdir -p /gjmou/software/mongodb/replica_sets/myrs_27017/log
# 创建日志文件
$ touch /gjmou/software/mongodb/replica_sets/myrs_27017/log/mongo.log

# 创建配置文件
$ touch /gjmou/software/mongodb/replica_sets/config/mongodb27017.conf
# 配置文件中录入以下内容
# 配置信息
port=27017 #端口
bind_ip=0.0.0.0 #默认是127.0.0.1
dbpath=/gjmou/software/mongodb/replica_sets/myrs_27017/data #数据库存放
logpath=/gjmou/software/mongodb/replica_sets/myrs_27017/log/mongo.log #日志文件
fork=true #设置后台运行
#auth=true #开启认证
# 指定副本集名称
replSet=myrs
```



### 创建副本节点

```bash
# 创建副本节点的数据/日志目录
$ mkdir -p /gjmou/software/mongodb/replica_sets/myrs_27018/data
$ mkdir -p /gjmou/software/mongodb/replica_sets/myrs_27018/log

# 创建日志文件
$ touch /gjmou/software/mongodb/replica_sets/myrs_27018/log/mongo.log

# 创建配置文件
$ touch /gjmou/software/mongodb/replica_sets/config/mongodb27018.conf

# 配置信息
port=27018 #端口
bind_ip=0.0.0.0 #默认是127.0.0.1
dbpath=/gjmou/software/mongodb/replica_sets/myrs_27018/data #数据库存放
logpath=/gjmou/software/mongodb/replica_sets/myrs_27018/log/mongo.log #日志文件
fork=true #设置后台运行
#auth=true #开启认证
# 指定副本集名称
replSet=myrs
```



### 创建仲裁者节点

```bash
# 创建副本节点的数据/日志目录
$ mkdir -p /gjmou/software/mongodb/replica_sets/myrs_27019/data
$ mkdir -p /gjmou/software/mongodb/replica_sets/myrs_27019/log

# 创建日志文件
$ touch /gjmou/software/mongodb/replica_sets/myrs_27019/log/mongo.log

# 创建配置文件
$ touch /gjmou/software/mongodb/replica_sets/config/mongodb27019.conf

# 配置信息
port=27019 #端口
bind_ip=0.0.0.0 #默认是127.0.0.1
dbpath=/gjmou/software/mongodb/replica_sets/myrs_27019/data #数据库存放
logpath=/gjmou/software/mongodb/replica_sets/myrs_27019/log/mongo.log #日志文件
fork=true #设置后台运行
#auth=true #开启认证
# 指定副本集名称
replSet=myrs
```



### 初始化副本集

#### 主节点

使用客户端命令连接主节点：

```bash
$ mongo --host localhost --port 27017
```

连接上之后很多命令都不可以用，必须初始化副本集才行

语法：

```bash
# 通常不需要参数的配置
$ rs.initiate(configuration)
```



初始化副本集：

```bash
$ rs.initiate()
{
    "info2": "no configuration specified. Using a default configuration for the set",
    "me": "mongodb1:27017",
    "ok": 1
}
$ myrs:OTHER
$ myrs:PRIMARY #初始化完成之后变成主节点

$ rs.conf() # 查看配置的相关信息
{
    "_id": "myrs",
    "version": 1,
    "term": 1,
# 当前只启动了主节点，所以才只有主节点
    "members": [
        {
            "_id": 0,
            "host": "mongodb1:27017",
            "arbiterOnly": false,
            "buildIndexes": true,
            "hidden": false,
            "priority": 1,
            "tags": {},
            "secondaryDelaySecs": NumberLong(0),
            "votes": 1
        }
    ],
    "protocolVersion": NumberLong(1),
    "writeConcernMajorityJournalDefault": true,
    "settings": {
        "chainingAllowed": true,
        "heartbeatIntervalMillis": 2000,
        "heartbeatTimeoutSecs": 10,
        "electionTimeoutMillis": 10000,
        "catchUpTimeoutMillis": -1,
        "catchUpTakeoverDelayMillis": 30000,
        "getLastErrorModes": {},
        "getLastErrorDefaults": {
            "w": 1,
            "wtimeout": 0
        },
        "replicaSetId": ObjectId("62d4115f0abafe96196bb2a2")
    }
}
```



#### 副本从节点

<font color="red">直接在主节点的shell命令中进行操作</font>

语法：

```bash
$ rs.add(host, arbiterOnly)
```

| Parameter   | Type               | Description                                                  |
| ----------- | ------------------ | ------------------------------------------------------------ |
| host        | string or document | 如果是一个字符串，需要指定新成员的主机名和可选端口号；如果是一个文档，需要指定在members数组中找到的副本集成员配置文档，必须在文档中指定主机字段；参见：*主机成员的配置文档* |
| arbiterOnly | boolean            | 仅在host为字符串时适用，如果为true，则添加的主机是仲裁者     |

主机成员的配置文档：

```json
{
    _id:<int>,
    host:<string>, // required
    arbiterOnly:<boolean>,
    buildIndexes:<boolean>,
    hidden:<boolean>,
    priority:<number>,
    tags:<document>,
    slaveDelay:<int>,
    votes:<number>
}
```



将27018副本节点添加到副本集中：

```bash
$ rs.add("192.168.239.71:27018")
{
    "ok": 1,
    "$clusterTime": {
        "clusterTime": Timestamp(1658066102, 1),
        "signature": {
            "hash": BinData(0, "AAAAAAAAAAAAAAAAAAAAAAAAAAA="), "keyId": NumberLong(0)
        }
    },
    "operationTime": Timestamp(1658066102, 1)
}
```



#### 仲裁节点

语法：

```bash
$ rs.addArb(host)
```

将27019的仲裁节点添加到副本集中：

```bash
$ rs.addArb("192.168.239.71:27019")
```



<font color="red">**如果仲裁节点没有反应，在主节点设置**</font>

```bash
$ db.adminCommand({
  "setDefaultRWConcern" : 1,
  "defaultWriteConcern" : {
    "w" : 2
  }
})
```



查看当前副本集状态：

```json
{
    "set": "myrs",
    "date": ISODate("2022-07-17T14:05:51.013Z"),
    "myState": 1,
    "term": NumberLong(1),
    "syncSourceHost": "",
    "syncSourceId": -1,
    "heartbeatIntervalMillis": NumberLong(2000),
    "majorityVoteCount": 2,
    "writeMajorityCount": 2,
    "votingMembersCount": 3,
    "writableVotingMembersCount": 2,
    "optimes": {
        "lastCommittedOpTime": {
            "ts": Timestamp(1658066746,1),
            "t": NumberLong(1)
        },
        "lastCommittedWallTime": ISODate("2022-07-17T14:05:46.262Z"),
        "readConcernMajorityOpTime": {
            "ts": Timestamp(1658066746,1),
            "t": NumberLong(1)
        },
        "appliedOpTime": {
            "ts": Timestamp(1658066746,1),
            "t": NumberLong(1)
        },
        "durableOpTime": {
            "ts": Timestamp(1658066746,1),
            "t": NumberLong(1)
        },
        "lastAppliedWallTime": ISODate("2022-07-17T14:05:46.262Z"),
        "lastDurableWallTime": ISODate("2022-07-17T14:05:46.262Z")
    },
    "lastStableRecoveryTimestamp": Timestamp(1658066746,1),
    "electionCandidateMetrics": {
        "lastElectionReason": "electionTimeout",
        "lastElectionDate": ISODate("2022-07-17T13:40:47.671Z"),
        "electionTerm": NumberLong(1),
        "lastCommittedOpTimeAtElection": {
            "ts": Timestamp(1658065247,1),
            "t": NumberLong(-1)
        },
        "lastSeenOpTimeAtElection": {
            "ts": Timestamp(1658065247,1),
            "t": NumberLong(-1)
        },
        "numVotesNeeded": 1,
        "priorityAtElection": 1,
        "electionTimeoutMillis": NumberLong(10000),
        "newTermStartDate": ISODate("2022-07-17T13:40:47.694Z"),
        "wMajorityWriteAvailabilityDate": ISODate("2022-07-17T13:40:47.704Z")
    },
    "members": [
        {
            "_id": 0,
            "name": "mongodb1:27017",
            "health": 1,
            "state": 1,
            "stateStr": "PRIMARY", #主节点
            "uptime": 1770,
            "optime": {
                "ts": Timestamp(1658066746,1),
                "t": NumberLong(1)
            },
            "optimeDate": ISODate("2022-07-17T14:05:46Z"),
            "lastAppliedWallTime": ISODate("2022-07-17T14:05:46.262Z"),
            "lastDurableWallTime": ISODate("2022-07-17T14:05:46.262Z"),
            "syncSourceHost": "",
            "syncSourceId": -1,
            "infoMessage": "",
            "electionTime": Timestamp(1658065247,2),
            "electionDate": ISODate("2022-07-17T13:40:47Z"),
            "configVersion": 7,
            "configTerm": 1,
            "self": true,
            "lastHeartbeatMessage": ""
        },
        {
            "_id": 1,
            "name": "192.168.239.71:27018",
            "health": 1,
            "state": 2,
            "stateStr": "SECONDARY", #从节点
            "uptime": 648,
            "optime": {
                "ts": Timestamp(1658066746,1),
                "t": NumberLong(1)
            },
            "optimeDurable": {
                "ts": Timestamp(1658066746,1),
                "t": NumberLong(1)
            },
            "optimeDate": ISODate("2022-07-17T14:05:46Z"),
            "optimeDurableDate": ISODate("2022-07-17T14:05:46Z"),
            "lastAppliedWallTime": ISODate("2022-07-17T14:05:46.262Z"),
            "lastDurableWallTime": ISODate("2022-07-17T14:05:46.262Z"),
            "lastHeartbeat": ISODate("2022-07-17T14:05:50.294Z"),
            "lastHeartbeatRecv": ISODate("2022-07-17T14:05:50.294Z"),
            "pingMs": NumberLong(0),
            "lastHeartbeatMessage": "",
            "syncSourceHost": "mongodb1:27017",
            "syncSourceId": 0,
            "infoMessage": "",
            "configVersion": 7,
            "configTerm": 1
        },
        {
            "_id": 2,
            "name": "192.168.239.71:27019",
            "health": 1,
            "state": 7,
            "stateStr": "ARBITER", # 仲裁者
            "uptime": 2,
            "lastHeartbeat": ISODate("2022-07-17T14:05:50.294Z"),
            "lastHeartbeatRecv": ISODate("2022-07-17T14:05:50.296Z"),
            "pingMs": NumberLong(1),
            "lastHeartbeatMessage": "",
            "syncSourceHost": "",
            "syncSourceId": -1,
            "infoMessage": "",
            "configVersion": 7,
            "configTerm": 1
        }
    ],
    "ok": 1,
    "$clusterTime": {
        "clusterTime": Timestamp(1658066746,1),
        "signature": {
            "hash": BinData(0,"AAAAAAAAAAAAAAAAAAAAAAAAAAA="),
            "keyId": NumberLong(0)
        }
    },
    "operationTime": Timestamp(1658066746,1)
}
```



#### 删除副本集节点

语法：

```bash
$ rs.remove(host)
```



### 副本集数据读写操作

主节点读写：

```bash
$ use articledb
$ db.comment.insert({"articleid":"10001","content":"boy next door", "userId":"1001","nickName":"Rose","createDateTime":new Date()})
WriteResult({ "nInserted" : 1 })

$ db.comment.find()
{ "_id" : ObjectId("62d419498e2e49050c2f1eb1"), "articleid" : "10001", "content" : "boy next door", "userId" : "1001", "nickName" : "Rose", "createDateTime" : ISODate("2022-07-17T14:14:33.234Z") }
```



副本从节点读写：

```bash
# 查看数据库直接报错
$ show dbs
uncaught exception: Error: listDatabases failed:
{
    "topologyVersion": {
        "processId": ObjectId("62d4105c83ec3490e35ad010"),
        "counter": NumberLong(8)
    },
    "ok": 0,
    "errmsg": "not master and slaveOk=false",
    "code": 13435,
    "codeName": "NotPrimaryNoSecondaryOk",
    "$clusterTime": {
        "clusterTime": Timestamp(1658067390,1),
        "signature": {
            "hash": BinData(0,"AAAAAAAAAAAAAAAAAAAAAAAAAAA="),
            "keyId": NumberLong(0)
        }
    },
    "operationTime": Timestamp(1658067390,1)
}

#设置为奴隶节点，允许从成员进行读的操作
$ rs.slaveOk()
# 新版本使用
$ rs.secondaryOk()
# 取消作为奴隶节点
$ rs.secondaryOk(false)

# 可以查询数据
$ use articledb
switched to db articledb
$ db.comment.find()
{ "_id" : ObjectId("62d419498e2e49050c2f1eb1"), "articleid" : "10001", "content" : "boy next door", "userId" : "1001", "nickName" : "Rose", "createDateTime" : ISODate("2022-07-17T14:14:33.234Z") }

$ db.comment.insert({"articleid":"10002","content":"yes,sir", "userId":"1002","nickName":"Jack","createDateTime":new Date()})
# 无法写入数据
WriteCommandError({
        "topologyVersion" : {
                "processId" : ObjectId("62d4105c83ec3490e35ad010"),
                "counter" : NumberLong(8)
        },
        "ok" : 0,
        "errmsg" : "not master",
        "code" : 10107,
        "codeName" : "NotWritablePrimary",
        "$clusterTime" : {
                "clusterTime" : Timestamp(1658067888, 1),
                "signature" : {
                        "hash" : BinData(0,"AAAAAAAAAAAAAAAAAAAAAAAAAAA="),
                        "keyId" : NumberLong(0)
                }
        },
        "operationTime" : Timestamp(1658067888, 1)
})
```



,<font color="red">**副本节点故障后重新启动会自动去读取主节点的数据**</font>



仲裁节点：

```bash
# 登录仲裁节点
$ mongo --port 27019
# 设置为奴隶节点
$ rs.secondaryOk()

# 无法进行读写的操作
$ show dbs
uncaught exception: Error: listDatabases failed:{
        "topologyVersion" : {
                "processId" : ObjectId("62d41c3438e2d71b50473563"),
                "counter" : NumberLong(1)
        },
        "ok" : 0,
        "errmsg" : "node is not in primary or recovering state",
        "code" : 13436,
        "codeName" : "NotPrimaryOrSecondary"
}
```



添加启动副本集的Shell脚本replica_sets.sh：

```bash
#!/bin/sh
# 启动主节点
`mongod --config /gjmou/software/mongodb/replica_sets/config/mongodb27017.conf`;

# 启动从节点
`mongod --config /gjmou/software/mongodb/replica_sets/config/mongodb27018.conf`;

# 启动仲裁者节点
`mongod --config /gjmou/software/mongodb/replica_sets/config/mongodb27019.conf`;
```



## 分片集群 Sharded Cluster

![img](../../md-photo/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L21pamljaHVpMjE1Mw==,size_16,color_FFFFFF,t_70.png)

**(1)Client**：最上面的小人就是客户端。对于客户端来说你数据库分不分片和我没关系，我只关心存取数据别的没什么好说的。

**(2)mongos**：即最上面的Router，mongos作为分片集群的入口所有的请求都由mongos来路由、分发、合并，这些动作对客户端驱动都是透明的。用户连接mongos就像是连接mongod一样使用。mongos通过缓存config server里面的元数据(metadata)确定每个分片有哪些数据，然后将读写请求分发到相应的某个或者某些shard。



搭建：(需要搭建11个mongo服务)

```yml
Web Server:
	# 路由节点
	Router1: 27117 
	Router2: 27217
	
	# 配置节点副本集
	Config Servers:
		Primary Config: 27119
		Secondary Config1: 27219
		Secondary Config2: 27319

	# 分片节点副本集
	Shard1:
		Primary Mongod1: 27118
		Secondary Mongod1: 27218
		Arbiter Mongod1: 27318
	Shard2:
		Primary Mongod2: 27418
		Secondary Mongod2: 27518
		Arbiter Mongod2: 27618
```



### 搭建分片副本集

<font color="red">**创建配置文件目录**</font>

```bash
$ mkdir -p /gjmou/software/mongodb/sharded_cluster/config
```



#### 第一套副本集

准备存放数据和日志的目录：

```bash
# 第一个分片集群 shardrs
# 主节点
$ mkdir -p /gjmou/software/mongodb/sharded_cluster/myshardrs01_27118/data
$ mkdir -p /gjmou/software/mongodb/sharded_cluster/myshardrs01_27118/log
# 创建配置文件
$ touch /gjmou/software/mongodb/sharded_cluster/config/myshardrs01_27118.config
# 录入以下内容
#配置mongodb数据库
systemLog:
  # 指定日志的输出格式为文件
  destination: file
  # 日志输出的目录
  path: "/gjmou/software/mongodb/sharded_cluster/myshardrs01_27118/log/mongod.log"
  # mongos或者mongod重启时，日志会追加
  logAppend: true
storage:
  # mongod实例存储其数据的目录
  dbPath: "/gjmou/software/mongodb/sharded_cluster/myshardrs01_27118/data"
  journal:
    # 启用或禁用持久性日志以确保数据文件保持有效和可恢复
    enabled: true
processManagement:
  # 启用在后台运行mongos或mongod进程
  fork: true
net:
  # 服务实例绑定的ip
  bindIp: 0.0.0.0 #默认是127.0.0.1
  # 绑定的端口
  port: 27118
replication:
  # 副本集名称
  replSetName: myshardrs01
sharding:
  # 分片角色
  clusterRole: shardsvr

# 从节点
$ mkdir -p /gjmou/software/mongodb/sharded_cluster/myshardrs01_27218/data
$ mkdir -p /gjmou/software/mongodb/sharded_cluster/myshardrs01_27218/log
# 创建配置文件
$ touch /gjmou/software/mongodb/sharded_cluster/config/myshardrs01_27218.config
# 修改上面的日志、数据目录以及端口号配置即可

# 仲裁者节点
$ mkdir -p /gjmou/software/mongodb/sharded_cluster/myshardrs01_27318/data
$ mkdir -p /gjmou/software/mongodb/sharded_cluster/myshardrs01_27318/log
# 创建配置文件
$ touch /gjmou/software/mongodb/sharded_cluster/config/myshardrs01_27318.config
# 修改上面的日志、数据目录以及端口号配置即可
```



启动相应的服务：

```bash
$ mongod --config XXX.config
```



#### 第二套副本集

```bash
# 第二个分片集群
# 主节点
$ mkdir -p /gjmou/software/mongodb/sharded_cluster/myshardrs02_27418/data
$ mkdir -p /gjmou/software/mongodb/sharded_cluster/myshardrs02_27418/log
# 创建配置文件
$ touch /gjmou/software/mongodb/sharded_cluster/config/myshardrs02_27418.config
# 录入以下内容
#配置mongodb数据库
systemLog:
  # 指定日志的输出格式为文件
  destination: file
  # 日志输出的目录
  path: "/gjmou/software/mongodb/sharded_cluster/myshardrs02_27418/log/mongod.log"
  # mongos或者mongod重启时，日志会追加
  logAppend: true
storage:
  # mongod实例存储其数据的目录
  dbPath: "/gjmou/software/mongodb/sharded_cluster/myshardrs02_27418/data"
  journal:
    # 启用或禁用持久性日志以确保数据文件保持有效和可恢复
    enabled: true
processManagement:
  # 启用在后台运行mongos或mongod进程
  fork: true
net:
  # 服务实例绑定的ip
  bindIp: 0.0.0.0 #默认是127.0.0.1
  # 绑定的端口
  port: 27418
replication:
  # 副本集名称
  replSetName: myshardrs02
sharding:
  # 分片角色
  clusterRole: shardsvr

# 从节点
$ mkdir -p /gjmou/software/mongodb/sharded_cluster/myshardrs02_27518/data
$ mkdir -p /gjmou/software/mongodb/sharded_cluster/myshardrs02_27518/log
# 创建配置文件
$ touch /gjmou/software/mongodb/sharded_cluster/config/myshardrs02_27518.config
# 修改上面的日志、数据目录以及端口号配置即可

# 仲裁者节点
$ mkdir -p /gjmou/software/mongodb/sharded_cluster/myshardrs02_27618/data
$ mkdir -p /gjmou/software/mongodb/sharded_cluster/myshardrs02_27618/log
# 创建配置文件
$ touch /gjmou/software/mongodb/sharded_cluster/config/myshardrs02_27618.config
# 修改上面的日志、数据目录以及端口号配置即可
```



### 搭建配置节点副本集

```bash
# 配置节点集群 configrs
# 主节点
$ mkdir -p /gjmou/software/mongodb/sharded_cluster/myconfigrs_27119/log
$ mkdir -p /gjmou/software/mongodb/sharded_cluster/myconfigrs_27119/data
# 创建配置文件
$ touch /gjmou/software/mongodb/sharded_cluster/config/myconfigrs_27119.config
# 录入以下内容
#配置mongodb数据库
systemLog:
  # 指定日志的输出格式为文件
  destination: file
  # 日志输出的目录
  path: "/gjmou/software/mongodb/sharded_cluster/myconfigrs_27119/log/mongod.log"
  # mongos或者mongod重启时，日志会追加
  logAppend: true
storage:
  # mongod实例存储其数据的目录
  dbPath: "/gjmou/software/mongodb/sharded_cluster/myconfigrs_27119/data"
  journal:
    # 启用或禁用持久性日志以确保数据文件保持有效和可恢复
    enabled: true
processManagement:
  # 启用在后台运行mongos或mongod进程
  fork: true
net:
  # 服务实例绑定的ip
  bindIp: 0.0.0.0 #默认是127.0.0.1
  # 绑定的端口
  port: 27119
replication:
  # 副本集名称
  replSetName: myconfigrs
sharding:
  # 配置角色
  clusterRole: configsvr

# 从节点
$ mkdir -p /gjmou/software/mongodb/sharded_cluster/myconfigrs_27219/log
$ mkdir -p /gjmou/software/mongodb/sharded_cluster/myconfigrs_27219/data
# 创建配置文件
$ touch /gjmou/software/mongodb/sharded_cluster/config/myconfigrs_27219.config
# 修改上面的日志、数据目录以及端口号配置即可

# 从节点
$ mkdir -p /gjmou/software/mongodb/sharded_cluster/myconfigrs_27319/log
$ mkdir -p /gjmou/software/mongodb/sharded_cluster/myconfigrs_27319/data
# 创建配置文件
$ touch /gjmou/software/mongodb/sharded_cluster/config/myconfigrs_27319.config
# 修改上面的日志、数据目录以及端口号配置即可
```

启动所有的实例，参见**sharded_cluster.sh**脚本

初始化副本集的操作

```bash
# 连接分片1的主节点
$ mongo --port 27118
# 执行初始化命令
$ rs.initiate()
# 添加从节点到副本集
$ rs.add("192.168.239.71:27218")
# 添加仲裁节点到副本集
$ rs.addArb("192.168.239.71:27318")

# 连接分片2的主节点
$ mongo --port 27418
# 执行初始化命令
$ rs.initiate()
# 添加从节点到副本集
$ rs.add("192.168.239.71:27518")
# 添加仲裁节点到副本集
$ rs.addArb("192.168.239.71:27618")

# 连接配置节点的主节点
$ mongo --port 27119
# 执行初始化命令
$ rs.initiate()
# 添加从节点1到副本集
$ rs.add("192.168.239.71:27219")
# 添加从节点2到副本集
$ rs.add("192.168.239.71:27319")
```



### 搭建路由

#### 创建路由节点

```bash
# 添加27117路由节点
# 创建路由节点日志存放文件夹（路由节点不需要data目录）
$ mkdir -p /gjmou/software/mongodb/sharded_cluster/mymongos_27117/log

# 创建路由配置文件
$ touch /gjmou/software/mongodb/sharded_cluster/config/mymongos_27117.config
# 录入以下内容
#配置mongodb数据库
systemLog:
  # 指定日志的输出格式为文件
  destination: file
  # 日志输出的目录
  path: "/gjmou/software/mongodb/sharded_cluster/mymongos_27117/log/mongod.log"
  # mongos或者mongod重启时，日志会追加
  logAppend: true
processManagement:
  # 启用在后台运行mongos或mongod进程
  fork: true
  # 指定用于保存mongos或mongod进程的进程id的文件位置，其中mongos或mongod将写入其pid
  pidFilePath: "/gjmou/software/mongodb/sharded_cluster/mymongos_27117/log/mongos.pid"
net:
  # 服务实例绑定的ip
  bindIp: 0.0.0.0 #默认是127.0.0.1
  # 绑定的端口
  port: 27117
sharding:
  # 指定配置节点副本集
  configDB: myconfigrs/192.168.239.71:27119,192.168.239.71:27219,192.168.239.71:27319
  

# 启动mongos
$ mongos --config mymongos_27117.config 
# 连接mongos客户端
$ mongo --port 27117
 
$ mongos> use aabb # 可以创建数据库
switched to db aabb

# 无法写入数据（无法找到存数据的分片，需要让路由知道分片副本集的位置）
$ mongos> db.aabb.insert({"boy":"next door"})
WriteCommandError({
        "ok" : 0,
        "errmsg" : "Database aabb could not be created :: caused by :: No shards found",
        "code" : 70,
        "codeName" : "ShardNotFound",
        "$clusterTime" : {
                "clusterTime" : Timestamp(1658590984, 1),
                "signature" : {
                        "hash" : BinData(0,"AAAAAAAAAAAAAAAAAAAAAAAAAAA="),
                        "keyId" : NumberLong(0)
                }
        },
        "operationTime" : Timestamp(1658590984, 1)
})


# 添加27217路由节点
# 创建路由节点日志存放文件夹（路由节点不需要data目录）
$ mkdir -p /gjmou/software/mongodb/sharded_cluster/mymongos_27217/log

# 创建路由配置文件
$ touch /gjmou/software/mongodb/sharded_cluster/config/mymongos_27217.config

# 配置跟第一个路由基本一致，只需要将27117改为27217即可
# 启动mongos
$ mongos --config mymongos_27217.config 
# 连接mongos客户端
$ mongo --port 27217

# 以下的分片副本集只需要在一个路由上配置即可
```



#### 添加分片副本集

```bash
$ mongos> sh.addShard("IP:PORT")
```



将分片副本集添加到路由中：

```bash
$ mongos> 
# 将分片1副本集添加到路由中
sh.addShard("myshardrs01/mongodb1:27118,192.168.239.71:27218,192.168.239.71:27318")
{
        "shardAdded" : "myshardrs01",
        "ok" : 1,
        "$clusterTime" : {
                "clusterTime" : Timestamp(1658591584, 1),
                "signature" : {
                        "hash" : BinData(0,"AAAAAAAAAAAAAAAAAAAAAAAAAAA="),
                        "keyId" : NumberLong(0)
                }
        },
        "operationTime" : Timestamp(1658591584, 1)
}


# 将分片2副本集添加到路由中
$ mongos> sh.addShard("myshardrs02/mongodb1:27418,192.168.239.71:27518,192.168.239.71:27618")
{
        "shardAdded" : "myshardrs02",
        "ok" : 1,
        "$clusterTime" : {
                "clusterTime" : Timestamp(1658591992, 4),
                "signature" : {
                        "hash" : BinData(0,"AAAAAAAAAAAAAAAAAAAAAAAAAAA="),
                        "keyId" : NumberLong(0)
                }
        },
        "operationTime" : Timestamp(1658591992, 4)
}

# 查看路由状态
$ mongos> sh.status()
--- Sharding Status --- 
  sharding version: {
        "_id" : 1,
        "minCompatibleVersion" : 5,
        "currentVersion" : 6,
        "clusterId" : ObjectId("62dc0973fa5c61179a18c022")
  }
  
# 两个分片副本集已经加入路由成功！
  shards:
        {  "_id" : "myshardrs01",  "host" : "myshardrs01/192.168.239.71:27218,mongodb1:27118",  "state" : 1,  "topologyTime" : Timestamp(1658591523, 2) }
        {  "_id" : "myshardrs02",  "host" : "myshardrs02/192.168.239.71:27518,mongodb1:27418",  "state" : 1,  "topologyTime" : Timestamp(1658591992, 2) }
  active mongoses:
        "5.0.9" : 1
  autosplit:
        Currently enabled: yes
  balancer:
        Currently enabled: yes
        Currently running: no
        Failed balancer rounds in last 5 attempts: 0
        Migration results for the last 24 hours: 
                5 : Success
  databases:
        {  "_id" : "config",  "primary" : "config",  "partitioned" : true }
                config.system.sessions
                        shard key: { "_id" : 1 }
                        unique: false
                        balancing: true
                        chunks:
                                myshardrs01     1019
                                myshardrs02     5
                        too many chunks to print, use verbose if you want to force print
                        
```



**Tips：如果添加分片失败，需要先手动移除分片，添加检查分片的信息正确性后，再次添加分片，移除分片的命令：**

```bash
$ mongos> use admin
$ mongos> db.runCommand({removeShard: "myshardrs02"})

# 当提示信息为以下情况时，需要将test数据库移动到其他分片才可以继续进行删除分片的操作
$ mongos> db.runCommand({removeShard: "myshardrs02"})
{
        "msg" : "draining ongoing",
        "state" : "ongoing",
        "remaining" : {
                "chunks" : NumberLong(0),
                "dbs" : NumberLong(1),
                "jumboChunks" : NumberLong(0)
        },
        "note" : "you need to drop or movePrimary these databases",
        "dbsToMove" : [
                "test"
        ],
        "ok" : 1,
        "$clusterTime" : {
                "clusterTime" : Timestamp(1658596370, 2),
                "signature" : {
                        "hash" : BinData(0,"AAAAAAAAAAAAAAAAAAAAAAAAAAA="),
                        "keyId" : NumberLong(0)
                }
        },
        "operationTime" : Timestamp(1658596370, 2)
}

# 将test数据库移动到myshardrs01分片中
$ db.runCommand( { movePrimary: "test", to: "myshardrs01" })

# 继续执行移除数据库的命令
$ db.runCommand({removeShard: "myshardrs02"})
# 移除数据库成功
{
        "msg" : "removeshard completed successfully",
        "state" : "completed",
        "shard" : "myshardrs02",
        "ok" : 1,
        "$clusterTime" : {
                "clusterTime" : Timestamp(1658596626, 3),
                "signature" : {
                        "hash" : BinData(0,"AAAAAAAAAAAAAAAAAAAAAAAAAAA="),
                        "keyId" : NumberLong(0)
                }
        },
        "operationTime" : Timestamp(1658596626, 3)
}
```

注意：如果只剩下最后一个shard，无法进行删除，移除时会自动转移分片数据，需要一个时间的过程，完成后，再次执行删除分片的命令才能真正的删除



#### 开启分片功能

命令：

```bash
# 开启分片功能
# 对数据库开启分片功能
$ sh.enableSharding("库名")
# 对集合开启分片功能
$ sh.shardCollection("库名.集合名",{"key":1})
```



对集合开启分片功能的命令：

```bash
$ sh.shardCollection(namespace, key, unique)
```



| Parameter | Type     | Description                                                  |
| --------- | -------- | ------------------------------------------------------------ |
| namespace | string   | 要（分片）共享的目标集合的命名空间，格式`<database>.<collection>` |
| key       | document | 用作分片键的索引规范文档。规定使用哪个字段进行分片，分片规则包含：`哈希策略`和`值范围策略` |
| unique    | boolean  | 当值为true的情况下，片段字段上回限制为确保是唯一索引。       |



在mongos上的articledb数据库配置sharding：

```bash
$ mongos> sh.enableSharding("articledb")
{
        "ok" : 1,
        "$clusterTime" : {
                "clusterTime" : Timestamp(1658597161, 16),
                "signature" : {
                        "hash" : BinData(0,"AAAAAAAAAAAAAAAAAAAAAAAAAAA="),
                        "keyId" : NumberLong(0)
                }
        },
        "operationTime" : Timestamp(1658597161, 1)
}

# 使用nickName作为片键，根据其哈希值进行数据分片
$ mongos> sh.shardCollection("articledb.comment", {"nickName":"hashed"})
{
        "ok" : 0,
        "errmsg" : "database articledb not found",
        "code" : 26,
        "codeName" : "NamespaceNotFound",
        "$clusterTime" : {
                "clusterTime" : Timestamp(1658597121, 25),
                "signature" : {
                        "hash" : BinData(0,"AAAAAAAAAAAAAAAAAAAAAAAAAAA="),
                        "keyId" : NumberLong(0)
                }
        },
        "operationTime" : Timestamp(1658597121, 25)
}

# 使用sh.status()命令可以看到
				# 对articledb数据库的comment集合进行分片
                articledb.comment
                		# 片键是nickName，规则是hashed
                        shard key: { "nickName" : "hashed" }
                        unique: false
                        balancing: true
                        chunks:
                                myshardrs01     2
                                myshardrs02     2
                        # 自动根据nickName的哈希值，放到不同的分片中
                        { "nickName" : { "$minKey" : 1 } } -->> { "nickName" : NumberLong("-4611686018427387902") } on : myshardrs01 Timestamp(1, 0) 
                        { "nickName" : NumberLong("-4611686018427387902") } -->> { "nickName" : NumberLong(0) } on : myshardrs01 Timestamp(1, 1) 
                        { "nickName" : NumberLong(0) } -->> { "nickName" : NumberLong("4611686018427387902") } on : myshardrs02 Timestamp(1, 2) 
                        { "nickName" : NumberLong("4611686018427387902") } -->> { "nickName" : { "$maxKey" : 1 } } on : myshardrs02 Timestamp(1, 3) 
                        

# 使用age作为片键，根据其数值范围进行数据分片
# 注意一个集合只能通过一个字段进行分片，所以这里使用author集合进行分片
$ mongos> sh.shardCollection("articledb.author", {"age":1})
# 使用sh.status()命令可以看到
				# 对articledb数据库的author集合进行分片
				articledb.author
						# 片键是age，规则是范围值
                        shard key: { "age" : 1 }
                        unique: false
                        balancing: true
                        # 不会自动进行分片，需要根据插入的值不同执行分片，目前默认放在myshardrs02中，根据集合的实际录入数值再进行相应的分片
                        chunks:
                                myshardrs02     1
                        { "age" : { "$minKey" : 1 } } -->> { "age" : { "$maxKey" : 1 } } on : myshardrs02 Timestamp(1, 0) 
```



#### 哈希策略演示

特点：<font color='red'>**数据量越大越均匀**</font>

```bash
$ mongos> use articledb
switched to db articledb

$ mongos> for (var i=1; i<=1000; i++) {db.comment.insert({"_id":i+"","nickName":"BoBo"+i})}

# 数据总量1000条
$ mongos> db.comment.count()
1000

# 分片1中存入的数量
$ myshardrs01:PRIMARY> db.comment.count()
507
# 分片二中存入的数量
$ myshardrs02:PRIMARY> db.comment.count()
493
```



**Tips：for循环时js的写法，因为mongo的shell是一个JavaScript的shell**

**Tips：从路由上插入的数据，必须包含片键（例子中是nickName），否则无法插入**



#### 数值策略演示

特点：<font color='red'>**根据数据的范围放到不同的分片，结构不均匀**</font>

数据块（chunk）默认寸为64M，**填满后才会考虑向其他片的数据块中填充数据**，因此为了测试，将数据块大小改为1M

```bash
# 将数据块大小改为1M
$ mongos> use config
switched to db config
$ mongos> db.settings.save({_id:"chunksize", value:1})
WriteResult({ "nMatched" : 1, "nUpserted" : 0, "nModified" : 0 })

# 插入数据
$ mongos> use articledb
$ mongos> for (var i=1; i<=20000;i++){db.author.save({"name":"BoBoBoBoBoBoBoBoBoBoBoBoBoBoBoBoBoBoBoBoBoBoBoBoBoBoBoBoBoBoBoBoBoBoBoBoBoBo"+i, "age":NumberInt(i%120)})}

# 查看插入记录的条数
$ mongos> db.author.count();
20000

# 分片1数据量
$ myshardrs01:PRIMARY> db.author.count();
166

# 分片2数据量
$ myshardrs02:PRIMARY> db.author.count();
19834

# 其中分片1的年龄大多为119，分片2的年龄大多为0 - 118

# 测试结束后将数据块大小改64M
$ mongos> use config
$ mongos> db.settings.save({_id:"chunksize", value:64})
WriteResult({ "nMatched" : 1, "nUpserted" : 0, "nModified" : 1 })
```



#### compass连接集群

防火墙添加开放路由端口：

```bash
# 27117
$ firewall-cmd --zone=public --add-port=27117/tcp --permanent
# 27217
$ firewall-cmd --zone=public --add-port=27217/tcp --permanent
# 刷新防火墙
$ sudo firewall-cmd --reload
```

compass工具连接路由端口就行：

![image-20220724204541663](../../md-photo/image-20220724204541663.png)

**不管连哪个路由都是一样的效果**



#### springboot连接集群

只需要修改yml的uri配置即可：

```yml
spring:
  application:
    name: spring-mongodb
  data:
    mongodb:
      # 集群语法
      uri: mongodb://mongodb1:27117,mongodb1:27217/articledb
```



<font color='red'>**Tips：建立分片键最好使用_id主键，否则mongoTemplate会报错：WriteError{code=61, message='Failed to target upsert by query :: could not extract exact shard key', details={}}.**</font>



编写启动分片集群中启动所有实例的shell脚本：sharded_cluster.sh

```bash
#!/bin/sh
# 启动配置节点
# 主节点
`mongod --config /gjmou/software/mongodb/sharded_cluster/config/myconfigrs_27119.config`;

# 从节点
`mongod --config /gjmou/software/mongodb/sharded_cluster/config/myconfigrs_27219.config`;

# 从节点
`mongod --config /gjmou/software/mongodb/sharded_cluster/config/myconfigrs_27319.config`;

wait;

# 启动分片1
# 主节点
`mongod --config /gjmou/software/mongodb/sharded_cluster/config/myshardrs01_27118.config`;

# 从节点
`mongod --config /gjmou/software/mongodb/sharded_cluster/config/myshardrs01_27218.config`;

# 仲裁节点
`mongod --config /gjmou/software/mongodb/sharded_cluster/config/myshardrs01_27318.config`;

# 启动分片2
# 主节点
`mongod --config /gjmou/software/mongodb/sharded_cluster/config/myshardrs02_27418.config`;

# 从节点
`mongod --config /gjmou/software/mongodb/sharded_cluster/config/myshardrs02_27518.config`;

# 仲裁节点
`mongod --config /gjmou/software/mongodb/sharded_cluster/config/myshardrs02_27618.config`;

wait;

# 启动路由
# 路由一
`mongos --config /gjmou/software/mongodb/sharded_cluster/config/mymongos_27117.config`;

# 路由二
`mongos --config /gjmou/software/mongodb/sharded_cluster/config/mymongos_27217.config`;
```



## 安全认证

#### 用户权限

角色说明：

| 角色                 | 权限描述                                                     |
| -------------------- | ------------------------------------------------------------ |
| read                 | 允许用户读取指定数据库                                       |
| readWrite            | 允许用户读写指定数据库                                       |
| dbAdmin              | 允许用户在指定数据库中执行管理函数，如索引创建、删除，查看统计或访问system.profile |
| userAdmin            | 允许用户向system.users集合写入，可以找指定数据库里创建、删除和管理用户 |
| clusterAdmin         | 只在admin数据库中可用，赋予用户所有分片和复制集相关函数的管理权限。 |
| readAnyDatabase      | 只在admin数据库中可用，赋予用户所有数据库的读权限            |
| readWriteAnyDatabase | 只在admin数据库中可用，赋予用户所有数据库的读写权限          |
| userAdminAnyDatabase | 只在admin数据库中可用，赋予用户所有数据库的userAdmin权限     |
| dbAdminAnyDatabase   | 只在admin数据库中可用，赋予用户所有数据库的dbAdmin权限。     |
| root                 | 只在admin数据库中可用。超级账号，超级权限                    |



#### 单实例环境

##### 创建用户及角色

单实例服务启动：

```bash
# 进入到桌面，启动单实例
$ cd ~
$ mongod --config mongodb.conf
# 进入客户端
$ mongo

# 切换到admin数据库
> use admin
switched to db admin

# 创建系统超级用户 myroot,设置密码123456，设置角色root
# 其中"root"是角色，其中："db":"admin"是针对哪个数据库
# db.createUser({user:"myroot", pwd:"123456",roles:[{"role": root","db":"admin"}]})
# 如果当前就在admin数据库下，并且只赋予用户一个角色，可以用以下的简写：
> db.createUser({user:"myroot", pwd:"123456", roles:["root"]})
Successfully added user: { "user" : "myroot", "roles" : [ "root" ] }

# 创建专门用来管理admin库的账号myadmin，只用来作为用户权限管理
> db.createUser({user:"myadmin",pwd:"123456",roles:[{role:"userAdminAnyDatabase","db":"admin"}]})
Successfully added user: {
        "user" : "myadmin",
        "roles" : [
                {
                        "role" : "userAdminAnyDatabase",
                        "db" : "admin"
                }
        ]
}

# 查看已经创建的用户情况
> db.system.users.find()
# myroot
{
    "_id": "admin.myroot",
    "userId": UUID("4d0abe23-10b1-4421-a658-dec6cbb4b145"),
    "user": "myroot",
    "db": "admin",
    "credentials": {
        "SCRAM-SHA-1": {
            "iterationCount": 10000,
            "salt": "d7/gmez5UnZ87iFm5Vf5nQ==",
            "storedKey": "slhY4ugNaSKVWuNnJ4XiLXB0jdo=",
            "serverKey": "jaO5oLw8RwxxtUgpa7rLeswuqqg="
        },
        "SCRAM-SHA-256": {
            "iterationCount": 15000,
            "salt": "X0BzAAMjuoeUn8QgHYAG0AFb2iLLI8zWmnhY8w==",
            "storedKey": "7C4fhrGRXzveBW7WPeAQMg+Vt7aFHtPIZndq1cq+iWQ=",
            "serverKey": "oxEhd6armtn7W/yz5nORB30rUoaJdV5A4lYjrY6FImY="
        }
    },
    "roles": [
        {
            "role": "root",
            "db": "admin"
        }
    ]
}

# myadmin
{
    "_id": "admin.myadmin",
    "userId": UUID("53289c5d-cf1d-4a17-ad00-4be7481e662a"),
    "user": "myadmin",
    "db": "admin",
    "credentials": {
        "SCRAM-SHA-1": {
            "iterationCount": 10000,
            "salt": "t8u5znCRUltuJ6KBMUU1dw==",
            "storedKey": "GvO+eu4di0oIRVI6jqp9qw/SvPU=",
            "serverKey": "r+1IUmXttPpkpKDgdRkE/t3vOxo="
        },
        "SCRAM-SHA-256": {
            "iterationCount": 15000,
            "salt": "SNaNkWiZTemisazkCooGbysaFCqU6540BJgxNA==",
            "storedKey": "msgtp0o1hSvj+Kr++6nX3PEEas4HnW//FqaKOWd2C0c=",
            "serverKey": "xybfVIyVaRUqOAsC/gEBUoOEj6hvpqBJPZ9dvGdC/R0="
        }
    },
    "roles": [
        {
            "role": "userAdminAnyDatabase",
            "db": "admin"
        }
    ]
}

# 删除用户
> db.dropUser("myadmin")

# 修改密码
> db.changeUserPassword("myroot", "123456")
```

<font color='red'>**Tips：如果不指定用户，那么创建的指定用户权限在所有数据库上有效**</font>



##### 认证测试

```bash
# 切换到admin数据库
> user admin
# 密码输错
> db.auth("myroot", "12345")
Error: Authentication failed.
0
# 密码正确
> db.auth("myroot", "123456")
1
```



创建普通的用户：

```bash
# 切换数据库
> use articledb
switched to db articledb

# 创建用户，读写权限readWrite，密码是123456
> db.createUser({user:"bobo", pwd:"123456",roles:[{role:"readWrite", db:"articledb"}]})
Successfully added user: {
    "user": "bobo",
    "roles": [
        {
            "role": "readWrite",
            "db": "articledb"
        }
    ]
}

# 登录bobo
> db.auth("bobo", "123456")

```



##### 服务端开启认证

```bash
# 编辑mongo配置
$ vi ~/mongodb.conf 
# 追加认证的配置
auth=true #开启认证
# yml配置
security:
	# 开启授权认证
	authorization: enabled
	
# 登录客户端shell
$ mongo
# 需要切换到对应的数据库下才能使用对应的登录命令
> use admin
switched to db admin
> db.auth("myroot", "123456")
1

# 登录之后才可以对数据库进行操作
> show dbs
admin   0.000GB
config  0.000GB
local   0.000GB
runoob  0.000GB
spring  0.000GB

> use articledb
switched to db articledb

> db.auth("bobo", "123456")
1
# 插入一条数据
> db.comment.insert({"nickName":"老周"})
WriteResult({ "nInserted" : 1 })

# bobo只能看到articledb，因为其只有articledb的权限
> show dbs
articledb  0.000GB
```

<font color='red'>**出现问题：uncaught exception: Error: Failed to acquire database information from privileges:xxx，使用一个用户认证后又使用另一个用户进行认证。解决办法：退出之后重新连接shell脚本**</font>



##### Spring认证芒果

如果不使用密码的方式连接，会抛出错误：

{"ok": 0.0, "errmsg": "command update requires authentication", "code": 13, "codeName": "Unauthorized"}

yml配置以下即可：

```yml
spring:
  application:
    name: spring-mongodb
  data:
    mongodb:
      # 无密码连接
      # uri: mongodb://mongodb1:27017/spring

      # 有密码连接
      uri: mongodb://bobo:123456@mongodb1:27017/articledb
```



##### compass认证芒果

![image-20220724233650119](../../md-photo/image-20220724233650119.png)





#### 副本集环境

##### 启动副本集

```bash
$ sh ~/shell-script/replica_sets.sh
```



##### 创建管理员用户

```bash
# 登录主节点
$ mongo --port 27017

# 切换到admin数据库
$ myrs:PRIMARY> use admin
switched to db admin

# 创建超级管理员账户
$ myrs:PRIMARY> db.createUser({user:"myroot",pwd:"123456",roles:["root"]})
Successfully added user: { "user" : "myroot", "roles" : [ "root" ] }
```



##### 创建普通用户

```bash
# 当前需要在myroot用户上
# 为articledb数据库创建用户
$ myrs:PRIMARY> use articledb
switched to db articledb

# 添加普通用户 （如果不想在创建用户的命令中指定数据库，那么需要切换到对应数据库再创建用户）
$ myrs:PRIMARY> db.createUser({user:"bobo", pwd:"123456", roles:["readWrite"]})
Successfully added user: { "user" : "bobo", "roles" : [ "readWrite" ] }
```



##### 生成key文件

<font color='red'>**一台机器生成key文件，然后分别复制到不同mongo实例中，所有实例公用一个key文件**</font>

<font color='red'>**副本集主节点中创建的用户会自动同步到其他节点中**</font>

生成key文件用户副本集的内部连接使用：

```bash
# 使用openssl生成密码文件（90位的加密文件）
$ openssl rand -base64 90 -out ./mongo.keyfile

# 降低keyfile的权限，只对当前用户只读
$ chmod 400 ./mongo.keyfile

# 将keyfile复制到不同实例下
$ cp mongo.keyfile /gjmou/software/mongodb/replica_sets/myrs_27017/
$ cp mongo.keyfile /gjmou/software/mongodb/replica_sets/myrs_27018/
$ cp mongo.keyfile /gjmou/software/mongodb/replica_sets/myrs_27019/
# 删除key文件
$ rm -f mongo.keyfile
```



##### 配置文件指定key文件

```bash
# 主节点添加配置
$ vi /gjmou/software/mongodb/replica_sets/config/mongodb27017.conf
# 添加以下内容
auth=true #开启认证
# 指定keyfile文件位置
keyFile=/gjmou/software/mongodb/replica_sets/myrs_27017/mongo.keyfile

# 从节点添加配置
# 将27017修改为27018

# 仲裁者节点添加配置
# 将27017修改为27019
```



##### 认证测试

```bash
# 连接主节点
$ mongo --port 27017
$ myrs:PRIMARY> use admin
switched to db admin
# myroot管理员用户登录测试
$ myrs:PRIMARY> db.auth("myroot","123456")
1

# 可以开始对数据库进行操作
$ myrs:PRIMARY> show dbs
admin      0.000GB
articledb  0.000GB
config     0.000GB
local      0.000GB

# bobo普通用户登录测试
$ myrs:PRIMARY> use articledb
switched to db articledb

# 无法直接查看数据库
$ myrs:PRIMARY> show dbs

# 执行登录操作
$ myrs:PRIMARY> db.auth("bobo","123456")
1

# 可以正常查看数据库
$ myrs:PRIMARY> show dbs
articledb  0.000GB
```



##### Spring认证芒果

yml配置以下即可：

```yml
spring:
  application:
    name: spring-mongodb
  data:
    mongodb:
      # 有密码连接
      uri: mongodb://bobo:123456@mongodb1:27017,mongodb1:27018,mongodb1:27019/articledb?connect=replicaSet&slaveOk=true&relicaSet=myrs
```



##### compass认证芒果

![image-20220725232641476](../../md-photo/image-20220725232641476.png)



#### 集群环境

##### 生成key文件

```bash
# 使用openssl生成密码文件（90位的加密文件）
$ openssl rand -base64 90 -out ./mongo.keyfile

# 降低keyfile的权限，只对当前用户只读
$ chmod 400 ./mongo.keyfile

# 将keyfile复制到不同实例下
# 配置实例
$ cp mongo.keyfile /gjmou/software/mongodb/sharded_cluster/myconfigrs_27119
$ cp mongo.keyfile /gjmou/software/mongodb/sharded_cluster/myconfigrs_27219
$ cp mongo.keyfile /gjmou/software/mongodb/sharded_cluster/myconfigrs_27319

# 路由实例
$ cp mongo.keyfile /gjmou/software/mongodb/sharded_cluster/mymongos_27117
$ cp mongo.keyfile /gjmou/software/mongodb/sharded_cluster/mymongos_27217

# 分片1
$ cp mongo.keyfile /gjmou/software/mongodb/sharded_cluster/myshardrs01_27118
$ cp mongo.keyfile /gjmou/software/mongodb/sharded_cluster/myshardrs01_27218
$ cp mongo.keyfile /gjmou/software/mongodb/sharded_cluster/myshardrs01_27318

# 分片2
$ cp mongo.keyfile /gjmou/software/mongodb/sharded_cluster/myshardrs02_27418
$ cp mongo.keyfile /gjmou/software/mongodb/sharded_cluster/myshardrs02_27518
$ cp mongo.keyfile /gjmou/software/mongodb/sharded_cluster/myshardrs02_27618

# 删除key文件
$ rm -f mongo.keyfile
```



##### 配置文件指定key文件

```bash
# 在所有的实例配置文件追加以下内容
$ vi /gjmou/software/mongodb/sharded_cluster/config/myconfigrs_27119.config
security:
	# 开启授权认证
	authorization: enabled
	# keyFile鉴权文件
	keyFile: /gjmou/software/mongodb/sharded_cluster/myconfigrs_27119/mongo.keyfile
	
# 其他的实例只需要修改keyFile路径即可
# 注意：路由mongos不需要authorization: enabled配置
```



##### 创建管理员用户

```bash
# 启动分片集群
$ sh ~/shell-script/sharded_cluster.sh

# 登录路由节点
$ mongo --port 27117

# 创建超级管理员账户（使用mongo的当前机器登录可以直接创建用户）
$ myrs:PRIMARY> db.createUser({user:"myroot",pwd:"123456",roles:["root"]})
Successfully added user: { "user" : "myroot", "roles" : [ "root" ] }
```



##### 创建普通用户

```bash
# 使用myroot管理员账号进行登录（需要重新连接mongo）
$ mongos> use admin
switched to db admin
$ mongos> 
$ mongos> db.auth("myroot", "123456")
1

# 切换数据库
$ mongos> use articledb
switched to db articledb

# 创建普通用户bobo
$ mongos> db.createUser({user:"bobo", pwd:"123456", roles:["readWrite"]})
Successfully added user: { "user" : "bobo", "roles" : [ "readWrite" ] }
```



##### 认证测试

```bash
# 测试超级管理员账号
# 连接路由
$ mongo --port 27117
# 无法看到数据库，需要登录认证
$ mongos> show dbs

# 执行登录操作
$ mongos> use admin
switched to db admin
$ mongos> db.auth("myroot", "123456")
1
# 登录之后可以查看数据库
$ mongos> show dbs
admin      0.000GB
articledb  0.005GB
config     0.004GB
test       0.000GB

# 普通用户登录测试
# 切换到articledb数据库
$ mongos> use articledb
switched to db articledb

# 执行登录操作
$ mongos> db.auth("bobo", "123456")
1
# 可以看到对应的数据库
mongos> show dbs
$ articledb  0.005GB
```



##### Spring认证芒果

yml配置以下即可：

```yml
spring:
  application:
    name: spring-mongodb
  data:
    mongodb:
      # 有密码连接
      uri: mongodb://bobo:123456@mongodb1:27117,mongodb1:27217/articledb
```



##### compass认证芒果

![image-20220726091149293](../../md-photo/image-20220726091149293.png)



## SpringBoot整合mongodb

pom引入依赖：

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-mongodb</artifactId>
</dependency>

```

yml配置：

```yml
server:
  port: 8091

spring:
  application:
    name: spring-mongodb
  data:
    mongodb:
      # 无密码连接
      # uri: mongodb://mongodb1:27017/spring
      # 有密码连接
      # mongodb://用户名:密码@服务器IP:端口/数据库名
      # 连接副本集语法
      # slaveOk=true:开启副本节点读的功能
      # connect=replicaSet：自动到副本集中选择读写的主机。如果slaveOk是打开的，则实现了读写分离
      uri: mongodb://mongodb1:27017,mongodb1:27018,mongodb1:27019/articledb?connect=replicaSet&slaveOk=true&relicaSet=myrs
```

添加mongo的监听器：

![image-20220716213824881](../../md-photo/image-20220716213824881.png)

内容如下：

```java
/**
 * 监听芒果 保存数据
 */
@Configuration
public class ApplicationReadyListener implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    private MongoTemplate mongoTemplate;

    private static final String TYPEKEY = "_class";

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        MongoConverter converter = mongoTemplate.getConverter();
        if (converter.getTypeMapper().isTypeKey(TYPEKEY)) {
            ((MappingMongoConverter) converter).setTypeMapper(new DefaultMongoTypeMapper(null));
        }
    }
}
```

接下来使用：**<font color="red">MongoTemplate</font>**模板进行相关的操作即可





## mongodb命令

注意：空值是null（小写），NULL和Null都不行；Mongo的可空类型是Null；

### 常用命令

```bash
$all #匹配所有
$elemMatch #数组元素匹配
$exists #字段是否存在: {"name" :{$exists:true}}
$gt #>
$gte #>=
$lt #<
$lte #<=
$in #in 交集: { "idlist":{$in:[1,2]} }或{"idlist" : [1,2]}
$nin #not in
$ne #!=,not null:{name: {$ne:null} }
$not #!
$or #||, { $or:[{key1: value1}, {key2:value2}] }
$regex #正则式，查结尾为1：{"name":{$regex:"1$"}}
like #like,{"name":/a/}, 查结尾{"name":/1$/}。/[Git]/失败，/Git]/是可以的
not like #{Name:{$not:/a/}}
null # 字段不存在或字段值为空 {name: null}
$size # 数组元素个数 {idlist:{$size:3}}

$set
db.Table1.update({_id:1},{$set:{Name:"tom"}});

# 查时间, UTC时间格式是 yyyy-MM-ddThh:mm:ss.fffZ，秒和毫秒之间是点"."而不是冒号
{ "CreateTime":{$gt:ISODate("2015-09-01T13:08:16.603Z")} }
{ "CreateTime":{$gt:ISODate("2016-05-09")} }
{ "CreateTime": { $gt:ISODate("2017-04-11"),$lt:ISODate("2017-04-12") } }

# 嵌套查询用句点符号
{ "Content._v._id":37 }
# 示例数据：
{"Content": {
    "_v": {
      "_id": 37,
    }
}}

# 嵌套对象数组元素匹配
{"Func.Items":{$elemMatch:{value:1} } } 
# 示例数据：
{"Func":{
    "Items":[ {value:1},{value:2} ]
}}

# 正则式
{name: /^T.*/}
{name: {$not: /^T.*/}}

# Javascript查询和$where查询
db.c1.find( { a : { $gt: 3 } } );
db.c1.find( { $where: "this.a > 3" } );
db.c1.find("this.a > 3");
f = function() { return this.a > 3; } db.c1.find(f);

# skip跳过指定文档数, limit返回指定文档数, sort:1正序asc|-1倒序desc
db.users.find().skip(10).limit(5).sort({name: 1}); #跳过前10条，返回5条，按name正排序
db.users.find().skip(10).limit(5).count(true);

# count(),count(true)或count(非0)
db.users.find().count();  #返回指定条件记录数
db.users.find().skip(10).limit(5).count(); #返回指定条件记录数，此处为uesr表所有记录数，而不是5。
db.users.find().skip(10).limit(5).count(true); #count(true)或count(非0)可返回限制记录数5
```



### 增删改的返回WriteResult

```bash
# 增删改的返回WriteResult
nInserted新增行数；#WriteResult({ "nInserted" : 1 }) 
nRemoved删除行数; #{"nRemoved": 1}
nMatched修改时查到的行数；#{"nMatched": 1, "nUpserted": 0, "nModified": 1}
nModified修改的行数，如果查到但没改变此值不增加，比如原值为tomxx，改为tomxx值不变所以此值不加；
nUpserted开启Upserted时执行update语句，找不到查找的值时新增的行数；
# WriteResult({ "nMatched" : 1, "nUpserted" : 0, "nModified" : 1 })

db.user.insert({_id:1,name:"tom"});
# WriteResult({ "nInserted" : 1 })

db.user.update({_id:1},{$set:{name:"tomxx"}},true);
# 第1次有变：WriteResult({ "nMatched" : 1, "nUpserted" : 0, "nModified" : 1 })
db.user.update({_id:1},{$set:{name:"tomxx"}},true);
# 第2次不变：WriteResult({ "nMatched" : 1, "nUpserted" : 0, "nModified" : 0 })

db.user.update({_id:10},{$set:{name:"tom"}}); #不存在10
# WriteResult({ "nMatched" : 0, "nUpserted" : 0, "nModified" : 0 }) 

db.user.update({_id:10},{$set:{name:"tom"}},true);#Upserted参数为true时，不存在则新增
# WriteResult({"nMatched" : 0,"nUpserted" : 1,"nModified" : 0,"_id" : 10})
```



### Aggregate聚合

```bash
# 注意：$group第一列好像必须得是_id，换其他名称出错

# count(1)，sum(xx)
db.log.aggregate([
{
  $match:{name:"test"}
},
{
  $group:{_id:"$name",count:{$sum:1},docs:{$sum:"$DocCount"}}
}]); 
# 结果
{
  "_id" : "test", 
  "count" : NumberInt(186), 
  "docs" : NumberLong(92500)
}

$group下面的$match过滤结果集，相当于having 
db.log.aggregate([
{
  $match:{Switch:2}
},
{
  $group:{_id:"$name",count:{$sum:1},docs:{$sum:"$DocCount"}}
},
{
  $match:{count:{$gte:10}}
}
]); 

db.user.aggregate([
{
    "$group":{"_id":{sex:1}, qty:{$sum:1},}
},
{
    "$match":{"qty":{$gt:1}}
}
]);
# 查询结果
{"_id": {sex:1}, "qty": 2}
{"_id": {sex:2}, "qty": 3}

db.user.aggregate([
{"$match":{"sex":1}},
{"$group":{
    "_id":{sex: "$sex", age:"$age"},
    qty:{$sum:1}
  }
},
{"$match":{"qty":{$gt:1}}}
]); 
可以优化为
db.user.aggregate([
{"$match":{"sex":1}},
{"$group":{
    "_id":"$age", 
    qty:{$sum:1}
  }
},
{"$match":{"qty":{$gt:1}}}
]); 

# 检查名称是否重复
db.user.aggregate([
{"$match":{"Status":1}},
{"$group":{
    "_id":{shopid:"$shopid",name:"$name"}, 
    qty:{$sum:1}
  }
},
{"$match":{"qty":{$gt:1}}}
]);
```

