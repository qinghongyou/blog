/*
Navicat MySQL Data Transfer

Source Server         : test
Source Server Version : 50716
Source Host           : localhost:3306
Source Database       : db_blog

Target Server Type    : MYSQL
Target Server Version : 50716
File Encoding         : 65001

Date: 2017-02-17 19:13:13
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for `tb_articletype`
-- ----------------------------
DROP TABLE IF EXISTS `tb_articletype`;
CREATE TABLE `tb_articletype` (
  `articleType_id` smallint(6) NOT NULL AUTO_INCREMENT,
  `articleType_name` varchar(20) DEFAULT NULL,
  `articleType_info` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`articleType_id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_articletype
-- ----------------------------
INSERT INTO `tb_articletype` VALUES ('1', '个人随想', '自己的感悟与想法');
INSERT INTO `tb_articletype` VALUES ('2', '个人日记', '记录自己每一天的事情');
INSERT INTO `tb_articletype` VALUES ('3', '人生感悟', '对事物的感觉与 悟性');
INSERT INTO `tb_articletype` VALUES ('4', '文章推荐', '来自其它地方的文章');

-- ----------------------------
-- Table structure for `tb_article`
-- ----------------------------
DROP TABLE IF EXISTS `tb_article`;
CREATE TABLE `tb_article` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `article_typeID` smallint(6) DEFAULT NULL,
  `article_title` varchar(40) DEFAULT NULL,
  `article_content` varchar(8000) DEFAULT NULL,
  `article_sdTime` varchar(30) DEFAULT NULL,
  `article_create` varchar(10) DEFAULT NULL,
  `article_info` varchar(500) DEFAULT NULL,
  `article_count` int(10) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `article_typeID` (`article_typeID`),
  CONSTRAINT `tb_article_ibfk_1` FOREIGN KEY (`article_typeID`) REFERENCES `tb_articletype` (`articleType_id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_article
-- ----------------------------
INSERT INTO `tb_article` VALUES ('1', '1', '面具', '有时候看到有些人带着面具活着，撒一些自认为无懈可击的慌，扯一些酸不溜湫的淡，为他们感到累。但是，不带着面具就一定好吗？连个巴掌都挨得那么真真切切。', '2017年02月17日 14:58:46', '原创', '人与面具', '0');
INSERT INTO `tb_article` VALUES ('2', '1', '礼貌', '可以忍受陌生人对自己扯皮、耍无赖，甚至更深的...但是对于亲近的人，总是很难忍受那些。甚至一个最简单的\"不\"字也总会在心中激起一层又一层的涟漪。现在明白以前为啥我妈老是因为我我认为的一点小事情而抹眼睛。所以，不要轻易跟自己身边发脾气，就算是不得不说\"不\"的时候，也请说的委婉些。', '2017年02月17日 15:00:09', '摘自', '为人处世', '2');
INSERT INTO `tb_article` VALUES ('3', '2', '社会实践', '今天第一次去社会实践，有点兴奋。回想在学校的日子，作为一名专业知识不深的大学生来说，进行了这次实习活动，让我们从实践中对这门自己即将从事的专业获得一个感性认识，为今后专业课的学习打下坚实的基础。<br>　　实践是大学生生活的第二课堂,是知识常新和发展的源泉,是检验真理的试金石,也是大学生锻炼成长的有效途径。一个人的知识和能力只有在实践中才能发挥作用,才能得到丰富、完善和发展。大学生成长,就要勤于实践,将所学的理论知识与实践相结合一起,在实践中继续学习,不断总结,逐步完善,有所创新,并在实践中提高自己由知识、能力、智慧等因素融合成的综合素质和能力,为自己事业的成功打下良好的基础。', '2017年02月17日 15:01:25', '原创', '校外社会实践', '0');
INSERT INTO `tb_article` VALUES ('4', '2', '晚风', '我愿借一股晚风，踏上几颗繁星，与你遨游万里夜空。<br>你说<br>前世多少万年的轮回，今生才能与你一见<br>你说<br>前世多少千年的福报，今生才能携你之手<br>相遇且短，却足以三生三世的回恋<br>在陌上花开之际<br>思念是否随花绽放<br>在木棉花开之际<br>每一片的花瓣上是否还会有你的容颜<br>我依然在那里站着<br>与风、与雨，是否还会与你', '2017年02月17日 15:03:03', '摘自', '诗歌', '3');
INSERT INTO `tb_article` VALUES ('5', '3', '态度决定一切', '工作中，只要每个人把握好自己的位置，拥有一个良好的心态是很重要的。前国家男子足球的教练，米卢同志不是也说过一句让人铭记的话：“态度决定一切”!是啊!拥有一个良好的心态，拥有一个美好的心情，这样工作起来不仅能给你带来快乐，也会给你一种成就感。', '2017年02月17日 15:04:00', '原创', '态度', '2');
INSERT INTO `tb_article` VALUES ('6', '3', '浅谈教育', '无论何时，对自我的教育，要不吝钱财。很多人打小见到父母对自己的教育不吝钱财，自己有了子女后，对自己的子女不吝钱财，却恰恰忘了，对自己的教&nbsp;育，也应该不吝钱财。实际上，从自己开始叛逆，能独立思考开始，或者离开父母，自己独自生存开始，我们也就开始了自己对自己教育的过程。如果这个过程中自&nbsp;己不愿意付出心血，又不愿意在自我教育上投入钱财，那么，发展不好，关键时候，才疏学浅，无力胜任新工作，也就不应该有怨言。', '2017年02月17日 15:04:53', '摘自', '教育', '0');
INSERT INTO `tb_article` VALUES ('7', '4', '思愁', '孤寂，从这里穿过那头。<br>　　<br>　　不知从何说起，淡淡的忧愁在心中埋下芽种。<br>　　<br>　　当所有思绪集中于万千，不曾不感到一丝丝忧伤。<br>　　<br>　　曾经拥有过最灿烂的烟火。也曾错拥包装的礼物。<br>　　<br>　　阳光灿烂下，人生是耀眼明亮闪耀光芒。<br>　　<br>　　阴森雾霾下，人生是漆黑一片魅影狼藉。<br>　　<br>　　生活的艰辛，一步一个脚印中缓缓前行。<br>　　<br>　　酸楚甜辣是天然的润滑剂。<br>　　<br>　　曾经，有多少个曾经。<br>　　<br>　　在梦里，梦见你，梦见你在这独自等待着这绽放的火苗。', '2017年02月17日 15:05:47', '原创', '抒情诗', '2');
INSERT INTO `tb_article` VALUES ('8', '4', '牛奶的味道', '前几天在食堂吃饭，我看见坐在旁边桌子的一个小男孩把牛奶倒在瓶盖里喝，喝完又把瓶盖舔一遍，这熟悉的情景让我不由得想起曾经的自己。<br>　　<br>　　小时候，我家经济条件不是很好，所以我从不吃零食，牛奶也很少喝，可以称得上是“十八年不知牛奶味”。<br>　　<br>　　记得有一天晚上，爸爸从工厂下班回来，摩托车篮里放了一个装得满满的牛奶瓶，我以为是牛奶，就拿起瓶子往嘴里倒，但我的嘴刚碰到瓶口就有苦苦的味道，我仔细一看，原来瓶子里装的是机油。还好我当时没有咽下去，要不然就得去医院了。<br>　　<br>　　能让我“大饱口福”的是夏天。因为天气热，爸爸干活的工厂会给工人每天发一瓶饮料，爸爸每次都把饮料带回家给我和弟弟还有姐姐喝。我们三个看见这一小瓶饮料就跟见到宝贝似得，高兴地不得了。我们把饮料倒在瓶盖里，一人咋一口，有时会拿来三个碗，每个碗里倒一点，然后像喝酒一样慢慢品味，只有几百毫升的饮料足够我们三个喝一晚上。有一次妈妈买了三瓶花生牛奶，我们三个一人一瓶抱在手心，一会儿喝一瓶盖，本打算分两三天喝完，没想到第二天剩下的牛奶就全酸了，当时妈妈气得打了我们一顿。<br>　　<br>　　我和弟弟上高中的时候，因为学习压力大，需要补充营养，而牛奶又太贵，每天一袋豆奶粉就成了最好的替代品，所以我记忆中牛奶的味道更接近豆奶粉。<br>　　<br>　　来到军校后，学习和训练任务都很重，不吃好喝好是不行的，每天一瓶牛奶是我的习惯。特别是天冷的时候，手中捧着一瓶热乎乎的牛奶，那沁人心脾的香味从鼻腔溜到心房，仿佛又把我带回到童年。', '2017年02月17日 15:07:25', '摘自', '回忆往事', '1');

-- ----------------------------
-- Table structure for `tb_friend`
-- ----------------------------
DROP TABLE IF EXISTS `tb_friend`;
CREATE TABLE `tb_friend` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `friend_name` varchar(20) DEFAULT NULL,
  `friend_sex` varchar(2) DEFAULT NULL,
  `friend_OICQ` varchar(20) DEFAULT NULL,
  `friend_blog` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_friend
-- ----------------------------
INSERT INTO `tb_friend` VALUES ('1', '小刘', '男', '1917845669', 'xiaoliu');
INSERT INTO `tb_friend` VALUES ('2', '小七', '女', '2657894569', 'xiaoqi');

-- ----------------------------
-- Table structure for `tb_master`
-- ----------------------------
DROP TABLE IF EXISTS `tb_master`;
CREATE TABLE `tb_master` (
  `master_name` varchar(50) NOT NULL,
  `master_password` varchar(10) DEFAULT NULL,
  `master_sex` varchar(2) DEFAULT NULL,
  `master_oicq` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`master_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_master
-- ----------------------------
INSERT INTO `tb_master` VALUES ('xiaoyou', '123456', '男', '1917958999');

-- ----------------------------
-- Table structure for `tb_photo`
-- ----------------------------
DROP TABLE IF EXISTS `tb_photo`;
CREATE TABLE `tb_photo` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `photo_addr` varchar(500) DEFAULT NULL,
  `photo_sdTime` varchar(30) DEFAULT NULL,
  `photo_desc` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8;



-- ----------------------------
-- Table structure for `tb_review`
-- ----------------------------
DROP TABLE IF EXISTS `tb_review`;
CREATE TABLE `tb_review` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `review_articleId` int(10) DEFAULT NULL,
  `review_author` varchar(50) DEFAULT NULL,
  `review_content` varchar(2000) DEFAULT NULL,
  `review_sdTime` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_review
-- ----------------------------
INSERT INTO `tb_review` VALUES ('1', '11', '愿青春静好', '写的很好，必须点赞！', '2017年02月17日 15:13:16');
INSERT INTO `tb_review` VALUES ('2', '14', '阿雪', '态度决定一切！', '2017年02月17日 15:14:02');
INSERT INTO `tb_review` VALUES ('3', '4', '小游', '你说\r\n前世多少万年的轮回，今生才能与你一见\r\n你说\r\n前世多少千年的福报，今生才能携你之手', '2017年02月17日 15:36:21');

-- ----------------------------
-- Table structure for `tb_word`
-- ----------------------------
DROP TABLE IF EXISTS `tb_word`;
CREATE TABLE `tb_word` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `word_title` varchar(50) DEFAULT NULL,
  `word_content` varchar(2000) DEFAULT NULL,
  `word_sdTime` varchar(30) DEFAULT NULL,
  `word_author` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_word
-- ----------------------------
INSERT INTO `tb_word` VALUES ('1', '祝福', '新年快乐！开开心心！', '2017年02月17日 15:14:54', '青青子衿');
INSERT INTO `tb_word` VALUES ('2', '学习', '你能教我学习java么？', '2017年02月17日 15:15:49', '扬子江');
