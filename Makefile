g-build:
	gradle clean
	gradle installDist

m-build:
	mvn package
	java -jar target/java_simple-1.0-SNAPSHOT-jar-with-dependencies.jar
