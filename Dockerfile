FROM ubuntu:latest
USER root
RUN apt-get update
RUN apt-get -y install curl gnupg
RUN curl -fsSL https://deb.nodesource.com/setup_19.x | bash -
RUN apt-get -y install nodejs
RUN npm install newman -g
RUN apt install openjdk-17-jdk openjdk-17-jre -y
RUN mkdir -p /ctk
RUN mkdir -p /ctk/config/tnt/v2/
RUN mkdir -p /ctk/postman-collection
WORKDIR /ctk
COPY config/tnt/v2/EventSubscription.json /ctk/config/tnt/v2/
COPY config/application.properties /ctk/config/application.properties
COPY suitexmls /ctk/suitexmls
COPY postman-collection /ctk/postman-collection/
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN chmod +x ./mvnw
RUN ./mvnw dependency:go-offline
COPY src ./src
RUN ./mvnw clean package spring-boot:repackage
COPY target/dcsa_ctk_provider-0.0.1.jar /ctk
ENTRYPOINT ["java","-jar","/ctk/dcsa_ctk_provider-0.0.1.jar"]
# For debug use folloiwng ENTRYPOINT or  CMD
#ENTRYPOINT ["java","-agentlib:jdwp=transport=dt_socket,address=*:5005,server=y,suspend=n","-Djava.security.egd=file:/dev/./urandom","-jar","/ctk/dcsa_ctk_provider-0.0.1.jar"]
#CMD ["./mvnw", "spring-boot:run"]