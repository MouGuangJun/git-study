# docker高级（上）

## Docker整体架构图

![img](../../../md-photo/3b292df5e0fe9925352c266b0d1303d78db17128.jpeg)



1. 用户是使用 Docker Client 与 Docker Daemon 建立通信，并发送请求给后者。
2.  Docker Daemon 作为 Docker 架构中的主体部分，首先提供 Docker Server 的功能使其可以接受 Docker Client 的请求。
3.  Docker Engine 执行 Docker 内部的一系列工作，每一项工作都是以一个 Job 的形式的存在。
4. Job 的运行过程中，当需要容器镜像时，则从 Docker Registry 中下载镜像，并通过镜像管理驱动 Graphdriver 将下载镜像以 Graph 的形式存储。
5. 当需要为 Docker 创建网络环境时，通过网络管理驱动 Networkdriver 创建并配置 Docker容器网络环境。
6. 当需要限制 Docker 容器运行资源或执行用户指令等操作时，则通过 Execdriver 来完成。
7. Libcontainer 是一项独立的容器管理包，Networkdriver 以及 Execdriver 都是通过 Libcontainer 来实现具体对容器进行的操作。

### Docker Client「发起请求」

1. Docker Client 是 和 Docker Daemon 建立通信的客户端。用户使用的可执行文件为 docker（一个命令行可执行文件），docker 命令使用后接参数的形式来实现一个完整的请求命令（例如：docker images，docker 为命令不可变，images 为参数可变）。
2. Docker Client 可以通过以下三种方式和 Docker Daemon 建立通信：tcp://host:port、unix://pathtosocket 和 fd://socketfd
3. Docker Client 发送容器管理请求后，由 Docker Daemon 接受并处理请求，当 Docker Client 接收到返回的请求相应并简单处理后，Docker Client 一次完整的生命周期就结束了。(一次完整的请求：发送请求→处理请求→返回结果)，与传统的 C/S 架构请求流程并无不同。



### Docker Daemon（后台守护进程）

Docker daemon 架构图

![img](../../../md-photo/3812b31bb051f8196c10a2baea0f17e52f73e749.jpeg)

Docker Server 架构图

![img](../../../md-photo/242dd42a2834349bd1d4aed7f95148c634d3bed2.jpeg)

1. Docker Server 相当于 C/S 架构的服务端。功能为接受并调度分发 Docker Client 发送的请求。接受请求后，Docker Server 通过路由与分发调度，找到相应的 Handler 来执行请求。
2.  在 Docker 的启动过程中，通过包 gorilla/mux 创建了一个 mux.Router 来提供请求的路由功能。在 Golang 中 gorilla/mux 是一个强大的 URL 路由器以及调度分发器。该 mux.Router 中添加了众多的路由项，每一个路由项由 HTTP 请求方法（PUT、POST、GET 或DELETE）、URL、Handler 三部分组成。
3. 创建完 mux.Router 之后，Docker 将 Server 的监听地址以及 mux.Router 作为参数来创建一个 httpSrv=http.Server{}，最终执行 httpSrv.Server() 为请求服务。
4.  在 Docker Server 的服务过程中，Docker Server 在 listener 上接受 Docker Client 的访问请求，并创建一个全新的 goroutine 来服务该请求。在 goroutine 中，首先读取请求内容并做解析工作，接着找到相应的路由项并调用相应的 Handler 来处理该请求，最后 Handler 处理完请求之后回复该请求。



### Docker Engine

1. Docker Engine 是 Docker 架构中的运行引擎，同时也 Docker 运行的核心模块。它扮演 Docker Container 存储仓库的角色，并且通过执行 Job 的方式来操纵管理这些容器。
2. 在 Docker Engine 数据结构的设计与实现过程中，有一个 Handler 对象。该 Handler 对象存储的都是关于众多特定 Job 的 Handler 处理访问。举例说明: Docker Engine 的Handler 对象中有一项为：{“create”: daemon.ContainerCreate,}，则说明当名为”create” 的 Job 在运行时，执行的是 daemon.ContainerCreate 的 Handler。



#### Job

1. 一个 Job 可以认为是 Docker 架构中 Docker Engine 内部最基本的工作执行单元。Docker 可以做的每一项工作，都可以抽象为一个 Job。例如：在容器内部运行一个进程，这是一个 Job；创建一个新的容器，这是一个 Job。Docker Server 的运行过程也是一个 Job，名为 ServeApi。
2. Job 的设计者，把 Job 设计得与 Unix 进程相仿。比如说：Job 有一个名称、有参数、有环境变量、有标准的输入输出、有错误处理，有返回状态等。



### Docker Registry(镜像注册中心)

1. Docker Registry 是一个存储容器镜像的仓库（注册中心），可理解为云端镜像仓库。按 Repository 来分类，docker pull 按照 [repository]:[tag] 来精确定义一个具体的 Image。
2. 在 Docker 的运行过程中，Docker Daemon 会与 Docker Registry 通信，并实现搜索镜像、下载镜像、上传镜像三个功能，这三个功能对应的 Job 名称分别为：“search”、”pull” 与 “push”。
3. Docker Registry 可分为公有仓库（ Docker Hub）和私有仓库。



###  Graph 「Docker 内部数据库」

Graph 架构图

![img](../../../md-photo/a1ec08fa513d26979841d9b76540eff34116d88a.jpeg)



#### Repository

1. 已下载镜像的保管者（包括下载的镜像和通过 Dockerfile 构建的镜像）。
2.  一个 Repository 表示某类镜像的仓库（例如：Ubuntu），同一个 Repository 内的镜像用 Tag 来区分（表示同一类镜像的不同标签或版本）。一个 Registry 包含多个Repository，一个 Repository 包含同类型的多个 Image。
3. 镜像的存储类型有 Aufs、Devicemapper、Btrfs、Vfs等。其中 CentOS 系统 7.x 以下版本使用 Devicemapper 的存储类型。
4.  同时在 Graph 的本地目录中存储有关于每一个的容器镜像具体信息，包含有：该容器镜像的元数据、容器镜像的大小信息、以及该容器镜像所代表的具体 rootfs。



