# Git使用手册

git密钥：https://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@github.com/MouGuangJun/[项目名].git

## 创建仓库

### 初始化仓库

<font color=>**git init** 命令用于在目录中创建新的 Git 仓库。

在目录中执行 **git init** 就可以创建一个 Git 仓库了。

例如我们在当前目录下创建一个名为 runoob 的项目：

```bash
$ mkdir runoob

$ cd runoob/

$ git init
Initialized empty Git repository in D:/git/runoob/.git/
# 初始化空 Git 仓库完毕。
```

现在你可以看到在你的项目中生成了 **.git** 这个子目录，这就是你的 Git 仓库了，所有有关你的此项目的快照数据都存放在这里。

**.git** 默认是隐藏的，可以用 ls -a 命令查看：

```bash
$ ls -a
./  ../  .git/
```



### 下载项目

**git clone** 拷贝一个 Git 仓库到本地，让自己能够查看该项目，或者进行修改。

拷贝项目命令格式如下：

```bash
 git clone [url]
```

**[url]** 是你要拷贝的项目。

例如我们拷贝 Github 上的项目：

```bash
$ git clone https://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@github.com/MouGuangJun/git-study.git
Cloning into 'git-study'...
remote: Enumerating objects: 6, done.
remote: Counting objects: 100% (6/6), done.
remote: Compressing objects: 100% (2/2), done.
remote: Total 6 (delta 0), reused 6 (delta 0), pack-reused 0
Receiving objects: 100% (6/6), done.
```

默认情况下，Git 会按照你提供的 URL 所指向的项目的名称创建你的本地项目目录。 通常就是该 URL 最后一个 / 之后的项目名称。如果你想要一个不一样的名字， 你可以在该命令后加上你想要的名称。

例如，以下实例拷贝远程 git 项目，本地项目名为 **git**：

```bash
$ git clone https://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@github.com/MouGuangJun/git-study.git git
Cloning into 'git'...
remote: Enumerating objects: 6, done.
remote: Counting objects: 100% (6/6), done.
remote: Compressing objects: 100% (2/2), done.
remote: Total 6 (delta 0), reused 6 (delta 0), pack-reused 0
Receiving objects: 100% (6/6), done.
```



## 提交与修改

Git 的工作就是创建和保存你的项目的快照及与之后的快照进行对比。

下面列出了有关创建与提交你的项目的快照的命令：

### git add

<font color="green">#添加文件到暂存区</font>

**git add** 命令可将该文件添加到暂存区。

添加一个或多个文件到暂存区：

```bash
git add [file1] [file2] ...
```

添加指定目录到暂存区，包括子目录：

```bash
git add [dir]
```

添加当前目录下的所有文件到暂存区：

```bash
git add .
```

以下实例我们添加两个文件：

```bash
$ mkdir first-commit #创建文件夹

$ cd first-commit/

$ touch helloworld.java #创建文件

$ touch helloworld.class #创建文件

$ git status -s #查看当前git状态
?? first-commit/
```

接下来我们执行 git add 命令来添加文件：

```bash
$ git add .

$ git status -s
A  first-commit/helloworld.class
A  first-commit/helloworld.java
```

现在我们修改 helloworld.java文件：

在  helloworld.java添加以下内容：

```java
public class helloworld {
    public static void main(String[] args) {
        System.out.println("Hello, World!");
    }
}
```

然后保存退出。

再执行一下 git status：

```bash
$ git status -s
A  helloworld.class
AM helloworld.java
```

**AM** 状态的意思是这个文件在我们将它添加到缓存之后又有改动。改动后我们再执行 **git add .** 命令将其添加到缓存中：

```bash
$ git add first-commit/helloworld.java
warning: LF will be replaced by CRLF in first-commit/helloworld.java.
The file will have its original line endings in your working directory

$ git status -s #查看状态
A  first-commit/helloworld.class
A  first-commit/helloworld.java
```

文件修改后，我们一般都需要进行 git add 操作，从而保存历史版本。

### git status

<font color="green">#查看仓库当前的状态，显示有变更的文件</font>

```bash
$ git status
On branch main
Your branch is up to date with 'origin/main'.

Changes to be committed:
  (use "git restore --staged <file>..." to unstage)
        new file:   first-commit/helloworld.class
        new file:   first-commit/helloworld.java
```

通常我们使用 **-s** 参数来获得简短的输出结果：

```bash
$ git status -s
AM first-commit/helloworld.class
A  first-commit/helloworld.java
```

**AM** 状态的意思是这个文件在我们将它添加到缓存之后又有改动。

### git commit

<font color="green">#提交暂存区到本地仓库。</font>

前面章节我们使用 git add 命令将内容写入暂存区。

git commit 命令将暂存区内容添加到本地仓库中。

提交暂存区到本地仓库中:

```bash
git commit -m [message]
```

[message] 可以是一些备注信息。

提交暂存区的指定文件到仓库区：

```bash
$ git commit [file1] [file2] ... -m [message]
```

**-a** 参数设置修改文件后不需要执行 git add 命令，直接来提交

```bash
$ git commit -a
```

设置提交代码时的用户信息

开始前我们需要先设置提交的用户信息，包括用户名和邮箱：

```bash
$ git config --global user.name 'runoob'
$ git config --global user.email test@runoob.com
```

如果去掉 --global 参数只对当前仓库有效。

<font color="red">**提交修改**</font>

接下来我们就可以对 SringM.java 的所有改动从暂存区内容添加到本地仓库中。

以下实例，我们使用 -m 选项以在命令行中提供提交注释。

```bash
$ touch SringM.java #添加文件
$ git add SringM.java #添加到暂存区

$ git commit -m "第一次版本提交" #提交到git本地仓库
[main 6dd14bd] 第一次版本提交
 3 files changed, 5 insertions(+)
 create mode 100644 first-commit/SringM.java
 create mode 100644 first-commit/helloworld.class
 create mode 100644 first-commit/helloworld.java
```

现在我们已经记录了快照。如果我们再执行 git status:

```bash
$ git status
On branch main
Your branch is ahead of 'origin/main' by 2 commits.
  (use "git push" to publish your local commits)

nothing to commit, working tree clean
```

以上输出说明我们在最近一次提交之后，没有做任何改动，是一个 "working directory clean"，翻译过来就是干净的工作目录。

如果你没有设置 -m 选项，Git 会尝试为你打开一个编辑器以填写提交信息。 如果 Git 在你对它的配置中找不到相关信息，默认会打开 vim。屏幕会像这样：

```bash

# Please enter the commit message for your changes. Lines starting
# with '#' will be ignored, and an empty message aborts the commit.
#
# On branch main
# Your branch is ahead of 'origin/main' by 1 commit.
#   (use "git push" to publish your local commits)
#
# Changes to be committed:
#       modified:   helloworld.class
#
# Untracked files:
#       StringM.java
#
```

如果你觉得 git add 提交缓存的流程太过繁琐，Git 也允许你用 -a 选项跳过这一步。命令格式如下：

我们先修改 hello.php 文件为以下内容：

```java
String a = "s";
return null != a && !"".equals(a);
this is a message;
are you good 马来西亚;
```

再执行以下命令：

```bash
$ git commit -am "提交Sring.java文件"
warning: LF will be replaced by CRLF in first-commit/SringM.java.
The file will have its original line endings in your working directory
[main 7ac819b] 提交Sring.java文件
 1 file changed, 2 insertions(+)
```

### git diff

<font color="green">#比较文件的不同，即暂存区和工作区的差异。</font>

