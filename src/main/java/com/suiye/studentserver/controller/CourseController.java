package com.suiye.studentserver.controller;

import com.suiye.studentserver.common.Result;
import com.suiye.studentserver.entity.Course;
import com.suiye.studentserver.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/course")
@CrossOrigin
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping("/page")
    public Result<Map<String, Object>> getPage(@RequestParam(defaultValue = "1") int pageNum,
                                               @RequestParam(defaultValue = "10") int pageSize,
                                               @RequestParam(required = false) String keyword) {
        Map<String, Object> data = courseService.getCoursePage(pageNum, pageSize, keyword);
        return Result.success(data);
    }

    @PostMapping("/save")
    public Result<Void> save(@RequestBody Course course) {
        courseService.saveCourse(course);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Integer id) {
        courseService.deleteCourse(id);
        return Result.success();
    }

    @GetMapping("/all")
    public Result<java.util.List<Course>> getAllCourses() {
        return Result.success(courseService.getAllCourses());
    }
}
