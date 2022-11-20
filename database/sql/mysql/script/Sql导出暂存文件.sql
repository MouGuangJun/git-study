/*
SQLyog Ultimate v12.08 (64 bit)
MySQL - 5.5.60 
*********************************************************************
*/
/*!40101 SET NAMES utf8 */;

create table `sp_manager` (
	`mg_id` int (11),
	`mg_name` varchar (96),
	`mg_pwd` char (192),
	`mg_time` int (10),
	`role_id` tinyint (11),
	`mg_mobile` varchar (96),
	`mg_email` varchar (192),
	`mg_state` tinyint (2)
); 
insert into `sp_manager` (`mg_id`, `mg_name`, `mg_pwd`, `mg_time`, `role_id`, `mg_mobile`, `mg_email`, `mg_state`) values('500','admin','$2y$10$sZlpZNoLAnoD1DtYO9REAODCPkpMb5bwl4oMzrMvJa83k9BY3KRwq','1486720211','0','12345678','adsfad@qq.com','1');
insert into `sp_manager` (`mg_id`, `mg_name`, `mg_pwd`, `mg_time`, `role_id`, `mg_mobile`, `mg_email`, `mg_state`) values('502','linken','$2y$10$sZlpZNoLAnoD1DtYO9REAODCPkpMb5bwl4oMzrMvJa83k9BY3KRwq','1486720211','34','1213213123','asdf@qq.com','0');
insert into `sp_manager` (`mg_id`, `mg_name`, `mg_pwd`, `mg_time`, `role_id`, `mg_mobile`, `mg_email`, `mg_state`) values('508','asdf1','$2y$10$uPh6Rb58Q5XSARplvJC0N.2fmFizK5snFcHDcTu4e.EiJEyGvcwiq','1511853015','30','123123','adfsa@qq.com','1');
insert into `sp_manager` (`mg_id`, `mg_name`, `mg_pwd`, `mg_time`, `role_id`, `mg_mobile`, `mg_email`, `mg_state`) values('509','asdf123','$2y$10$sZlpZNoLAnoD1DtYO9REAODCPkpMb5bwl4oMzrMvJa83k9BY3KRwq','1511853353','40','1111','asdf@qq.com','0');
insert into `sp_manager` (`mg_id`, `mg_name`, `mg_pwd`, `mg_time`, `role_id`, `mg_mobile`, `mg_email`, `mg_state`) values('512','admin1','$2y$10$0sKmRkkmpvhOn61GFWLQGOK4A3vNXF4mv8lmAryVUE3/OaiVOod/6','1668775694','-1','13587932543','1977985@qq.com',NULL);
