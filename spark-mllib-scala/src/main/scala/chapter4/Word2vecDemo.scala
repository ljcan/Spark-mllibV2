package chapter4

import org.apache.spark.ml.feature.Word2Vec
import org.apache.spark.sql.SparkSession

/**
 * Word2vec 是一个 Estimator，它采用一系 列代 表文档 的词语来训练 word2vecmodel。
 * 该模型将每个词语映射到 一个固定大小的向量 。
 * word2vecmodel 使用文档中每个词语的平均数 来将文档转换为向量，然后这个向量可以作为预测的特征，来计算文档相似度计算等 。
 *
 * 词向量的长度（长度*词总量*8 不大于Int.MaxValue)
 */
object Word2vecDemo {

  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .config("spark.driver.host","localhost")
      .appName("Word2vecDemo")
      .master("local")
      .getOrCreate()
    val documentDF = spark.createDataFrame(Seq(
      ("Hi I heard about Spark".split(" ")),
      ("I wish Java cloud use case classes".split(" ")),
      ("logistic regression model are neat".split(" ")))
      .map(Tuple1.apply)
    ).toDF("text")

    //训练从词到向量的映射
    val word2Vec = new Word2Vec()
      .setInputCol("text")
      .setOutputCol("result")
      .setVectorSize(3)
      .setMinCount(0)

    val model = word2Vec.fit(documentDF)
    val result = model.transform(documentDF)
    //打印词向量
    model.getVectors.foreach(println(_))
    /**
     * [heard,[-0.12953028082847595,-0.02439754828810692,-0.0045671830885112286]]
     * [are,[-0.11031222343444824,-0.06575259566307068,0.12774573266506195]]
     * [neat,[0.011359920725226402,-0.040843334048986435,0.11315673589706421]]
     * [model,[0.1662672758102417,-0.1106506809592247,-0.08882179856300354]]
     * [logistic,[0.06702212989330292,0.010217220522463322,-0.03706975281238556]]
     * [classes,[-0.002347775036469102,-0.053962014615535736,-0.10906215012073517]]
     * [I,[-0.14980706572532654,0.10542768985033035,0.13521014153957367]]
     * [cloud,[0.04672885313630104,9.357648668810725E-4,-0.06231185048818588]]
     * [regression,[0.042541176080703735,0.016550712287425995,0.09806815534830093]]
     * [Spark,[-4.574352642521262E-4,0.07803013920783997,-0.15273161232471466]]
     * [use,[-0.12255867570638657,-0.09879001975059509,0.11405684798955917]]
     * [Hi,[0.09183481335639954,-0.046595457941293716,0.1320793181657791]]
     * [case,[-0.0991075336933136,-0.10489686578512192,0.054523881524801254]]
     * [about,[-0.037332721054553986,-0.04663863778114319,0.1361648142337799]]
     * [Java,[0.06396650522947311,0.025200217962265015,-0.05962473899126053]]
     * [wish,[-0.10191691666841507,-0.09718813747167587,0.1269114911556244]]
     */
    result.select("result").take(3).foreach(println)
    /**
     * [[-0.04505853790324182,0.0131652370095253,0.04923109570518136]]
     * [[-0.05214894406630524,-0.031896194991921735,0.0285290889441967]]
     * [[0.035375655815005305,-0.0380957355722785,0.042615814507007604]]
     */

    //进行预测，找出与Spark语义最近的词汇
    model.findSynonyms("Spark",10).foreach(println(_))

    /**
     * [cloud,0.7162488698959351]
     * [Java,0.7075538635253906]
     * [classes,0.5963349342346191]
     * [logistic,0.48506537079811096]
     * [model,0.12952490150928497]
     * [heard,-0.05070462077856064]
     * [I,-0.3164113461971283]
     * [case,-0.6223816871643066]
     * [regression,-0.7387735843658447]
     * [use,-0.7520141005516052]
     */

  }

}
