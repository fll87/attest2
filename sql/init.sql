-- ----------------------------
-- 空表 用于验证
-- ----------------------------
DROP TABLE IF EXISTS "dual";
CREATE TABLE "dual" ();

-- ----------------------------
-- 参数配置表
-- ----------------------------
DROP TABLE IF EXISTS "sys_config";
CREATE TABLE "sys_config" (
  "config_id" SERIAL PRIMARY KEY,
  "config_type" int2 NOT NULL,
  "config_name" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "config_key" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "config_value" varchar(500) COLLATE "pg_catalog"."default" NOT NULL,
  "remark" varchar(500) COLLATE "pg_catalog"."default",
  "creator" varchar(64) COLLATE "pg_catalog"."default",
  "create_time" timestamp NOT NULL,
  "updater" varchar(64) COLLATE "pg_catalog"."default",
  "update_time" timestamp DEFAULT NULL,
  "deleted" int2 NOT NULL DEFAULT 0
);
COMMENT ON COLUMN "sys_config"."config_id" IS '参数主键';
COMMENT ON COLUMN "sys_config"."config_type" IS '系统内置（1是 2否）';
COMMENT ON COLUMN "sys_config"."config_name" IS '参数名称';
COMMENT ON COLUMN "sys_config"."config_key" IS '参数键名';
COMMENT ON COLUMN "sys_config"."config_value" IS '参数键值';
COMMENT ON COLUMN "sys_config"."remark" IS '备注';
COMMENT ON COLUMN "sys_config"."creator" IS '创建者';
COMMENT ON COLUMN "sys_config"."create_time" IS '创建时间';
COMMENT ON COLUMN "sys_config"."updater" IS '更新者';
COMMENT ON COLUMN "sys_config"."update_time" IS '更新时间';
COMMENT ON COLUMN "sys_config"."deleted" IS '是否删除';
COMMENT ON TABLE "sys_config" IS '参数配置表';

-- ----------------------------
-- 参数配置表初始化数据
-- ----------------------------
INSERT INTO "sys_config" VALUES (1, 1, '登陆验证码的开关', 'sys.account.captchaEnabled', 'true', '', 'admin', now(), '', NULL, 0);
INSERT INTO "sys_config" VALUES (2, 1, '用户登录-黑名单列表', 'sys.login.blackIPList', '', '设置登录IP黑名单限制，多个匹配项以;分隔，支持匹配（*通配、网段）', 'admin', now(), '', NULL, 0);
INSERT INTO "sys_config" VALUES (3, 1, '短信发送的开关', 'sys.account.smsEnabled', 'true', '', 'admin', now(), '', NULL, 0);
INSERT INTO "sys_config" VALUES (4, 1, '邮箱发送的开关', 'sys.account.emailEnabled', 'true', '', 'admin', now(), '', NULL, 0);
INSERT INTO "sys_config" VALUES (5, 1, '验证码模板', 'sms-verification-code', '【安全认证】 您好， 本次验证码为：{},有效期为五分钟，请尽快验证。', '', 'admin', now(), '', NULL, 0);


-- ----------------------------
-- 企业-用户关联表
-- ----------------------------
DROP TABLE IF EXISTS "sys_customer_user";
CREATE TABLE "sys_customer_user" (
  "customer_id" int8 NOT NULL,
  "user_id" int8 NOT NULL,
  CONSTRAINT "sys_customer_user_pkey" PRIMARY KEY ("customer_id", "user_id")
);
COMMENT ON TABLE "sys_customer_user" IS '企业-用户表';

-- ----------------------------
-- 字典数据表
-- ----------------------------
DROP TABLE IF EXISTS "sys_dict_data";
CREATE TABLE "sys_dict_data" (
  "dict_code" SERIAL PRIMARY KEY,
  "dict_sort" int4 NOT NULL,
  "dict_label" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "dict_value" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "dict_type" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "status" int2 NOT NULL,
  "list_class" varchar(100) COLLATE "pg_catalog"."default",
  "css_class" varchar(100) COLLATE "pg_catalog"."default",
  "remark" varchar(500) COLLATE "pg_catalog"."default",
  "creator" varchar(64) COLLATE "pg_catalog"."default",
  "create_time" timestamp NOT NULL,
  "updater" varchar(64) COLLATE "pg_catalog"."default",
  "update_time" timestamp DEFAULT NULL,
  "deleted" int2 NOT NULL DEFAULT 0,
  "is_default" char(1) COLLATE "pg_catalog"."default" NOT NULL DEFAULT 'N'::bpchar
);
COMMENT ON COLUMN "sys_dict_data"."dict_code" IS '字典编码';
COMMENT ON COLUMN "sys_dict_data"."dict_sort" IS '字典排序';
COMMENT ON COLUMN "sys_dict_data"."dict_label" IS '字典标签';
COMMENT ON COLUMN "sys_dict_data"."dict_value" IS '字典键值';
COMMENT ON COLUMN "sys_dict_data"."dict_type" IS '字典类型';
COMMENT ON COLUMN "sys_dict_data"."status" IS '状态（0正常 1停用）';
COMMENT ON COLUMN "sys_dict_data"."list_class" IS '表格回显样式';
COMMENT ON COLUMN "sys_dict_data"."css_class" IS '样式属性（其他样式扩展）';
COMMENT ON COLUMN "sys_dict_data"."remark" IS '备注';
COMMENT ON COLUMN "sys_dict_data"."creator" IS '创建者';
COMMENT ON COLUMN "sys_dict_data"."create_time" IS '创建时间';
COMMENT ON COLUMN "sys_dict_data"."updater" IS '更新者';
COMMENT ON COLUMN "sys_dict_data"."update_time" IS '更新时间';
COMMENT ON COLUMN "sys_dict_data"."deleted" IS '是否删除';
COMMENT ON COLUMN "sys_dict_data"."is_default" IS '是否默认（Y是 N否）';
COMMENT ON TABLE "sys_dict_data" IS '字典数据表';

-- ----------------------------
-- 字典数据表数据
-- ----------------------------
INSERT INTO "sys_dict_data" VALUES (1, 1, '男', '1', 'system_user_sex', 0, 'default', 'A', '性别男', 'admin', now(), '', NULL, 0, 'N');
INSERT INTO "sys_dict_data" VALUES (2, 2, '女', '2', 'system_user_sex', 1, 'success', '', '性别女', 'admin', now(), '', NULL, 0, 'N');
INSERT INTO "sys_dict_data" VALUES (3, 0, '其它', '0', 'system_operate_type', 0, 'default', '', '其它操作', 'admin', now(), '', NULL, 0, 'N');
INSERT INTO "sys_dict_data" VALUES (4, 1, '查询', '1', 'system_operate_type', 0, 'info', '', '查询操作', 'admin', now(), '', NULL, 0, 'N');
INSERT INTO "sys_dict_data" VALUES (5, 2, '新增', '2', 'system_operate_type', 0, 'primary', '', '新增操作', 'admin', now(), '', NULL, 0, 'N');
INSERT INTO "sys_dict_data" VALUES (6, 3, '修改', '3', 'system_operate_type', 0, 'warning', '', '修改操作', 'admin', now(), '', NULL, 0, 'N');
INSERT INTO "sys_dict_data" VALUES (7, 4, '删除', '4', 'system_operate_type', 0, 'danger', '', '删除操作', 'admin', now(), '', NULL, 0, 'N');
INSERT INTO "sys_dict_data" VALUES (8, 5, '导出', '5', 'system_operate_type', 0, 'default', '', '导出操作', 'admin', now(), '', NULL, 0, 'N');
INSERT INTO "sys_dict_data" VALUES (9, 6, '导入', '6', 'system_operate_type', 0, 'default', '', '导入操作', 'admin', now(), '', NULL, 0, 'N');

