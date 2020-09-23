package chapter4

import org.apache.spark.ml.feature.ElementwiseProduct
import org.apache.spark.ml.linalg.Vectors
import org.apache.spark.sql.SparkSession

object ElementwiseProductDemo {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("ElementwiseProductDemo")
      .master("local")
      .getOrCreate()

    val frame = spark.createDataFrame(Seq(
      ("a", Vectors.dense(1.0, 2.0, 3.0)),
      ("b", Vectors.dense(4.0, 5.0, 6.0))
    )).toDF("id","vector")

    val trasnformingVector = Vectors.dense(0.0,1.0,2.0)
    val product = new ElementwiseProduct()
      .setScalingVec(trasnformingVector)
      .setInputCol("vector")
      .setOutputCol("result")

    val frame1 = product.transform(frame)

    frame1.show()

    /**
     * +---+-------------+--------------+
     * | id|       vector|        result|
     * +---+-------------+--------------+
     * |  a|[1.0,2.0,3.0]| [0.0,2.0,6.0]|
     * |  b|[4.0,5.0,6.0]|[0.0,5.0,12.0]|
     * +---+-------------+--------------+
     */

  }
}
