package com.task.orm.ormimpl;

import com.task.parsingstrategy.table.Table;
import com.task.orm.ORMInterface;
import com.task.parsingstrategy.ParsingStrategy;
import com.task.parsingstrategy.parsingstrategyimpl.CSVParsingStrategy;
import com.task.parsingstrategy.parsingstrategyimpl.DatabaseParsingStrategy;
import com.task.parsingstrategy.parsingstrategyimpl.JSONParsingStrategy;
import com.task.parsingstrategy.parsingstrategyimpl.XMLParsingStrategy;
import com.task.readwritesource.readwritesourceimpl.ConnectionReadWriteSource;
import com.task.readwritesource.DataReadWriteSource;
import com.task.readwritesource.readwritesourceimpl.FileReadWriteSource;
import com.task.writingstrategy.WritingStrategy;
import com.task.writingstrategy.writingstrategyimpl.CSVWritingStrategy;
import com.task.writingstrategy.writingstrategyimpl.DatabaseWritingStrategy;
import com.task.writingstrategy.writingstrategyimpl.JSONWritingStrategy;
import com.task.writingstrategy.writingstrategyimpl.XMLWritingStrategy;
import lombok.SneakyThrows;
import org.apache.commons.io.FilenameUtils;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;

public class ORM implements ORMInterface {

    //for parsing strategy

    /**
     * Fills model list.
     *
     * @param inputSource Connection.
     * @param cls Unknown class.
     * @param <T> Unknown type.
     * @return Model list.
     */
    @Override
    @SneakyThrows
    public <T> List<T> readAll(DataReadWriteSource<?> inputSource, Class<T> cls) {
        Table table = convertToTable(inputSource);

        return convertTableToListOfClasses(table, cls);
    }

    /**
     * Fills values into model fields.
     *
     * @param table Table - Map(key, value).
     * @param cls Unknown class.
     * @param <T> Unknown type.
     * @return Model list.
     */
    private <T> List<T> convertTableToListOfClasses(Table table, Class<T> cls) {
        List<T> result = new ArrayList<>();

        for (int index = 0; index < table.size(); index++) {
            Map<String, String> row = table.getTableRowByIndex(index);
            T instance = reflectTableRowToClass(row, cls);
            result.add(instance);
        }

        return result;
    }

    /**
     * Sets up values into model class.
     *
     * @param row Key, value.
     * @param cls Unknown class.
     * @param <T> Unknown type.
     * @return Cls instance.
     */
    @SneakyThrows
    private <T> T reflectTableRowToClass(Map<String, String> row, Class<T> cls) {
        T instance = cls.getDeclaredConstructor().newInstance();

        for (Field each : cls.getDeclaredFields()) {
            each.setAccessible(true);
            String value = row.get(each.getName());

            if (value != null) {
                each.set(instance, transformValueToFieldType(each, value));
            }
        }

        return instance;
    }

    /**
     * Fills values into fields.
     *
     * @param field From unknown class.
     * @param value To fill field.
     * @return Required object.
     */
    private static Object transformValueToFieldType(Field field, String value) {
        Map<Class<?>, Function<String, Object>> typeToFunction = new LinkedHashMap<>();

        typeToFunction.put(String.class, s -> s);
        Function<String, Object> parseInt = Integer::parseInt;
        typeToFunction.put(int.class, parseInt);
        typeToFunction.put(Float.class, Float::parseFloat);
        typeToFunction.put(LocalDate.class, LocalDate::parse);
        typeToFunction.put(LocalDateTime.class, LocalDate::parse);
        typeToFunction.put(Long.class, Long::parseLong);
        typeToFunction.put(BigInteger.class, BigInteger::new);

        return typeToFunction.getOrDefault(field.getType(), type -> {
            throw new UnsupportedOperationException("Type is not supported by parser " + type);
        }).apply(value);
    }

    /**
     * Checks kind of input source.
     *
     * @param dataInputSource From unknown class.
     * @return Table of values.
     */
    private Table convertToTable(DataReadWriteSource dataInputSource) {

        if (dataInputSource instanceof ConnectionReadWriteSource) {
            ConnectionReadWriteSource databaseSource = (ConnectionReadWriteSource) dataInputSource;
            return new DatabaseParsingStrategy().parseToTable(databaseSource);

        } else if (dataInputSource instanceof FileReadWriteSource) {
            FileReadWriteSource fileSource = (FileReadWriteSource) dataInputSource;
            return getStringParsingStrategy(fileSource).parseToTable(fileSource);

        } else {
            throw new UnsupportedOperationException("Unknown DataInputSource " + dataInputSource);
        }
    }

    /**
     * Checks extension of file by characters.
     *
     * @param inputSource From unknown class.
     * @return Parsing strategy.
     */
    private ParsingStrategy<FileReadWriteSource> getStringParsingStrategy(FileReadWriteSource inputSource) {
        String content = inputSource.getContent();
        char firstChar = content.charAt(0);

        switch (firstChar) {
            case '{':
            case '[':
                return new JSONParsingStrategy();
            case '<':
                return new XMLParsingStrategy();
            default:
                return new CSVParsingStrategy();
        }
    }

    //for writing strategy

    /**
     * Checks kind of writing source and writes to file.
     *
     * @param fileToInsert From unknown class.
     * @param objects List to write.
     */
    @Override
    public <T> void writeAll(DataReadWriteSource fileToInsert, List<?> objects) {

        if(fileToInsert instanceof FileReadWriteSource){
            WritingStrategy fileStrategy = getTypeOfFileToInsert(fileToInsert);
            fileStrategy.write(objects);

        } else if(fileToInsert instanceof ConnectionReadWriteSource){
            DatabaseWritingStrategy dbStrategy = new DatabaseWritingStrategy();
            dbStrategy.write(objects);
        }
    }

    /**
     * Checks extension of file to choose a writing strategy.
     *
     * @param fileToInsert From unknown class.
     * @return Writing strategy.
     */
    private WritingStrategy getTypeOfFileToInsert(DataReadWriteSource fileToInsert) {
        String extension = FilenameUtils
                .getExtension(((FileReadWriteSource) fileToInsert).getSource().getName());

        switch (extension) {
            case "json":
                return new JSONWritingStrategy(((FileReadWriteSource) fileToInsert).getSource());
            case "xml":
                return new XMLWritingStrategy(((FileReadWriteSource) fileToInsert).getSource());
            default:
                return new CSVWritingStrategy(((FileReadWriteSource) fileToInsert).getSource());
        }
    }


}

