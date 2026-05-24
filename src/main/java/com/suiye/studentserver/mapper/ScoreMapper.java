package com.suiye.studentserver.mapper;

import com.suiye.studentserver.entity.Score;
import com.suiye.studentserver.entity.ScoreVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ScoreMapper {

    @Select("<script>" +
            "SELECT s.id, s.student_id as studentId, s.course_id as courseId, s.score, s.original_score as originalScore, " +
            "st.name as studentName, st.student_number as studentNumber, st.major as major, " +
            "c.name as courseName, c.course_number as courseNumber, c.credit as credit " +
            "FROM score s " +
            "LEFT JOIN student st ON s.student_id = st.id " +
            "LEFT JOIN course c ON s.course_id = c.id " +
            "<where> " +
            "<if test='keyword != null and keyword != \"\"'> " +
            "AND (st.name LIKE CONCAT('%', #{keyword}, '%') OR c.name LIKE CONCAT('%', #{keyword}, '%') OR st.student_number LIKE CONCAT('%', #{keyword}, '%')) " +
            "</if> " +
            "</where> " +
            "ORDER BY s.id DESC " +
            "LIMIT #{offset}, #{limit}" +
            "</script>")
    List<ScoreVO> findByPage(@Param("offset") int offset, @Param("limit") int limit, @Param("keyword") String keyword);

    @Select("<script>" +
            "SELECT COUNT(*) " +
            "FROM score s " +
            "LEFT JOIN student st ON s.student_id = st.id " +
            "LEFT JOIN course c ON s.course_id = c.id " +
            "<where> " +
            "<if test='keyword != null and keyword != \"\"'> " +
            "AND (st.name LIKE CONCAT('%', #{keyword}, '%') OR c.name LIKE CONCAT('%', #{keyword}, '%') OR st.student_number LIKE CONCAT('%', #{keyword}, '%')) " +
            "</if> " +
            "</where>" +
            "</script>")
    int count(@Param("keyword") String keyword);

    @Insert("INSERT INTO score(student_id, course_id, score, original_score) VALUES(#{studentId}, #{courseId}, #{score}, #{originalScore})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Score score);

    @Update("UPDATE score SET student_id=#{studentId}, course_id=#{courseId}, score=#{score}, original_score=#{originalScore} WHERE id=#{id}")
    int update(Score score);

    @Delete("DELETE FROM score WHERE id=#{id}")
    int delete(Integer id);
    
    @Select("SELECT * FROM score WHERE student_id=#{studentId} AND course_id=#{courseId}")
    Score findByStudentAndCourse(@Param("studentId") Integer studentId, @Param("courseId") Integer courseId);

    @Select("SELECT s.id, s.student_id as studentId, s.course_id as courseId, s.score, s.original_score as originalScore, " +
            "st.name as studentName, st.student_number as studentNumber, st.major as major, " +
            "c.name as courseName, c.course_number as courseNumber, c.credit as credit " +
            "FROM score s " +
            "LEFT JOIN student st ON s.student_id = st.id " +
            "LEFT JOIN course c ON s.course_id = c.id")
    List<ScoreVO> findAll();
}