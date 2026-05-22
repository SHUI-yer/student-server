package com.suiye.studentserver.mapper;

import com.suiye.studentserver.entity.Course;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CourseMapper {

    @Select("SELECT * FROM course WHERE name LIKE CONCAT('%', #{keyword}, '%') OR course_number LIKE CONCAT('%', #{keyword}, '%')")
    List<Course> search(String keyword);

    @Select("SELECT * FROM course LIMIT #{offset}, #{limit}")
    List<Course> findByPage(@Param("offset") int offset, @Param("limit") int limit);

    @Select("SELECT COUNT(*) FROM course")
    int count();

    @Insert("INSERT INTO course(course_number, name, credit, teacher, semester) " +
            "VALUES(#{courseNumber}, #{name}, #{credit}, #{teacher}, #{semester})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Course course);

    @Update("UPDATE course SET course_number=#{courseNumber}, name=#{name}, credit=#{credit}, " +
            "teacher=#{teacher}, semester=#{semester} WHERE id=#{id}")
    int update(Course course);

    @Delete("DELETE FROM course WHERE id=#{id}")
    int delete(Integer id);

    @Select("SELECT * FROM course WHERE id=#{id}")
    Course findById(Integer id);
}
