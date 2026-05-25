package com.suiye.studentserver.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student {
    @ExcelProperty("ID")
    private Integer id;

    @NotBlank(message = "姓名不能为空")
    @Size(max = 20, message = "姓名长度不能超过20个字符")
    @ExcelProperty("姓名")
    private String name;

    @NotBlank(message = "学号不能为空")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "学号只能包含字母和数字")
    @ExcelProperty("学号")
    private String studentNumber;

    @NotBlank(message = "性别不能为空")
    @ExcelProperty("性别")
    private String gender;

    @NotNull(message = "年龄不能为空")
    @Min(value = 1, message = "年龄不能小于1岁")
    @Max(value = 120, message = "年龄不能大于120岁")
    @ExcelProperty("年龄")
    private Integer age;

    @NotBlank(message = "专业不能为空")
    @ExcelProperty("专业")
    private String major;

    @NotBlank(message = "班级不能为空")
    @ExcelProperty("班级")
    private String className;

    @ExcelIgnore
    private String avatarUrl;
}
