FROM openjdk:11
LABEL maintainer="georg.wittberger@gmail.com"
ARG JAR_FILE
ARG LIQUIGRAPH_CHANGELOG
ENV LIQUIGRAPH_CHANGELOG ${LIQUIGRAPH_CHANGELOG:-classpath:/db/liquigraph/changelog.xml}
COPY $JAR_FILE /skoop/skoop-server.jar
VOLUME ["/skoop/config", "/skoop/tmp", "/skoop/logs"]
EXPOSE 8080
WORKDIR /skoop
ENTRYPOINT ["java", "-jar", "skoop-server.jar", "--spring.profiles.active=prod", "--liquigraph.change-log=${LIQUIGRAPH_CHANGELOG}"]
