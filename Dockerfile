FROM eclipse-temurin:17-jdk-jammy

RUN apt-get update && apt-get -y install curl gnupg postgresql-client

RUN echo "deb https://repo.scala-sbt.org/scalasbt/debian all main" | tee /etc/apt/sources.list.d/sbt.list \
&& echo "deb https://repo.scala-sbt.org/scalasbt/debian /" | tee /etc/apt/sources.list.d/sbt_old.list \
&& curl -sL "https://keyserver.ubuntu.com/pks/lookup?op=get&search=0x2EE0EA64E40A89B84B2DF73499E82A75642AC823" | apt-key add \
&& apt-get update && apt-get -y install sbt

ENV ROOT_DIR /app
WORKDIR $ROOT_DIR

COPY scripts .
COPY src .
COPY build.sbt .

COPY report /report

RUN sbt compile

RUN chmod +x ./scripts/entrypoint.sh

ENTRYPOINT ["./scripts/entrypoint.sh"]
