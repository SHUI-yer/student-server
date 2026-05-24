package com.suiye.studentserver.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class ScoreVO {
    @ExcelProperty("ID")
    private Integer id;

    @ExcelProperty("学生姓名")
    private String studentName;

    @ExcelProperty("学号")
    private String studentNumber;

    @ExcelProperty("专业")
    private String major;

    @ExcelProperty("课程名称")
    private String courseName;

    @ExcelProperty("课程编号")
    private String courseNumber;

    @ExcelProperty("学分")
    private Integer credit;

    @ExcelProperty("成绩")
    private Double score;

    @ExcelProperty("原始成绩")
    private Double originalScore;

    private Integer studentId;
    private Integer courseId;
}
