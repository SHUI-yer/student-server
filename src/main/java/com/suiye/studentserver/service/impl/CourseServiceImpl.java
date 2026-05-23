package com.suiye.studentserver.service.impl;

import com.suiye.studentserver.entity.Course;
import com.suiye.studentserver.mapper.CourseMapper;
import com.suiye.studentserver.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseMapper courseMapper;

    @Override
    public Map<String, Object> getCoursePage(int pageNum, int pageSize, String keyword) {
        int offset = (pageNum - 1) * pageSize;
        List<Course> list;
        int total;

        if (keyword != null && !keyword.isEmpty()) {
            list = courseMapper.search(keyword);
            total = list.size();
        } else {
            list = courseMapper.findByPage(offset, pageSize);
            total = courseMapper.count();
        }

        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("total", total);
        return result;
    }

    @Override
    public int saveCourse(Course course) {
        if (course.getId() == null) {
            return courseMapper.insert(course);
        } else {
            return courseMapper.update(course);
        }
    }

    @Override
    public int deleteCourse(Integer id) {
        return courseMapper.delete(id);
    }

    @Override
    public List<Course> getAllCourses() {
        return courseMapper.findAll();
    }
}
