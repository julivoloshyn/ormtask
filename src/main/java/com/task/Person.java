package com.task;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
public class Person {

    private String name;
    private int age;
    private float salary;
    private String position;
    private LocalDate dateOfBirth;
}
