package com.suiye.studentserver.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Course {
    private Integer id;
    private String courseNumber;
    private String name;
    private Integer credit;
    private String teacher;
    private String semester;
}
