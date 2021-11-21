This project demonstrates how to use kafka from java with the help of [quarkus](https://quarkus.io/guides/kafka)

It demonstrates the following use-cases:
- http rest endpoint, send the received param to `foods` topic
- peridically sends random key:value to `weather` topic
- consums messages from `animals` topics, logs to stdout

## Kafka in local env

You can use [redpanda](https://vectorized.io/) which a lightweght kafka api compatible tool (no zookeper).

```
docker compose up -d kafka
```

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./mvnw compile quarkus:dev
```

## watching the topics 

```
docker compose logs -f weather animals foods
```

or manually from dev env:
```
kafka-console-consumer.sh \
  --bootstrap-server 127.0.0.1:9092 \
  --topic weather \
  --value-deserializer org.apache.kafka.common.serialization.IntegerDeserializer \
  --property print.key=true
```

## Creating a native executable

You can create a native executable using: 
```shell script
mvn package -Pnative
```
