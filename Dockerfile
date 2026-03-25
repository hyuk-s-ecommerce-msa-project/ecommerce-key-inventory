FROM eclipse-temurin:21-jdk-jammy
VOLUME /tmp
COPY build/libs/key-inventory-service-1.1.jar KeyInventoryServer.jar
ENTRYPOINT ["java", "-jar", "KeyInventoryServer.jar"]