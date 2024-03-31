# 使用 JDK 17 作为基础镜像
FROM eclipse-temurin:17-jdk-focal

# 在容器中创建一个目录来存放应用程序
WORKDIR /app

# 将本地的 JAR 文件复制到容器中的 /app 目录
COPY target/ytls-xmtx-reward-0.1.jar /app/app.jar

# 暴露 Spring Boot 应用程序默认端口
EXPOSE 6666

# 指定容器启动时运行的命令
CMD ["java", "-jar", "app.jar"]
