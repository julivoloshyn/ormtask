package com.task.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@TableAnnotation("person2")
public class Person2 {

    private String name;
    private String position;
    private int age;

}
