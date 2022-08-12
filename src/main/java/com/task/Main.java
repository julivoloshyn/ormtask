package com.task;

import lombok.SneakyThrows;

import java.io.File;
import java.net.URL;
import java.util.List;


public class Main {

    @SneakyThrows
    public static void main(String[] args){

        URL url = Main.class.getClassLoader().getResource("sample.json");
        List<Person> result = new ORM().transform(new File(url.toURI()), Person.class);

    }


}
