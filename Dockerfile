FROM openjdk

WORKDIR /app

COPY target/learn-spring-webflux-0.0.1-SNAPSHOT.jar /app/learn-spring-webflux.jar

ENTRYPOINT ["java", "-jar", "learn-spring-webflux.jar"]