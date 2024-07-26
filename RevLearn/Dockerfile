
#Looking into Dockerfiles

#from maven:3.3.2-oraclejdk as build
from maven:3.8.4-openjdk-17-slim as build
workdir /theApp
copy pom.xml .
run mvn dependency:go-offline -B
copy src ./src
run mvn package -DskipTests


#from oracle/graalvm-ce:21.3.0-java17
from eclipse-temurin:17-jdk
workdir /theApp

#ERROR: failed to solve: failed to compute cache key:
#failed to calculate checksum of ref 3633481f-e69d-439a-a183-d8a6bb9179f4::9od0mzmyu8sa4f3icbnqbrhvi:
#"/target/RevLearn-0.0.1-SNAPSHOT.jar": not found
run ls
copy --from=build ./target/RevLearn-0.0.1-SNAPSHOT.jar ./RevLearn-0.0.1-SNAPSHOT.jar
expose 8080
entrypoint ["java","-jar","RevLearn-0.0.1-SNAPSHOT.jar"]

