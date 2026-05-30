package com.suiye.studentserver.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import com.suiye.studentserver.common.Result;
import com.suiye.studentserver.entity.Student;
import com.suiye.studentserver.entity.Course;
import com.suiye.studentserver.entity.ScoreImportDTO;
import com.suiye.studentserver.entity.ScoreVO;
import com.suiye.studentserver.service.CourseService;
import com.suiye.studentserver.service.ScoreService;
import com.suiye.studentserver.service.StudentService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/excel")
@CrossOrigin
public class ExcelController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private ScoreService scoreService;

    @Autowired
    private CourseService courseService;

    /**
     * 导出学生数据为 Excel (支持筛选)
     */
    @GetMapping("/export/student")
    public void exportStudentExcel(
            @RequestParam(required = false) String major,
            @RequestParam(required = false) String className,
            HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("学生档案数据", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

        List<Student> list = studentService.getFilteredStudents(major, className);
        EasyExcel.write(response.getOutputStream(), Student.class).sheet("学生信息").doWrite(list);
    }

    /**
     * 导出成绩数据为 Excel (支持筛选)
     */
    @GetMapping("/export/score")
    public void exportScoreExcel(
            @RequestParam(required = false) String studentNumber,
            @RequestParam(required = false) String courseNumber,
            @RequestParam(required = false) String major,
            HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("学生成绩数据", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

        List<ScoreVO> list = scoreService.getFilteredScores(studentNumber, courseNumber, major);
        EasyExcel.write(response.getOutputStream(), ScoreVO.class).sheet("成绩信息").doWrite(list);
    }

    /**
     * 导出课程数据为 Excel (支持筛选)
     */
    @GetMapping("/export/course")
    public void exportCourseExcel(
            @RequestParam(required = false) String semester,
            @RequestParam(required = false) String teacher,
            HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("课程信息数据", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

        List<Course> list = courseService.getFilteredCourses(semester, teacher);
        EasyExcel.write(response.getOutputStream(), Course.class).sheet("课程信息").doWrite(list);
    }

    /**
     * 导入学生 Excel 数据
     */
    @PostMapping("/import/student")
    public Result<String> importStudentExcel(@RequestParam("file") MultipartFile file) throws IOException {
        EasyExcel.read(file.getInputStream(), Student.class, new PageReadListener<Student>(dataList -> {
            studentService.batchSave(dataList);
        })).sheet().doRead();
        return Result.success("学生数据导入成功");
    }

    /**
     * 导入课程 Excel 数据
     */
    @PostMapping("/import/course")
    public Result<String> importCourseExcel(@RequestParam("file") MultipartFile file) throws IOException {
        EasyExcel.read(file.getInputStream(), Course.class, new PageReadListener<Course>(dataList -> {
            courseService.batchSave(dataList);
        })).sheet().doRead();
        return Result.success("课程数据导入成功");
    }

    /**
     * 导入成绩 Excel 数据
     */
    @PostMapping("/import/score")
    public Result<String> importScoreExcel(@RequestParam("file") MultipartFile file) throws IOException {
        EasyExcel.read(file.getInputStream(), ScoreImportDTO.class, new PageReadListener<ScoreImportDTO>(dataList -> {
            scoreService.batchImport(dataList);
        })).sheet().doRead();
        return Result.success("成绩数据导入成功");
    }

    /**
     * 下载学生导入模板
     */
    @GetMapping("/template/student")
    public void downloadStudentTemplate(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("学生信息导入模板", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

        List<Student> templateData = Arrays.asList(
            new Student(null, "张三", "2024001", "男", 20, "计算机科学", "2401班", null)
        );
        EasyExcel.write(response.getOutputStream(), Student.class).sheet("学生信息").doWrite(templateData);
    }

    /**
     * 下载课程导入模板
     */
    @GetMapping("/template/course")
    public void downloadCourseTemplate(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("课程信息导入模板", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

        List<Course> templateData = Arrays.asList(
            new Course(null, "CS101", "高等数学", 5, "张老师", "2023秋")
        );
        EasyExcel.write(response.getOutputStream(), Course.class).sheet("课程信息").doWrite(templateData);
    }

    /**
     * 下载成绩导入模板
     */
    @GetMapping("/template/score")
    public void downloadScoreTemplate(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("成绩录入导入模板", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

        List<ScoreImportDTO> templateData = Arrays.asList(
            new ScoreImportDTO("2024001", "CS101", 85.5)
        );
        EasyExcel.write(response.getOutputStream(), ScoreImportDTO.class).sheet("成绩录入").doWrite(templateData);
    }
}
