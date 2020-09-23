package chapter4

import org.apache.spark.ml.feature.{RegexTokenizer, Tokenizer}
import org.apache.spark.sql.functions.udf
import org.apache.spark.sql.{Column, SparkSession}

object TokenizerDemo extends {

//  def udf(stringsToInt: Seq[String] => Int) = ???

  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("TokenizerDemo")
      .master("local")
      .getOrCreate()
    val sentenceData = spark.createDataFrame(Seq(
      (0,"Hi I heard about Spark"),
      (0,"I wish Java cloud use case classes"),
      (1,"logistic regression model are neat")
    )).toDF("label","sentence")

    val tokenizer = new Tokenizer()
      .setInputCol("sentence")
      .setOutputCol("words")

    val regexTokenizer = new RegexTokenizer()
      .setInputCol("sentence")
      .setOutputCol("words")
      .setPattern("\\W")

//    val countTokens = udf { (words: Seq[String]) => words.length }
    //自定义UDF函数
    val code = (arg:String) =>{
      arg.split(" ").length
    }
    val addCol = udf(code)

    val tokenized = tokenizer.transform(sentenceData)
    tokenized.select("sentence", "words")
      .withColumn("tokens", addCol(sentenceData("sentence"))).show(false)

    val regexTokenized = regexTokenizer.transform(sentenceData)
    regexTokenized.select("sentence","words")
      .withColumn("tokens",addCol(sentenceData("sentence"))).show(false)
  }

}
