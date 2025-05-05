FROM openjdk:25-jdk-slim
WORKDIR /app
# Copy the shaded JAR(JAR that contains all dependencies) file into the container
ADD target/brisca-*-shaded.jar /app/brisca.jar
EXPOSE 80

# Configurable env variables
# =============================================================================
# This is the port the HTTP server is listening on.
# ENV HTTP_PORT=8000
# This is the hostname the HTTP server is listening on.
#   See hostname definition at: https://docs.oracle.com/javase/8/docs/api/java/net/InetSocketAddress.html#InetSocketAddress-java.lang.String-int-:~:text=of%20valid%20port%20values.-,InetSocketAddress,-public%C2%A0InetSocketAddress(String
# ENV HTTP_HOSTNAME=0.0.0.0
# =============================================================================

# Command to run the application
CMD ["java", "-jar", "/app/brisca.jar"]