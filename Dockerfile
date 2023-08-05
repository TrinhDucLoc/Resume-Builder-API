FROM registry-pdc.vn.eit.zone/external_platform/adoptopenjdk:8u252-b09-jdk-openj9-0.20.0-bionic
ENV PORT 8080
EXPOSE 8080

COPY target/*.jar /opt/app.jar

WORKDIR /opt

ENTRYPOINT ./import-cert.sh /etc/certs && \
    exec java $JAVA_OPTS -Dspring.profiles.active=$PROFILE -Duser.timezone=Asia/Ho_Chi_Minh -jar app.jar
