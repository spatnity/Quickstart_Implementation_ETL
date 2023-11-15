# jdbc-etl

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Start the source and target databases

All the commands in this example are expected to be run from the example directory, at the same level than the `pom.xml` file.

In a first terminal, let's start the source database by executing the command below:

```shell script
docker run -p 5432:5432 \
-e POSTGRES_USER=ETL_source_user \
-e POSTGRES_PASSWORD=1234567@8_source \
-e POSTGRES_DB=source_db \
-v ${PWD}/src/test/resources/init-source-db.sql:/docker-entrypoint-initdb.d/init-source-db.sql \
docker.io/postgres:15.0
```

In a second terminal, let's start the target database:

```shell script
docker run -p 5433:5432 \
-e POSTGRES_USER=ETL_target_user \
-e POSTGRES_PASSWORD=1234567@8_target \
-e POSTGRES_DB=target_db \
-v ${PWD}/src/test/resources/init-target-db.sql:/docker-entrypoint-initdb.d/init-target-db.sql \
docker.io/postgres:15.0
```

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
mvn compile quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

Extract, Transform and Load related logs should be output as below:

```shell script
2023-11-14 15:12:55,878 INFO  [route17] (Camel (camel-9) thread #9 - timer://insertCamel) Extracting data from source database
2023-11-14 15:12:55,881 INFO  [route17] (Camel (camel-9) thread #9 - timer://insertCamel) -> Transforming review for hotel 'Grand Hotel'
2023-11-14 15:12:55,886 INFO  [route17] (Camel (camel-9) thread #9 - timer://insertCamel) -> Loading transformed data in target database
2023-11-14 15:12:55,893 INFO  [route17] (Camel (camel-9) thread #9 - timer://insertCamel) -> Transforming review for hotel 'Middle Hotel'
2023-11-14 15:12:55,897 INFO  [route17] (Camel (camel-9) thread #9 - timer://insertCamel) -> Loading transformed data in target database
2023-11-14 15:12:55,904 INFO  [route17] (Camel (camel-9) thread #9 - timer://insertCamel) -> Transforming review for hotel 'Small Hotel'
2023-11-14 15:12:55,909 INFO  [route17] (Camel (camel-9) thread #9 - timer://insertCamel) -> Loading transformed data in target database
```

## Packaging and running the application

The application can be packaged using:

```shell script
mvn package
```
It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an  _über-jar_  as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable executing the command below:

```
java -jar target/quarkus-app/quarkus-run.jar
```

The application should output the same logs than in previous section.

## Creating a native executable

Finally, the application can be compiled to native with the following command:

```shell script
mvn package -Dnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: 

```shell script
mvn package -Dnative -Dquarkus.native.container-build=true
```

Either way, the resulting native executable could be start as below:

```
./target/*-runner
```

The application should output the same logs than in previous section.

If you want to learn more about building native executables, please consult https://quarkus.io/guides/maven-tooling.