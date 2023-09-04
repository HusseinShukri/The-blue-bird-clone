FROM maven:3.8.3-openjdk-17-slim AS build

WORKDIR /app
COPY pom.xml .
COPY src ./src

RUN mvn package -DskipTests

FROM openjdk:17-jre-slim

RUN useradd -m myuser
USER myuser
WORKDIR /home/myuser

COPY --from=build /app/target/com.twitter.clone-1.0-SNAPSHOT.jar /home/myuser/com.twitter.clone-1.0-SNAPSHOT.jar

EXPOSE 7070

CMD ["java", "-jar", "com.twitter.clone-1.0-SNAPSHOT.jar"]

