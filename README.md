# ChaTop Application

This project uses Java 17 with Maven, and Spring Boot 3.

It is the backend of an app, you an find the front-end here : https://github.com/OpenClassrooms-Student-Center/Developpez-le-back-end-en-utilisant-Java-et-Spring

## Start the project :

Git clone:

    git clone https://github.com/Shikizzz/FullStack-P3-ChaTop.git

## Database initialization :

If you don't already have MySql, follow this guide : https://dev.mysql.com/doc/mysql-getting-started/en/

In your terminal, type these commands :

> mysql -u username -p password       (MySql connection)

> CREATE DATABASE chatop

> USE chatop

> SOURCE {path_of_the_file}/script.sql   (script is in the ressources folder)

## Run project :

In your terminal, go to the preject root and use this command:
mvn spring-boot:run
