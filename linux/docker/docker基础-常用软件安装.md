# docker基础-常用软件安装

## tomcat安装

```bash
# 查询tomcat
$ docker search tomcat
# 下载tomcat
$ docker pull tomcat
# 查看是否已经下载成功
$ docker images
REPOSITORY   TAG       IMAGE ID       CREATED         SIZE
tomcat       latest    fb5657adc892   8 months ago    680MB

# 启动tomcat
# -p 主机端口:docker容器端口
# -P 随机分配主机端口
# -it 前台交互
# -p 后台运行
$ docker run -it -p 8080:8080 fb5657adc892

# 进入tomcat内部
$ docker exec -it 89133f45a3e2 bash
# 删除webapps
$docker rm -rf webapps
# 复制一份出来
$docker mv webapps.dist/ webapps
$docker cp -r webapps/ webapps.dist

# 查看tomcat启动端口号
$ docker ps -a
CONTAINER ID   IMAGE          COMMAND             CREATED          STATUS          PORTS                                         NAMES
89133f45a3e2   fb5657adc892   "catalina.sh run"   11 minutes ago   Up 11 minutes   0.0.0.0:49153->8080/tcp, :::49153->8080/tcp   vibrant_borg
```

此时访问docker:49153，其中docker为宿主机ip地址，可以看到tomcat启动成功：

![image-20220821232855016](../../../md-photo/image-20220821232855016.png)



## mysql安装

```bash
# 查询mysql
$ docker search mysql
# 下载mysql
$ docker pull mysql
# 查看是否已经下载成功
$ docker images
REPOSITORY   TAG       IMAGE ID       CREATED         SIZE
mysql        latest    3218b38490ce   8 months ago    516MB

# 启动mysql
$ docker run -p 3306:3306 -e MYSQL_ROOT_PASSWORD=123456 -d 3218b38490ce

# 连接到mysql终端
$ docker exec -it 44c215b8b811 bash
# 连接mysql(之后的操作跟mysql一致)
$docker mysql -uroot -p
```



使用sqlyog连接mysql：

![image-20220822203610446](../../../md-photo/image-20220822203610446.png)



**备份数据库的数据（使用数据卷）**

**<font color='red'>此时就算容器被删除了，重启容器也可以将数据卷中的数据恢复回来，达到备份文件的效果。</font>**

```bash
# 对mysql容器添加数据卷
$ docker run -d -p 3306:3306 --privileged=true -v /local/mysql/log:/var/log/mysql -v /local/mysql/data:/var/lib/mysql -v /local/mysql/conf:/etc/mysql/conf.d -e MYSQL_ROOT_PASSWORD=123456 3218b38490ce

# 解决中文乱码问题
$ vi /local/mysql/conf/my.cnf
# 输入以下内容
[client]
default_character_set=utf8
[mysqld]
collation_server=utf8_general_ci
character_set_server=utf8

# 重启mysql容器
$ docker restart 8eee716b0277

# 连接mysql
$docker mysql -uroot -p
# 可以看到mysql的字符集已经修改为utf8
$docker>mysql show variables like '%character%';
+--------------------------+--------------------------------+
| Variable_name            | Value                          |
+--------------------------+--------------------------------+
| character_set_client     | utf8mb3                        |
| character_set_connection | utf8mb3                        |
| character_set_database   | utf8mb3                        |
| character_set_filesystem | binary                         |
| character_set_results    | utf8mb3                        |
| character_set_server     | utf8mb3                        |
| character_set_system     | utf8mb3                        |
| character_sets_dir       | /usr/share/mysql-8.0/charsets/ |
+--------------------------+--------------------------------+
```



## 安装redis

### 基础命令

```bash
# 查询redis
$ 略
# 下载redis
$ 略
# 查看是否已经下载成功
$ 略

# 启动redis
$ docker run -d -p 6379:6379 16ecd2772934

# 进入redis内部
$ docker exec -it 9026d99cf5d6 bash
# 连接redis客户端
$docker redis-cli
```



### redis配置

引入redis原生配置文件，并修改以下部分的内容：

```bash
# 允许redis监听所有的端口，注释该部分内容
# bind 127.0.0.1

# 关闭保护模式
protected-mode no
# 以守护进程的方式执行
daemonize no
```

将修改后的redis.conf放到/local/redis/conf目录下。



### 指定容器卷

```bash
$ docker run -d -p 6379:6379 --privileged=true -v /local/redis/conf/redis.conf:/etc/redis/redis.conf -v /local/redis/data:/data 16ecd2772934 redis-server /etc/redis/redis.conf

# 启动后修改databases配置为10，然后重启redis容器，发现使用select 15会报错，证明redis使用了/local/redis/conf/redis.conf该路径下的配置进行启动

# 退出redis时，发现redis的dump.rdb文件保存到了/local/redis/data目录下，实现了数据的备份
```

