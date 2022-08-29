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



### redis集群扩容

**<font color='red'>添加6485和6486端口实例</font>**

```bash
# 添加6485和6486实例
# 6485
$ docker run -d --name redis-node7 --net host --privileged=true -v /local/redis_cluster/redis-node7/data:/data 16ecd2772934 --cluster-enabled yes --appendonly yes --port 6485
# 6486
$ docker run -d --name redis-node8 --net host --privileged=true -v /local/redis_cluster/redis-node8/data:/data 16ecd2772934 --cluster-enabled yes --appendonly yes --port 6486

# 将6485节点作为master
$ docker exec -it redis-node7 bash

# 添加master节点(后面那个6479是master节点)
# 这里是将节点加入了集群中，但是并没有分配slot，所以这个节点并没有真正的开始分担集群工作
$docker redis-cli --cluster add-node 192.168.239.74:6485 192.168.239.74:6479

# 检查redis集群状态
$docker redis-cli --cluster check 192.168.239.74:6485
```

当前集群状态：

![image-20220824211248651](../../../md-photo/image-20220824211248651.png)



```bash
# 重新分配哈希槽位(后面那个6485是master节点)
$docker redis-cli --cluster reshard 192.168.239.74:6485
>>> Performing Cluster Check (using node 192.168.239.74:6485)
M: 7486231378debd48feba4011b8a6eb07acb2e263 192.168.239.74:6485
   slots: (0 slots) master
M: 7962fcbb671c29fd77c17df9b273236798db8fe2 192.168.239.74:6479
   slots:[0-5460] (5461 slots) master
   1 additional replica(s)
S: b7b061f463b189ec8ad4d108e8bd07895de420d8 192.168.239.74:6482
   slots: (0 slots) slave
   replicates 7962fcbb671c29fd77c17df9b273236798db8fe2
M: 7cbdb8196d621ba2f6ecd5c6b1b4410410622960 192.168.239.74:6481
   slots:[10923-16383] (5461 slots) master
   1 additional replica(s)
M: e1d9c657878f395e741623e3be54eb52a8909aef 192.168.239.74:6480
   slots:[5461-10922] (5462 slots) master
   1 additional replica(s)
S: 2ae8e22cb1933a8ac3ffdd86e34cc2a01ba80f4b 192.168.239.74:6484
   slots: (0 slots) slave
   replicates 7cbdb8196d621ba2f6ecd5c6b1b4410410622960
S: 3507680d64e12564e2f1dde314fc56a1dc9988dd 192.168.239.74:6483
   slots: (0 slots) slave
   replicates e1d9c657878f395e741623e3be54eb52a8909aef
[OK] All nodes agree about slots configuration.
>>> Check for open slots...
>>> Check slots coverage...
[OK] All 16384 slots covered.
How many slots do you want to move (from 1 to 16384)? 4096 #[分配的哈希槽数=总槽数/master数]
What is the receiving node ID? 7486231378debd48feba4011b8a6eb07acb2e263#[被分配的master]
Please enter all the source node IDs.
  Type 'all' to use all the nodes as source nodes for the hash slots.
  Type 'done' once you entered all the source nodes IDs.
Source node #1: all #[全部重新分配]
```

![image-20220824212141531](../../../md-photo/image-20220824212141531.png)



```bash
# 查看重新分配后的结果
$docker redis-cli --cluster check 192.168.239.74:6485
192.168.239.74:6485 (74862313...) -> 1 keys | 4096 slots | 0 slaves.
192.168.239.74:6479 (7962fcbb...) -> 0 keys | 4096 slots | 1 slaves.
192.168.239.74:6481 (7cbdb819...) -> 1 keys | 4096 slots | 1 slaves.
192.168.239.74:6480 (e1d9c657...) -> 0 keys | 4096 slots | 1 slaves.
[OK] 2 keys in 4 masters.
0.00 keys per slot on average.
>>> Performing Cluster Check (using node 192.168.239.74:6485)
M: 7486231378debd48feba4011b8a6eb07acb2e263 192.168.239.74:6485 
   # 可以看到6485节点的哈希槽位是之前的槽位分出来的，而非重新均分
   slots:[0-1364],[5461-6826],[10923-12287] (4096 slots) master
M: 7962fcbb671c29fd77c17df9b273236798db8fe2 192.168.239.74:6479
   slots:[1365-5460] (4096 slots) master
   1 additional replica(s)
S: b7b061f463b189ec8ad4d108e8bd07895de420d8 192.168.239.74:6482
   slots: (0 slots) slave
   replicates 7962fcbb671c29fd77c17df9b273236798db8fe2
M: 7cbdb8196d621ba2f6ecd5c6b1b4410410622960 192.168.239.74:6481
   slots:[12288-16383] (4096 slots) master
   1 additional replica(s)
M: e1d9c657878f395e741623e3be54eb52a8909aef 192.168.239.74:6480
   slots:[6827-10922] (4096 slots) master
   1 additional replica(s)
S: 2ae8e22cb1933a8ac3ffdd86e34cc2a01ba80f4b 192.168.239.74:6484
   slots: (0 slots) slave
   replicates 7cbdb8196d621ba2f6ecd5c6b1b4410410622960
S: 3507680d64e12564e2f1dde314fc56a1dc9988dd 192.168.239.74:6483
   slots: (0 slots) slave
   replicates e1d9c657878f395e741623e3be54eb52a8909aef
[OK] All nodes agree about slots configuration.
>>> Check for open slots...
>>> Check slots coverage...
[OK] All 16384 slots covered.
```

