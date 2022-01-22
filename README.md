# employee-system Project

This is a demo project to showcase the architectural depth. Uncle Bob's clean architecture has been taken as motivation for the design here.

## Pre requisites

- docker
- Check if all proxies are set

### Clone the project

```shell script
git clone https://github.com/pricelessjunk/personio.git
```

### Run tests

```shell script
> docker-compose up personio-postgres   # Start the db
> mvnw test                             # db needed for the integration tests
``` 

### Run Service

```shell script
> mvnw clean package -DskipTests
> docker-compose build
> docker-compose up
``` 

This starts the postgres database in the background and 

