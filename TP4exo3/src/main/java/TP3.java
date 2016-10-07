import java.io.IOException;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.File;
import java.io.FileInputStream;
import java.lang.Math;
import java.net.URI;

import org.apache.commons.io.FileUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Writable;
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


    public static class ConfCachFiles implements Writable
    {
        private static IntWritable steplog;

        public ConfCachFiles()
        {
            steplog = new IntWritable(10);
        }

        public IntWritable getStep()
        {
            return steplog;
        }

        public void setStep(int value)
        {
            this.steplog = new IntWritable(value);
        }

        @Override
        public void write(DataOutput out) throws IOException
        {
            steplog.write(out);
        }

        @Override
        public void readFields(DataInput in) throws IOException
        {
            steplog.readFields(in);
        }

    }

    public static class TP3Mapper extends Mapper<Object, Text, Text, AvgMaxMinWritable>{

        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {

            String[] line = value.toString().split(",");

            if(!line[4].equals("Population"))
                {
                    if(!line[4].equals(""))
                        {
                            int num_pop = (int)Double.parseDouble(line[4]);
                            int base = (int)Double.parseDouble(context.getConfiguration().getStrings("steplog","10")[0]);
                            int log_pop = (int)Math.round(Math.log(num_pop) / Math.log(base));
                            int log_pop10 = (int)Math.pow(10,log_pop);
                            Text result = new Text(log_pop10+"");
                            context.write(result,
                                          new AvgMaxMinWritable(1,(int)num_pop));
                        }
                }
        }
    }

    public static class TP3Reducer extends Reducer<Text,AvgMaxMinWritable,Text,AvgMaxMinWritable> {

        private static AvgMaxMinWritable result = new AvgMaxMinWritable();
        private boolean first = true;
        public void reduce(Text key, Iterable<AvgMaxMinWritable> values,
                           Context context
                           ) throws IOException, InterruptedException {
            if(first)
                {
                    context.write(new Text("classe\t\tcount\t\tavg\t\tmax\t\tmin"),null);
                    first = false;
                }
            int sum = 0;
            int avg = 0;
            int max = 0;
            int min = 0;

            for(AvgMaxMinWritable value : values)
                {
                    sum += value.getCounter();
                    avg += value.getPop();

                    if(max < value.getPop())
                        max = value.getPop();

                    if(min > value.getPop() || min == 0)
                        min = value.getPop();
                }

            result.setCounter(sum);

            result.setAvg(avg/sum);
            result.setMax(max);
            result.setMin(min);

            context.write(key,result);
        }
    }
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "TP3");
        job.setNumReduceTasks(1);
        job.setJarByClass(TP3.class);
        job.setMapperClass(TP3Mapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(AvgMaxMinWritable.class);
        job.setReducerClass(TP3Reducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(AvgMaxMinWritable.class);
        job.setOutputFormatClass(TextOutputFormat.class);
        job.setInputFormatClass(TextInputFormat.class);
        /***/
        job.addCacheFile(new Path(args[2]).toUri());
        /***/
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
