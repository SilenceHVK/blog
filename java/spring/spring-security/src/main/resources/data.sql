SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for user_info
-- ----------------------------
DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info` (
    `user_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '管理员ID',
    `user_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
    `encrypted_password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '登录密码',
    `role` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户角色',
    PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Records of user_info
-- ----------------------------
BEGIN;
INSERT INTO `user_info` VALUES ('001', 'admin', '$2a$10$T27zdP0qn9MK48l6lbAG7eSE3jnolaLWBhGOG9gnn26kkgOVRuoIW', 'admin');
INSERT INTO `user_info` VALUES ('003', 'user', '$2a$10$T27zdP0qn9MK48l6lbAG7eSE3jnolaLWBhGOG9gnn26kkgOVRuoIW', 'normal');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;