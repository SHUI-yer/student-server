package com.suiye.studentserver.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Course {
    @ExcelProperty("ID")
    private Integer id;

    @NotBlank(message = "课程编号不能为空")
    @ExcelProperty("课程编号")
    private String courseNumber;

    @NotBlank(message = "课程名称不能为空")
    @ExcelProperty("课程名称")
    private String name;

    @NotNull(message = "学分不能为空")
    @Min(value = 1, message = "学分最少为1分")
    @Max(value = 10, message = "学分最多为10分")
    @ExcelProperty("学分")
    private Integer credit;

    @NotBlank(message = "授课教师不能为空")
    @ExcelProperty("授课教师")
    private String teacher;

    @NotBlank(message = "学期信息不能为空")
    @ExcelProperty("学期")
    private String semester;
}
