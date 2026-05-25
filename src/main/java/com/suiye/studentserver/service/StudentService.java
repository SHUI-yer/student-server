package com.suiye.studentserver.service;

import com.suiye.studentserver.entity.Student;
import java.util.List;
import java.util.Map;

public interface StudentService {
    Map<String, Object> getStudentPage(int pageNum, int pageSize, String keyword);
    int saveStudent(Student student);
    int deleteStudent(Integer id);
    List<Student> getAllStudents();
    List<Student> getFilteredStudents(String major, String className);
    Student getStudentById(Integer id);
    void batchSave(List<Student> students);
}
