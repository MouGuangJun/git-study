# jmeter压力测试

## 下载与安装

下载地址：

[Apache JMeter - Download Apache JMeter](https://jmeter.apache.org/download_jmeter.cgi)

![image-20220729223200245](../../md-photo/image-20220729223200245-16591051247201.png)



<font color='red'>安装要求：Java8+环境</font>

## 初步使用

1.下载至本地后，解压压缩包，点击进入bin文件夹，双击jmeter.bat文件，会启动JMeter GUI工具

2.选择Options | Zoom In调整屏幕比例

3.选择Options --> Choose Language --> Chinese(Simplified)，设置语言为中文简体



1.选择TestPlan，右键-->添加-->线程-->线程组

![image-20220729233326933](../../md-photo/image-20220729233326933-16591088138403.png)



200个线程循环100次进行访问

![image-20220729233602930](../../md-photo/image-20220729233602930.png)



名称：线程组名称，可修改

线程数：即为并发请求数量，可修改

ramp-UI时间（秒）：即为几秒内开启全部线程，可修改

循环次数：循环执行多少次



<font color='red'>ctrl + s：保存</font>

添加Http请求：

![image-20220729234104721](../../md-photo/image-20220729234104721.png)



![image-20220729235803065](../../md-photo/image-20220729235803065.png)



请求名称，可不改

Web服务器信息，网络协议、域名或IP、端口号，可自行修改

接口请求：请求方式、请求路径、编码格式，可自行修改

参数传递：消息体数据存储JSON信息



添加查看结果树：

![image-20220729235646711](../../md-photo/image-20220729235646711.png)





开始执行：

![image-20220729235205039](../../md-photo/image-20220729235205039.png)



查看运行的响应结果：

![image-20220730000114024](../../md-photo/image-20220730000114024.png)