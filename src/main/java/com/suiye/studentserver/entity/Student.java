package com.suiye.studentserver.entity;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student {
    private Integer id;
    private String name;
    private String studentNumber;
    private String gender;
    private Integer age;
    private String major;
    private String className;
}
