# mariadb下载与安装

## 下载

官网：

[MariaDB Foundation - MariaDB.org](https://mariadb.org/)



找到文件下载位置：

![image-20220817113857243](../../md-photo/image-20220817113857243.png)



下载服务器：

![image-20220817113927189](../../md-photo/image-20220817113927189.png)



选择稳定运行的版本：

![image-20220817114012637](../../md-photo/image-20220817114012637.png)



选择linux操作系统：

![image-20220817114104785](../../md-photo/image-20220817114104785.png)



## 安装

文件部署：

```bash
# 解压文件
$ tar -xvf mariadb-10.8.4-linux-systemd-x86_64.tar.gz

# 修改文件夹名称
$ mv mariadb-10.8.4-linux-systemd-x86_64 mariadb

# 移动到软件安装的目录下
$ mv mariadb/ /gjmou/software/
```



用户创建：

```bash
#  确认是否有 maria 用户
$ getent passwd mysql

# 创建用户
$ useradd -r -s /sbin/nologin -d /data/mysql mysql

# 创建目录及授权
$ mkdir -p /data/mysql
$ chown mysql.mysql /data/mysql

# 设置目录权限
$ chown -R root.mysql /gjmou/software/mariadb/
```



准备文件：

```bash
# 进入目录，创建软链接
$ cd /gjmou/software

# 不需要创建mysql文件夹，自动进行软链接的操作
$ ln -sv /gjmou/software/mariadb/ /usr/local/mysql
"/usr/local/mysql" -> "/gjmou/software/mariadb/"

# 设置目录权限
$ chown -R root.mysql /usr/local/mysql

# 准备配置文件
$ mkdir /etc/mysql/

$ vi /etc/my.cnf
#添加（或修改）以下内容
datadir = /data/mysql
innodb_file_per_table = on
skip_name_resolve = on
socket=/data/mysql/mysql.sock

# 创建日志文件目录（不创建启动失败）
$ mkdir /var/log/mariadb
$ mkdir /var/run/mariadb
$ chown mysql:mysql /var/log/mariadb
$ chown mysql:mysql /var/run/mariadb
```



安装数据库：

```bash
# 进入mysql文件目录
$ cd /usr/local/mysql
# 需要安装一个软件
$ yum install -y libaio

# 安装数据库
$ ./scripts/mysql_install_db --datadir=/data/mysql --user=mysql

# 准备服务脚本
$ cp support-files/systemd/mariadb.service /usr/lib/systemd/system/mysqld.service

# 设置PATH路径并生效
$ echo 'PATH=/usr/local/mysql/bin:$PATH' >> /etc/profile
$ source /etc/profile

# 创建 /tmp/mysql.sock 软链接
$ ln -s /data/mysql/mysql.sock /tmp/mysql.sock
```



启动服务：

```bash
# 启动服务
$ systemctl enable mysqld.service
$ systemctl start mysqld
```



## 创建远程访问用户

```bash
$ GRANT ALL PRIVILEGES ON *.* TO 'remotes'@'%' IDENTIFIED BY '123456';
# 第一个位置,为数据库, 第二个位置,为表,所以 *.*,表示可以访问任意数据的任意表
# 'remotes'@'%', remotes表示远端登录使用的用户名,%表示允许任意ip登录,可将指定ip替换掉%, remotes与%可以自定义
#IDENTIFIED BY '123456' 登录时的使用的密码,(方便记忆就用了123456,生产环境一定要替换掉)


# 查看用户及绑定的主机地址
sql> select user, host from user;
+-------------+-----------+
| User        | Host      |
+-------------+-----------+
| remotes     | %         |
|             | localhost |
| mariadb.sys | localhost |
| mysql       | localhost |
| root        | localhost |
|             | mariadb1  |
+-------------+-----------+
```



防火墙开放端口号：

```bash
# 查看开放的端口
sudo firewall-cmd --zone=public --list-ports
# 3306 端口对外开放
$ firewall-cmd --zone=public --add-port=3306/tcp --permanent  
# 重启防火墙
$ firewall-cmd --reload
```



## 可视化界面

![image-20220817174016321](../../md-photo/image-20220817174016321.png)

输入以上的内容，远程连接成功！
