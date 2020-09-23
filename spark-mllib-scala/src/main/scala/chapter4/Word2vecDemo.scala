package chapter4

import org.apache.spark.ml.feature.Word2Vec
import org.apache.spark.sql.SparkSession

/**
 * Word2vec 是一个 Estimator，它采用一系 列代 表文档 的词语来训练 word2vecmodel。
 * 该模型将每个词语映射到 一个固定大小的向量 。
 * word2vecmodel 使用文档中每个词语的平均数 来将文档转换为向量，然后这个向量可以作为预测的特征，来计算文档相似度计算等 。
 */
object Word2vecDemo {

  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("Word2vecDemo")
      .master("local")
      .getOrCreate()
    val documentDF = spark.createDataFrame(Seq(
      ("Hi I heard about Spark".split(" ")),
      ("I wish Java cloud use case classes".split(" ")),
      ("logistic regression model are neat".split(" ")))
      .map(Tuple1.apply)
    ).toDF("text")

    //训练从词到向量的映射
    val word2Vec = new Word2Vec()
      .setInputCol("text")
      .setOutputCol("result")
      .setVectorSize(3)
      .setMinCount(0)

    val model = word2Vec.fit(documentDF)
    val result = model.transform(documentDF)
    result.select("result").take(3).foreach(println)

    /**
     * [[-0.04505853790324182,0.0131652370095253,0.04923109570518136]]
     * [[-0.05214894406630524,-0.031896194991921735,0.0285290889441967]]
     * [[0.035375655815005305,-0.0380957355722785,0.042615814507007604]]
     */

  }

}
