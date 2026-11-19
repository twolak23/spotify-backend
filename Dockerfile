FROM eclipse-temurin:21-jre

WORKDIR /var/lib/jenkins/workspace/spotify-back

RUN useradd --system --uid 10001 appuser

COPY --chown=appuser:appuser target/spotify-backend.jar app.jar

USER appuser

EXPOSE 8080

ADD target/spotify-backend.jar spotify-backend.jar

ENTRYPOINT ["java", "-jar", "spotify-backend.jar"]