-- ----------------------------
-- 字典类型表
-- ----------------------------
DROP TABLE IF EXISTS "sys_dict_type";
CREATE TABLE "sys_dict_type" (
  "dict_id" SERIAL PRIMARY KEY,
  "dict_name" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "dict_type" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "status" int2 NOT NULL,
  "remark" varchar(500) COLLATE "pg_catalog"."default",
  "creator" varchar(64) COLLATE "pg_catalog"."default",
  "create_time" timestamp NOT NULL,
  "updater" varchar(64) COLLATE "pg_catalog"."default",
  "update_time" timestamp DEFAULT NULL,
  "deleted_time" timestamp,
  "deleted" int2 NOT NULL DEFAULT 0
);
COMMENT ON COLUMN "sys_dict_type"."dict_id" IS '字典主键';
COMMENT ON COLUMN "sys_dict_type"."dict_name" IS '字典名称';
COMMENT ON COLUMN "sys_dict_type"."dict_type" IS '字典类型';
COMMENT ON COLUMN "sys_dict_type"."status" IS '状态（0正常 1停用）';
COMMENT ON COLUMN "sys_dict_type"."remark" IS '备注';
COMMENT ON COLUMN "sys_dict_type"."creator" IS '创建者';
COMMENT ON COLUMN "sys_dict_type"."create_time" IS '创建时间';
COMMENT ON COLUMN "sys_dict_type"."updater" IS '更新者';
COMMENT ON COLUMN "sys_dict_type"."update_time" IS '更新时间';
COMMENT ON COLUMN "sys_dict_type"."deleted_time" IS '删除时间';
COMMENT ON COLUMN "sys_dict_type"."deleted" IS '是否删除';
COMMENT ON TABLE "sys_dict_type" IS '字典类型表';

-- ----------------------------
-- 字典类型表数据
-- ----------------------------
INSERT INTO "sys_dict_type" VALUES (1, '用户性别', 'system_user_sex', 0, NULL, 'admin', now(), '', NULL, NULL, 0);
INSERT INTO "sys_dict_type" VALUES (2, '操作类型', 'system_operate_type', 0, NULL, 'admin', now(), '', NULL, NULL, 0);

-- ----------------------------
-- 系统访问记录表
-- ----------------------------
DROP TABLE IF EXISTS "sys_login_log";
CREATE TABLE "sys_login_log" (
  "info_id" SERIAL PRIMARY KEY,
  "username" varchar(255) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "ipaddr" varchar(255) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "login_location" varchar(255) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "browser" varchar(255) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "os" varchar(255) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "status" int2 DEFAULT 0,
  "msg" varchar(255) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "login_time" timestamp
);
COMMENT ON COLUMN "sys_login_log"."info_id" IS '访问ID';
COMMENT ON COLUMN "sys_login_log"."username" IS '用户账号';
COMMENT ON COLUMN "sys_login_log"."ipaddr" IS '登录IP地址';
COMMENT ON COLUMN "sys_login_log"."login_location" IS '登录地点';
COMMENT ON COLUMN "sys_login_log"."browser" IS '浏览器类型';
COMMENT ON COLUMN "sys_login_log"."os" IS '操作系统';
COMMENT ON COLUMN "sys_login_log"."status" IS '登录状态（0成功 1失败）';
COMMENT ON COLUMN "sys_login_log"."msg" IS '提示消息';
COMMENT ON COLUMN "sys_login_log"."login_time" IS '访问时间';
COMMENT ON TABLE "sys_login_log" IS '系统访问记录';

-- ----------------------------
-- 菜单权限表
-- ----------------------------
DROP TABLE IF EXISTS "sys_menu";
CREATE TABLE "sys_menu" (
  "menu_id" SERIAL PRIMARY KEY,
  "menu_name" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "perms" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "menu_type" char(1) NOT NULL,
  "order_num" int4 NOT NULL,
  "parent_id" int8 NOT NULL DEFAULT 0,
  "path" varchar(200) COLLATE "pg_catalog"."default",
  "outer_chain" bool NOT NULL DEFAULT false,
  "icon" varchar(100) COLLATE "pg_catalog"."default",
  "status" int2 NOT NULL DEFAULT 0,
  "visible" bool NOT NULL,
  "creator" varchar(64) COLLATE "pg_catalog"."default",
  "create_time" timestamp NOT NULL,
  "updater" varchar(64) COLLATE "pg_catalog"."default",
  "update_time" timestamp DEFAULT NULL,
  "deleted" int2 NOT NULL DEFAULT 0
);
COMMENT ON COLUMN "sys_menu"."menu_id" IS '菜单ID';
COMMENT ON COLUMN "sys_menu"."menu_name" IS '菜单名称';
COMMENT ON COLUMN "sys_menu"."perms" IS '权限标识';
COMMENT ON COLUMN "sys_menu"."menu_type" IS '菜单类型（M目录 C菜单 F按钮）';
COMMENT ON COLUMN "sys_menu"."order_num" IS '显示顺序';
COMMENT ON COLUMN "sys_menu"."parent_id" IS '父菜单ID';
COMMENT ON COLUMN "sys_menu"."path" IS '路由地址';
COMMENT ON COLUMN "sys_menu"."outer_chain" IS '是否为外链';
COMMENT ON COLUMN "sys_menu"."icon" IS '菜单图标';
COMMENT ON COLUMN "sys_menu"."status" IS '菜单状态（0正常 1停用）';
COMMENT ON COLUMN "sys_menu"."visible" IS '是否隐藏';
COMMENT ON COLUMN "sys_menu"."creator" IS '创建者';
COMMENT ON COLUMN "sys_menu"."create_time" IS '创建时间';
COMMENT ON COLUMN "sys_menu"."updater" IS '更新者';
COMMENT ON COLUMN "sys_menu"."update_time" IS '更新时间';
COMMENT ON COLUMN "sys_menu"."deleted" IS '是否删除';
COMMENT ON TABLE "sys_menu" IS '菜单权限表';

