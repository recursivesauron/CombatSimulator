#FROM arm64v8/gradle AS build
FROM gradle:6.8.2-jdk15 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle uberJar
FROM openjdk:15-jdk-slim
EXPOSE 5423
EXPOSE 29092
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/CombatSimulator-1.0-SNAPSHOT-uber.jar /app/combat-simulation.jar
ENTRYPOINT ["java", "-jar","/app/combat-simulation.jar"]