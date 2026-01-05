# StateBudget
# The purpose of this applicattion is to encourage people to study the state budget of the Hellenic Republic among the years 2019-2025 in order to get familiar with economic statements. Our app adresses all ages but mostly young people so that in a fun way understand the importansce of ecomomic management and delve into political topics. 

# Service 
# StateBudegetManager loads data from csv files and launch the corresponding table of annual budget. Users can make changes on this tables by creating scenarios with altered information, compare yearly data and view complex diagrams.

# How it works 



# Utilization of Maven for structure 
# mvn clean install  -installing maven on the repository 
# mvn clean compile -compilation of classes
# mvn exec:java -execution during developement 
# mvn package -convert into .jar 
# mvn jacoco:report -checking test coverege
# mvn checkstyle:check -checking code's formating 
# java -jar target/StateBudgetMaven-0.0.1-SNAPSHOT.jar -execution of .jar 

# Working on five packages (model, data, logic, main, ui) 
# model: budget and methods for changes 
# data: loads data from csv
# logic: mansages loading, analysis and comparison of data
# main: includes main method (execution of the programm)
# ui: screens visible to the user  



# .gitignore
