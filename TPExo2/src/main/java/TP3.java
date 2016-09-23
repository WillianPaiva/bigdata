import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Counter;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class TP3 {
    public static class TP3Mapper extends Mapper<Object, Text, Text, IntWritable>{
	
	public void map(Object key, Text value, Context context
			) throws IOException, InterruptedException {
	    
	    String[] line = value.toString().split(",");
	    if(!line[4].equals("Population"))
		{
		     if(!line[4].equals(""))
			 {
			     context.write(value, new IntWritable(1) );
			     context.getCounter("WCP","total_pop").increment(Integer.parseInt(line[4]));
			     context.getCounter("WCP","nb_pop").increment(1);
			 }
		     context.getCounter("WCP","nb_cities").increment(1); 
		}
	}
    }
    
    public static class TP3Reducer extends Reducer<Text,IntWritable,Text,IntWritable> {
	public void reduce(Text key, Iterable<IntWritable> values,
			   Context context
			   ) throws IOException, InterruptedException {
	    for(IntWritable value : values)
		context.write(key, null );
	}
    }
    public static void main(String[] args) throws Exception {
	Configuration conf = new Configuration();
	Job job = Job.getInstance(conf, "TP3");
	job.setNumReduceTasks(1);
	job.setJarByClass(TP3.class);
	job.setMapperClass(TP3Mapper.class);
	job.setMapOutputKeyClass(Text.class);
	job.setMapOutputValueClass(IntWritable.class);
	job.setReducerClass(TP3Reducer.class);
	job.setOutputKeyClass(Text.class);
	job.setOutputValueClass(IntWritable.class);
	job.setOutputFormatClass(TextOutputFormat.class);
	job.setInputFormatClass(TextInputFormat.class);
	FileInputFormat.addInputPath(job, new Path(args[0]));
	FileOutputFormat.setOutputPath(job, new Path(args[1]));
	System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}