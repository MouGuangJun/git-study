# docker基础

## 场景

​	假定您在开发一个尚硅谷的谷粒商城，您使用的是一台笔记本电脑而且您的开发环境具有特定的配置。其他开发人员身处的环境配置也各有不同。您正在形发的应用依赖于您当前的配置且还要依赖于某些配置文件。此外，您的企业还拥有标准化的测试和生产环境，且具有自身的配置和一系列支持文件。您希望尽可能多在本地模拟这些环境而不产生重新创建服务器环境的开销。请问?
​	您要如何确保应用能够在这些环境中运行和通过质量检测?并且在部署过程中不出现令人头疼的版本、配置问题，也无需重新编写代码和讲行故障修复?

​	答案就是使用容器。Docker之所以发展如此迅速，也是因为它对此给出了一个标准化的解决方案---**<font color='red'>系统平滑移植，容器虚拟化技术</font>**。
​	环境配置相当麻烦，换一台机器，就要重来一次，费力费时。很多人想到，能不能从根本上解决问题，软件可以带环境安装﹖也就是说，安装的时候，**<font color='red'>把原始环境一模一样地复制过来。开发人员利用Docker可以消除协作编码时“在我的机器上可正常工作”的问题。</font>**



![image-20220816223238427](../../../md-photo/image-20220816223238427.png)

​	之前在服务器配置一个应用的运行环境，要安装各种软件，就拿尚硅谷电商项目的环境来说，Java/RabitMQ/MySQL/JDBC驱动包等。安装和配置这些东西有多麻烦就不说了，它还不能跨平台。假如我们是在Windows上安装的这些环境，到了Linux又得重新装。况且就算不跨操作系统，换另一台同样操作系统的服务器，**<font color='red'>要移植应用也是非常麻烦的</font>**。

​	传统上认为，软件编码开发测试结束后，所产出的成果即是程序或是能够编译执行的二进制字节码等(Java为例)。而为了让这些程序可以顺利执行，开发团队也得准备完整的部署文件，让运维团队得以部署应用程式，开发需要清楚的告诉运维部署团队，用的全部配置文件+所有软件环境。不过，即便如此，仍然常常发生部署失败的状况。Docker的出现使得**<font color='red'>Docker得以打破过去「程序即应用」的观念。透过镜像(images)将作业系统核心除外，运作应用程式所需要的系统环境，由下而上打包，达到应用程式跨平台间的无缝接轨运作。</font>**



## 简介

