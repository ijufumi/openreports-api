### This Dockerfile is for production
FROM eclipse-temurin:21-jdk-jammy

RUN apt-get update && apt-get -y install curl gnupg postgresql-client

RUN echo "deb https://repo.scala-sbt.org/scalasbt/debian all main" | tee /etc/apt/sources.list.d/sbt.list \
&& echo "deb https://repo.scala-sbt.org/scalasbt/debian /" | tee /etc/apt/sources.list.d/sbt_old.list \
&& curl -sL "https://keyserver.ubuntu.com/pks/lookup?op=get&search=0x2EE0EA64E40A89B84B2DF73499E82A75642AC823" | apt-key add \
&& apt-get update && apt-get -y install sbt

ENV APP_DIR /app
WORKDIR $APP_DIR

COPY build.sbt .
COPY scripts ./scripts
COPY src ./src
COPY project ./project
COPY reports ./reports

RUN sbt package

# TODO: need to change for production
ENTRYPOINT ["bash", "/app/scripts/entrypoint.sh"]
