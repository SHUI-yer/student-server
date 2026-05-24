package com.suiye.studentserver.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用于 Excel 导入的成绩实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScoreImportDTO {
    @ExcelProperty("学号")
    private String studentNumber;

    @ExcelProperty("课程编号")
    private String courseNumber;

    @ExcelProperty("百分制成绩")
    private Double originalScore;
}
