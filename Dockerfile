# Use uma imagem base com JDK e Maven
FROM maven:3.8.3-openjdk-11-slim AS builder

# Copie os arquivos do projeto
COPY . /tools
WORKDIR /tools

# copy pom.xml from context into image
COPY pom.xml /app/pom.xml

# Compilar o projeto
RUN mvn clean install

# Imagem de runtime
FROM openjdk:11-jre-slim

# Copie o arquivo JAR compilado do estágio de construção
COPY --from=builder /tools/target/tools-0.0.1-SNAPSHOT.jar /app/tools-0.0.1-SNAPSHOT.jar

# Expor a porta que sua aplicação está ouvindo
EXPOSE 8080

# Comando de execução da aplicação
CMD ["java", "-jar", "/app/tools-0.0.1-SNAPSHOT.jar"]