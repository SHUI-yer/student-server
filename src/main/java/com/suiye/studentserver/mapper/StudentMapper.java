package com.suiye.studentserver.mapper;

import com.suiye.studentserver.entity.Student;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface StudentMapper {

    @Select("SELECT * FROM student WHERE name LIKE CONCAT('%', #{keyword}, '%') OR student_number LIKE CONCAT('%', #{keyword}, '%')")
    List<Student> search(String keyword);

    @Select("SELECT * FROM student")
    List<Student> findAll();

    @Select("SELECT * FROM student ORDER BY id DESC LIMIT #{offset}, #{limit}")
    List<Student> findByPage(@Param("offset") int offset, @Param("limit") int limit);

    @Select("SELECT COUNT(*) FROM student")
    int count();

    @Insert("INSERT INTO student(name, student_number, gender, age, major, class_name) " +
            "VALUES(#{name}, #{studentNumber}, #{gender}, #{age}, #{major}, #{className})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Student student);

    @Update("UPDATE student SET name=#{name}, student_number=#{studentNumber}, gender=#{gender}, " +
            "age=#{age}, major=#{major}, class_name=#{className} WHERE id=#{id}")
    int update(Student student);

    @Delete("DELETE FROM student WHERE id=#{id}")
    int delete(Integer id);

    @Select("SELECT * FROM student WHERE id=#{id}")
    Student findById(Integer id);
}