重新分配后的结果：

![image-20220824212557496](../../../md-photo/image-20220824212557496.png)



将6486实例作为6485实例的从节点

```bash
# 7486231378debd48feba4011b8a6eb07acb2e263是6485实例的redis编号
# 192.168.239.74:6486 192.168.239.74:6485是需要创建主从关系的两个实例
$docker redis-cli --cluster add-node 192.168.239.74:6486 192.168.239.74:6485 --cluster-slave --cluster-master-id 7486231378debd48feba4011b8a6eb07acb2e263

# 查看redis集群状态
$docker redis-cli --cluster check 192.168.239.74:6485
192.168.239.74:6485 (74862313...) -> 1 keys | 4096 slots | 1 slaves.
192.168.239.74:6479 (7962fcbb...) -> 0 keys | 4096 slots | 1 slaves.
192.168.239.74:6481 (7cbdb819...) -> 1 keys | 4096 slots | 1 slaves.
192.168.239.74:6480 (e1d9c657...) -> 0 keys | 4096 slots | 1 slaves.
[OK] 2 keys in 4 masters.
0.00 keys per slot on average.
>>> Performing Cluster Check (using node 192.168.239.74:6485)
M: 7486231378debd48feba4011b8a6eb07acb2e263 192.168.239.74:6485
   slots:[0-1364],[5461-6826],[10923-12287] (4096 slots) master
   1 additional replica(s)
M: 7962fcbb671c29fd77c17df9b273236798db8fe2 192.168.239.74:6479
   slots:[1365-5460] (4096 slots) master
   1 additional replica(s)
S: b7b061f463b189ec8ad4d108e8bd07895de420d8 192.168.239.74:6482
   slots: (0 slots) slave
   replicates 7962fcbb671c29fd77c17df9b273236798db8fe2
M: 7cbdb8196d621ba2f6ecd5c6b1b4410410622960 192.168.239.74:6481
   slots:[12288-16383] (4096 slots) master
   1 additional replica(s)
M: e1d9c657878f395e741623e3be54eb52a8909aef 192.168.239.74:6480
   slots:[6827-10922] (4096 slots) master
   1 additional replica(s)
# 可以看到6486作为7486231378debd48feba4011b8a6eb07acb2e263【6485】的从节点
S: 2004378407c91ba144da45e1025db52e099d49a8 192.168.239.74:6486
   slots: (0 slots) slave
   replicates 7486231378debd48feba4011b8a6eb07acb2e263
S: 2ae8e22cb1933a8ac3ffdd86e34cc2a01ba80f4b 192.168.239.74:6484
   slots: (0 slots) slave
   replicates 7cbdb8196d621ba2f6ecd5c6b1b4410410622960
S: 3507680d64e12564e2f1dde314fc56a1dc9988dd 192.168.239.74:6483
   slots: (0 slots) slave
   replicates e1d9c657878f395e741623e3be54eb52a8909aef
[OK] All nodes agree about slots configuration.
>>> Check for open slots...
>>> Check slots coverage...
[OK] All 16384 slots covered.
```



### redis集群缩容

**<font color='red'>删除6485和6486端口实例</font>**

先删除从节点，再删除主节点（主节点（读写）可能还在写数据，所以得先删除从节点（只读））

