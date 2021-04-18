FROM openjdk:8 AS builder
# install the latest available version of the kotlin compiler
RUN apt update && apt upgrade -y && apt install curl unzip -y
WORKDIR /opt
RUN export LOC="$(curl -IBs https://github.com/JetBrains/kotlin/releases/latest | sed 's|[^[:print:]]||g' | grep -i 'location:')" \
 && export VER=$(echo $LOC | sed 's|.*tag/v||g')                                                                                  \
 && echo "Downloading Kotlin compiler version $VER"                                                                               \
 && curl -o kc.zip -L "https://github.com/JetBrains/kotlin/releases/download/v$VER/kotlin-compiler-$VER.zip"
RUN unzip kc.zip # unzips to kotlinc directory
ENV PATH=/opt/kotlinc/bin:$PATH
# show compiler version
RUN kotlinc -version
# compile and package app
WORKDIR /app
COPY . .
RUN kotlinc -d app.jar -include-runtime *.kt

FROM openjdk:8
WORKDIR /app
COPY --from=builder /app/app.jar .
CMD ["java", "-jar", "app.jar"]
