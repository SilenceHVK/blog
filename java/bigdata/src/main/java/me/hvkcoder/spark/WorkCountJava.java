package me.hvkcoder.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.*;
import scala.Tuple2;

import java.util.Arrays;

/**
 * Spark 实现单词统计
 *
 * @author h_vk
 * @since 2022/2/11
 */
public class WorkCountJava {
  public static void main(String[] args) {
    SparkConf sparkConf = new SparkConf();
    sparkConf.setMaster("local"); // 设置单机运行
    sparkConf.setAppName("WordCountSparkJava");

    JavaSparkContext sparkContext = new JavaSparkContext(sparkConf);
    JavaRDD<String> stringJavaRDD =
        sparkContext.textFile(ClassLoader.getSystemResource("data.txt").getPath());
    JavaPairRDD<String, Integer> result =
        stringJavaRDD
            .flatMap((FlatMapFunction<String, String>) s -> Arrays.stream(s.split(" ")).iterator())
            .mapToPair((PairFunction<String, String, Integer>) s -> new Tuple2<>(s, 1))
            .reduceByKey((Function2<Integer, Integer, Integer>) Integer::sum);
    result.foreach(
        (VoidFunction<Tuple2<String, Integer>>)
            value -> System.out.println(value._1() + ":" + value._2()));
  }
}
