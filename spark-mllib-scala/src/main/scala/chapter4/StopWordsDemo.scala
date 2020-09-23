package chapter4

import org.apache.spark.ml.feature.{StopWordsRemover, Tokenizer}
import org.apache.spark.sql.SparkSession

object StopWordsDemo {

  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder()
      .master("local")
      .appName("StopWordsDemo")
      .getOrCreate()
    val sentenceData = spark.createDataFrame(Seq(
      (0,"Hi I heard about Spark"),
      (0,"I wish Java cloud use case classes"),
      (1,"logistic regression model are neat")
    )).toDF("label","sentence")

    val tokenizer = new Tokenizer()
      .setInputCol("sentence")
      .setOutputCol("words")

    val tokenized = tokenizer.transform(sentenceData)
    //去除停止词
    val stopWordsRemover = new StopWordsRemover()
      .setInputCol("words")
      .setOutputCol("removeStopwords")

    stopWordsRemover.transform(tokenized).select("sentence","words","removeStopwords").show(false)



  }

}
