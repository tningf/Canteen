# Project Name
Canteen Management System

## Description
The Canteen Management System is a Spring Boot application designed to manage the operations of a canteen. It provides functionalities for managing products items, orders, and user authentication. The system uses Microsoft SQL Server as the database and is built with Java 21 and Maven. The application can be run locally or deployed using Docker.

## Technologies Used
- Java: 21
- Spring Boot: 3.4.0
- Maven: 3.9.9
- DBMS: Microsoft SQL Server

## Prerequisites
- Java SDK 21
- A Microsoft SQL Server instance

## Setup
1. Clone the repository:
    ```sh
    git clone https://github.com/tningf/Canteen.git
    ```
2. Navigate to the project directory:
    ```sh
    cd Canteen
    ```
3. Build the project:
    ```sh
    mvn clean install
    ```

## Configuration
The application uses the following environment variables for configuration:
- `SPRING_DATASOURCE_URL`: The URL of the Microsoft SQL Server instance. (E.g. jdbc:sqlserver://localhost:1433;databaseName=your-database-name)
- `SPRING_DATASOURCE_USERNAME`: The username for the Microsoft SQL Server instance.
- `SPRING_DATASOURCE_PASSWORD`: The password for the Microsoft SQL Server instance.


## Running the Application
To run the application, use the following command:
```sh
mvn spring-boot:run
```
By default, the API will be available at http://localhost:8080.

## Docker guidelines

1. Build the Docker image:
    ```sh
    docker build -t canteen:0.0.1 .
    ```

2. Run the Docker container:
    ```sh
    docker run --name canteen -p 8080:8080 canteen:0.0.1
    ```