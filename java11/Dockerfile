FROM openjdk:11 AS builder
# compile and package app
WORKDIR /app
COPY . .
RUN javac *.java
RUN jar cvfe app.jar SquareRoot *.class

FROM openjdk:11
WORKDIR /app
COPY --from=builder /app/app.jar .
CMD ["java", "-jar", "app.jar"]
