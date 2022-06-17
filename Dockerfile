FROM openjdk:17-slim-bullseye as build

RUN apt-get update && apt-get -y install curl gnupg

RUN echo "deb https://repo.scala-sbt.org/scalasbt/debian all main" | tee /etc/apt/sources.list.d/sbt.list
RUN echo "deb https://repo.scala-sbt.org/scalasbt/debian /" | tee /etc/apt/sources.list.d/sbt_old.list
RUN curl -sL "https://keyserver.ubuntu.com/pks/lookup?op=get&search=0x2EE0EA64E40A89B84B2DF73499E82A75642AC823" | apt-key add
RUN apt-get update && apt-get -y install sbt

WORKDIR /app

COPY . .

RUN sbt clean assembly

FROM openjdk:17-slim-bullseye as deploy

WORKDIR /app

COPY ./scripts/entrypoint.sh ./entrypoint.sh

COPY --from=build /app/target/scala-2.13/open-report-api.jar open-report-api.jar

RUN chmod +s ./entrypoint.sh

ENTRYPOINT ["./entrypoint.sh"]
