配置方式如下。首先将虚拟机的网络设置成如下桥接模式，桥接模式下可以把虚拟机的网络和主机的当成同等地位，它们是在同一个局域网，所以在配置虚拟机网络时，照着主机的配置下就可以。

![img](../../md-photo/8b408a5e882a4cc8a5135cb0f92d801b.png)

设置好上面的后，我们先用ipconfg /all在windows主机下看下ip信息，这里不用看Vnet1，Vnet8的那些配置，直接看你连的网线或者wifi的那个网络ip信息，我电脑是如下。

![img](../../md-photo/56b64b0ec71b480db2ba2ce0cfd4a17d.png)

 然后我们照着这个配置，把虚拟机上的配置改一下即可，这里只需要把上图中的ipv4地址换一个不一样的就行，我虚拟机上的centos网络配置路径是/etc/sysconfig/network-scripts/ifcfg-ens33。打开这个文件，添加或修改如下配置。最后重启一下虚拟机就可以了（因为上面网络连接方式也修改了，所以最好是重启下虚拟机）。

![img](../../md-photo/2abfb582523c481caed9fd2f44bdd884.png)

如果还是不能连网，试着把dns也配置上试试。也就是在上面的文件中加上

dns1=xxx.xxx.xxx.xxx

dns2=xxx.xxx.xxx.xxx

其中xxx是你在windows上查到的dns地址。