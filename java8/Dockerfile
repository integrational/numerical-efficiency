FROM openjdk:8 AS builder
# show compiler version
RUN javac -version
# compile and package app
WORKDIR /app
COPY . .
RUN javac *.java
RUN jar cvfe app.jar SquareRoot *.class

FROM openjdk:8
WORKDIR /app
COPY --from=builder /app/app.jar .
CMD ["java", "-jar", "app.jar"]