Docker 是一个开源的应用容器引擎，基于 [Go 语言](https://www.runoob.com/go/go-tutorial.html) 并遵从 Apache2.0 协议开源。

Docker 可以让开发者打包他们的应用以及依赖包到一个轻量级、可移植的容器中，然后发布到任何流行的 Linux 机器上，也可以实现虚拟化。

容器是完全使用沙箱机制，相互之间不会有任何接口（类似 iPhone 的 app）,更重要的是容器性能开销极低。



Docker的主要目标是“Build，Ship and Run Any App.Anywhere”，也就是通过对应用组件的封装、分发、部署、运行等生命周期的管理，使用户的APP(可以是一个WEB应用或数据库应用等等）及其运行环境能够做到**<font color='red'>“一次镜像，处处运行”。</font>**

![image-20220816223817488](../../../md-photo/image-20220816223817488.png)

<font color='blue'>Linux容器技术的出现就解决了这样一个问题，而Docker就是在它的基础上发展过来的</font>。将应用打成镜像，通过镜像成为运行在Docker容器上面的实例，而Docker容器在任何操作系统上都是一致的，这就实现了跨平台、跨服务器。**<font color='red'>只需要一次配置好环境换到别的机子上就可以一键部署好，大大简化了操作。</font>**



## docker和虚拟机的区别

**虚拟机（virtual machine）**就是带环境安装的一种解决方案。

它可以在一种操作系统里面运行另一种操作系统，比如在Windows10系统里面运行Linux系统CentOS7。应用程序对此毫无感知，因为虚拟机看上去跟真实系统一模一样，而对于底层系统来说，虚拟机就是一个普通文件，不需要了就删掉，对其他部分毫无影响。这类虚拟机完美的运行了另一套系统，能够使应用程序，操作系统和硬件三者之间的逻辑不变。

![image-20220816225108504](../../../md-photo/image-20220816225108504.png)





**Linux容器(Linux Containers，缩写为LXC)**
Linux容器是与系统其他部分隔离开的一系列进程，从另一个镜像运行，并由该镜像提供支持进程所需的全部文件。容器提供的镜像包含了应用的所有依赖项，因而在从开发到测试再到生产的整个过程中，它都具有可移植性和一致性。
**<font color='red'>Linux容器不是模拟一个完整的操作系统而是对进程进行隔离</font>**。有了容器，就可以将软件运行所需的所有资源打包到一个隔离的容器中。**<font color='blue'>容器与虚拟机不同，不需要捆绑一整套操作系统</font>**，只需要软件工作所需的库资源和设置。系统因此而变得高效轻量并保证部署在任何环境中的软件都能始终如一地运行。

![image-20220816225432493](../../../md-photo/image-20220816225432493.png)



## 下载和安装

参见../../软件安装/docker下载与安装.md



## docker三要素

### 镜像(Image)

​	Docker镜像（Image）就是一个<font color='red'>只读的模板</font>。镜像可以用来创建Docker容器，<font color='red'>一个镜像可以创建很多容器</font>。
它也相当于是一个root文件系统。比如官方镜像centos:7就包含了完整的一套centos:7最小系统的root文件系统。相当于容器的“源代码”，<font color='red'>docker镜像文件类似于Java的类模板，而docker容器实例类似于java中new出来的实例对象</font>。

### 容器(container)

1. 从面向对象角度

   Docker利用容器(Container)独立运行的一个或一组应用，应用程序或服务运行在容器里面，容器就类似于一个虚拟化的运行环境，<font color='red'>容器是用镜像创建的运行实例</font>。就像是Java中的类和实例对象一样，镜像是静态的定义，容器是镜像运行时的实体。容器为镜像提供了一个标准的和隔离的运行环境,它可以被启动、开始、停止、删除。每个容器都是相互隔离的、保证安全的平台

2. 从镜像容器角度

   **<font color='red'>可以把容器看做是一个简易版的Linux环境</font>**(包括root用户权限、进程空间、用户空间和网络空间等）和运行在其中的应用程序。

### 仓库(repository)

仓库（Repository）是<font color='red'>集中存放镜像文件的场所</font>。
类似于

> 1. Maven仓库，存放各种jar包的地方;
> 2. github仓库，存放各种git项目的地方;
> 3. Docker公司提供的官方registry被称为Docker Hub，存放各种镜像模板的地方。



仓库分为公开仓库（Public）和私有仓库（Private）两种形式。

<font color='red'>最大的公开仓库是Docker Hub(https://hub.docker.com/)</font>，
存放了数量庞大的镜像供用户下载。国内的公开仓库包括阿里云、网易云等



### 总结

<font color='blue'>需要正确的理解仓库/镜像/容器这几个概念</font>:
	Docker本身是一个容器运行载体或称之为管理引擎。我们把应用程序和配置依赖打包好形成一个可交付的运行环境，这个打包好的运行环境就是image镜像文件。只有通过这个镜像文件才能生成Docker容器实例(类似Java中new出来一个对象)。
	image 文件可以看作是容器的模板。Docker 根据 image文件生成容器的实例。同一个image文件，可以生成多个同时运行的容器实例。
<font color='blue'>镜像文件</font>

* image文件生成的容器实例，本身也是一个文件，称为镜像文件。

<font color='blue'>容器实例</font>

* 一个容器运行一种服务，当我们需要的时候，就可以通过docker客户端创建一个对应的运行实例，也就是我们的容器

<font color='blue'>仓库</font>

* 就是放一堆镜像的地方，我们可以把镜像发布到仓库中，需要的时候再从仓库中拉下来就可以了。





## 常用命令

### 帮助启动类命令

```bash
# 启动docker
$ systemctl start docker
# 停止docker
$ systemctl stop docker
# 重启docker
$ systemctl restart docker
# 查看docker状态
$ systemctl status docker
# 开机启动
$ systemctl enable docker
# 查看docker概要信息
$ docker info
# 查看docker总体帮助文档
$ docker --help
# 查看docker命令帮助文档
$ docker 具体命令 --help
```



### 镜像命令

#### docker images

列出本地主机上的镜像：

```bash
$ docker images
REPOSITORY    TAG       IMAGE ID       CREATED         SIZE
hello-world   latest    feb5d9fea6a5   10 months ago   13.3kB
```



命令选项说明：

-a :列出本地所有的镜像（含历史映像层)

-q :只显示镜像ID。

如：docker images -q



查询结果说明：

| 选项       | 说明             |
| ---------- | ---------------- |
| REPOSITORY | 表示镜像的仓库源 |
| TAG        | 镜像的标签       |
| IMAGE ID   | 镜像ID           |
| CREATED    | 镜像创建时间     |
| SIZE       | 镜像大小         |


​	同一仓库源可以有多个TAG版本，代表这个仓库源的不同个版本，我们使用**<font color='red'>REPOSITORY:TAG来定义不同的镜像</font>**。如果你不指定一个镜像的版本标签，例如你只使用ubuntu，docker将<font color='blue'>默认使用ubuntu:latest镜像【最新版本的镜像】</font>



#### docker search

查询hello-world镜像：

```bash
$ docker search hello-world
```

![image-20220817211447651](../../../md-photo/image-20220817211447651.png)



命令选项说明：

--limit：只列出N个镜像，默认25个，

只查询redis的前五个：docker search --limit 5 redis



查询结果说明：

| 选项        | 说明           |
| ----------- | -------------- |
| NAME        | 镜像名称       |
| DESCRIPTION | 镜像说明       |
| STARS       | 点赞数量       |
| OFFICIAL    | 是否是官方的   |
| AUTOMATED   | 是否是自动构建 |



#### docker pull

命令：

**docker pull镜像名字[:TAG]**

没有TAG就是最新版等价于docker pull 镜像名字:latest

下载镜像：

```bash
# 不带版本号(最新版本)
$ docker pull ubuntu

# 带版本号
$ docker pull redis:6.0.8
```



#### docker system df

查看镜像/容器/数据卷所占的空间：

```bash
$ docker system df
TYPE            TOTAL     ACTIVE    SIZE      RECLAIMABLE
Images          3         1         177MB     177MB (99%)
Containers      2         0         0B        0B
Local Volumes   0         0         0B        0B
Build Cache     0         0         0B        0B
```



#### docker rmi

命令：

**docker rmi某个XXX镜像名字ID**

```bash
# 删除hello-world镜像
$ docker rmi -f feb5d9fea6a5
Untagged: hello-world:latest
Untagged: hello-world@sha256:2498fce14358aa50ead0cc6c19990fc6ff866ce72aeb5546e1d59caac3d0d60f
Deleted: sha256:feb5d9fea6a5e9606aa995e879d862b825965ba48de054caab5ef356dc6b3412

# 删除单个
$ docker rmi -f 镜像ID

# 删除多个
$ docker rmi -f 镜像名1:TAG 镜像名2:TAG

# 删除全部
$ docker rmi -f $(docker images -qa)
```



### 容器命令

#### 下载镜像

<font color='red'>**有镜像才能创建容器**，这是根本前提(下载一个centos或者ubuntu**镜像**演示)</font>

docker pull centos / docker pull ubuntu

本次演示用ubuntu演示



#### 启动命令

**docker run [OPTIONS] IMAGE [COMMAND] [ARG...]**



OPTIONS说明（常用）：

| 参数                        | 作用                                                         |
| --------------------------- | ------------------------------------------------------------ |
| --name                      | "容器新名字"为容器指定一个名称                               |
| -d                          | 后台运行容器并返回容器ID，也即启动守护式容器(后台运行)       |
| <font color='red'>-i</font> | <font color='red'>以交互模式运行容器，通常与t同时使用</font> |
| <font color='red'>-t</font> | <font color='red'>为容器重新分配一个伪输入终端，通常与-i同时使用;也即启动交互式容器(前台有伪终端，等待交互)</font> |
| -P                          | <font color='red'>随机</font>端口映射，大写P                 |
| -p                          | <font color='red'>指定</font>端口映射，小写p                 |



端口映射说明：

| 参数                          | 说明                              |
| ----------------------------- | --------------------------------- |
| -p hostPort:containerPort     | 端口映射-p 8080:80                |
| -p ip:hostPort:containerPort  | 配置监听地址-p 10.0.0.100:8080:80 |
| -p ip::containerPort          | 随机分配端口-p 10.0.0.100::80     |
| -p hostPort:containerPort:udp | 指定协议-p 8080:80:tcp            |
| -p 81:80 -p 443:443           | 指定多个                          |



**启动交互式容器（前台命令行，不作为守护进程运行）**

```bash
# 启动ubuntu
$ docker run -it ubuntu /bin/bash
```

![image-20220817221325148](../../../md-photo/image-20220817221325148.png)

说明：

#使用镜像ubuntu:latest以<font color='red'>交互模式</font>启动一个容器，在容器内执行/bin/bash命令。docker run -it ubuntu /bin/bash
参数说明:

| 参数      | 说明                                                         |
| --------- | ------------------------------------------------------------ |
| -i        | 交互式操作                                                   |
| -t        | 终端                                                         |
| ubuntu    | ubuntu镜像                                                   |
| /bin/bash | 放在镜像名后的是命令，这里我们希望有个交互式Shell，因此用的是/bin/bash |

<font color='red'>**要退出终端，直接输入exit**</font>。



#### 查看正在运行的容器

命令：

**docker ps[OPTIONS]**

OPTIONS说明（常用）：

| 参数 | 说明                                      |
| ---- | ----------------------------------------- |
| -a   | 列出当前所有正在运行的容器+历史上运行过的 |
| -l   | 显示最近创建的容器                        |
| -n   | 显示最近n个创建的容器                     |
| -q   | 只显示容器编号                            |

如：docker ps -a

![image-20220817223403806](../../../md-photo/image-20220817223403806.png)



```bash
# 查看所有正在运行的容器
$ docker ps
CONTAINER ID   IMAGE     COMMAND       CREATED          STATUS          PORTS     NAMES
0df4551525fd   ubuntu    "/bin/bash"   15 seconds ago   Up 15 seconds             busy_einstein
```



可以看到，之前启动ubuntu的时候没有指定实例的names，所以这里随机生成了一个names->**busy_einstein**。

再次使用--name的参数进行启动：

```bash
$ docker run -it --name myu1 ubuntu bash
```



可以看到此时docker中存在两个实例，而且容器的名称是我们指定的名称。

![image-20220817222718328](../../../md-photo/image-20220817222718328.png)



#### 退出容器

**exit**

run进去容器，exit退出，容器停止



**ctrl+p+q**

run进去容器，ctrl+p+q退出，容器不停止



#### 重启已经停止运行的容器

命令：

**docker start 容器ID或者容器名**

如：docker start 4a9c1b8919c6



#### 停止容器

命令：

**docker stop 容器ID或者容器名**

如：docker stop 4a9c1b8919c6



#### 强制停止容器

命令：

**docker kill 容器ID或者容器名**

如：docker kill 4a9c1b8919c6



#### 删除已经停止的容器

**<font color='red'>默认情况下不能删除正在运行的容器，如果要强制删除，需要使用-f参数</font>**

命令：
**docker rm 容器ID或者容器名**

如：docker rm  4a9c1b8919c6



一次性删除多个容器实例

docker rm -f $(docker ps -a -q)

docker ps -a -q | xargs docker rm



#### 高级命令

**<font color='red'>有镜像才能创建容器，这是根本前提(下载一个Redis6.0.8镜像演示)</font>**

##### 启动守护式容器(后台服务器)

在大部分的场景下，我们希望docker的服务是在后台运行的，我们可以过-d指定容器的后台运行模式。

命令：**docker run -d 容器名**，如docker run -d redis:6.0.8



使用镜像ubuntu:latest以后台模式启动一个容器

```bash
$ docker run -d ubuntu
# 使用docker ps -a命令
```

问题：然后docker ps -a进行查看,<font color='blue'>会发现容器已经退出</font>

![image-20220817235412540](../../../md-photo/image-20220817235412540.png)

- 很重要的要说明的一点: <font color='red'>Docker容器后台运行,就必须有一个前台进程</font>。
- 容器运行的命令如果不是那些<font color='red'>一直挂起的命令</font>（比如运行top，tail），就是会自动退出的。
- 这个是docker的机制问题,比如你的web容器,我们以nginx为例，正常情况下，我们配置启动服务只需要启动响应的service即可。例如service nginx start但是,这样做,nginx为后台进程模式运行,就导致docker前台没有运行的应用，这样的容器后台启动后,会立即自杀因为他觉得他没事可做了。所以，最佳的解决方案是,<font color='red'>将你要运行的程序以前台进程的形式运行，常见就是命令行模式，表示我还有交互操作，别中断（**使用 alt + q + p的方式退出**）</font>。



**redis前后台启动演示**

前台模式启动：

```bash
$ docker run -it redis:6.0.8
```

![image-20220818125224684](../../../md-photo/image-20220818125224684.png)



后台守护式启动：

```bash
$ docker run -d redis:6.0.8
```

![image-20220818125522285](../../../md-photo/image-20220818125522285.png)

![image-20220818125701630](../../../md-photo/image-20220818125701630.png)



##### 查看容器日志

命令：**docker logs 容器ID**

```bash
$ docker logs 410a4375c6cb 
```

![image-20220818130019498](../../../md-photo/image-20220818130019498.png)



##### 查看容器内运行的进程

命令：**docker top 容器ID**

```bash
$ docker top 410a4375c6cb
```

![image-20220818222728373](../../../md-photo/image-20220818222728373.png)



##### 查看容器内部细节

命令：**docker inspect 容器ID**

```bash
$ docker inspect 410a4375c6cb
```

![image-20220818222906142](../../../md-photo/image-20220818222906142.png)



##### 进入正在运行的容器并以命令行交互

**docker exec -it 容器ID bashShell**

```bash
# 利用刚才启动（守护模式进程）的redis容器410a4375c6cb进行测试
$ docker exec -it 410a4375c6cb bash
```

![image-20220818224138727](../../../md-photo/image-20220818224138727.png)

**docker attach 容器ID**

```bash
# 利用ubuntu镜像进行测试（redis不能进行操作）
$ docker run -it ubuntu bash

# 使用ctrl + p + q退出终端

# 查看进程
$ docker ps
CONTAINER ID   IMAGE         COMMAND                  CREATED              STATUS              PORTS      NAMES
bd9620066270   redis:6.0.8   "docker-entrypoint.s…"   About a minute ago   Up About a minute   6379/tcp   crazy_colden
30e7978a83ba   ubuntu        "bash"                   About a minute ago   Up About a minute              hopeful_snyder

# 重新连接到进程
$ docker attach 30e7978a83ba

# 退出进程
$ exit

# ubuntu容器停止运行
$ docker ps -a
CONTAINER ID   IMAGE         COMMAND                  CREATED         STATUS                     PORTS     NAMES
bd9620066270   redis:6.0.8   "docker-entrypoint.s…"   7 minutes ago   Exited (0) 5 minutes ago             crazy_colden
30e7978a83ba   ubuntu        "bash"                   7 minutes ago   Exited (0) 7 seconds ago             hopeful_snyder
```

![image-20220818225654000](../../../md-photo/image-20220818225654000.png)



区别：

attach 直接进入容器启动命令的终端，不会启动新的进程用exit退出，<font color='red'>会导致容器的停止</font>。
exec是在容器中打开新的终端，并且可以启动新的进程用exit退出，<font color='red'>不会导致容器的停止</font>。



**<font color='red'>推荐使用docker exec命令，因为退出容器终端，不会导致容器的停止。</font>**



##### 从容器内拷贝文件到主机上

容器→主机

命令：**docker cp 容器ID:容器内路径 目的主机路径**

```bash
$ docker cp 30e7978a83ba:/tmp/a.text ~
```

![image-20220818232138851](../../../md-photo/image-20220818232138851.png)



##### 导入和导出容器

<font color='red'>export导出容器的内容流作为一个tar归档文件[对应import命令]</font>

命令：**docker export 容器ID > 本地路径/文件名.tar**

```bash
$ docker export 30e7978a83ba > ~/local.tar
```

<font color='red'>import 从tar包中的内容创建一新的文件系统再导入为镜像[对应export]</font>

命令：**cat 文件名.tar | docker import -镜像用户/镜像名:镜像版本号**

```bash
$ cat local.tar | docker import - root/ubuntu:latest
```

![image-20220818233650592](../../../md-photo/image-20220818233650592.png)





## 镜像的分层概念

参见：./docker镜像分层原理.md



## docker镜像commit

docker commit提交容器副本使之成为一个新的镜像
命令：**docker commit -m="提交的描述信息" -a="作者" 容器ID 要创建的目标镜像名:[标签名]**

### 案例

#### ubuntu安装vim

```bash
# 启动ubuntu
$ docker run -it ba6acccedd29 bash
# 进入docker内部，发现vim命令不存在
$docker vim a.txt
bash: vim: command not found
# 先更新我们的包管理工具
$docker apt-get update 
# 然后安装我们需要的vim命令
$docker apt-get install vim
# 此时发现可以使用vim命令了
$docker vim a.txt
```

![image-20220821113000901](../../../md-photo/image-20220821113000901.png)



#### commit生成新镜像

```bash
$ docker commit -m "vim command add ok" -a "admin" 3eb73d362829 vim/ubuntu:1.0.1
```

![image-20220821113525457](../../../md-photo/image-20220821113525457.png)

**<font color='red'>总结：可以看出新产生这个镜像是在原有的ubuntu镜像中再叠加了一层可以使用vim命令的镜像，可以结合镜像分层的概念进行理解</font>**



## 将镜像推送到阿里云

选择个人实例：

![image-20220821115653310](../../../md-photo/image-20220821115653310.png)



创建命名空间：

![image-20220821115738744](../../../md-photo/image-20220821115738744.png)

![image-20220821115847401](../../../md-photo/image-20220821115847401.png)



添加镜像仓库：

![image-20220821121456446](../../../md-photo/image-20220821121456446.png)



下一步选择“本地仓库”

![image-20220821121534144](../../../md-photo/image-20220821121534144.png)



使用对应的命令将镜像推送到仓库：

![image-20220821121606787](../../../md-photo/image-20220821121606787.png)



```bash
# 将镜像推送到Registry
# 登录阿里云
$ docker login --username=aliyun5304398942 registry.cn-hangzhou.aliyuncs.com

# 创建仓库推送任务
$ docker tag b0a727a79acb registry.cn-hangzhou.aliyuncs.com/gjmou/custom-ubuntu:1.0.1

# 执行推送操作
$ docker push registry.cn-hangzhou.aliyuncs.com/gjmou/custom-ubuntu:1.0.1

# 删除本地镜像
$ docker rmi -f b0a727a79acb

# 重新从阿里云仓库拉取
$ docker pull registry.cn-hangzhou.aliyuncs.com/gjmou/custom-ubuntu:1.0.1
```

将镜像从本地推送到阿里云：

![image-20220821122147189](../../../md-photo/image-20220821122147189.png)



将阿里云的镜像下载回本地：

![image-20220821122820846](../../../md-photo/image-20220821122820846.png)



查看仓库中的镜像版本信息：

![image-20220821122859317](../../../md-photo/image-20220821122859317.png)



## 将镜像推送到私有库

### 下载镜像docker registry

命令：

```bash
$ docker pull registry
```



### 启动registry容器

命令:

```bash
$ docker run -d -p 5000:5000 -v /gjmou/registry/:/tmp/registry --privileged=true registry
```


默认情况，仓库被创建在容器的/var/lib/registry目录下，建议自行用容器卷映射，方便于宿主机联调

![image-20220821182517973](../../../md-photo/image-20220821182517973.png)



### 在ubuntu中安装ifconfig命令

```bash
# 启动默认的ubuntu容器
$ docker run -it ba6acccedd29 bash
# 安装ifconfig命令
$docker apt-get update
$docker apt-get install net-tools

# 提交生成新镜像
$ docker commit -m "ifconfig command add ok" -a "admin" 61f074158e95 gjmou/ubuntu:1.0.2
```



### 查看私服仓库中存在的镜像

命令：

```bash
$ curl -X GET http://localhost:5000/v2/_catalog 
{"repositories":[]}
```



### 将新镜像修改为符合私服规范的Tag

按照公式:**docker tag 镜像:Tag Host:Port/Repository:Tag**

```bash
# 使用命令docker tag将gjmou/ubuntu:1.0.2这个镜像修改为192.168.239.74:5000/gjmou/ubuntu:1.0.2
$ docker tag gjmou/ubuntu:1.0.2 192.168.239.74:5000/gjmou/ubuntu:1.0.2
```

修改结果：

![image-20220821184353681](../../../md-photo/image-20220821184353681.png)



### 修改配置文件使之支持http

docker默认不允许http方式推送镜像，通过配置选项来取消这个配置：

在/etc/docker/daemon.json中添加以下内容：

```bash
{
	"insecure-registries":["192.168.239.74:5000"]
}
```

![image-20220821215809450](../../../md-photo/image-20220821215809450.png)

**<font color='red'>修改了配置文件，建议重启docker</font>**



### 将带有ifconfig命令的ubuntu推送到本地仓库

命令：

```bash
# 推送到私服上
$ docker push 192.168.239.74:5000/gjmou/ubuntu:1.0.2

# 查看私服上的镜像
$ curl -X GET http://localhost:5000/v2/_catalog 
{"repositories":["gjmou/ubuntu"]}
```



### 从私服中拉回镜像

```bash
# 从私服中拉回镜像
$ docker pull 192.168.239.74:5000/gjmou/ubuntu:1.0.2
```

![image-20220821220435201](../../../md-photo/image-20220821220435201.png)



## docker容器数据卷

**<font color='red'>容器卷记得加入--privileged=true</font>**

Docker挂载主机目录访问如果出现<font color='red'>cannot open directory .: Permission denied</font>

解决办法:在挂载目录后多加一个--privileged=true参数即可

> 原因是CentOS7安全模块会比之前系统版本加强，不安全的会先禁止，所以目录挂载的情况被默认为不安全的行为，在SELinux里面挂载目录被禁止掉了，如果要开启，我们一般使用--privleged=true命令，扩大容器的权限解决挂载目录没有权限的问题，也即使用该参数，container内的root拥有真正的root权限，否则，container内的root只是外部的一个普通用户权限。



### 定义和用处

定义：

**<font color='red'>将docker容器内的数据保存到宿主机的磁盘中</font>**

卷就是目录或文件，存在于一个或多个容器中，由docker挂载到容器，但不属于联合文件系统，因此能够绕过Union File System提供一些用于持续存储或共享数据的特性:
卷的设计目的就是**<font color='red'>数据的持久化</font>**，完全独立于容器的生存周期，因此Docker不会在容器删除时删除其挂载的数据卷



用处：

将运用与运行的环境打包镜像，run后形成容器实例运行，但是我们对数据的要求希望是**<font color='red'>持久化</font>**的
Docker容器产生的数据，如果不备份，那么当容器实例删除后，容器内的数据自然也就没有了。为了能保存数据在docker中我们使用卷。

特点:

- 数据卷可在容器之间共享或重用数据
- 卷中的更改可以直接实时生效，爽
- 数据卷中的更改不会包含在镜像的更新中
- 数据卷的生命周期一直持续到没有容器使用它为止



### 案例

命令：**docker run -it --privileged=true -v /宿主机绝对路径目录:/容器内目录 镜像名**

**<font color='red'>容器和宿主机中的数据是相通的，一侧修改了，另一侧会跟着修改（就算容器停止了，主机修改了对应的内容，容器重启的时候还会同步回来）</font>**

```bash
# 启动docker容器并建立容器和宿主机的目录映射关系
$ docker run -it --privileged=true -v /tmp/host_data:/tmp/docker_data ba6acccedd29
# 在docker容器中创建文件
$docker/tmp/docker_data touch dockerin.txt
# 在宿主机上可以获取
$/tmp/host_data ls
dockerin.txt

# 在宿主机中创建文件
$/tmp/host_data touch hostin.txt
# 在docker容器中可以获得
$docker/tmp/docker_data ls
dockerin.txt  hostin.txt
```



查看数据卷是否挂载成功：

命令：**docker inspect 容器ID**

```bash
$ docker inspect 7a0cf77dc214
```

![image-20220821223933922](../../../md-photo/image-20220821223933922.png)



### 容器读写规则

命令：**docker run -it --privileged=true -v /宿主机绝对路径目录:/容器内目录 读写权限[ro/rw] 镜像名**

其中ro = read only ， rw = read write

如果是ro的话，只能宿主机写文件，docker容器只能读文件，不能进行写的操作。

```bash
$ docker run -it --privileged=true -v /tmp/host_data:/tmp/docker_data:ro ba6acccedd29

# 在宿主机中添加文件
$/tmp/host_data echo "aaa" > a.txt

# 在docker容器中试图修改该文件，发生错误，该文件无法写入
$docker/tmp/docker_data echo "222" > a.txt 
bash: a.txt: Read-only file system
```



### 容器卷的继承和共享

命令：**docker run -it --privileged=true --volumes-from 父类容器ID 镜像名**

```bash
# 启动父类容器
$ docker run -it --privileged=true -v /tmp/host_data:/tmp/docker_data ba6acccedd29   

# 启动子类容器
$ docker run -it --privileged=true --volumes-from 1bc741e88c20 ba6acccedd29  

# 在宿主机中创建文件
$ touch b.txt

# 在父类容器中存在该文件
$docker-super ls
a.txt  b.txt

# 在子类容器中也存在该文件
$docker-this ls
a.txt  b.txt
```

**<font color='red'>此时无论父容器与子容器谁挂掉，都不影响数据卷的挂载，在重启后都能完成数据的同步。完成了数据的共享</font>**



