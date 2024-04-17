FROM openjdk:11
COPY target/agent*.jar /usr/src/agent.jar
COPY src/main/resources/application.properties /opt/conf/application.properties
CMD ["java", "-jar", "/usr/src/agent.jar", "--spring.config.location=file:/opt/conf/application.properties"]