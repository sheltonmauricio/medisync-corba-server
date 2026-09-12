FROM maven:3.9-eclipse-temurin-21

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean compile
RUN mvn dependency:build-classpath "-Dmdep.outputFile=classpath.txt"

CMD ["sh", "-c", "java -cp target/classes:$(cat classpath.txt) -DOAPort=1050 org.jacorb.naming.NameServer"]