```bash
# 删除6486从节点
# 192.168.239.74:6486 为实例ip和端口号，2004378407c91ba144da45e1025db52e099d49a8为redis编号
$docker redis-cli --cluster del-node 192.168.239.74:6486 2004378407c91ba144da45e1025db52e099d49a8

>>> Removing node 2004378407c91ba144da45e1025db52e099d49a8 from cluster 192.168.239.74:6486
>>> Sending CLUSTER FORGET messages to the cluster...
>>> Sending CLUSTER RESET SOFT to the deleted node.

# 查看当前集群状态
$docker redis-cli --cluster check 192.168.239.74:6485   
```

![image-20220824214015177](../../../md-photo/image-20220824214015177.png)



清空6485的所有槽位，全部给6479实例（**<font color='red'>如果不全部分给一个实例，按照个数重复执行一下的命令即可</font>**）：

```bash
$docker redis-cli --cluster reshard 192.168.239.74:6485
>>> Performing Cluster Check (using node 192.168.239.74:6485)
M: 7486231378debd48feba4011b8a6eb07acb2e263 192.168.239.74:6485
   slots:[0-1364],[5461-6826],[10923-12287] (4096 slots) master
M: 7962fcbb671c29fd77c17df9b273236798db8fe2 192.168.239.74:6479
   slots:[1365-5460] (4096 slots) master
   1 additional replica(s)
S: b7b061f463b189ec8ad4d108e8bd07895de420d8 192.168.239.74:6482
   slots: (0 slots) slave
   replicates 7962fcbb671c29fd77c17df9b273236798db8fe2
M: 7cbdb8196d621ba2f6ecd5c6b1b4410410622960 192.168.239.74:6481
   slots:[12288-16383] (4096 slots) master
   1 additional replica(s)
M: e1d9c657878f395e741623e3be54eb52a8909aef 192.168.239.74:6480
   slots:[6827-10922] (4096 slots) master
   1 additional replica(s)
S: 2ae8e22cb1933a8ac3ffdd86e34cc2a01ba80f4b 192.168.239.74:6484
   slots: (0 slots) slave
   replicates 7cbdb8196d621ba2f6ecd5c6b1b4410410622960
S: 3507680d64e12564e2f1dde314fc56a1dc9988dd 192.168.239.74:6483
   slots: (0 slots) slave
   replicates e1d9c657878f395e741623e3be54eb52a8909aef
[OK] All nodes agree about slots configuration.
>>> Check for open slots...
>>> Check slots coverage...
[OK] All 16384 slots covered.
# 重新分配的哈希槽个数
How many slots do you want to move (from 1 to 16384)? 4096
# 接收被删除实例上的所有哈希槽
What is the receiving node ID? 7962fcbb671c29fd77c17df9b273236798db8fe2
Please enter all the source node IDs.
  Type 'all' to use all the nodes as source nodes for the hash slots.
  Type 'done' once you entered all the source nodes IDs.
# 被删除的redis实例，可以为多个（all：全部；输入实例编号完成后，按done结束）
Source node #1: 7486231378debd48feba4011b8a6eb07acb2e263
Source node #2: done
```

![image-20220824214945993](../../../md-photo/image-20220824214945993.png)



查看重新分配的结果：

```bash
$docker redis-cli --cluster check 192.168.239.74:6485 
```

![image-20220824215331988](../../../md-photo/image-20220824215331988.png)



```bash
# 删除6485主节点
$docker redis-cli --cluster del-node 192.168.239.74:6485 7486231378debd48feba4011b8a6eb07acb2e263

# 检查集群状态信息 可以看到恢复了三主三从
$docker redis-cli --cluster check 192.168.239.74:6479 
192.168.239.74:6479 (7962fcbb...) -> 1 keys | 5461 slots | 1 slaves.
192.168.239.74:6481 (7cbdb819...) -> 3 keys | 5461 slots | 1 slaves.
192.168.239.74:6480 (e1d9c657...) -> 1 keys | 5462 slots | 1 slaves.
[OK] 5 keys in 3 masters.
0.00 keys per slot on average.
>>> Performing Cluster Check (using node 192.168.239.74:6479)
M: 7962fcbb671c29fd77c17df9b273236798db8fe2 192.168.239.74:6479
   slots:[2731-6826],[10923-12287] (5461 slots) master
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
   slots:[0-1364],[12288-16383] (5461 slots) master
   1 additional replica(s)
M: e1d9c657878f395e741623e3be54eb52a8909aef 192.168.239.74:6480
   slots:[1365-2730],[6827-10922] (5462 slots) master
   1 additional replica(s)
[OK] All nodes agree about slots configuration.
>>> Check for open slots...
>>> Check slots coverage...
[OK] All 16384 slots covered.
```

redis集群的状态信息：

![image-20220824220337593](../../../md-photo/image-20220824220337593.png)
