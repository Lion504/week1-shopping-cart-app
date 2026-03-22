FROM eclipse-temurin:11-jdk

WORKDIR /app

COPY target/shopping-cart-app-1.0-SNAPSHOT.jar app.jar

ENV LANG=en_US.UTF-8
ENV LANGUAGE=en_US:en
ENV LC_ALL=en_US.UTF-8

ENTRYPOINT ["java", "-jar", "app.jar"]
