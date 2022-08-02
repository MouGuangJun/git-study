# redis下载与安装

## 下载

[Redis](https://redis.io/)

![image-20220717233812486](../../md-photo/image-20220717233812486.png)



![image-20220717233828191](../../md-photo/image-20220717233828191.png)



选择下载的操作系统：

![image-20220717233856612](../../md-photo/image-20220717233856612.png)



## 安装

```bash
# 移动到相应的目录下
$ mv redis-stack-server-6.2.2-v5.rhel7.x86_64.tar.gz /gjmou/software/redis/redis-stack-server-6.2.2-v5.rhel7.x86_64.tar.gz

# 解压
$ tar -xvf redis-stack-server-6.2.2-v5.rhel7.x86_64.tar.gz 

# 重命名文件夹
$ mv redis-stack-server-6.2.2-v5 redis


# 设置环境变量
$ vi /etc/profile
#redis
export REDIS_HOME=/gjmou/software/redis
export PATH=$PATH:${REDIS_HOME}/bin

# 生效环境变量
$ source /etc/profile

# 开放防火墙端口
$ firewall-cmd --zone=public --add-port=6379/tcp --permanent 

# 更新防火墙
sudo firewall-cmd --reload
```



## 图形化界面

添加redis.conf文件：

```bash
# ~~~ WARNING ~~~ If the computer running Redis is directly exposed to the
# internet, binding to all the interfaces is dangerous and will expose the
# instance to everybody on the internet. So by default we uncomment the
# following bind directive, that will force Redis to listen only into
# the IPv4 loopback interface address (this means Redis will be able to
# accept connections only from clients running into the same computer it
# is running).
#
# IF YOU ARE SURE YOU WANT YOUR INSTANCE TO LISTEN TO ALL THE INTERFACES
# JUST COMMENT THE FOLLOWING LINE.
# 如果你确定你的实例监听所有的接口  
# 注释下面这行。  
# ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
bind 0.0.0.0   
```



使用命令：

```bash
$ redis-server redis.conf
```



图形化界面Another Redis Desktop Manager下载地址

https://github.com/qishibo/AnotherRedisDesktopManager/



设置连接信息：

![image-20220718092712784](../../md-photo/image-20220718092712784.png)



连接成功：

![image-20220718092756099](../../md-photo/image-20220718092756099.png)
