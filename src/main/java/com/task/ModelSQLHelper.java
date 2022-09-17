package com.task;

import com.task.dto.TableAnnotation;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ModelSQLHelper {
    private final List<String> availableFieldsInDatabase;

    public String buildSQL(Object object){
        Class<? extends Object> cls = object.getClass();

        String tableName = getTableName(cls);
        String fields = getFields(cls);
        String arguments = getArguments(cls);
        return String.format("INSERT INTO %s (%s) VALUES (%s);",
                tableName, fields, arguments);
    }

    @SneakyThrows
    public void bindArguments(Object objectToInsert, PreparedStatement preparedStatement) {
        int index = 1;

        for(Field field : objectToInsert.getClass().getDeclaredFields()){

            if(availableFieldsInDatabase.contains(field.getName())){
                field.setAccessible(true);
                preparedStatement.setObject(index, field.get(objectToInsert));
                index++;
            }
        }
    }

    private String getArguments(Class<?> cls) {
        List<Field> fields = Arrays.asList(cls.getDeclaredFields());
        List<String> listFieldNames = fields.stream().map(Field::getName)
                .filter(availableFieldsInDatabase::contains).map(field -> "?")
                .collect(Collectors.toList());

        return String.join(",", listFieldNames);
    }

    private String getFields(Class<?> cls) {
        List<Field> fields = Arrays.asList(cls.getDeclaredFields());
        List<String> listFieldNames = fields.stream().map(Field::getName)
                .filter(availableFieldsInDatabase::contains)
                .collect(Collectors.toList());

        return String.join(",", listFieldNames);
    }

    private String getTableName(Class<?> cls) {
        return cls.getAnnotation(TableAnnotation.class).value();
    }

    public static List<String> collectMetaInformation(Connection connection, Object objectToInsert) {

        // SELECT * FROM objectToInsert > annotation > person
        // ResultSet.getMetadata
        return List.of("name", "age", "position");
    }
}
