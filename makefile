
build: OlympicsProject.class

OlympicsProject.class: OlympicsProject.java
	javac OlympicsProject.java

run: OlympicsProject.class 
	java -cp .:mssql-jdbc-12.8.1.jre11.jar OlympicsProject

clean:
	rm OlympicsProject.class