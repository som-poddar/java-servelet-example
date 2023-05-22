m-build:
	mvn package
	java -cp target/java-prom-exporter-example-1.0-SNAPSHOT.jar com.mycompany.app.App
