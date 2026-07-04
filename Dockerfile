FROM eclipse-temurin:21-jre

WORKDIR /var/lib/jenkins/workspace/spotify-back

RUN useradd --system --uid 10001 appuser

COPY --chown=appuser:appuser target/kubernetes-performance-back.jar app.jar

USER appuser

EXPOSE 8080

ADD target/kubernetes-performance-back.jar kubernetes-performance-back.jar

ENTRYPOINT ["java", "-jar", "kubernetes-performance-back.jar"]


