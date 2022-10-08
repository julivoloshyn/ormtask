package com.task.writingstrategy.writingstrategyimpl;

import com.task.writingstrategy.WritingStrategy;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class CSVWritingStrategy implements WritingStrategy {

    private File file;

    /**
     * Parses list to csv format.
     *
     * @param objects List of content.
     */
    @SneakyThrows
    @Override
    public void write(List<?> objects) {
        Class cls = objects.get(0).getClass();
        List<Field> fields = Arrays.asList(cls.getDeclaredFields());

        List<String> strings = new ArrayList<>();
        strings.add(convertToHeader(fields));
        strings.addAll(objects.stream().map(item -> convertFieldsToString(item, fields)).collect(Collectors.toList()));

        String data = String.join(System.lineSeparator(), strings);
        FileUtils.writeStringToFile(file, data, StandardCharsets.UTF_8);
    }

    /**
     * Converts fields to string.
     *
     * @param object Object.
     * @param fields List of fields.
     */
    @SneakyThrows
    public String convertFieldsToString(Object object, List<Field> fields){
        List<String> person = new ArrayList<>();

        for(Field field : fields){
            field.setAccessible(true);
            String str = String.valueOf(field.get(object));
            person.add(str);
        }

        return String.join(", ", person);
    }

    /**
     * Coverts first line to headers.
     *
     * @param fields List of fields.
     */
    private String convertToHeader(List<Field> fields) {
        List<String> headers = new ArrayList<>();

        for(Field field : fields){
            headers.add(field.getName());
        }

        return String.join(", ", headers);
    }

}
