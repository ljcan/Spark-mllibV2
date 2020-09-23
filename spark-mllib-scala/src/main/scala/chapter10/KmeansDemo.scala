package chapter10

import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.clustering.{KMeans, KMeansModel}
import org.apache.spark.ml.feature.{OneHotEncoder, StandardScaler, VectorAssembler}
import org.apache.spark.sql.SparkSession

object KmeansDemo{
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .master("local")
      .appName("KmeansDemo")
      .getOrCreate()

    val data = spark.read.format("csv").option("header",true).load("file:///Users/liujunqiang/Downloads/spark-mllib/spark-mllib-scala/src/main/scala/data/customers_data.csv")
    data.show(4)

    //将数据转换为数值型的，并且缓存
    val data1 = data.select(
      data("Channel").cast("Double"),
      data("Region").cast("Double"),
      data("Fresh").cast("Double"),
      data("Milk").cast("Double"),
      data("Grocery").cast("Double"),
      data("Frozen").cast("Double"),
      data("Detergents_Paper").cast("Double"),
      data("Delicassen").cast("Double")
    ).cache()

    //将类别特征转换为二元编码
    val datahot1 = new OneHotEncoder()
      .setInputCol("Channel")
      .setOutputCol("ChannelVec")
      .setDropLast(false)

    val datahot2 = new OneHotEncoder()
      .setInputCol("Region")
      .setOutputCol("RegionVec")
      .setDropLast(false)

    //将新生成的特征变量以及之前得6个连续型字段组成一个新的向量
    val featuresArray = Array("ChannelVec","RegionVec","Fresh","Milk","Grocery","Frozen","Detergents_Paper","Delicassen")
    //组合为特征向量
    val vectorAssembler = new VectorAssembler()
      .setInputCols(featuresArray)
      .setOutputCol("features")

    //对特征进行规范化
    val standardScaler = new StandardScaler()
      .setInputCol("features")
      .setOutputCol("scaledFeatures")
      .setWithStd(true)
      .setWithMean(false)

    //kmeans算法
    val kmeans = new KMeans()
      .setFeaturesCol("scaledFeatures")
      .setSeed(123)
      .setK(4)

    //把转换二元向量，特征规范化转换等组装到流水线上，因为pipeline上没有聚类的评估函数，因此，流水线中不纳入kmeans算法
    val pipeline1 = new Pipeline().setStages(Array(datahot1,datahot2,vectorAssembler,standardScaler))
    val data2 = pipeline1.fit(data1).transform(data1)
    //训练模型
    val model:KMeansModel = kmeans.fit(data2)
    val result = model.transform(data2)

    //评估模型
    result.show(10)
//    model.computeCost()
    //显示聚类结果：
    println("cluster centers:")
    model.clusterCenters.foreach(println)
    result.collect().foreach(row=>{
      println(row(10)+" is predicted as cluster "+row(11))
    })

    println("=======================")
    //当k=4的记录数
    result.select("scaledFeatures","prediction").groupBy("prediction").count().show()
    /**
     * +----------+-----+
     * |prediction|count|
     * +----------+-----+
     * |         1|    3|
     * |         3|   45|
     * |         2|  268|
     * |         0|  124|
     * +----------+-----+
     */
    result.select("scaledFeatures","prediction").filter(i=>i(1)==0).show(20)
    result.select("scaledFeatures","prediction").filter(i=>i(1)==0).select("scaledFeatures")
      .show(20)


    /**
     * 模型优化
     * 循环选取哪个K最好
     */
    val KSSE = (2 to 20 by 1).toList.map{k=>
      val kmeans = new KMeans().setFeaturesCol("scaledFeatures").setK(k).setSeed(123)
      val model1 = kmeans.fit(data2)

      //评估性能
//      model.compute
      //k,实际迭代次数，聚类类别编号，每类的记录数，类中心
      (k,model1.getMaxIter,model1.summary.trainingCost,model1.summary.cluster,model1.summary.clusterSizes,model1.clusterCenters)
    }
    KSSE.map(x=>(x._1,x._3)).sortBy(_._2).foreach(println)
//    KSSE.map(x=>(x._1,x._3)).sortBy(_._2)

    //将结果画为折线图，观察出最佳K值

  }
}
