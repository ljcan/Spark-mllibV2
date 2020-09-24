package chapter4

import org.apache.spark.ml.feature.StopWordsRemover
import org.apache.spark.sql.SparkSession

/**
 * 去除停止词
 */
object StopWordRemoverDemo {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder()
      .appName("StopWordRemoverDemo")
      .master("local")
      .getOrCreate()

    val data = spark.createDataFrame(Seq(
      (0,Seq("i","saw","the","red","head")),
      (0,Seq("had","a","he","task"))
    )).toDF("id","raw")

    val filtered = new StopWordsRemover()
      .setInputCol("raw")
      .setOutputCol("filtered")

    filtered.transform(data).show(false)

    /**
     * +---+------------------------+----------------+
     * |id |raw                     |filtered        |
     * +---+------------------------+----------------+
     * |0  |[i, saw, the, red, head]|[saw, red, head]|
     * |0  |[had, a, he, task]      |[task]          |
     * +---+------------------------+----------------+
     */
  }

}
