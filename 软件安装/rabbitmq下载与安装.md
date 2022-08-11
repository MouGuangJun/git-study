# RabbitMQ下载与安装

## rabbitMQ官网

https://www.rabbitmq.com/install-rpm.html



## 安装Erlang

因rabbitMQ是erlang开发的，所以首先咱们要安装它的开发环境！这里在rabbitMQ官网有阐述，同时也给提供了简单一些的方法：

![image-20220805224559390](../../md-photo/image-20220805224559390.png)



## 选择对应的版本进行下载

![image-20220805224711624](../../md-photo/image-20220805224711624.png)





![image-20220805224838187](../../md-photo/image-20220805224838187.png)



将文件上传到linux上，执行以下的命令进行安装：

```bash
$ sudo yum install erlang-23.3.4.1-1.el7.x86_64.rpm

# 测试
$ erl
Erlang/OTP 23 [erts-11.2.2.1] [source] [64-bit] [smp:1:1] [ds:1:1:10] [async-threads:1] [hipe]

Eshell V11.2.2.1  (abort with ^G)
1> 
```



注意rabbitmq与erlang的版本对应关系：

![image-20220805225157015](../../md-photo/image-20220805225157015.png)





## 安装rabbitmq

找到rabbitmq对应的下载位置：

![image-20220805225408955](../../md-photo/image-20220805225408955.png)



![image-20220805225422623](../../md-photo/image-20220805225422623.png)



下载能与Erlang对应上的版本：

![image-20220805225707766](../../md-photo/image-20220805225707766.png)



点击下载：



![image-20220805225743504](../../md-photo/image-20220805225743504.png)



将文件上传到linux上，执行以下的命令进行安装：

```bash
$ sudo yum install rabbitmq-server-3.9.21-1.el8.noarch.rpm 
```



启动rabbitmq的插件管理<font color='red'>**可视化控制台**</font>：

```bash
$ rabbitmq-plugins enable rabbitmq_management
Enabling plugins on node rabbit@rabbit1:
rabbitmq_management
The following plugins have been configured:
  rabbitmq_management
  rabbitmq_management_agent
  rabbitmq_web_dispatch
Applying plugin configuration to rabbit@rabbit1...
The following plugins have been enabled:
  rabbitmq_management
  rabbitmq_management_agent
  rabbitmq_web_dispatch

set 3 plugins.
Offline change; changes will take effect at broker restart.
```



 启动rabbitmq服务：

```bash
$ systemctl start rabbitmq-server
```



## 开放默认端口号

15672（管理控制台的端口号）、5672（TCP监听端口）

```bash
# 添加端口
#27017改成要添加的端口（--permanent永久生效，没有此参数重启后失效）
firewall-cmd --zone=public --add-port=15672/tcp --permanent
firewall-cmd --zone=public --add-port=5672/tcp --permanent   
# 更新防火墙
sudo firewall-cmd --reload
```



## 将默认用户放出来使用

```bash
$ vi ./usr/lib/rabbitmq/lib/rabbitmq_server-3.9.21/plugins/rabbit-3.9.21/ebin/rabbit.app
```

![image-20220805233617598](../../md-photo/image-20220805233617598.png)



![image-20220805233647728](../../md-photo/image-20220805233647728.png)



##  然后打开连接

http://rabbitmq1:15672

![image-20220805232329743](../../md-photo/image-20220805232329743.png)

输入账号guest和密码guest进行登录，如果登录失败，执行重启命令，让rabbitmq进行刷新配置信息的操作，成功登录到rabbit的图形化界面中：

![image-20220805234209709](../../md-photo/image-20220805234209709.png)



## 用户操作

创建一个账户：

```bash
# 添加 admin 用户并设置密码
$ rabbitmqctl add_user admin 123456

# 添加 admin 用户为administrator角色
$ rabbitmqctl set_user_tags admin administrator

# 设置 admin 用户的权限，指定允许访问的vhost以及write/read
$ rabbitmqctl set_permissions -p "/" admin ".*" ".*" ".*"

# 查看vhost（/）允许哪些用户访问
$ rabbitmqctl list_permissions -p /

# 查看用户列表
$ rabbitmqctl list_users
```



## 常用命令

```bash
# 启动
$ systemctl start rabbitmq-server

# 重启
$ systemctl restart rabbitmq-server
```



