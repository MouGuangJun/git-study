# mysql下载与安装

## 下载

[mysql官网下载](https://www.mysql.com/)

点击DOWNLOADS

![在这里插入图片描述](../../md-photo/3e4b86e1aa1c4e21bd7484731f4b9802.png)



![在这里插入图片描述](../../md-photo/87b914a1e43d4c2c90e4bc0b8e898e2e.png)

这里选择 `Downloads Archives`

![在这里插入图片描述](../../md-photo/133578100c7d4443a49e5244d3d525c6.png)



进入页面，选择 `MySQL Community Server`

![在这里插入图片描述](../../md-photo/05de983ae9d240569f58b179d5b97219.png)

选择操作系统和版本

![在这里插入图片描述](../../md-photo/05de983ae9d240569f58b179d5b97219-16591003312449.png)



## 安装

```bash
# 创建mysql根目录
$ mkdir -p /gjmou/software/mysql

# 移动到指定的目录
$ mv mysql-8.0.28-1.el7.x86_64.rpm-bundle.tar /gjmou/software/mysql/

# 执行解压的操作
$ tar -xvf mysql-8.0.28-1.el7.x86_64.rpm-bundle.tar 
```



可以看到解压后的文件都是 rpm 文件，所以需要用到 `rpm` 包资源管理器相关的指令安装这些 rpm 的安装包

在安装执行 rpm 安装包之前先下载 `openssl-devel` 插件，因为 mysql 里面有些 rpm 的安装依赖于该插件。



下载openssl-devel插件：

```bash
$ yum install openssl-devel
```



安装完该插件之后，`依次`执行以下命令安装这些 rpm 包：

```bash
$ rpm -ivh mysql-community-common-8.0.28-1.el7.x86_64.rpm

$ rpm -ivh mysql-community-client-plugins-8.0.28-1.el7.x86_64.rpm

$ rpm -ivh mysql-community-libs-8.0.28-1.el7.x86_64.rpm

$ rpm -ivh mysql-community-libs-compat-8.0.28-1.el7.x86_64.rpm

$ rpm -ivh mysql-community-devel-8.0.28-1.el7.x86_64.rpm

$ rpm -ivh mysql-community-client-8.0.28-1.el7.x86_64.rpm

$ rpm -ivh mysql-community-icu-data-files-8.0.28-1.el7.x86_64.rpm

$ rpm -ivh mysql-community-server-8.0.28-1.el7.x86_64.rpm
```



如果出现以下错误：

警告：mysql-community-libs-8.0.28-1.el7.x86_64.rpm: 头V4 RSA/SHA256 Signature, 密钥 ID 3a79bd29: NOKEY
错误：依赖检测失败：
        mariadb-libs 被 mysql-community-libs-8.0.28-1.el7.x86_64 取代

执行命令：

```bash
$ rpm -e mariadb-libs --nodeps
```



如果出现以下错误：

/usr/bin/perl 被 mysql-community-server-8.0.28-1.el7.x86_64 需要

mysql-community-icu-data-files = 8.0.28-1.el7 被 mysql-community-server-8.0.28-1.el7.x86_64 需要
net-tools 被 mysql-community-server-8.0.28-1.el7.x86_64 需要
perl(Getopt::Long) 被 mysql-community-server-8.0.28-1.el7.x86_64 需要
perl(strict) 被 mysql-community-server-8.0.28-1.el7.x86_64 需要

执行命令：

```bash
$ yum install net-tools
$ yum install -y perl-Module-Install.noarch
```



在 Linux 中 MySQL 安装好了之后系统会自动的注册一个服务，服务名称叫做 mysqld，所以可以通过以下命令操作 MySQL：

启动 MySQL 服务：<font color="red">`systemctl start mysqld`</font>

重启 MySQL 服务：<font color="red">`systemctl restart mysqld`</font>

关闭 MySQL 服务：<font color="red">`systemctl stop mysqld`</font>



```bash
# 启动mysql服务
systemctl start mysqld

# 查看mysql自动生成的随机密码
cat /var/log/mysqld.log
```

mysql生成的随机密码：

![image-20220718225125362](../../md-photo/image-20220718225125362.png)



连接mysql：

```bash
# 连接 MySQL 
mysql -u root -p
```

连接mysql成功：

![image-20220718225409307](../../md-photo/image-20220718225409307.png)



修改root用户密码：

```sql
ALTER USER 'root'@'localhost' IDENTIFIED BY '123456';
# 出现以下错误
ERROR 1819 (HY000): Your password does not satisfy the current policy requirements

# 先设置一个满足规范的密码
ALTER USER 'root'@'localhost' IDENTIFIED BY 'MouGuangJun_%4s';

# 将密码复杂度校验调整简单类型
set global validate_password.policy = 0;
# 设置密码最少位数限制为 4 位
set global validate_password.length = 4;

# 将密码设置为123456
ALTER USER 'root'@'localhost' IDENTIFIED BY '123456';
```



创建用户与权限分配：

默认的 root 用户只能当前节点localhost访问，是无法远程访问的，我们还需要创建一个新的账户，用于远程访问

法格式：`CREATE USER <用户名> [ IDENTIFIED ] BY [ PASSWORD ] <口令>`

```sql
# mysql 8.0 以下
create user 'remotes'@'%' IDENTIFIED BY '123456';
# mysql 8.0
create user 'remotes'@'%' IDENTIFIED WITH mysql_native_password BY '123456';
```

PS: mysql8.0 的默认密码验证不再是 password 。所以在创建用户时，create user ‘username’@‘%’ identified by ‘password’; 客户端是无法连接服务的，所以在创建用户的时候需要加上 WITH mysql_native_password

创建完用户之后还需要给用户分配权限，这里我将 `remotes` 这个用户分配了所有的权限：

```sql
grant all on *.* to 'remotes'@'%';

# 需要将remotes用户的host修改为全部ip可以访问
use mysql;

update user set host = '%' where user='remotes';

# 使授权立即生效
flush privileges;
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



## 卸载

卸载 MySQL 前需要先停止 MySQL

命令：`systemctl stop mysqld`

停止 MySQL 之后查询 MySQL 的安装文件：`rpm -qa | grep -i mysql`

```bash
$ rpm -qa | grep -i mysql
mysql-community-common-8.0.28-1.el7.x86_64
mysql-community-devel-8.0.28-1.el7.x86_64
mysql-community-client-plugins-8.0.28-1.el7.x86_64
mysql-community-client-8.0.28-1.el7.x86_64
mysql-community-libs-8.0.28-1.el7.x86_64
mysql-community-icu-data-files-8.0.28-1.el7.x86_64
mysql-community-libs-compat-8.0.28-1.el7.x86_64
mysql-community-server-8.0.28-1.el7.x86_64

# 卸载上述查询出来的所有的 MySQL 安装包
rpm -e mysql-community-common-8.0.28-1.el7.x86_64 --nodeps
rpm -e mysql-community-devel-8.0.28-1.el7.x86_64 --nodeps
rpm -e mysql-community-client-plugins-8.0.28-1.el7.x86_64 --nodeps
rpm -e mysql-community-client-8.0.28-1.el7.x86_64 --nodeps
rpm -e mysql-community-libs-8.0.28-1.el7.x86_64 --nodeps
rpm -e mysql-community-icu-data-files-8.0.28-1.el7.x86_64 --nodeps
rpm -e mysql-community-libs-compat-8.0.28-1.el7.x86_64 --nodeps
rpm -e mysql-community-server-8.0.28-1.el7.x86_64 --nodeps

# 删除MySQL的数据存放目录
rm -rf /var/lib/mysql/

# 删除MySQL的配置文件备份
rm -rf /etc/my.cnf.rpmsave
```



## 可视化界面



![image-20220718231736190](../../md-photo/image-20220718231736190.png)

输入以上的内容，远程连接成功！

