# Redis常用命令

## 常用命令

### 启动redis

```bash
$ redis-server redis.config
```



redis.config常用配置：

| 参数                 | 值                      | 注释                                                   |
| -------------------- | ----------------------- | ------------------------------------------------------ |
| port                 | 7000                    | 端口号                                                 |
| bind                 | 0.0.0.0                 | 对所有端口开放                                         |
| daemonize            | yes                     | redis后台运行                                          |
| pidfile              | /var/run/redis_7000.pid | redis自动创建的进程监控                                |
| cluster-enabled      | yes                     | 开启集群                                               |
| cluster-config-file  | nodes_7000.conf         | 集群的配置 配置文件首次启动自动生成                    |
| cluster-node-timeout | 15000                   | 请求超时 默认15秒，可自行设置                          |
| appendonly           | yes                     | aof日志开启 有需要就开启，它会每次写操作都记录一条日志 |
| maxmemory            | 7516192768              | 限制内存在7G                                           |



## Redis主从复制

### 一主多从

<font color='red'>**一个主从复制只能有一个主服务器**</font>

<font color='red'>**在主机上写，在从机上可以读取数据**</font>
<font color='red'>**在从机上写数据报错**</font>



![image](../../md-photo/c9b1bc8879334ea0b54882d7f3eeb897.png)



优点：

- 读写分离（**master以写为主**；**slave以读为主**）
- 容灾快速恢复（一个从服务器挂了，还可以从其它服务器读取）



实际配置：

端口号配置：

主Redis：5379

从Redis1：5380

从Redis2：5381



主Redis搭建：

```bash
# 创建主从复制的根目录
$ mkdir -p /gjmou/software/redis/master_slave
# 创建配置文件文件夹
$ mkdir -p /gjmou/software/redis/master_slave/config
# 创建pid存放文件夹 mkdir -p /gjmou/software/redis/master_slave/config
$ mkdir -p /gjmou/software/redis/master_slave/pid
# 创建数据存放的文件夹
$ mkdir -p /gjmou/software/redis/master_slave/dbfile

# 创建配置文件redis5379.config
$ touch /gjmou/software/redis/master_slave/config/redis5379.config
# 录入以下内容
# 引入公用配置文件
include /gjmou/software/redis/bin/redis.config
# 开启守护进程
daemonize yes
# 端口号
port 5379
# 对所有端口开放
bind 0.0.0.0
# 数据文件名字
dbfilename dump5379.rdb
# 关闭aop
appendonly no
# pid文件
pidfile /gjmou/software/redis/master_slave/pid/redis5379.pid
```



从Redis搭建：

```bash
# 分别创建redis5380.config redis5381.config 文件，将主Redis的5379全部替换为对应的端口号即可
```



启动所有redis服务：

```bash
$ redis-server redis5379.config 
$ redis-server redis5380.config   
$ redis-server redis5381.config   
```



查看redis的状态：

```bash
# 连接redis客户端
$ redis-cli -p 5379
$ info replication
role:master # 当前是一个主机
connected_slaves:0 #下面没有从服务器
master_failover_state:no-failover
master_replid:c7b9718fd66e8911f6b297f8d455b2bdb540fe3b
master_replid2:0000000000000000000000000000000000000000
master_repl_offset:0
second_repl_offset:-1
repl_backlog_active:0
repl_backlog_size:1048576
repl_backlog_first_byte_offset:0
repl_backlog_histlen:0

```



配置主从关系：

在Redis从服务器*5380*和*5381*上执行：

```bash
$ slaveof <ip>[主Redis-ip] <port>[主Redis-端口号]
$ slaveof 127.0.0.1 5379

# 防止重启后失效，在从Redis服务器配置文件追加
# 作为5379的从服务器
slaveof 127.0.0.1 5379
```



添加快速启动一主多从的shell脚本：master_slave.sh

```shell
#!/bin/sh
# 启动主服务器
`redis-server /gjmou/software/redis/master_slave/config/redis5379.config`;

# 启动从服务器1
`redis-server /gjmou/software/redis/master_slave/config/redis5380.config`;

# 启动从服务器2
`redis-server /gjmou/software/redis/master_slave/config/redis5381.config`;
```