#### GraphDB

1. 已下载容器镜像之间关系的记录者。
2. GraphDB 是一个构建在 SQLite 之上的小型数据库，实现了节点的命名以及节点之间关联关系的记录。



### Driver 「执行部分」

Driver 是 Docker 架构中的驱动模块。通过 Driver 驱动，Docker 可以实现对 Docker 容器执行环境的定制。即 Graph 负责镜像的存储，Driver 负责容器的执行。

#### Graphdriver

Graphdriver 架构图

![img](../../../md-photo/6d81800a19d8bc3e40310332ad30fb16a9d34554.jpeg)

1. Graphdriver 主要用于完成容器镜像的管理，包括存储与获取。
2.  存储：docker pull 下载的镜像由 Graphdriver 存储到本地的指定目录( Graph 中 )。
3. 获取：docker run（create）用镜像来创建容器的时候由 Graphdriver 到本地 Graph中获取镜像。



#### Networkdriver

Networkdriver 架构图

![img](../../../md-photo/b3fb43166d224f4aae2a5958264ccd5a9922d109.jpeg)

Networkdriver 的用途是完成 Docker 容器网络环境的配置，其中包括:

- Docker 启动时为 Docker 环境创建网桥。
- Docker 容器创建时为其创建专属虚拟网卡设备。
- Docker 容器分配IP、端口并与宿主机做端口映射，设置容器防火墙策略等。



#### Execdriver

Execdriver 架构图

![img](../../../md-photo/2934349b033b5bb5bcbf40d219688831b700bcf1.jpeg)

1. Execdriver 作为 Docker 容器的执行驱动，负责创建容器运行命名空间、容器资源使用的统计与限制、容器内部进程的真正运行等。
2. 现在 Execdriver 默认使用 Native 驱动，不依赖于 LXC。



###  Libcontainer 「函数库」

Libcontainer 架构图

![img](../../../md-photo/37d12f2eb9389b50e7489a09b58eb8d5e6116e7d.jpeg)

1. Libcontainer 是 Docker 架构中一个使用 Go 语言设计实现的库，设计初衷是希望该库可以不依靠任何依赖，直接访问内核中与容器相关的 API。
2. Docker 可以直接调用 Libcontainer 来操纵容器的 Namespace、Cgroups、Apparmor、网络设备以及防火墙规则等。
3. Libcontainer 提供了一整套标准的接口来满足上层对容器管理的需求。或者说 Libcontainer 屏蔽了 Docker 上层对容器的直接管理。
4. 

###  Docker Container 「服务交付的最终形式」

Docker Container 架构

![img](../../../md-photo/caef76094b36acaf4d2924325362d01803e99c72.jpeg)



1. Docker Container（ Docker 容器 ）是 Docker 架构中服务交付的最终体现形式。

2. Docker 按照用户的需求与指令，订制相应的 Docker 容器：

   - 用户通过指定容器镜像，使得 Docker 容器可以自定义 rootfs 等文件系统。

   - 用户通过指定计算资源的配额，使得 Docker 容器使用指定的计算资源。

   - 用户通过配置网络及其安全策略，使得 Docker 容器拥有独立且安全的网络环境。

   - 用户通过指定运行的命令，使得 Docker 容器执行指定的工作。



## DockerFile

