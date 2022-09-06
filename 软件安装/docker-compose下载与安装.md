# docker-compose下载与安装



## 下载

官网地址：

[Overview of Docker Compose | Docker Documentation](https://docs.docker.com/compose/)



查看docker引擎的版本:

```bash
$ docker version
 Version:           20.10.17
 API version:       1.41
 Go version:        go1.17.11
 Git commit:        100c701
 Built:             Mon Jun  6 23:05:12 2022
 OS/Arch:           linux/amd64
 Context:           default
 Experimental:      true

Server: Docker Engine - Community
 Engine:
  Version:          20.10.17
  API version:      1.41 (minimum version 1.12)
  Go version:       go1.17.11
  Git commit:       a89b842
  Built:            Mon Jun  6 23:03:33 2022
  OS/Arch:          linux/amd64
  Experimental:     false
 containerd:
  Version:          1.6.7
  GitCommit:        0197261a30bf81f1ee8e6a4dd2dea0ef95d67ccb
 runc:
  Version:          1.1.3
  GitCommit:        v1.1.3-0-g6724737
 docker-init:
  Version:          0.19.0
  GitCommit:        de40ad0
```



查看compose和docker引擎的版本对应关系：

![image-20220830222335715](../../md-photo/image-20220830222335715.png)





## 开始安装compase

下载最新版的docker-compose文件 ：

```bash
$ sudo curl -L https://github.com/docker/compose/releases/download/1.29.2/docker-compose-`uname -s`-`uname -m` -o /usr/local/bin/docker-compose
```

若是github访问太慢，可以用daocloud下载 ：

```bash
$ sudo curl -L https://get.daocloud.io/docker/compose/releases/download/1.29.2/docker-compose-`uname -s`-`uname -m` -o /usr/local/bin/docker-compose
```

添加可执行权限：

```bash
$ sudo chmod +x /usr/local/bin/docker-compose
```

测试安装结果：

```bash
$ docker-compose --version
docker-compose version 1.29.2, build 5becea4c
```

卸载docker-compose：

```bash
$ sudo rm /usr/local/bin/docker-compose
```



