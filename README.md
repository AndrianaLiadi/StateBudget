# StateBudget
The purpose of this applicattion is to encourage people to study the state budget of the Hellenic Republic among the years 2019-2025 in order to get familiar with economic statements. Our app adresses all ages but mostly young people so that in a fun way understand the importansce of ecomomic management and delve into political topics. 

# Service 
### StateBudegetManager loads data from csv files and launch the corresponding table of annual budget. Users can make changes on this tables by creating scenarios with altered information, compare yearly data and view complex diagrams.

# How it works 

### git clone - cloning repository on our devices 
### cd StateBudget  -
# Structure
## Utilization of ***Maven*** for build structure 
### mvn clean install  -installing maven on the repository 
### mvn clean compile -compilation of classes
### mvn exec:java -execution during developement 
### mvn package -convert into .jar
### mvn jacoco:report -checking test coverege
### mvn checkstyle:check -checking code's formating 
### java -jar target/StateBudgetMaven-0.0.1-SNAPSHOT.jar -execution of .jar

### file .gitignore where target and class files are saved to avoid conflicts
### csv files of Hellenic's Republic budget from 2019-2025 are saved on the repository
### ***pom.xml*** - maven configuration


## Working on five ***packages*** (model, data, logic, main, ui) 
### model: budget and methods for changes 
### data: loads data from csv
### logic: mansages loading, analysis and comparison of data
### main: includes main method (execution of the programm) 
### ui: screens visible to the user  

## 22 classes in total 
### model: 4 classes
### data: 1 class
### logic: 2 classes
### main: 2 classes
### ui: 13 classes
### <sub>*on package main there is 1 class running on terminal and another for the GUI for safety reasons, depending on pom.xml which one is used<sub>


 ## Tests 
 ### Each class has their own test on a seperate file called test including:
 ### model: 4 classes
 ### data: 1 class
 ### logic: 2 classes
 ### main: 2 classes
 ### ui: 13 classes
 ### test.csv: structure of csv files to run on tests

StateBudget
 ├── settings
 │   ├── org.eclipse.core.resources.prefs
 │   ├── org.eclipse.jdt.core.prefs
 │   └── org.eclipse.m2e.core.prefs
 ├── .vscode
 │     └── {} settings.json
 ├── src/main/java/
 │   ├── model                    
 │   │   ├── Budget.java           
 │   │   ├── BudgetItem.java       
 │   │   ├── BudgetChange.java     
 │   │   └── Scenario.java         
 │   ├── data                    
 │   │   └── BudgetDataLoader.java 
 │   ├── logic                    
 │   │   ├── BudgetService.java  
 │   │   └── ReportGenerator.java  
 │   ├── main 
 │   │    ├── Main.java
 │   │   └── MainGUI.java
 │   └── ui
 │        ├── AboutScreen.java
 │        ├── AppController.java
 │        ├── BudgetChangesScreen.java
 │        ├── BudgetChabgeTable.java
 │        ├── BudgetScreen.java
 │        ├── BudgetTablePrinter.java
 │        ├── ChartPanel.java
 │        ├── ContactScreen.java
 │        ├── HomeScreen.java
 │        ├── LandingScreen.java
 │        ├── RegistrationScreen.java
 │        ├── ReportScreen.java
 │        └── ScenarioScreen.java
 │         
 ├── src/test/java/                
 ├── data                        
 │    └── BudgetDataLoader.java
 ├── logic
 │    ├── BudgetServiceTest.java 
 │    └── ReportGeneratorTest.java
 ├── main
 │    ├── MainTest.java
 │    └── MainGUITest.java 
 ├── model
 │    ├── BudgetChangeTest.java
 │    ├── BudgetItemTest.java
 │    ├── BudgetTest.java
 │    └── ScenarioTest.java
 └── ui
 │    ├── AboutScreenTest.java
 │    ├── AppControlerTest.java
 │    ├── BudgetChangesScreenTest.java
 │    ├── BudgetChangeTableTest.java
 │    ├── BudgetScreenTest.java 
 │    ├── BudgetTablePrinterTest.java
 │    ├── ChartPanelTest.java
 │    ├── ContactScreenTest.java
 │    ├── HomeScreenTest.java
 │    ├── LandingScreenTest.java
 │    ├── RegistrationScreenTest.java
 │    ├── ReportScreenTest.java
 │    └── ScenarioScreenTest.java
 ├── budget-2019.csv
 ├── budget-2020.csv
 ├── budget-2021.csv
 ├── budget-2022.csv
 ├── budget-2023.csv
 ├── budget-2024.csv
 ├── budget-2025.csv 
 ├── test.csv 
 ├── pom.xml                       
 ├── .gitignore                     
 └── README.md  
 

# Algorithms and Data Structure 
## Data Structure
### Hierarchical Budget Model (Composite Pattern) - Tree structure for budget organization
### Immutable Change Tracking - Historical audit trail with mathematical metrics
### Scenario Management - Version control for budget variations
### Report Generation - Structured financial documentation

## Key Algorithims 
### Data Processing Pipeline (O(n) complexity)
### CSV parsing with Unicode support and data cleaning
### Stream-based loading for memory-efficient large file handling
### Automatic type detection (Revenue/Expenditure categorization)
### Budget comparison with O(N+M) pairwise item matching
### Recursive aggregation for hierarchical sum calculations
### Percentage change computation with edge-case handling (zero-division protection)
### Surplus/deficit analysis with net impact calculation
### Deep cloning for safe scenario creation
### Change application with item search and update operations
### Automatic summary generation from change sets
### Multi-pass aggregation by financial categories
### Formatted output generation with proper financial notation
### File export with automatic filename sanitization

## Design Patterns Implemented
### Composite Pattern (BudgetItem hierarchy) -Hierachical budget items, enables tree-like structures
### Prototype Pattern (Cloneable interface) - supports object duplication
### Memento-like Pattern (BudgetChange) - tracks state changes
### Strategy Pattern - Extensible comparison and export methods
### Factory Pattern - Budget object creation from multiple sources
### Observer Pattern - Automatic summary updates on changes
### Builder Pattern - Incremental report construction

## Data Storage Structures
### **ArrayList BudgetItem** - Sequential access for calculations
### **List BudgetItem** - Hierarchical List, tree organization of categories/subcategories
### **List BudgetChange** - Chronological change tracking

## Performance Characteristics
### Space Complexity: Proportional to data size with streaming optimizations
### Time Complexity: Linear for most operations
### Memory Safety: Deep copying prevents data corruption
### Error Handling: Protected calculations (zero-division prevention)

## Mathematical Operations
### Financial calculations: Absolute/percentage changes, net impact
### Statistical aggregation: Category-wise summation, hierarchical totals
### Data validation: Input cleaning, edge-case handling, error resilience



