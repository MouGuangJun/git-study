# docker下载与安装

## 官网安装步骤查看

查看官网：

https://www.docker.com/

![image-20220817093138331](../../md-photo/image-20220817093138331.png)



划到该位置：

![image-20220817093259948](../../md-photo/image-20220817093259948.png)



在linxu操作系统上的安装手册：

![image-20220817093408122](../../md-photo/image-20220817093408122.png)

![image-20220817093451039](../../md-photo/image-20220817093451039.png)



## 环境准备

Docker 并非是一个通用的容器工具，它依赖于已存在并运行的 Linux 内核环境。

Docker 实质上是在已经运行的 Linux 下制造了一个隔离的文件环境，因此它执行的效率几乎等同于所部署的 Linux 主机。

因此，Docker 必须部署在 Linux 内核的系统上。如果其他系统想部署 Docker 就必须安装一个虚拟 Linux 环境。

![img](../../md-photo/CV09QJMI2fb7L2k0.png)



在 Windows 上部署 Docker 的方法都是先安装一个虚拟机，并在安装 Linux 系统的的虚拟机中运行 Docker。

Docker运行在CentOS7(64-bit)上，要求系统为64位、Linux系统内核版本伪3.8以上

查看linux内核版本

```bash
$ cat /etc/redhat-release
CentOS Linux release 7.6.1810 (Core) 

# 打印当前系统的相关信息（内核版本号、硬件架构、主机名称和操作系统类型等）
$ uname -r
3.10.0-957.el7.x86_64
```



## 开始安装

1. 旧版本的 Docker 被称为docker或docker-engine。如果安装了这些，请卸载它们以及相关的依赖项。**没有安装过可以跳过这步**。

```bash
$ sudo yum remove docker \
                  docker-client \
                  docker-client-latest \
                  docker-common \
                  docker-latest \
                  docker-latest-logrotate \
                  docker-logrotate \
                  docker-engine
```



2. 设置存储库 -安装必须依赖的包

```bash
$ sudo yum install -y yum-utils
```

![image-20220817095853343](../../md-photo/image-20220817095853343.png)



```bash
$ sudo yum-config-manager \
    --add-repo \
    https://download.docker.com/linux/centos/docker-ce.repo
```

![image-20220817100008561](../../md-photo/image-20220817100008561.png)



3. 安装 Docker 引擎

```bash
$ sudo yum install docker-ce docker-ce-cli containerd.io docker-compose-plugin
```

![image-20220817100103893](../../md-photo/image-20220817100103893.png)

发生了如下的错误：

![image-20220817100748330](../../md-photo/image-20220817100748330.png)



这是由于国内访问不到docker官方镜像的缘故，可以通过aliyun的源来完成：

```bash
$ sudo yum-config-manager --add-repo http://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo
```

![image-20220817100930377](../../md-photo/image-20220817100930377.png)



**再次执行安装docker引擎的命令**

遇到如下情况，输入y即可：

![image-20220817101031727](../../md-photo/image-20220817101031727.png)



4. 安装完成后，启动docker

```bash
$ sudo systemctl start docker
# 查看docker版本
$  docker -v
Docker version 20.10.17, build 100c701
```



5. 运行docker的hello-world案例

```bash
$ sudo docker run hello-world
```

![image-20220817111822533](../../md-photo/image-20220817111822533.png)

**docker run发生了什么?**

>1. docker run 镜像时会首先会检查本地是否有该镜像，如果有直接运行。
>2. 如果本地无法查找镜像，则尝试去远程镜像仓库进行下载。
>3. 如果远程镜像仓库有该镜像则拉取（pull）到本地仓库并运行该镜像
>4. 如果远程镜像仓库没有该镜像则直接返回错误信息。

![image](../../md-photo/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBAR2FsZW4tZ2Fv,size_18,color_FFFFFF,t_70,g_se,x_16.png)



6. 查看Docker虚拟机上下载有什么镜像 使用超级管理员 sudo docker images命令

```bash
$ sudo docker images
REPOSITORY   TAG       IMAGE ID   CREATED   SIZE
```



7. 配置阿里云镜像加速器：

控制台：

![image-20220817204439828](../../md-photo/image-20220817204439828.png)



点击左侧菜单：

![image-20220817204516105](../../md-photo/image-20220817204516105.png)



弹性计算：

![image-20220817204609361](../../md-photo/image-20220817204609361.png)



容器镜像服务：

![image-20220817204646027](../../md-photo/image-20220817204646027.png)



地址：

[容器镜像服务](https://cr.console.aliyun.com/cn-hangzhou/instances/mirrors)

![image-20220817102207769](../../md-photo/image-20220817102207769.png)



执行以下的命令：

```bash
$ sudo mkdir -p /etc/docker
$ sudo tee /etc/docker/daemon.json <<-'EOF'
{
  "registry-mirrors": ["https://s4m2e7j6.mirror.aliyuncs.com"]
}
EOF
$ sudo systemctl daemon-reload
$ sudo systemctl restart docker
```



## 开始卸载

```bash
# 关闭docker
$ systemctl stop docker

# 卸载docker服务端和客户端
$ yum remove docker-ce docker-ce-cli containerd.io

# 删除文件
$ rm -rf /var/lib/docker
$ rm -rf /var/lib/containerd
```

