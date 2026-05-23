package com.suiye.studentserver.entity;

import lombok.Data;

@Data
public class ScoreVO {
    private Integer id;
    private Integer studentId;
    private String studentName;
    private String studentNumber;
    private String major;
    private Integer courseId;
    private String courseName;
    private String courseNumber;
    private Integer credit;
    private Double score;
}
