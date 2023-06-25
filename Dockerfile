FROM openjdk:17
ADD target/login-0.0.1-SNAPSHOT.jar .
EXPOSE 8080
CMD java -jar login-0.0.1-SNAPSHOT.jar