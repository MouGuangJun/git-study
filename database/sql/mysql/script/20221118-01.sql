SELECT * FROM sp_manager;

INSERT INTO `sp_manager` (`mg_id`, `mg_name`, `mg_pwd`, `mg_time`, `role_id`, `mg_mobile`, `mg_email`, `mg_state`) VALUES('512','admin1','$2y$10$0sKmRkkmpvhOn61GFWLQGOK4A3vNXF4mv8lmAryVUE3/OaiVOod/6','1668775694','-1','13587932543','1977985@qq.com',NULL);


SELECT * FROM sp_role;
UPDATE sp_role SET ps_ids = '102,0,107,109,154,155,145,146,148' WHERE role_id = '44';



SELECT * FROM sp_user;