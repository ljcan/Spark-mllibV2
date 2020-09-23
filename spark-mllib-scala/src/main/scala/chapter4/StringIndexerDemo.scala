package chapter4

import org.apache.spark.ml.feature.StringIndexer
import org.apache.spark.sql.SparkSession

/**
 * 字符串-索引变换
 *
 * 最常见的标签索引获取为0
 */
object StringIndexerDemo {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder()
      .appName("StringIndexerDemo")
      .master("local")
      .getOrCreate()

    val df = spark.createDataFrame(Seq(
      (0,"a"),
      (1,"b"),
      (2,"c"),
      (3,"a"),
      (4,"a"),
      (5,"c")
    )).toDF("id","category")

    val indexer = new StringIndexer()
      .setInputCol("category")
      .setOutputCol("categoryIndex")

    val indexed = indexer.fit(df).transform(df)
    indexed.show()

    /**
     * +---+--------+-------------+
     * | id|category|categoryIndex|
     * +---+--------+-------------+
     * |  0|       a|          0.0|
     * |  1|       b|          2.0|
     * |  2|       c|          1.0|
     * |  3|       a|          0.0|
     * |  4|       a|          0.0|
     * |  5|       c|          1.0|
     * +---+--------+-------------+
     */



  }

}
