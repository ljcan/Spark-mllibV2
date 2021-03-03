package chapter3

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.{DataType, IntegerType, StringType, StructField, StructType}

object demo1 {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .config("spark.driver.host","localhost")
      .appName("demo1")
      .master("local")
      .getOrCreate()

    val schema = StructType(Seq(
      StructField("id",IntegerType,false),
      StructField("name",StringType,false),
      StructField("age",IntegerType,false)
    ))
    val table = spark.read.schema(schema).json("/Users/liujunqiang/Downloads/spark-mllib/spark-sql/src/main/scala/data/person.json")
//    val data = spark.read.format("json").load("/Users/liujunqiang/Downloads/spark-mllib/spark-sql/src/main/scala/data/person.json")
    table.createOrReplaceTempView("person")
    spark.sql("select * from person").show()

  }

}
