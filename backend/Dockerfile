FROM hseeberger/scala-sbt AS builder
COPY . /usr/src/musicbox-backend
WORKDIR /usr/src/musicbox-backend
RUN sbt universal:packageBin

FROM openjdk:8-jre-slim
COPY --from=builder /usr/src/musicbox-backend/target/universal /usr/bin/musicbox-backend
WORKDIR /usr/src/musicbox-backend/musicbox-backend-0.1/bin
ENTRYPOINT ["/usr/bin/bash", "musicbox-backend"]