-- ----------------------------
-- 菜单权限表数据
-- ----------------------------
-- 一级菜单
insert into "sys_menu" values('1', '角色管理', 'system:role:list', 'M', 1, 0, 'system/role/index',false,'',0,false,'',now(),'',NULL,0);
insert into "sys_menu" values('2', '用户管理', 'system:user:list', 'M', 2, 0, 'system/user/index',false,'',0,false,'',now(),'',NULL,0);
insert into "sys_menu" values('3', '日志管理', '', 'M', 99, 0, '',false,'',0,false,'',now(),'',NULL,0);
insert into "sys_menu" values('4', '全局配置', '', 'M', 50, 0, '',false,'',0,false,'',now(),'',NULL,0);
insert into "sys_menu" values('5', '消息订阅', 'app:msg:config', 'M', 19, 0, 'app/msg/config/index',false,'',0,false,'',now(),'',NULL,0);
insert into "sys_menu" values('6', '认证管理', 'bpm:attest:list', 'M', 11, 0, 'bpm/attest/index',false,'',0,false,'',now(),'',NULL,0);
insert into "sys_menu" values('7', '资产管理', 'app:assets:list', 'M', 12, 0, 'app/assets/index',false,'',0,false,'',now(),'',NULL,0);
insert into "sys_menu" values('8', '证书管理', 'app:cert:list', 'M', 13, 0, 'app/cert/index',false,'',0,false,'',now(),'',NULL,0);
insert into "sys_menu" values('9', '企业信息', 'app:info:view', 'M', 14, 0, 'app/info/index',false,'',0,false,'',now(),'',NULL,0);
insert into "sys_menu" values('10', '企业管理', 'system:customer:list', 'M', 3, 0, 'system/customer/index',false,'',0,false,'',now(),'',NULL,0);
insert into "sys_menu" values('11', '任务管理', 'system:task:list', 'M', 4, 0, 'system/task/index',false,'',0,false,'',now(),'',NULL,0);
insert into "sys_menu" values('12', '大屏视图', 'system:screen:view', 'M', 70, 0, 'system/screen/index',false,'',0,false,'',now(),'',NULL,0);
-- 二级菜单
insert into "sys_menu" values('100', '登录日志', 'monitor:loginLog:list', 'C', 1, 3, 'monitor/loginLog/index', false,'',0,false,'',now(),'',NULL,0);
insert into "sys_menu" values('101', '操作日志', 'monitor:operlog:list', 'C', 2, 3, 'monitor/operlog/index', false,'',0,false,'',now(),'',NULL,0);
insert into "sys_menu" values('105', '流程模板管理', 'system:bmp:list', 'C', 1, 4, 'system/bmp/index', false,'',0,false,'',now(),'',NULL,0);
-- 用户管理按钮
insert into "sys_menu" values('1000', '用户查询', 'system:user:query', 'F', 1, 2, '', false,'',0,false,'',now(),'',NULL,0);
insert into "sys_menu" values('1001', '用户新增', 'system:user:add', 'F', 1, 2, '', false,'',0,false,'',now(),'',NULL,0);
insert into "sys_menu" values('1002', '用户修改', 'system:user:edit', 'F', 1, 2, '', false,'',0,false,'',now(),'',NULL,0);
insert into "sys_menu" values('1003', '用户删除', 'system:user:remove', 'F', 1, 2, '', false,'',0,false,'',now(),'',NULL,0);
insert into "sys_menu" values('1004', '重置密码', 'system:user:resetPwd', 'F', 1, 2, '', false,'',0,false,'',now(),'',NULL,0);
-- 角色管理按钮
insert into "sys_menu" values('1010', '角色查询', 'system:role:query', 'F', 1, 1, '', false,'',0,false,'',now(),'',NULL,0);
insert into "sys_menu" values('1011', '角色新增', 'system:role:add', 'F', 1, 1, '', false,'',0,false,'',now(),'',NULL,0);
insert into "sys_menu" values('1012', '角色修改', 'system:role:edit', 'F', 1, 1, '', false,'',0,false,'',now(),'',NULL,0);
insert into "sys_menu" values('1013', '角色删除', 'system:role:remove', 'F', 1, 1, '', false,'',0,false,'',now(),'',NULL,0);
-- 菜单管理按钮
insert into "sys_menu" values('1020', '菜单查询', 'system:menu:query', 'F', 1, 1, '', false,'',0,false,'',now(),'',NULL,0);
insert into "sys_menu" values('1021', '菜单新增', 'system:menu:add', 'F', 1, 1, '', false,'',0,false,'',now(),'',NULL,0);
insert into "sys_menu" values('1022', '菜单修改', 'system:menu:edit', 'F', 1, 1, '', false,'',0,false,'',now(),'',NULL,0);
insert into "sys_menu" values('1023', '菜单删除', 'system:menu:remove', 'F', 1, 1, '', false,'',0,false,'',now(),'',NULL,0);
-- 流程模板管理按钮
insert into "sys_menu" values('1030', '流程模板查询', 'system:bmp:query', 'F', 1, 105, '', false,'',0,false,'',now(),'',NULL,0);
insert into "sys_menu" values('1031', '流程模板新增', 'system:bmp:add', 'F', 1, 105, '', false,'',0,false,'',now(),'',NULL,0);
insert into "sys_menu" values('1032', '流程模板修改', 'system:bmp:edit', 'F', 1, 105, '', false,'',0,false,'',now(),'',NULL,0);
insert into "sys_menu" values('1033', '流程模板删除', 'system:bmp:remove', 'F', 1, 105, '', false,'',0,false,'',now(),'',NULL,0);
-- 认证管理按钮
insert into "sys_menu" values('1040', '申请认证', 'bpm:attest:add', 'F', 1, 6, '', false,'',0,false,'',now(),'',NULL,0);
insert into "sys_menu" values('1041', '取消认证', 'bpm:attest:edit', 'F', 1, 6, '', false,'',0,false,'',now(),'',NULL,0);
insert into "sys_menu" values('1042', '认证详情', 'bpm:attest:query', 'F', 1, 6, '', false,'',0,false,'',now(),'',NULL,0);


-- ----------------------------
-- 通知公告表
-- ----------------------------
DROP TABLE IF EXISTS "sys_notice";
CREATE TABLE "sys_notice" (
  "notice_id" SERIAL PRIMARY KEY,
  "notice_title" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "notice_content" text COLLATE "pg_catalog"."default" NOT NULL,
  "notice_type" int2 NOT NULL,
  "status" int2 NOT NULL,
  "creator" varchar(64) COLLATE "pg_catalog"."default",
  "create_time" timestamp NOT NULL,
  "updater" varchar(64) COLLATE "pg_catalog"."default",
  "update_time" timestamp DEFAULT NULL,
  "deleted" int2 NOT NULL DEFAULT 0
);
COMMENT ON COLUMN "sys_notice"."notice_id" IS '公告ID';
COMMENT ON COLUMN "sys_notice"."notice_title" IS '公告标题';
COMMENT ON COLUMN "sys_notice"."notice_content" IS '公告内容';
COMMENT ON COLUMN "sys_notice"."notice_type" IS '公告类型（1通知 2公告）';
COMMENT ON COLUMN "sys_notice"."status" IS '公告状态（0正常 1关闭）';
COMMENT ON COLUMN "sys_notice"."creator" IS '创建者';
COMMENT ON COLUMN "sys_notice"."create_time" IS '创建时间';
COMMENT ON COLUMN "sys_notice"."updater" IS '更新者';
COMMENT ON COLUMN "sys_notice"."update_time" IS '更新时间';
COMMENT ON COLUMN "sys_notice"."deleted" IS '是否删除';
COMMENT ON TABLE "sys_notice" IS '通知公告表';

