FROM openjdk:8 AS builder
# install sbt
RUN echo "deb https://repo.scala-sbt.org/scalasbt/debian all main" > /etc/apt/sources.list.d/sbt.list                         \
 && echo "deb https://repo.scala-sbt.org/scalasbt/debian /"        > /etc/apt/sources.list.d/sbt_old.list                     \
 && curl -sL "https://keyserver.ubuntu.com/pks/lookup?op=get&search=0x2EE0EA64E40A89B84B2DF73499E82A75642AC823" | apt-key add \
 && apt update && apt upgrade -y && apt install sbt -y
# show sbt version (can't run in /)
WORKDIR /tmp
RUN sbt version
# compile and package app
WORKDIR /app
COPY . .
RUN sbt assembly

FROM openjdk:8
WORKDIR /app
COPY --from=builder /app/target/scala-3*/square-root-scala3-assembly-*.jar ./app.jar
CMD ["java", "-ea", "-jar", "app.jar"]
