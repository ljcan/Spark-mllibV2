package chapter4

import org.apache.spark.ml.feature.{IndexToString, StringIndexer}
import org.apache.spark.sql.SparkSession

object Index2StringDemo {

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

    val convert = new IndexToString()
      .setInputCol("categoryIndex")
      .setOutputCol("originalCategory")

    val converted = convert.transform(indexed)
    converted.show()

  }

}
