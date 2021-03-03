package demo

import org.apache.spark.sql.SparkSession

object demo02 {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .config("spark.driver.host","localhost")
      .master("local")
      .appName("demo2")
      .getOrCreate()
//    spark.readStream
//      .format("socket")

  }

}
