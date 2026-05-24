package com.suiye.studentserver.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Course {
    @ExcelProperty("ID")
    private Integer id;

    @ExcelProperty("课程编号")
    private String courseNumber;

    @ExcelProperty("课程名称")
    private String name;

    @ExcelProperty("学分")
    private Integer credit;

    @ExcelProperty("授课教师")
    private String teacher;

    @ExcelProperty("学期")
    private String semester;
}
