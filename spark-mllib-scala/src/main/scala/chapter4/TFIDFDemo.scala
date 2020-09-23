package chapter4

import org.apache.spark.ml.feature.{HashingTF, IDF, Tokenizer}
import org.apache.spark.sql.SparkSession

object TFIDFDemo {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("TFIDFDemo")
      .master("local")
      .getOrCreate()
    val sentenceData = spark.createDataFrame(Seq(
      (0,"Hi I heard about Spark"),
      (0,"I wish Java cloud use case classes"),
      (1,"logistic regression model are neat")
    )).toDF("label","sentence")

    val tokenizer = new Tokenizer().setInputCol("sentence").setOutputCol("words")
    //将句子分为单个词语
    val wordsData = tokenizer.transform(sentenceData)

    val hashingTF = new HashingTF().setInputCol("words").setOutputCol("rawFeatures").setNumFeatures(20)
    //将单词转换为特征向量
    val featureizeData = hashingTF.transform(wordsData)

    val idf = new IDF().setInputCol("rawFeatures").setOutputCol("features")
    val idfModel = idf.fit(featureizeData)

    val frame = idfModel.transform(featureizeData)

    frame.select("label","features").take(3).foreach(println)

    /**
     * [0,(20,[6,8,13,16],[0.28768207245178085,0.6931471805599453,0.28768207245178085,0.5753641449035617])]
     * [0,(20,[2,7,13,15,16],[0.28768207245178085,2.0794415416798357,0.28768207245178085,0.6931471805599453,0.28768207245178085])]
     * [1,(20,[2,3,4,6,19],[0.28768207245178085,0.6931471805599453,0.6931471805599453,0.28768207245178085,0.6931471805599453])]
     */


  }

}
