package bigdata;

import java.util.List;
import java.util.Map;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

public class TPSpark {

	public static void main(String[] args) {
		//System.out.println(args.);
		
		String filePath = args[0];
		SparkConf conf = new SparkConf().setAppName("TP Spark");
		JavaSparkContext context = new JavaSparkContext(conf);
		
		//RDD création
		JavaRDD<Map<String,String>> rdd;
		JavaRDD<String> rddToMap = context.textFile(filePath,Integer.parseInt(conf.get("spark.executor.instances")));

		//System.out.println("Number of partitions: "+rdd.getNumPartitions());
	}
	
}