官网：[Dockerfile reference | Docker Documentation](https://docs.docker.com/engine/reference/builder/)



### 定义

Dockerfile是用来构建Docker镜像的文本文件，是由一条条构建镜像所需的指令和参数构成的脚本。

![image-20220824221407332](../../../md-photo/image-20220824221407332.png)



构建过程：

- 编写Dockerfile文件
- docker build命令构建镜像
- docker run依镜像运行容器实例



### DockerFile解析

DockerFile内容基础知识：

> 每条保留字指令都**<font color='red'>必须为大写字母</font>**且后面要跟随至少一个参数
>
> 指令按照从上到下，顺序执行
>
> #表示注释
>
> 每条指令都会创建一个新的镜像层并对镜像进行提交



Docker执行DockerFile的大致流程：

- docker从基础镜像运行一个容器
- 执行一条指令并对容器作出修改
- 执行类似docker commit的操作提交一个新的镜像层
- docker再基于刚提交的镜像运行一个新容器
- 执行dockerfile中的下一条指令直到所有指令都执行完成



总结：

从应用软件的角度来看，Dockerfile、Docker镜像与Docker容器分别代表软件的三个不同阶段：

- Dockerfile是软件的原材料
- Docker镜像是软件的交付品
- Docker容器则可以认为是软件镜像的运行态，也即依照镜像运行的容器实例

Dockerfile面向开发，Docker镜像成为交付标准，Docker容器则涉及部署与运维，三者缺一不可，合力充当Docker体系的基石。

![image-20220824222658012](../../../md-photo/image-20220824222658012.png)



### DockerFile常用保留字指令

#### FROM

基础镜像，当前新镜像是基于哪个镜像的，指定一个已经存在的镜像作为模板，第一条必须是from



#### MAINTAINER

镜像维护者的姓名和邮箱地址



#### RUN

容器构建时需要运行的命令

shell格式：

```dockerfile
#<命令行命令>等同于，在终端操作的 shell命令。
RUN <命令行命令>
# 如先安装一vim命令：
RUN yum -y install vim
```

exec格式：

```dockerfile
RUN ["可执行文件"，“参数1"，“参数2"]
#例如RUN ./test.php dev offline，等价于:
RUN [ "./test.php", "dev", "offline"]
```

**<font color='red'>RUN是在docker build时运行</font>**



#### EXPOSE

当前容器对外暴露出的端口



#### WORKDIR

指定在创建容器后，终端默认登陆的进来工作目录，一个落脚点（*相当于Windows的桌面，linux的~目录*）



#### USER

指定该镜像以什么样的用户去执行，如果都不指定，默认是root


#### ENV

用来在构建镜像过程中设置环境变量

*ENV MY_PATH /usr/mytest*
这个环境变量可以在后续的任何RUN指令中使用，这就如同在命令前面指定了环境变量前缀一样;也可以在其它指令中直接使用这些环境变量，

比如: *WORKDIR $MY_PATH*



#### ADD

将宿主机目录下的文件拷贝进镜像且会自动处理URL和解压tar压缩包



#### COPY

类似ADD，拷贝文件和目录到镜像中。
将从构建上下文目录中<源路径>的文件/目录复制到新的一层的镜像内的<目标路径>位置

```dockerfile
# <src源路径>:源文件或者源目录
# <dest目标路径>:容器内的指定路径，该路径不用事先建好，路径不存在的话，会自动创建路径
COPY ["src" , "dest"]
```



#### VOLUME

容器数据卷，用于数据保存和持久化工作



#### CMD

指定容器启动后的要干的事情

##### 格式

```dockerfile
# CMD指令的格式和RUN相似，也是两种格式
# shell格式:
CMD <命令>

# exec格式:
CMD ["可执行文件", "参数1", 参数2"...]

# 参数列表格式
CMD ["参数1"，"参数2"...]。在指定了ENTRYPOINT指令后，用CMD指定具体的参数。
```



##### 注意

Dockerfile中可以有多个CMD,指令，**<font color='red'>但只有最后一个生效，CMD会被docker run之后的参数替换</font>**

演示：

```dockerfile
# 可以看到tomcat的dockerfile最后执行了一个CMD命令来启动tomcat
CMD ["catalina.sh", "run"]
```

![image-20220824230709117](../../../md-photo/image-20220824230709117.png)



正常启动tomcat：

```bash
# 正常启动tomcat
$ docker run -d -p 8080:8080 tomcat:latest

# 进入docker容器中
$ 略

# 将webapps.list中的资源放到webapps下
$docker rm -rf webapps
$docker mv webapps.dist/ webapps

# 查看tomcat进程
$docker ps -ef | grep tomcat
root          1      0  6 15:08 ?        00:00:06 /usr/local/openjdk-11/bin/java -Djava.util.logging.config.file=/usr/local/tomcat/conf/logging.properties -Djava.util.logging.manager=org.apache.juli.ClassLoaderLogManager -Djdk.tls.ephemeralDHKeySize=2048 -Djava.protocol.handler.pkgs=org.apache.catalina.webresources -Dorg.apache.catalina.security.SecurityListener.UMASK=0027 -Dignore.endorsed.dirs= -classpath /usr/local/tomcat/bin/bootstrap.jar:/usr/local/tomcat/bin/tomcat-juli.jar -Dcata
```

正常访问到tomcat主页：

![image-20220824231223078](../../../md-photo/image-20220824231223078.png)



覆盖tomcat本身的CMD命令来启动tomcat：

```bash
# 覆盖tomcat本身的CMD命令来启动tomcat
$ docker run -it --name=tomcat-it -p 8080:8080 tomcat:latest bash

# 可以看到并没有tomcat进程
$docker ps -ef | grep tomcat
root          8      1  0 15:16 pts/0    00:00:00 grep tomcat

# 将webapps.list中的资源放到webapps下
$docker rm -rf webapps
$docker mv webapps.dist/ webapps
```

无法访问到tomcat主页：

![image-20220824231756958](../../../md-photo/image-20220824231756958.png)



##### CMD和RUN的区别

CMD是在docker run时运行。

RUN是在 docker build时运行。



#### ENTRYPOINT

也是用来指定一个容器启动时要运行的命令
类似于CMD指令，但是ENTRYPOINT**<font color='red'>不会被docker run后面的命令覆盖</font>**，而且这些命令行参数**<font color='red'>会被当作参数送给ENTRYPOINT指令指定的程序</font>**。



##### 格式

```dockerfile
ENTRYPOINT ["<executeable>", "<param1>", "<param2>", ...]
```

ENTRYPOINT可以和CMD一起用，一般是**<font color='red'>默认给个参数，但在命令行的时候可能会修改时才会使用CMD</font>**，这里的CMD等于是在给ENTRYPOINT传参。
当指定了ENTRYPOINT后，CMD的含义就发生了变化，不再是直接运行其命令而是**<font color='red'>将CMD的内容作为参数传递给ENTRYPOINT指令</font>**，他两个组合
会变成

```dockerfile
ENTRYPOINT "<CMD>"
```



##### 案例

假设已通过Dockerfile构建了<font color='red'>nginx:test镜像</font>：

```dockerfile
FROM nginx
ENTRYPOINT ["nginx", "-c"] #定参
CMD ["/etc/nginx/nginx.conf"] #变参
```



| 是否传参         | 按照dockerfile编写执行         | 传参运行                                     |
| ---------------- | ------------------------------ | -------------------------------------------- |
| Docker命令       | docker run nginx:test          | docker run nginx:test -c /etc/nginx/new.conf |
| 衍生出的实际命令 | nginx -c /etc/nginx/nginx.conf | nginx -c /etc/nginx/new.conf                 |



### DockerFile案例

#### 需求

通过编写DockerFile执行，对centos7安装**<font color='blue'>vim命令 + ifconfig命令 + jdk8</font>**



#### 准备工作

将jdk安装包放到Dockerfile同级目录：

![image-20220825223641304](../../../md-photo/image-20220825223641304.png)



#### 编写DockerFile文件

**<font color='red'>Dockerfile，其中只有D大写（编写规范）</font>**

```bash
# 新建存放DockerFile文件目录
$ mkdir -p /gjmou/dockerfile

# 编写Dockerfile
$  vi Dockerfile
# 输入以下的内容
```

```dockerfile
# Dockerfile中的内容
FROM centos:7
MAINTAINER gjmou<1900794909@qq.com>
ENV MYPATH /usr/local
WORKDIR $MYPATH
#安装vim编辑器
RUN yum -y install vim
#安装ifconfig命令查看网络IP
RUN yum -y install net-tools
#安装java8及lib库
RUN yum -y install glibc.i686
# 开始安装Java
RUN mkdir /usr/local/java
#ADD是相对路径jar,把jdk-8u161-linux-x64.tar.gz添加到容器中,安装包必须要和Dockerfile文件在同一位置
ADD jdk-8u161-linux-x64.tar.gz /usr/local/java/
#配置java环境变量
ENV JAVA_HOME /usr/local/java/jdk1.8.0_161
ENV JRE_HOME $JAVA_HOME/jre
ENV CLASSPATH $JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar:$JRE_HOME/lib:$CLASSPATH
ENV PATH $JAVA_HOME/bin:$PATH
EXPOSE 80

CMD echo $MYPATH
CMD echo "success-------------ok"
CMD /bin/bash
```



#### 构建镜像文件

命令：**docker build -t 新镜像名:TAG .**，**<font color='red'>注意：最后面有一个空格加一个点</font>**

```bash
# 下载centos7镜像
$ docker pull centos:7
# 进入Dockerfile文件所在的目录下
$  cd /gjmou/dockerfile/

# 执行构建镜像的命令
$ docker build -t centos-java8:1.0.1 .
```

构建镜像成功：

![image-20220825225457221](../../../md-photo/image-20220825225457221.png)

构建完成的镜像：

![image-20220825225611411](../../../md-photo/image-20220825225611411.png)



#### 启动镜像容器

```bash
$ docker run -it centos-java8:1.0.1 bash
# 当前工作目录
$docker pwd
/usr/local

# vim命令
$docker vim a.txt
$docker cat a.txt 
this is on a vim command

# ifconfig命令
eth0: flags=4163<UP,BROADCAST,RUNNING,MULTICAST>  mtu 1500
        inet 172.17.0.2  netmask 255.255.0.0  broadcast 172.17.255.255
        ether 02:42:ac:11:00:02  txqueuelen 0  (Ethernet)
        RX packets 8  bytes 656 (656.0 B)
        RX errors 0  dropped 0  overruns 0  frame 0
        TX packets 0  bytes 0 (0.0 B)
        TX errors 0  dropped 0 overruns 0  carrier 0  collisions 0

lo: flags=73<UP,LOOPBACK,RUNNING>  mtu 65536
        inet 127.0.0.1  netmask 255.0.0.0
        loop  txqueuelen 1000  (Local Loopback)
        RX packets 0  bytes 0 (0.0 B)
        RX errors 0  dropped 0  overruns 0  frame 0
        TX packets 0  bytes 0 (0.0 B)
        TX errors 0  dropped 0 overruns 0  carrier 0  collisions 0

# java安装成功
$docker java -version
java version "1.8.0_161"
Java(TM) SE Runtime Environment (build 1.8.0_161-b12)
Java HotSpot(TM) 64-Bit Server VM (build 25.161-b12, mixed mode)
```

容器结果：

![image-20220825225928690](../../../md-photo/image-20220825225928690.png)



## 虚悬镜像

```bash
# 编写Dockerfile
$ vi /gjmou/dockerfile/none/Dockerfile
FROM ubuntu
CMD echo "action is success"

# 错误的执行构建镜像命令(不指定名字和Tag)
$ docker build .
# 成功构建了镜像
Sending build context to Docker daemon  2.048kB
Step 1/2 : FROM ubuntu
 ---> ba6acccedd29
Step 2/2 : CMD echo "action is success"
 ---> Running in b9ad7c871f78
Removing intermediate container b9ad7c871f78
 ---> 9d951a483f5d
Successfully built 9d951a483f5d

$ docker images
```

查看镜像的结果：

![image-20220825231127785](../../../md-photo/image-20220825231127785.png)



查看本地的所有虚悬镜像：

```bash
# 查看本地的所有虚悬镜像
$ docker image ls -f dangling=true
```



删除所有的虚悬镜像：

```bash
# 删除所有的虚悬镜像
$ docker image prune
```



## Docker微服务实战

### 准备工作

随便用maven工具生成一个可以运行的jar包

```bash
# 创建存放jar包的文件
$ mkdir -p /gjmou/docker

# 将spring的jar包文件上传到该位置
$ ls
spring-no-database-0.0.1-SNAPSHOT.jar
```



### 编写Dockerfile文件

```bash
# 创建Dockerfile文件
$ vi /gjmou/docker/Dockerfile
# 录入以下的内容
```

```dockerfile
# 基础镜像使用Java
FROM java:8
# 作者
MAINTAINER gjmou
# VOLUME 指定临时文件目录为/tmp，将宿主机的/local/docker/tmp目录作为其容器卷
VOLUME /local/docker/tmp /tmp
# 将jar包添加到容器中并进行重命名的操作
ADD spring-no-database-0.0.1-SNAPSHOT.jar web_app.jar
# 运行jar包
ENTRYPOINT ["java", "-jar", "/web_app.jar"]
# 暴露8097端口作为服务
EXPOSE 8097
```



### 构建镜像文件

命令：**docker build -f 宿主机中DockerFile文件的绝对路径 -t 新镜像名称[:版本号] .**

```bash
# 创建宿主机容器卷目录
$ mkdir -p /local/docker/tmp
# 构建镜像文件
$ docker build -t web_app:1.0.1 .
```

![image-20220826092102664](../../../md-photo/image-20220826092102664.png)



### 运行应用程序

```bash
# 运行docker镜像文件
$ docker run --name web_app -p 8097:8097 -d web_app:1.0.1 

# 进入docker容器内部
$ docker exec -it web_app bash

# 查看容器中的java进程
$docker ps -ef | grep java
root          1      0 30 01:22 ?        00:00:04 java -jar /web_app.jar
root         39     31  0 01:22 pts/0    00:00:00 grep java
```



### 测试应用程序

可以成功的访问应用程序：

![image-20220826092938774](../../../md-photo/image-20220826092938774.png)





## Docker Network

查看docker的宿主机，可以发现多了docker的相关网络配置：

![image-20220829201501012](../../../md-photo/image-20220829201501012.png)



### 常用命令

**<font color='red'>主要作用：</font>**

- 容器间的互联和通信以及端口映射
- 容器lP变动时候可以通过服务名直接网络通信而不受到影响



#### 查看网络信息

命令：**docker network ls**

```bash
$ docker network ls
NETWORK ID     NAME      DRIVER    SCOPE
3d9b37cee445   bridge    bridge    local
acb3fbf135bf   host      host      local
d687b5d2af0f   none      null      local
```



#### 添加网络配置信息

命令：**docker network create 网络名称**

```bash
$ docker network create aa_network
```



#### 删除网络配置信息

命令：**docker network rm 网络名称**

```bash
$ docker network rm aa_network
```



#### 查看网络数据源信息

命令：**docker network inspect 网络名称**

```bash
$ docker network inspect bridg
```

```json
[
    {
        "Name": "bridge",
        "Id": "3d9b37cee4450cd96973eb95ba947e2301a30ba7db4d7c050664ae7cfb5686bc",
        "Created": "2022-08-29T19:55:03.297314307+08:00",
        "Scope": "local",
        "Driver": "bridge",
        "EnableIPv6": false,
        "IPAM": {
            "Driver": "default",
            "Options": null,
            "Config": [
                {
                    "Subnet": "172.17.0.0/16",
                    "Gateway": "172.17.0.1"
                }
            ]
        },
        "Internal": false,
        "Attachable": false,
        "Ingress": false,
        "ConfigFrom": {
            "Network": ""
        },
        "ConfigOnly": false,
        "Containers": {},
        "Options": {
            "com.docker.network.bridge.default_bridge": "true",
            "com.docker.network.bridge.enable_icc": "true",
            "com.docker.network.bridge.enable_ip_masquerade": "true",
            "com.docker.network.bridge.host_binding_ipv4": "0.0.0.0",
            "com.docker.network.bridge.name": "docker0",
            "com.docker.network.driver.mtu": "1500"
        },
        "Labels": {}
    }
]
```





### 网络模式

#### 总体介绍

| 网络模式  | 简介                                                         |
| --------- | ------------------------------------------------------------ |
| bridge    | 为每一个容器分配、设置IP等，并将容器连接到一个docker0<br />**<font color='red'>虚拟网桥，默认为该模式。</font>** |
| host      | 容器将不会虚拟出自己的网卡，配置自己的IP等，而是使用宿主机的IP和端口。 |
| none      | 容器有独立的Network namespace，但并没有对其进行任何网络设置，如分配veth pair 和网桥连接，IP等。 |
| container | 新创建的容器不会创建自己的网卡和配置自己的IP，而是和一个指定的容器共享IP、端口范围等 |



#### 容器默认IP生产规则

```bash
$ docker run  -it --name u1 ubuntu bash
# ctrl + p + q退出当前容器

# 查看当前的IP配置信息
$ docker inspect | tail -n 20
```

```json
"Networks": {
    "bridge": {
        "IPAMConfig": null,
        "Links": null,
        "Aliases": null,
        "NetworkID": "3d9b37cee4450cd96973eb95ba947e2301a30ba7db4d7c050664ae7cfb5686bc",
        "EndpointID": "01d16cbe4b795637a34ee4dcc44567c7c8d5d8b66523e5d39a4d865337b9e11e",
        "Gateway": "172.17.0.1",
        "IPAddress": "172.17.0.2",
        "IPPrefixLen": 16,
        "IPv6Gateway": "",
        "GlobalIPv6Address": "",
        "GlobalIPv6PrefixLen": 0,
        "MacAddress": "02:42:ac:11:00:02",
        "DriverOpts": null
    }
}
```



以同样的方式启动u2，然后发现u2的ip地址为172.17.0.3，但此时如果删除u2容器，再以同样的方式启动u3容器，**<font color='red'>会发现u3容器的ip也是172.17.0.3</font>**

![image-20220829205646591](../../../md-photo/image-20220829205646591.png)

![image-20220829205737891](../../../md-photo/image-20220829205737891.png)

![image-20220829205814852](../../../md-photo/image-20220829205814852.png)



通过上述案例，我们发现：**<font color='red'>docker容器内部的ip有可能是会发生变化的</font>**。



### 案例说明

#### bridge

##### 描述

Docker服务默认会创建一个docker0网桥（其上有一个docker0内部接口），该桥接网络的名称为docker0，它在<font color='red'>内核层</font>连通了其他的物理或虚拟网卡，这就将所有容器和本地主机都放到<font color='red'>同一个物理网络</font>。Docker默认指定了docker0接口的IP地址和子网掩码，<font color='red'>让主机和容器之间可以通过网桥相互通信</font>。

```bash
#查看bridge 网络的详细信息，并通过grep获取名称项
$ docker network inspect bridge | grep name
 "com.docker.network.bridge.name": "docker0",

# docker创建的物理/虚拟网卡
$ ip address | grep docker
docker0: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500
```



- Docker使用Linux桥接，在宿主机虚拟一个Docker容器网桥(docker0)，Docker启动一个容器时会根据Docker网桥的网段分配给容器一个IP地址，称为Container-IP，同时Docker网桥是每个容器的默认网关。因为在同一宿主机内的容器都接入同一个网桥，这样容器之间就能够通过容器的Container-lP直接通信。

- docker run 的时候，没有指定network的话默认使用的网桥模式就是bridge，使用的就是docker0。在宿主机ifconfg,就可以看到docker0和自己create的network(后面讲)eth0，eth1，eth2……代表网卡一，网卡二，网卡三…… 。lo代表127.0.0.1，即localhost。 inet addr用来表示网卡的IP地址。

- 网桥docker0创建一对对等虚拟设备接口一个叫veth，另一个叫eth0，成对匹配。

  - 整个宿主机的网桥模式都是docker0，类似一个交换机有一堆接口，每个接口叫veth，在本地主机和容器内分别创建一个虚拟接口，并让他们彼此联通（这样一对接口叫veth pair）；
  - 每个容器实例内部也有一块网卡，每个接口叫eth0；
  - docker0上面的每个veth匹配某个容器实例内部的eth0，两两配对，一一匹配。

  

  通过上述，将宿主机上的所有容器都连接到这个内部网络上，两个容器在同一个网络下，会从这个网关下各自拿到分配的ip，此时两个容器的网络是互通的。

图示：

![image-20220829212010488](../../../md-photo/image-20220829212010488.png)



##### 示例

```bash
# 启动端口为8081的tomcat
$ docker run -d --name tomcat81 -p:8081:8080 billygoo/tomcat8-jdk8

# 启动端口为8082的tomcat
$ docker run -d --name tomcat82 -p:8082:8080 billygoo/tomcat8-jdk8

# 查看宿主机的ip，可以看到多了两个网络配置
$ ip address
# veth-28 <==> eht0-27
28: veth1012b09@if27: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500 qdisc noqueue master docker0 state UP group default 
    link/ether 5e:a7:15:a6:3d:d8 brd ff:ff:ff:ff:ff:ff link-netnsid 0
    inet6 fe80::5ca7:15ff:fea6:3dd8/64 scope link 
       valid_lft forever preferred_lft forever
# veth-30 <==> eht0-29
30: vetha9cb580@if29: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500 qdisc noqueue master docker0 state UP group default 
    link/ether 86:cf:26:7d:72:98 brd ff:ff:ff:ff:ff:ff link-netnsid 1
    inet6 fe80::84cf:26ff:fe7d:7298/64 scope link 
       valid_lft forever preferred_lft forever
       
       
# 进入tomcat81容器内部
$ docker exec -it tomcat81 bash
$docker ip addr
# eht0-27 <==> veth-28
27: eth0@if28: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500 qdisc noqueue state UP group default 
    link/ether 02:42:ac:11:00:02 brd ff:ff:ff:ff:ff:ff link-netnsid 0
    inet 172.17.0.2/16 brd 172.17.255.255 scope global eth0
       valid_lft forever preferred_lft forever
       
# 进入tomcat82容器内部
$ docker exec -it tomcat82 bash
$docker ip addr
# eht0-29 <==> veth-30
29: eth0@if30: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500 qdisc noqueue state UP group default 
    link/ether 02:42:ac:11:00:03 brd ff:ff:ff:ff:ff:ff link-netnsid 0
    inet 172.17.0.3/16 brd 172.17.255.255 scope global eth0
       valid_lft forever preferred_lft forever
```

![image-20220829214944465](../../../md-photo/image-20220829214944465.png)

可以看到容器和宿主机之间通过eth0和veth建立关系，并使用docker0进行容器间的相互通信。



#### host

##### 描述

容器将<font  color='red'>不会获得</font>一个独立的Network Namespace，而是和宿主机共用一个Network Namespace。<font color='red'>容器将不会虚拟出自己的网卡而是使用宿主机的IP和端口</font>。

![image-20220829221101277](../../../md-photo/image-20220829221101277.png)



##### 示例

```bash
# 8083端口使用host模式启动tomcat
# $ docker run -d --name tomcat83 -p:8083:8080 --network host billygoo/tomcat8-jdk8
$ docker run -d --name tomcat83 --network host billygoo/tomcat8-jdk8
```

启动的时候会有警告:

*<font color='red'>WARNING: Published ports are discarded when using host network mode</font>*

原因：

docker启动时指定--network=host或-net=host，如果还指定了-p映射端口，那这个时候就会有此警告，并且通过-p设置的参数将不会起到任何作用，端口号会以主机端口号为主，重复时则递增。

此时容器和宿主机没有进行端口映射：

![image-20220829221630230](../../../md-photo/image-20220829221630230.png)

解决:

解决的办法就是使用docker的其他网络模式，例如--network=bridge，这样就可以解决问题，或者直接无视。



```bash
# 查看当前容器的网络配置信息
$ docker inspect tomcat83 | tail -n 20
```

```json
"Networks": {
    "host": {
        "IPAMConfig": null,
        "Links": null,
        "Aliases": null,
        "NetworkID": "acb3fbf135bfb7a3c6a497611ca5b54b2184a8610b48de7d5fd58b0976fe9d83",
        "EndpointID": "fae4a54e12866ed032355f35f456db47d05635f3478a188cd5ab0a62ce99ac86",
        "Gateway": "",
        "IPAddress": "",
        "IPPrefixLen": 0,
        "IPv6Gateway": "",
        "GlobalIPv6Address": "",
        "GlobalIPv6PrefixLen": 0,
        "MacAddress": "",
        "DriverOpts": null
    }
}
```

可以看到IPAddress和Gateway都为空。



此时宿主机的ip配置信息跟容器的ip配置信息一样：

```bash
# 宿主机ip配置信息
$ ip address
1: lo: <LOOPBACK,UP,LOWER_UP> mtu 65536 qdisc noqueue state UNKNOWN group default qlen 1000
    link/loopback 00:00:00:00:00:00 brd 00:00:00:00:00:00
    inet 127.0.0.1/8 scope host lo
       valid_lft forever preferred_lft forever
    inet6 ::1/128 scope host 
       valid_lft forever preferred_lft forever
2: ens33: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500 qdisc pfifo_fast state UP group default qlen 1000
    link/ether 00:0c:29:7a:39:99 brd ff:ff:ff:ff:ff:ff
    inet 192.168.239.74/24 brd 192.168.239.255 scope global noprefixroute ens33
       valid_lft forever preferred_lft forever
    inet6 fe80::3d40:f863:b911:36e7/64 scope link noprefixroute 
       valid_lft forever preferred_lft forever
3: docker0: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500 qdisc noqueue state UP group default 
    link/ether 02:42:b2:f3:aa:36 brd ff:ff:ff:ff:ff:ff
    inet 172.17.0.1/16 brd 172.17.255.255 scope global docker0
       valid_lft forever preferred_lft forever
    inet6 fe80::42:b2ff:fef3:aa36/64 scope link 
       valid_lft forever preferred_lft forever
28: veth1012b09@if27: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500 qdisc noqueue master docker0 state UP group default 
    link/ether 5e:a7:15:a6:3d:d8 brd ff:ff:ff:ff:ff:ff link-netnsid 0
    inet6 fe80::5ca7:15ff:fea6:3dd8/64 scope link 
       valid_lft forever preferred_lft forever
30: vetha9cb580@if29: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500 qdisc noqueue master docker0 state UP group default 
    link/ether 86:cf:26:7d:72:98 brd ff:ff:ff:ff:ff:ff link-netnsid 1
    inet6 fe80::84cf:26ff:fe7d:7298/64 scope link 
       valid_lft forever preferred_lft forever
       
# 容器ip配置信息
# 进入容器内部
$ docker exec -it tomcat83 bash
$docker ip address
1: lo: <LOOPBACK,UP,LOWER_UP> mtu 65536 qdisc noqueue state UNKNOWN group default qlen 1000
    link/loopback 00:00:00:00:00:00 brd 00:00:00:00:00:00
    inet 127.0.0.1/8 scope host lo
       valid_lft forever preferred_lft forever
    inet6 ::1/128 scope host 
       valid_lft forever preferred_lft forever
2: ens33: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500 qdisc pfifo_fast state UP group default qlen 1000
    link/ether 00:0c:29:7a:39:99 brd ff:ff:ff:ff:ff:ff
    inet 192.168.239.74/24 brd 192.168.239.255 scope global noprefixroute ens33
       valid_lft forever preferred_lft forever
    inet6 fe80::3d40:f863:b911:36e7/64 scope link noprefixroute 
       valid_lft forever preferred_lft forever
3: docker0: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500 qdisc noqueue state UP group default 
    link/ether 02:42:b2:f3:aa:36 brd ff:ff:ff:ff:ff:ff
    inet 172.17.0.1/16 brd 172.17.255.255 scope global docker0
       valid_lft forever preferred_lft forever
    inet6 fe80::42:b2ff:fef3:aa36/64 scope link 
       valid_lft forever preferred_lft forever
28: veth1012b09@if27: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500 qdisc noqueue master docker0 state UP group default 
    link/ether 5e:a7:15:a6:3d:d8 brd ff:ff:ff:ff:ff:ff link-netnsid 0
    inet6 fe80::5ca7:15ff:fea6:3dd8/64 scope link 
       valid_lft forever preferred_lft forever
30: vetha9cb580@if29: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500 qdisc noqueue master docker0 state UP group default 
    link/ether 86:cf:26:7d:72:98 brd ff:ff:ff:ff:ff:ff link-netnsid 1
    inet6 fe80::84cf:26ff:fe7d:7298/64 scope link 
       valid_lft forever preferred_lft forever
```



可以看到容器的IP配置信息跟宿主机的一摸一样：

![image-20220829222442079](../../../md-photo/image-20220829222442079.png)



####  none

##### 描述

在none模式下，并不为Docker容器进行任何网络配置。

也就是说，这个Docker容器没有网卡、IP、路由等信息，只有一个lo需要我们自己为Docker容器添加网卡、配置

IP等。



##### 示例

```bash
# 启动docker容器
$ docker run -d --name tomcat84 -p:8084:8080 --network none billygoo/tomcat8-jdk8
# 查看当前容器的网络配置信息
$ docker inspect tomcat84 | tail -n 20
```

```json
"Networks": {
    "none": {
        "IPAMConfig": null,
        "Links": null,
        "Aliases": null,
        "NetworkID": "d687b5d2af0f092a187e476f4be318a13cf654e81b479a82a2a78d6abcb4abd1",
        "EndpointID": "8f19d394ecbbdf913be3ea89d7a6d3f64db1a407c850b7f4896a727fd6dd20e1",
        "Gateway": "",
        "IPAddress": "",
        "IPPrefixLen": 0,
        "IPv6Gateway": "",
        "GlobalIPv6Address": "",
        "GlobalIPv6PrefixLen": 0,
        "MacAddress": "",
        "DriverOpts": null
    }
}
```

可以看到IPAddress和Gateway都为空。



查看容器内的配置信息：

```bash
# 进入容器内部
$ docker exec -it tomcat84 bash

# 查看网络配置信息
$docke ip addr
1: lo: <LOOPBACK,UP,LOWER_UP> mtu 65536 qdisc noqueue state UNKNOWN group default qlen 1000
    link/loopback 00:00:00:00:00:00 brd 00:00:00:00:00:00
    inet 127.0.0.1/8 scope host lo
       valid_lft forever preferred_lft forever
```

可以看到，只有lo配置信息。



#### container

##### 描述

新建的容器和已经存在的一个容器共享一个网络ip配置而不是和宿主机共享。新创建的容器不会创建自己的网卡,

配置自己的IP，而是和一个指定的容器共享IP、端口范围等。同样，两个容器除了网络方面，其他的如文件系统

、进程列表等还是隔离的。

![image-20220829223440027](../../../md-photo/image-20220829223440027.png)



##### 示例

```bash
# 创建两个容器
$ docker run -d --name tomcat85 -p:8085:8080 billygoo/tomcat8-jdk8

# tomcat86容器借用tomcat85容器的网络配置信息
$ docker run -d --name tomcat86 --network container:tomcat85 -p:8086:8080 billygoo/tomcat8-jdk8
```

此时启动tomcat85能够成功，但是启动tomcat86报错：

*<font color='red'>docker: Error response from daemon: conflicting options: port publishing and the container type network mode.</font>*

tomcat86和tomcat85（容器内部）公用同一个ip同一个端口，导致端口冲突，应该换一个默认端口不为8080（或者不需要占用端口）的镜像来启动容器。



使用Alpine进行演示：

**Alpine Linux是一款独立的、非商业的通用Linux发行版，专为追求安全性、简单性和资源效率的用户而设计。可能很多人没听说过这个Linux发行版本，但是经常用Docker的朋友可能都用过，因为他小，简单，安全而著称，所以作为基础镜像是非常好的一个选择，可谓是麻雀虽小但五脏俱全，镜像非常小巧，不到6M的大小，所以特别适合容器打包。**

```bash
# 下载Alpine镜像
$ docker pull alpine
# 启动Alpine1
$ docker run -it --name Alpine1 alpine /bin/sh
# 启动Alpine2
$ docker run -it --name Alpine2 --network container:Alpine1 alpine /bin/sh
```

注意：Alpine使用的shell脚本为**/bin/sh**



查看容器内的配置信息：

```bash
# Alpine1
$docker ip addr
1: lo: <LOOPBACK,UP,LOWER_UP> mtu 65536 qdisc noqueue state UNKNOWN qlen 1000
    link/loopback 00:00:00:00:00:00 brd 00:00:00:00:00:00
    inet 127.0.0.1/8 scope host lo
       valid_lft forever preferred_lft forever
37: eth0@if38: <BROADCAST,MULTICAST,UP,LOWER_UP,M-DOWN> mtu 1500 qdisc noqueue state UP 
    link/ether 02:42:ac:11:00:02 brd ff:ff:ff:ff:ff:ff
    inet 172.17.0.2/16 brd 172.17.255.255 scope global eth0
       valid_lft forever preferred_lft forever
       
# Apline2
$docker ip addr
1: lo: <LOOPBACK,UP,LOWER_UP> mtu 65536 qdisc noqueue state UNKNOWN qlen 1000
    link/loopback 00:00:00:00:00:00 brd 00:00:00:00:00:00
    inet 127.0.0.1/8 scope host lo
       valid_lft forever preferred_lft forever
37: eth0@if38: <BROADCAST,MULTICAST,UP,LOWER_UP,M-DOWN> mtu 1500 qdisc noqueue state UP 
    link/ether 02:42:ac:11:00:02 brd ff:ff:ff:ff:ff:ff
    inet 172.17.0.2/16 brd 172.17.255.255 scope global eth0
       valid_lft forever preferred_lft forever
```

可以看到两个容器公用同一网络配置信息。

![image-20220829225451240](../../../md-photo/image-20220829225451240.png)



如果此时关闭Alpine1容器，可以发现Alpine2的网络配置不能使用了：

关闭Alpine1容器：

![image-20220829225705515](../../../md-photo/image-20220829225705515.png)

Alpine2容器的网络配置状态：

![image-20220829225756182](../../../md-photo/image-20220829225756182.png)





### 自定义网络

#### 解决问题

docker中的两个容器之间可以通过ip地址进行通信。但是常用的桥接模式下，ip可能因为容器重启而发生变化，**<font color='red'>如果此时需要通过容器名称来进行通信，那么此时需要用到自定网络</font>**。



#### 示例

```bash
# 新建自定义网络
$ docker network create zzyy_network

# 创建tomcat81容器
$ docker run -d --name tomcat81 --network zzyy_network -p:8081:8080 billygoo/tomcat8-jdk8
# 创建tomcat82容器
$ docker run -d --name tomcat82 --network zzyy_network -p:8082:8080 billygoo/tomcat8-jdk8

# 进入tomcat81容器
$ docker exec -it tomcat81 bash
# 可以成功ping到tomcat82
$docker ping tomcat82
PING tomcat82 (172.19.0.3) 56(84) bytes of data.
64 bytes from tomcat82.zzyy_network (172.19.0.3): icmp_seq=1 ttl=64 time=0.041 ms
...

# 进入tomcat82容器
$ docker exec -it tomcat82 bash
# 可以成功ping到tomcat81
$docker ping tomcat81
PING tomcat81 (172.19.0.2) 56(84) bytes of data.
64 bytes from tomcat81.zzyy_network (172.19.0.2): icmp_seq=1 ttl=64 time=0.035 ms
...
```



#### 结论

**<font color='red'>自定义网络本身就维护好了主机名和ip的对应关系（ ip和域名都能通）</font>**
