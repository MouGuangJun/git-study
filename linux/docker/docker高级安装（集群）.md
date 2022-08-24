# docker高级安装（集群）

## mysql主从复制

**<font color='red'>3307为主服务器，3308为从服务器</font>**

### 主服务器

```bash
# 添加配置文件
$ vi /local/mysql_master/conf/my.cnf
# 录入以下的内容
##master 
[mysqld]
server-id=1
log-bin=master-bin
##复制指定库
##binlog-do-db=db_test
##忽略指定库
##replicate-ignore-db=mysql
##replicate-ignore-db=sys
##character set
character-set-server=utf8
collation-server=utf8_unicode_ci 
init_connect='SET collation_connection=utf8_unicode_ci'
init_connect='SET NAMES utf8'
skip-character-set-client-handshake
skip-name-resolve
secure-file-priv=/var/lib/mysql
sql_mode=ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION

# 启动mysql容器
$ docker run -p 3307:3306 --name mysql_master --privileged=true -v /local/mysql_master/log:/var/log/mysql -v /local/mysql_master/data:/var/lib/mysql -v /local/mysql_master/conf:/etc/mysql  -e MYSQL_ROOT_PASSWORD=123456 -d 3218b38490ce

# 进入容器
$ docker exec -it 4f55ab05d4ef bash

# 登录mysql
$docker mysql -uroot -p123456

# 创建主从复制用户
$docker>mysql use mysql;
$docker>mysql create user 'slave'@'%' IDENTIFIED WITH mysql_native_password BY '123456';
# 授权
$docker>mysql GRANT REPLICATION SLAVE ON *.* TO 'slave'@'%';
# 使授权立即生效
$docker>mysql flush privileges;

#查看状态，这里需要记住这个File和Position两个属性，slave从机需要
$docker>mysql show master status;
+-------------------+----------+--------------+------------------+-------------------+
| File              | Position | Binlog_Do_DB | Binlog_Ignore_DB | Executed_Gtid_Set |
+-------------------+----------+--------------+------------------+-------------------+
| master-bin.000003 |      843 |              |                  |                   |
+-------------------+----------+--------------+------------------+-------------------+
```



### 从服务器

```bash
# 添加配置文件
$ vi /local/mysql_slave/conf/my.cnf
# 录入以下的内容
#slave
[client]
default-character-set=utf8

[mysql]
default-character-set=utf8

[mysqld]
#slave
server-id=2
log-bin=slave01-bin
#1 只读
read-only=1
#复制指定库
#binlog-do-db=db_test
#忽略指定库
#replicate-ignore-db=mysql
#replicate-ignore-db=sys
#character set
character-set-server=utf8
collation-server=utf8_unicode_ci 
init_connect='SET collation_connection=utf8_unicode_ci'
init_connect='SET NAMES utf8'
skip-character-set-client-handshake
skip-name-resolve
secure-file-priv=/var/lib/mysql
sql_mode=ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION


$ docker run --name mysql_slave --privileged=true -v /local/mysql_slave/log:/var/log/mysql -v /local/mysql_slave/data:/var/lib/mysql -v /local/mysql_slave/conf:/etc/mysql -p 3308:3306 -e MYSQL_ROOT_PASSWORD=123456 --link mysql_master:master -d 3218b38490ce

# 进入容器内部
$ docker exec -it 4e0be2d7173b bash

# 登录mysql
$docker mysql -uroot -p123456

# 切换为master的从机
# 注意master_log_file，master_log_pos，master_port
$docker>mysql change master to master_host='192.168.239.74', master_user='slave', master_password='123456', master_port=3307, master_log_file='master-bin.000003', master_log_pos=843, master_connect_retry=30;

#启用从机
$docker>mysql start slave;

#检测是否成功
$docker>mysql show slave status\G;
```



主从搭建成功：

![image-20220823124159674](../../../md-photo/image-20220823124159674.png)



### 测试

```bash
# 主服务器
$docker>mysql_master create database docker_db;
$docker>mysql_master use docker_db;
$docker>mysql_master create table docker(id int, name varchar(20));
$docker>mysql_master insert into docker values(1, 'z3');
$docker>mysql_master select * from docker;
+------+------+
| id   | name |
+------+------+
|    1 | z3   |
+------+------+

# 从服务器
$docker>mysql_slave use docker_db;
$docker>mysql_slave select * from docker;
+------+------+
| id   | name |
+------+------+
|    1 | z3   |
```



## redis主从复制

### 启动redis实例

启动6个redis实例：

**<font color='red'>端口分别为6479、6480、6481、6482、6483、6484</font>**

```bash
# 6479
$ docker run -d --name redis-node1 --net host --privileged=true -v /local/redis_cluster/redis-node1/data:/data 16ecd2772934 --cluster-enabled yes --appendonly yes --port 6479
# 6480
$ docker run -d --name redis-node2 --net host --privileged=true -v /local/redis_cluster/redis-node2/data:/data 16ecd2772934 --cluster-enabled yes --appendonly yes --port 6480
# 6481
$ docker run -d --name redis-node3 --net host --privileged=true -v /local/redis_cluster/redis-node3/data:/data 16ecd2772934 --cluster-enabled yes --appendonly yes --port 6481
# 6482
$ docker run -d --name redis-node4 --net host --privileged=true -v /local/redis_cluster/redis-node4/data:/data 16ecd2772934 --cluster-enabled yes --appendonly yes --port 6482
# 6483
$ docker run -d --name redis-node5 --net host --privileged=true -v /local/redis_cluster/redis-node5/data:/data 16ecd2772934 --cluster-enabled yes --appendonly yes --port 6483
# 6484
$ docker run -d --name redis-node6 --net host --privileged=true -v /local/redis_cluster/redis-node6/data:/data 16ecd2772934 --cluster-enabled yes --appendonly yes --port 6484
```

