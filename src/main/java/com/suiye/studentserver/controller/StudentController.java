package com.suiye.studentserver.controller;

import com.suiye.studentserver.common.Result;
import com.suiye.studentserver.entity.Student;
import com.suiye.studentserver.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/student")
@CrossOrigin
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/page")
    public Result<Map<String, Object>> getPage(@RequestParam(defaultValue = "1") int pageNum,
                                               @RequestParam(defaultValue = "10") int pageSize,
                                               @RequestParam(required = false) String keyword) {
        Map<String, Object> data = studentService.getStudentPage(pageNum, pageSize, keyword);
        return Result.success(data);
    }

    @PostMapping("/save")
    public Result<Void> save(@RequestBody @Validated Student student) {
        studentService.saveStudent(student);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Integer id) {
        studentService.deleteStudent(id);
        return Result.success();
    }

    @GetMapping("/all")
    public Result<java.util.List<Student>> getAll() {
        return Result.success(studentService.getAllStudents());
    }
}
