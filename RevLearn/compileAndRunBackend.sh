
set -e
clear
echo "DB_NAME $DB_NAME"

echo "Shell script: Compiling into jar using maven"
mvn package -DskipTests

#application.properties is the default spring profile
#application-h2.properties is our own custom made profile
#application-server.properties could be another

echo "Shell script: Running the jar"
java -jar target/RevLearn-0.0.1-SNAPSHOT.jar --spring.profiles.active=h2