### 薪火相传

![image](../../md-photo/38909d2838084e148671181a5fc784f1.png)

从服务器还可以作为其他从服务器的主服务器，可以帮主服务器减轻同步数据的压力。

<font color="red">**风险：一旦某个slave出现意外停止运行后，以这台slave为主的其他slave都无法保存数据。**</font>

配置：将5381从服务器作为5380的从服务器

```bash
# 登录5381从服务器
$ redis-cli -p 5381

$ slaveof 127.0.0.1 5380
OK
$ info replication # 查看当前的状态
# Replication
role:slave
master_host:127.0.0.1
master_port:5380 # 主服务器为另外一个从服务器5380
master_link_status:up
master_last_io_seconds_ago:6
master_sync_in_progress:0
slave_read_repl_offset:3022
slave_repl_offset:3022
slave_priority:100
slave_read_only:1
replica_announced:1
connected_slaves:0
master_failover_state:no-failover
master_replid:56f7a1dd679df94d4c21fd7d215fbb0fdcb4aa47
master_replid2:0000000000000000000000000000000000000000
master_repl_offset:3022
second_repl_offset:-1
repl_backlog_active:1
repl_backlog_size:1048576
repl_backlog_first_byte_offset:1679
repl_backlog_histlen:1344
```



### 反客为主

反客为主指一个master服务器同时挂2个salve服务器。当master宕机后，在2个slave服务器的哪一台服务器中执行slaveof no one 命令，哪一台服务器就会升级为master服务器。

```bash
# 将主服务器5379挂掉
$ redis-cli -p 5379
$ shutdown SAVE

# 登录5380从服务器
$ redis-cli -p 5380
# 将从服务器设置为主服务器
$ slaveof no one
127.0.0.1:5380> info replication
# Replication
role:master # 可以看到5380晋升为主服务器
connected_slaves:0
master_failover_state:no-failover
master_replid:9817b76002fc76af32a44b27d1ca293f0621a340
master_replid2:56f7a1dd679df94d4c21fd7d215fbb0fdcb4aa47
master_repl_offset:3494
second_repl_offset:3495
repl_backlog_active:1
repl_backlog_size:1048576
repl_backlog_first_byte_offset:1
repl_backlog_histlen:3494
```



### 哨兵模式

<font color="red">**反客为主的自动版**</font>，能够后台监控主机是否故障，如果故障了根据投票数自动将从库转换为主库

配置：

```bash
# 创建哨兵文件夹
$ mkdir -p /gjmou/software/redis/master_slave/sentinel

# 创建哨兵配置文件
$ touch /gjmou/software/redis/master_slave/sentinel/sentinel.config 
# 录入以下内容
# 其中master为监控对象起的服务器名称， 1 为至少有多少个哨兵同意迁移的数量。
sentinel monitor master 127.0.0.1 5379 1

# 启动哨兵
$ redis-sentinel sentinel.config 

# 关闭原来的主服务器 5379
$ 127.0.0.1:5379> shutdown save

# 过10秒左右后查看哨兵日志
# 发现主服务器已经连不上了
+sdown master master 127.0.0.1 5379
+odown master master 127.0.0.1 5379 #quorum 1/1
+new-epoch 1
+try-failover master master 127.0.0.1 5379
+vote-for-leader 62c1b15f2b5116cda099e4c0512720a3ad3c2852 1
+elected-leader master master 127.0.0.1 5379
+failover-state-select-slave master master 127.0.0.1 5379
# 选择新的主服务器
+selected-slave slave 127.0.0.1:5381 127.0.0.1 5381 @ master 127.0.0.1 5379
+failover-state-send-slaveof-noone slave 127.0.0.1:5381 127.0.0.1 5381 @ master 127.0.0.1 5379
+failover-state-wait-promotion slave 127.0.0.1:5381 127.0.0.1 5381 @ master 127.0.0.1 5379
+promoted-slave slave 127.0.0.1:5381 127.0.0.1 5381 @ master 127.0.0.1 5379
+failover-state-reconf-slaves master master 127.0.0.1 5379
+slave-reconf-sent slave 127.0.0.1:5380 127.0.0.1 5380 @ master 127.0.0.1 5379
+slave-reconf-inprog slave 127.0.0.1:5380 127.0.0.1 5380 @ master 127.0.0.1 5379
+slave-reconf-done slave 127.0.0.1:5380 127.0.0.1 5380 @ master 127.0.0.1 5379
+failover-end master master 127.0.0.1 5379
# 进行切换主服务器的操作
+switch-master master 127.0.0.1 5379 127.0.0.1 5381
+slave slave 127.0.0.1:5380 127.0.0.1 5380 @ master 127.0.0.1 5381
+slave slave 127.0.0.1:5379 127.0.0.1 5379 @ master 127.0.0.1 5381
# 原来的主服务器作为新主服务器的从服务器
+sdown slave 127.0.0.1:5379 127.0.0.1 5379 @ master 127.0.0.1 5381

# 此时查看新选中的5381服务器的状态
$ 127.0.0.1:5381> info replication
# Replication
role:master # 5381已经切换为主服务器
connected_slaves:1
slave0:ip=127.0.0.1,port=5380,state=online,offset=36154,lag=0
master_failover_state:no-failover
master_replid:0c386170dcde148c7ca38d4efa25557e4c2068b5
master_replid2:a5cbf63b90525930e185c6f914f26e2f3a46c3d9
master_repl_offset:36154
second_repl_offset:8048
repl_backlog_active:1
repl_backlog_size:1048576
repl_backlog_first_byte_offset:1
repl_backlog_histlen:36154
```



