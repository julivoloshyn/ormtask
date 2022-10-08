package com.task;

import com.task.dto.Person;
import com.task.dto.Person2;
import com.task.orm.ORMInterface;
import com.task.orm.ormimpl.ORM;
import com.task.readwritesource.DataReadWriteSource;
import com.task.readwritesource.readwritesourceimpl.ConnectionReadWriteSource;
import com.task.readwritesource.readwritesourceimpl.FileReadWriteSource;
import lombok.SneakyThrows;

import java.io.File;
import java.sql.*;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class Main {

    private static final ORMInterface ORM = new ORM();

    public static void main(String[] args) {

        withConnection(connection -> {
            process(connection);
            return null;
        });

        //reading files
        File fileCSVr = new File("src/main/resources/fileToRead.csv");
        List<Person2> listFromCSV = ORM.readAll(new FileReadWriteSource(fileCSVr), Person2.class);

        File fileXMLr = new File("src/main/resources/fileToRead.xml");
        List<Person> listFromXML = ORM.readAll(new FileReadWriteSource(fileXMLr), Person.class);

        File fileJSONr = new File("src/main/resources/fileToRead.json");
        List<Person> listFromJSON = ORM.readAll(new FileReadWriteSource(fileJSONr), Person.class);

        //writing to files
        File fileJSONw = new File("src/main/resources/fileToWrite.json");
        ORM.writeAll(new FileReadWriteSource(fileJSONw), listFromJSON);

        File fileXMLw = new File("src/main/resources/fileToWrite.xml");
        ORM.writeAll(new FileReadWriteSource(fileXMLw), listFromXML);

        File fileCSVw = new File("src/main/resources/fileToWrite.csv");
        ORM.writeAll(new FileReadWriteSource(fileCSVw), listFromCSV);

    }

    /**
     * Writes and reads database.
     *
     * @param connection Connection.
     */
    @SneakyThrows
    private static void process(Connection connection) {

        DataReadWriteSource<ResultSet> rw = new ConnectionReadWriteSource(connection, "Person2");
        List<Person> listFromDB = ORM.readAll(rw, Person.class);

        Object objectToInsert = new Person2("Alex", "Senior", 30);
        ORM.writeAll(rw, Collections.singletonList(objectToInsert));

    }

    /**
     * Connects to database.
     *
     * @param function Function.
     */
    @SneakyThrows
    public static void withConnection(Function<Connection, Void> function) {
        try (Connection с = DriverManager.getConnection("jdbc:sqlite:sample.db")) {
            try (Statement stmt = с.createStatement()) {
                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Person2 " +
                        "(id INTEGER not NULL, " +
                        " name VARCHAR(255), " +
                        " position VARCHAR(255), " +
                        " age INTEGER, " +
                        " PRIMARY KEY ( id ))");

                stmt.executeUpdate("DELETE FROM person");
                stmt.executeUpdate("INSERT INTO person (name, position, age) VALUES ('Josh', 'Junior', 19)");
            }
            function.apply(с);
        }
    }

}