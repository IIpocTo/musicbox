FROM hseeberger/scala-sbt AS builder
COPY . /usr/src/musicbox-backend
WORKDIR /usr/src/musicbox-backend
RUN sbt clean assembly

FROM openjdk:8-jre-slim
COPY --from=builder /usr/src/musicbox-backend-0.1.jar /usr/bin/musicbox-backend.jar
ENTRYPOINT ["java", "-jar", "/usr/bin/musicbox-backend.jar"]
