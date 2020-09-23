package chapter4

import org.apache.spark.ml.feature.Binarizer
import org.apache.spark.sql.SparkSession

/**
 * 二值化，即通过设置阔值，将连续型的特征转化为两个值。 大于阔值为 1，否则为 0
 */
object BinarizerDemo {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("CounterDemo")
      .master("local")
      .getOrCreate()

    val df = spark.createDataFrame(Seq(
      (0,0.1),
      (1,0.8),
      (2,0.3)
    )).toDF("id","feature")

    val binarizer = new Binarizer().setInputCol("feature")
      .setOutputCol("binarizer_feature")
        .setThreshold(0.5)        //设置阀值

    binarizer.transform(df).show(false)

    /**
     * +---+-------+-----------------+
     * |id |feature|binarizer_feature|
     * +---+-------+-----------------+
     * |0  |0.1    |0.0              |
     * |1  |0.8    |1.0              |
     * |2  |0.3    |0.0              |
     * +---+-------+-----------------+
     */



  }

}
