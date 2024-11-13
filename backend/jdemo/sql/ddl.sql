-- 创建库
create database if not exists api;

-- 切换库
use api;

-- 用户表
create table if not exists user (
    id bigint auto_increment comment 'id' primary key,
    userName varchar(256) null comment '用户昵称',
    userAccount varchar(256) not null comment '账号',
    userAvatar varchar(1024) null comment '用户头像',
    gender tinyint null comment '性别',
    userRole varchar(256) default 'user' not null comment '用户角色：user/admin',
    userPassword varchar(512) not null comment '密码',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete tinyint default 0 not null comment '是否删除',
	accessKey varchar(512) not null comment 'accessKey',
	secretKey varchar(512) not null comment 'secretKey',
    constraint uni_userAccount unique (userAccount)
) comment '用户';

-- 接口信息表
create table if not exists apiinfo (
	id bigint auto_increment comment 'id' primary key,
	name varchar(256) not null comment '接口名称',
	createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
	updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
	isDelete tinyint default 0 not null comment '是否删除',
	description varchar(256) null comment '接口描述',
	url varchar(512) not null comment '接口地址',
	`requestParams` text null comment '请求参数',
	requestHeader text null comment '请求头',
	responseHeader text null comment '响应头',
	status int default 0 not null comment '接口状态0-关闭，1-开启',
	method varchar(256) not null comment '请求类型',
	userId bigint not null comment '创建人'
) comment '接口信息表';

-- 用户调用接口关系表：
create table if not exists api.`user_interface_info` (
  id bigint not null auto_increment comment '主键' primary key,
  userId bigint not null comment '调用用户id',
  interfaceInfoId bigint not null comment '接口id',
  totalNum int default 0 not null comment '总调用次数',
  leftNum int default 0 not null comment '剩余调用次数',
  `status` int default 0 not null comment '0-正常；1-禁用',
  createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
  updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
  isDelete tinyint default 0 not null comment '是否删除'
) comment '用户调用接口关系表'; 

-- mock一些假数据喂入：
insert into api.apiinfo (name,description,url,method,userId) values ('李一','李一','http://liyi.com','get',1112);
insert into api.apiinfo (name,description,url,method,userId) values ('网上光','网上光','http://lifdsyi.com','get',5412);
insert into api.apiinfo (name,description,url,method,userId) values ('刘思','刘思','http://livcsdyi.com','get',12);
insert into api.apiinfo (name,description,url,method,userId) values ('赵强','赵强','http://lifdsewyi.com','get',22);
insert into api.apiinfo (name,description,url,method,userId) values ('刚子','刚子','http://ligfhyi.com','get',412);
insert into api.apiinfo (name,description,url,method,userId) values ('果子狸','果子狸','http://liygfdgi.com','get',992);
insert into api.apiinfo (name,description,url,method,userId) values ('麦麦','麦麦','http://ligvdfbvcyi.com','get',62);
insert into api.apiinfo (name,description,url,method,userId) values ('小强','小强','http://lvgdfgiyi.com','get',512);
insert into api.apiinfo (name,description,url,method,userId) values ('丽妃','丽妃','http://ligfdyi.com','get',6782);

insert into api.`user` (userName,userAccount,userAvatar,gender,userRole, userPassword) 
	values ('victory','victory','https://api.dicebear.com/9.x/glass/svg',0,'admin','12345678');
insert into api.`user` (userName,userAccount,userAvatar,gender,userRole, userPassword) 
	values ('yaya','yaya','https://api.dicebear.com/9.x/dylan/svg',0,'user','12345678');
insert into api.`user` (userName,userAccount,userAvatar,gender,userRole, userPassword) 
	values ('susu','susu','https://api.dicebear.com/9.x/micah/svg',0,'user','12345678');
insert into api.`user` (userName,userAccount,userAvatar,gender,userRole, userPassword) 
	values ('wuwu','wuwu','https://api.dicebear.com/9.x/miniavs/svg',0,'user','12345678');

