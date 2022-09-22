# vmware配置nat网络

## 一、环境

宿主机：win10

虚拟软件：[VMware](https://so.csdn.net/so/search?q=VMware&spm=1001.2101.3001.7020) Workstation 15 Pro

[虚拟机](https://so.csdn.net/so/search?q=虚拟机&spm=1001.2101.3001.7020)：centos7

提示：阅读本文需要具备一些计算机网络基本知识。

## 二、详细步骤

### 1、设置虚拟机网关

点击导航栏上面的【编辑】–>【虚拟网络编辑器】，并以【管理员】的身份打开虚拟机。
![image](../../md-photo/7bb9a71597a040c0b40dc77186c49dcd.png)
点击【VMnet8 NAT模式】，取消使用本地使用本地dhcp服务，配置网络段（子网ip段）为192.168.200.0，点击NAT设置。
![image](../../md-photo/f71aab9c556c4ac19d80b390510cdc66.png)
配置【网关ip】，注意【网关ip】需要在【子网ip】段下，这里设置为192.168.200.2
请不要设置为192.168.200.1，否则会出错。
![image](../../md-photo/d3728bd7cd96465899b67fdd9ab6691a.png)

### 2、配置虚拟机静态ip

打开命令行，输入【vim /etc/sysconfig/network-scripts/ifcfg-ens33 】，并修改配置文件内容。
![image](../../md-photo/9e292eba65de4ae889857bc92cd35c7d.png)
\#ip
IPADDR=192.168.200.200
NETMASK=255.255.255.0
\#gateway
GATEWAY=192.168.200.2
\#dns
DNS1=192.168.200.2
重启网络服务。【service network restart】
![image](../../md-photo/43172fafc62449fd80fa0706b7da4390.png)

### 3、检测是否配置成功

ping www.baidu.com 网络通畅，配置完成
![image](../../md-photo/3692530988ed43a3bdd5e98fc509c2ad.png)
查看此时的静态ip，就是我们在配置文件里输入的ip地址
![image](../../md-photo/0a9d65915e8f4761b703183ff325fe6f.png)

## 三、可能遇到的问题及解决方法

首先，需要确保上面写的每一步自己都清楚具体含义；其次，网络问题的排查需要有点耐心，因为同一个问题可能有不同的原因导致。

1.NAT模式下，虚拟机设置静态ip的示意图（务必清楚每一个ip地址的含义）
![image](../../md-photo/afc88929a8664989a39a0506719c4887.png)
【更详细的构架图】这里详细解释了虚拟交换机和虚拟网卡的区别

![image](../../md-photo/fb4859fbad674fbc8fcae669c40b9389.png)