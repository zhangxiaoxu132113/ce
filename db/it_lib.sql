/*
Navicat MySQL Data Transfer

Source Server         : 本地
Source Server Version : 50624
Source Host           : localhost:3306
Source Database       : mw_web_app

Target Server Type    : MYSQL
Target Server Version : 50624
File Encoding         : 65001

Date: 2017-03-01 17:22:39
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for it_lib
-- ----------------------------
DROP TABLE IF EXISTS `it_lib`;
CREATE TABLE `it_lib` (
  `id` varchar(36) NOT NULL,
  `name` varchar(100) NOT NULL,
  `pic` varchar(1000) DEFAULT NULL,
  `bg_pic` varchar(1000) DEFAULT NULL,
  `url` varchar(1000) DEFAULT NULL,
  `category` int(11) DEFAULT NULL,
  `create_on` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of it_lib
-- ----------------------------
INSERT INTO `it_lib` VALUES ('0145ac4c-2c77-432a-9204-2b2729352003', 'AngularJS', 'http://img.knowledge.csdn.net/upload/base/1458526868366_366.jpg', 'http://img.knowledge.csdn.net/upload/base/1458526868518_518.jpg', 'http://lib.csdn.net/base/angularjs', '2', '1488334691213');
INSERT INTO `it_lib` VALUES ('02d2a04a-8c4a-4b8d-aefa-9794a32d342a', 'Python', 'http://img.knowledge.csdn.net/upload/base/1452500783406_406.jpg', 'http://img.knowledge.csdn.net/upload/base/1452500783519_519.jpg', 'http://lib.csdn.net/base/python', '2', '1488334688789');
INSERT INTO `it_lib` VALUES ('044e4f7f-d4c4-4c94-98e6-f0a4e9fb6696', 'Cocos引擎', 'http://img.knowledge.csdn.net/upload/base/1464328595855_855.jpg', 'http://img.knowledge.csdn.net/upload/base/1464328597315_315.jpg', 'http://lib.csdn.net/base/cocos', '2', '1488334690772');
INSERT INTO `it_lib` VALUES ('066c5316-dbc7-44ed-8ac0-9858039aba1b', 'C#', 'http://img.knowledge.csdn.net/upload/base/1462342584443_443.jpg', 'http://img.knowledge.csdn.net/upload/base/1462327594341_341.jpg', 'http://lib.csdn.net/base/csharp', '2', '1488334691118');
INSERT INTO `it_lib` VALUES ('0bd5ddb1-caa4-4144-a84a-8baccefb8239', 'iOS', 'http://img.knowledge.csdn.net/upload/base/1472802208692_692.jpg', 'http://img.knowledge.csdn.net/upload/base/1472802209070_70.jpg', 'http://lib.csdn.net/base/ios', '2', '1488334688912');
INSERT INTO `it_lib` VALUES ('0e89e208-b374-45bd-aae5-3f2bcdd30d02', '深度学习', 'http://img.knowledge.csdn.net/upload/base/1474966029376_376.jpg', 'http://img.knowledge.csdn.net/upload/base/1474966029754_754.jpg', 'http://lib.csdn.net/base/deeplearning', '2', '1488334690083');
INSERT INTO `it_lib` VALUES ('165802b5-db5b-4a7f-a4d2-035111cdb9f3', 'Docker', 'http://img.knowledge.csdn.net/upload/base/1452500466484_484.jpg', 'http://img.knowledge.csdn.net/upload/base/1452500466656_656.jpg', 'http://lib.csdn.net/base/docker', '2', '1488334690366');
INSERT INTO `it_lib` VALUES ('17c79a3e-a625-4810-8cb1-211a3979c04a', 'OpenCV', 'http://img.knowledge.csdn.net/upload/base/1467269388326_326.jpg', 'http://img.knowledge.csdn.net/upload/base/1467269388411_411.jpg', 'http://lib.csdn.net/base/opencv', '2', '1488334690440');
INSERT INTO `it_lib` VALUES ('20c51a1a-9986-4a64-a950-20ded1a9428f', '虚拟现实（VR）', 'http://img.knowledge.csdn.net/upload/base/1461807587796_796.jpg', 'http://img.knowledge.csdn.net/upload/base/1461807588401_401.jpg', 'http://lib.csdn.net/base/vr', '2', '1488334691143');
INSERT INTO `it_lib` VALUES ('28004f09-0315-42c2-bc60-a0a156fca7e4', '机器学习', 'http://img.knowledge.csdn.net/upload/base/1452498104247_247.jpg', 'http://img.knowledge.csdn.net/upload/base/1452672714363_363.jpg', 'http://lib.csdn.net/base/machinelearning', '2', '1488334690133');
INSERT INTO `it_lib` VALUES ('2aeeccaa-b20c-41be-ba2f-c47542e94c7a', '.NET', 'http://img.knowledge.csdn.net/upload/base/1470876331285_285.jpg', 'http://img.knowledge.csdn.net/upload/base/1470876331774_774.jpg', 'http://lib.csdn.net/base/dotnet', '2', '1488334690402');
INSERT INTO `it_lib` VALUES ('2d1ad403-c9ed-4c18-916e-83af24990d78', 'C++', 'http://img.knowledge.csdn.net/upload/base/1466674255874_874.jpg', 'http://img.knowledge.csdn.net/upload/base/1466674256106_106.jpg', 'http://lib.csdn.net/base/cplusplus', '2', '1488334690697');
INSERT INTO `it_lib` VALUES ('2d4db7e1-01f2-4b34-9720-b67910e3aa44', 'Java', 'http://img.knowledge.csdn.net/upload/base/1453701371636_636.jpg', 'http://img.knowledge.csdn.net/upload/base/1453701371793_793.jpg', 'http://lib.csdn.net/base/java', '2', '1488334689770');
INSERT INTO `it_lib` VALUES ('2dd03d24-7740-49e6-bb15-eee263bb5a4e', 'Apache Spark', 'http://img.knowledge.csdn.net/upload/base/1453368118762_762.jpg', 'http://img.knowledge.csdn.net/upload/base/1453368118941_941.jpg', 'http://lib.csdn.net/base/spark', '2', '1488334690378');
INSERT INTO `it_lib` VALUES ('2e37bc73-2671-4a60-9886-6dad9834531b', 'Hadoop', 'http://img.knowledge.csdn.net/upload/base/1457509711701_701.jpg', 'http://img.knowledge.csdn.net/upload/base/1457509712049_49.jpg', 'http://lib.csdn.net/base/hadoop', '2', '1488334689722');
INSERT INTO `it_lib` VALUES ('2eaebc3e-5a88-48fb-81de-bdeb8c5cf617', 'Java SE', 'http://img.knowledge.csdn.net/upload/base/1453169124297_297.jpg', 'http://img.knowledge.csdn.net/upload/base/1453169124795_795.jpg', 'http://lib.csdn.net/base/javase', '2', '1488334688812');
INSERT INTO `it_lib` VALUES ('30914321-b068-4571-8956-48edf63acdfe', 'Android', 'http://img.knowledge.csdn.net/upload/base/1455589744328_328.jpg', 'http://img.knowledge.csdn.net/upload/base/1455589744746_746.jpg', 'http://lib.csdn.net/base/android', '2', '1488334689734');
INSERT INTO `it_lib` VALUES ('3c5f7ac8-44f8-4484-8142-fb2b95465811', 'Unity3D', 'http://img.knowledge.csdn.net/upload/base/1470302448213_213.jpg', 'http://img.knowledge.csdn.net/upload/base/1470302448986_986.jpg', 'http://lib.csdn.net/base/unity3d', '2', '1488334690146');
INSERT INTO `it_lib` VALUES ('4560702e-4da4-4503-9a02-bda674f3c3b6', 'MongoDB', 'http://img.knowledge.csdn.net/upload/base/1478676229520_520.jpg', 'http://img.knowledge.csdn.net/upload/base/1478676230624_624.jpg', 'http://lib.csdn.net/base/mongodb', '2', '1488334689801');
INSERT INTO `it_lib` VALUES ('4c66f1cf-e7b3-4382-a5dd-731772805804', 'Oracle', 'http://img.knowledge.csdn.net/upload/base/1469610323861_861.jpg', 'http://img.knowledge.csdn.net/upload/base/1469610324168_168.jpg', 'http://lib.csdn.net/base/oracle', '2', '1488334690416');
INSERT INTO `it_lib` VALUES ('4f8ff99a-3cc9-4528-af2f-9d1d7b7ba6eb', '敏捷', 'http://img.knowledge.csdn.net/upload/base/1471918993092_92.jpg', 'http://img.knowledge.csdn.net/upload/base/1471918993684_684.jpg', 'http://lib.csdn.net/base/agile', '2', '1488334690108');
INSERT INTO `it_lib` VALUES ('552b045f-61a9-4056-b2db-6c18784c13b0', 'Linux', 'http://img.knowledge.csdn.net/upload/base/1468390230134_134.jpg', 'http://img.knowledge.csdn.net/upload/base/1468390230399_399.jpg', 'http://lib.csdn.net/base/linux', '2', '1488334690427');
INSERT INTO `it_lib` VALUES ('58ee0bd7-42bf-4fff-baaa-ccd602e360fc', 'Go', 'http://img.knowledge.csdn.net/upload/base/1471395508721_721.jpg', 'http://img.knowledge.csdn.net/upload/base/1471395509081_81.jpg', 'http://lib.csdn.net/base/go', '2', '1488334690121');
INSERT INTO `it_lib` VALUES ('598d6821-1767-452c-a43d-d7d85d18cfe3', 'Rust', 'http://img.knowledge.csdn.net/upload/base/1452498182335_335.jpg', 'http://img.knowledge.csdn.net/upload/base/1452498182501_501.jpg', 'http://lib.csdn.net/base/rust', '2', '1488334691396');
INSERT INTO `it_lib` VALUES ('5ccd896f-3f49-4c62-8fa5-1b4bfdef4cf9', 'Git', 'http://img.knowledge.csdn.net/upload/base/1460535308634_634.jpg', 'http://img.knowledge.csdn.net/upload/base/1460535308967_967.jpg', 'http://lib.csdn.net/base/git', '2', '1488334691188');
INSERT INTO `it_lib` VALUES ('5d0b3c33-36cf-4deb-af37-9640ace2ca0b', '人工智能', 'http://img.knowledge.csdn.net/upload/base/1479972981201_201.jpg', 'http://img.knowledge.csdn.net/upload/base/1479972981646_646.jpg', 'http://lib.csdn.net/base/ai', '2', '1488334688887');
INSERT INTO `it_lib` VALUES ('65cbda47-5d6c-4cd8-9cd5-204b8125c8b0', '区块链', 'http://img.knowledge.csdn.net/upload/base/1476175342721_721.jpg', 'http://img.knowledge.csdn.net/upload/base/1476175342919_919.jpg', 'http://lib.csdn.net/base/blockchain', '2', '1488334690042');
INSERT INTO `it_lib` VALUES ('6b76dbfd-e7c3-4a1b-91b7-dde2fde6d343', '算法与数据结构', 'http://img.knowledge.csdn.net/upload/base/1461035533512_512.jpg', 'http://img.knowledge.csdn.net/upload/base/1461035533624_624.jpg', 'http://lib.csdn.net/base/datastructure', '2', '1488334691170');
INSERT INTO `it_lib` VALUES ('77386bb9-3d81-4203-820b-2a60238f88f8', '信息无障碍', 'http://img.knowledge.csdn.net/upload/base/1463378338830_830.jpg', 'http://img.knowledge.csdn.net/upload/base/1463378339629_629.jpg', 'http://lib.csdn.net/base/accessibility', '2', '1488334690784');
INSERT INTO `it_lib` VALUES ('7c064abc-2ac5-4ec1-b788-f1375c9e9418', '微信开发', 'http://img.knowledge.csdn.net/upload/base/1452500582376_376.jpg', 'http://img.knowledge.csdn.net/upload/base/1452500582505_505.jpg', 'http://lib.csdn.net/base/wechat', '2', '1488334688825');
INSERT INTO `it_lib` VALUES ('839bf106-0213-4b7b-9bbe-8d594b6409eb', 'OpenStack', 'http://img.knowledge.csdn.net/upload/base/1481875458834_834.jpg', 'http://img.knowledge.csdn.net/upload/base/1481875459286_286.jpg', 'http://lib.csdn.net/base/openstack', '2', '1488334691344');
INSERT INTO `it_lib` VALUES ('857f1323-b62e-49b1-b99d-1cf948da1a74', 'C语言', 'http://img.knowledge.csdn.net/upload/base/1466132253931_931.jpg', 'http://img.knowledge.csdn.net/upload/base/1466132254151_151.jpg', 'http://lib.csdn.net/base/c', '2', '1488334690710');
INSERT INTO `it_lib` VALUES ('8e946e7b-4335-44a5-9bc7-d28ea237617e', 'Objective-C', 'http://img.knowledge.csdn.net/upload/base/1477273962202_202.jpg', 'http://img.knowledge.csdn.net/upload/base/1477273962706_706.jpg', 'http://lib.csdn.net/base/objective-c', '2', '1488334690059');
INSERT INTO `it_lib` VALUES ('901c71da-4239-4066-b826-b020e07df140', '大型网站架构', 'http://img.knowledge.csdn.net/upload/base/1458091865915_915.jpg', 'http://img.knowledge.csdn.net/upload/base/1458091866269_269.jpg', 'http://lib.csdn.net/base/architecture', '2', '1488334691201');
INSERT INTO `it_lib` VALUES ('a4d7db53-1234-41b9-beee-4e2bb347743c', 'Scala', 'http://img.knowledge.csdn.net/upload/base/1469086540236_236.jpg', 'http://img.knowledge.csdn.net/upload/base/1469086540456_456.jpg', 'http://lib.csdn.net/base/scala', '2', '1488334690452');
INSERT INTO `it_lib` VALUES ('a81ecc7b-65dc-4ded-8dca-6578dedca60e', 'CSS3', 'http://img.knowledge.csdn.net/upload/base/1478154582437_437.jpg', 'http://img.knowledge.csdn.net/upload/base/1478154582895_895.jpg', 'http://lib.csdn.net/base/css3', '2', '1488334688873');
INSERT INTO `it_lib` VALUES ('b7bcdd47-8eac-4cbb-8cfd-c82ab8eadc60', 'MySQL', 'http://img.knowledge.csdn.net/upload/base/1454051093684_684.jpg', 'http://img.knowledge.csdn.net/upload/base/1454051093847_847.jpg', 'http://lib.csdn.net/base/mysql', '2', '1488334688932');
INSERT INTO `it_lib` VALUES ('b8b927ce-464b-4253-ba57-f99a590fa436', '计算机网络', 'http://img.knowledge.csdn.net/upload/base/1463023002983_983.jpg', 'http://img.knowledge.csdn.net/upload/base/1463023003630_630.jpg', 'http://lib.csdn.net/base/computernetworks', '2', '1488334691101');
INSERT INTO `it_lib` VALUES ('c1de90c8-78a5-449f-9da1-bc18a94afcb7', 'HTML5', 'http://img.knowledge.csdn.net/upload/base/1467787369452_452.jpg', 'http://img.knowledge.csdn.net/upload/base/1467787369692_692.jpg', 'http://lib.csdn.net/base/html5', '2', '1488334690464');
INSERT INTO `it_lib` VALUES ('c6e43062-b05b-4867-bdac-9f95c417decd', '操作系统', 'http://img.knowledge.csdn.net/upload/base/1464938192555_555.jpg', 'http://img.knowledge.csdn.net/upload/base/1464938192789_789.jpg', 'http://lib.csdn.net/base/operatingsystem', '2', '1488334690736');
INSERT INTO `it_lib` VALUES ('c70eb17c-3db0-4aaf-83f2-ee220d21f02e', '直播技术', 'http://img.knowledge.csdn.net/upload/base/1472614759773_773.jpg', 'http://img.knowledge.csdn.net/upload/base/1472263291115_115.jpg', 'http://lib.csdn.net/base/liveplay', '2', '1488334690096');
INSERT INTO `it_lib` VALUES ('cbb8f525-6c71-41c8-80f4-4a2302b742af', 'jQuery', 'http://img.knowledge.csdn.net/upload/base/1459143634568_568.jpg', 'http://img.knowledge.csdn.net/upload/base/1459143634893_893.jpg', 'http://lib.csdn.net/base/jquery', '2', '1488334689788');
INSERT INTO `it_lib` VALUES ('cd37b64d-d9b9-491f-839f-cb49b621a5e3', '微服务', 'http://img.knowledge.csdn.net/upload/base/1480906262404_404.jpg', 'http://img.knowledge.csdn.net/upload/base/1480906262550_550.jpg', 'http://lib.csdn.net/base/microservice', '2', '1488334689746');
INSERT INTO `it_lib` VALUES ('cd41113e-fd38-473b-bb55-3457a17d30b5', 'PHP', 'http://img.knowledge.csdn.net/upload/base/1463455906707_707.jpg', 'http://img.knowledge.csdn.net/upload/base/1463455907301_301.jpg', 'http://lib.csdn.net/base/php', '2', '1488334689701');
INSERT INTO `it_lib` VALUES ('d189831c-3313-4e57-9a22-177f69b5d22b', 'Swift', 'http://img.knowledge.csdn.net/upload/base/1452496101906_906.jpg', 'http://img.knowledge.csdn.net/upload/base/1452496102061_61.jpg', 'http://lib.csdn.net/base/swift', '2', '1488334690390');
INSERT INTO `it_lib` VALUES ('d196ad2a-5ec2-4bb0-973d-9884d1ab7fdb', 'Hive', 'http://img.knowledge.csdn.net/upload/base/1476779840210_210.jpg', 'http://img.knowledge.csdn.net/upload/base/1476779841137_137.jpg', 'http://lib.csdn.net/base/hive', '2', '1488334690071');
INSERT INTO `it_lib` VALUES ('d8ca5611-766d-45aa-ad7e-26d183bc823e', 'Hbase', 'http://img.knowledge.csdn.net/upload/base/1479363734443_443.jpg', 'http://img.knowledge.csdn.net/upload/base/1479363734557_557.jpg', 'http://lib.csdn.net/base/hbase', '2', '1488334688898');
INSERT INTO `it_lib` VALUES ('dd518b8f-9a1d-4c2c-9b85-1e6ee9b5d061', 'React Native', 'http://img.knowledge.csdn.net/upload/base/1473751990490_490.jpg', 'http://img.knowledge.csdn.net/upload/base/1473663968868_868.jpg', 'http://lib.csdn.net/base/reactnative', '2', '1488334688850');
INSERT INTO `it_lib` VALUES ('eb1ab750-430e-43e5-a81c-6dabeea13884', '软件测试', 'http://img.knowledge.csdn.net/upload/base/1467193268346_346.jpg', 'http://img.knowledge.csdn.net/upload/base/1467193268437_437.jpg', 'http://lib.csdn.net/base/softwaretest', '2', '1488334690684');
INSERT INTO `it_lib` VALUES ('ee322fab-d002-43d4-88ad-dedd8b8abf6f', 'Node.js', 'http://img.knowledge.csdn.net/upload/base/1461727345705_705.jpg', 'http://img.knowledge.csdn.net/upload/base/1461727346291_291.jpg', 'http://lib.csdn.net/base/nodejs', '2', '1488334691154');
INSERT INTO `it_lib` VALUES ('f057fc98-a366-4387-8c09-6a872837b658', '嵌入式开发', 'http://img.knowledge.csdn.net/upload/base/1464164053876_876.jpg', 'http://img.knowledge.csdn.net/upload/base/1464164054490_490.jpg', 'http://lib.csdn.net/base/embeddeddevelopment', '2', '1488334690759');
INSERT INTO `it_lib` VALUES ('f458bd9c-8db3-4826-b5b9-c6d53129913a', 'JavaScript', 'http://img.knowledge.csdn.net/upload/base/1458106113412_412.jpg', 'http://img.knowledge.csdn.net/upload/base/1458106113531_531.jpg', 'http://lib.csdn.net/base/javascript', '2', '1488334689689');
INSERT INTO `it_lib` VALUES ('f5c7aacf-71a3-43b1-baf4-40cea8013fe3', 'React', 'http://img.knowledge.csdn.net/upload/base/1465887837340_340.jpg', 'http://img.knowledge.csdn.net/upload/base/1465887837577_577.jpg', 'http://lib.csdn.net/base/react', '2', '1488334690722');
INSERT INTO `it_lib` VALUES ('fac6b9c8-f10c-4d38-871e-a76d21fafb1b', 'Bluemix', 'http://img.knowledge.csdn.net/upload/base/1463125918859_859.jpg', 'http://img.knowledge.csdn.net/upload/base/1463125919684_684.jpg', 'http://lib.csdn.net/base/bluemix', '2', '1488334691130');
INSERT INTO `it_lib` VALUES ('ff39b567-55cd-4a46-8e5a-7121f43184c2', 'Java EE', 'http://img.knowledge.csdn.net/upload/base/1456818035722_722.jpg', 'http://img.knowledge.csdn.net/upload/base/1456818036186_186.jpg', 'http://lib.csdn.net/base/javaee', '2', '1488334689655');
INSERT INTO `it_lib` VALUES ('fff3821c-25bb-48b6-9aee-4a9d5ecd9942', 'Redis', 'http://img.knowledge.csdn.net/upload/base/1464683375884_884.jpg', 'http://img.knowledge.csdn.net/upload/base/1464683376164_164.jpg', 'http://lib.csdn.net/base/redis', '2', '1488334690747');
