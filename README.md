# StateBudget
### The purpose of this applicattion is to encourage people to study the state budget of the Hellenic Republic among the years 2019-2025 in order to get familiar with economic statements. Our app adresses all ages but mostly young people so that in a fun way understand the importansce of ecomomic management and delve into political topics. 

# Service 
### StateBudegetManager loads data from csv files and launch the corresponding table of annual budget. Users can make changes on this tables by creating scenarios with altered information, compare yearly data and view complex diagrams.

# How it works 


# Structure

## Utilization of Maven for build structure 
### mvn clean install  -installing maven on the repository 
### mvn clean compile -compilation of classes
### mvn exec:java -execution during developement 
### mvn package -convert into .jar
### mvn jacoco:report -checking test coverege
### mvn checkstyle:check -checking code's formating 
### java -jar target/StateBudgetMaven-0.0.1-SNAPSHOT.jar -execution of .jar

### file .gitignore where target and class files are saved to avoid conflicts 
### pom.xml 


## Working on five packages (model, data, logic, main, ui) 
### model: budget and methods for changes 
### data: loads data from csv
### logic: mansages loading, analysis and comparison of data
### main: includes main method (execution of the programm) 
### ui: screens visible to the user  

## 23 classes in total 
### model: 4 classes
### data: 1 class
### logic: 2 classes
### main: 2 classes
### ui: 14 classes
### *on package main there is 1 class running on terminal and another for the GUI for safety reasons, depending on pom.xml which one is used.

### csv files of Hellenic's Republic budget from 2019-2025 are saved on the repository

 ## Tests 
 ### Each class has their own test on a seperate file called test including:
 ### model: 4 classes
 ### data: 1 class
 ### logic: 2 classes
 ### main: 2 classes
 ### ui: 14 classes
 ### test.csv: structure of csv files to run on tests

 



