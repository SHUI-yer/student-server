package com.suiye.studentserver.service;

import com.suiye.studentserver.entity.Course;
import java.util.Map;

public interface CourseService {
    Map<String, Object> getCoursePage(int pageNum, int pageSize, String keyword);
    int saveCourse(Course course);
    int deleteCourse(Integer id);
    java.util.List<Course> getAllCourses();
}
