package chapter4

import org.apache.spark.ml.feature.VectorIndexer
import org.apache.spark.sql.SparkSession

object VectorIndexerDemo {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("CounterDemo")
      .master("local")
      .getOrCreate()

    val source = spark.read.format("libsvm").load("/Users/liujunqiang/Downloads/spark-mllib/spark-mllib-scala/src/main/scala/data/sample_libsvm_data.txt")
    val vectorIndexer =new VectorIndexer()
      .setInputCol("features")
      .setOutputCol("indexed")
      .setMaxCategories(10)

    val model = vectorIndexer.fit(source)

    val indexerData = model.transform(source)

    indexerData.show()




  }

}
