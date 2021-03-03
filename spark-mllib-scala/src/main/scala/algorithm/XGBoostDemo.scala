package algorithm

import ml.dmlc.xgboost4j.java.XGBoost
import ml.dmlc.xgboost4j.scala.spark.{XGBoostClassificationModel, XGBoostClassifier}
import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.feature.{IndexToString, StringIndexer, VectorAssembler}
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.{StructField, StructType}

object XGBoostDemo {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession
        .builder()
        .config("spark.driver.host","localhost")
        .master("local")
        .appName("XGBoostDemo")
        .getOrCreate()

//    new StructType(Array(
//        StructField("spe")
//    ))

    var inputData = spark.read.format("csv")
      .load("/Users/liujunqiang/Downloads/spark-mllib/spark-mllib-scala/src/main/scala/data/iris.csv")
      .toDF("sepal length","sepal width","petal length","petal width","class")

      inputData = inputData.select(
          inputData("sepal length").cast("Double"),
          inputData("sepal width").cast("Double"),
          inputData("petal length").cast("Double"),
          inputData("petal width").cast("Double"),
          inputData("class").cast("String")
      )
    //划分测试集和训练集
    val Array(training,test) = inputData.randomSplit(Array(0.8,0.2),123)
    //数据集向量化
    val assembler = new VectorAssembler()
      .setInputCols(Array("sepal length","sepal width","petal length","petal width"))
      .setOutputCol("features")
    //字符串转换为索引
    val lastIndex = new StringIndexer()
      .setInputCol("class")
      .setOutputCol("classIndex")
      .fit(training)

      val booster = new XGBoostClassifier(
          Map("eta" -> 0.1f,
              "max_depth" -> 2,
              "objective" -> "multi:softprob",
              "num_class" -> 3,
              "num_round" -> 100,
              "num_workers" -> 2
          )
      )
    booster.setFeaturesCol("features")
    booster.setLabelCol("classIndex")

    val labelConvert = new IndexToString()
      .setInputCol("prediction")
      .setOutputCol("realLabel")
      .setLabels(lastIndex.labels)

    val pipeline = new Pipeline().setStages(Array(assembler,lastIndex,booster,labelConvert))
    //训练模型
    val model = pipeline.fit(training)
    //进行测试
    val prediction = model.transform(test)
    prediction.show(false)


//    new XGBoostClassificationModel(Map())





  }

}