<font color='red'>**复制延时**</font>

由于所有的写操作都是先在Master上操作，然后同步更新到Slave上，所以从Master同步到Slave机器有一定的延迟，当系统很繁忙的时候，延迟问题会更加严重，Slave机器数量的增加也会使这个问题更加严重。



<font color='red'>**故障恢复**</font>

![image](../../md-photo/f521c900282b461f80abf97334fe6b29.png)



优先级在<font color="red">**redis.config**</font>中配置，默认：slave-priority 100，值越小优先级越高
偏移量是指获得原主机数据最全的（<font color="red">**跟主服务器的数据最相似的**</font>）
每个redis实例启动后都会随机生成一个40位的runid



### springboot配置

<font color='red'>**需要将所有从Redis的slaveof 地址从127.0.0.1改为主机对应的ip**</font>

<font color='red'>**需要将所有哨兵的sentinel monitor master地址从127.0.0.1改为主机对应的ip**</font>

#### 单个哨兵的模式

springboot的配置文件：

```properties
#主从复制 1哨兵模式
spring.redis.sentinel.master=master
spring.redis.sentinel.nodes=192.168.239.72:26379
```

#### 三个哨兵的模式

```bash
# 创建存放pid和日志的文件夹
$ mkdir -p /gjmou/software/redis/master_slave/sentinel/pid
$ mkdir -p /gjmou/software/redis/master_slave/sentinel/log

# 创建27001哨兵配置文件
$ touch /gjmou/software/redis/master_slave/sentinel/sentinel27001.config
# 输入以下内容
# 端口号
port 27001
# 守护进程运行
daemonize yes
pidfile /gjmou/software/redis/master_slave/sentinel/pid/sentinel-27001.pid
logfile /gjmou/software/redis/master_slave/sentinel/log/sentinel-27001.log

# 监控192.168.239.72:5379实例，实例取名为master，当有两个哨兵认为实例下线后，自动进行故障转移
sentinel monitor master 192.168.239.72 5379 2
# 服务不可达时间，心跳超过这个时间，sentinel将认为节点挂了
sentinel down-after-milliseconds master 5000
sentinel failover-timeout master 60000
sentinel parallel-syncs master 1

# 以同样的方式创建27002、27003哨兵，修改端口号和pid、log文件
```



在master_slave.sh文件中添加对哨兵的启动

```bash
# 启动哨兵27001
`redis-sentinel /gjmou/software/redis/master_slave/sentinel/sentinel27001.config`;

# 启动哨兵27002
`redis-sentinel /gjmou/software/redis/master_slave/sentinel/sentinel27002.config`;

# 启动哨兵27003
`redis-sentinel /gjmou/software/redis/master_slave/sentinel/sentinel27003.config`;
```



