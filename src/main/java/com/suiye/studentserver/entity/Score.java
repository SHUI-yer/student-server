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
    private Double score;
}
