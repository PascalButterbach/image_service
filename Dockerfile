FROM adoptopenjdk/openjdk15

WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./

RUN chmod +x ./mvnw
RUN ./mvnw dependency:go-offline

COPY src ./src

EXPOSE 9002

CMD ["./mvnw", "spring-boot:run"]