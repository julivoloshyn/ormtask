package com.task.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@TableAnnotation("person")
public class Person {

    private String name;
    private int age;
    private int salary;
    private String position;
    private LocalDate dateOfBirth;
    private float xxx;
}
