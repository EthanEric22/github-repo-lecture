-- ----------------------------
-- Table structure for `t_user`
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `no` int NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `userid` varchar(256) NOT NULL,
  `username` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  UNIQUE KEY `unique_userid_password` (`userid`, `password`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
ALTER TABLE `t_user` AUTO_INCREMENT = 1;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` (`userid`, `username`, `password`, `phone`) VALUES ('supervisor', '担当者', '9204876', '13749582345');
INSERT INTO `t_user` (`userid`, `username`, `password`, `phone`) VALUES ('mario', 'マリオ', '8701924', '13948579264');
INSERT INTO `t_user` (`userid`, `username`, `password`, `phone`) VALUES ('luigi', 'ルイジ', '5623918', '13847291650');
INSERT INTO `t_user` (`userid`, `username`, `password`, `phone`) VALUES ('net', 'ネット名', '3847261', '13928716503');
INSERT INTO `t_user` (`userid`, `username`, `password`, `phone`) VALUES ('test', 'テスト', '1903728', '13790543861');
INSERT INTO `t_user` (`userid`, `username`, `password`, `phone`) VALUES ('chief', '主任', '8394726', '13598467291');
INSERT INTO `t_user` (`userid`, `username`, `password`, `phone`) VALUES ('administrator', '管理者', '9873216', '13856231047');
INSERT INTO `t_user` (`userid`, `username`, `password`, `phone`) VALUES ('usertest', 'ユーザー テスト', '4873921', '13765498237');
INSERT INTO `t_user` (`userid`, `username`, `password`, `phone`) VALUES ('network', 'ネットワーク', '2037581', '13902837456');
INSERT INTO `t_user` (`userid`, `username`, `password`, `phone`) VALUES ('artdesign', '美工デザイン', '3827615', '13687249172');
INSERT INTO `t_user` (`userid`, `username`, `password`, `phone`) VALUES ('frontenddevelopment', 'フロントエンド開発', '9482037', '13574920683');
INSERT INTO `t_user` (`userid`, `username`, `password`, `phone`) VALUES ('backenddevelopment', 'バックエンド開発', '5619238', '13950178462');
INSERT INTO `t_user` (`userid`, `username`, `password`, `phone`) VALUES ('channeldevelopemtn', 'チャネル開発', '4895612', '13791562084');
INSERT INTO `t_user` (`userid`, `username`, `password`, `phone`) VALUES ('testuser', 'テストユーザー', '3278946', '13824680571');
INSERT INTO `t_user` (`userid`, `username`, `password`, `phone`) VALUES ('systemuser', 'システムユーザー', '2743908', '13952093781');
INSERT INTO `t_user` (`userid`, `username`, `password`, `phone`) VALUES ('maintenance', 'システム保守', '9831247', '13657819203');
INSERT INTO `t_user` (`userid`, `username`, `password`, `phone`) VALUES ('systemdesign', 'システム設計', '8472931', '13749156208');
INSERT INTO `t_user` (`userid`, `username`, `password`, `phone`) VALUES ('softwaredevelopment', 'ソフトウェア開発', '6507823', '13908456792');