![img](https://img-blog.csdnimg.cn/cb9d06a6ff9e4e4296ca977712779f06.png)



# 搭建集群

## 镜像队列

![image-20220809231322108](../../md-photo/image-20220809231322108.png)



启动第一个节点：

```bash
# RABBITMQ_NODE_PORT：指定端口号
# RABBITMQ_SERVER_START_ARGS：设置启动参数
# RABBITMQ_NODENAME：设置节点名称
$ RABBITMQ_NODE_PORT=5682 RABBITMQ_SERVER_START_ARGS="-rabbitmq_management listener [{port,15682}]" RABBITMQ_NODENAME=rabbit_node1 rabbitmq-server -detached
```





启动第二个节点：

```bash
$ RABBITMQ_NODE_PORT=5692 RABBITMQ_SERVER_START_ARGS="-rabbitmq_management listener [{port,15692}]" RABBITMQ_NODENAME=rabbit_node2 rabbitmq-server -detached
```



开放端口号：

```bash
# 添加端口
#27017改成要添加的端口（--permanent永久生效，没有此参数重启后失效）
firewall-cmd --zone=public --add-port=15682/tcp --permanent
firewall-cmd --zone=public --add-port=5682/tcp --permanent   
firewall-cmd --zone=public --add-port=15692/tcp --permanent
firewall-cmd --zone=public --add-port=5692/tcp --permanent   
# 更新防火墙
sudo firewall-cmd --reload
```



把rabbit_node1作为主节点：

```bash
# 停止app
$ rabbitmqctl -n rabbit_node1 stop_app
# 重置节点
$ rabbitmqctl -n rabbit_node1 reset
# 启动app
$ rabbitmqctl -n rabbit_node1 start_app
```



把rabbit_node2操作为从节点：

```bash
# 停止app
$ rabbitmqctl -n rabbit_node2 stop_app
# 重置节点
$ rabbitmqctl -n rabbit_node2 reset
# 将rabbit_node2作为rabbit_node1的从节点
$ rabbitmqctl -n rabbit_node2 join_cluster rabbit_node1@rabbit1 # 其中rabbit1是自己的主机名
# 启动app
$ rabbitmqctl -n rabbit_node2 start_app
```



可以看到，搭建集群成功：



![image-20220810090742215](../../md-photo/image-20220810090742215.png)



## 镜像队列配置

>- RabbitMQ默认集群模式，并不保证队列的高可用性，尽管交换机、绑定这些可以复制到集群里的任何一个节点，但是队列内容不会复制。虽然该模式解决一项目组节点压力，但队列节点宕机直接导致该队列无法应用，只能等待重启，所以要想在队列节点宕机或故障也能正常应用，就要复制队列内容到集群里的每个节点，必须要创建镜像队列。
>- 镜像队列是基于普通的集群模式的，然后再添加一些策略，所以你还是得先配置普通集群，然后才能设置镜像队列，我们就以上面的集群接着做。



可以通过控制台设置镜像队列：

![image-20220810091410572](../../md-photo/image-20220810091410572.png)



测试结果：

![image-20220810091659242](../../md-photo/image-20220810091659242.png)



在node1添加一条消息：

![image-20220810091800194](../../md-photo/image-20220810091800194.png)



关闭node1：



![image-20220810091839762](../../md-photo/image-20220810091839762.png)



![image-20220810091854021](../../md-photo/image-20220810091854021.png)



可以看到node2还是能够拿到消息：



![image-20220810091941098](../../md-photo/image-20220810091941098.png)



## haproxy

下载地址：

[Index of /repo/pkgs/haproxy (fedoraproject.org)](https://src.fedoraproject.org/repo/pkgs/haproxy/)



执行安装haproxy的操作

```bash
# 安装gcc编译插件
$ yum -y install gcc automake autoconf libtool make

# 移动到专门管理软件安装的位置
$ mv ~/haproxy-2.6.0.tar.gz /gjmou/software/

# 执行解压的操作
$ tar -xvf haproxy-2.6.0.tar.gz

# 进入到haproxy-2.6.0文件夹下，进行安装的操作
# 使用uname -r查看内核，如：3.10.0-957.el7.x86_64，此时该参数就为linux31
$ cd haproxy-2.6.0
$ make TARGET=linux31 PREFIX=/gjmou/software/haproxy
# 执行安装操作
$ make install PREFIX=/gjmou/software/haproxy


# 创建配置文件夹
$ mkdir -p /gjmou/software/haproxy/config
# 添加配置文件
$ touch /gjmou/software/haproxy/config/haproxy.cfg
# 录入以下的内容

#logging options
global
	log 127.0.0.1 local0 info
	maxconn 5120
	chroot /gjmou/software/haproxy
	uid 99
	gid 99
	daemon # 后台方式运行
	quiet
	pidfile /var/run/haproxy.pid

defaults
	log global
	#使用4层代理模式，”mode http”为7层代理模式
	mode tcp
	#if you set mode to tcp,then you nust change tcplog into httplog
	option tcplog
	option dontlognull
	retries 3
	option redispatch
	maxconn 2000
	timeout connect 10s
    ##客户端空闲超时时间为 60秒 则HA 发起重连机制
    timeout client 10s
    ##服务器端链接超时时间为 15秒 则HA 发起重连机制
    timeout server 10s	
#front-end IP for consumers and producters

listen rabbitmq_cluster #这里是配置负载均衡，rabbitmq_cluster是名字，可以任意
	bind 0.0.0.0:5672 #这里是监听的IP地址和端口，端口号可以在0-65535之间，要避免端口冲突
	#配置TCP模式
	mode tcp
	#balance url_param userid
	#balance url_param session_id check_post 64
	#balance hdr(User-Agent)
	#balance hdr(host)
	#balance hdr(Host) use_domain_only
	#balance rdp-cookie
	#balance leastconn
	#balance source //ip
	#简单的轮询
	balance roundrobin
	#rabbitmq集群节点配置 #inter 每隔五秒对mq集群做健康检查， 2次正确证明服务器可用，2次失败证明服务器不可用，并且配置主备机制
        server rabbit_node1 127.0.0.1:5682 check inter 5000 rise 2 fall 2
        server rabbit_node2 127.0.0.1:5692 check inter 5000 rise 2 fall 2
#配置haproxy web监控，查看统计信息
listen stats
	bind 0.0.0.0:8100
	mode http
	option httplog
	stats enable
	#设置haproxy监控地址为http://localhost:8100/rabbitmq-stats
	stats uri /rabbitmq-stats
	stats refresh 5s
```



开放8100端口，web端查看haproxy的代理情况：

```bash
firewall-cmd --zone=public --add-port=8100/tcp --permanent   
# 更新防火墙
sudo firewall-cmd --reload
```



**<font color='red'>按照镜像队列的启动方式启动两个节点</font>**

启动haproxy负载均衡：

```bash
$ /gjmou/software/haproxy/sbin/haproxy -f /gjmou/software/haproxy/config/haproxy.cfg
```



启动后访问路径：

[Statistics Report for HAProxy](http://rabbitmq1:8100/rabbitmq-stats)

![image-20220810221418082](../../md-photo/image-20220810221418082.png)

## SpringBoot集成测试

因为负载均衡绑定的端口为rabbitmq的默认端口，所以配置信息不需要任何的修改：

![image-20220810222715954](../../md-photo/image-20220810222715954.png)



队列配置：

```java
@Configuration
public class RabbitLBConfig {

    @Bean
    public Queue lbQueue() {
        return QueueBuilder.durable("lb-queue").build();
    }
}
```



测试类：

```java
@Test
public void testLoadBalance() {
    rabbitTemplate.convertAndSend("lb-queue", "hello,this is lb......");
}
```



停止rabbit_node2节点：

```bash
$ rabbitmqctl -n rabbit_node2 stop_app
```

![image-20220810222406160](../../md-photo/image-20220810222406160.png)





可以看到消息发送成功：

![image-20220810222804023](../../md-photo/image-20220810222804023.png)



启动rabbit_node2节点：

```bash
$ rabbitmqctl -n rabbit_node2 start_app
```



可以看到节点2的数据已经同步，保证了集群的高可用：

![image-20220810222955227](../../md-photo/image-20220810222955227.png)





## 集群启动操作Shell脚本

启动：start-cluster.sh

```bash
#!/bin/sh
# 启动rabbit_node1
`RABBITMQ_NODE_PORT=5682 RABBITMQ_SERVER_START_ARGS="-rabbitmq_management listener [{port,15682}]" RABBITMQ_NODENAME=rabbit_node1 rabbitmq-server -detached`;

# 启动rabbit_node2
`RABBITMQ_NODE_PORT=5692 RABBITMQ_SERVER_START_ARGS="-rabbitmq_management listener [{port,15692}]" RABBITMQ_NODENAME=rabbit_node2 rabbitmq-server -detached`;

wait;

# 启动haproxy
`/gjmou/software/haproxy/sbin/haproxy -f /gjmou/software/haproxy/config/haproxy.cfg`;
```



关闭：stop-cluster.sh

```bash
#!/bin/sh
# 关闭rabbitmq
echo "关闭rabbitmq开始..."
pids="`ps -ef | grep "rabbitmq" | awk '{print $2}'`";
for pid in ${pids}
do
 echo  ${pid}
 kill -9 ${pid}
done;

# 关闭haproxy
echo "关闭haproxy开始..."
pids="`ps -ef | grep "haproxy" | awk '{print $2}'`";
for pid in ${pids}
do
 echo  ${pid}
 kill -9 ${pid}
done;
```

