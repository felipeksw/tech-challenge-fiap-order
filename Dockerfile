FROM amazoncorretto:21

COPY . /app

WORKDIR /app

RUN chmod 755 ./gradlew

RUN ./gradlew clean build \
    && mv build/libs/order-*.jar /app/app.jar \
    && rm -rf build

EXPOSE 8080

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-Dspring.profiles.active=prd", "-Xmx512m", "-Xms512m", "-jar", "app.jar"]