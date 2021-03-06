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
		String regionFile = args[1];
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
		
		JavaPairRDD<Integer,Double> histo1 = doubledRDD.mapToPair(x -> {
			int log_pop10 = (int)Math.pow(10,(int) Math.round(Math.log10(x)));
			return new Tuple2<Integer,Double>(log_pop10,x);
		});
		JavaPairRDD<Integer,StatCounter> histoFinal = histo1.aggregateByKey(new StatCounter(), (c,d) -> c.merge(d), (c1,d2) -> c1.merge(d2));
		
		/**
		 * Exercice 5
		 */
		
		JavaRDD<String> rddToJoin = context.textFile(regionFile, Integer.parseInt(conf.get("spark.executor.instances")));
		
		JavaPairRDD<String, String> citiesMap = rddToMap.mapToPair(x -> { 
			String[] y = {};
			if(!x.split(",")[4].equals("Population"))
			{
				if(!x.split(",")[4].equals(""))
					y = x.split(",");
				return new Tuple2<String,String>( (y[0]+y[3]).toUpperCase() ,x);
			}
			return new Tuple2<String,String>("",x);
		});
		
		JavaPairRDD<String, String> regionMap = rddToJoin.mapToPair(x -> { 
			String[] y = {};
			if(!x.split(",")[4].equals(""))
				y = x.split(",");
			return new Tuple2<String,String>( (y[0]+y[1]).toUpperCase() ,x);
		});
		
		JavaPairRDD<String, Tuple2<String,String>> joinedMap = citiesMap.join(regionMap);
	}
}
