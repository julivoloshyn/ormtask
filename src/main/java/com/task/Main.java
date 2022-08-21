package com.task;

import com.task.dto.Person;
import com.task.orm.ORMInterface;
import com.task.orm.ormimpl.ORM;
import com.task.readwritesource.DataReadWriteSource;
import com.task.readwritesource.readwritesourceimpl.ConnectionReadWriteSource;
import com.task.readwritesource.readwritesourceimpl.FileReadWriteSource;
import lombok.SneakyThrows;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.function.Function;

public class Main {

    private static final ORMInterface ORM = new ORM();

    public static void main(String[] args) throws Exception {

        withConnection(connection -> {
            process(connection);
            return null;
        });

        //reading files
        File fileCSVr = new File("src/main/resources/fileToRead.csv");
        List<Person> listFromCSV = ORM.readAll(new FileReadWriteSource(fileCSVr), Person.class);

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

    @SneakyThrows
    private static void process(Connection connection) {

        DataReadWriteSource<ResultSet> rw = new ConnectionReadWriteSource(connection, "person");
        List<Person> listFromDB = ORM.readAll(rw, Person.class);
        System.out.println(listFromDB);

    }

    @SneakyThrows
    private static void withConnection(Function<Connection, Void> function) {
        try (Connection с = DriverManager.getConnection("jdbc:sqlite:sample.db")) {
            try (Statement stmt = с.createStatement()) {
                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS person " +
                        "(id INTEGER not NULL, " +
                        " name VARCHAR(255), " +
                        " position VARCHAR(255), " +
                        " age INTEGER, " +
                        " PRIMARY KEY ( id ))");

                stmt.executeUpdate("DELETE FROM person");
                for (int index = 0; index < 10; index++) {
                    stmt.executeUpdate("INSERT INTO person (name, position, age) VALUES ('1', '1', 1)");
                }
            }
            function.apply(с);
        }
    }
}