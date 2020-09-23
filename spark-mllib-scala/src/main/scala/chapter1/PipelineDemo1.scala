package chapter1

import org.apache.spark.ml.{Pipeline, PipelineModel}
import org.apache.spark.ml.classification.LogisticRegression
import org.apache.spark.ml.feature.{HashingTF, Tokenizer}
import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.ml.linalg.{Vectors,Vector}

object PipelineDemo1 {

  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder()
      .master("local")
      .appName("PipelineDemo1")
      .getOrCreate()

    val training = spark.createDataFrame(Seq(
      (0L,"a b c d e spark",1.0),
      (1L,"b d",0.0),
      (2L,"spark f g h",1.0),
      (3L,"spark hadoop",0.0)
    )).toDF("id","text","label")

    val tokenizer = new Tokenizer()
      .setInputCol("text")
      .setOutputCol("words")
    val hasingTF = new HashingTF()
      .setNumFeatures(1000)
      .setInputCol(tokenizer.getOutputCol)
      .setOutputCol("features")

    val lr = new LogisticRegression()
      .setMaxIter(10)
      .setRegParam(0.001)

    val pipeline = new Pipeline()
      .setStages(Array(tokenizer,hasingTF,lr))

    //在训练集上使用pipeline
    val model = pipeline.fit(training)
    //将安装好的流水线保存到磁盘中
    model.write.overwrite().save("/opt/mllib_model_file/lr-model")
    //将未安装好的pipeline保存到磁盘中
    pipeline.write.overwrite().save("/opt/mllib_model_file/unfit-lr-model")

    //装载模型
    val sameModel = PipelineModel.load("/opt/mllib_model_file/lr-model")
    //准备测试数据，不包含标签label
    val testing = spark.createDataFrame(Seq(
      (4L,"spark i j k"),
      (5L,"l m n"),
      (6L,"spark hadoop flink"),
      (7L,"apache hadoop")
    )).toDF("id","text")
    //用测试数据进行预测
    model.transform(testing)
      .select("id","text","probability","prediction")   //注意：后面两列列名固定，不能写错,（probability:预测的概率，prediction：预测的标签）
      .collect()
      .foreach { case Row(id: Long, text: String, probability: Vector, predicition: Double) =>
        println(s"($id,$text)----->prob=$probability,prediction=$predicition")
      }

    /**
     * (4,spark i j k)----->prob=[0.7383530890076225,0.2616469109923776],prediction=0.0
     * (5,l m n)----->prob=[0.9040447202686991,0.09595527973130087],prediction=0.0
     * (6,spark hadoop flink)----->prob=[0.9967474861782645,0.003252513821735373],prediction=0.0
     * (7,apache hadoop)----->prob=[0.9990235788314131,9.764211685868018E-4],prediction=0.0
     */



  }

}
