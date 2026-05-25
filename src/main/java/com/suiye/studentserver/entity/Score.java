package com.suiye.studentserver.entity;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Score {
    private Integer id;

    @NotNull(message = "学生ID不能为空")
    private Integer studentId;

    @NotNull(message = "课程ID不能为空")
    private Integer courseId;

    private Double score; // 最终学分成绩 (百分制成绩 * 学分 / 100)

    @NotNull(message = "原始成绩不能为空")
    @Min(value = 0, message = "成绩不能低于0分")
    @Max(value = 100, message = "成绩不能超过100分")
    private Double originalScore; // 原始百分制成绩 (0-100)
}