git diff 命令比较文件的不同，即比较文件在暂存区和工作区的差异。

git diff 命令显示已写入暂存区和已经被修改但尚未写入暂存区文件的区别。

git diff 有两个主要的应用场景。

- 尚未缓存的改动：**git diff**
- 查看已缓存的改动： **git diff --cached**
- 查看已缓存的与未缓存的所有改动：**git diff HEAD**
- 显示摘要而非整个 diff：**git diff --stat**

显示暂存区和工作区的差异:

```bash
$ git diff [file]
```

例子：

```bash
$ touch diff.java #创建文件
#添加以下内容：
this is first line;
$ git add diff.java #添加到暂存区
#修改文件的内容：
this is first line;
this is second line;

$ git diff diff.java
warning: LF will be replaced by CRLF in first-commit/diff.java.
The file will have its original line endings in your working directory
diff --git a/first-commit/diff.java b/first-commit/diff.java
index 3ae5cc3..dc63fa9 100644
--- a/first-commit/diff.java
+++ b/first-commit/diff.java
@@ -1 +1,2 @@
 this is first line;
+this is second line;
```

显示暂存区和上一次提交(commit)的差异:

```bash
$ git diff --cached [file]
或
$ git diff --staged [file]
```

例子：

```bash
$ git diff --staged/$ git diff --cached
diff --git a/first-commit/diff.java b/first-commit/diff.java
new file mode 100644
index 0000000..3ae5cc3
--- /dev/null
+++ b/first-commit/diff.java
@@ -0,0 +1 @@
+this is first line;
```

显示两次提交之间的差异:

```bash
$ git diff [first-branch]...[second-branch]
```

例子：

```bash
$ git diff 93398396bd212132e3d3b17ade9479ac23b582ba 7ac819b4802375703870f1586f803ca9975b9fe4
diff --git a/first-commit/SringM.java b/first-commit/SringM.java
index 34c271f..d738215 100644
--- a/first-commit/SringM.java
+++ b/first-commit/SringM.java
@@ -1,2 +1,4 @@
 String a = "s";
 return null != a && !"".equals(a);
+this is a message;
+are you good 马来西亚;
```

### git reset

<font color="green"> 回退版本。</font>

git reset 命令用于回退版本，可以指定退回某一次提交的版本。

git reset 命令语法格式如下：

```bash
git reset [--soft | --mixed | --hard] [HEAD]
```

**--soft参数**：仅仅移动本地库`HEAD`指针

**--mixed参数[默认]**：移动本地库`HEAD`指针，重置暂存区

 **--hard参数**：移动本地库`HEAD`指针，重置暂存区，重置工作区[<font color="gold">会对工作区中的物理文件进行操作</font>]

实例：

<font color="red"> 回退所有内容到上一个版本：**git reset HEAD^**</font> 

```bash
$ vim reset.java #新增reset.java文件
#输入以下内容：
this is a reset file!

$ git add reset.java #添加到暂存区
warning: LF will be replaced by CRLF in first-commit/reset.java.
The file will have its original line endings in your working directory

$ git commit -m "提交reset.java文件" #提交到本地仓库
[main fa7854c] 提交reset.java文件
 1 file changed, 1 insertion(+)
 create mode 100644 first-commit/reset.java
 
$ git diff fa7854c57a8c9a2c9a497aa0f74f8d6a27b03311
$ 65679154a0fbb2af1e5b318102a652ea862a3f6a #查看刚才提交的版本与上个版本之间的区别
diff --git a/first-commit/reset.java b/first-commit/reset.java
deleted file mode 100644
index 8976447..0000000
--- a/first-commit/reset.java
+++ /dev/null
@@ -1 +0,0 @@
-this is a reset file!

$ git reset HEAD^ #回退到上个版本
$ git log #查看记录发现刚才提交的内容被撤回了
commit 65679154a0fbb2af1e5b318102a652ea862a3f6a (HEAD -> main)
Author: MouGuangJun <1900794909@qq.com>
Date:   Sat Jun 18 12:03:07 2022 +0800

    新一轮的全部提交
```

<font color="red"> 回退指定文件到上一个版本：**git reset HEAD^ [file]**</font> 

```bash
vim reset.java #修改reset.java文件
#修改为以下的内容
this is a reset file!
this is after commit line;

$ git commit -am "这次提交的reset.java文件需要回退" #执行提交的操作
warning: LF will be replaced by CRLF in first-commit/reset.java.
The file will have its original line endings in your working directory
[main 4b1a26a] 这次提交的reset.java文件需要回退
 1 file changed, 1 insertion(+)
 
$ git reset HEAD^ reset.java #回退reset.java文件
Unstaged changes after reset:
M       first-commit/reset.java

$ git ls-files -s #查看暂存区中文件
100644 d738215599aa93c9808562680e33b282e9f092a0 0       SringM.java
100644 e69de29bb2d1d6434b8b29ae775ad8c2e48c5391 0       StringM.java
100644 ba88c777fc64fd2d599dabd2ad3a88f2488e1d98 0       diff.java
100644 f13ddea1772915a71f1d739ca4f828da6a31a84b 0       helloworld.class
100644 0a4c6d1126d35fdd9dbf761bb0018a0de4014424 0       helloworld.java
100644 89764475acfc373216f87937a8cda206950eba07 0       reset.java

$ git cat-file -p 89764475acfc373216f87937a8cda206950eba07
this is a reset file! #版本已经被回退
```

<font color="red"> 回退到指定版本：**git reset [version]**</font> 

```bash
$ vim reset-all.java #添加测试文件
#写入以下的内容：
all reset to order version!

$ git add . #添加到暂存区
warning: LF will be replaced by CRLF in first-commit/reset-all.java.
The file will have its original line endings in your working directory

$ git commit -m "这是无效的提交" #提交文件
[main 4f6e95a] 这是无效的提交
 1 file changed, 1 insertion(+)
 create mode 100644 first-commit/reset-all.java
 
$ git log #查看日志
commit 4f6e95aec67b54494dfa48a7d9e0d8b1190db54a (HEAD -> main)
Author: MouGuangJun <1900794909@qq.com>
Date:   Sat Jun 18 14:53:50 2022 +0800

    这是无效的提交

commit 4b1a26a560a52e04216a29e1f38098afa6150bcc
Author: MouGuangJun <1900794909@qq.com>
Date:   Sat Jun 18 14:42:32 2022 +0800

    这次提交的reset.java文件需要回退
    
$ git reset 4b1a26a560a52e04216a29e1f38098afa6150bcc #回退到指定版本

$ git ls-files -c #查看暂存区文件，发现reset-all.java已经不存在
SringM.java
StringM.java
diff.java
helloworld.class
helloworld.java
reset.java
```

<font color="red"> 参数--hard和参数--mixed的区别</font> 

```bash
$ git status -s #使用该文件进行测试
A  reset-all.java

$ git commit -m "测试mixed参数" #进行commit的操作
[main f543065] 测试mixed参数
 1 file changed, 1 insertion(+)
 create mode 100644 first-commit/reset-all.java
 
$ git reset HEAD^ #回退版本

$ git status -s #查看状态，发现reset-all.java文件还在工作区
?? reset-all.java

$ git add . #测试--hard参数开始

$ git commit -m "测试hard参数" #进行commit的操作
[main 46e7cea] 测试hard参数
 1 file changed, 1 insertion(+)
 create mode 100644 first-commit/reset-all.java

$ git reset --hard HEAD^ #回退版本
HEAD is now at 11d6aa1 测试单个文件的版本回复

$ git status -s  #查看状态，发现reset-all.java文件不存在于工作区中了

```

