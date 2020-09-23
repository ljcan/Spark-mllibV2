package chapter6

import org.apache.spark.mllib.random.RandomRDDs._
import org.apache.spark.{SparkConf, SparkContext}

object RandomDemo {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
      .setAppName("RandomDemo")
      .setMaster("local")
    val sc = new SparkContext(conf)

    //生成100个符合正态分布N（0，1）的RDD，并且分布在10个分区中
    val value = normalRDD(sc, 100L, 10)

    //把生成的随机数转换为N（1，4）正态分布
    val value1 = value.map(x => 1.0 + 2.0 * x)

    value1.foreach(println)


  }

}