启动参数详解：

| 参数                                           | 作用                              |
| ---------------------------------------------- | --------------------------------- |
| docker run                                     | 创建并运行docker容器实例          |
| --name redis-node-1                            | 容器名字                          |
| --net host                                     | 使用宿主机的IP和端口，默认        |
| --privileged=true                              | 获取宿主机root用户权限            |
| -v /local/redis_cluster/redis-node5/data:/data | 容器卷，宿主机地址:docker内部地址 |
| 16ecd2772934                                   | redis镜像                         |
| --cluster-enabled yes                          | 开启redis集群                     |
| --appendonly yes                               | 开启持久化                        |
| --port 6386                                    | redis端口号                       |



### 构建集群关系

```bash
# 进入6479进行集群搭建的操作
$ docker exec -it 4b2538889cca bash

$ redis-cli --cluster create 192.168.239.74:6479 192.168.239.74:6480 192.168.239.74:6481 192.168.239.74:6482 192.168.239.74:6483 192.168.239.74:6484 --cluster-replicas 1 

# 查看集群的构建关系
# 登录6479客户端
$docker redis-cli -p 6479
# 查看集群状态
$docker>redis-cli cluster info
# 查看集群节点状态
$docker>redis-cli cluster nodes
```

集群的状态信息：

![image-20220823231229078](../../../md-photo/image-20220823231229078.png)



![image-20220823231332246](../../../md-photo/image-20220823231332246.png)



### 检查集群信息

```bash
# 检查集群信息（通过6479演示）
$docker redis-cli --cluster check 192.168.239.74:6479
# 实例中对应的key的个数
192.168.239.74:6479 (7962fcbb...) -> 1 keys | 5461 slots | 1 slaves.
192.168.239.74:6481 (7cbdb819...) -> 1 keys | 5461 slots | 1 slaves.
192.168.239.74:6480 (e1d9c657...) -> 0 keys | 5462 slots | 1 slaves.
[OK] 2 keys in 3 masters.
0.00 keys per slot on average.
>>> Performing Cluster Check (using node 192.168.239.74:6479)
# 主从状态
M: 7962fcbb671c29fd77c17df9b273236798db8fe2 192.168.239.74:6479
   slots:[0-5460] (5461 slots) master
   1 additional replica(s)
S: 2ae8e22cb1933a8ac3ffdd86e34cc2a01ba80f4b 192.168.239.74:6484
   slots: (0 slots) slave
   replicates 7cbdb8196d621ba2f6ecd5c6b1b4410410622960
S: b7b061f463b189ec8ad4d108e8bd07895de420d8 192.168.239.74:6482
   slots: (0 slots) slave
   replicates 7962fcbb671c29fd77c17df9b273236798db8fe2
S: 3507680d64e12564e2f1dde314fc56a1dc9988dd 192.168.239.74:6483
   slots: (0 slots) slave
   replicates e1d9c657878f395e741623e3be54eb52a8909aef
M: 7cbdb8196d621ba2f6ecd5c6b1b4410410622960 192.168.239.74:6481
   slots:[10923-16383] (5461 slots) master
   1 additional replica(s)
M: e1d9c657878f395e741623e3be54eb52a8909aef 192.168.239.74:6480
   slots:[5461-10922] (5462 slots) master
   1 additional replica(s)
[OK] All nodes agree about slots configuration.
>>> Check for open slots...
>>> Check slots coverage...
[OK] All 16384 slots covered.
```



### redis实际情况演示

```bash
# 通过集群的方式进入客户端（***-c 代表以集群的方式进入客户端***）
# 进入redis-node1节点
$docker redis-cli -c -p 6479
```



redis通过哈希槽进行跳转：

![image-20220823232654168](../../../md-photo/image-20220823232654168.png)



### redis主从切换

```bash
# 由之前可知6479是6482的主机，查看6479关闭后，6482是否能切换为主机
$ docker stop redis-node1

# 从第二个节点进去查看集群信息
$ docker exec -it redis-node2 bash

# 进入redis客户端
$docker redis-cli -c -p 6480

# 查看集群节点信息
$docker>redis-cli cluster nodes
```

可以看到已经成功切换：

![image-20220824085827313](../../../md-photo/image-20220824085827313.png)



重启后的主从变化：

```bash
# 启动redis-node1节点
$ docker start redis-node1

# 从第一个节点进去查看集群信息
$ docker exec -it redis-node1 bash

# 进入redis客户端
$docker redis-cli -c -p 6479

# 查看集群节点信息
$docker>redis-cli cluster nodes
```

![image-20220824090309347](../../../md-photo/image-20220824090309347.png)



### redis实例启停命令

```bash
# 启动redis实例命令
$ docker start redis-node1 redis-node2 redis-node3 redis-node4 redis-node5 redis-node6

# 停止redis实例命令
$ docker stop redis-node1 redis-node2 redis-node3 redis-node4 redis-node5 redis-node6
```



