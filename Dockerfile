FROM ubuntu:22.04

ENV DEBIAN_FRONTEND=noninteractive

RUN apt-get update && apt-get install -y \
    openjdk-17-jdk \
    wget \
    && rm -rf /var/lib/apt/lists/*

RUN wget https://download2.gluonhq.com/openjfx/17.0.2/openjfx-17.0.2_linux-x64_bin-sdk.zip -O /tmp/openjfx.zip && \
    unzip -q /tmp/openjfx.zip -d /opt/ && \
    mv /opt/javafx-sdk-17.0.2 /opt/javafx && \
    rm /tmp/openjfx.zip

WORKDIR /app

COPY target/shopping-cart-app-1.0-SNAPSHOT.jar app.jar

ENV JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
ENV PATH=$PATH:/opt/javafx/lib

ENV LANG=en_US.UTF-8
ENV LANGUAGE=en_US:en
ENV LC_ALL=en_US.UTF-8

ENTRYPOINT ["java", "--module-path", "/opt/javafx/lib", "--add-modules", "javafx.controls,javafx.fxml", "-jar", "app.jar"]
