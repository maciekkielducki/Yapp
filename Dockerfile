FROM amazoncorretto:21
# Set the working directory in the container
WORKDIR /app

# Copy the jar file to the container
COPY target/Yapp-1.0-SNAPSHOT.jar app.jar

# Make port 8080 available to the world outside this container
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]