-- 1. 创建学生表 (student)
CREATE TABLE IF NOT EXISTS `student` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(50) NOT NULL COMMENT '姓名',
  `student_number` varchar(50) NOT NULL COMMENT '学号',
  `gender` varchar(10) DEFAULT '男' COMMENT '性别',
  `age` int(11) DEFAULT NULL COMMENT '年龄',
  `major` varchar(100) NOT NULL COMMENT '专业',
  `class_name` varchar(50) DEFAULT NULL COMMENT '班级',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_student_number` (`student_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学生信息表';

-- 2. 创建课程表 (course)
CREATE TABLE IF NOT EXISTS `course` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `course_number` varchar(50) NOT NULL COMMENT '课程号',
  `name` varchar(100) NOT NULL COMMENT '课程名称',
  `credit` int(11) NOT NULL COMMENT '学分',
  `teacher` varchar(50) DEFAULT NULL COMMENT '授课教师',
  `semester` varchar(50) DEFAULT NULL COMMENT '开课学期',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_course_number` (`course_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程信息表';

-- 3. 创建成绩表 (score)
CREATE TABLE IF NOT EXISTS `score` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '成绩ID',
  `student_id` int(11) NOT NULL COMMENT '学生ID',
  `course_id` int(11) NOT NULL COMMENT '课程ID',
  `score` double NOT NULL COMMENT '成绩',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_student_course` (`student_id`,`course_id`),
  KEY `idx_student_id` (`student_id`),
  KEY `idx_course_id` (`course_id`),
  CONSTRAINT `fk_score_student` FOREIGN KEY (`student_id`) REFERENCES `student` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_score_course` FOREIGN KEY (`course_id`) REFERENCES `course` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学生成绩表';