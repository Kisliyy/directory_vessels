FROM gradle:7.6.2 as build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon -x test

RUN mkdir /app

FROM bellsoft/liberica-openjre-alpine:11
COPY --from=build /home/gradle/src/build/libs/*.jar /app/directory-vessels.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/directory_vessels.jar"]