springboot的配置文件：

```
#主从复制 3哨兵模式
spring.redis.sentinel.master=master
spring.redis.sentinel.nodes=192.168.239.72:27001,192.168.239.72:27002,192.168.239.72:27003
```



## Redis搭建集群

Redis 集群实现了对Redis的水平扩容，即启动N个redis节点，将整个数据库分布存储在这N个节点中，<font color='red'>**每个节点存储总数据的1/N**</font>。
Redis 集群通过分区（partition）来提供一定程度的可用性（availability）： 即使集群中有一部分节点失效或者无法进行通讯， 集群也可以继续处理命令请求。



搭建集群：

<font color='red'>**创建6个redis实例：5479,5480,5481,5482,5483,5484**</font>



创建集群文件夹：

```bash
$ mkdir -p /gjmou/software/redis/cluster

# 添加配置文件夹
$ mkdir -p /gjmou/software/redis/cluster/config

# 添加记录节点信息文件夹
$ mkdir -p /gjmou/software/redis/cluster/config/nodes
# 添加存放pid文件的文件夹
$ mkdir -p /gjmou/software/redis/cluster/config/pid
# 参见存放日志的文件夹
$ mkdir -p /gjmou/software/redis/cluster/config/log

# 创建5479配置文件
$ touch /gjmou/software/redis/cluster/config/redis5479.config
# 输入以下的内容
# 引入公用配置文件
include /gjmou/software/redis/bin/redis.config
# 开启守护进程
daemonize yes
# 端口号
port 5479
# 对所有端口开放
bind 0.0.0.0
# 数据文件名字
dbfilename dump5479.rdb
# 关闭aop
appendonly no
# pid文件
pidfile /gjmou/software/redis/cluster/config/pid/redis5479.pid
# 日志文件目录
logfile /gjmou/software/redis/cluster/config/log/redis5479.log
# 开启集群
cluster-enabled yes
# 限制内存在7G
maxmemory 7516192768
# 设置集群节点配置文件
cluster-config-file /gjmou/software/redis/cluster/config/nodes/nodes_5479.conf

# 同样的创建redis5480、redis5481、redis5482、redis5483、redis5484配置文件夹和配置文件，只需要修改端口号、集群节点配置文件、pid文件、日志文件目录即可
```

<font color='green'>**Tips：vi编辑的时候，使用%s/5479/5480命令快速将5379替换为5480**</font>



编写快速启动shell脚本：cluster.sh

```bash
#!/bin/sh
# 启动redis5479
`redis-server /gjmou/software/redis/cluster/config/redis5479.config`;

# 启动redis5480
`redis-server /gjmou/software/redis/cluster/config/redis5480.config`;

# 启动redis5481
`redis-server /gjmou/software/redis/cluster/config/redis5481.config`;

# 启动redis5482
`redis-server /gjmou/software/redis/cluster/config/redis5482.config`;

# 启动redis5483
`redis-server /gjmou/software/redis/cluster/config/redis5483.config`;

# 启动redis5484
`redis-server /gjmou/software/redis/cluster/config/redis5484.config`;
```



将6个节点合成一个集群：

