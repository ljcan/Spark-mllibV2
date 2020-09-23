package chapter6

import org.apache.spark.ml.linalg
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.ml.linalg.{Matrices, Vectors}
import org.apache.spark.mllib.linalg.distributed.{IndexedRow, IndexedRowMatrix, RowMatrix}
import org.apache.spark.mllib.linalg.Vector
import org.apache.spark.rdd.RDD

/**
 * 本地矩阵
 */
object MatrixDemo {

  def main(args: Array[String]): Unit = {
    val matrix = Matrices.dense(3, 2, Array(1, 2, 3, 4, 5, 6))
    println(matrix)
  }

}

/**
 * 分布式矩阵
 */
class DisMatrixDemo{
  val sc = new SparkContext(new SparkConf()
    .setAppName("DisMatrixDemo")
    .setMaster("local"))
//  val rows:RDD[Vector] = sc.parallelize(Seq(1,2,3))
//  private val matrix = new RowMatrix(rows)
//  matrix.numCols()      //矩阵列数
//  matrix.numRows()      //矩阵行数

//  val indexRdd = sc.parallelize(Seq(1,2,3,4,5,6))
//  indexRdd = indexRdd.map(line=>Vectors.dense(line))
//    .map(v=>new IndexedRow(v.size,v))
//
//  new IndexedRowMatrix()
}
