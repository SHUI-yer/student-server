package com.suiye.studentserver.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Score {
    private Integer id;
    private Integer studentId;
    private Integer courseId;
    private Double score; // 最终学分成绩 (百分制成绩 * 学分 / 100)
    private Double originalScore; // 原始百分制成绩 (0-100)
}