```bash
# --cluster-replicas 1 其中的1代表从机的数量，目前是最简单的集群：1主一从
$ redis-cli --cluster create --cluster-replicas 1 192.168.239.72:5479 192.168.239.72:5480 192.168.239.72:5481 192.168.239.72:5482 192.168.239.72:5483 192.168.239.72:5484
# redis对应的提示信息
# 代表一共有6个节点
>>> Performing hash slots allocation on 6 nodes...
Master[0] -> Slots 0 - 5460
Master[1] -> Slots 5461 - 10922
Master[2] -> Slots 10923 - 16383
# 端口5483的节点是端口5479节点的从节点，以此类推
Adding replica 192.168.239.72:5483 to 192.168.239.72:5479
Adding replica 192.168.239.72:5484 to 192.168.239.72:5480
Adding replica 192.168.239.72:5482 to 192.168.239.72:5481
>>> Trying to optimize slaves allocation for anti-affinity
[WARNING] Some slaves are in the same host as their master
# 下面三台机器是主服务器
# 前面的一串是ID唯一标识
M: 7e103462dc34029b0c5ed564bfb8663638faaed6 192.168.239.72:5479
   slots:[0-5460] (5461 slots) master
M: d154b70cb87015e47272aea55a09a59375b233fb 192.168.239.72:5480
   slots:[5461-10922] (5462 slots) master
M: c009c1e809310e3428a93a06011ff6b3ef7c020b 192.168.239.72:5481
   slots:[10923-16383] (5461 slots) master
# 下面三台机器是从服务器   
S: c702fd5a628a49fa9b80167331d367794f26c0c3 192.168.239.72:5482
   replicates 7e103462dc34029b0c5ed564bfb8663638faaed6
S: 7d1183a1495130f1548ccf56aab50b72a056c84a 192.168.239.72:5483
   replicates d154b70cb87015e47272aea55a09a59375b233fb
S: 4171814eaf109498c49131556416682b1655fdb3 192.168.239.72:5484
   replicates c009c1e809310e3428a93a06011ff6b3ef7c020b
Can I set the above configuration? (type 'yes' to accept): yes
>>> Nodes configuration updated
>>> Assign a different config epoch to each node
>>> Sending CLUSTER MEET messages to join the cluster
Waiting for the cluster to join
.
>>> Performing Cluster Check (using node 192.168.239.72:5479)
# 最终结果：5482是5479的从节点 通过唯一ID进行主从确定
M: 7e103462dc34029b0c5ed564bfb8663638faaed6 192.168.239.72:5479
# 代表当前节点的哈希槽为0-5460，主节点才有哈希槽，redis集群中内置了16384（0-16383）个哈希槽。
   slots:[0-5460] (5461 slots) master
   1 additional replica(s)
S: 7d1183a1495130f1548ccf56aab50b72a056c84a 192.168.239.72:5483
   slots: (0 slots) slave
   replicates d154b70cb87015e47272aea55a09a59375b233fb
# 插槽10923-16383
M: c009c1e809310e3428a93a06011ff6b3ef7c020b 192.168.239.72:5481
   slots:[10923-16383] (5461 slots) master
   1 additional replica(s)
S: 4171814eaf109498c49131556416682b1655fdb3 192.168.239.72:5484
   slots: (0 slots) slave
   replicates c009c1e809310e3428a93a06011ff6b3ef7c020b
S: c702fd5a628a49fa9b80167331d367794f26c0c3 192.168.239.72:5482
   slots: (0 slots) slave
   replicates 7e103462dc34029b0c5ed564bfb8663638faaed6
# 插槽5461-10922
M: d154b70cb87015e47272aea55a09a59375b233fb 192.168.239.72:5480
   slots:[5461-10922] (5462 slots) master
   1 additional replica(s)
[OK] All nodes agree about slots configuration.
>>> Check for open slots...
>>> Check slots coverage...
[OK] All 16384 slots covered.
```



<font color='red'>**其中slots是集群使用公式CRC16(Key) % 16384来确定key属于哪个插槽的。根据计算出来的值，放到对应的主/从redis服务器上**</font>



此时使用mset批量添加键值会报错

```bash
$ mset k1 v1 k2 v2
(error) CROSSSLOT Keys in request don't hash to the same slot

# 由于无法计算插槽的位置，所以需要添加这批键值的分组
$ mset k1{key} v1 k2{key} v2

# 计算key的插槽值
$ cluster keyslot key
(integer) 12539

# 查询插槽中有几个键 只能查看自己插槽中的键值
$ cluster countkeysinslot 12539
(integer) 2

# 获取插槽中的键值 10代表一次获取10个元素 只能查看自己插槽中的键值
$ cluster getkeysinslot 12539 10
```



客户端测试：

```bash
# 连接客户端
$ redis-cli -h 192.168.239.72 -p 5479 -c
```

说明:

```bash
（1）-h 对应的IP地址，不写的话默认就是本机。

（2）-p 对应的是集群中指定实例的端口。

（3）-c 这是必须的，代表集群启动。
```

查看集群信息：

```bash
$ cluster info # 查询节点信息
```

查看节点信息：

