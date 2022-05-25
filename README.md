# spring-boot

## Technical:

1. Framework: Spring Boot v2.6.6 (or more)
2. Java 8
3. Thymeleaf
4. Bootstrap v.5.1.3 (or more)

## Setup with Intellij IDE

1. Create project from Initializr: File > New > project > Spring Initializr (or fork/clone initial repo)
2. Create database with name "demo" as configuration in application.properties
3. Run sql script to
    - create table resources/DbCreateTables.sql
    - insert example input : resources/DbInsertTestValues.sql

## Implement a Feature

1. Create entity class and place in package com.poseidon.app.dal.entity
2. Create repository class and place in com.poseidon.app.dal.repository
3. Create service class and place in com.poseidon.app.domain.service
4. Create (front) controller class and place in package com.poseidon.app.web.frontController
5. Create (api) restController class and place in package com.poseidon.app.web.apiController
6. Create view files and place in src/main/resource/templates

## Write Unit Test

1. Create unit test and place in folder src/test/java

## Security (if not already done)

1. Create userEntity service to load userEntity from database
2. Add configuration class and place in package com.poseidon.app.configuration
