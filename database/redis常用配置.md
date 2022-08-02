```bash
# Redis configuration file example

# Note on units: when memory size is needed, it is possible to specify
# it in the usual form of 1k 5GB 4M and so forth:
# 注意:当需要内存大小时，可以指定  
# 通常的形式是1k 5GB 4M等等: 
#
# 1k => 1000 bytes
# 1kb => 1024 bytes
# 1m => 1000000 bytes
# 1mb => 1024*1024 bytes
# 1g => 1000000000 bytes
# 1gb => 1024*1024*1024 bytes
#
# units are case insensitive so 1GB 1Gb 1gB are all the same.
# 单位不区分大小写，所以1GB 1Gb 1gB都是相同的。 

################################## INCLUDES ###################################

# Include one or more other config files here.  This is useful if you
# have a standard template that goes to all Redis servers but also need
# to customize a few per-server settings.  Include files can include
# other files, so use this wisely.
# 
# 在这里包含一个或多个其他配置文件。 这很有用，如果你  
# 有一个标准的模板，去所有的Redis服务器，但也需要  
# 来定制一些每个服务器的设置。 包含文件可以包含  
# 其他文件，所以要明智地使用它。  
#
# Notice option "include" won't be rewritten by command "CONFIG REWRITE"
# from admin or Redis Sentinel. Since Redis always uses the last processed
# line as value of a configuration directive, you'd better put includes
# at the beginning of this file to avoid overwriting config change at runtime.
#
# 注意选项"include"不会被命令"CONFIG REWRITE"重写  
# 从管理员或Redis哨兵。 因为Redis总是使用最后一个处理  
# line作为配置指令的值，你最好放入包括  
# 以避免在运行时覆盖配置更改。 
#
# If instead you are interested in using includes to override configuration
# options, it is better to use include as the last line.
# 
# 如果您对使用include重写配置感兴趣的话  
# 选项，最好使用include作为最后一行。
#
# include .\path\to\local.conf
# include c:\path\to\other.conf

################################## NETWORK #####################################

# By default, if no "bind" configuration directive is specified, Redis listens
# for connections from all the network interfaces available on the server.
# It is possible to listen to just one or multiple selected interfaces using
# the "bind" configuration directive, followed by one or more IP addresses.
#
# 默认情况下，如果没有指定“bind”配置指令，Redis会监听  
# 表示来自服务器上所有可用网络接口的连接。  
# 可以只监听一个或多个选择的接口  
# bind配置指令，后面跟着一个或多个IP地址。
#
# Examples:
#
# bind 192.168.1.100 10.0.0.1
# bind 127.0.0.1 ::1
#
# ~~~ WARNING ~~~ If the computer running Redis is directly exposed to the
# internet, binding to all the interfaces is dangerous and will expose the
# instance to everybody on the internet. So by default we uncomment the
# following bind directive, that will force Redis to listen only into
# the IPv4 lookback interface address (this means Redis will be able to
# accept connections only from clients running into the same computer it
# is running).
#
# ~~~警告~~~如果运行Redis的计算机直接暴露在  
# internet，绑定到所有接口是危险的，会暴露  
# 实例在互联网上的每个人。 默认情况下，我们取消注释  
# 以下绑定指令，这将迫使Redis只听进入  
# IPv4回看接口地址(这意味着Redis将能够  
# 只接受运行在同一台计算机上的客户端的连接  
# 运行)。  
#
# IF YOU ARE SURE YOU WANT YOUR INSTANCE TO LISTEN TO ALL THE INTERFACES
# JUST COMMENT THE FOLLOWING LINE.
# 如果您确定您希望您的实例侦听所有接口  
# 注释下面这一行。  
# ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
bind 127.0.0.1

# Protected mode is a layer of security protection, in order to avoid that
# Redis instances left open on the internet are accessed and exploited.
#
# 保护模式是一层安全保护，为了避免这种情况  
# Redis实例在互联网上打开被访问和利用。  
#
# When protected mode is on and if:
# 当保护模式开启时，如果:
#
# 1) The server is not binding explicitly to a set of addresses using the
#    "bind" directive.
# 2) No password is configured.
#
# 1) 控件将服务器显式绑定到一组地址  “绑定”指令。 
# 2) 未配置密码。
# 
#
# The server only accepts connections from clients connecting from the
# IPv4 and IPv6 loopback addresses 127.0.0.1 and ::1, and from Unix domain
# sockets.
#
# 服务器只接受来自客户端的连接  
# IPv4和IPv6的loopback地址127.0.0.1和::1，和来自Unix域  
# 套接字。  
#
# By default protected mode is enabled. You should disable it only if
# you are sure you want clients from other hosts to connect to Redis
# even if no authentication is configured, nor a specific set of interfaces
# are explicitly listed using the "bind" directive.
#
# 缺省情况下，保护模式处于开启状态。 只有当  
# 你确定你想从其他主机的客户端连接到Redis  
# ，即使没有配置身份验证，也没有指定一组接口  
# 使用"bind"指令显式列出。
#
protected-mode yes

# Accept connections on the specified port, default is 6379 (IANA #815344).
# If port 0 is specified Redis will not listen on a TCP socket.
# 
# 接受指定端口上的连接，默认是6379 (IANA #815344)。  
# 如果指定端口0,Redis不会监听TCP套接字。  
#
port 6379

# TCP listen() backlog.
#
# In high requests-per-second environments you need an high backlog in order
# to avoid slow clients connections issues. Note that the Linux kernel
# will silently truncate it to the value of /proc/sys/net/core/somaxconn so
# make sure to raise both the value of somaxconn and tcp_max_syn_backlog
# in order to get the desired effect.
#
# 在每秒请求数较高的环境中，您需要按顺序处理大量积压  
# 避免客户端连接速度慢的问题。 注意，Linux内核  
# 将无声地将它截断为/proc/sys/net/core/somaxconn so的值  
# 确保提高somaxconn和tcp_max_syn_backlog的值  
# 来获得想要的效果。 
#此参数确定了TCP连接中已完成队列(完成三次握手之后)的长度， 当然此值必须不大于Linux系统定义的/proc/sys/net/core/
#somaxconn值，默认是511，而Linux的默认参数值是128。当系统并发量大并且客户端速度缓慢的时候，可以将这二个参数一起参考设定。 
#
tcp-backlog 511

# Unix socket.
#
# Specify the path for the Unix socket that will be used to listen for
# incoming connections. There is no default, so Redis will not listen
# on a unix socket when not specified.
#
# 指定将用于监听的Unix套接字的路径  
# 传入的连接。 没有默认，所以Redis不会听  
# 在Unix套接字上指定。  
#
# unixsocket指定unix socket文件路径。
# unixsocketperm指定unix socket文件权限。
#
# unixsocket选项没有默认值，不指定unixsocket就不会监听任何。
# 如果不指定unixsocketperm，unix socket文件将使用默认权限（umask相关）
#
# unixsocket /tmp/redis.sock
# unixsocketperm 700

# Close the connection after a client is idle for N seconds (0 to disable)
# 客户端空闲N秒后关闭连接(0禁用)  0:代表永不断开
# 在timeout时间内如果没有数据交互，redis侧将关闭连接。
# 没有数据交互：redis客户端不向服务端发送任何数据。
#
timeout 0

# TCP keepalive.
#
# If non-zero, use SO_KEEPALIVE to send TCP ACKs to clients in absence
# of communication. This is useful for two reasons:
#
# 如果非零，使用SO_KEEPALIVE发送TCP ack给缺席的客户端  
# 的沟通。 这是有用的两个原因:  
#
# 1) Detect dead peers. 发现死去的同伴。
# 2) Take the connection alive from the point of view of network
#    equipment in the middle.
# 从中间网络设备的角度来看，连接是活的。
#
# On Linux, the specified value (in seconds) is the period used to send ACKs.
# Note that to close the connection the double of the time is needed.
# On other kernels the period depends on the kernel configuration.
#
# 在Linux系统下，该值为发送ack的周期，单位为秒。  
# 注意关闭连接需要双倍的时间。  
# 在其他内核上，周期取决于内核配置。  
#
#在linux系统中，客户端发送的最后一个数据包与redis发送的第一个保活探测报文之间的时间间隔。单位是秒。
#
#就是用来定时向client发送tcp_ack包来探测client是否存活的。默认不探测，官方建议值为60秒。假如连接断开了能及时知道
#
# A reasonable value for this option is 60 seconds.
# 这个选项的合理值是60秒。  
#
tcp-keepalive 0

################################# GENERAL #####################################

# By default Redis does not run as a daemon. Use 'yes' if you need it.
# Note that Redis will write a pid file in /var/run/redis.pid when daemonized.
# NOT SUPPORTED ON WINDOWS daemonize no
#
# 默认情况下，Redis不作为守护进程运行。 如果你需要，用“是”。  
# 注意Redis会在/var/run/ Redis中写入一个pid文件。 当监控pid。  
# 不支持WINDOWS守护进程  

# If you run Redis from upstart or systemd, Redis can interact with your
# supervision tree. Options:
#
#如果你从upstart或systemd运行Redis, Redis可以与你的监督树交互。 选项:  
#
#   supervised no      - no supervision interaction  没有监督互动
#   supervised upstart - signal upstart by putting Redis into SIGSTOP mode  信号upstart将Redis进入SIGSTOP模式 
#   supervised systemd - signal systemd by writing READY=1 to $NOTIFY_SOCKET  通过向$NOTIFY_SOCKET写入READY=1来通知系统  
#   supervised auto    - detect upstart or systemd method based on  检测upstart或systemd方法基于
#                        UPSTART_JOB or NOTIFY_SOCKET environment variables
# Note: these supervision methods only signal "process is ready."
#       They do not enable continuous liveness pings back to your supervisor.
# NOT SUPPORTED ON WINDOWS supervised no
# 注意:这些监督方法只表示“过程就绪”。 它们不能使连续的活动ping返回到您的上级。  
# 不支持WINDOWS监督号  
#

# If a pid file is specified, Redis writes it where specified at startup
# and removes it at exit.
#
# 如果指定了pid文件，Redis会在启动时将其写入指定的位置  
# 并在退出时删除它。  
#
# When the server runs non daemonized, no pid file is created if none is
# specified in the configuration. When the server is daemonized, the pid file
# is used even if not specified, defaulting to "/var/run/redis.pid".
#
# 当服务器非守护时，如果没有pid文件，则不创建pid文件  
# 在配置中指定。 当服务器被守护时，pid文件即使没有指定
# 也会被使用，默认为“/var/run/redis.pid”。  
#
# Creating a pid file is best effort: if Redis is not able to create it
# nothing bad happens, the server will start and run normally.
# NOT SUPPORTED ON WINDOWS pidfile /var/run/redis.pid
#
# 创建一个pid文件是最好的努力:如果Redis不能创建它  
# 如果没有什么坏事发生，服务器将正常启动并运行。  
# 不支持pidfile /var/run/redis.pid  

# Specify the server verbosity level.  指定服务器详细信息级别。
# This can be one of:  这可以是:
# debug (a lot of information, useful for development/testing)   大量信息，对开发/测试有用  
# verbose (many rarely useful info, but not a mess like the debug level)  许多很少有用的信息，但不像调试级别那样混乱  
# notice (moderately verbose, what you want in production probably)  比较详细，可能在生产中需要什么  
# warning (only very important / critical messages are logged)   只记录非常重要/关键的消息  
loglevel notice

# Specify the log file name. Also 'stdout' can be used to force
# Redis to log on the standard output.
#
# 指定日志文件名称。 'stdout'也可以用来强制Redis登录到标准输出。  
#
logfile ""

# To enable logging to the Windows EventLog, just set 'syslog-enabled' to
# yes, and optionally update the other syslog parameters to suit your needs.
# If Redis is installed and launched as a Windows Service, this will
# automatically be enabled.
#
# 要启用日志记录到Windows EventLog，只需将“syslog-enabled”设置为  
# yes，并可选地更新其他syslog参数以满足您的需要。  
# 如果Redis是作为Windows服务安装和启动的，这将自动启用。  
#
# syslog-enabled no

# Specify the source name of the events in the Windows Application log.
# 在Windows应用程序日志中指定事件的源名称。 
#
# syslog-ident redis

# Set the number of databases. The default database is DB 0, you can select
# a different one on a per-connection basis using SELECT <dbid> where
# dbid is a number between 0 and 'databases'-1
#
# 设置数据库数量。 默认数据库是DB 0，你可以使用select <dbid>在每个连接上选择一个不同的数据库，其中dbid是0到'databases'-1之间的数字  
#
databases 16

################################ SNAPSHOTTING  ################################
#
# Save the DB on disk: 将DB保存到磁盘:
#
#   save <seconds> <changes>  保存<秒> <修改>
#
#   Will save the DB if both the given number of seconds and the given
#   number of write operations against the DB occurred.
#
#   如果给定的秒数和给定的  发生对DB的写操作的次数
#
#   In the example below the behaviour will be to save:
#   after 900 sec (15 min) if at least 1 key changed
#   after 300 sec (5 min) if at least 10 keys changed
#   after 60 sec if at least 10000 keys changed
#
#   在下面的例子中，行为将是save:  
#   900秒(15分钟)后，如果至少有一个密钥改变  
#   300秒(5分钟)后，如果至少有10个键改变  
#   60秒后，如果至少有10000个密钥更改
#
#   Note: you can disable saving completely by commenting out all "save" lines.
#
#   注意:您可以通过注释掉所有“保存”行来完全禁用保存。
#
#   It is also possible to remove all the previously configured save
#   points by adding a save directive with a single empty string argument
#   like in the following example:
#
#   也可以通过添加一个带有空字符串参数的save指令来删除所有之前配置的保存点，就像下面的例子:
#
#   save ""

save 900 1
save 300 10
save 60 10000

# By default Redis will stop accepting writes if RDB snapshots are enabled
# (at least one save point) and the latest background save failed.
# This will make the user aware (in a hard way) that data is not persisting
# on disk properly, otherwise chances are that no one will notice and some
# disaster will happen.
#
# 默认情况下，如果RDB快照启用(至少一个保存点)，并且最近的后台保存失败，Redis将停止接受写操作。  
# 这将使用户意识到(以一种艰难的方式)数据没有正确地持久化到磁盘上，否则很可能没有人会注意到，从而发生一些灾难。  
#
# If the background saving process will start working again Redis will
# automatically allow writes again.
#
# 如果后台保存进程重新开始工作，Redis会自动允许再次写入。 
#
# However if you have setup your proper monitoring of the Redis server
# and persistence, you may want to disable this feature so that Redis will
# continue to work as usual even if there are problems with disk,
# permissions, and so forth.
#
# 然而，如果你已经设置了适当的监控Redis服务器和持久性，你可能想要禁用此功能，以便Redis将继续正常工作，即使有磁盘，权限等问题。  
# 
# 当生成 RDB 文件出错时是否继续处理 Redis 写命令，默认为不处理
#
stop-writes-on-bgsave-error yes

# Compress string objects using LZF when dump .rdb databases?
# For default that's set to 'yes' as it's almost always a win.
# If you want to save some CPU in the saving child set it to 'no' but
# the dataset will likely be bigger if you have compressible values or keys.
#
# 压缩字符串对象使用LZF转储。rdb数据库?  
# 默认设置为“yes”，因为它几乎总是一个胜利。  
# 如果你想节省一些CPU在保存子设置为'no'，但数据集可能会更大，如果你有可压缩的值或键  
#
#默认值是yes。对于存储到磁盘中的快照，可以设置是否进行压缩存储。
#如果是的话，redis会采用LZF算法进行压缩。如果你不想消耗CPU来进行压缩的话，可以设置为关闭此功能，但是存储在磁盘上的快照会比较大。
#
rdbcompression yes

# Since version 5 of RDB a CRC64 checksum is placed at the end of the file.
# This makes the format more resistant to corruption but there is a performance
# hit to pay (around 10%) when saving and loading RDB files, so you can disable it
# for maximum performances.
#
# 由于RDB版本5,CRC64校验和被放在文件的末尾。  
# 这使形式更能抵抗腐败，但有一个性能  
# 点击支付(大约10%)保存和加载RDB文件，所以你可以禁用它  
# 表示最大性能。  
#
# RDB files created with checksum disabled have a checksum of zero that will
# tell the loading code to skip the check.
#
# 禁用校验和创建的RDB文件的校验和为零，这将告诉加载代码跳过检查。  
#
# 默认值是yes。在存储快照后，我们还可以让redis使用CRC64算法来进行数据校验，
# 但是这样做会增加大约10%的性能消耗，如果希望获取到最大的性能提升，可以关闭此功能。
#
rdbchecksum yes

# The filename where to dump the DB  转储数据库的文件名
dbfilename dump.rdb

# The working directory.
#
# The DB will be written inside this directory, with the filename specified
# above using the 'dbfilename' configuration directive.
#
# DB将被写入到这个目录中，文件名使用上面的'dbfilename'配置指令指定。  
#
# The Append Only File will also be created inside this directory.
#
# 仅追加文件也将在这个目录中创建。
#
# Note that you must specify a directory here, not a file name.
#
# 注意，这里必须指定一个目录，而不是文件名。 
#
dir ./

################################# REPLICATION #################################

# Master-Slave replication. Use slaveof to make a Redis instance a copy of
# another Redis server. A few things to understand ASAP about Redis replication.
#
# 主从复制。 使用slaveof使一个Redis实例成为另一个Redis服务器的副本。 关于Redis复制的一些事情要了解ASAP。 
#
# 1) Redis replication is asynchronous, but you can configure a master to
#    stop accepting writes if it appears to be not connected with at least
#    a given number of slaves.
# 2) Redis slaves are able to perform a partial resynchronization with the
#    master if the replication link is lost for a relatively small amount of
#    time. You may want to configure the replication backlog size (see the next
#    sections of this file) with a sensible value depending on your needs.
# 3) Replication is automatic and does not need user intervention. After a
#    network partition slaves automatically try to reconnect to masters
#    and resynchronize with them.
#
# 1) Redis复制是异步的，但是你可以配置一个主服务器，当它看起来没有连接到至少给定数量的从服务器时，停止接受写操作。
# 2) 如果复制链路丢失了一段相对较短的时间，Redis从服务器能够执行与主服务器的部分重新同步。 您可能希望根据您的需要配置复制积压大小(请参阅本文件的下一部分)，并使用一个合理的值
# 3) 复制是自动的，不需要用户干预。 在网络分区之后，从服务器自动尝试重新连接到主服务器并与它们重新同步。  
#
# 例如: slaveof 127.0.0.1 6379
#
# slaveof <masterip> <masterport>

# If the master is password protected (using the "requirepass" configuration
# directive below) it is possible to tell the slave to authenticate before
# starting the replication synchronization process, otherwise the master will
# refuse the slave request.
#
# 如果主服务器是密码保护的(使用下面的“requirepass”配置指令)，可以在启动复制同步过程之前告诉从服务器进行身份验证，否则主服务器将拒绝从服务器的请求。
#
# 例如:masterauth 123456
#
# masterauth <master-password>

# When a slave loses its connection with the master, or when the replication
# is still in progress, the slave can act in two different ways:
#
# 当从机失去与主机的连接，或者复制仍在进行时，从机有两种不同的行为方式:  
#
# 1) if slave-serve-stale-data is set to 'yes' (the default) the slave will
#    still reply to client requests, possibly with out of date data, or the
#    data set may just be empty if this is the first synchronization.
#
# 2) if slave-serve-stale-data is set to 'no' the slave will reply with
#    an error "SYNC with master in progress" to all the kind of commands
#    but to INFO and SLAVEOF.
#
# 1)如果slave-server-stale-data设置为“yes”(默认)，slave仍然会回复客户端请求，可能会有过期的数据，或者如果这是第一次同步，数据集可能只是空的。  
# 2)如果slave-server-stale-data设置为“no”，那么除了INFO和SLAVEOF命令外，slave将对所有命令回复一个错误“SYNC with master in progress”。 
#
slave-serve-stale-data yes

# You can configure a slave instance to accept writes or not. Writing against
# a slave instance may be useful to store some ephemeral data (because data
# written on a slave will be easily deleted after resync with the master) but
# may also cause problems if clients are writing to it because of a
# misconfiguration.
#
# 你可以配置一个从属实例接受或不接受写入。 对从服务器实例的写入可能有助于存储一些短暂的数据(因为写在从服务器上的数据在与主服务器重新同步后很容易被删除)
# ，但如果客户端因为错误配置而写入它，也可能会导致问题。  
#
# Since Redis 2.6 by default slaves are read-only.
#
# 因为Redis 2.6默认从服务器是只读的。
#
# Note: read only slaves are not designed to be exposed to untrusted clients
# on the internet. It's just a protection layer against misuse of the instance.
# Still a read only slave exports by default all the administrative commands
# such as CONFIG, DEBUG, and so forth. To a limited extent you can improve
# security of read only slaves using 'rename-command' to shadow all the
# administrative / dangerous commands.
#
# 注意:只读从机不会被设计为暴露给互联网上的不受信任的客户端。 它只是防止滥用实例的保护层。 只读从机在默认情况下导出所有管理命令，如CONFIG、DEBUG等。 
# 在一定程度上，您可以使用'rename-command'来跟踪所有管理/危险命令来提高只读从服务器的安全性。
# 
# slave-read-only yes 从服务器只有读权限
# slave-read-only no  从服务器可以有读写权限
# 
slave-read-only yes

# Replication SYNC strategy: disk or socket.
#
# 复制同步策略:磁盘同步或套接字同步。
#
# -------------------------------------------------------
# WARNING: DISKLESS REPLICATION IS EXPERIMENTAL CURRENTLY
# 警告:无磁盘复制目前处于实验阶段  
# -------------------------------------------------------
#
# New slaves and reconnecting slaves that are not able to continue the replication
# process just receiving differences, need to do what is called a "full
# synchronization". An RDB file is transmitted from the master to the slaves.
# The transmission can happen in two different ways:
#
# 新的从机和重新连接的从机不能继续复制过程，只是接收差异，需要做所谓的“完全同步”。 RDB文件从主服务器传输到从服务器。 传播可以通过两种不同的方式发生:  
#
# 1) Disk-backed: The Redis master creates a new process that writes the RDB
#                 file on disk. Later the file is transferred by the parent
#                 process to the slaves incrementally.
# 2) Diskless: The Redis master creates a new process that directly writes the
#              RDB file to slave sockets, without touching the disk at all.
#
# 1) 磁盘支持:Redis master创建一个新的进程，将RDB文件写入磁盘。 之后，父进程将文件递增地传输给从进程。  
# 2) 无磁盘:Redis主进程创建一个新进程，直接将RDB文件写入从socket，而不需要接触磁盘。  
#
# With disk-backed replication, while the RDB file is generated, more slaves
# can be queued and served with the RDB file as soon as the current child producing
# the RDB file finishes its work. With diskless replication instead once
# the transfer starts, new slaves arriving will be queued and a new transfer
# will start when the current one terminates.
#
# 使用磁盘支持的复制，在生成RDB文件的同时，只要生成RDB文件的当前子进程完成它的工作，更多的从进程就可以排队并与RDB文件一起服务。 
# 而对于无磁盘复制，一旦传输开始，新的从机将进入队列，当当前传输终止时将开始新的传输。  
#
# When diskless replication is used, the master waits a configurable amount of
# time (in seconds) before starting the transfer in the hope that multiple slaves
# will arrive and the transfer can be parallelized.
#
# 当使用无磁盘复制时，主服务器在开始传输之前等待一段可配置的时间(以秒为单位)，希望多个从服务器能够到达，并将传输并行化。  
#
# With slow disks and fast (large bandwidth) networks, diskless replication
# works better.
#
# 对于慢速磁盘和快速(大带宽)网络，无磁盘复制工作得更好。  
# 
# repl-diskless-sync no  默认不使用diskless同步方式
#
repl-diskless-sync no

# When diskless replication is enabled, it is possible to configure the delay
# the server waits in order to spawn the child that transfers the RDB via socket
# to the slaves.
#
# 当启用无磁盘复制时，可以配置服务器等待的延迟，以便生成通过套接字将RDB传输到从服务器的子进程。  
#
# This is important since once the transfer starts, it is not possible to serve
# new slaves arriving, that will be queued for the next RDB transfer, so the server
# waits a delay in order to let more slaves arrive.
#
# 这是很重要的，因为一旦传输开始，就不可能为新的从服务器服务，这些新从服务器将排队等待下一个RDB传输，所以服务器等待延迟，以便让更多的从服务器到达。
#
# The delay is specified in seconds, and by default is 5 seconds. To disable
# it entirely just set it to 0 seconds and the transfer will start ASAP.
#
# 延迟时间以秒为单位设置，缺省值为5秒。 要完全禁用它，只需将其设置为0秒，传输将尽快开始。
#
repl-diskless-sync-delay 5

# Slaves send PINGs to server in a predefined interval. It's possible to change
# this interval with the repl_ping_slave_period option. The default value is 10
# seconds.
#
# 从服务发送ping到服务器在一个预定义的间隔。 可以使用repl_ping_slave_period选项更改这个时间间隔。 缺省值是10秒。 
#
# slave端向server端发送pings的时间区间设置，默认为10秒
#
# repl-ping-slave-period 10

# The following option sets the replication timeout for:
# 以下选项将复制超时设置为:  
#
# 1) Bulk transfer I/O during SYNC, from the point of view of slave.  
# 2) Master timeout from the point of view of slaves (data, pings).
# 3) Slave timeout from the point of view of masters (REPLCONF ACK pings).
#
# 1) 同步期间的批量传输I/O，从slave的角度来看。  
# 2) 从从站(数据，ping)的角度看主超时。 
# 3) 从主服务器的角度看从服务器超时(REPLCONF ACK ping)。
#
# It is important to make sure that this value is greater than the value
# specified for repl-ping-slave-period otherwise a timeout will be detected
# every time there is low traffic between the master and the slave.
#
# 重要的是要确保该值大于repl-ping-slave-period指定的值，否则每当主服务器和从服务器之间的通信流量较低时，就会检测到超时。  
#
# repl-timeout 60

# Disable TCP_NODELAY on the slave socket after SYNC?
# 在从端套接字上禁用TCP_NODELAY ?  
#
# If you select "yes" Redis will use a smaller number of TCP packets and
# less bandwidth to send data to slaves. But this can add a delay for
# the data to appear on the slave side, up to 40 milliseconds with
# Linux kernels using a default configuration.
#
# 如果你选择“是”，Redis将使用更少的TCP数据包和更少的带宽发送数据给从服务。 但是，这可能会增加数据出现在从端上的延迟，对于使用默认配置的Linux内核来说，最长可达40毫秒。
#
# If you select "no" the delay for data to appear on the slave side will
# be reduced but more bandwidth will be used for replication.
#
# 如果您选择“否”，数据出现在从端的延迟将会减少，但更多的带宽将用于复制。
#
# By default we optimize for low latency, but in very high traffic conditions
# or when the master and slaves are many hops away, turning this to "yes" may
# be a good idea.
#
# 默认情况下，我们优化低延迟，但在非常高的流量条件下，或当主服务器和从服务器相距许多跳时，将此设置为“是”可能是一个好主意。  
#
repl-disable-tcp-nodelay no

# Set the replication backlog size. The backlog is a buffer that accumulates
# slave data when slaves are disconnected for some time, so that when a slave
# wants to reconnect again, often a full resync is not needed, but a partial
# resync is enough, just passing the portion of data the slave missed while
# disconnected.
#
# 设置复制积压大小。 
# 积压是一个缓冲区，当从机断开连接一段时间后，它会积累从机数据，所以当从机想重新连接时，通常不需要完全重新同步，但部分重新同步就足够了，只需要传递从机断开连接时错过的那部分数据。  
#
# The bigger the replication backlog, the longer the time the slave can be
# disconnected and later be able to perform a partial resynchronization.
#
# 复制积压越大，从服务器断开连接的时间就越长，稍后就能够执行部分重新同步。  
#
# The backlog is only allocated once there is at least a slave connected.
#
# 只有在至少有一个从属连接的情况下，才会分配积压。  
#
#----------------------------------------------------------------------------
# 注意：
#
# repl_backlog_buffer 是一个环形缓冲区，主库会记录自己写到的位置，从库则会记录自己 已经读到的位置。
#
# 主从库的连接恢复之后，从库首先会给主库发送 psync 命令，并把自己当前的 slave_repl_offset 发给主库，主库会判断自己的 master_repl_offset 和 slave_repl_offset 之间的差距。 
#
# 在不影响正常业务的情况下redis主从同步时总会出现timeout，部分同步失败的情况。需要评估一下repl-backlog-size的大小，来避免复制时出现异常
#
# 这个参数和所需的缓冲空间大小有关。缓冲空间的计算公式是：缓冲空间大小 = 主库 写入命令速度 * 操作大小 - 主从库间网络传输命令速度 * 操作大小。在实际应用中，考虑 
# 到可能存在一些突发的请求压力，我们通常需要把这个缓冲空间扩大一倍，
#
# 即 repl_backlog_size = 缓冲空间大小 * 2，这也就是 repl_backlog_size 的最终值。
#
# 如果主库每秒写入 2000 个操作，每个操作的大小为 2KB，网络每秒能传输 1000 个操作，那么，有 1000 个操作需要缓冲起来，这就至少需要 2MB 的缓冲空间。否 
# 则，新写的命令就会覆盖掉旧操作了。为了应对可能的突发压力，我们最终把 repl_backlog_size 设为 4MB。
#
#
# repl-backlog-size 1mb

# After a master has no longer connected slaves for some time, the backlog
# will be freed. The following option configures the amount of seconds that
# need to elapse, starting from the time the last slave disconnected, for
# the backlog buffer to be freed.
#
# 当主服务器不再连接从服务器一段时间后，积压的事务将被释放。 下面的选项配置从最后一个从属服务器断开连接开始所需的秒数，以便释放积压缓冲区。  
#
# A value of 0 means to never release the backlog.
#
# 值为0意味着永远不发布积压。 
#
# repl-backlog-ttl 3600

# The slave priority is an integer number published by Redis in the INFO output.
# It is used by Redis Sentinel in order to select a slave to promote into a
# master if the master is no longer working correctly.
#
# slave优先级是一个由Redis在INFO输出中发布的整数。 Redis Sentinel使用它是为了在主服务器不能正常工作时选择一个从服务器提升为主服务器。  
#
# A slave with a low priority number is considered better for promotion, so
# for instance if there are three slaves with priority 10, 100, 25 Sentinel will
# pick the one with priority 10, that is the lowest.
#
# 一个优先级数字低的从机被认为更有利于提升，因此，如果有三个优先级为10,100,25的从机，哨兵会选择优先级为10的从机，这是最低的。  
#
# However a special priority of 0 marks the slave as not able to perform the
# role of master, so a slave with priority of 0 will never be selected by
# Redis Sentinel for promotion.
#
# 但是优先级为0的从机不能担任主的角色，所以优先级为0的从机永远不会被Redis哨兵选择升级。
#
# By default the priority is 100.
# 缺省情况下优先级为100。
#
slave-priority 100

# It is possible for a master to stop accepting writes if there are less than
# N slaves connected, having a lag less or equal than M seconds.
#
# 如果连接的从服务器少于N，且延迟小于或等于M秒，则主服务器可能停止接受写操作。  
#
# The N slaves need to be in "online" state.
#
# N个从服务需要处于“在线”状态。
#
# The lag in seconds, that must be <= the specified value, is calculated from
# the last ping received from the slave, that is usually sent every second.
#
# 延迟以秒为单位，必须<=指定的值，从从从接收到的最后一个ping计算，通常是每秒发送一次。 
#
# This option does not GUARANTEE that N replicas will accept the write, but
# will limit the window of exposure for lost writes in case not enough slaves
# are available, to the specified number of seconds.
#
# 这个选项不保证N个副本将接受写，但是在没有足够的slave可用的情况下，将限制丢失的写的暴露窗口到指定的秒数。  
#
# For example to require at least 3 slaves with a lag <= 10 seconds use:
#
# 例如，需要至少3个从服务，延迟<= 10秒使用:  
# 配置表示：从服务器的数量少于3个，或者三个从服务器的延迟（lag）值都大于或等于10秒时，主服务器将拒绝执行写命令。
#
# min-slaves-to-write 3
# min-slaves-max-lag 10
#
# Setting one or the other to 0 disables the feature.
#
# 将其中一个设置为0将禁用该特性。 
#
# By default min-slaves-to-write is set to 0 (feature disabled) and
# min-slaves-max-lag is set to 10.
#
# 缺省情况下，min-slave-to-write设置为0(特性禁用)，min-slave-max-lag设置为10。  
#

################################## SECURITY ###################################

# Require clients to issue AUTH <PASSWORD> before processing any other
# commands.  This might be useful in environments in which you do not trust
# others with access to the host running redis-server.
#
# 要求客户端在处理任何其他命令之前发出AUTH <PASSWORD>。 在您不相信其他人可以访问运行redis-server的主机的环境中，这可能很有用。  
#
# This should stay commented out for backward compatibility and because most
# people do not need auth (e.g. they run their own servers).
#
# 为了向后兼容，这应该被注释掉，因为大多数人不需要认证(例如，他们运行自己的服务器)。
#
# Warning: since Redis is pretty fast an outside user can try up to
# 150k passwords per second against a good box. This means that you should
# use a very strong password otherwise it will be very easy to break.
#
# 警告:由于Redis的速度非常快，外部用户每秒可以尝试多达150k个密码。 这意味着你应该使用一个非常强的密码，否则它将非常容易被破解。  
#
# requirepass foobared

# Command renaming.
# 命令重命名。
#
# It is possible to change the name of dangerous commands in a shared
# environment. For instance the CONFIG command may be renamed into something
# hard to guess so that it will still be available for internal-use tools
# but not available for general clients.
#
# 在共享环境中可以更改危险命令的名称。 例如，CONFIG命令可能会被重命名为一些难以猜测的东西，这样它仍然可以用于内部使用的工具，但不能用于普通客户端。  
#
# Example:
#
# rename-command CONFIG b840fc02d524045429941cc15f59e41cb7be6c52
#
# It is also possible to completely kill a command by renaming it into
# an empty string:
#
# 也可以通过将一个命令重命名为空字符串来完全终止它:  
#
# rename-command CONFIG ""
#
# Please note that changing the name of commands that are logged into the
# AOF file or transmitted to slaves may cause problems.
#
# 请注意，更改登录到AOF文件或传输给从服务的命令的名称可能会导致问题。  
#

################################### LIMITS ####################################

# Set the max number of connected clients at the same time. By default
# this limit is set to 10000 clients, however if the Redis server is not
# able to configure the process file limit to allow for the specified limit
# the max number of allowed clients is set to the current file limit
# minus 32 (as Redis reserves a few file descriptors for internal uses).
#
# 设置同时连接的最大客户端数。 默认情况下这个限制设置为10000个客户,但是如果复述,服务器不能配置过程文件限制允许指定限制允许的最大数量的客户设置为当前文件限制- 
# 32(复述,储备一些为内部使用文件描述符)。  
#
# Once the limit is reached Redis will close all the new connections sending
# an error 'max number of clients reached'.
#
# 一旦达到上限，Redis将关闭所有的新连接，发送一个错误'max number of clients reached'。  
#
# maxclients 10000

# If Redis is to be used as an in-memory-only cache without any kind of
# persistence, then the fork() mechanism used by the background AOF/RDB
# persistence is unnecessary. As an optimization, all persistence can be
# turned off in the Windows version of Redis. This will redirect heap
# allocations to the system heap allocator, and disable commands that would
# otherwise cause fork() operations: BGSAVE and BGREWRITEAOF.
# This flag may not be combined with any of the other flags that configure
# AOF and RDB operations.
#
# 如果Redis只是作为内存缓存而没有任何持久性，那么后台AOF/RDB持久性所使用的fork()机制就没有必要了。 作为一个优化，所有的持久性可以关闭在Windows版本的Redis。 
# 这将重定向堆分配到系统堆分配器，并禁用可能导致fork()操作的命令:BGSAVE和BGREWRITEAOF。 这个标志不能与任何其他配置AOF和RDB操作的标志结合。 
#
# persistence-available [(yes)|no]

# Don't use more memory than the specified amount of bytes.
# When the memory limit is reached Redis will try to remove keys
# according to the eviction policy selected (see maxmemory-policy).
#
# 不要使用超过指定字节量的内存。 当内存达到限制时，Redis会根据选择的eviction策略(参见maxmemory-policy)来删除键。 
#
# If Redis can't remove keys according to the policy, or if the policy is
# set to 'noeviction', Redis will start to reply with errors to commands
# that would use more memory, like SET, LPUSH, and so on, and will continue
# to reply to read-only commands like GET.
#
# 如果Redis不能根据策略删除键，或者策略被设置为“noeviction”，Redis会开始回复需要占用更多内存的命令，比如set, LPUSH等等，并且会继续回复只读命令，比如GET。 
#
# This option is usually useful when using Redis as an LRU cache, or to set
# a hard memory limit for an instance (using the 'noeviction' policy).
#
# 当使用Redis作为LRU缓存，或者为实例设置硬内存限制(使用'noeviction'策略)时，这个选项通常很有用。  
#
# WARNING: If you have slaves attached to an instance with maxmemory on,
# the size of the output buffers needed to feed the slaves are subtracted
# from the used memory count, so that network problems / resyncs will
# not trigger a loop where keys are evicted, and in turn the output
# buffer of slaves is full with DELs of keys evicted triggering the deletion
# of more keys, and so forth until the database is completely emptied.
#
# 警告: 如果你有一个附加在maxmemory上的slave实例，输出缓冲区的大小需要从使用的内存计数中减去，这样网络问题/重同步不会触发一个循环，键被驱逐， 
# 反过来，从服务器的输出缓冲区被逐出的键的DELs填满，从而触发更多的键的删除，以此类推，直到数据库完全清空。
#
# In short... if you have slaves attached it is suggested that you set a lower
# limit for maxmemory so that there is some free RAM on the system for slave
# output buffers (but this is not needed if the policy is 'noeviction').
#
# 总之…… 如果你附加了slave，建议你设置一个maxmemory的下限，这样系统上就有一些空闲RAM用于slave输出缓冲区(但如果策略是'noeviction'，这是不需要的)。 
#
# WARNING: not setting maxmemory will cause Redis to terminate with an
# out-of-memory exception if the heap limit is reached.
#
# 警告:如果没有设置maxmemory将导致Redis在达到堆限制时以内存不足异常终止。  
#
# NOTE: since Redis uses the system paging file to allocate the heap memory,
# the Working Set memory usage showed by the Windows Task Manager or by other
# tools such as ProcessExplorer will not always be accurate. For example, right
# after a background save of the RDB or the AOF files, the working set value
# may drop significantly. In order to check the correct amount of memory used
# by the redis-server to store the data, use the INFO client command. The INFO
# command shows only the memory used to store the redis data, not the extra
# memory used by the Windows process for its own requirements. Th3 extra amount
# of memory not reported by the INFO command can be calculated subtracting the
# Peak Working Set reported by the Windows Task Manager and the used_memory_peak
# reported by the INFO command.
#
# 注意:由于Redis使用系统分页文件来分配堆内存，Windows任务管理器或其他工具如ProcessExplorer显示的工作集内存使用情况并不总是准确的。 
# 例如，在后台保存RDB或AOF文件之后，工作集的值可能会显著下降。 为了检查redis-server用于存储数据的正确内存量，使用INFO client命令。 
# INFO命令只显示用于存储redis数据的内存，而不显示Windows进程用于自身需求的额外内存。 
# 通过减去Windows任务管理器报告的峰值工作集和INFO命令报告的used_memory_peak，可以计算出INFO命令没有报告的额外内存数量。  
#
# 设置最大内存，redis内存使用达到上限。可以通过设置LRU算法来删除部分key，释放空间。
#
# maxmemory <bytes>

# MAXMEMORY POLICY: how Redis will select what to remove when maxmemory
# is reached. You can select among five behaviors:
#
# MAXMEMORY POLICY:当达到MAXMEMORY时，Redis如何选择要删除的内容。 您可以在以下五种行为中进行选择:  
#
# volatile-lru -> remove the key with an expire set using an LRU algorithm  使用LRU算法删除具有过期设置的密钥  
# allkeys-lru -> remove any key according to the LRU algorithm  根据LRU算法移除任意密钥
# volatile-random -> remove a random key with an expire set  删除具有过期集的随机密钥
# allkeys-random -> remove a random key, any key  删除一个随机键，任意键
# volatile-ttl -> remove the key with the nearest expire time (minor TTL)  删除过期时间最近的密钥(次要TTL)  
# noeviction -> don't expire at all, just return an error on write operations  不要过期，只在写操作时返回错误  
#
# Note: with any of the above policies, Redis will return an error on write
#       operations, when there are no suitable keys for eviction.
#
# 注意:使用上面的任何策略，当没有合适的键退出时，Redis会在写操作中返回一个错误。  
#
#       At the date of writing these commands are: set setnx setex append
#       incr decr rpush lpush rpushx lpushx linsert lset rpoplpush sadd
#       sinter sinterstore sunion sunionstore sdiff sdiffstore zadd zincrby
#       zunionstore zinterstore hset hsetnx hmset hincrby incrby decrby
#       getset mset msetnx exec sort
#
# The default is:
#
# maxmemory-policy noeviction

# LRU and minimal TTL algorithms are not precise algorithms but approximated
# algorithms (in order to save memory), so you can tune it for speed or
# accuracy. For default Redis will check five keys and pick the one that was
# used less recently, you can change the sample size using the following
# configuration directive.
#
# LRU和最小TTL算法不是精确算法，而是近似算法(为了节省内存)，所以您可以调整它的速度或精度。 
# 默认的Redis会检查5个键，然后选择最近用得较少的那个，你可以使用下面的配置指令来改变样本的大小。 
#
# The default of 5 produces good enough results. 10 Approximates very closely
# true LRU but costs a bit more CPU. 3 is very fast but not very accurate.
#
# 默认值5会产生足够好的结果。 10非常接近真实的LRU，但花费更多的CPU。 3非常快，但不是很准确。  
#
# maxmemory-samples 5

############################## APPEND ONLY MODE ###############################

# By default Redis asynchronously dumps the dataset on disk. This mode is
# good enough in many applications, but an issue with the Redis process or
# a power outage may result into a few minutes of writes lost (depending on
# the configured save points).
#
# 默认情况下，Redis异步地将数据集转储到磁盘上。 这种模式在许多应用程序中已经足够好了，但是Redis进程的问题或停电可能会导致几分钟的写入丢失(取决于配置的保存点)。 
#
# The Append Only File is an alternative persistence mode that provides
# much better durability. For instance using the default data fsync policy
# (see later in the config file) Redis can lose just one second of writes in a
# dramatic event like a server power outage, or a single write if something
# wrong with the Redis process itself happens, but the operating system is
# still running correctly.
#
# 仅追加文件是另一种持久性模式，它提供了更好的持久性。 例如使用默认数据fsync策略配置文件中(见后)
# 复述,可以失去只是一秒的写在一个戏剧性的事件像一个服务器断电,或一个写如果复述过程本身出了问题,但正确操作系统仍在运行。  
#
# AOF and RDB persistence can be enabled at the same time without problems.
# If the AOF is enabled on startup Redis will load the AOF, that is the file
# with the better durability guarantees.
#
# 可以同时启用AOF和RDB持久性，而不会出现问题。 如果AOF在启动时启用，Redis将加载AOF，这是有更好的持久性保证的文件。  
#
# Please check http://redis.io/topics/persistence for more information.
#
# 请查看http://redis.io/topics/persistence了解更多信息。  
#
#是否开启aof
appendonly no

#是否开启混合持久化
#混合持久化生成一个aof文件，文件里面包含rdb和aof两种格式
#每次rewrite的时候会全局生成一个rdb快照，然后追加aof
#
aof-use-rdb-preamble yes

# The name of the append only file (default: "appendonly.aof")  仅追加文件的名称(默认:"appendonly.aof")  
appendfilename "appendonly.aof"

# The fsync() call tells the Operating System to actually write data on disk
# instead of waiting for more data in the output buffer. Some OS will really flush
# data on disk, some other OS will just try to do it ASAP.
#
# fsync()调用告诉操作系统实际将数据写入磁盘，而不是等待输出缓冲区中的更多数据。 有些操作系统真的会在磁盘上刷新数据，而有些操作系统只是想尽快完成。  
#
# Redis supports three different modes:
# Redis支持三种不同的模式:
#
# no: don't fsync, just let the OS flush the data when it wants. Faster.
# always: fsync after every write to the append only log. Slow, Safest.
# everysec: fsync only one time every second. Compromise.
#
# no:不要fsync，让操作系统在需要的时候刷新数据。 得更快。  
# always: fsync在每次写入仅追加日志之后。 缓慢的,安全的。  
# everysec:每秒只同步一次。 妥协。  
#
# The default is "everysec", as that's usually the right compromise between
# speed and data safety. It's up to you to understand if you can relax this to
# "no" that will let the operating system flush the output buffer when
# it wants, for better performances (but if you can live with the idea of
# some data loss consider the default persistence mode that's snapshotting),
# or on the contrary, use "always" that's very slow but a bit safer than
# everysec.
#
# 默认是“everysec”，因为这通常是速度和数据安全之间的折衷。 由你理解如果你能放松这个“不”字,让操作系统刷新输出缓冲区时,为了更好的表现(
# 但是如果你可以忍受一些数据丢失的想法考虑默认快照的持久性模式),或相反,使用“总是”非常缓慢但比everysec更安全一点。  
#
# More details please check the following article:
# http://antirez.com/post/redis-persistence-demystified.html
#
# If unsure, use "everysec".
# 如果不确定，使用“everysec”。
#

# appendfsync always
appendfsync everysec
# appendfsync no

# When the AOF fsync policy is set to always or everysec, and a background
# saving process (a background save or AOF log background rewriting) is
# performing a lot of I/O against the disk, in some Linux configurations
# Redis may block too long on the fsync() call. Note that there is no fix for
# this currently, as even performing fsync in a different thread will block
# our synchronous write(2) call.
#
# 当AOF fsync策略设置为always或everysec，并且后台保存进程(后台保存或AOF日志后台重写)对磁盘执行大量I/O时，在一些Linux配置中，Redis可能会在fsync()调用上阻塞太长时间。 
# 注意，目前还没有解决这个问题，因为即使在不同的线程中执行fsync也会阻塞我们的同步写(2)调用。
#
# In order to mitigate this problem it's possible to use the following option
# that will prevent fsync() from being called in the main process while a
# BGSAVE or BGREWRITEAOF is in progress.
#
# 为了缓解这个问题，可以使用以下选项来防止fsync()在执行BGSAVE或BGREWRITEAOF操作时在主进程中被调用。  
#
# This means that while another child is saving, the durability of Redis is
# the same as "appendfsync none". In practical terms, this means that it is
# possible to lose up to 30 seconds of log in the worst scenario (with the
# default Linux settings).
#
# 这意味着当另一个孩子在存钱时，Redis的持久性和“appendfsync none”是一样的。 在实践中，这意味着在最坏的情况下(使用默认的Linux设置)可能会丢失多达30秒的日志。 
#
# If you have latency problems turn this to "yes". Otherwise leave it as
# "no" that is the safest pick from the point of view of durability.
#
# 如果你有延迟问题，请将此设置为“是”。 否则，从持久性的角度来看，选择“不”是最安全的。
#
# bgrewriteaof机制，在一个子进程中进行aof的重写，从而不阻塞主进程对其余命令的处理，同时解决了aof文件过大问题。
#
# 现在问题出现了，同时在执行bgrewriteaof操作和主进程写aof文件的操作，两者都会操作磁盘，而bgrewriteaof往往会涉及大量磁盘操作，这样就会造成主进程在写aof文件的时候出现阻塞的情形，现在no-app
# endfsync-on-rewrite参数出场了。如果该参数设置为no，是最安全的方式，不会丢失数据，但是要忍受阻塞的问题。如果设置为yes呢？这就相当于将appendfsync设置为no，这说明并没有执行磁盘操作，只是写
# 入了缓冲区，因此这样并不会造成阻塞（因为没有竞争磁盘），但是如果这个时候redis挂掉，就会丢失数据。丢失多少数据呢？在linux的操作系统的默认设置下，最多会丢失30s的数据。
#
# 因此，如果应用系统无法忍受延迟，而可以容忍少量的数据丢失，则设置为yes。如果应用系统无法忍受数据丢失，则设置为no。
#
no-appendfsync-on-rewrite no

# Automatic rewrite of the append only file.
#
# 自动重写仅追加的文件。
#
# Redis is able to automatically rewrite the log file implicitly calling
# BGREWRITEAOF when the AOF log size grows by the specified percentage.
#
# 当AOF日志大小按指定百分比增长时，Redis能够隐式地调用BGREWRITEAOF自动重写日志文件。
#
# This is how it works: Redis remembers the size of the AOF file after the
# latest rewrite (if no rewrite has happened since the restart, the size of
# the AOF at startup is used).
#
# Redis的工作原理是这样的:Redis会记住最近一次重写后的AOF文件的大小(如果重启后没有重写，则使用启动时的AOF文件的大小)。  
#
# This base size is compared to the current size. If the current size is
# bigger than the specified percentage, the rewrite is triggered. Also
# you need to specify a minimal size for the AOF file to be rewritten, this
# is useful to avoid rewriting the AOF file even if the percentage increase
# is reached but it is still pretty small.
#
# 这个基本大小将与当前大小进行比较。 如果当前大小大于指定的百分比，则会触发重写。 
# 此外，您还需要指定要重写的AOF文件的最小大小，这有助于避免重写AOF文件，即使达到了百分比增长，但它仍然非常小。  
#
# Specify a percentage of zero in order to disable the automatic AOF
# rewrite feature.
#
# 指定一个百分比为零，以禁用自动AOF重写特性。  
#
# AOF文件最小重写大小，只有当AOF文件大小大于该值时候才可能重写,4.0默认配置64mb。
# auto-aof-rewrite-min-size 64mb
#
# 当前AOF文件大小和最后一次重写后的大小之间的比率等于或者等于指定的增长百分比，如100代表当前AOF文件是上次重写的两倍时候才重写。
# auto-aof-rewrite-percentage 100
#

auto-aof-rewrite-percentage 100
auto-aof-rewrite-min-size 64mb

# An AOF file may be found to be truncated at the end during the Redis
# startup process, when the AOF data gets loaded back into memory.
# This may happen when the system where Redis is running
# crashes, especially when an ext4 filesystem is mounted without the
# data=ordered option (however this can't happen when Redis itself
# crashes or aborts but the operating system still works correctly).
#
# 在Redis启动过程中，当AOF数据加载回内存时，可能会发现AOF文件在末尾被截断。 这可能发生在Redis运行的系统崩溃的时候，特别是当一个ext4文件系统没有安装data=ordered选项的时候(
# 但是这不会发生在Redis本身崩溃或中止但操作系统仍然正常工作的时候)。  
# 
# Redis can either exit with an error when this happens, or load as much
# data as possible (the default now) and start if the AOF file is found
# to be truncated at the end. The following option controls this behavior.
#
# 当这种情况发生时，Redis可以退出错误，或者加载尽可能多的数据(现在的默认值)，并在AOF文件被发现在结束时被截断时启动。 下面的选项控制此行为。  
#
# If aof-load-truncated is set to yes, a truncated AOF file is loaded and
# the Redis server starts emitting a log to inform the user of the event.
# Otherwise if the option is set to no, the server aborts with an error
# and refuses to start. When the option is set to no, the user requires
# to fix the AOF file using the "redis-check-aof" utility before to restart
# the server.
#
# 如果AOF -load-truncated被设置为yes，一个被截断的AOF文件被加载，Redis服务器开始发送日志来通知用户这个事件。 否则，如果该选项设置为no，服务器将终止错误并拒绝启动。 
# 当选项设置为no时，用户需要在重启服务器之前使用“redisc -check- AOF”工具修复AOF文件。 
#
# Note that if the AOF file will be found to be corrupted in the middle
# the server will still exit with an error. This option only applies when
# Redis will try to read more data from the AOF file but not enough bytes
# will be found.
#
# 注意，如果AOF文件在中间被发现损坏，服务器仍然会以错误退出。 这个选项只适用于当Redis将试图从AOF文件读取更多的数据，但没有找到足够的字节。
#
aof-load-truncated yes

################################ LUA SCRIPTING  ###############################

# Max execution time of a Lua script in milliseconds.
# Lua脚本的最大执行时间，以毫秒为单位。 
#
# If the maximum execution time is reached Redis will log that a script is
# still in execution after the maximum allowed time and will start to
# reply to queries with an error.
#
# 如果达到最大执行时间，Redis将记录脚本在最大允许的时间后仍然在执行，并将开始回复一个错误的查询。 
#
# When a long running script exceeds the maximum execution time only the
# SCRIPT KILL and SHUTDOWN NOSAVE commands are available. The first can be
# used to stop a script that did not yet called write commands. The second
# is the only way to shut down the server in the case a write command was
# already issued by the script but the user doesn't want to wait for the natural
# termination of the script.
#
# 当长时间运行的脚本超过最大执行时间时，只能使用script KILL和SHUTDOWN NOSAVE命令。 第一种方法可用于停止尚未调用写命令的脚本。 
# 如果脚本已经发出了写命令，但用户不想等待脚本的自然终止，那么第二种方法是关闭服务器的唯一方法。  
#
# Set it to 0 or a negative value for unlimited execution without warnings.
#
# 将它设置为0或负值，表示无限执行而不警告。 
#
lua-time-limit 5000

################################ REDIS CLUSTER  ###############################
#
# ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
# WARNING EXPERIMENTAL: Redis Cluster is considered to be stable code, however
# in order to mark it as "mature" we need to wait for a non trivial percentage
# of users to deploy it in production.
#
# 警告:Redis Cluster被认为是稳定的代码，但是为了标记它“成熟”，我们需要等待相当大比例的用户将它部署到生产环境中。  
# ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
#
# Normal Redis instances can't be part of a Redis Cluster; only nodes that are
# started as cluster nodes can. In order to start a Redis instance as a
# cluster node enable the cluster support uncommenting the following:
#
# 普通的Redis实例不能成为Redis集群的一部分; 只有作为集群节点启动的节点可以。 为了启动一个Redis实例作为集群节点，启用集群支持取消注释如下:  
#
# 作为集群节点启动
# cluster-enabled yes

# Every cluster node has a cluster configuration file. This file is not
# intended to be edited by hand. It is created and updated by Redis nodes.
# Every Redis Cluster node requires a different cluster configuration file.
# Make sure that instances running in the same system do not have
# overlapping cluster configuration file names.
#
# 每个集群节点都有一个集群配置文件。 此文件不打算手工编辑。 它由Redis节点创建和更新。 每个Redis Cluster节点都需要一个不同的集群配置文件。 
# 确保在同一系统中运行的实例没有重叠的集群配置文件名称。  
#
# cluster-config-file nodes-6379.conf

# Cluster node timeout is the amount of milliseconds a node must be unreachable
# for it to be considered in failure state.
# Most other internal time limits are multiple of the node timeout.
#
# 集群节点超时是指节点在故障状态下不可达的毫秒数。 大多数其他内部时间限制是节点超时的倍数。  
#
# cluster-node-timeout 15000

# A slave of a failing master will avoid to start a failover if its data
# looks too old.
#
# 失败的主服务器的从服务器将避免启动故障转移，如果它的数据看起来太旧。  
#
# There is no simple way for a slave to actually have a exact measure of
# its "data age", so the following two checks are performed:
#
# 没有一种简单的方法可以让一个slave真正精确地测量它的“数据时代”，所以执行以下两个检查:  
#
# 1) If there are multiple slaves able to failover, they exchange messages
#    in order to try to give an advantage to the slave with the best
#    replication offset (more data from the master processed).
#    Slaves will try to get their rank by offset, and apply to the start
#    of the failover a delay proportional to their rank.
#
# 2) Every single slave computes the time of the last interaction with
#    its master. This can be the last ping or command received (if the master
#    is still in the "connected" state), or the time that elapsed since the
#    disconnection with the master (if the replication link is currently down).
#    If the last interaction is too old, the slave will not try to failover
#    at all.
#
# 1) 如果有多个从机能够进行故障转移，则它们交换消息，以尽量使从机获得最佳复制偏移量(从机处理的数据更多)。 从机将尝试通过抵消获得他们的秩，并应用到故障转移的开始延迟与他们的秩成比例。 
# 2) 每一个从属节点都计算与它的主节点最后一次交互的时间。 这可能是最后一次收到ping或命令(如果主服务器仍然处于“连接”状态)，或者从与主服务器断开连接以来所经过的时间(如果复制链路当前断开)。 
#    如果最后的交互时间太长，从机根本不会尝试故障转移。 
#
# The point "2" can be tuned by user. Specifically a slave will not perform
# the failover if, since the last interaction with the master, the time
# elapsed is greater than:
#
# 点“2”可以由用户调整。 特别地，如果从主机与上一次交互的时间大于以下情况，从主机将不会执行故障转移:  
#
#   (node-timeout * slave-validity-factor) + repl-ping-slave-period
#
# So for example if node-timeout is 30 seconds, and the slave-validity-factor
# is 10, and assuming a default repl-ping-slave-period of 10 seconds, the
# slave will not try to failover if it was not able to talk with the master
# for longer than 310 seconds.
#
# 因此，例如，如果节点超时为30秒，从节点有效性因子为10，并假设默认的repl-ping-slave-period为10秒，那么如果从节点与主节点的通话时间超过310秒，它将不会尝试进行故障转移。
#
# A large slave-validity-factor may allow slaves with too old data to failover
# a master, while a too small value may prevent the cluster from being able to
# elect a slave at all.
#
# 一个较大的从服务器有效性因子可能允许数据太旧的从服务器对主服务器进行故障转移，而一个太小的值可能会阻止集群选择一个从服务器。  
#
# For maximum availability, it is possible to set the slave-validity-factor
# to a value of 0, which means, that slaves will always try to failover the
# master regardless of the last time they interacted with the master.
# (However they'll always try to apply a delay proportional to their
# offset rank).
#
# 为了获得最大可用性，可以将从服务器有效性因子设置为0，这意味着从服务器将始终尝试故障转移主服务器，而不管它们最后一次与主服务器交互是什么时候。 (
# 然而，它们总是尝试应用与它们的偏移秩成比例的延迟)。  
#
# Zero is the only value able to guarantee that when all the partitions heal
# the cluster will always be able to continue.
#
# 0是唯一能够保证当所有分区恢复时集群始终能够继续的值。
#
# cluster-slave-validity-factor 10

# Cluster slaves are able to migrate to orphaned masters, that are masters
# that are left without working slaves. This improves the cluster ability
# to resist to failures as otherwise an orphaned master can't be failed over
# in case of failure if it has no working slaves.
#
# 群集从站能够迁移到孤立的主站，即没有工作从站的主站。 这提高了集群抵抗故障的能力，否则，如果没有工作从站，则孤立的主节点在发生故障时无法进行故障转移。
#
# Slaves migrate to orphaned masters only if there are still at least a
# given number of other working slaves for their old master. This number
# is the "migration barrier". A migration barrier of 1 means that a slave
# will migrate only if there is at least 1 other working slave for its master
# and so forth. It usually reflects the number of slaves you want for every
# master in your cluster.
#
# 从站只有在他们的主站还有至少一定数量的其他工作从站时，才会迁移到主站。 这个数字就是“迁移障碍”。 迁移障碍为 1 意味着只有当其主站至少有 1 
# 个其他工作从属服务器时，从属服务器才会迁移，依此类推。 它通常反映群集中每个主站所需的从站数量。
#
# Default is 1 (slaves migrate only if their masters remain with at least
# one slave). To disable migration just set it to a very large value.
# A value of 0 can be set but is useful only for debugging and dangerous
# in production.
#
# 默认值是1(从站只有在他们的主站仍然与至少一个从站在一起时才迁移)。 要禁用迁移，只需将其设置为一个非常大的值。 可以设置值0，但它只在调试时有用，在生产环境中很危险。  
#
# 故障迁移：masert的子节点数量
# cluster-migration-barrier 1

# By default Redis Cluster nodes stop accepting queries if they detect there
# is at least an hash slot uncovered (no available node is serving it).
# This way if the cluster is partially down (for example a range of hash slots
# are no longer covered) all the cluster becomes, eventually, unavailable.
# It automatically returns available as soon as all the slots are covered again.
#
# 默认情况下，如果 Redis 集群节点检测到至少有一个哈希槽未被覆盖（没有可用的节点正在为其提供服务），则会停止接受查询。 
# 这样，如果群集部分关闭（例如，不再覆盖一系列哈希槽），则所有群集最终将不可用。 一旦所有插槽再次被覆盖，它就会自动恢复可用。
#
# However sometimes you want the subset of the cluster which is working,
# to continue to accept queries for the part of the key space that is still
# covered. In order to do so, just set the cluster-require-full-coverage
# option to no.
#
# 但是，有时您希望正在工作的集群子集继续接受对仍然覆盖的密钥空间部分的查询。 为了做到这一点，只需将cluster-require-full-coverage选项设置为no。  
#
# cluster-require-full-coverage yes

# In order to setup your cluster make sure to read the documentation
# available at http://redis.io web site.

################################## SLOW LOG ###################################

# The Redis Slow Log is a system to log queries that exceeded a specified
# execution time. The execution time does not include the I/O operations
# like talking with the client, sending the reply and so forth,
# but just the time needed to actually execute the command (this is the only
# stage of command execution where the thread is blocked and can not serve
# other requests in the meantime).
#
# Redis慢日志是一种记录超过指定执行时间的查询的系统。 执行时间不包括I / O操作,比如与客户端,发送应答等等,但就实际执行命令所需的时间(这是唯一阶段命令执行的线程被阻塞,不能同时处理其他请求)。
#
# You can configure the slow log with two parameters: one tells Redis
# what is the execution time, in microseconds, to exceed in order for the
# command to get logged, and the other parameter is the length of the
# slow log. When a new command is logged the oldest one is removed from the
# queue of logged commands.
#
#您可以用两个参数配置慢日志:一个参数告诉Redis要记录命令的执行时间(以微秒为单位)，另一个参数是慢日志的长度。 当记录新命令时，最旧的命令将从记录命令队列中删除。 

# The following time is expressed in microseconds, so 1000000 is equivalent
# to one second. Note that a negative number disables the slow log, while
# a value of zero forces the logging of every command.
#
# 下面的时间以微秒表示，因此1000000相当于1秒。 请注意，负数禁用慢日志，而值为零则强制记录每个命令。 
# 执行命令的时长超过这个阈值时就会被记录下来
#
slowlog-log-slower-than 10000

# There is no limit to this length. Just be aware that it will consume memory.
# You can reclaim memory used by the slow log with SLOWLOG RESET.
#
# 这个长度没有限制。 只需要注意它会消耗内存。 您可以使用SLOWLOG RESET回收慢日志使用的内存。 
# 指定慢查询日志最多存储的条数,慢日志达到最大条数以后，最旧的命令将从记录命令队列中删除。
#
slowlog-max-len 128

################################ LATENCY MONITOR ##############################

# The Redis latency monitoring subsystem samples different operations
# at runtime in order to collect data related to possible sources of
# latency of a Redis instance.
#
# Redis延迟监控子系统在运行时对不同的操作进行采样，以收集与Redis实例可能的延迟来源相关的数据。
#
# Via the LATENCY command this information is available to the user that can
# print graphs and obtain reports.
#
# 可以通过LATENCY命令打印图形和获取报告的用户可以使用这些信息。  
#
# The system only logs operations that were performed in a time equal or
# greater than the amount of milliseconds specified via the
# latency-monitor-threshold configuration directive. When its value is set
# to zero, the latency monitor is turned off.
#
# 系统只记录在等于或大于delay -monitor-threshold配置指令指定的毫秒数的时间内执行的操作。 当它的值设置为0时，将关闭延迟监视器。  
#
# By default latency monitoring is disabled since it is mostly not needed
# if you don't have latency issues, and collecting data has a performance
# impact, that while very small, can be measured under big load. Latency
# monitoring can easily be enabled at runtime using the command
# "CONFIG SET latency-monitor-threshold <milliseconds>" if needed.
#
# 默认情况下，延迟监视是禁用的，因为如果没有延迟问题，通常不需要它，而且收集数据会对性能产生影响，虽然影响很小，但在大负载下可以测量。 
# 如果需要，可以在运行时使用命令“CONFIG SET Latency -monitor-threshold <毫秒>”轻松启用延迟监视。
#
latency-monitor-threshold 0

############################# EVENT NOTIFICATION ##############################

# Redis can notify Pub/Sub clients about events happening in the key space.
# This feature is documented at http://redis.io/topics/notifications
#
# Redis可以通知Pub/Sub客户端key空间发生的事件。 这个特性在http://redis.io/topics/notifications上有文档记录  
#
# For instance if keyspace events notification is enabled, and a client
# performs a DEL operation on key "foo" stored in the Database 0, two
# messages will be published via Pub/Sub:
#
# 例如，如果启用了keyspace事件通知，客户端对存储在Database 0中的键foo执行DEL操作，两条消息将通过Pub/Sub发布:  
#
# PUBLISH __keyspace@0__:foo del
# PUBLISH __keyevent@0__:del foo
#
# It is possible to select the events that Redis will notify among a set
# of classes. Every class is identified by a single character:
#
# Redis可以在一组类中选择要通知的事件。 每个类都由一个字符标识:  
#
#  K     Keyspace events, published with __keyspace@<db>__ prefix.
#  E     Keyevent events, published with __keyevent@<db>__ prefix.
#  g     Generic commands (non-type specific) like DEL, EXPIRE, RENAME, ...
#  $     String commands
#  l     List commands
#  s     Set commands
#  h     Hash commands
#  z     Sorted set commands
#  x     Expired events (events generated every time a key expires)
#  e     Evicted events (events generated when a key is evicted for maxmemory)
#  A     Alias for g$lshzxe, so that the "AKE" string means all the events.
#
#  The "notify-keyspace-events" takes as argument a string that is composed
#  of zero or multiple characters. The empty string means that notifications
#  are disabled.
#
# “notify-keyspace-events”接受一个由零个或多个字符组成的字符串作为参数。 空字符串意味着禁用通知。  
#
#  Example: to enable list and generic events, from the point of view of the
#           event name, use:
#
# 示例:从事件名称的角度来看，要启用列表事件和通用事件，请使用:  
#
#  notify-keyspace-events Elg
#
#  Example 2: to get the stream of the expired keys subscribing to channel
#             name __keyevent@0__:expired use:
#
# 示例2:获取订阅通道名称__keyevent@0__的过期密钥流:  
#
#  notify-keyspace-events Ex
#
#  By default all notifications are disabled because most users don't need
#  this feature and the feature has some overhead. Note that if you don't
#  specify at least one of K or E, no events will be delivered.
#
# 默认情况下，所有通知都是禁用的，因为大多数用户不需要这个功能，而且这个功能有一些开销。 注意，如果您没有指定K或E中的至少一个，则不会传递任何事件。  
#
notify-keyspace-events ""

############################### ADVANCED CONFIG ###############################

# Hashes are encoded using a memory efficient data structure when they have a
# small number of entries, and the biggest entry does not exceed a given
# threshold. These thresholds can be configured using the following directives.
#
# 当哈希只有少量条目且最大条目不超过给定阈值时，使用内存高效的数据结构进行编码。 可以使用以下指令配置这些阈值。  
#
hash-max-ziplist-entries 512
hash-max-ziplist-value 64

# Lists are also encoded in a special way to save a lot of space.
# The number of entries allowed per internal list node can be specified
# as a fixed maximum size or a maximum number of elements.
# For a fixed maximum size, use -5 through -1, meaning:
# 
# 列表也以一种特殊的方式编码，以节省大量空间。 每个内部列表节点允许的条目数量可以指定为一个固定的最大大小或元素的最大数量。 对于固定的最大大小，使用-5到-1，这意味着:  
#
# -5: max size: 64 Kb  <-- not recommended for normal workloads  不建议用于正常工作负载
# -4: max size: 32 Kb  <-- not recommended  不推荐
# -3: max size: 16 Kb  <-- probably not recommended  可能不推荐
# -2: max size: 8 Kb   <-- good
# -1: max size: 4 Kb   <-- good
# Positive numbers mean store up to _exactly_ that number of elements
# per list node.
# The highest performing option is usually -2 (8 Kb size) or -1 (4 Kb size),
# but if your use case is unique, adjust the settings as necessary.
#
# 正数表示每个列表节点存储的元素数与列表节点的元素数完全相同。 最高性能的选项通常是-2 (8 Kb大小)或-1 (4 Kb大小)，但如果您的用例是唯一的，则根据需要调整设置。  
#
list-max-ziplist-size -2

# Lists may also be compressed.
# Compress depth is the number of quicklist ziplist nodes from *each* side of
# the list to *exclude* from compression.  The head and tail of the list
# are always uncompressed for fast push/pop operations.  Settings are:
#
# 列表也可以被压缩。 压缩深度是快速列表ziplist节点的数量，从列表的*每*一侧排除*压缩。 对于快速的推送/弹出操作，列表的头部和尾部总是未压缩的。 设置:  
#
# 0: disable all list compression
# 1: depth 1 means "don't start compressing until after 1 node into the list,
#    going from either the head or tail"
#    So: [head]->node->node->...->node->[tail]
#    [head], [tail] will always be uncompressed; inner nodes will compress.
# 2: [head]->[next]->node->node->...->node->[prev]->[tail]
#    2 here means: don't compress head or head->next or tail->prev or tail,
#    but compress all nodes between them.
# 3: [head]->[next]->[next]->node->node->...->node->[prev]->[prev]->[tail]
# etc.
#
# 0： 禁用所有列表压缩
# 1： 深度1表示“直到1个节点之后才开始压缩到列表中，从头部或尾部”  [头]、[尾]永远不会压缩; 内部节点会压缩。
# 2： (头)- >(下)- >节点- >节点- >… ->node->[prev]->[tail] 2这里的意思是:不要压缩head或head->next或tail->prev或tail，而是压缩它们之间的所有节点。
# 3： (头)- >[下]- >[下]- >节点- >节点- >…- >节点- >[上一页]- >[上一页]- >[尾巴]等。 
list-compress-depth 0

# Sets have a special encoding in just one case: when a set is composed
# of just strings that happen to be integers in radix 10 in the range
# of 64 bit signed integers.
# The following configuration setting sets the limit in the size of the
# set in order to use this special memory saving encoding.
#
# 集合在一种情况下有一种特殊的编码:当一个集合由恰好是64位有符号整数范围内基数为10的整数的字符串组成时。 为了使用这种特殊的内存保存编码，下面的配置设置设置了集合大小的限制。
#
set-max-intset-entries 512

# Similarly to hashes and lists, sorted sets are also specially encoded in
# order to save a lot of space. This encoding is only used when the length and
# elements of a sorted set are below the following limits:
#
# 与散列和列表类似，为了节省大量空间，也对排序集进行了特殊编码。 这种编码仅在排序集的长度和元素低于以下限制时使用:  
#
zset-max-ziplist-entries 128
zset-max-ziplist-value 64

# HyperLogLog sparse representation bytes limit. The limit includes the
# 16 bytes header. When an HyperLogLog using the sparse representation crosses
# this limit, it is converted into the dense representation.
#
# HyperLogLog稀疏表示字节限制。 该限制包括16字节的报头。 当使用稀疏表示的HyperLogLog越过这一限制时，它将被转换为密集表示。
#
# A value greater than 16000 is totally useless, since at that point the
# dense representation is more memory efficient.
#
# 大于16000的值是完全无用的，因为此时密集表示的内存效率更高。
#
# The suggested value is ~ 3000 in order to have the benefits of
# the space efficient encoding without slowing down too much PFADD,
# which is O(N) with the sparse encoding. The value can be raised to
# ~ 10000 when CPU is not a concern, but space is, and the data set is
# composed of many HyperLogLogs with cardinality in the 0 - 15000 range.
#
# 建议值为~ 3000，这样既可以实现空间高效编码，又不会对PFADD造成太大的影响，PFADD的稀疏编码为O(N)。 
# 当不需要考虑CPU而需要考虑空间时，该值可以提高到~ 10000，并且数据集由许多hyperlogglogs组成，基数在0 - 15000范围内。 
#
hll-sparse-max-bytes 3000

# Active rehashing uses 1 millisecond every 100 milliseconds of CPU time in
# order to help rehashing the main Redis hash table (the one mapping top-level
# keys to values). The hash table implementation Redis uses (see dict.c)
# performs a lazy rehashing: the more operation you run into a hash table
# that is rehashing, the more rehashing "steps" are performed, so if the
# server is idle the rehashing is never complete and some more memory is used
# by the hash table.
#
# 主动重哈希每100毫秒使用1毫秒的CPU时间来帮助重哈希主Redis哈希表(映射顶级键到值的一个)。 哈希表实现复述,使用(见dict.c)
# 执行一个懒惰改作:操作越多改作遇到一个哈希表,越改作“步骤”执行,如果服务器空闲时再处理就不算完整和一些更多的内存使用哈希表。
#
# The default is to use this millisecond 10 times every second in order to
# actively rehash the main dictionaries, freeing memory when possible.
#
# 默认情况下，每秒使用该毫秒10次，以便主动重新散列主字典，尽可能释放内存。 
#
# If unsure:
# use "activerehashing no" if you have hard latency requirements and it is
# not a good thing in your environment that Redis can reply from time to time
# to queries with 2 milliseconds delay.
#
# 如果不确定:  
# 如果你对延迟有硬性要求，那就使用“activerehash no”。在你的环境中，Redis时不时地以2毫秒的延迟回复查询并不是一件好事。  
#
# use "activerehashing yes" if you don't have such hard requirements but
# want to free memory asap when possible.
#
# 如果你没有这样的硬性要求，但希望尽可能尽快释放内存，请使用“主动哈希是”。  
#
activerehashing yes

# The client output buffer limits can be used to force disconnection of clients
# that are not reading data from the server fast enough for some reason (a
# common reason is that a Pub/Sub client can't consume messages as fast as the
# publisher can produce them).
#
# 客户端输出缓冲区限制可用于强制断开由于某种原因从服务器读取数据不够快的客户端连接(一个常见的原因是Pub/Sub客户端使用消息的速度不及发布者产生消息的速度)。 
#
# The limit can be set differently for the three different classes of clients:
#
# 可以为三类不同的客户端设置不同的限制:  
#
# normal -> normal clients including MONITOR clients   正常的客户端，包括MONITOR客户端
# slave  -> slave clients  从机的客户端
# pubsub -> clients subscribed to at least one pubsub channel or pattern   客户端订阅了至少一个发布-订阅通道或模式  
#
# The syntax of every client-output-buffer-limit directive is the following:
#
# 每个client-output-buffer-limit指令的语法如下:  
#
# client-output-buffer-limit <class> <hard limit> <soft limit> <soft seconds>
#
# Client-output-buffer-limit <class> <硬限制> <软限制> <软秒>  
#
# A client is immediately disconnected once the hard limit is reached, or if
# the soft limit is reached and remains reached for the specified number of
# seconds (continuously).
# So for instance if the hard limit is 32 megabytes and the soft limit is
# 16 megabytes / 10 seconds, the client will get disconnected immediately
# if the size of the output buffers reach 32 megabytes, but will also get
# disconnected if the client reaches 16 megabytes and continuously overcomes
# the limit for 10 seconds.
#
# 一旦达到硬限制，客户端立即断开连接，或者如果达到软限制，并且在指定的秒数内(连续)保持连接。  
# 比如如果硬限制是32字节和软限制是16 mb / 10秒,客户端会立即断开输出缓冲区的大小达到32字节,但也会断开如果客户达到16字节,不断克服了限制10秒钟。
#
# By default normal clients are not limited because they don't receive data
# without asking (in a push way), but just after a request, so only
# asynchronous clients may create a scenario where data is requested faster
# than it can read.
#
# 默认情况下，普通客户端不受限制，因为它们不会在没有请求的情况下接收数据(以push方式)，而是在请求之后接收数据，所以只有异步客户端可能会创建这样一种场景:数据请求的速度比读取的速度快。  
#
# Instead there is a default limit for pubsub and slave clients, since
# subscribers and slaves receive data in a push fashion.
#
# 相反，对于发布和从服务器有一个默认的限制，因为订阅者和从服务器以推送的方式接收数据。 
#
# Both the hard or the soft limit can be disabled by setting them to zero.
# 可以通过将硬限制或软限制设置为零来禁用它们。  
#
client-output-buffer-limit normal 0 0 0
client-output-buffer-limit slave 256mb 64mb 60
client-output-buffer-limit pubsub 32mb 8mb 60

# Redis calls an internal function to perform many background tasks, like
# closing connections of clients in timeot, purging expired keys that are
# never requested, and so forth.
#
# Redis调用一个内部函数来执行许多后台任务，比如timeot关闭客户端连接，清除从未被请求的过期密钥，等等。  
#
# Not all tasks are perforemd with the same frequency, but Redis checks for
# tasks to perform according to the specified "hz" value.
#
# 不是所有的任务都以相同的频率执行，但是Redis会根据指定的“hz”值来检查任务是否执行。
#
# By default "hz" is set to 10. Raising the value will use more CPU when
# Redis is idle, but at the same time will make Redis more responsive when
# there are many keys expiring at the same time, and timeouts may be
# handled with more precision.
#
# 缺省情况下，“hz”为10。 提高该值将使用更多的CPU时，Redis是空闲的，但同时将使Redis更响应时，有许多键在同一时间过期，超时可能会处理更精确。  
#
# The range is between 1 and 500, however a value over 100 is usually not
# a good idea. Most users should use the default of 10 and raise this up to
# 100 only in environments where very low latency is required.
#
# 范围在1到500之间，但是超过100通常不是一个好主意。 大多数用户应该使用默认值10，并只在需要非常低延迟的环境中将其提高到100。  
#
hz 10

# When a child rewrites the AOF file, if the following option is enabled
# the file will be fsync-ed every 32 MB of data generated. This is useful
# in order to commit the file to the disk more incrementally and avoid
# big latency spikes.
#
# 当子进程重写AOF文件时，如果启用了以下选项，则每生成32mb的数据就会对文件进行fsync。 这对于以更增量的方式将文件提交到磁盘并避免大的延迟峰值非常有用。 
#
aof-rewrite-incremental-fsync yes

################################## INCLUDES ###################################

# Include one or more other config files here.  This is useful if you
# have a standard template that goes to all Redis server but also need
# to customize a few per-server settings.  Include files can include
# other files, so use this wisely.
#
# 在这里包含一个或多个其他配置文件。 如果你有一个标准的模板去所有的Redis服务器，但也需要定制一些每个服务器的设置，这是很有用的。 包含文件可以包含其他文件，所以要明智地使用它。
#
# include /path/to/local.conf
# include /path/to/other.conf
```