-- ----------------------------
-- 用户操作日志表
-- ----------------------------
DROP TABLE IF EXISTS "sys_oper_log";
CREATE TABLE "sys_oper_log" (
  "oper_id" SERIAL PRIMARY KEY,
  "title" varchar(255) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "business_type" int2 DEFAULT 0,
  "method" varchar(255) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "request_method" varchar(255) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "operator_type" int2 DEFAULT 0,
  "oper_name" varchar(255) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "oper_url" varchar(255) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "oper_ip" varchar(255) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "oper_location" varchar(255) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "oper_param" varchar(2000) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "json_result" varchar(2000) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "status" int2 DEFAULT 0,
  "error_msg" varchar(255) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "oper_time" timestamp ,
  "cost_time" int8 DEFAULT 0
);
COMMENT ON COLUMN "sys_oper_log"."oper_id" IS '日志主键';
COMMENT ON COLUMN "sys_oper_log"."title" IS '模块标题';
COMMENT ON COLUMN "sys_oper_log"."business_type" IS '业务类型（字典system_operate_type）';
COMMENT ON COLUMN "sys_oper_log"."method" IS '方法名称';
COMMENT ON COLUMN "sys_oper_log"."request_method" IS '请求方式';
COMMENT ON COLUMN "sys_oper_log"."operator_type" IS '操作类别（0其它 1后台用户 2企业端用户）';
COMMENT ON COLUMN "sys_oper_log"."oper_name" IS '操作人员';
COMMENT ON COLUMN "sys_oper_log"."oper_url" IS '请求URL';
COMMENT ON COLUMN "sys_oper_log"."oper_ip" IS '主机地址';
COMMENT ON COLUMN "sys_oper_log"."oper_location" IS '操作地点';
COMMENT ON COLUMN "sys_oper_log"."oper_param" IS '请求参数';
COMMENT ON COLUMN "sys_oper_log"."json_result" IS '返回参数';
COMMENT ON COLUMN "sys_oper_log"."status" IS '操作状态（0正常 1异常）';
COMMENT ON COLUMN "sys_oper_log"."error_msg" IS '错误消息';
COMMENT ON COLUMN "sys_oper_log"."oper_time" IS '操作时间';
COMMENT ON COLUMN "sys_oper_log"."cost_time" IS '消耗时间';

-- ----------------------------
-- 角色信息表
-- ----------------------------
DROP TABLE IF EXISTS "sys_role";
CREATE TABLE "sys_role" (
  "role_id" SERIAL PRIMARY KEY,
  "role_name" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "role_key" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "role_sort" int4 NOT NULL,
  "data_scope" int2 NOT NULL,
  "status" int2 NOT NULL,
  "remark" varchar(500) COLLATE "pg_catalog"."default",
  "creator" varchar(64) COLLATE "pg_catalog"."default",
  "create_time" timestamp NOT NULL,
  "updater" varchar(64) COLLATE "pg_catalog"."default",
  "update_time" timestamp DEFAULT NULL,
  "deleted" int2 NOT NULL DEFAULT 0
);
COMMENT ON COLUMN "sys_role"."role_id" IS '角色ID';
COMMENT ON COLUMN "sys_role"."role_name" IS '角色名称';
COMMENT ON COLUMN "sys_role"."role_key" IS '角色权限字符串';
COMMENT ON COLUMN "sys_role"."role_sort" IS '显示顺序';
COMMENT ON COLUMN "sys_role"."data_scope" IS '数据范围（1：全部数据权限 2：自定数据权限）';
COMMENT ON COLUMN "sys_role"."status" IS '角色状态（0正常 1停用）';
COMMENT ON COLUMN "sys_role"."remark" IS '备注';
COMMENT ON COLUMN "sys_role"."creator" IS '创建者';
COMMENT ON COLUMN "sys_role"."create_time" IS '创建时间';
COMMENT ON COLUMN "sys_role"."updater" IS '更新者';
COMMENT ON COLUMN "sys_role"."update_time" IS '更新时间';
COMMENT ON COLUMN "sys_role"."deleted" IS '是否删除';
COMMENT ON TABLE "sys_role" IS '角色信息表';

-- ----------------------------
-- 角色信息表数据
-- ----------------------------
INSERT INTO "sys_role" VALUES (1, '超级管理员', 'super_admin', 1, 1, 0, '超级管理员', 'admin', now(), '', NULL, 0);
INSERT INTO "sys_role" VALUES (2, '管理员', 'admin', 2, 2, 0, '管理员', 'admin', now(), '', NULL, 0);

-- ----------------------------
-- 角色和菜单关联表
-- ----------------------------
DROP TABLE IF EXISTS "sys_role_menu";
CREATE TABLE "sys_role_menu" (
  "role_id" int8 NOT NULL,
  "menu_id" int8 NOT NULL,
  CONSTRAINT "sys_role_menu_pkey" PRIMARY KEY ("role_id", "menu_id")
);
COMMENT ON COLUMN "sys_role_menu"."role_id" IS '角色ID';
COMMENT ON COLUMN "sys_role_menu"."menu_id" IS '菜单ID';
COMMENT ON TABLE "sys_role_menu" IS '角色和菜单关联表';

-- ----------------------------
-- 角色和菜单关联表数据
-- ----------------------------
INSERT INTO "sys_role_menu" VALUES (1, 1);
INSERT INTO "sys_role_menu" VALUES (1, 2);
INSERT INTO "sys_role_menu" VALUES (2, 1);
INSERT INTO "sys_role_menu" VALUES (2, 2);
INSERT INTO "sys_role_menu" VALUES (3,6);
INSERT INTO "sys_role_menu" VALUES (3,1040);
INSERT INTO "sys_role_menu" VALUES (3,1041);
INSERT INTO "sys_role_menu" VALUES (3,1042);

-- ----------------------------
-- 用户信息表
-- ----------------------------
DROP TABLE IF EXISTS "sys_user";
CREATE TABLE "sys_user" (
  "user_id" SERIAL PRIMARY KEY,
  "username" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "password" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "nickname" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "remark" varchar(500) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "email" varchar(50) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "mobile" varchar(11) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "sex" int2 DEFAULT 0,
  "avatar" varchar(100) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "status" int2 NOT NULL,
  "login_ip" varchar(50) COLLATE "pg_catalog"."default",
  "login_date" timestamp,
  "creator" varchar(64) COLLATE "pg_catalog"."default",
  "create_time" timestamp NOT NULL,
  "updater" varchar(64) COLLATE "pg_catalog"."default",
  "update_time" timestamp DEFAULT NULL,
  "deleted" int2 NOT NULL DEFAULT 0,
  "user_type" int2 NOT NULL DEFAULT 0,
  "pwd_update_time" timestamp
)
;
COMMENT ON COLUMN "sys_user"."user_id" IS '用户ID';
COMMENT ON COLUMN "sys_user"."username" IS '用户账号';
COMMENT ON COLUMN "sys_user"."password" IS '密码';
COMMENT ON COLUMN "sys_user"."nickname" IS '用户昵称';
COMMENT ON COLUMN "sys_user"."remark" IS '备注';
COMMENT ON COLUMN "sys_user"."email" IS '用户邮箱';
COMMENT ON COLUMN "sys_user"."mobile" IS '手机号码';
COMMENT ON COLUMN "sys_user"."sex" IS '用户性别';
COMMENT ON COLUMN "sys_user"."avatar" IS '头像地址';
COMMENT ON COLUMN "sys_user"."status" IS '帐号状态（0正常 1停用）';
COMMENT ON COLUMN "sys_user"."login_ip" IS '最后登录IP';
COMMENT ON COLUMN "sys_user"."login_date" IS '最后登录时间';
COMMENT ON COLUMN "sys_user"."creator" IS '创建者';
COMMENT ON COLUMN "sys_user"."create_time" IS '创建时间';
COMMENT ON COLUMN "sys_user"."updater" IS '更新者';
COMMENT ON COLUMN "sys_user"."update_time" IS '更新时间';
COMMENT ON COLUMN "sys_user"."deleted" IS '是否删除';
COMMENT ON COLUMN "sys_user"."user_type" IS '用户类型（0管理端 1企业端）';
COMMENT ON COLUMN "sys_user"."pwd_update_time" IS '最后修改密码时间';
COMMENT ON TABLE "sys_user" IS '用户信息表';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO "sys_user" VALUES (1, 'admin', '812e2b235e61301e92fc20a0156954bc61992140e5d0e9fdbe69eedecbc719710f5d11e92a028331bcfb7a44f2d8d9bd1466fafb5df81354c0a658b2fb7415ee98fb9a3b9c375f82b4c9c1a770ad1183', '管理员', '管理员', 'admin@126.com', '13333333333', 1, '', 0, '', NULL, 'admin', now(), NULL, NULL, 0, 0, NULL);

