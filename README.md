# StateBudget
The purpose of this applicattion is to encourage people to study the state budget of the Hellenic Republic among the years 2019-2025 in order to get familiar with economic statements. Our app adresses all ages but mostly young people so that in a fun way understand the importansce of ecomomic management and delve into political topics. 

# How it works 
1. Loads data of national budget (2019-2025) from csv files.
2. Presentation of annual state budget
3. Creation of scenarios for changes on specific data
4. Comparison of scenarios and between annual budgets
5. Creation of complex diagrams

# Manual 
 User enters the app by adding personal info.  
	By clicking on ***Home*** and "**Δες τον προϋπολογισμό**" appears national budget of 2025.  
 User can choose any year from 2019 to 2025 on the bar on top.  
	After than they can click on ***files*** and select ***Scenarios***.  
 There they make changes and apply.  
 Button ***Reports*** is used to view diagrams and comparison among years.

# Structure
## Utilization of ***Maven*** for build structure 
**mvn clean install**  -installing maven on the repository  
**mvn clean compile** -compilation of classes  
**mvn exec:java** -execution during developement   
**mvn package** -convert into .jar  
***java -jar target/StateBudgetMaven-0.0.1-SNAPSHOT.jar*** -execution of .jar

***pom.xml*** - maven configuration  
**mvn clean test** - execute test  
**mvn jacoco:report** - checking test coverege  
**mvn checkstyle:check** -checking code's formating  
**csv files** of Hellenic's Republic budget from 2019-2025 are saved on the repository  
file **.gitignore** where target and class files are saved to avoid conflicts

## Repository's structure 
## Working on five ***packages*** (model, data, logic, main, ui) 
**model:** budget and methods for changes   
**data:** loads data from csv  
**logic:** mansages loading, analysis and comparison of data  
**main:** includes main method (execution of the programm)  
**uι:** screens visible to the user

## 22 classes in total 
**model:** 4 classes  
**data:** 1 class  
**logic:** 2 classes  
**main:** 2 classes  
**ui:** 13 classes  
### <sub>*on package main there is 1 class running on terminal and another for the GUI for safety reasons, depending on pom.xml which one is used<sub>

 ## Tests 
 Each class has their own test on a seperate file called test including:  
 **model:** 4 classes  
 **data:** 1 class  
 **logic:** 2 classes  
 **main:** 2 classes  
 **ui:** 13 classes  
 **test.csv:** structure of csv files to run on tests
 
## Diagram of Structure 
**StateBudget**  
 ├── settings  
 │   ├── org.eclipse.core.resources.prefs\
 │   ├── org.eclipse.jdt.core.prefs\
 │   └── org.eclipse.m2e.core.prefs\
 ├── .vscode\
 │     └── {} settings.json  
 ├── docs  
 │   │   ├── data  
 │   │   │     ├── BudgetDataLoader.html  
 │   │   │     ├── package-summary.html    
 │   │   │     └── package-tree.html    
 │   │   ├── legal      
 │   │   │      ├── COPYRIGHT  
 │   │   │      ├──dejavufonts.md  
 │	 │	 │	    ├──jquery.md  
 │	 │	 │	    ├──jqueryui.md  
 │	 │	 │	    └── LISCENCE  
 │   │   ├── logic    
 │   │   │      ├── BudgetService.html    
 │   │   │      ├── ReportGenerator.html   
 │   │   │      ├── package-summary.html    
 │   │   │      └── package-tree.html    
 │   │   ├── main    
 │   │   │      ├── Main.html    
 │   │   │      ├── MainGUI.html   
 │   │   │      ├── package-summary.html    
 │   │   │      └── package-tree.html    
 │   │   ├── model  
 │   │   │      ├── Budget.html   
 │   │   │      ├── BudgetChange.html    
 │   │   │      ├── BudgetItem.html    
 │   │   │      ├── Scenario.html    
 │   │   │      ├── package-summary.html    
 │   │   │      └── package-tree.html    
 │   │   ├──resource-files  
 │   │   │       └── fonts    
 │   │   ├── script-files  
 │   │   ├── ui      
 │   │   │    ├── AboutScreen.html  
 │   │   │    ├── AppController.html  
 │   │   │    ├── BudgetChangesScreen.html  
 │   │   │    ├── BudgetChangeTable.html  
 │   │   │    ├── BudgetScreen.html  
 │   │   │    ├── BudgetTablePrinter.html  
 │   │   │    ├── ChartPanel.html  
 │   │   │    ├── ContactScreen.html  
 │   │   │    ├── HomeScreen.html  
 │   │   │    ├── LandingScreen.html  
 │   │   │    ├── RegistrationScreen.html  
 │   │   │    ├── ReportScreen.html  
 │   │   │    ├── ScenarioScreen.html  
 │   │   │    ├── package-summary.html  
 │   │   │    └── package-tree.html  
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
 │   │   ├── Main.java  
 │   │   └── MainGUI.java  
 │   ├── ui  
 │   │    ├── AboutScreen.java  
 │   │    ├── AppController.java  
 │   │    ├── BudgetChangesScreen.java  
 │   │    ├── BudgetChabgeTable.java  
 │   │    ├── BudgetScreen.java  
 │   │    ├── BudgetTablePrinter.java  
 │   │    ├── ChartPanel.java  
 │   │    ├── ContactScreen.java  
 │   │    ├── HomeScreen.java  
 │   │    ├── LandingScreen.java  
 │   │    ├── RegistrationScreen.java  
 │   │    ├── ReportScreen.java  
 │   │    └── ScenarioScreen.java  
 │          
 ├── src/test/java/                
 │  ├── data                         
 │  │    └── BudgetDataLoader.java  
 │  ├── logic  
 │  │    ├── BudgetServiceTest.java  
 │  │    └── ReportGeneratorTest.java  
 │  ├── main  
 │  │     ├── MainTest.java  
 │  │     └── MainGUITest.java   
 │  ├── model  
 │  │     ├── BudgetChangeTest.java  
 │  │     ├── BudgetItemTest.java  
 │  │     ├── BudgetTest.java  
 │  │    └── ScenarioTest.java  
 │  ├──ui  
 │  │   ├── AboutScreenTest.java  
 │  │   ├── AppControlerTest.java  
 │  │   ├── BudgetChangesScreenTest.java  
 │  │   ├── BudgetChangeTableTest.java  
 │  │   ├── BudgetScreenTest.java   
 │  │   ├── BudgetTablePrinterTest.java  
 │  │   ├── ChartPanelTest.java  
 │  │   ├── ContactScreenTest.java  
 │  │   ├── HomeScreenTest.java  
 │  │   ├── LandingScreenTest.java  
 │  │   ├── RegistrationScreenTest.java  
 │  │   ├── ReportScreenTest.java  
 │  │   └── ScenarioScreenTest.java  
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
 ├── target  
 ├── .classpath  
 └── README.md  
	
 ## UML Diagram
  Here is the UML diagram of the project.  https://github.com/AndrianaLiadi/StateBudget/blob/b8680a7efbd7104906331384760c2ebfa761e38e/FINALUML.png  

