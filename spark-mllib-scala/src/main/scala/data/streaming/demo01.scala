package data.streaming

import org.apache.spark.sql.SparkSession

object demo01 {

  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder()
      .config("spark.driver.host","localhost")
      .master("local")
      .appName("structStreaming")
      .getOrCreate()

    //引入隐式转换
    import spark.implicits._

    val lines = spark.readStream
      .format("socket")
      .option("host","localhost")
      .option("port",9999)
      .load()
    val words = lines.as[String].flatMap(_.split(" "))
    val wordcounts = words.groupBy("value").count()

    val query = wordcounts.writeStream
      .outputMode("complete")
      .format("console")
      .start()

    query.awaitTermination()
  }

}
