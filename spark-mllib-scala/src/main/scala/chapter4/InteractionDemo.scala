package chapter4

import org.apache.spark.ml.feature.{Interaction, VectorAssembler}
import org.apache.spark.sql.SparkSession

/**
 * 交互式
 */
object InteractionDemo {

  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("InteractionDemo")
      .master("local")
      .getOrCreate()

    val data = spark.createDataFrame(Seq(
      (1,1.0,2.0,3.0,4.0,5.0,6.0),
      (2,2.0,3.0,4.0,5.0,6.0,7.0),
      (3,3.0,4.0,5.0,6.0,7.0,8.0),
      (4,4.0,5.0,6.0,7.0,8.0,9.0)
    )).toDF("id1","id2","id3","id4","id5","id6","id7")

    val assembler1 = new VectorAssembler()
      .setInputCols(Array("id2", "id3", "id4"))
      .setOutputCol("vec1")

    val assembled1 = assembler1.transform(data)

    val assembler2 = new VectorAssembler()
      .setInputCols(Array("id5", "id6", "id7"))
      .setOutputCol("vec2")

    val assembled2 = assembler2.transform(assembled1).select("id1","vec1","vec2")

    val interaction = new Interaction()
      .setInputCols(Array("id1", "vec1", "vec2"))
      .setOutputCol("interactionVec")

    val result = interaction.transform(assembled2)
    result.show()








  }

}
