package chapter13

import breeze.linalg.DenseVector
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.regression.{LabeledPoint, StreamingLinearRegressionWithSGD}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

object StreamingLinearRegressionDemo {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
      .setMaster("local")
      .setAppName("StreamingLinearRegressionDemo")
    val sc = new SparkContext(conf)
    val ssc = new StreamingContext(sc,Seconds(3))
    val stream = ssc.textFileStream("/Users/liujunqiang/Downloads/spark-mllib/spark-mllib-scala/src/main/scala/data/streaming")

    val numFeatures = 11
    val zeroVector = DenseVector.zeros[Double](numFeatures)
    val model = new StreamingLinearRegressionWithSGD()
      .setInitialWeights(Vectors.dense(zeroVector.data))
      .setNumIterations(20)
      .setRegParam(0.8)
      .setStepSize(0.01)

    //创建一个含标签的数据流
    val leabelStream = stream.map{
      line =>
        val split = line.split(";")
        val y = split(11).toDouble
        val features = split.slice(0,11).map(_.toDouble)
        LabeledPoint(label = y,features = Vectors.dense(features))
    }
    //在数据流上训练测试模型
    model.trainOn(leabelStream)
    model.predictOnValues(leabelStream.map(l=>(l.label,l.features))).print()
    println("=====================")

    ssc.start()
    ssc.awaitTermination()

  }

}
