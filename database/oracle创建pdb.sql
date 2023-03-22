--切换到oracle用户
linux：su - oracle
--启动oracle监听
linux：lsnrctl start|status|stop

--Oracle需要启动的服务：
--OracleOraDB19Home1MTSRecoveryService、OracleOraDB19Home1TNSListener、OracleRemExecServiceV2、OracleServiceORCL、OracleVssWriterORCL
--以sysdba的身份登录
sqlplus / as sysdba

--查看连接名称
show con_name;

--如果得到的结果为YES，那么就是CDB的数据库，否则，则不是。
select CDB from v$database;

--创建用户：  在CDB容器中创建用户时，前面必须添加C## ,而PDB数据库不需要加前缀
create user c##ROOT identified by 123456;


--其中pdb1是我创建的可插接式数据库，pdb1是创建的用户，123456是密码。file_name_convert换成相应目录就OK了
create pluggable database pdb1 admin user pdb1 identified by 123456 roles=(connect) file_name_convert=('D:\Program_Files\app\root\oradata\ORCL\pdbseed','D:\Program_Files\app\root\oradata\ORCL\pdb1');

--打开所有的插接式数据库（如果想指定数据库，那么将all改为对应的pdb名称即可）
alter pluggable database all open;
--所有的PDB都能够在CDB打开后能够自己开启【save state命令保存的是我们键入此命令前对应PDB的状态！】
alter pluggable database all save state;
--把所有PDB的状态设置都清除
alter pluggable database all discard state;

--把数据库整个都关闭
shutdown immediate;

--启动数据库
startup;


--在PDB数据库操作：【基于已在容器中创建】
--切入要操作的pdb
show pdbs; -- 展示pdb数据库集合
alter session set container=PDB1; -- 切入到PDB 数据库
alter session set container=CDB$ROOT; -- 切回到CDB 容器数据库

--创建用户，分配角色
-- 创建用户
create user pdb1admin identified by pdb1admin;
 
--分配权限
grant create session to pdb1admin;
grant create table to pdb1admin;
grant create tablespace to pdb1admin;
grant create view to pdb1admin;
grant connect,resource to pdb1admin;


--查询pdb下的所有创建的新用户
select a.name,b.username, b.password,b.created 
from v$pdbs a left join cdb_users b on a.con_id = b.con_id 
where a.con_id in(3,4,5)
--排除掉系统自己创建的用户，这个时间可根据情况选择
and to_char(b.created,'yyyymmdd')!='20190530'
--可以对指定的pdb进行用户查询
--and a.name = 'pdb1'
order by 1;

--创建表空间
--create tablespace TSCCMS logging datafile 'D:\oracle\oradata\TSCCMS01.dbf' size 2048M autoextend on next 200M maxsize 20480M extent management local;
create tablespace pdb1admin datafile 'D:\oracle\oradata\pdb1admin.DBF' size 100m;

--删除表空间 【此操作不会删除物理文件】
drop tablespace pdb1admin;

--更改数据文件大小
alter database datafile 'D:\oracle\oradata\pdb1admin.DBF' resize 50m;

--创建数据库用户并赋予权限
create user CCMS identified by CCMS default tablespace TSCCMS;
grant connect ,resource,dba  to  CCMS;
grant create session  to  CCMS;

--创建数据库文件夹并赋予权限
--在linux操作系统上时，需要注意文件夹权限属于哪个用户？ 重新给文件夹赋权：chown -R oracle:oinstall dir_CCMS 
create or replace directory  dir_CCMS as 'D:\oracle\dir_CCMS';
grant read ,write on directory   dir_CCMS to CCMS ;



-------导入数据开始 以下用到ORCLPDB的地方，以后都修改为自己的容器--------------
--这里需要配置ORCLPDB的监听 listener.ora 和 tnsnames.ora
--监听相关命令：lsnrctl stop/start/status
-- listener.ora需要追加内容：
SID_LIST_LISTENER =
   (SID_LIST =
       (SID_DESC =
           (GLOBAL_DBNAME = ORCLPDB)
           (SID_NAME = ORCLPDB)
        )
    )
	
-- tnsnames.ora需要追加内容：
PDB1 =
  (DESCRIPTION =
    (ADDRESS = (PROTOCOL = TCP)(HOST = oracle1)(PORT = 1521))
    (CONNECT_DATA =
      (SERVER = DEDICATED)
      (SERVICE_NAME = PDB1)
    )
  )


--从dmp中将数据导入到pdb中【需要将dumpfile放到数据库文件夹--dir_CCMS中】
impdp CCMS/CCMS@ORCLPDB full=y directory=dir_CCMS dumpfile=credit_20220224.dmp remap_schema=CCMS:CCMS remap_tablespace=TSCCMS:TSCCMS table_exists_action=replace logfile =CCMS_20220501.log

--所有创建用户导入文件的命令
--CCMS
--创建CCMS表空间
create tablespace TSCCMS logging datafile 'D:\oracle\oradata\TSCCMS01.dbf' size 2048M autoextend on next 200M maxsize 20480M extent management local;
--创建CCMS用户并授权
create user CCMS identified by CCMS default tablespace TSCCMS;
grant connect ,resource,dba  to  CCMS;
grant create session  to  CCMS;

--GCMS
--创建GCMS表空间
create tablespace TSGCMS logging datafile 'D:\oracle\oradata\TSGCMS01.dbf' size 2048M autoextend on next 200M maxsize 20480M extent management local;
--创建GCMS用户并授权
create user GCMS identified by GCMS default tablespace TSGCMS;
grant connect ,resource,dba  to  GCMS;
grant create session  to  GCMS;


--CLMS
--创建CLMS表空间
create tablespace TSCLMS logging datafile 'D:\oracle\oradata\TSCLMS01.dbf' size 2048M autoextend on next 200M maxsize 20480M extent management local;
--创建CLMS用户并授权
create user CLMS identified by CLMS default tablespace TSCLMS;
grant connect ,resource,dba  to  CLMS;
grant create session  to  CLMS;

--导入dmp文件语句
impdp CCMS/CCMS@ORCLPDB full=y directory=dir_CCMS dumpfile=credit_20220224.dmp remap_schema=CCMS:CCMS remap_tablespace=TSCCMS:TSCCMS table_exists_action=replace logfile =CCMS_20220501.log


--导入表时，出现以下的错误
