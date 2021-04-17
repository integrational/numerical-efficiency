FROM openjdk:8 AS builder
# install kotlin compiler of the defined version
ENV v=1.4.32
RUN apt update && apt upgrade -y && apt install curl unzip -y
WORKDIR /opt
RUN curl -o kc.zip -L https://github.com/JetBrains/kotlin/releases/download/v$v/kotlin-compiler-$v.zip
RUN unzip kc.zip # unzips to kotlinc directory
ENV PATH=/opt/kotlinc/bin:$PATH
# compile and package app
WORKDIR /app
COPY . .
RUN kotlinc -d app.jar -include-runtime *.kt

FROM openjdk:8
WORKDIR /app
COPY --from=builder /app/app.jar .
CMD ["java", "-jar", "app.jar"]
