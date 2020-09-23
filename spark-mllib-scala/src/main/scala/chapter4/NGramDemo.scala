package chapter4

import org.apache.spark.ml.feature.{NGram, Tokenizer}
import org.apache.spark.sql.SparkSession

object NGramDemo {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder()
      .master("local")
      .appName("NGramDemo")
      .getOrCreate()
    val sentenceData = spark.createDataFrame(Seq(
      (0,"Hi I heard about Spark"),
      (0,"I wish Java cloud use case classes"),
      (1,"logistic regression model are neat")
    )).toDF("id","sentence")

    val tokenizer = new Tokenizer()
      .setInputCol("sentence")
      .setOutputCol("words")
    val tokenized = tokenizer.transform(sentenceData)

    val ngram = new NGram().setN(3).setInputCol("words").setOutputCol("n-gram")
    ngram.transform(tokenized).select("n-gram").show(false)

    /**
     * +--------------------------------------------------------------------------------+
     * |n-gram                                                                          |
     * +--------------------------------------------------------------------------------+
     * |[hi i heard, i heard about, heard about spark]                                  |
     * |[i wish java, wish java cloud, java cloud use, cloud use case, use case classes]|
     * |[logistic regression model, regression model are, model are neat]               |
     * +--------------------------------------------------------------------------------+
     */


  }

}