```bash
$ cluster nodes # 查看集群节点信息
$ info replication # 查看主从关系信息
$ info server # 查看当前主机的信息
```



故障恢复：

```bash
# 将主机5479下线
$ 192.168.239.72:5479> shutdown save
not connected>

# 查看当前的集群信息
$ 127.0.0.1:5482> cluster nodes
4171814eaf109498c49131556416682b1655fdb3 192.168.239.72:5484@15484 slave c009c1e809310e3428a93a06011ff6b3ef7c020b 0 1658416190493 3 connected
# 5479的主机已经下线
7e103462dc34029b0c5ed564bfb8663638faaed6 192.168.239.72:5479@15479 master,fail - 1658416143251 1658416140000 1 disconnected
d154b70cb87015e47272aea55a09a59375b233fb 192.168.239.72:5480@15480 master - 0 1658416190000 2 connected 5461-10922
7d1183a1495130f1548ccf56aab50b72a056c84a 192.168.239.72:5483@15483 slave d154b70cb87015e47272aea55a09a59375b233fb 0 1658416190000 2 connected
# 从节点晋升为主节点
c702fd5a628a49fa9b80167331d367794f26c0c3 192.168.239.72:5482@15482 myself,master - 0 1658416189000 7 connected 0-5460
c009c1e809310e3428a93a06011ff6b3ef7c020b 192.168.239.72:5481@15481 master - 0 1658416191520 3 connected 10923-16383


# 重新启动5479
$ redis-server /gjmou/software/redis/cluster/config/redis5479.config 

$ 127.0.0.1:5479> cluster nodes
7d1183a1495130f1548ccf56aab50b72a056c84a 192.168.239.72:5483@15483 slave d154b70cb87015e47272aea55a09a59375b233fb 0 1658416469000 2 connected
# 5479作为5482的从节点
7e103462dc34029b0c5ed564bfb8663638faaed6 192.168.239.72:5479@15479 myself,slave c702fd5a628a49fa9b80167331d367794f26c0c3 0 1658416469000 7 connected
d154b70cb87015e47272aea55a09a59375b233fb 192.168.239.72:5480@15480 master - 0 1658416472268 2 connected 5461-10922
c009c1e809310e3428a93a06011ff6b3ef7c020b 192.168.239.72:5481@15481 master - 0 1658416470000 3 connected 10923-16383
4171814eaf109498c49131556416682b1655fdb3 192.168.239.72:5484@15484 slave c009c1e809310e3428a93a06011ff6b3ef7c020b 0 1658416471000 3 connected
# 5482仍然是主节点
c702fd5a628a49fa9b80167331d367794f26c0c3 192.168.239.72:5482@15482 master - 0 1658416471247 7 connected 0-5460
```



<font color='red'>**如果某一段插槽的主从节点全部挂掉，如果redis配置文件中配置了 cluster-require-full-coverage为yes，那么整个集群挂掉；如果为no，那么只是该插槽部分的数据无法使用，其他插槽还是能提供使用。**</font>



搭建一主两从集群：

```bash
# 添加redis5485、redis5486、redis5487
# 配置信息同redis5479，只需要将5479全部替换为对应的端口号即可

# cluster.sh文件中添加内容
# 启动redis5485
`redis-server /gjmou/software/redis/cluster/config/redis5485.config`;

# 启动redis5486
`redis-server /gjmou/software/redis/cluster/config/redis5486.config`;

# 启动redis5487
`redis-server /gjmou/software/redis/cluster/config/redis5487.config`;
# 添加内容结束

# 初始化集群
redis-cli --cluster create --cluster-replicas 2 192.168.239.72:5479 192.168.239.72:5480 192.168.239.72:5481 192.168.239.72:5482 192.168.239.72:5483 192.168.239.72:5484 192.168.239.72:5485 192.168.239.72:5486 192.168.239.72:5487
```



springboot连接集群：

```properties
# springboot 集群配置
spring.redis.cluster.nodes=192.168.239.72:5479,192.168.239.72:5480,192.168.239.72:5481,192.168.239.72:5482,192.168.239.72:5483,192.168.239.72:5484,192.168.239.72:5485,192.168.239.72:5486,192.168.239.72:5487

```

