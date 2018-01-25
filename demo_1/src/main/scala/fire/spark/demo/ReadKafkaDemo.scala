package fire.spark.demo

import org.apache.spark.streaming.StreamingContext
import org.fire.spark.streaming.core.FireStreaming
import org.fire.spark.streaming.core.plugins.kafka.KafkaDirectSource

/**
  * Created by guoning on 2018/1/25.
  *
  */
object ReadKafkaDemo extends FireStreaming {
  /**
    * 处理函数
    *
    * @param ssc
    */
  override def handle(ssc: StreamingContext): Unit = {

    val source = new KafkaDirectSource[String, String](ssc)

    val logs = source.getDStream[String](_.value())

    logs.foreachRDD((rdd, time) => {
      rdd.take(10).foreach(println)
      source.updateOffsets(time.milliseconds)
    })


  }
}
