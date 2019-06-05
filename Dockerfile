FROM maven:3.5-jdk-8 
WORKDIR /usr/src/app
COPY . .
RUN mvn -f ./pom.xml clean package

EXPOSE 8080
ENTRYPOINT ["java","-jar","target/loginweb-1.0-SNAPSHOT-jar-with-dependencies.jar"]
