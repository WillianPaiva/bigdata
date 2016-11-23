package bigdata;

import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaDoubleRDD;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.util.StatCounter;

import scala.Tuple2;
import scala.collection.TraversableOnce;

public class TPSpark {

	public static void main(String[] args) {
		//System.out.println(args.);
		
		String filePath = args[0];
		SparkConf conf = new SparkConf().setAppName("TP Spark");
		JavaSparkContext context = new JavaSparkContext(conf);
		
		//RDD création
		JavaRDD<String> rddToMap = context.textFile(filePath, Integer.parseInt(conf.get("spark.executor.instances")));

		/**
		 * Exercice 1
		 */
		System.out.println("Number of partitions: "+rddToMap.getNumPartitions());
		
		JavaRDD<Tuple2<String,Integer>> tupledRDD = rddToMap.map(x -> { 
			int y = -1;
			if(!x.split(",")[4].equals("Population"))
			{
				if(!x.split(",")[4].equals(""))
					y = Integer.parseInt(x.split(",")[4]);
				return new Tuple2<String,Integer>(x.split(",")[1],y);
			}
			return new Tuple2<String,Integer>("Cities", -1);
		});
		
		System.out.println("Number of elements in RDD: "+tupledRDD.count());
		

		/**
		 * Exercice 2
		 */
		JavaRDD<Tuple2<String,Integer>> filteredRDD = tupledRDD.filter(x -> x._2 != -1);
	
		System.out.println("Number of elements in filtered RDD: "+filteredRDD.count());
		
		/**
		 * Exercice 3
		 */
		JavaDoubleRDD doubledRDD = filteredRDD.mapToDouble(x -> x._2);
		
		System.out.println(doubledRDD.stats());
		
		/**
		 * Exercice 4
		 */
		/*JavaPairRDD<Integer,Double> histo1 = doubledRDD.mapToPair(x -> {
			int log_pop10 = (int)Math.pow(10,(int) Math.round(Math.log10(x)));
			return new Tuple2<Integer,Double>(log_pop10,x);
		});
		
		JavaPairRDD<Integer,Iterable<Double>> histo2 = histo1.groupByKey();
		
		JavaRDD<StatCounter> histoFinal = histo2.map( x -> context.parallelize((List<Double>) x._2).mapToDouble(y -> y).stats());
		
		histoFinal.collect().forEach(System.out::println);*/
	}
}
