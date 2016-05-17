import java.util.Properties
import org.apache.kafka.clients.consumer.{ConsumerRecord, KafkaConsumer}
import org.apache.kafka.common.errors.WakeupException
import scala.collection.JavaConversions._

object KafkaUtil {

  // Basic properties
  def getProperties(servers: String, groupId: String): Properties = {
    val props = new Properties()
    props.put("bootstrap.servers", servers)
    props.put("group.id", groupId)
    props.put("enable.auto.commit", "true")
    props.put("auto.commit.interval.ms", "1000")
    props.put("session.timeout.ms", "30000")
    props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    props
  }

  // Set offset 0 for assigned partitions
  def seekToBeginning(consumer: KafkaConsumer[String, String]): Unit = {
    // Call poll() once to make sure we join a consumer group and get assigned partitions
    // Then seek() to the correct offset in the partitions we are assigned to
    consumer.poll(0)
    consumer.assignment().foreach { tp =>
      consumer.seekToBeginning(tp)
    }
  }

  // Apply func for each record returned by the consumer
  def forEachRecord(consumer: KafkaConsumer[String, String])(func: (ConsumerRecord[String, String]) => Unit): Unit = {

    // It is very important that before exiting the thread you will call consumer.close()
    // This will do any last commits if needed and will send the group coordinator a message that the consumer is leaving the group
    // Rebalancing will be triggered immediately and you won’t need to wait for the session to time out

    // A shutdown hook will be used to generate a WakeupException and be able to close the consumer

    Util.addShutdownHook(Thread.currentThread()) { () =>
      consumer.wakeup()
    }

    try {
      while (true) {
        val records = consumer.poll(100)
        records.foreach(func)
      }
    }
    catch {
      case e: WakeupException => /* shutting down, ignore */
    }
    finally {
      // Close consumer
      consumer.close()
      println("Consumer closed")
    }

  }

}
