import java.io.IOException;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class TP3 {

	

	public static class TP3Mapper extends Mapper<Object, Text, Text, LongWritable>{

		private TreeMap<Long,String> ktown;
		int topk;
		
		/******/
		protected void setup(Context context)
		{
			ktown = new TreeMap<Long,String>();
			topk = context.getConfiguration().getInt("topk", 1);
		}

		public void map(Object key, Text value, Context context
				) throws IOException, InterruptedException {

			String[] line = value.toString().split(",");
			
			if(!line[4].equals("Population"))
			{
				if(!line[4].equals(""))
				{
					String name = line[2];
					this.add(Long.parseLong(line[4]), name);
				}
			}
		}
		
		private void add(long a,String town)
		{
			if(ktown.size() < topk)
			{
				ktown.put(a,town);
			}
			else
			{
				long first = ktown.firstKey();
				if(first < a)
				{
					ktown.remove(first);
					ktown.put(a, town);
				}
			}
		}
		
		protected  void cleanup(Context context) throws IOException, InterruptedException
		{
			for(long d : ktown.keySet())
			{
				context.write(new Text(ktown.get(d)), new LongWritable(d));
			}
		}
	}

	public static class TP3Reducer extends Reducer<Text,LongWritable,Text,LongWritable> {

		private TreeMap<Long,String> ktown;
		int topk;
		
		/******/
		protected void setup(Context context)
		{
			ktown = new TreeMap<Long,String>();
			topk = context.getConfiguration().getInt("topk", 1);
		}
		
		public void reduce(Text key, Iterable<LongWritable> values,
				Context context
				) throws IOException, InterruptedException {
			for(LongWritable v : values)
			{
				this.add(v.get(),key.toString());
			}
		}
		
		private void add(long a,String town)
		{
			if(ktown.size() < topk)
			{
				ktown.put(a,town);
			}
			else
			{
				long first = ktown.firstKey();
				if(first < a)
				{
					ktown.remove(first);
					ktown.put(a, town);
				}
			}
		}
		
		protected  void cleanup(Context context) throws IOException, InterruptedException
		{
			for(long d : ktown.keySet())
			{
				context.write(new Text(ktown.get(d)), new LongWritable(d));
			}
		}
	}
	
	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		conf.set("topk", args[2]);
		Job job = Job.getInstance(conf, "TP3");
		job.setNumReduceTasks(1);
		job.setJarByClass(TP3.class);
		job.setMapperClass(TP3Mapper.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(LongWritable.class);
		job.setReducerClass(TP3Reducer.class);
		job.setCombinerClass(TP3Reducer.class);
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(LongWritable.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		job.setInputFormatClass(TextInputFormat.class);
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}
