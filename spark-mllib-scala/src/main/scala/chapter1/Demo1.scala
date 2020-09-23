package chapter1

import org.apache.spark.ml.classification.LogisticRegression
import org.apache.spark.ml.linalg.{Vectors,Vector}
import org.apache.spark.ml.param.ParamMap
import org.apache.spark.sql.{Row, SQLContext, SparkSession}
import org.apache.spark.{SparkConf, SparkContext}

object Demo1 {

  def main(args: Array[String]): Unit = {
//    val conf =new SparkConf().setMaster("local").setAppName("Demo1")
//    val sc = new SparkContext(conf)
//    val sQLContext = new SQLContext(sc)

    val spark = SparkSession.builder()
      .appName("Demo1")
      .master("local")
      .getOrCreate()

    val training1=spark.createDataFrame(Seq(
      (1.0,Vectors.dense(0.0,1.1,0.1)),
      (0.0,Vectors.dense(2.0,1.0,-1.0)),
      (0.0,Vectors.dense(2.0,1.3,1.0)),
      (1.0,Vectors.dense(0.0,1.2,-0.5))
    )).toDF("label","features")

    val lr = new LogisticRegression()

    lr.setMaxIter(10)
      .setRegParam(0.01)

    //训练lr模型
    val model1 = lr.fit(training1)

    println("model 1 use params :"+model1.parent.extractParamMap())

//    model 1 use params :{
//	logreg_63d8dc709236-aggregationDepth: 2,
//	logreg_63d8dc709236-elasticNetParam: 0.0,
//	logreg_63d8dc709236-family: auto,
//	logreg_63d8dc709236-featuresCol: features,
//	logreg_63d8dc709236-fitIntercept: true,
//	logreg_63d8dc709236-labelCol: label,
//	logreg_63d8dc709236-maxIter: 10,
//	logreg_63d8dc709236-predictionCol: prediction,
//	logreg_63d8dc709236-probabilityCol: probability,
//	logreg_63d8dc709236-rawPredictionCol: rawPrediction,
//	logreg_63d8dc709236-regParam: 0.01,
//	logreg_63d8dc709236-standardization: true,
//	logreg_63d8dc709236-threshold: 0.5,
//	logreg_63d8dc709236-tol: 1.0E-6
//}

    //parmMap指定参数
    val paramMap1=ParamMap(lr.maxIter->20)
      .put(lr.maxIter,30)                     //指定一个参数，覆盖原来的maxIter
      .put(lr.regParam->0.1,lr.threshold->0.55)     //指定多个参数

    val paramMpa2=ParamMap(lr.probabilityCol->"myProbability")    //修改输出列名
    val paramCombine=paramMap1++paramMpa2

    //用paramCombine参数训练一个新的模型，这些参数将会覆盖原来lr.set方法中设置的参数
    val model2=lr.fit(training1,paramCombine)

    println("model 2 was fit use params : "+model2.parent.extractParamMap())

    //准备测试数据
    val tesing=spark.createDataFrame(Seq(
      (1.0,Vectors.dense(-1.0,1.5,1.3)),
      (0.0,Vectors.dense(3.0,2.0,-0.1)),
      (1.0,Vectors.dense(0.0,2.2,-1.5))
    )).toDF("label","features")

    //使用Transformer.transform()方法对测试数据进行预测
    model2.transform(tesing)
      .select("features","label","myProbability","prediction")
      .collect()
      .foreach{case Row(features:Vector,label:Double,prob:Vector,prediction:Double)=>
        println(s"($features,$label)->prob=$prob,prediction=$prediction")
      }

//    ([-1.0,1.5,1.3],1.0)->prob=[0.057073041710340625,0.9429269582896593],prediction=1.0
//    ([3.0,2.0,-0.1],0.0)->prob=[0.9238522311704118,0.07614776882958811],prediction=0.0
//    ([0.0,2.2,-1.5],1.0)->prob=[0.10972776114779748,0.8902722388522026],prediction=1.0








  }



}
