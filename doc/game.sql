/*
 Navicat Premium Data Transfer

 Source Server         : game
 Source Server Type    : MySQL
 Source Server Version : 50650
 Source Host           : 47.241.62.70:3306
 Source Schema         : game

 Target Server Type    : MySQL
 Target Server Version : 50650
 File Encoding         : 65001

 Date: 14/04/2022 10:42:24
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for ball_admin
-- ----------------------------
DROP TABLE IF EXISTS `ball_admin`;
CREATE TABLE `ball_admin` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `admin_name` varchar(255) DEFAULT NULL,
  `admin_password` varchar(255) DEFAULT NULL,
  `nickname` varchar(20) DEFAULT NULL,
  `token` varchar(255) NOT NULL,
  `google_code` varchar(255) DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL,
  `menu_group_id` bigint(20) DEFAULT NULL,
  `created_at` bigint(20) DEFAULT '0' COMMENT '创建时间',
  `updated_at` bigint(20) DEFAULT '0' COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `menu_group_id` (`menu_group_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='后端管理账号';

-- ----------------------------
-- Records of ball_admin
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for ball_announcement
-- ----------------------------
DROP TABLE IF EXISTS `ball_announcement`;
CREATE TABLE `ball_announcement` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `content` text COMMENT '提现名称',
  `language` varchar(20) DEFAULT '0' COMMENT '语言编码',
  `status` tinyint(4) DEFAULT '1' COMMENT '1正常 2禁用',
  `created_at` bigint(20) DEFAULT '0' COMMENT '创建时间',
  `updated_at` bigint(20) DEFAULT '0' COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='轮播公告';

-- ----------------------------
-- Records of ball_announcement
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for ball_balance_change
-- ----------------------------
DROP TABLE IF EXISTS `ball_balance_change`;
CREATE TABLE `ball_balance_change` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `player_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家id',
  `change_money` bigint(20) DEFAULT '0' COMMENT '变化金额',
  `init_money` bigint(20) DEFAULT '0' COMMENT '初始金额',
  `dned_money` bigint(20) DEFAULT NULL COMMENT '变化后的金额',
  `balance_change_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '1充值 2提现 3投注 4赢 5佣金 6人工 ',
  `remark` varchar(255) DEFAULT '' COMMENT '备注',
  `created_at` bigint(20) DEFAULT '0' COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `player_id` (`player_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='账变表';

-- ----------------------------
-- Records of ball_balance_change
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for ball_bank_card
-- ----------------------------
DROP TABLE IF EXISTS `ball_bank_card`;
CREATE TABLE `ball_bank_card` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `player_id` bigint(20) NOT NULL COMMENT '玩家ID',
  `card_number` varchar(255) NOT NULL DEFAULT '' COMMENT '卡号',
  `bank_name` varchar(255) NOT NULL DEFAULT '' COMMENT '银行名字',
  `back_encoding` varchar(20) DEFAULT '' COMMENT '银行编码',
  `card_name` varchar(1) DEFAULT '' COMMENT '持卡人姓名',
  `country` varchar(255) DEFAULT '' COMMENT '国际',
  `province` varchar(255) DEFAULT '' COMMENT '省份',
  `city` varchar(255) NOT NULL DEFAULT '' COMMENT '城市',
  `sub_branch` varchar(255) NOT NULL DEFAULT '' COMMENT '支行',
  `status` tinyint(20) DEFAULT '1' COMMENT '状态 1正常 2禁用',
  `back_card_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT 'usdt 地址',
  `protocol` varchar(255) DEFAULT '' COMMENT '协议',
  `created_at` bigint(20) DEFAULT '0' COMMENT '创建时间',
  `updated_at` bigint(20) DEFAULT '0' COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `player_id` (`player_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='银行卡';

-- ----------------------------
-- Records of ball_bank_card
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for ball_bet
-- ----------------------------
DROP TABLE IF EXISTS `ball_bet`;
CREATE TABLE `ball_bet` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `game_id` bigint(20) DEFAULT '0' COMMENT '游戏id ',
  `player_id` bigint(20) DEFAULT '0' COMMENT '玩家id',
  `game_loss_per_cent_id` bigint(20) DEFAULT '0' COMMENT '游戏赔率 id(索引)',
  `bet_money` bigint(20) DEFAULT '0' COMMENT '下住金额',
  `hand_money` bigint(20) DEFAULT '0' COMMENT '手续费',
  `winning_amount` bigint(20) DEFAULT '0' COMMENT '中奖金额',
  `status` tinyint(4) DEFAULT '1' COMMENT '1 未结算 2已结结算 3撤单  4回滚',
  `created_at` bigint(20) DEFAULT '0' COMMENT '创建时间',
  `updated_at` bigint(20) DEFAULT '0' COMMENT '结算时间 默认为 0',
  PRIMARY KEY (`id`),
  KEY `game_id` (`game_id`),
  KEY `p` (`player_id`),
  KEY `d` (`game_loss_per_cent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='下注';

-- ----------------------------
-- Records of ball_bet
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for ball_channel_of_disbursement
-- ----------------------------
DROP TABLE IF EXISTS `ball_channel_of_disbursement`;
CREATE TABLE `ball_channel_of_disbursement` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL DEFAULT '' COMMENT '支付渠道名字',
  `disbursementzh_type` tinyint(4) DEFAULT '0' COMMENT '支付类型  自行填写',
  `channel_type` tinyint(4) DEFAULT '0' COMMENT '通道类型 1 线上支付 2线下支付 ',
  `kinds` tinyint(4) DEFAULT '0' COMMENT '1 支付渠道 2提现渠道',
  `status` tinyint(20) DEFAULT '1' COMMENT '状态 1开启 2关闭',
  `created_at` bigint(20) DEFAULT '0' COMMENT '创建时间',
  `updated_at` bigint(20) DEFAULT '0' COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='支付/提现渠道';

-- ----------------------------
-- Records of ball_channel_of_disbursement
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for ball_commission_strategy
-- ----------------------------
DROP TABLE IF EXISTS `ball_commission_strategy`;
CREATE TABLE `ball_commission_strategy` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL DEFAULT '' COMMENT '优惠名称',
  `commission_strategy_type` tinyint(4) DEFAULT '0' COMMENT '1 下注返佣 2盈利返佣 3充值返佣',
  `commission_level` tinyint(4) DEFAULT '0' COMMENT '返佣层级',
  `automatic_distribution` tinyint(4) DEFAULT '0' COMMENT '自动发放 1 自动 2不自动',
  `status` tinyint(20) DEFAULT '1' COMMENT '状态 1开启 2关闭',
  `created_at` bigint(20) DEFAULT '0' COMMENT '创建时间',
  `updated_at` bigint(20) DEFAULT '0' COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='反佣策略';

-- ----------------------------
-- Records of ball_commission_strategy
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for ball_deposit_policy
-- ----------------------------
DROP TABLE IF EXISTS `ball_deposit_policy`;
CREATE TABLE `ball_deposit_policy` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL DEFAULT '' COMMENT '优惠名称',
  `deposit_policy_type` tinyint(4) DEFAULT '0' COMMENT '优惠类型 1首冲 2每次',
  `start_time` bigint(20) DEFAULT '0' COMMENT '开始时间',
  `end_time` bigint(20) DEFAULT '0' COMMENT '结束时间',
  `preferential_standard` varchar(255) DEFAULT '' COMMENT '优惠标准',
  `preferential_per` varchar(255) NOT NULL DEFAULT '' COMMENT '优惠百分比',
  `preferential_top` varchar(255) NOT NULL DEFAULT '' COMMENT '优惠上限',
  `status` tinyint(20) DEFAULT '1' COMMENT '状态 1开启 2关闭',
  `remark` varchar(255) DEFAULT '' COMMENT '协议',
  `created_at` bigint(20) DEFAULT '0' COMMENT '创建时间',
  `updated_at` bigint(20) DEFAULT '0' COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='存款策略';

-- ----------------------------
-- Records of ball_deposit_policy
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for ball_game
-- ----------------------------
DROP TABLE IF EXISTS `ball_game`;
CREATE TABLE `ball_game` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `alliance_logo` varchar(255) DEFAULT '' COMMENT '联盟logo',
  `alliance_name` varchar(255) DEFAULT '' COMMENT '联盟名字',
  `main_logo` varchar(255) DEFAULT '' COMMENT '主队logo',
  `main_name` varchar(255) DEFAULT '' COMMENT '主队名字',
  `guest_logo` varchar(255) DEFAULT '' COMMENT '客队logo',
  `guest_name` varchar(255) DEFAULT '' COMMENT '客队名字',
  `start_time` bigint(20) DEFAULT '0' COMMENT '开赛时间',
  `score` varchar(255) DEFAULT '' COMMENT '比分 0:0/(0-0)  括号库里面是上半场',
  `game_status` tinyint(4) DEFAULT '1' COMMENT '比赛状态 1 没有开始 2正常进行  3结束',
  `top` tinyint(4) DEFAULT '2' COMMENT '置顶  1置顶 2 不指定',
  `hot` tinyint(4) DEFAULT '2' COMMENT '是否热门 1 热门 2非热门',
  `even` tinyint(4) DEFAULT '2' COMMENT '是否保本 1保本 2不保本(默认为2)',
  `status` tinyint(4) DEFAULT '1' COMMENT '状态 1开启  2关闭',
  `created_at` bigint(20) DEFAULT '0' COMMENT '入库时间',
  `updated_at` bigint(20) DEFAULT '0' COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='游戏赛事';

-- ----------------------------
-- Records of ball_game
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for ball_game_loss_per_cent
-- ----------------------------
DROP TABLE IF EXISTS `ball_game_loss_per_cent`;
CREATE TABLE `ball_game_loss_per_cent` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `game_id` bigint(20) DEFAULT NULL COMMENT '游戏id ',
  `score` varchar(255) DEFAULT '' COMMENT '比分',
  `game_type` tinyint(4) DEFAULT '0' COMMENT '比赛类型 1全场 2上半场',
  `loss_per_cent` int(11) DEFAULT '0' COMMENT '赔率',
  `even` varchar(255) DEFAULT '2' COMMENT '是否保本 1保本 2不保本(默认为2)',
  `status` tinyint(4) DEFAULT '1' COMMENT '状态 1开启  2关闭',
  `created_at` bigint(20) DEFAULT '0' COMMENT '创建时间',
  `updated_at` bigint(20) DEFAULT '0' COMMENT '同步时间',
  PRIMARY KEY (`id`),
  KEY `game_id` (`game_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='游戏赔率';


-- ----------------------------
-- Table structure for ball_logger
-- ----------------------------
DROP TABLE IF EXISTS `ball_logger`;
CREATE TABLE `ball_logger` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `logger_kinkds` tinyint(4) DEFAULT '0' COMMENT '日志种类 1下注日志(玩家) 2登录日志 3操作日志',
  `admin_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '管理员id  默认为0',
  `player_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家日志 默认为 0',
  `content` text COMMENT '日志 内容 ',
  `ip` varchar(255) DEFAULT '' COMMENT '操作的ip',
  `order_number` varchar(255) DEFAULT '' COMMENT '订单号',
  `unit_type` varchar(255) DEFAULT '' COMMENT '设备型号',
  `created_at` bigint(20) DEFAULT '0' COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `admin_id` (`admin_id`),
  KEY `p` (`player_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='日志表';

-- ----------------------------
-- Records of ball_logger
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for ball_payment_management
-- ----------------------------
DROP TABLE IF EXISTS `ball_payment_management`;
CREATE TABLE `ball_payment_management` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `channel_of_disbursement_id` varchar(255) NOT NULL COMMENT '支付渠道 id  (显示三个参数 在前段)',
  `shopper_nickname` varchar(255) NOT NULL COMMENT '商户别名',
  `front_display` tinyint(4) NOT NULL COMMENT '前台是否显示 1显示 2不显示',
  `collection_key` varchar(255) DEFAULT '' COMMENT '代收秘钥',
  `pay_key` varchar(255) DEFAULT '' COMMENT '代付秘钥',
  `stop_money` bigint(20) DEFAULT '0' COMMENT '停用金额',
  `automatic_exchange_rate` tinyint(4) DEFAULT '0' COMMENT '自动汇率 1开启 2关闭',
  `exchange_rate` varchar(255) NOT NULL COMMENT '汇率(自动汇率打开 这个不生效)',
  `max_get_money` varchar(255) NOT NULL DEFAULT '0' COMMENT '最高入款金额',
  `min_get_money` bigint(20) DEFAULT '0' COMMENT '最低入款金额',
  `expiration_time` bigint(20) NOT NULL COMMENT '过期时间 订单',
  `front_explain` varchar(255) DEFAULT '' COMMENT '前台说明',
  `created_at` bigint(11) DEFAULT '0' COMMENT '创建时间',
  `updated_at` bigint(11) DEFAULT '0' COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='支付管理';

-- ----------------------------
-- Records of ball_payment_management
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for ball_player
-- ----------------------------
DROP TABLE IF EXISTS `ball_player`;
CREATE TABLE `ball_player` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `version` bigint(20) DEFAULT NULL,
  `username` varchar(255) NOT NULL COMMENT '用户名',
  `password` varchar(255) NOT NULL COMMENT '密码',
  `invitation_code` varchar(255) NOT NULL COMMENT '邀请码(每个账户生成的时候就会生成唯一性)',
  `superior_id` bigint(20) DEFAULT NULL COMMENT '直属上级(添加索引)',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态1正常 2封禁',
  `account_type` int(1) DEFAULT NULL COMMENT '账号类型 1测试号 2正常号 3代理账号',
  `balance` bigint(20) DEFAULT '0' COMMENT '钱包余额(2位小数/100)',
  `token` varchar(255) NOT NULL COMMENT 'token  用户的唯一标识',
  `the_new_ip` varchar(255) NOT NULL COMMENT '最新的一次登录ip',
  `cumulative_reflect` bigint(20) DEFAULT '0' COMMENT '累计提现',
  `the_last_ip` varchar(255) NOT NULL COMMENT '上一次登录的ip',
  `vip_level` tinyint(11) DEFAULT NULL COMMENT '会员的级别  比如他的上一级是 3  他就是4',
  `cumulative_top_up` bigint(20) DEFAULT '0' COMMENT '累计充值',
  `cumulative_winning` bigint(20) DEFAULT '0' COMMENT '累计中奖',
  `promote_income` bigint(20) DEFAULT '0' COMMENT '推广收入',
  `e_mail` varchar(255) DEFAULT NULL COMMENT '电子邮箱',
  `phone` varchar(255) DEFAULT NULL COMMENT '手机号码',
  `group_size` int(11) DEFAULT '0' COMMENT '团队人数',
  `max_reflect` bigint(20) DEFAULT '0' COMMENT '最大提现金额',
  `max_top_up` bigint(20) DEFAULT '0' COMMENT '最大的充值金额',
  `first_reflect` bigint(20) DEFAULT '0' COMMENT '首次提现金额',
  `first_top_up` bigint(20) DEFAULT '0' COMMENT '首次充值金额',
  `first_top_up_time` bigint(20) DEFAULT '0' COMMENT '首次充值金额时间',
  `cumulative_Qr` bigint(20) DEFAULT '0' COMMENT '累计打码量',
  `need_Qr` bigint(20) DEFAULT '0' COMMENT '离下次提现所需要的打码量',
  `accumulative_bet` bigint(20) DEFAULT '0' COMMENT '累计投注金额',
  `on_line_top_up` bigint(20) DEFAULT '0' COMMENT '线上充值金额',
  `offline_top_up` bigint(20) DEFAULT '0' COMMENT '线下充值金额',
  `artificial_add` bigint(20) DEFAULT '0' COMMENT '人工加款',
  `artificial_subtract` bigint(20) DEFAULT '0' COMMENT '人工减款',
  `cumulative_back_water` bigint(20) DEFAULT '0' COMMENT '累计反水',
  `directly_subordinate_num` int(11) DEFAULT '0' COMMENT '直属下级个数',
  `reflect_times` int(11) DEFAULT '0' COMMENT '提现次数',
  `created_at` bigint(11) DEFAULT '0' COMMENT '创建时间',
  `updated_at` bigint(11) DEFAULT '0' COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='玩家账号';

-- ----------------------------
-- Records of ball_player
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for ball_record
-- ----------------------------
DROP TABLE IF EXISTS `ball_record`;
CREATE TABLE `ball_record` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `player_id` bigint(20) DEFAULT '0' COMMENT '玩家id',
  `money` bigint(20) DEFAULT '0' COMMENT '金额',
  `record_type` tinyint(4) DEFAULT '0' COMMENT '账单类型 1线下充值 2线上充值 3提现 4佣金  5奖励的金额 6人工加款 ',
  `status` tinyint(4) DEFAULT '1' COMMENT '状态 1正常(审核通过) 2待审核 3审核不通过',
  `remark` varchar(255) DEFAULT '' COMMENT '备注(比如审核不通过的原因)',
  `created_at` bigint(20) DEFAULT '0' COMMENT '创建时间',
  `updated_at` bigint(20) DEFAULT '0' COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `player_id` (`player_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='财务账单';

-- ----------------------------
-- Records of ball_record
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for ball_slideshow
-- ----------------------------
DROP TABLE IF EXISTS `ball_slideshow`;
CREATE TABLE `ball_slideshow` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT '0' COMMENT '游戏id ',
  `policy_type` tinyint(4) DEFAULT '0' COMMENT '策略类型 1存款策略 2返佣策略',
  `deposit_policy_id` bigint(20) DEFAULT '0' COMMENT '存款策略id',
  `commission_strategy_id` bigint(20) DEFAULT '0' COMMENT '返佣策略id',
  `language` varchar(20) DEFAULT '0' COMMENT '语言编码',
  `image_url` varchar(255) DEFAULT '' COMMENT '图片地址',
  `status` tinyint(4) DEFAULT '1' COMMENT '1显示 2不显示(前提是 返佣和 存款策略id 为0)',
  `created_at` bigint(20) DEFAULT '0' COMMENT '创建时间',
  `updated_at` bigint(20) DEFAULT '0' COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='轮播图';

-- ----------------------------
-- Records of ball_slideshow
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for ball_system_config
-- ----------------------------
DROP TABLE IF EXISTS `ball_system_config`;
CREATE TABLE `ball_system_config` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `register_if_need_verification_code` tinyint(4) DEFAULT '0' COMMENT '注册是否需要验证码 1需要 2不需要',
  `verification_code_layout` tinyint(4) DEFAULT '0' COMMENT '验证码 格式 1 纯数字 2串字母 3字母数字',
  `password_max_error_times` tinyint(4) DEFAULT '0' COMMENT '密码连续错误的次数',
  `password_error_lock_time` int(11) DEFAULT '0' COMMENT '密码连续错误锁屏时间(秒)',
  `server_url` varchar(255) DEFAULT '' COMMENT '客服连接',
  `card_can_need_nums` int(11) DEFAULT '0' COMMENT '会员最多绑卡数量',
  `recharge_code_conversion_rate` bigint(20) DEFAULT '0' COMMENT '充值打码量转换比例',
  `captcha_threshold` bigint(20) DEFAULT '0' COMMENT '用户打码设量设置阀值',
  `bet_hand_money_rate` bigint(20) DEFAULT '0' COMMENT '投注手续费率',
  `fast_money` bigint(20) DEFAULT '0' COMMENT '快捷金额',
  `usdt_withdraw_per` bigint(20) DEFAULT '0' COMMENT 'usdt 提现汇率',
  `withdraw_usdt_automatic_per` bigint(20) DEFAULT '0' COMMENT '提现usdt自动汇率',
  `even_need_hand_money` bigint(20) NOT NULL DEFAULT '0' COMMENT '保本扣除手续费',
  `max_usdt_account_nums` int(11) NOT NULL DEFAULT '0' COMMENT '最多可绑定usdt账号数量',
  `max_pix_account_nums` int(11) NOT NULL DEFAULT '0' COMMENT '最多可绑定pix账号数量',
  `withdraw_password_can_update` tinyint(4) NOT NULL DEFAULT '0' COMMENT '提现密码是否可以修改 1 可以 2不可以',
  `withdraw_password_show_need` tinyint(4) NOT NULL DEFAULT '0' COMMENT '控制首页提现密码是否可以关闭',
  `everyday_withdraw_times` int(11) NOT NULL DEFAULT '0' COMMENT '每日的提现上线次数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统配置';

-- ----------------------------
-- Records of ball_system_config
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for ball_system_notice
-- ----------------------------
DROP TABLE IF EXISTS `ball_system_notice`;
CREATE TABLE `ball_system_notice` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(255) DEFAULT '' COMMENT '标题',
  `content` text COMMENT '内容',
  `language` varchar(20) DEFAULT '0' COMMENT '语言编码',
  `status` tinyint(4) DEFAULT '1' COMMENT '1正常 2禁用',
  `created_at` bigint(20) DEFAULT '0' COMMENT '创建时间',
  `updated_at` bigint(20) DEFAULT '0' COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统公告';

-- ----------------------------
-- Records of ball_system_notice
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for ball_withdraw_management
-- ----------------------------
DROP TABLE IF EXISTS `ball_withdraw_management`;
CREATE TABLE `ball_withdraw_management` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `channel_of_disbursement_id` bigint(20) DEFAULT '0' COMMENT '提现渠道',
  `name` varchar(255) DEFAULT '0' COMMENT '提现名称',
  `iamge_url` varchar(255) DEFAULT '0' COMMENT '图片地址',
  `language` varchar(20) DEFAULT '0' COMMENT '语言编码',
  `status` tinyint(4) DEFAULT '1' COMMENT '1启用 2关闭',
  `created_at` bigint(20) DEFAULT '0' COMMENT '创建时间',
  `updated_at` bigint(20) DEFAULT '0' COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='提现方式';

-- ----------------------------
-- Records of ball_withdraw_management
-- ----------------------------
BEGIN;
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