**HEAD 说明：**

- HEAD 表示当前版本
- HEAD^ 上一个版本
- HEAD^^ 上上一个版本
- 以此类推...

可以使用～数字表示

- HEAD~0 表示当前版本
- HEAD~1 上一个版本
- HEAD^2 上上一个版本
- 以此类推...



<font color="red">**git reset HEAD**</font>

git reset HEAD 命令用于恢复暂存区中的内容。

```bash
bird.txt$ vim tree.txt
#添加内容：
this is a tree!
$ vim bird.txt
#添加内容：
this is a bird

$ git add . #添加到暂存区
$ git status -s
A  bird.txt
A  tree.txt

$ git reset HEAD bird.txt #将bird.txt从暂存区撤回
$ git status -s
A  tree.txt
?? bird.txt

$ git commit -m "this is not bird" #此时提交时，只有tree被提交了，bird不会被提交
[main 141dda9] this is not bird
 1 file changed, 1 insertion(+)
 create mode 100644 first-commit/tree.txt
```

<font color="red">执行 git reset HEAD 以取消之前 git add 添加，但不希望包含在下一提交快照中的缓存。</font>

### git rm

<font color="green">#将文件从暂存区和工作区中删除。</font>

git rm 命令用于删除文件。

如果只是简单地从工作目录中手工删除文件，运行 **git status** 时就会在 **Changes not staged for commit** 的提示。

git rm 删除文件有以下几种形式：

1、将文件从暂存区和工作区中删除：

```bash
git rm <file>
```

以下实例从暂存区和工作区中删除 runoob.txt 文件：

```bash
git rm runoob.txt 
```

如果删除之前修改过并且已经放到暂存区域的话，则必须要用强制删除选项 **-f**。

强行从暂存区和工作区中删除修改后的 runoob.txt 文件：

```bash
git rm -f runoob.txt 
```

如果想把文件从暂存区域移除，但仍然希望保留在当前工作目录中，换句话说，仅是从跟踪清单中删除，使用 **--cached** 选项即可：

```bash
git rm --cached <file>
```

以下实例从暂存区中删除 runoob.txt 文件：

```bash
git rm --cached runoob.txt
```

实例：

```bash
$ touch runoob.txt
$ git add runoob.txt #添加到暂存区

$ git rm --cached runoob.txt #从暂存区中将文件删除
rm 'first-commit/runoob.txt'

$ git status -s #runoob.txt文件已经从暂存区移除
A  bird.txt
?? runoob.txt

$ git rm -f runoob.txt #删除runnoob物理文件
rm 'first-commit/runoob.txt'

$ ls
bird.txt  helloworld.class  helloworld.java  SringM.java  tree.txt
```

### git mv

<font color="green">移动或重命名工作区文件。</font>

git mv 命令用于移动或重命名一个文件、目录或软连接。

```bash
git mv [file] [newfile]
```

如果新文件名已经存在，但还是要重命名它，可以使用 **-f** 参数：

```bash
git mv -f [file] [newfile]
```

实例：

```bash
#将helloworld.class文件移动到当前路径的moved文件夹下
$ mkdir moved #创建文件夹
$ git add .

$ git mv helloworld.class moved/helloworld.class #移动文件

$ ls #文件已经被移动到对应的目录下
bird.txt  helloworld.java  moved/  SringM.java  tree.txt
```



## 提交日志

Git 提交历史一般常用两个命令：

- **git log** - 查看历史提交记录。
- **git blame <file>** - 以列表形式查看指定文件的历史修改记录。

<font color="red">**git log**</font>
在使用 Git 提交了若干更新之后，又或者克隆了某个项目，想回顾下提交历史，我们可以使用 git log 命令查看。

针对我们前一章节的操作，使用 git log 命令列出历史提交记录如下：

```bash
$ git log
commit 141dda99b013f6f35ebf49170c32bb9db636858c (HEAD -> main)
Author: MouGuangJun <1900794909@qq.com>
Date:   Sat Jun 18 15:51:11 2022 +0800

    this is not bird

commit 6dd14bda3756a6a1c31873e5181098ca95e0ec61
Author: MouGuangJun <1900794909@qq.com>
Date:   Sat Jun 18 10:53:07 2022 +0800

    第一次版本提交

commit 2383e55eed29239b2835ee56751b1038290c1249 (origin/git-branch-study)
Author: MouGuangJun <1900794909@qq.com>
Date:   Thu Jun 16 23:29:35 2022 +0800

    first commit
```

我们可以用 --oneline 选项来查看历史记录的简洁的版本。

```bash
$ git log --oneline
141dda9 (HEAD -> main) this is not bird
6dd14bd 第一次版本提交
2383e55 (origin/git-branch-study) first commit
```

你也可以用 --reverse 参数来逆向显示所有日志。

```bash
$ git log --reverse --oneline
2383e55 (origin/git-branch-study) first commit
6dd14bd 第一次版本提交
141dda9 (HEAD -> main) this is not bird
```

如果只想查找指定用户的提交日志可以使用命令：git log --author , 例如，比方说我们要找 Git 源码中MouGuangJun 提交的部分：

```bash
$ git log --author=MouGuangJun --oneline
141dda9 (HEAD -> main) this is not bird
6dd14bd 第一次版本提交
2383e55 (origin/git-branch-study) first commit
```

如果你要指定日期，可以执行几个选项：--since 和 --before，但是你也可以用 --until 和 --after。

例如，如果我要看 Git 项目中从2022-06-18开始到三周前的所有提交，我可以执行这个（我还用了 --no-merges 选项以隐藏合并提交）：

```bash
$ git log --oneline --after={3.week.ago} --before={2022-06-18} --no-merges
141dda9 (HEAD -> main) this is not bird
6dd14bd 第一次版本提交
2383e55 (origin/git-branch-study) first commit
```

<font color="red">**git blame**</font>
如果要查看指定文件的修改记录可以使用 git blame 命令，格式如下：

```bash
git blame <file>
```

git blame 命令是以列表形式显示修改记录，如下实例：

```bash
$ git blame tree.txt
141dda99 (MouGuangJun 2022-06-18 15:51:11 +0800 1) this is a tree!
```



<font color="red">**git show**</font>

```bash
$ git show 2383e55eed29239b2835ee56751b1038290c1249 #使用git show命令查看选中版本的提交内容
commit 2383e55eed29239b2835ee56751b1038290c1249 (origin/git-branch-study)
Author: MouGuangJun <1900794909@qq.com>
Date:   Thu Jun 16 23:29:35 2022 +0800

    first commit

diff --git a/git-add/add.txt b/git-add/add.txt
new file mode 100644
index 0000000..db4e5b3
--- /dev/null
+++ b/git-add/add.txt
@@ -0,0 +1 @@
+add new file to git!
\ No newline at end of file
diff --git a/git-commit/commit.txt b/git-commit/commit.txt
new file mode 100644
index 0000000..66aa4a9
--- /dev/null
+++ b/git-commit/commit.txt
@@ -0,0 +1 @@
+commit file to github!
\ No newline at end of file
```



## 远程操作

### 连接远程仓库

本地 Git 仓库和 GitHub 仓库之间的传输是通过SSH加密的，所以我们需要配置验证信息：

<font color="red">1.使用以下命令生成 SSH Key：</font>

```bash
$ ssh-keygen -t rsa -C "1900794909@qq.com"
```

这使用默认的一路回车就行。

