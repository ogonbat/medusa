java_version:
	java -version

run:
	mvn install
	cd node
	mvn exec:exec