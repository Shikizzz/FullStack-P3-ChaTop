This project uses Java 17 with Maven, and Spring Boot 3.

Start the project :

Git clone:

    git clone https://github.com/Shikizzz/FullStack-P3-ChaTop.git

Database initialization :

If you don't already have MySql, follow this guide : https://dev.mysql.com/doc/mysql-getting-started/en/

In your terminal, type these commands :
    mysql -u username -p password       (MySql connection)
    CREATE DATABASE chatop
    USE chatop
    SOURCE {path_of_the_file}/script.sql   (script is in the ressources folder)

Run project :

In your terminal, go to the preject root and use this command:
mvn spring-boot:run