-- ----------------------------
-- 用户和角色关联表
-- ----------------------------
DROP TABLE IF EXISTS "sys_user_role";
CREATE TABLE "sys_user_role" (
  "user_id" int8 NOT NULL,
  "role_id" int8 NOT NULL,
  CONSTRAINT "sys_user_role_pkey" PRIMARY KEY ("user_id", "role_id")
);
COMMENT ON COLUMN "sys_user_role"."user_id" IS '用户ID';
COMMENT ON COLUMN "sys_user_role"."role_id" IS '角色ID';
COMMENT ON TABLE "sys_user_role" IS '用户和角色关联表';

-- ----------------------------
-- 用户和角色关联表数据
-- ----------------------------
INSERT INTO "sys_user_role" VALUES (1, 1);


-- ----------------------------
-- 索引
-- ----------------------------
CREATE UNIQUE INDEX "dict_type" ON "sys_dict_type" USING btree (
  "dict_type" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
CREATE INDEX "idx_status" ON "sys_login_log" USING btree (
  "status" "pg_catalog"."int2_ops" ASC NULLS LAST
);
CREATE INDEX "idx_time" ON "public"."sys_login_log" USING btree (
  "login_time" "pg_catalog"."timestamp_ops" ASC NULLS LAST
);
CREATE INDEX "idx_oper_bus" ON "sys_oper_log" USING btree (
  "business_type" "pg_catalog"."int2_ops" ASC NULLS LAST
);
CREATE INDEX "idx_oper_status" ON "sys_oper_log" USING btree (
  "status" "pg_catalog"."int2_ops" ASC NULLS LAST
);
CREATE INDEX "idx_oper_time" ON "sys_oper_log" USING btree (
  "oper_time" "pg_catalog"."timestamp_ops" ASC NULLS LAST
);
CREATE INDEX "idx_userType" ON "sys_user" USING btree (
  "user_type" "pg_catalog"."int2_ops" ASC NULLS LAST
);
CREATE UNIQUE INDEX "idx_username" ON "sys_user" USING btree (
  "username" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "update_time" "pg_catalog"."timestamp_ops" ASC NULLS LAST,
  "user_type" "pg_catalog"."int2_ops" ASC NULLS LAST
);

-- ----------------------------
-- 短信发送记录表
-- ----------------------------
DROP TABLE IF EXISTS "sys_sms_send_log";
CREATE TABLE sys_sms_send_log (
  "id" SERIAL PRIMARY KEY,
  "mobile" varchar(11) COLLATE "pg_catalog"."default" NOT NULL,
  "content" varchar(500) COLLATE "pg_catalog"."default" NOT NULL DEFAULT ''::character varying,
  "scene" int2 NOT NULL,
  "create_ip" varchar(255) COLLATE "pg_catalog"."default" NOT NULL DEFAULT ''::character varying,
  "today_index" int4 NOT NULL DEFAULT 0,
  "creator" varchar(64) COLLATE "pg_catalog"."default",
  "create_time" timestamp NOT NULL,
  "updater" varchar(64) COLLATE "pg_catalog"."default",
  "update_time" timestamp
);

CREATE INDEX "idx_mobile" ON "sys_sms_send_log" USING btree (
  "mobile" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);

COMMENT ON COLUMN "sys_sms_send_log"."id" IS '主键';
COMMENT ON COLUMN "sys_sms_send_log"."mobile" IS '手机号';
COMMENT ON COLUMN "sys_sms_send_log"."content" IS '短信内容';
COMMENT ON COLUMN "sys_sms_send_log"."scene" IS '发送场景（参考SmsSceneEnum）';
COMMENT ON COLUMN "sys_sms_send_log"."create_ip" IS 'IP';
COMMENT ON COLUMN "sys_sms_send_log"."today_index" IS '今日发送的第几条';
COMMENT ON COLUMN "sys_sms_send_log"."creator" IS '创建者';
COMMENT ON COLUMN "sys_sms_send_log"."create_time" IS '创建时间';
COMMENT ON COLUMN "sys_sms_send_log"."updater" IS '更新者';
COMMENT ON COLUMN "sys_sms_send_log"."update_time" IS '更新时间';
COMMENT ON TABLE "sys_sms_send_log" IS '短信发送记录表';

-- ----------------------------
-- 邮箱发送记录表
-- ----------------------------
DROP TABLE IF EXISTS "sys_email_send_log";
CREATE TABLE sys_email_send_log (
      "id" SERIAL PRIMARY KEY,
      "email" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
      "content" varchar(500) COLLATE "pg_catalog"."default" NOT NULL DEFAULT ''::character varying,
      "scene" int2 NOT NULL,
      "create_ip" varchar(255) COLLATE "pg_catalog"."default" NOT NULL DEFAULT ''::character varying,
      "today_index" int4 NOT NULL DEFAULT 0,
      "creator" varchar(64) COLLATE "pg_catalog"."default",
      "create_time" timestamp NOT NULL,
      "updater" varchar(64) COLLATE "pg_catalog"."default",
      "update_time" timestamp
);

CREATE INDEX "idx_email" ON "sys_email_send_log" USING btree (
  "email" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);

COMMENT ON COLUMN "sys_email_send_log"."id" IS '主键';
COMMENT ON COLUMN "sys_email_send_log"."email" IS '邮箱';
COMMENT ON COLUMN "sys_email_send_log"."content" IS '内容';
COMMENT ON COLUMN "sys_email_send_log"."scene" IS '发送场景（1验证码 2平台消息）';
COMMENT ON COLUMN "sys_email_send_log"."create_ip" IS 'IP';
COMMENT ON COLUMN "sys_email_send_log"."today_index" IS '今日发送的第几条';
COMMENT ON COLUMN "sys_email_send_log"."creator" IS '创建者';
COMMENT ON COLUMN "sys_email_send_log"."create_time" IS '创建时间';
COMMENT ON COLUMN "sys_email_send_log"."updater" IS '更新者';
COMMENT ON COLUMN "sys_email_send_log"."update_time" IS '更新时间';
COMMENT ON TABLE "sys_email_send_log" IS '邮箱发送记录表';

-- ----------------------------
-- 流程模板表
-- ----------------------------
DROP TABLE IF EXISTS "sys_bmp_template";
CREATE TABLE sys_bmp_template (
    "id" SERIAL PRIMARY KEY,
    "template_id" varchar(32) COLLATE "pg_catalog"."default" NOT NULL DEFAULT ''::character varying,
    "template_key" varchar(32) COLLATE "pg_catalog"."default" NOT NULL DEFAULT ''::character varying,
    "name" varchar(32) COLLATE "pg_catalog"."default" NOT NULL DEFAULT ''::character varying,
    "path" varchar(255) COLLATE "pg_catalog"."default" NOT NULL DEFAULT ''::character varying,
    "creator" varchar(64) COLLATE "pg_catalog"."default",
    "create_time" timestamp NOT NULL,
    "updater" varchar(64) COLLATE "pg_catalog"."default",
    "update_time" timestamp
);

COMMENT ON COLUMN "sys_bmp_template"."id" IS '主键';
COMMENT ON COLUMN "sys_bmp_template"."template_id" IS '模板id（接口返回id）';
COMMENT ON COLUMN "sys_bmp_template"."template_key" IS '模板key';
COMMENT ON COLUMN "sys_bmp_template"."name" IS '模板名称';
COMMENT ON COLUMN "sys_bmp_template"."path" IS '路径';
COMMENT ON COLUMN "sys_bmp_template"."creator" IS '创建者';
COMMENT ON COLUMN "sys_bmp_template"."create_time" IS '创建时间';
COMMENT ON COLUMN "sys_bmp_template"."updater" IS '更新者';
COMMENT ON COLUMN "sys_bmp_template"."update_time" IS '更新时间';
COMMENT ON TABLE "sys_bmp_template" IS '流程模板表';

-- ----------------------------
-- 认证服务类型表
-- ----------------------------
DROP TABLE IF EXISTS "sys_attest_type";
CREATE TABLE sys_attest_type (
      "id" SERIAL PRIMARY KEY,
      "name" varchar(32) COLLATE "pg_catalog"."default" NOT NULL DEFAULT ''::character varying,
      "parent_id" int8 NOT NULL DEFAULT 0,
      "order_num" int4 NOT NULL,
      "creator" varchar(64) COLLATE "pg_catalog"."default",
      "create_time" timestamp NOT NULL,
      "updater" varchar(64) COLLATE "pg_catalog"."default",
      "update_time" timestamp,
      "deleted" int2 DEFAULT 0
);

COMMENT ON COLUMN "sys_attest_type"."id" IS '主键';
COMMENT ON COLUMN "sys_attest_type"."name" IS '服务类型';
COMMENT ON COLUMN "sys_attest_type"."parent_id" IS '服务类型父id';
COMMENT ON COLUMN "sys_attest_type"."order_num" IS '显示顺序';
COMMENT ON COLUMN "sys_attest_type"."creator" IS '创建者';
COMMENT ON COLUMN "sys_attest_type"."create_time" IS '创建时间';
COMMENT ON COLUMN "sys_attest_type"."updater" IS '更新者';
COMMENT ON COLUMN "sys_attest_type"."update_time" IS '更新时间';
COMMENT ON COLUMN "sys_attest_type"."deleted" IS '是否删除';
COMMENT ON TABLE "sys_attest_type" IS '认证服务类型表';

-- ----------------------------
-- 认证服务类型表数据
-- ----------------------------
INSERT INTO "sys_attest_type" VALUES (1, '信息安全', 0, 1, '', now(), NULL, NULL, 0);
INSERT INTO "sys_attest_type" VALUES (2, '防病毒', 0, 2, '', now(), NULL, NULL, 0);
INSERT INTO "sys_attest_type" VALUES (1002,	'安全路由器', 1, 1, '', now(), NULL, NULL, 0);
INSERT INTO "sys_attest_type" VALUES (1003,	'防火墙', 1, 1, '', now(), NULL, NULL, 0);
INSERT INTO "sys_attest_type" VALUES (1004,	'(IDS)入侵检测系统', 1, 1, '', now(), NULL, NULL, 0);
INSERT INTO "sys_attest_type" VALUES (1005,	'(IPS)入侵防御系统', 1, 1, '', now(), NULL, NULL, 0);
INSERT INTO "sys_attest_type" VALUES (1006,	'漏洞扫描', 1, 1, '', now(), NULL, NULL, 0);
INSERT INTO "sys_attest_type" VALUES (1007,	'隔离', 1, 1, '', now(), NULL, NULL, 0);
INSERT INTO "sys_attest_type" VALUES (1008,	'身份鉴别', 1, 1, '', now(), NULL, NULL, 0);
INSERT INTO "sys_attest_type" VALUES (1009,	'完整性鉴别', 1, 1, '', now(), NULL, NULL, 0);
INSERT INTO "sys_attest_type" VALUES (1010,	'不可否认性鉴别', 1, 1, '', now(), NULL, NULL, 0);
INSERT INTO "sys_attest_type" VALUES (1011,	'密钥管理', 1, 1, '', now(), NULL, NULL, 0);
INSERT INTO "sys_attest_type" VALUES (1012,	'安全操作系统', 1, 1, '', now(), NULL, NULL, 0);
INSERT INTO "sys_attest_type" VALUES (1013,	'安全数据库', 1, 1, '', now(), NULL, NULL, 0);
INSERT INTO "sys_attest_type" VALUES (1014,	'访问控制', 1, 1, '', now(), NULL, NULL, 0);
INSERT INTO "sys_attest_type" VALUES (1015,	'信息过滤', 1, 1, '', now(), NULL, NULL, 0);
INSERT INTO "sys_attest_type" VALUES (1016,	'WEB过滤防护', 1, 1, '', now(), NULL, NULL, 0);
INSERT INTO "sys_attest_type" VALUES (1017,	'网站恢复', 1, 1, '', now(), NULL, NULL, 0);
INSERT INTO "sys_attest_type" VALUES (1018,	'本地数据备份与恢复', 1, 1, '', now(), NULL, NULL, 0);
INSERT INTO "sys_attest_type" VALUES (1019,	'安全审计', 1, 1, '', now(), NULL, NULL, 0);
INSERT INTO "sys_attest_type" VALUES (1020,	'日志分析', 1, 1, '', now(), NULL, NULL, 0);
INSERT INTO "sys_attest_type" VALUES (1021,	'非授权外联监测', 1, 1, '', now(), NULL, NULL, 0);
INSERT INTO "sys_attest_type" VALUES (1022,	'远程主机监测', 1, 1, '', now(), NULL, NULL, 0);
INSERT INTO "sys_attest_type" VALUES (1023,	'主机文件监测', 1, 1, '', now(), NULL, NULL, 0);
INSERT INTO "sys_attest_type" VALUES (1024,	'文件加密', 1, 1, '', now(), NULL, NULL, 0);
INSERT INTO "sys_attest_type" VALUES (1025,	'安全管理平台', 1, 1, '', now(), NULL, NULL, 0);
INSERT INTO "sys_attest_type" VALUES (1026,	'VPN', 1, 1, '', now(), NULL, NULL, 0);
INSERT INTO "sys_attest_type" VALUES (1027,	'防DoS系统', 1, 1, '', now(), NULL, NULL, 0);
INSERT INTO "sys_attest_type" VALUES (1028,	'互联网上网服务营业场所信息安全管理系统', 1, 1, '', now(), NULL, NULL, 0);
INSERT INTO "sys_attest_type" VALUES (1029,	'CA', 1, 1, '', now(), NULL, NULL, 0);
INSERT INTO "sys_attest_type" VALUES (1030,	'安全交换机', 1, 1, '', now(), NULL, NULL, 0);
INSERT INTO "sys_attest_type" VALUES (1031,	'电子数据存储介质复制工具', 1, 1, '', now(), NULL, NULL, 0);
INSERT INTO "sys_attest_type" VALUES (1032,	'电子数据存储介质写保护设备', 1, 1, '', now(), NULL, NULL, 0);
INSERT INTO "sys_attest_type" VALUES (1033,	'网关', 1, 1, '', now(), NULL, NULL, 0);
INSERT INTO "sys_attest_type" VALUES (1034,	'电子文档安全管理', 1, 1, '', now(), NULL, NULL, 0);
INSERT INTO "sys_attest_type" VALUES (1035,	'USB移动存储介质管理系统', 1, 1, '', now(), NULL, NULL, 0);
INSERT INTO "sys_attest_type" VALUES (1036,	'内网主机监测', 1, 1, '', now(), NULL, NULL, 0);
INSERT INTO "sys_attest_type" VALUES (1037,	'主机信息泄漏防护主机信息泄漏防护', 1, 1, '', now(), NULL, NULL, 0);
INSERT INTO "sys_attest_type" VALUES (1038,	'云操作系统', 1, 1, '', now(), NULL, NULL, 0);
INSERT INTO "sys_attest_type" VALUES (1039,	'终端接入控制产品', 1, 1, '', now(), NULL, NULL, 0);
INSERT INTO "sys_attest_type" VALUES (1040,	'电子签章产品', 1, 1, '', now(), NULL, NULL, 0);
INSERT INTO "sys_attest_type" VALUES (1041,	'主机安全检查产品', 1, 1, '', now(), NULL, NULL, 0);
INSERT INTO "sys_attest_type" VALUES (1042,	'数据销毁软件产品', 1, 1, '', now(), NULL, NULL, 0);
INSERT INTO "sys_attest_type" VALUES (1043,	'UTM', 1, 1, '', now(), NULL, NULL, 0);
INSERT INTO "sys_attest_type" VALUES (1044,	'互联网公共上网服务场所信息安全管理系统', 1, 1, '', now(), NULL, NULL, 0);
INSERT INTO "sys_attest_type" VALUES (1045,	'互联网公共上网服务场所信息安全管理系统(无线接入前端)', 1, 1, '', now(), NULL, NULL, 0);
INSERT INTO "sys_attest_type" VALUES (1046,	'工业控制系统网络审计产品', 1, 1, '', now(), NULL, NULL, 0);
INSERT INTO "sys_attest_type" VALUES (1047,	'邮件安全专项产品', 1, 1, '', now(), NULL, NULL, 0);
INSERT INTO "sys_attest_type" VALUES (1048,	'运维安全管理产品', 1, 1, '', now(), NULL, NULL, 0);
INSERT INTO "sys_attest_type" VALUES (1049,	'安全加固类', 1, 1, '', now(), NULL, NULL, 0);
INSERT INTO "sys_attest_type" VALUES (1050,	'文档打印安全监控与审计产品', 1, 1, '', now(), NULL, NULL, 0);
INSERT INTO "sys_attest_type" VALUES (1051,	'工业控制系统安全管理平台', 1, 1, '', now(), NULL, NULL, 0);
INSERT INTO "sys_attest_type" VALUES (1052,	'网站内容安全检查产品', 1, 1, '', now(), NULL, NULL, 0);
INSERT INTO "sys_attest_type" VALUES (1053,	'远程接入控制产品', 1, 1, '', now(), NULL, NULL, 0);
INSERT INTO "sys_attest_type" VALUES (1054,	'网页防算改产品', 1, 1, '', now(), NULL, NULL, 0);
INSERT INTO "sys_attest_type" VALUES (1055,	'移动终端安全管理与接入控制产品', 1, 1, '', now(), NULL, NULL, 0);
INSERT INTO "sys_attest_type" VALUES (1056,	'云计算安全综合防御产品', 1, 1, '', now(), NULL, NULL, 0);
INSERT INTO "sys_attest_type" VALUES (1057,	'网络型流量控制产品', 1, 1, '', now(), NULL, NULL, 0);
INSERT INTO "sys_attest_type" VALUES (1058,	'网站安全云防护平台', 1, 1, '', now(), NULL, NULL, 0);
INSERT INTO "sys_attest_type" VALUES (1059,	'工业控制系统专用防火墙', 1, 1, '', now(), NULL, NULL, 0);
INSERT INTO "sys_attest_type" VALUES (1060,	'工业控制网络安全隔离与信息交换系统', 1, 1, '', now(), NULL, NULL, 0);
INSERT INTO "sys_attest_type" VALUES (1061,	'移动智能终端安全监测产品', 1, 1, '', now(), NULL, NULL, 0);
INSERT INTO "sys_attest_type" VALUES (1062,	'安全型硬拷贝产品', 1, 1, '', now(), NULL, NULL, 0);
INSERT INTO "sys_attest_type" VALUES (1063,	'网站监测产品', 1, 1, '', now(), NULL, NULL, 0);
INSERT INTO "sys_attest_type" VALUES (1064,	'网络存储产品', 1, 1, '', now(), NULL, NULL, 0);
INSERT INTO "sys_attest_type" VALUES (1065,	'安全型硬拷贝产品', 1, 1, '', now(), NULL, NULL, 0);
INSERT INTO "sys_attest_type" VALUES (1066,	'移动终端安全管理平台', 1, 1, '', now(), NULL, NULL, 0);
INSERT INTO "sys_attest_type" VALUES (1067,	'移动智能终端操作系统', 1, 1, '', now(), NULL, NULL, 0);
INSERT INTO "sys_attest_type" VALUES (1068,	'数据泄露防护产品', 1, 1, '', now(), NULL, NULL, 0);
INSERT INTO "sys_attest_type" VALUES (1069,	'大数据平台安全管理产品', 1, 1, '', now(), NULL, NULL, 0);
INSERT INTO "sys_attest_type" VALUES (1070,	'网络型防火墙 (22国标)', 1, 1, '', now(), NULL, NULL, 0);
INSERT INTO "sys_attest_type" VALUES (1071,	'web应用防火墙 (2020国标)', 1, 1, '', now(), NULL, NULL, 0);
INSERT INTO "sys_attest_type" VALUES (1072,	'数据库防火墙 (22国标)', 1, 1, '', now(), NULL, NULL, 0);
INSERT INTO "sys_attest_type" VALUES (1073,	'主机型防火墙 (2020国标)', 1, 1, '', now(), NULL, NULL, 0);
INSERT INTO "sys_attest_type" VALUES (1074,	'网络及安全设备配置检查产品', 1, 1, '', now(), NULL, NULL, 0);
INSERT INTO "sys_attest_type" VALUES (1075,	'负载均衡产品', 1, 1, '', now(), NULL, NULL, 0);
INSERT INTO "sys_attest_type" VALUES (2001,	'计算机病毒防护产品', 2, 1, '', now(), NULL, NULL, 0);
INSERT INTO "sys_attest_type" VALUES (2002,	'移动安全产品', 2, 1, '', now(), NULL, NULL, 0);


-- ----------------------------
-- 认证流程表
-- ----------------------------
DROP TABLE IF EXISTS "bpm_attest";
CREATE TABLE bpm_attest (
     "id" SERIAL PRIMARY KEY,
     "process_id" varchar(32) COLLATE "pg_catalog"."default" NOT NULL DEFAULT ''::character varying,
     "process_name" varchar(32) COLLATE "pg_catalog"."default" NOT NULL DEFAULT ''::character varying,
     "template_id" varchar(32) COLLATE "pg_catalog"."default" NOT NULL DEFAULT ''::character varying,
     "template_key" varchar(32) COLLATE "pg_catalog"."default" NOT NULL DEFAULT ''::character varying,
     "customer_id" int8 NOT NULL DEFAULT 0,
     "status" int2 NOT NULL DEFAULT 0,
     "attest_type" int8 NOT NULL DEFAULT 0,
     "start_time" timestamp NOT NULL,
     "end_time" timestamp NOT NULL,
     "creator" varchar(64) COLLATE "pg_catalog"."default",
     "create_time" timestamp NOT NULL,
     "updater" varchar(64) COLLATE "pg_catalog"."default",
     "update_time" timestamp,
     "deleted" int2 DEFAULT 0
);

COMMENT ON COLUMN "bpm_attest"."id" IS '主键';
COMMENT ON COLUMN "bpm_attest"."process_id" IS '流程id';
COMMENT ON COLUMN "bpm_attest"."process_name" IS '流程名称';
COMMENT ON COLUMN "bpm_attest"."template_id" IS '模板id';
COMMENT ON COLUMN "bpm_attest"."template_key" IS '模板key';
COMMENT ON COLUMN "bpm_attest"."customer_id" IS '企业id';
COMMENT ON COLUMN "bpm_attest"."status" IS '流程状态（1进行中 4已取消 6已通过 7未通过）';
COMMENT ON COLUMN "bpm_attest"."attest_type" IS '认证服务类别(sys_attest_type主键)';
COMMENT ON COLUMN "bpm_attest"."start_time" IS '流程开始时间';
COMMENT ON COLUMN "bpm_attest"."end_time" IS '流程结束时间';
COMMENT ON COLUMN "bpm_attest"."creator" IS '创建者';
COMMENT ON COLUMN "bpm_attest"."create_time" IS '创建时间';
COMMENT ON COLUMN "bpm_attest"."updater" IS '更新者';
COMMENT ON COLUMN "bpm_attest"."update_time" IS '更新时间';
COMMENT ON COLUMN "bpm_attest"."deleted" IS '是否删除';
COMMENT ON TABLE "bpm_attest" IS '认证流程表';

CREATE INDEX "idx_bpm_status" ON "bpm_attest" USING btree (
  "status" "pg_catalog"."int2_ops" ASC NULLS LAST
);
CREATE INDEX "idx_bpm_customer_id" ON "bpm_attest" USING btree (
  "customer_id" "pg_catalog"."int8_ops" ASC NULLS LAST
);



---------企业信息表------------
DROP TABLE IF EXISTS "app_customer";
CREATE TABLE "app_customer" (
"id" SERIAL PRIMARY KEY,
"name" varchar(128) COLLATE "default" DEFAULT NULL::character varying NOT NULL,
"type" int2 NOT NULL,
"status" int2 DEFAULT 0,
"logon_status" int2 DEFAULT 0,
"attest_status" int2 DEFAULT 0,
"social_code" varchar(64) COLLATE "default" DEFAULT NULL::character varying,
"address" varchar(64) COLLATE "default" DEFAULT NULL::character varying,
"legal_person" varchar(32) COLLATE "default" DEFAULT NULL::character varying,
"legal_person_num" varchar(100) COLLATE "default" DEFAULT NULL::character varying,
"regdate" date,
"expiration_date" date,
"business_scope" varchar(512) COLLATE "default" DEFAULT NULL::character varying,
"contacts" varchar(32) COLLATE "default" DEFAULT NULL::character varying,
"picture_url" varchar(128) COLLATE "default" DEFAULT NULL::character varying,
"create_time" timestamp(0),
"update_time" timestamp(0),
"audit_user_id" int2,
"deleted" int2 DEFAULT 0,
"account" varchar(255) COLLATE "default" DEFAULT NULL::character varying,
"password" varchar(255) COLLATE "default" DEFAULT NULL::character varying,
"tel" varchar(13) COLLATE "default" DEFAULT NULL::character varying,
"email" varchar(60) COLLATE "default" DEFAULT NULL::character varying
);
COMMENT ON TABLE "app_customer" IS '企业信息表';
COMMENT ON COLUMN "app_customer"."id" IS '企业ID';

COMMENT ON COLUMN "app_customer"."name" IS '企业名称';

COMMENT ON COLUMN "app_customer"."type" IS '企业类型';

COMMENT ON COLUMN "app_customer"."status" IS '企业状态（0正常，1冻结）';

COMMENT ON COLUMN "app_customer"."logon_status" IS '企业注册审核状态（0审核中，1已通过，2未通过）';

COMMENT ON COLUMN "app_customer"."attest_status" IS '企业认证状态（0未认证，1认证中，2已通过，3未通过）';

COMMENT ON COLUMN "app_customer"."social_code" IS '社会代码';

COMMENT ON COLUMN "app_customer"."address" IS '地址';

COMMENT ON COLUMN "app_customer"."legal_person" IS '法人';

COMMENT ON COLUMN "app_customer"."legal_person_num" IS '法人身份证号（sm4加密）';

COMMENT ON COLUMN "app_customer"."regdate" IS '注册日期';

COMMENT ON COLUMN "app_customer"."expiration_date" IS '营业有效期';

COMMENT ON COLUMN "app_customer"."business_scope" IS '经营范围';

COMMENT ON COLUMN "app_customer"."contacts" IS '联系人';

COMMENT ON COLUMN "app_customer"."picture_url" IS '图片（营业执照）存储路径';

COMMENT ON COLUMN "app_customer"."create_time" IS '创建时间';

COMMENT ON COLUMN "app_customer"."update_time" IS '更新时间';

COMMENT ON COLUMN "app_customer"."audit_user_id" IS '审核人';

COMMENT ON COLUMN "app_customer"."deleted" IS '是否删除0正常；1删除';

COMMENT ON COLUMN "app_customer"."account" IS '账号';

COMMENT ON COLUMN "app_customer"."password" IS '密码';

COMMENT ON COLUMN "app_customer"."tel" IS '手机号';

COMMENT ON COLUMN "app_customer"."email" IS '邮箱';

-- 企业角色
INSERT INTO "sys_role" ("role_id", "role_name", "role_key", "role_sort", "data_scope", "status", "remark", "creator", "create_time", "updater", "update_time", "deleted")
VALUES ('3', '企业', 'customer', '3', '2', '0', '企业端角色', 'admin', '2023-04-11 14:51:59', NULL, NULL, '0');



-- 资产表
DROP TABLE IF EXISTS "app_asset";
CREATE TABLE "app_asset" (
"id" SERIAL NOT NULL,
"customer_id" int2 NOT NULL,
"name" varchar(128) COLLATE "default" DEFAULT NULL::character varying,
"type" int2 DEFAULT 1,
"sort" int2 DEFAULT 0,
"net_work" int2 DEFAULT 1,
"ip" varchar(255) COLLATE "default" NOT NULL,
"mac" varchar(17) COLLATE "default" DEFAULT NULL::character varying,
"importance" int2 DEFAULT 0,
"desc" varchar(128) COLLATE "default" DEFAULT NULL::character varying,
"create_time" timestamp(0),
"update_time" timestamp(0),
"deleted" int2 DEFAULT 0,
"account" varchar(128) COLLATE "default" DEFAULT NULL::character varying,
"password" varchar(255) COLLATE "default" DEFAULT NULL::character varying
);
COMMENT ON TABLE "app_asset" IS '资产表';
COMMENT ON COLUMN "app_asset"."customer_id" IS '企业id';

COMMENT ON COLUMN "app_asset"."name" IS '资产名称';

COMMENT ON COLUMN "app_asset"."type" IS '类型1主机2web';

COMMENT ON COLUMN "app_asset"."sort" IS '资产类别';

COMMENT ON COLUMN "app_asset"."net_work" IS '1内网2外网';

COMMENT ON COLUMN "app_asset"."ip" IS 'ip、url（sm4加密）';

COMMENT ON COLUMN "app_asset"."mac" IS 'mac';

COMMENT ON COLUMN "app_asset"."importance" IS '重要程度';

COMMENT ON COLUMN "app_asset"."desc" IS '描述';

COMMENT ON COLUMN "app_asset"."create_time" IS '创建时间';

COMMENT ON COLUMN "app_asset"."update_time" IS '修改时间';

COMMENT ON COLUMN "app_asset"."deleted" IS '是否删除0正常；1删除';

COMMENT ON COLUMN "app_asset"."account" IS '账号';

COMMENT ON COLUMN "app_asset"."password" IS '密码';
