# gitee使用手册

## SSH

设置ssh公钥：

![image-20220913210914833](../../md-photo/image-20220913210914833.png)



git生成的公钥默认文件路径：**C:\Users\19007\.ssh\id_rsa.pub**

![image-20220913211034021](../../md-photo/image-20220913211034021.png)



## 创建仓库

[新建仓库 - Gitee.com](https://gitee.com/projects/new)

![image-20220913210416799](../../md-photo/image-20220913210416799.png)





## 建立连接

```bash
# 建立连接
$ git remote add origin git@gitee.com:guang_jun_mu/vue-cli.git

# 推送内容
$ git push -u origin "master"
```



## vscode

选择需要推送的远程分支：

![image-20220913211542741](../../md-photo/image-20220913211542741.png)

![image-20220913211622019](../../md-photo/image-20220913211622019.png)