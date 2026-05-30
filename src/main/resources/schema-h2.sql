-- H2 Database Schema (MySQL Compatible Mode)

CREATE TABLE IF NOT EXISTS student (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(50) NOT NULL,
  student_number VARCHAR(50) NOT NULL,
  gender VARCHAR(10) DEFAULT '男',
  age INT,
  major VARCHAR(100) NOT NULL,
  class_name VARCHAR(50),
  avatar_url VARCHAR(255),
  CONSTRAINT uk_student_number UNIQUE (student_number)
);

CREATE TABLE IF NOT EXISTS course (
  id INT AUTO_INCREMENT PRIMARY KEY,
  course_number VARCHAR(50) NOT NULL,
  name VARCHAR(100) NOT NULL,
  credit INT NOT NULL,
  teacher VARCHAR(50),
  semester VARCHAR(50),
  CONSTRAINT uk_course_number UNIQUE (course_number)
);

CREATE TABLE IF NOT EXISTS score (
  id INT AUTO_INCREMENT PRIMARY KEY,
  student_id INT NOT NULL,
  course_id INT NOT NULL,
  score DOUBLE NOT NULL,
  original_score DOUBLE,
  CONSTRAINT uk_student_course UNIQUE (student_id, course_id),
  CONSTRAINT fk_score_student FOREIGN KEY (student_id) REFERENCES student(id) ON DELETE CASCADE,
  CONSTRAINT fk_score_course FOREIGN KEY (course_id) REFERENCES course(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_student_id ON score(student_id);
CREATE INDEX IF NOT EXISTS idx_course_id ON score(course_id);
