# oracle下载与安装

## 下载

[Database Software Downloads | Oracle 中国](https://www.oracle.com/cn/database/technologies/oracle-database-software-downloads.html#19c)

![image-20220911155019098](../../md-photo/image-20220911155019098.png)





下载预安装包：

[Oracle Linux 7 (x86_64) Latest | Oracle, Software. Hardware. Complete.](https://yum.oracle.com/repo/OracleLinux/OL7/latest/x86_64/index.html)

![image-20220911174331438](../../md-photo/image-20220911174331438.png)







上传文件到指定目录：

```bash
# oracle安装包
$ mv ~/oracle-database-ee-19c-1.0-1.x86_64.rpm /gjmou/software/oracle/
# oracle预安装包
$ mv ~/oracle-database-preinstall-19c-1.0-3.el7.x86_64.rpm /gjmou/software/oracle/
# 进入oracle的文件夹
$ cd /gjmou/software/oracle/
```



## 安装oracle

```bash
# 安装oracle预安装包
$ yum -y localinstall oracle-database-preinstall-19c-1.0-3.el7.x86_64.rpm

# 安装oracle
$ yum -y localinstall oracle-database-ee-19c-1.0-1.x86_64.rpm
```



## 配置环境

```bash
# 如果配置过程中内存不足，需要增加虚拟机的内存
$ /etc/init.d/oracledb_ORCLCDB-19c configure

# 重新执行该命令时，需要删除以下两个文件
# $ORACLE_HOME/network/admin/listener.ora
# $ORACLE_HOME/network/admin/tnsnames.ora
```



## 修改root密码

```bash
# 修改root密码
$ passwd
# 修改为123456
```





## 增加配置

```bash
# 切换到oracle用户
$ su - oracle

$ vi .bash_profile
# 在该文档里面追加以下内容
export ORACLE_HOME=/opt/oracle/product/19c/dbhome_1
export PATH=$PATH:/opt/oracle/product/19c/dbhome_1/bin
export ORACLE_SID=ORCLCDB
stty erase ^h
# export NLS_LANG="AMERICAN_AMERICA.UTF8"
export NLS_LANG="SIMPLIFIED CHINESE_CHINA".AL32UTF8

# 使其配置生效
$ source .bash_profile
```



## 开始使用数据库

```bash
# 登录数据库
$ sqlplus / as sysdba

# 查看所有的pdb
sql> show pdbs;
    CON_ID CON_NAME                       OPEN MODE  RESTRICTED
---------- ------------------------------ ---------- ----------
         2 PDB$SEED                       READ ONLY  NO
         3 ORCLPDB1                       READ WRITE NO
```

