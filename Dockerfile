# 使用轻量 JDK 8 镜像（一行搞定）
FROM openjdk:8-jre-slim

# 将本地的 JAR 文件复制到容器中
COPY target/purchase-approve-system.jar purchase-approve-system.jar

EXPOSE 58888

# 启动命令
ENTRYPOINT ["java", "-jar", "purchase-approve-system.jar"]