# Spring Boot Translation Service with Kafka

## Overview

This project is a Spring Boot application that provides a translation service using Kafka for asynchronous message processing. The service accepts translation requests via a REST API, processes them using the LibreTranslate API, and returns the results through Kafka topics.

The system consists of the following components:

- **REST API**: Accepts translation requests from clients
- **Kafka Producer**: Sends translation requests to a Kafka topic
- **Kafka Consumer**: Processes translation requests and invokes the translation service
- **Translation Service**: Integrates with LibreTranslate API to perform the actual translations
- **Error Handling**: Failed translations are sent to a dead-letter topic for later processing

## Architecture

The system flow is as follows:

1. Client sends a translation request to the REST API
2. The API forwards the request to the `translation-requests` Kafka topic
3. A Kafka consumer processes the request by calling the translation service
4. The translated text is published to the `translation-responses` Kafka topic
5. If an error occurs, the message is sent to the `translation-errors` topic or a dead-letter topic

## Prerequisites

- Java 11+
- Maven 3.6+
- Docker and Docker Compose (for running Kafka)
- LibreTranslate instance (self-hosted or public)

## Setup Instructions

### 1. Clone the Repository

```bash
git clone https://github.com/yourusername/translation-service.git
cd translation-service
```

### 2. Set Up Kafka

You can use Docker Compose to set up Kafka quickly:

Create a `docker-compose.yml` file in the project root:

```yaml
version: '3'
services:
  zookeeper:
    image: confluentinc/cp-zookeeper:latest
    environment:
      ZOOKEEPER_CLIENT_PORT: 2181
      ZOOKEEPER_TICK_TIME: 2000
    ports:
      - "2181:2181"

  kafka:
    image: confluentinc/cp-kafka:latest
    depends_on:
      - zookeeper
    ports:
      - "9092:9092"
    environment:
      KAFKA_BROKER_ID: 1
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://localhost:9092
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
      KAFKA_AUTO_CREATE_TOPICS_ENABLE: "true"
```

Start the Kafka environment:

```bash
docker-compose up -d
```

Create the required Kafka topics:

```bash
docker exec -it <kafka-container-id> kafka-topics --bootstrap-server localhost:9092 --create --topic translation-requests --partitions 1 --replication-factor 1
docker exec -it <kafka-container-id> kafka-topics --bootstrap-server localhost:9092 --create --topic translation-responses --partitions 1 --replication-factor 1
docker exec -it <kafka-container-id> kafka-topics --bootstrap-server localhost:9092 --create --topic translation-errors --partitions 1 --replication-factor 1
docker exec -it <kafka-container-id> kafka-topics --bootstrap-server localhost:9092 --create --topic translation-requests.DLT --partitions 1 --replication-factor 1
```

### 3. Set Up LibreTranslate

Option 1: Use Docker to run LibreTranslate:

```bash
docker run -it --rm -p 5000:5000 libretranslate/libretranslate
```

Option 2: Install LibreTranslate directly. Follow the instructions on the [LibreTranslate GitHub repository](https://github.com/LibreTranslate/LibreTranslate).

### 4. Configure the Application

Edit `src/main/resources/application.properties` to configure your application:

```properties
# Server Configuration
server.port=8080

# Kafka Configuration
spring.kafka.bootstrap-servers=localhost:9092
spring.kafka.consumer.group-id=translation-group
spring.kafka.consumer.auto-offset-reset=earliest

# LibreTranslate Configuration
libretranslate.url=http://localhost:5000/translate
# If your LibreTranslate instance requires an API key
# libretranslate.api-key=your-api-key
```

### 5. Add Maven Dependencies

Add the LibreTranslate Java client dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>space.dynomake</groupId>
    <artifactId>libretranslate</artifactId>
    <version>2.0.0</version>
</dependency>
```

### 6. Build and Run the Application

```bash
mvn clean install
mvn spring-boot:run
```

## Usage

### Send a Translation Request

Use the REST API to send a translation request:

```bash
curl -X POST http://localhost:8080/api/translate \
  -H "Content-Type: application/json" \
  -d '{
    "text": "Hello, world!",
    "sourceLanguage": "en",
    "targetLanguage": "es"
  }'
```

You should receive a response with a request ID, for example:

```json
{
  "message": "Translation request accepted with ID: 123e4567-e89b-12d3-a456-426614174000"
}
```

### Consume Translation Results

To consume the translation results, you need to set up a consumer for the `translation-responses` Kafka topic. You could implement a separate microservice or use a Kafka CLI tool to see the results:

```bash
docker exec -it <kafka-container-id> kafka-console-consumer --bootstrap-server localhost:9092 --topic translation-responses --from-beginning
```

### Monitoring Errors

To monitor translation errors, consume messages from the `translation-errors` and DLT topics:

```bash
docker exec -it <kafka-container-id> kafka-console-consumer --bootstrap-server localhost:9092 --topic translation-errors --from-beginning
docker exec -it <kafka-container-id> kafka-console-consumer --bootstrap-server localhost:9092 --topic translation-requests.DLT --from-beginning
```

## Advanced Configuration

### Scaling Consumers

To handle higher loads, you can increase the number of consumer instances by modifying the `ConcurrentKafkaListenerContainerFactory` bean in `KafkaConfig.java`:

```java
@Bean
public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(...) {
    // ...
    factory.setConcurrency(3); // Set number of consumer threads
    // ...
    return factory;
}
```

### Security Configuration

For production environments, consider securing your Kafka cluster. Update your Kafka configuration in `KafkaConfig.java` to include security settings:

```java
configProps.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_SSL");
configProps.put(SaslConfigs.SASL_MECHANISM, "PLAIN");
configProps.put(SaslConfigs.SASL_JAAS_CONFIG, "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"your-username\" password=\"your-password\";");
```

## Troubleshooting

### Connection to Kafka Failed

- Ensure Kafka is running: `docker ps`
- Check Kafka logs: `docker logs <kafka-container-id>`
- Verify the bootstrap server address in `application.properties`

### LibreTranslate Connection Issues

- Ensure LibreTranslate is running and accessible
- Check that the URL in `TranslationService.java` is correct
- If using an API key, verify it's correctly set

### Messages Not Being Consumed

- Check consumer group status: `docker exec -it <kafka-container-id> kafka-consumer-groups --bootstrap-server localhost:9092 --describe --group translation-group`
- Verify the topics exist: `docker exec -it <kafka-container-id> kafka-topics --bootstrap-server localhost:9092 --list`

## License

[MIT License]

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.
