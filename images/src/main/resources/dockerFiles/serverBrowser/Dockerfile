FROM openjdk:25-jdk-slim
WORKDIR /app

ADD jars/* /app/libs/

# Copy the main jar file into the container
ADD jars/ServerBrowser.jar /app/ServerBrowser.jar
ADD jars/GameServer.jar /app/GameServer.jar

ADD scripts/java-spawner /app/spawner
ADD scripts/healthcheck /app/healthcheck

EXPOSE 8000-12000

# Configurable env variables
# =============================================================================
# This is the port the HTTP server is listening on.
# ENV HTTP_PORT=8000
# This is the hostname the HTTP server is listening on.
#   See hostname definition at: https://docs.oracle.com/javase/8/docs/api/java/net/InetSocketAddress.html#InetSocketAddress-java.lang.String-int-:~:text=of%20valid%20port%20values.-,InetSocketAddress,-public%C2%A0InetSocketAddress(String
# ENV HTTP_HOSTNAME=0.0.0.0
# =============================================================================

# Required volume
# =============================================================================
# /app/recordings/
# This stores the actions of past games as JSON.
# If not provided past games are volatile.
# =============================================================================

# Command to run the application
CMD ["java", "-cp", "/app/ServerBrowser.jar:libs/*", "com.briscagame.serverBrowser.ServerBrowser"]