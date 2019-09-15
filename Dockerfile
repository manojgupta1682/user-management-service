FROM openjdk:8-jdk-alpine
COPY ./target/user-management-services-0.0.1-SNAPSHOT.jar /usr/app/
WORKDIR /usr/app
RUN sh -c 'touch user-management-services-0.0.1-SNAPSHOT.jar'
ENTRYPOINT ["java","-jar","user-management-services-0.0.1-SNAPSHOT.jar"]