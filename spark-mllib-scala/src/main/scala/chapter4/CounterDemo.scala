package chapter4

import org.apache.spark.ml.feature.{CountVectorizer, CountVectorizerModel}
import org.apache.spark.sql.SparkSession

object CounterDemo {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("CounterDemo")
      .master("local")
      .getOrCreate()
    val df = spark.createDataFrame(Seq(
      (0,Array("a","b","c")),
      (1,Array("a","b","b","c","a"))
    )).toDF("id","words")
    //从预料库中拟合CountVectorizerModel
    val cvModel:CountVectorizerModel = new CountVectorizer()
      .setInputCol("words")
      .setOutputCol("features")
      .setVocabSize(3)
      .setMinDF(2)
      .fit(df)

    //也可以用先验词汇表定义CountVectorizerModel
    val cvm = new CountVectorizerModel(Array("a","b","c"))
      .setInputCol("words")
      .setOutputCol("features")

    cvModel.transform(df).show(false)

    /**
     * 向量代表每个词汇表中每个词语出现的次数
     * +---+---------------+-------------------------+
     * |id |words          |features                 |
     * +---+---------------+-------------------------+
     * |0  |[a, b, c]      |(3,[0,1,2],[1.0,1.0,1.0])|
     * |1  |[a, b, b, c, a]|(3,[0,1,2],[2.0,2.0,1.0])|
     * +---+---------------+-------------------------+
     */


  }

}
