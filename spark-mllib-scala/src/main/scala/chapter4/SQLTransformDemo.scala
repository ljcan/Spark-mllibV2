package chapter4

import org.apache.spark.ml.feature.SQLTransformer
import org.apache.spark.sql.SparkSession

object SQLTransformDemo {

  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder()
      .master("local")
      .appName("SQLTransformDemo")
      .getOrCreate()

    val frame = spark.createDataFrame(Seq(
      (0, 1.0, 3.0),
      (1, 2.0, 4.0),
      (2, 5.0, 3.0)
    )).toDF("id", "v1", "v2")

    /**
     * __THIS__ 表示输入数据的基础表
     */
    val sQLTransformer = new SQLTransformer().setStatement("" +
      "SELECT *,(v1+v2)  as v3,(v1*v2) as v4 FROM __THIS__")

    sQLTransformer.transform(frame).show()

    /**
     * +---+---+---+---+----+
     * | id| v1| v2| v3|  v4|
     * +---+---+---+---+----+
     * |  0|1.0|3.0|4.0| 3.0|
     * |  1|2.0|4.0|6.0| 8.0|
     * |  2|5.0|3.0|8.0|15.0|
     * +---+---+---+---+----+
     */



  }

}
