package chapter7

import org.apache.spark.SparkConf
import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.evaluation.RegressionEvaluator
import org.apache.spark.ml.feature.{OneHotEncoder, SQLTransformer, VectorAssembler, VectorIndexer}
import org.apache.spark.ml.regression.{DecisionTreeRegressor, LinearRegression}
import org.apache.spark.ml.tuning.{ParamGridBuilder, TrainValidationSplit}
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.{avg, broadcast, col, max}

object LogisticDemo {
  def main(args: Array[String]): Unit = {

    val conf = new SparkConf()
    conf.set("spark.driver.host","localhost")

    val spark = SparkSession.builder()
      .appName("LogisticDemo")
      .config(conf)
      .master("local")
      .getOrCreate()

//    spark.sql("")

    val data = spark.read.format("csv").option("header",true)
      .load("file:///Users/liujunqiang/Downloads/spark-mllib/spark-mllib-scala/src/main/scala/data/hour.csv")
    data.show(10)

    val data1= data.select(
      data("season").cast("Double"),
      data ("yr").cast("Double"),
      data ("mnth").cast("Double"),
      data ("hr").cast("Double"),
      data("holiday") .cast( "Double"),
      data ("weekday").cast( "Double"),
      data("workingday").cast("Double"),
      data("weathersit") .cast("Double"),
      data ("temp") .cast ("Double"),
      data("atemp") .cast( "Double"),
      data ("hum") .cast( "Double"),
      data("windspeed" ).cast("Double"),
      data("cnt").cast("Double").alias("label"))
    //生成一个存放以上预测特征的特征向量
    val featureArray = Array("season","yr","mnth","hr","holiday","weekday","workingday","weathersit","temp","atemp","hum","windspeed")
    val assembler = new VectorAssembler().setInputCols(featureArray).setOutputCol("features")

    //对类别特征进行索引化和数值化(VectorIndexer解决数据集中的类别特征Vector。它可以自动识别哪些特征是类别型的，并且将原始值转换为类别指标。)
    val featureIndex = new VectorIndexer().setInputCol("features").setOutputCol("indexedFeatures").setMaxCategories(24)
    //对类别特征使用OneHotEncoder，把他们转换为二元向量
    val data2=new OneHotEncoder().setInputCol("season").setOutputCol("seasonVec")
    val data3=new OneHotEncoder().setInputCol("yr").setOutputCol("yrVec")
    val data4=new OneHotEncoder().setInputCol("mnth").setOutputCol("mnthVec")
    val data5=new OneHotEncoder().setInputCol("hr").setOutputCol("hrVec")
    val data6=new OneHotEncoder().setInputCol("holiday").setOutputCol("holidayVec")
    val data7=new OneHotEncoder().setInputCol("weekday").setOutputCol("weekdayVec")
    val data8=new OneHotEncoder().setInputCol("workingday").setOutputCol("workingdayVec")
    val data9=new OneHotEncoder().setInputCol("weathersit").setOutputCol("weathersitVec")
    //因 OneHotEncoder不是 Estimator，这里我们对采用回归算法的数据另外进行处理，先
    //建立一个流水线， 把以上转换组装到这个流水线上
    val pipeline = new Pipeline().setStages(Array(data2,data3,data4,data5,data6,data7,data8,data9))
    val data_lr = pipeline.fit(data1).transform(data1)

//    val assembler_lr = new VectorAssembler().setInputCols(featureArray).setOutputCol("feature_lr")
    //将数据集划分，一部分分为训练集，一部分分为决策模型,30%数据用于决策，12作为种子
    //对data1进行随机划分，作为决策模型
    val Array(trainingData,testData) = data1.randomSplit(Array(0.7,0.3),12)
    //对data_lr进行随机划分，作为分类模型
    val Array(trainingData_lr,testData_lr) = data_lr.randomSplit(Array(0.7,0.3),12)

    //设置决策树回归模型参数
    val dt = new DecisionTreeRegressor()
      .setLabelCol("label")
      .setFeaturesCol("indexedFeatures")
      .setMaxBins(64)
      .setMaxDepth(15)

    //设置线性回归模型参数
    val lr = new LinearRegression()
      .setFeaturesCol("features")
      .setLabelCol("label")
      .setFitIntercept(true)
      .setMaxIter(20)
      .setRegParam(0.3)
      .setElasticNetParam(0.8)

    //把决策树涉及的特征转换及模型训练组装在一个流水线上
    val pipelineTree = new Pipeline().setStages(Array(assembler,featureIndex,dt))
    //把线性回归涉及的特征转换，模型训练组装载在一个流水线
    val pipelineLR = new Pipeline().setStages(Array(assembler,lr))

    //训练决策树回归模型
    val model1 = pipelineTree.fit(trainingData)
    //训练线性回归模型
    val modelLR = pipelineLR.fit(trainingData_lr)

    //做出预测
    //预测决策树回归的值
    val predictions = model1.transform(testData)
    //预测线性回归模型的值
    val predictions_lr = modelLR.transform(testData_lr)

    println("决策回归值=====>",predictions)
    println("线性回归模型值=======>",predictions_lr)

    //评估模型
    //RegressionEvaluator.setMetricName可以定义四种评估器: rmse (默认)、 mse、 r2、 mae
    val evaluator = new RegressionEvaluator()
      .setLabelCol("label")
      .setPredictionCol("prediction")
      .setMetricName("rmse")

    val rmse = evaluator.evaluate(predictions)
    val rmse_lr = evaluator.evaluate(predictions_lr)

    println("rmse:========",rmse)
    println("rmse_lr========",rmse_lr)

    /**
     * (rmse:========,61.00515083400415)
     * (rmse_lr========,140.5587177270622)
     */

    //线性模型优化
    //atemp列贡献不大，因此可以过滤掉改项
    val assembler_lr1 = new VectorAssembler().setInputCols(Array("season","yr","mnth","hr","holiday","weekday","workingday","weathersit","temp","hum","windspeed"))
      .setOutputCol("features_lr1")

    //对label标签进行转换，使其更接近正态分布
    val sqlTransformer = new SQLTransformer().setStatement(
      "SELECT *,SQRT(label) as label1 FROM __THIS__"
    )
    //这里我们利用训练验证划分法对线性回归模型进行优化，对参数进行网格化 ，将数据集划分为训练集、验证集和测试集 。
    //建立线性回归模型，预测label1的值，设置回归参数
    val lr1 = new LinearRegression()
      .setLabelCol("label1")
      .setFeaturesCol("features_lr1")
      .setFitIntercept(true)
    //将特征组合，特征优化，训练模型装订到流水线中
    val pipeline_lr1 = new Pipeline().setStages(Array(assembler_lr1,sqlTransformer,lr1))

    //建立网格参数
    val paramGrid =new ParamGridBuilder()
      .addGrid(lr1.elasticNetParam,Array(0.0,0.8,1.0))
      .addGrid(lr1.regParam,Array(0.1,0.3,0.5))
      .addGrid(lr1.maxIter,Array(20,30))
      .build()

    //计算测试误差
    val evaluator_lr1 = new RegressionEvaluator()
      .setLabelCol("label1")
      .setPredictionCol("prediction")
      .setMetricName("rmse")

    //利用交叉验证方法
    val trainValidationSplit = new TrainValidationSplit()
      .setEstimator(pipeline_lr1)
      .setEvaluator(evaluator_lr1)
      .setEstimatorParamMaps(paramGrid)
      .setTrainRatio(0.8)

    //训练模型自动选择最优参数
    val lrModel1 = trainValidationSplit.fit(trainingData_lr)

    //查看模型全部参数
    lrModel1.getEstimatorParamMaps.foreach {
      println
    }
    lrModel1.getEvaluator.extractParamMap()
    lrModel1 . getEvaluator.isLargerBetter

    //用最好的参数组合做出预测
    val prediction_lr1 = lrModel1.transform(testData_lr)
    val rmse_lr1 = evaluator_lr1.evaluate(prediction_lr1)
    println("rmse_lr1=========",rmse_lr1)
    /**
     * (rmse_lr1=========,4.872124510278292)
     */
    // 显示转换后特征值的前 5 行信息
    prediction_lr1.select("features_lr1","label","label1","prediction") .show(5)

    /**
     * +--------------------+-----+------------------+------------------+
     * |        features_lr1|label|            label1|        prediction|
     * +--------------------+-----+------------------+------------------+
     * |(11,[0,2,7,8,9,10...| 25.0|               5.0|2.6122299340594415|
     * |(11,[0,2,7,8,9,10...| 33.0| 5.744562646538029|0.0729961817440441|
     * |[1.0,0.0,1.0,0.0,...|  5.0|  2.23606797749979| 3.853133070751188|
     * |[1.0,0.0,1.0,0.0,...| 13.0| 3.605551275463989|  3.62828609273608|
     * |[1.0,0.0,1.0,0.0,...| 14.0|3.7416573867739413| 2.544241441948475|
     * +--------------------+-----+------------------+------------------+
     */




  }

}
