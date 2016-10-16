import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class TP3 {
  public static class TP3Mapper
       extends Mapper<IntWritable, Point2DWritable, Text, IntWritable>{

    public void map(IntWritable key, Point2DWritable value, Context context
        ) throws IOException, InterruptedException {
        double x = value.getPoint().getX();
        double y = value.getPoint().getY();
        if(x*x+y*y <= 1){
            context.write(new Text("inside"), new IntWritable(1));
        }
    }
  }

  public static class TP3Reducer
       extends Reducer<Text,IntWritable,Text,DoubleWritable> {
    public void reduce(Text key, Iterable<IntWritable> values,
                       Context context
                       ) throws IOException, InterruptedException {
        int split = Integer.parseInt(context.getConfiguration().get("splits"));
        int points = Integer.parseInt(context.getConfiguration().get("points"));
        double success = 0;
        double drops = (split*points);
        for(IntWritable value : values){
            success += value.get();
        }
        context.write(new Text("PI"), new DoubleWritable(4*(success/drops)));
    }
  }

  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
    conf.set("splits",args[1]);
    conf.set("points",args[2]);
    Job job = Job.getInstance(conf, "TP3");
    job.setNumReduceTasks(1);
    job.setJarByClass(TP3.class);
    job.setMapperClass(TP3Mapper.class);
    job.setMapOutputKeyClass(Text.class);
    job.setMapOutputValueClass(IntWritable.class);
    job.setReducerClass(TP3Reducer.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(DoubleWritable.class);
    job.setOutputFormatClass(TextOutputFormat.class);
    job.setInputFormatClass(RandomPointInputFormat.class);
    FileOutputFormat.setOutputPath(job, new Path(args[0]));
    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}
