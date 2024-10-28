FROM eclipse-temurin:11-jdk AS build

# Install sbt
RUN apt-get update && \
    apt-get install -y curl && \
    curl -L -o sbt-1.9.7.deb https://repo.scala-sbt.org/scalasbt/debian/sbt-1.9.7.deb && \
    dpkg -i sbt-1.9.7.deb && \
    apt-get update && \
    apt-get install -y sbt && \
    rm sbt-1.9.7.deb && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

ENV RATE_LIMIT=5

WORKDIR /app

COPY build.sbt .
COPY project project
COPY src src

RUN sbt clean compile assembly

FROM eclipse-temurin:11-jre-jammy

WORKDIR /app

COPY --from=build /app/target/scala-2.13/*.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]