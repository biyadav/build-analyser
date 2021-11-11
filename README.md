# build-analyser   

This Application is  a standalone java App  which  reads  a file in predefined format (sample is with name logFile.txt)  and 
for each event in that file  calculates the execution time and  saves the details in  HSQLDB  File  .

This project uses :
 * Java 8
 * Log4j (for naive logging  which creates log  file in same directory from whare jar executed )
 * hsqldb for persistent store
 * maven as build and dependency manager


Solution Summary :

Since the file is relatively big so reading all content in one go can cause memory issue .

* Using two threads  one to read another to process and insert in db and clear the already processed data from memory.

* A Reader thread using buffered stream   and adding read line to a  queue '

* Another Thread processor is reading from the same queue  and stores the read line to a temp Map till the corresponding
  FINISHED Record is read (Problem states that  Record for state FINISHED may come later at any number ).
  After we have both  FINISHED and STARTED  STATE for given id and host (id can be same on diff host so id+ host unique)
  calculating the execution time and adding to a list and removing the entry from map for given host+ id.
  once we have as many elements in List as the batchSize (hardcoded , should have read from property) , insert them 
  all in batch to hsqldb.


************************
To build Project
************************
From directory where pom.xml is present Run :    mvn clean package 
After build it will create two jars  . Jar with name build-analyser-0.0.1-SNAPSHOT-jar-with-dependencies.jar is executable jar with all dependencies
(its not springboot Application ).


**************************
To Run the App:
**************************
run below command in targe directory where jar is created 

java -jar build-analyser-0.0.1-SNAPSHOT-jar-with-dependencies.jar <File complete Path e.g. "C:/STSWorkSpace/build-analyser/target/logFile.txt" >


