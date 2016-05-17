import org.apache.kafka.clients.consumer.KafkaConsumer
import scala.collection.JavaConversions._

object Consumer {

  def main(args: Array[String]) {

    val consumer = {
      val props = KafkaUtil.getProperties(servers = "localhost:9092", groupId = "test01")
      new KafkaConsumer[String, String](props)
    }

    // Subscribe to one topic
    consumer.subscribe(List("test"))

    // Go to beginning
    KafkaUtil.seekToBeginning(consumer)

    // Process records
    KafkaUtil.forEachRecord(consumer) { record =>
      println(s"offset = ${record.offset()}, key = ${record.key()}, value = ${record.value}")
    }

  }

}