成功的话会在 **~/** [ 当前用户/c/Users/19007/路径下]下生成 **.ssh** 文件夹，进去，打开 **id_rsa.pub**，复制里面的 **key**。

<font color="red">2.回到 github 上，进入**Account => Settings**（账户配置）。</font>

左边选择 **SSH and GPG keys**，然后点击 **New SSH key** 按钮,title 设置标题，可以随便填，粘贴在你电脑上生成的 key。

<font color="red">3.为了验证是否成功，输入以下命令：</font>

```bash
$ ssh -T git@github.com
The authenticity of host 'github.com (20.205.243.166)' can't be established.
ED25519 key fingerprint is SHA256:+DiY3wvvV6TuJJhbpZisF/zLDA0zPMSvHdkr4UvCOqU.
This key is not known by any other names
Are you sure you want to continue connecting (yes/no/[fingerprint])? yes #输入yes
Warning: Permanently added 'github.com' (ED25519) to the list of known hosts.
Hi MouGuangJun! You've successfully authenticated, but GitHub does not provide shell access.
```

<font color="red">4.在github上面创建一个仓库</font>

图形化操作，不再展示

我们根据 GitHub 的提示，在本地的仓库下运行命令：

```bash
$ git add .

$ git commit -m "添加到新的仓库中"
[main c7eb0c0] 添加到新的仓库中
 2 files changed, 1 insertion(+)
 create mode 100644 first-commit/bird.txt
 rename first-commit/{ => moved}/helloworld.class (100%)

$ git branch -M master #创建主分支

$ git remote add origin git@github.com:MouGuangJun/git-study.git #连接到远程仓库中

$ git push -u origin master #将本地仓库中的内容推送到github的git-study仓库的主分支中
Enumerating objects: 20, done.
Counting objects: 100% (20/20), done.
Delta compression using up to 8 threads
Compressing objects: 100% (12/12), done.
Writing objects: 100% (20/20), 1.72 KiB | 441.00 KiB/s, done.
Total 20 (delta 1), reused 6 (delta 0), pack-reused 0
remote: Resolving deltas: 100% (1/1), done.
To github.com:MouGuangJun/git-study.git
 * [new branch]      master -> master
branch 'master' set up to track 'origin/master'.
```



### git remote

<font color="green"># 远程仓库操作</font>

**git remote** 命令用于在远程仓库的操作。

本章节内容我们将以 Github 作为远程仓库来操作。

<font color="red">显示所有远程仓库：</font>

以下我们先载入远程仓库，然后查看信息：

```bash
$ git clone https://github.com/MouGuangJun/git-study.git #clone项目
Cloning into 'git-study'...
remote: Enumerating objects: 20, done.
remote: Counting objects: 100% (20/20), done.
remote: Compressing objects: 100% (11/11), done.
remote: Total 20 (delta 1), reused 20 (delta 1), pack-reused 0
Receiving objects: 100% (20/20), done.
Resolving deltas: 100% (1/1), done.

$ cd git-study/ #进入clone项目的路径中
$ git remote -v #查看远程仓库信息
origin  https://github.com/MouGuangJun/git-study.git (fetch)
origin  https://github.com/MouGuangJun/git-study.git (push)
```

**origin** 为远程地址的别名。

<font color="red">显示某个远程仓库的信息：</font>

```bash
$ git remote show origin
* remote origin
  Fetch URL: https://github.com/MouGuangJun/git-study.git
  Push  URL: https://github.com/MouGuangJun/git-study.git
  HEAD branch: master
  Remote branch:
    master tracked
  Local branch configured for 'git pull':
    master merges with remote master
  Local ref configured for 'git push':
    master pushes to master (up to date)
```

<font color="red">添加远程版本库：</font>

```bash
git remote add [shortname] [url]
```

shortname 为本地的版本库，例如：

```bash
# 提交到 Github
$ git remote add origin git@github.com:MouGuangJun/git-study.git
$ git push -u origin master
```

其他相关命令：

```bash
git remote rm name  # 删除远程仓库
git remote rename old_name new_name  # 修改仓库名
```

### git fetch

<font color="red">fetch和pull的却别：fetch = pull + merge，git fetch是**将远程主机的最新内容拉到本地，用户在检查了以后决定是否合并到工作本机分支中**，而git pull 则是**程主机的最新内容拉下来后直接合并**</font>

<font color="green">#从远程获取代码库</font>

**git fetch** 命令用于从远程获取代码库。

该命令执行完后需要执行 git merge 远程分支到你所在的分支。

从远端仓库提取数据并尝试合并到当前分支：

```bash
git merge
```

该命令就是在执行 git fetch 之后紧接着执行 git merge 远程分支到你所在的任意分支。

假设你配置好了一个远程仓库，并且你想要提取更新的数据，你可以首先执行:

```bash
git fetch [alias]
```

以上命令告诉 Git 去获取它有你没有的数据，然后你可以执行：

```bash
git merge [alias]/[branch]
```

以上命令将服务器上的任何更新（假设有人这时候推送到服务器了）合并到你的当前分支。

本章节以https://github.com/MouGuangJun/git-study.git为例。

接下来我们在 Github 上点击 **README.md** 并在线修改它:

![image-20220618181851534](../../md-photo/image-20220618181851534.png)

然后我们在本地更新修改。

```bash
$ git fetch origin
remote: Enumerating objects: 7, done.
remote: Counting objects: 100% (7/7), done.
remote: Compressing objects: 100% (5/5), done.
remote: Total 6 (delta 0), reused 0 (delta 0), pack-reused 0
Unpacking objects: 100% (6/6), 1.50 KiB | 32.00 KiB/s, done.
From https://github.com/MouGuangJun/git-study
   c7eb0c0..7984582  master     -> origin/master
```

以上信息"0205aab..febd8ed master -> origin/master" 说明 master 分支已被更新，我们可以使用以下命令将更新同步到本地：

```bash
$ git merge origin/master
Updating c7eb0c0..7984582
Fast-forward
 README.md | 3 +++
 1 file changed, 3 insertions(+)
 create mode 100644 README.md
 
$ ls #README.MD文件已经被更新
first-commit/  git-add/  git-commit/  README.md
```

### git pull

<font color="green">#下载远程代码并合并[参见git分支管理]</font>

**git pull** 命令用于从远程获取代码并合并本地的版本。

**git pull** 其实就是 **git fetch** 和 **git merge FETCH_HEAD** 的简写。

命令格式如下：

```bash
git pull <远程主机名> <远程分支名>:<本地分支名>
```

**实例**

更新操作：

```bash
$ git pull
$ git pull origin
```

将远程主机 origin 的 master 分支拉取过来，与本地的 first_branch分支合并。

```bash
git pull origin master:first_branch
```

如果远程分支是与当前分支合并，则冒号后面的部分可以省略。

```bash
git pull origin master
```

上面命令表示，取回 origin/master 分支，再与本地的 first_branch分支合并。

上面的 pull 操作用 fetch 表示为：

以我的 https://github.com/MouGuangJun/git-study.git为例，远程载入合并本地分支。

```bash
$ git remote -v #查看当前远程分支的状态
origin  https://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@github.com/MouGuangJun/git-study.git (fetch)
origin  https://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@github.com/MouGuangJun/git-study.git (push)

$ git pull origin master : ThreeBranch #将远程的master与本地的ThreeBranch合并
From https://github.com/MouGuangJun/git-study
 * branch            master     -> FETCH_HEAD
 * branch            HEAD       -> FETCH_HEAD
 * branch            ThreeBranch -> FETCH_HEAD
Merge made by the 'ort' strategy.
 remote file | 3 +++
 1 file changed, 3 insertions(+)
 create mode 100644 remote file
```

### git push

<font color="green">#上传远程代码并合并</font>

**git push** 命用于从将本地的分支版本上传到远程并合并。

命令格式如下：

```bash
git push <远程主机名> <本地分支名>:<远程分支名>
```

如果本地分支名与远程分支名相同，则可以省略冒号：

```bash
git push <远程主机名> <本地分支名>
```

**实例**

以下命令将本地的 master 分支推送到 origin 主机的 master 分支。

```bash
$ git push origin master
```

相等于：

```bash
$ git push origin master:master
```

如果本地版本与远程版本有差异，但又要强制推送可以使用 --force 参数：

```bash
git push --force origin master
```

删除主机的分支可以使用 --delete 参数，以下命令表示删除 origin 主机的 master 分支：

```bash
git push origin --delete master
```

以我的  https://github.com/MouGuangJun/git-study.git为例

```bash
$ touch push_file.txt #创建本地文件

$ git add . #提交到本地仓库
warning: LF will be replaced by CRLF in push_file.txt.
The file will have its original line endings in your working directory

$ git commit -m "测试push命令"
[ThreeBranch 1a759c8] 测试push命令
 1 file changed, 1 insertion(+)
 create mode 100644 push_file.txt
 
 #将本地分支ThreeBranch推送到origin/ThreeBranch分支下
$ git push origin ThreeBranch:ThreeBranch #远程分支
Enumerating objects: 12, done.
Counting objects: 100% (12/12), done.
Delta compression using up to 8 threads
Compressing objects: 100% (8/8), done.
Writing objects: 100% (10/10), 1.12 KiB | 575.00 KiB/s, done.
Total 10 (delta 3), reused 1 (delta 0), pack-reused 0
remote: Resolving deltas: 100% (3/3), done.
To https://github.com/MouGuangJun/git-study.git
   34b80e3..1a759c8  ThreeBranch -> ThreeBranch
   
#将主机上的第三分支删除
$ git push origin --delete ThreeBranch
To https://github.com/MouGuangJun/git-study.git
 - [deleted]         ThreeBranch
 
$ git branch -a #第三分支已经被删除
  SecondBranch
* ThreeBranch
  first_branch
  master
  remotes/origin/SecondBranch
  remotes/origin/first_branch
  remotes/origin/master
```



## 其他

### 提交github的基本操作

```bash
$ git init
Initialized empty Git repository in D:/git/git/.git/
#初始化git

$ ls -a
./  ../  .git/  git-add/  git-commit/
#查看所有文件

$ git add .
#添加当前路径下的所有文件

$ git status -s
A  git-add/add.txt
A  git-commit/commit.txt
#查看git的状态

$ git commit -m "first commit"
[master (root-commit) 2383e55] first commit
 2 files changed, 2 insertions(+)
 create mode 100644 git-add/add.txt
 create mode 100644 git-commit/commit.txt
 #提交文件到暂存区中
 
$ git branch -M main
#创建主分支
 
$ git remote add origin
$ https://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@github.com/MouGuangJun/git-study.git
#远程连接到github的仓库中

$ git push -u origin main
Enumerating objects: 6, done.
Counting objects: 100% (6/6), done.
Delta compression using up to 8 threads
Compressing objects: 100% (2/2), done.
Writing objects: 100% (6/6), 387 bytes | 193.00 KiB/s, done.
Total 6 (delta 0), reused 0 (delta 0), pack-reused 0
To https://github.com/MouGuangJun/git-study.git
 * [new branch]      main -> main
branch 'main' set up to track 'origin/main'.
#将提交的文件推送到github的主分支中
```

### 查看暂存区中的文件

一、简介
<font color="red">**git ls-files** </font>命令是用来查看暂存区中文件信息
二、常用参数
参数说明（括号里是简写）

–cached(-c)显示暂存区中的文件，git ls-files命令默认的参数
–deleted(-d)显示删除的文件
–modified(-m) 显示修改过的文件
–other(-o)显示没有被git跟踪的文件
–stage(-s) 显示mode以及文件对应的Blob对象，进而我们可以获取暂存区中对应文件里面的内容。
二、实例
1.如何查看暂存区中有哪些文件？
<font color="red">git ls-files</font>

1.如何查看暂存区中bb.txt文件内容是什么？
首先，我们需要查查bb.txt文件对应的Blob对象，如下：
git ls-files -s – bb.txt 或者直接 git ls-files -s

然后通过Blob对象，查询bb.txt里面的内容：
<font color="red">git cat-file -p 6cef</font>

查看文件：
git ls-files -c 或者 git ls-files --cached 其他类似



### git删除本地分支

如果需要删除本地的分支，可以用**git branch -d**的命令来实现，如果要强制删除的话，可以把**-d**换成大写的**-D**。

```bash
$ git branch -d MouGuangJun
Deleted branch MouGuangJun (was 231aa00).

$ git branch -d origin/master
Deleted branch origin/master (was 7984582).
```



### git分支管理

![img](../../md-photo/095739b5605ed15a8578c29cac3d9dbd.png)

#### 1.GIT创建分支

#####  **1.1 创建本地分支**

新分支都是基于原有分支创建， 而在实践开发中基本从线上分支（与线上代码同步的分支）： <font color="red">master 分支</font>创建。

从master创建本地分支也有两种方式：基于<font color="red">本地master</font>分支创建分支、基于<font color="red">线上master分支</font>创建分支

<font color="green">**基于本地master分支创建分支（命令窗口进入工程根目录）**
</font>

```bash
$ git branch #查看本地分支信息
* first_branch
  master
```

*****号分支表示其为当前分支，所以得切换至master分支

```bash
$ git checkout master #切换到master分支
Switched to branch 'master'
Your branch is up to date with 'origin/master'.

$ git branch #查看当前分支
  first_branch
* master

$ git pull #更新本地master分支代码至最新
Already up to date.

# 创建并切换到新分支相当于：git branch SecondBranch + git checkout SecondBranch
# 基于本地master分支代码创建新分支：SecondBranch,并切换到该分支
$ git checkout -b SecondBranch
Switched to a new branch 'SecondBranch'

git branch #查看当前分支
$ git branch
* SecondBranch
  first_branch
  master
```



<font color="green">**基于远程master分支创建分支**</font>

首先查看本地、线上分支信息（调用以下指令前建议先执行"git pull -p"防止本地git分支信息缓存）：

```bash
$ git branch -a
* SecondBranch
  first_branch
  master
  remotes/origin/first_branch
  remotes/origin/master
```

普通名称为本地分支、* 显示为当前分支、remotes/..显示为远程分支；

切换至远程分支：

```bash
$ git checkout remotes/origin/master
Note: switching to 'remotes/origin/master'.

You are in 'detached HEAD' state. You can look around, make experimental
changes and commit them, and you can discard any commits you make in this
state without impacting any branches by switching back to a branch.

If you want to create a new branch to retain commits you create, you may
do so (now or later) by using -c with the switch command. Example:

  git switch -c <new-branch-name>

Or undo this operation with:

  git switch -

Turn off this advice by setting config variable advice.detachedHead to false

HEAD is now at 2989233 Update README.md
#基于远程分支创建的新分支

git checkout -b ThreeBranch

#查看分支的状态
$ git branch
  SecondBranch
* ThreeBranch
  first_branch
  master
```

![img](../../md-photo/9ecca2760945c842e5cbed8c831f6edc.png)

##### **1.2 创建远程分支**

创建远程分支可以直接由本地新分支推送完成也可以在远程分支管理系统（例如：github 、gitLab）上可视化操作完成；

<font color="green">**本地新分支推送创建远程分支**</font>

在 1部分我们在本地创建了一新分支， 如果在该分支更新了代码， 然后执行"git add "、"git commit"指令后， 再执行以下指令可在远程创建新分支；

```bash
$ git checkout SecondBranch #切换到第一部分创建的分支
Switched to branch 'SecondBranch'

# git push <远程主机名> <本地分支名>:<远程分支名>
$ git push origin SecondBranch:SecondBranch #在远程创建分支，并把本地的分支信息推送上去
Total 0 (delta 0), reused 0 (delta 0), pack-reused 0
remote:
remote: Create a pull request for 'SecondBranch' on GitHub by visiting:
remote:      https://github.com/MouGuangJun/git-study/pull/new/SecondBranch
remote:
To https://github.com/MouGuangJun/git-study.git
 * [new branch]      SecondBranch -> SecondBranch
```

远程分支已经创建成功：

![image-20220618215258955](../../md-photo/image-20220618215258955.png)



![img](../../md-photo/92085d5512b0cc541ac82fbff18fab74.png)



<font color="green">**GitHub上创建远程分支**</font>

![image-20220618221249425](../../md-photo/image-20220618221249425.png)

![img](../../md-photo/408dda1bad9ff3a9aa735962251fb728.png)

PS: 上述创建的同名本地分支跟远程分支并没建立关联， 这样操作指令会比较不方便（如“git pull”指令得输入对应的远程分支名）， 怎么让其建立关联？

```bash
# 切换到ThreeBranch分支
$ git checkout ThreeBranch
Switched to branch 'ThreeBranch'

$ git branch
  SecondBranch
* ThreeBranch
  first_branch
  master

# 执行代码更新命令
$ git pull
From https://github.com/MouGuangJun/git-study
 * [new branch]      ThreeBranch -> origin/ThreeBranch
There is no tracking information for the current branch.
Please specify which branch you want to merge with.
See git-pull(1) for details.

    git pull <remote> <branch>

If you wish to set tracking information for this branch you can do so with:

    git branch --set-upstream-to=origin/<branch> ThreeBranch
    
#按照其提示提示执行指令
#git branch --set-upstream-to=origin/<远程分支名> <本地分支名>如设置当前分支，第二个参数可省略
$ git branch --set-upstream-to=origin/ThreeBranch
branch 'ThreeBranch' set up to track 'origin/ThreeBranch'.

#关联成功的提示
$ git pull
Already up to date.
```



#### 2.GIT切换分支

##### 2.1工作区没新代码切换分支

创建好新分支后就可以在新分支进行开发， 但可能中途需要去维护其他分支代码；这个时候就得切换分支了。

```bash
# 切换分支的命令
git checkout [分支名字]
```

ps: 编辑代码不会直接在develop、master分支操作，因为最终代码要同时合并到这两个分支上，所以一般均在新分支开发（即使是很小的改动）；

##### **2.2工作区有新代码切换分支**

工作区间的代码均已提交到本地仓库（当前分支）， 那切换分支没什么问题， 但如果工作区域代码尚未提交，这时切换分支会怎样呢？

有时候无法切换， 有时候能正常切换；

能正常切换：改动的代码能正常合并到切换后的分支（自动合并成功）

无法切换：改动的代码不能正常合并到切换后的分支（自动合并失败）工作区间有未提交代码，切换分支自动执行"git merge"操作，故有冲突将无法切换成功；

当然也会有这样的场景： 当前分支代码没写完，还不想提交且因为有紧急需求不得不切换分支；这个时候可以 使用“[工作现场](https://link.zhihu.com/?target=http%3A//www.kancloud.cn/wteamxq/git_rank/276490)”将代码暂时放着：

```bash
$ vim README.md #修改文件
# 添加以下内容
checkout after stash changed!

$ git stash save "将第三个分支的文件暂存" #stash暂存修改的文件
Saved working directory and index state On ThreeBranch: 将第三个分支的文 件暂存
 
$ git status -s #可以看到暂存区中不存在改动的文件

$ git checkout SecondBranch #切换到其他的分支
Switched to branch 'SecondBranch'

$ git checkout ThreeBranch #切换回原来的分支
Switched to branch 'ThreeBranch'
#执行了自动合并的操作
Your branch is up to date with 'origin/ThreeBranch'.

$ cat README.md #文件已经被修改
this is master bracth, please do not move this file! 


$ git stash pop #将stash暂存的文件恢复回来
On branch ThreeBranch
Your branch is up to date with 'origin/ThreeBranch'.

Changes not staged for commit:
  (use "git add <file>..." to update what will be committed)
  (use "git restore <file>..." to discard changes in working directory)
        modified:   README.md

no changes added to commit (use "git add" and/or "git commit -a")
Dropped refs/stash@{0} (f56372a267a45b4ac82383094310068e7b43f0c7)

$ cat README.md #文件已经恢复
this is master bracth, please do not move this file!


checkout after stash changed!

$ git stash save "我要切换到别的分支，暂存这部分的文件"
Saved working directory and index state On ThreeBranch: 我要切换到别的分 支，暂存这部分的文件
```

图解：

![img](../../md-photo/bbb552bfb7223214c21d7c57907f42c1.png)



在新分支开发完后：

![img](../../md-photo/72cfcbab7db7e1a8628ebb5a9cf0331f.png)



如果本来想在A分支上开发， 开发过程中才发现当前处在B分支，想强制将工作区间代码迁到A分支也可以借助“[工作现场](https://link.zhihu.com/?target=http%3A//www.kancloud.cn/wteamxq/git_rank/276490)”完成：

```bash
$ vim README.md #修改文件
#添加以下内容
overflow to SecondBranch!

$ git stash save "准备覆盖到第二分支" #暂存文件
Saved working directory and index state On ThreeBranch: 准备覆盖到第二分支

$ git checkout SecondBranch #切换分支
Switched to branch 'SecondBranch'

$ git stash pop #将ThreeBranch的文件覆盖到第二分支
On branch SecondBranch
Changes not staged for commit:
  (use "git add <file>..." to update what will be committed)
  (use "git restore <file>..." to discard changes in working directory)
        modified:   README.md

no changes added to commit (use "git add" and/or "git commit -a")
Dropped refs/stash@{0} (c506b31662d66e9f4c91746bc9ed62744110a139)

$ cat README.md #文件已经成功覆盖到第二分支
this is master bracth, please do not move this file!


checkout after stash changed!

overflow to SecondBranch!
```

图解：

![img](../../md-photo/54f0b95647cd3847fc0ee378f00b9816.png)

切换到新的分支后：

![img](../../md-photo/e25ec334c061f9f3e845fd33367f60ea.png)



##### **2.3切换分支异常处理（windows下）**

不少在windows下使用git会碰到这样的问题： 从A分支切换到B分支由于git异常导致虽然切换分支成功，但在当前B分支上留存了大量A分支的代码（被git认为是新的改动或新增文件）；

我们可以将所有改动提交， 然后使用远程分支代码覆盖！放心提交到本地仓库，反正后面这个commit会被覆盖。

```bash
$ vim newFile.java #添加一个新的文件

$ git add . #添加到暂存区

$ git status -s #查看暂存区状态
M  README.md
A  newFile.java

$ git commit -m "这个commit会被覆盖" #将文件提交到ThreeBranch分支本地仓库
[ThreeBranch分支本地仓库 0c59ad9] 这个commit会被覆盖
 2 files changed, 6 insertions(+)
 create mode 100644 newFile.java
 
$ git reset --hard origin/SecondBranch #回退到SecondBranch分支的HEAD版本
HEAD is now at 2989233 Update README.md

$ ls #发现文件newFile.java已经被回退
first-commit/  git-add/  git-commit/  README.md

$ git status -s #暂存区中的文件被清空


```

图解：

![img](../../md-photo/ad3b1c3ceabb82881dfde60c037be8bf.png)



<font color="red">**删除stash区的内容**</font>

```bash
$ git stash list
stash@{0}: On ThreeBranch: 我要切换到别的分支，暂存这部分的文件

# 删除暂存区中的所有内容
$ git stash clear

# 删除指定的队列
$ git stash drop stash@{0}
```



#### 3.GIT合并分支

##### 3.1合并本地分支代码

例如在新分支ThreeBranch开发的功能已完成并已提交；接下来走测试流程需要将代码合并到SecondBranch分支（我所在团队SecondBranch为测试分支，不同团队的测试分支会不一样）

![img](../../md-photo/c0da229eca9308445f112df4c8a2386d.png)

```bash
$ git branch #查看当前分支
  SecondBranch
* ThreeBranch
  first_branch
  master
  
$ vim mergebranch_local.txt #创建一个新的文件

$ git add mergebranch_local.txt #执行提交的操作
warning: LF will be replaced by CRLF in mergebranch_local.txt.
The file will have its original line endings in your working directory

$ git commit -m “"合并分支测试使用"
[ThreeBranch 81f9224] 合并分支测试使用
 1 file changed, 1 insertion(+)
 create mode 100644 mergebranch_local.txt
 
 
$ git log #查看提交状态
commit 81f92248b874daff9bfd23dcecf5f71f9d033569 (HEAD -> ThreeBranch)
Author: MouGuangJun <1900794909@qq.com>
Date:   Sun Jun 19 11:49:25 2022 +0800

    合并分支测试使用
    
$ git checkout SecondBranch #切换到第二分支
Switched to branch 'SecondBranch'

$ git merge ThreeBranch #合第三分支的提交
Updating 2989233..81f9224
Fast-forward
 mergebranch_local.txt | 1 +
 1 file changed, 1 insertion(+)
 create mode 100644 mergebranch_local.txt
 
$ git log #可以看到ThreeBranch上的commit已经被合并过来
commit 81f92248b874daff9bfd23dcecf5f71f9d033569 (HEAD -> SecondBranch, ThreeBranch)
Author: MouGuangJun <1900794909@qq.com>
Date:   Sun Jun 19 11:49:25 2022 +0800

    合并分支测试使用
#提交commit到远程（ThreeBranch分支有多少个commit就会生成几个）
$ git push origin SecondBranch #推送到远程分支
Enumerating objects: 4, done.
Counting objects: 100% (4/4), done.
Delta compression using up to 8 threads
Compressing objects: 100% (2/2), done.
Writing objects: 100% (3/3), 335 bytes | 167.00 KiB/s, done.
Total 3 (delta 1), reused 0 (delta 0), pack-reused 0
remote: Resolving deltas: 100% (1/1), completed with 1 local object.
To https://github.com/MouGuangJun/git-study.git
   2989233..81f9224  SecondBranch -> SecondBranch
```

可以看到文件已经被推送到第二分支上

![image-20220619120454238](../../md-photo/image-20220619120454238.png)



##### 3.2合并远程分支代码

如果新分支由多人维护，为保证更新代码为最新，使用上述方式合并分支会比较麻烦：

首先得切换到本地ThreeBranch分支(git checkout ThreeBranch)；

然后更新代码(git pull)；

再次切换回develop分支(git checkout SecondBranch)；

最后才能执行合并操作(git merge ThreeBranch);

其实可直接将远程的 ThreeBranch分支代码合并到本地 SecondBranch分支：

在第三分支上创建一个remote file文件：

![image-20220619121234370](../../md-photo/image-20220619121234370.png)



```bash
$ git branch -a #查看分支的状态
* SecondBranch
  ThreeBranch
  first_branch
  master
  remotes/origin/SecondBranch
  remotes/origin/ThreeBranch
  remotes/origin/first_branch
  remotes/origin/master
 
#直接将远程分支remotes/origin/ThreeBranch合并到本地分支SecondBranch
$ git pull origin ThreeBranch
remote: Enumerating objects: 4, done.
remote: Counting objects: 100% (4/4), done.
remote: Compressing objects: 100% (2/2), done.
remote: Total 3 (delta 1), reused 0 (delta 0), pack-reused 0
Unpacking objects: 100% (3/3), 676 bytes | 28.00 KiB/s, done.
From https://github.com/MouGuangJun/git-study
 * branch            ThreeBranch -> FETCH_HEAD
   2989233..775c7c3  ThreeBranch -> origin/ThreeBranch
Merge made by the 'ort' strategy.
 remote file | 1 +
 1 file changed, 1 insertion(+)
 create mode 100644 remote file
 
$ ls -a #remote file已经被合并下来
 ./    .git/           git-add/      mergebranch_local.txt  'remote file'
 ../   first-commit/   git-commit/   README.md
```



##### **3.3合并代码冲突解决**

无论哪种方式合并分支代码代码冲突是无可避免的情况， 开发流程使用了typescript/sass等需要执行编译的语言更容易产生冲突；

分支合并代码冲突跟一般代码冲突一样： git能处理的冲突会自动解决， 否则就得手动解决冲突；冲突是由于多人维护代码导致，一定要找到相关人一起讨论冲突代码的取舍；

修改ThreeBranch远程的文件：

![image-20220619121953897](../../md-photo/image-20220619121953897.png)



```bash
$ cat remote\ file #修改SecondBranch的remote file文件
this is a remotes file!


this is local modify!

$ git commit -am "本地修改的remote file文件" #提交第二分支本地的修改
[SecondBranch 701ef07] 本地修改的remote file文件
 1 file changed, 3 insertions(+)
 
$ git pull origin ThreeBranch #从远程合并分支
From https://github.com/MouGuangJun/git-study
 * branch            ThreeBranch -> FETCH_HEAD
Auto-merging remote file
CONFLICT (content): Merge conflict in remote file
Automatic merge failed; fix conflicts and then commit the result. #提示需要解决冲突

$ git status -s #查看暂存区当前的状态
UU "remote file"

19007@LAPTOP-R9JHJ6PC MINGW64 /d/git/git-study/git-study (SecondBranch|MERGING)
$ cat remote\ file #查看有冲突的文件
this is a remotes file!

<<<<<<< HEAD #"HEAD"到"======="之间为当前分支改动

this is local modify!
======= #"======="到">>>>>>> ThreeBranch"为待合并分支的改动
this is remote modify
>>>>>>> 34b80e31b4272cb9c263878fb0eacc6c0a399b57

$ cat remote\ file #手动解决冲突后
this is a remotes file!


this is local modify!
this is remote modify

$ git add -A #告诉git冲突已解决

$ git status -s
M  "remote file"

git commit -m "[ThreeBranch]-合并SecondBranch代码" #合并完成，提交代码
git push
```



##### **3.4合并代码异常处理**



同切换分支异常情况， 在windows下合并分支也会出现异常， 异常处理方式同上：

```bash
# 参见2-2.3
# 将所有改动提交到本地仓库
git add -A
git commit -m "这个commit会被覆盖"
# B 是当前分支名
git reset --hard origin/B
```



### IDEA连接GitHub

1、设置git路径：

![image-20220626171711040](../../md-photo/image-20220626171711040.png)



开始使用本地的git

![image-20220802232007499](../../md-photo/image-20220802232007499.png)





2、配置SSH密钥

![image-20220626171800456](../../md-photo/image-20220626171800456.png)



3、配置github仓库地址

![image-20220626171843796](../../md-photo/image-20220626171843796.png)

git@github.com:MouGuangJun/design-mode.git

![image-20220626171906620](../../md-photo/image-20220626171906620.png)





4.克隆远程的项目（复制github的url即可）

![image-20220917124108923](../../md-photo/image-20220917124108923.png)

设置ssh方式的url：

![image-20220918223046755](../../md-photo/image-20220918223046755.png)

git的ssh方式：**<font color='red'>git@github.com:MouGuangJun/仓库名.git</font>**

![image-20220918223109067](../../md-photo/image-20220918223109067.png)



### VSCode连接GitHub

点击终端，新建终端。

```bash
# 初始化仓库
$ git init

#创建主分支
$ git branch -M master

#连接到远程仓库中
$ git remote add master git@github.com:MouGuangJun/Vue-Study.git

# 将所有文件添加到暂存区
$ git add .

# 提交当前的文件到git中
$ git commit -m "第一次将vue的项目提交到远程仓库"

 #将本地仓库中的内容推送到github的远程仓库中（第一个master为远程的）
$ git push -u master master

# 在远程创建开发分支(development_branch)
# 拉取远程分支的信息
$ git pull

# 切换到开发分支
$ git checkout remotes/master/development_branch
```



vscode检出开发分支：

![image-20220903181820898](../../md-photo/image-20220903181820898.png)



![image-20220903181937096](../../md-photo/image-20220903181937096.png)



然后进行对应的提交、推送操作：

![image-20220903182022962](../../md-photo/image-20220903182022962.png)

![image-20220903182046719](../../md-photo/image-20220903182046719.png)



### Git 常用命令大全

git 常用命令(**点击图片查看大图**)：

![img](../../md-photo/011500266295799.jpg)

```bash
git init                                                  # 初始化本地git仓库（创建新仓库）
git config --global user.name "xxx"                       # 配置用户名
git config --global user.email "xxx@xxx.com"              # 配置邮件
git config --global color.ui true                         # git status等命令自动着色
git config --global color.status auto
git config --global color.diff auto
git config --global color.branch auto	
git config --global color.interactive auto
git config --global --unset http.proxy                    # remove  proxy configuration on git
git clone git+ssh://git@192.168.53.168/VT.git             # clone远程仓库
git status                                                # 查看当前版本状态（是否修改）
git add xyz                                               # 添加xyz文件至index
git add .                                                 # 增加当前子目录下所有更改过的文件至index
git commit -m 'xxx'                                       # 提交
git commit --amend -m 'xxx'                               # 合并上一次提交（用于反复修改）
git commit -am 'xxx'                                      # 将add和commit合为一步
git rm xxx                                                # 删除index中的文件
git rm -r *                                               # 递归删除
git log                                                   # 显示提交日志
git log -1                                                # 显示1行日志 -n为n行
git log -5
git log --stat                                            # 显示提交日志及相关变动文件
git log -p -m
git show dfb02e6e4f2f7b573337763e5c0013802e392818         # 显示某个提交的详细内容
git show dfb02                                            # 可只用commitid的前几位
git show HEAD                                             # 显示HEAD提交日志
git show HEAD^                                            # 显示HEAD的父（上一个版本）的提交日志 ^^为上两个版本 ^5为上5个版本
git tag                                                   # 显示已存在的tag
git tag -a v2.0 -m 'xxx'                                  # 增加v2.0的tag
git show v2.0                                             # 显示v2.0的日志及详细内容
git log v2.0                                              # 显示v2.0的日志
git diff                                                  # 显示所有未添加至index的变更
git diff --cached                                         # 显示所有已添加index但还未commit的变更
git diff HEAD^                                            # 比较与上一个版本的差异
git diff HEAD -- ./lib                                    # 比较与HEAD版本lib目录的差异
git diff origin/master..master                            # 比较远程分支master上有本地分支master上没有的
git diff origin/master..master --stat                     # 只显示差异的文件，不显示具体内容
git remote add origin git+ssh://git@192.168.53.168/VT.git # 增加远程定义（用于push/pull/fetch）
git branch                                                # 显示本地分支
git branch --contains 50089                               # 显示包含提交50089的分支
git branch -a                                             # 显示所有分支
git branch -r                                             # 显示所有原创分支
git branch --merged                                       # 显示所有已合并到当前分支的分支
git branch --no-merged                                    # 显示所有未合并到当前分支的分支
git branch -m master master_copy                          # 本地分支改名
git checkout -b master_copy                               # 从当前分支创建新分支master_copy并检出
git checkout -b master master_copy                        # 上面的完整版
git checkout features/performance                         # 检出已存在的features/performance分支
git checkout --track hotfixes/BJVEP933                    # 检出远程分支hotfixes/BJVEP933并创建本地跟踪分支
git checkout v2.0                                         # 检出版本v2.0
git checkout -b devel origin/develop                      # 从远程分支develop创建新本地分支devel并检出
git checkout -- README                                    # 检出head版本的README文件（可用于修改错误回退）
git merge origin/master                                   # 合并远程master分支至当前分支
git cherry-pick ff44785404a8e                             # 合并提交ff44785404a8e的修改
git push origin master                                    # 将当前分支push到远程master分支
git push origin :hotfixes/BJVEP933                        # 删除远程仓库的hotfixes/BJVEP933分支
git push --tags                                           # 把所有tag推送到远程仓库
git fetch                                                 # 获取所有远程分支（不更新本地分支，另需merge）
git fetch --prune                                         # 获取所有原创分支并清除服务器上已删掉的分支
git pull origin master                                    # 获取远程分支master并merge到当前分支
git mv README README2                                     # 重命名文件README为README2
git reset --hard HEAD                                     # 将当前版本重置为HEAD（通常用于merge失败回退）
git rebase
git branch -d hotfixes/BJVEP933                           # 删除分支hotfixes/BJVEP933（本分支修改已合并到其他分支）
git branch -D hotfixes/BJVEP933                           # 强制删除分支hotfixes/BJVEP933
git ls-files                                              # 列出git index包含的文件
git show-branch                                           # 图示当前分支历史
git show-branch --all                                     # 图示所有分支历史
git whatchanged                                           # 显示提交历史对应的文件修改
git revert dfb02e6e4f2f7b573337763e5c0013802e392818       # 撤销提交dfb02e6e4f2f7b573337763e5c0013802e392818
git ls-tree HEAD                                          # 内部命令：显示某个git对象
git rev-parse v2.0                                        # 内部命令：显示某个ref对于的SHA1 HASH
git reflog                                                # 显示所有提交，包括孤立节点
git show HEAD@{5}
git show master@{yesterday}                               # 显示master分支昨天的状态
git log --pretty=format:'%h %s' --graph                   # 图示提交日志
git show HEAD~3
git show -s --pretty=raw 2be7fcb476
git stash                                                 # 暂存当前修改，将所有至为HEAD状态
git stash list                                            # 查看所有暂存
git stash show -p stash@{0}                               # 参考第一次暂存
git stash apply stash@{0}                                 # 应用第一次暂存
git grep "delete from"                                    # 文件中搜索文本“delete from”
git grep -e '#define' --and -e SORT_DIRENT
git gc
git fsck
```

