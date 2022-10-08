# ORM

ORM is an app which can read from- and write data to files and databases. ORM works with JSON, XML and CSV files.

## Technologies

Project created with:

* Java version: 1.8
* Maven version: 3.0

## Usage

There are two models: Person and Person2 (Person2 is simplified). You can add your own model.

### File

* Reading example:

`File fileCSVr = new File("src/main/resources/fileToRead.csv");`\
`List<Person2> listFromCSV = ORM.readAll(new FileReadWriteSource(fileCSVr), Person2.class);`

* Writing example:

`File fileJSONw = new File("src/main/resources/fileToWrite.json");`\
`ORM.writeAll(new FileReadWriteSource(fileJSONw), listFromJSON);`

`listFromJSON` is a data that was already parsed from JSON file as in reading example.

### Database

In the method `withConnection` you can write your own statement. To view database get `Ultimate` version of `IntelliJ IDEA` or install browser extension `SQLite Manager`.

* Reading example:

`DataReadWriteSource<ResultSet> rw = new ConnectionReadWriteSource(connection, "Person2");`\
`List<Person> listFromDB = ORM.readAll(rw, Person.class);`


* Writing example:

` Object objectToInsert = new Person2("Alex", "Senior", 30);`\
`ORM.writeAll(rw, Collections.singletonList(objectToInsert));`

## Setup

To run this project, install it locally

For tests running `mvn clean test` \
For project running `mvn tomcat:start`