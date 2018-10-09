FROM openjdk:10
LABEL maintainer="georg.wittberger@gmail.com"
ARG JAR_FILE
COPY $JAR_FILE /myskills/myskills-server.jar
VOLUME ["/myskills/config", "/myskills/tmp", "/myskills/logs"]
EXPOSE 8080
WORKDIR /myskills
ENTRYPOINT ["java", "-jar", "myskills-server.jar", "--spring.profiles.active=prod"]
