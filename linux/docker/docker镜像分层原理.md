# docker镜像分层原理

文章参见：

[Docker 进阶之镜像分层详解 - docker查看镜像分层](https://blog.csdn.net/qq_43762191/article/details/125515120)

## 镜像分层的好处

知道了镜像是分层的，那么我们是不是好奇为啥要这么设计呢？
试想一下我们如果不分层会有什么问题？

以拉取镜像为例！
拉取镜像的镜像很大，比如Redis的镜像有100多M
第一次我们拉取6.2版本的Redis，下载了完成的100M到本地，下次我要下载6.2.6版本的，是不是又得下载100M。
尽管可能两个版本之间就改了几行配置文件。

这样是非常低效的。**如果能只下载有差异的部分就好了**！

这个痛点，也就是镜像分层要解决的问题。实际上，Docker也是这么实现的。
第一次下载redis:6.2时，因为之前没有下载过，所以下载了所有的层，总共113M。网络慢点的话还是需要花一些时间的！

![img](../../../md-photo/ac80e20162bfe9a0a138eb92465b8b15.png)



第二次下载redis:7.0-rc，就变得快了很多！因为前面3层是redis:6.2是一样的，这些层已经下载过了！

![img](../../../md-photo/748202d39321e2eac9d65f22d0c3e48e.png)



如果版本2是基于版本1的基础上，那么版本2不需要copy一份全量的数据，只需一份和版本1差异化的增量数据即可！

这样的最终好处是，可以体现在以下方面：

- 拉取**更快**：因为分层了，只需拉取本地不存在的层即可！
- 存储**更少**：因为共同的层只需存储一份即可！
- 运行时存储**更少**：容器运行时可以共享相同的层！

对于第3点，多个基于相同镜像运行的容器，都可以直接使用相同的镜像层，每个容器只需一个自己的可写层即可：

![img](../../../md-photo/fb95d1f4e7ea39f0584dde98d2b5be72.png)



## Docker镜像加载原理

下面这张图想必各位是不陌生了，再往下还有一张。那我们要如何在这么多不陌生的人里面脱颖而出呢？就看谁能把这两张图说出花来了哈。

![image](../../../md-photo/c5156bbcbb5c4daf8b39f91ddad19d39.png)



bootfs(boot file system) 主要包含 bootloader 和 kernel, bootloader 主要是引导加载 kernel，Linux 刚启动时会加载 bootfs 文件系统，在Docker 镜像的最底层是引导文件系统 bootfs。这一层与我们典型的 Linux/Unix 系统是一样的，包含 boot 加载器和内核。当 boot 加载完成之后整个内核就都在内存中了，此时内存的使用权已由 bootfs 转交给内核，此时系统也会卸载 bootfs。

rootfs (root file system) ，在 bootfs 之上。包含的就是典型 Linux 系统中的 /dev, /proc, /bin, /etc 等标准目录和文件。rootfs 就是各种不同的操作系统发行版，比如 Ubuntu，Centos 等等。

对于一个精简的 OS，rootfs 可以很小，只需要包括最基本的命令、工具和程序库就可以了，因为底层直接用 Host 的 kernel，自己只需要提供 rootfs 就行了。由此可见对于不同的 linux 发行版, bootfs 基本是一致的, rootfs 会有差别, 因此不同的发行版可以公用 bootfs。



![image](../../../md-photo/00342a6d461741f0b0b973ba84cc8dd2.png)



**Docker镜像层都是只读的，容器层是可写的**。 当容器启动时，一个新的可写层被加载到镜像的顶部。 这一层通常被称作“容器层”，“容器层”之下的都叫“镜像层”。所有对容器的改动，无论添加、删除、还是修改文件都只会发生在容器层中。

那么，先让我们来重新认识一下与 Docker 镜像相关的 4 个概念：rootfs、Union mount、image 以及 layer。



### rootfs

Rootfs：代表一个 Docker Container 在启动时（而非运行后）其内部进程可见的文件系统视角，或者是 Docker Container 的根目录。当然，该目录下含有 Docker Container 所需要的系统文件、工具、容器文件等。

传统来说，Linux 操作系统内核启动时，内核首先会挂载一个只读（read-only）的 rootfs，当系统检测其完整性之后，决定是否将其切换为读写（read-write）模式，或者最后在 rootfs 之上另行挂载一种文件系统并忽略 rootfs。Docker 架构下，依然沿用 Linux 中 rootfs 的思想。当 Docker Daemon 为 Docker Container 挂载 rootfs 的时候，与传统 Linux 内核类似，将其设定为只读（read-only）模式。在 rootfs 挂载完毕之后，和 Linux 内核不一样的是，Docker Daemon 没有将 Docker Container 的文件系统设为读写（read-write）模式，而是利用 Union mount 的技术，在这个只读的 rootfs 之上再挂载一个读写（read-write）的文件系统，挂载时该读写（read-write）文件系统内空无一物。

![image](../../../md-photo/6123de76f79a4965ba07a484bc69ab10.png)



正如 read-only 和 read-write 的含义那样，该容器中的进程对 rootfs 中的内容只拥有读权限，对于 read-write 读写文件系统中的内容既拥有读权限也拥有写权限。容器虽然只有一个文件系统，但该文件系统由“两层”组成，分别为读写文件系统和只读文件系统。



### Union mount

Union mount：代表一种文件系统挂载的方式，允许同一时刻多种文件系统挂载在一起，并以一种文件系统的形式，呈现多种文件系统内容合并后的目录。

一般情况下，通过某种文件系统挂载内容至挂载点的话，挂载点目录中原先的内容将会被隐藏。而 Union mount 则不会将挂载点目录中的内容隐藏，反而是将挂载点目录中的内容和被挂载的内容合并，并为合并后的内容提供一个统一独立的文件系统视角。通常来讲，被合并的文件系统中只有一个会以读写（read-write）模式挂载，而其他的文件系统的挂载模式均为只读（read-only）。实现这种 Union mount 技术的文件系统一般被称为 Union Filesystem，较为常见的有 UnionFS、AUFS、OverlayFS 等。

Docker 实现容器文件系统 Union mount 时，提供多种具体的文件系统解决方案，如 Docker 早版本沿用至今的的 AUFS，还有在 docker 1.4.0 版本中开始支持的 OverlayFS 等。

![image](../../../md-photo/f0aff972cffb4d24968439206525f139.png)



AUFS 挂载 Ubuntu 文件系统示意图

使用镜像 ubuntu 创建的容器中，可以暂且将该容器整个 rootfs 当成是一个文件系统。上文也提到，挂载时读写（read-write）文件系统中空无一物。既然如此，从用户视角来看，容器内文件系统和 rootfs 完全一样，用户完全可以按照往常习惯，无差别的使用自身视角下文件系统中的所有内容；然而，从内核的角度来看，两者在有着非常大的区别。追溯区别存在的根本原因，那就不得不提及 AUFS 等文件系统的 COW（copy-on-write）特性。

COW 文件系统和其他文件系统最大的区别就是：从不覆写已有文件系统中已有的内容。由于通过 COW 文件系统将两个文件系统（rootfs 和 read-write filesystem）合并，最终用户视角为合并后的含有所有内容的文件系统，然而在 Linux 内核逻辑上依然可以区别两者，那就是用户对原先 rootfs 中的内容拥有只读权限，而对 read-write filesystem 中的内容拥有读写权限。

既然对用户而言，全然不知哪些内容只读，哪些内容可读写，这些信息只有内核在接管，那么假设用户需要更新其视角下的文件 /etc/hosts，而该文件又恰巧是 rootfs 只读文件系统中的内容，内核是否会抛出异常或者驳回用户请求呢？答案是否定的。当此情形发生时，COW 文件系统首先不会覆写 read-only 文件系统中的文件，即不会覆写 rootfs 中 /etc/hosts，其次反而会将该文件拷贝至读写文件系统中，即拷贝至读写文件系统中的 /etc/hosts，最后再对后者进行更新操作。如此一来，纵使 rootfs 与 read-write filesystem 中均由 /etc/hosts，诸如 AUFS 类型的 COW 文件系统也能保证用户视角中只能看到 read-write filesystem 中的 /etc/hosts，即更新后的内容。

当然，这样的特性同样支持 rootfs 中文件的删除等其他操作。例如：用户通过 apt-get 软件包管理工具安装 Golang，所有与 Golang 相关的内容都会被安装在读写文件系统中，而不会安装在 rootfs。此时用户又希望通过 apt-get 软件包管理工具删除所有关于 MySQL 的内容，恰巧这部分内容又都存在于 rootfs 中时，删除操作执行时同样不会删除 rootfs 实际存在的 MySQL，而是在 read-write filesystem 中删除该部分内容，导致最终 rootfs 中的 MySQL 对容器用户不可见，也不可访。

掌握 Docker 中 rootfs 以及 Union mount 的概念之后，再来理解 Docker 镜像，就会变得水到渠成。



### image

Docker 中 rootfs 的概念，起到容器文件系统中基石的作用。对于容器而言，其只读的特性，也是不难理解。神奇的是，实际情况下 Docker 的 rootfs 设计与实现比上文的描述还要精妙不少。

继续以 ubuntu 为例，虽然通过 AUFS 可以实现 rootfs 与 read-write filesystem 的合并，但是考虑到 rootfs 自身接近 200MB 的磁盘大小，如果以这个 rootfs 的粒度来实现容器的创建与迁移等，是否会稍显笨重，同时也会大大降低镜像的灵活性。而且，若用户希望拥有一个 ubuntu 的 rootfs，那么是否有必要创建一个全新的 rootfs，毕竟 ubuntu 和 ubuntu 的 rootfs 中有很多一致的内容。

Docker 中 image 的概念，非常巧妙的解决了以上的问题。最为简单的解释 image，就是 Docker 容器中只读文件系统 rootfs 的一部分。换言之，实际上 Docker 容器的 rootfs 可以由多个 image 来构成。多个 image 构成 rootfs 的方式依然沿用 Union mount 技术。

![image](../../../md-photo/81cb81036c4e4b58b030bdcf7c551c7f.png)



多个 Image 构成 rootfs 的示意图。图中，rootfs 中每一层 image 中的内容划分只为了阐述清楚 rootfs 由多个 image 构成，并不代表实际情况中 rootfs 中的内容划分。

从上图可以看出，举例的容器 rootfs 包含 4 个 image，其中每个 image 中都有一些用户视角文件系统中的一部分内容。4 个 image 处于层叠的关系，除了最底层的 image，每一层的 image 都叠加在另一个 image 之上。另外，每一个 image 均含有一个 image ID，用以唯一的标记该 image。

基于以上的概念，Docker Image 中又抽象出两种概念：Parent Image 以及 Base Image。除了容器 rootfs 最底层的 image，其余 image 都依赖于其底下的一个或多个 image，而 Docker 中将下一层的 image 称为上一层 image 的 Parent Image。imageID_0 是 imageID_1 的 Parent Image，imageID_2 是 imageID_3 的 Parent Image，而 imageID_0 没有 Parent Image。对于最下层的 image，即没有 Parent Image 的镜像，在 Docker 中习惯称之为 Base Image。

通过 image 的形式，原先较为臃肿的 rootfs 被逐渐打散成轻便的多层。Image 除了轻便的特性，同时还有上文提到的只读特性，如此一来，在不同的容器、不同的 rootfs 中 image 完全可以用来复用。

多 image 组织关系与复用关系如图下图（图中镜像名称的举例只为将 image 之间的关系阐述清楚，并不代表实际情况中相应名称 image 之间的关系）：

![image](../../../md-photo/85493db257994cd4ae5442c8c215df5b.png)



多 image 组织关系示意图

图中共罗列了 11 个 image，这 11 个 image 之间的关系呈现一副森林图。森林中含有两棵树，左边树中包含 5 个节点，即含有 5 个 image；右边树中包含 6 个节点，即含有 6 个 image。图中，有些 image 标记了红色字段，意味该 image 代表某一种容器镜像 rootfs 的最上层 image。如图中的 ubuntu:14.04，代表 imageID_3 为该类型容器 rootfs 的最上层，沿着该节点找到树的根节点，可以发现路径上还有 imageID_2，imageID_1 和 imageID_0。特殊的是，imageID_2 作为 imageID_3 的 Parent Image，同时又是容器镜像 ubuntu:12.04 的 rootfs 中的最上层，可见镜像 ubuntu:14.04 只是在镜像 ubuntu:12.04 之上，再另行叠加了一层。因此，在下载镜像 ubuntu:12.04 以及 ubuntu:14.04 时，只会下载一份 imageID_2、imageID_1 和 imageID_0，实现 image 的复用。同时，右边树中 mysql:5.6、mongo:2.2、debian:wheezy 和 debian:jessie 也呈现同样的关系。



### layer

Docker 术语中，layer 是一个与 image 含义较为相近的词。容器镜像的 rootfs 是容器只读的文件系统，rootfs 又是由多个只读的 image 构成。于是，rootfs 中每个只读的 image 都可以称为一层 layer。

除了只读的 image 之外，Docker Daemon 在创建容器时会在容器的 rootfs 之上，再 mount 一层 read-write filesystem，而这一层文件系统，也称为容器的一层 layer，常被称为 top layer。

因此，总结而言，Docker 容器中的每一层只读的 image，以及最上层可读写的文件系统，均被称为 layer。如此一来，layer 的范畴比 image 多了一层，即多包含了最上层的 read-write filesystem。

有了 layer 的概念，大家可以思考这样一个问题：容器文件系统分为只读的 rootfs，以及可读写的 top layer，那么容器运行时若在 top layer 中写入了内容，那这些内容是否可以持久化，并且也被其它容器复用？

上文对于 image 的分析中，提到了 image 有复用的特性，既然如此，再提一个更为大胆的假设：容器的 top layer 是否可以转变为 image？

答案是肯定的。Docker 的设计理念中，top layer 转变为 image 的行为（Docker 中称为 commit 操作），大大释放了容器 rootfs 的灵活性。Docker 的开发者完全可以基于某个镜像创建容器做开发工作，并且无论在开发周期的哪个时间点，都可以对容器进行 commit，将所有 top layer 中的内容打包为一个 image，构成一个新的镜像。Commit 完毕之后，用户完全可以基于新的镜像，进行开发、分发、测试、部署等。不仅 docker commit 的原理如此，基于 Dockerfile 的 docker build，其追核心的思想，也是不断将容器的 top layer 转化为 image。



## Docker 镜像下载

Docker Image 作为 Docker 生态中的精髓，下载过程中需要 Docker 架构中多个组件的协作。Docker 镜像的下载流程如图：

![image](../../../md-photo/415ed442f6b644aeb9fc22a4a27aec04.png)



1、docker client发送镜像的tag到registry。
2、registry根据镜像tag，得到镜像的manifest文件，返回给docker client。
3、docker client拿到manifest文件后，根据其中的config的digest，也就是image ID，检查下镜像在本地是否存在。
4、如果镜像不存在，则下载config文件，并根据config文件中的diff_ids得到镜像每一层解压后的digest。
5、然后根据每层解压后的digest文件，检查本地是否存在，如果不存在，则通过manifest文件中的6、layer的digest下载该层并解压，然后校验解压后digest是否匹配。
7、下载完所有层后，镜像就下载完毕



## 镜像存储

略