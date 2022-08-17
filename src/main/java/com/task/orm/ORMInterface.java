package com.task.orm;

import com.task.readwritesource.DataReadWriteSource;
import lombok.SneakyThrows;

import java.util.List;

public interface ORMInterface {

    @SneakyThrows
    <T> List<T> readAll(DataReadWriteSource<?> inputSource, Class<T> cls);

}
