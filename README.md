# kafka-consumer

Basic consumer for Kafka.

#### Features

- It will create some basic properties needed by the consumer
- It will subscribe to a `test` topic
- It will reset the offset to zero to consume from the very beginning
- It will process each record available, printing its offset, key, and value

#### Usage

- Follow the instructions at [http://kafka.apache.org/documentation.html#quickstart](http://kafka.apache.org/documentation.html#quickstart) to:
    - Start Kafka
    - Create a `test` topic
    - Send some messages
- Clone this repository:
    - `git clone https://github.com/rferrerme/kafka-consumer.git`
- Run:
    - `sbt`
- Once in "sbt", use `run` to execute the consumer code
- Use `CTRL+C` to exit

#### Notes

- `KafkaUtil.forEachRecord` includes a "shutdown hook" to make sure that the consumer is closed. That will generate a `java.lang.IllegalStateException: Shutdown in progress` exception if `sbt run` is used instead of doing it in two steps (first `sbt`, then `run`)
- Reusable code has been extracted to `KafkaUtil` and `Util` objects