# Algorithms and Data Structure 
## Data Structure
**Hierarchical Budget Model (Composite Pattern)**- Tree structure for budget organization  
**Immutable Change Tracking** - Historical audit trail with mathematical metrics  
**Scenario Management** - Version control for budget variations  
**Report Generation** - Structured financial documentation  

## Key Algorithims 
**loadFromCSV(String filePath, int year)** ***(BudgetDataLoader)*** :  loads csv files  
**compareBudgets(Budget budgetA, Budget budgetB)** ***(BudgetService)***: compares budgets  
**getTotal()** ***(BudgetItem)***: retrospective calculation of totals  
**applyChanges()** ***(Scenario)***: apply changes on scenarios  
**public String generateSummary(Scenario scenario, List<BudgetChange> changes)** ***(ReportGenerator)*** : genarates reports   

## Data Storage Structures
 **ArrayList BudgetItem** - Sequential access for calculations  
 **List BudgetItem** - Hierarchical List, tree organization of categories/subcategories  
 **List BudgetChange** - Chronological change tracking  

## Design Patterns Implemented
**Composite Pattern (BudgetItem hierarchy)** -Hierachical budget items, enables tree-like structures  
**Prototype Pattern (Cloneable interface)** - supports object duplication  
**Memento-like Pattern (BudgetChange)** - tracks state changes  
**Strategy Pattern** - Extensible comparison and export methods  
**Factory Pattern** - Budget object creation from multiple sources  
**Observer Pattern** - Automatic summary updates on changes  
**Builder Pattern** - Incremental report construction  

## Performance Characteristics
**Space Complexity:** Proportional to data size with streaming optimizations  
**Time Complexity:** Linear for most operations  
**Memory Safety:** Deep copying prevents data corruption  
**Error Handling:** Protected calculations (zero-division prevention)

## Mathematical Operations
**Financial calculations:** Absolute/percentage changes, net impact  
**Statistical aggregation:** Category-wise summation, hierarchical totals  
**Data validation:**  Input cleaning, edge-case handling, error resilience  

# Additional Technical Report 
There are zero **checkstyle** errors   
We used **Java doc** couverege for more prisize exlanation  
We checked our test couverage with **jacoco** reports ( target- classes - index.html)
## Test Coverege 
<img width="1185" height="252" alt="image" src="https://github.com/user-attachments/assets/e6aba4fe-7cf4-4033-a9f8-5c3c9348abd5" />


Here is the promotional video of our application 
https://youtu.be/7u8bsMBlu-E?si=ViH-KmpZqLFJfvfC  
Το παρόν οπτικοακουστικό υλικό (βίντεο) και η παρουσίαση της εργασίας
διατίθενται υπό την άδεια Creative Commons Αναφορά Δημιουργού 4.0 Διεθνές.

The video and presentation content of this project are licensed under a
Creative Commons Attribution 4.0 International License (CC BY 4.0).

To view a copy of this license, visit:
http://creativecommons.org/licenses/by/4.0/

MIT License

Copyright (c) 2026 AndrianaLiadi

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.




