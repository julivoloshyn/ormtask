package com.task;

import com.task.dto.Person;
import com.task.orm.ORMInterface;
import com.task.orm.ormimpl.ORM;
import com.task.readwritesource.readwritesourceimpl.ConnectionReadWriteSource;
import com.task.readwritesource.DataReadWriteSource;
import lombok.SneakyThrows;

import java.net.URL;
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
    }

    @SneakyThrows
    private static void process(Connection connection) {
        URL url = Main.class.getClassLoader().getResource("sample.json");

        List<Person> result;
        // result = ORM.readAll(new FileReadWriteSource(new File(url.toURI())), Person.class);
        // result.add(new Person("WRITE", BigInteger.ZERO, BigInteger.ZERO, "WRITE", LocalDate.now(), 0F)));
        // ORM.writeAll(new ORMInterface.FileReadWriteSource(new File(url.toURI())), result);

        DataReadWriteSource<ResultSet> rw = new ConnectionReadWriteSource(connection, "person");
        result = ORM.readAll(rw, Person.class);
        //result.add(new Person("WRITE", BigInteger.ZERO, BigInteger.ZERO, "WRITE", LocalDate.now(), 0F));
        // ORM.writeAll(rw, result);
    }

    @SneakyThrows
    private static void withConnection(Function<Connection, Void> function) {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:sample.db")) {
            try (Statement statement = c.createStatement()) {
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS person " +
                        "(id INTEGER not NULL, " +
                        " name VARCHAR(255), " +
                        " position VARCHAR(255), " +
                        " age INTEGER, " +
                        " PRIMARY KEY ( id ))");

                statement.executeUpdate("DELETE FROM person");
                for (int index = 0; index < 10; index++) {
                    statement.executeUpdate("INSERT INTO person (name, position, age) VALUES ('1', '1', 1)");
                }
            }
            function.apply(c);
        }
    }

}
