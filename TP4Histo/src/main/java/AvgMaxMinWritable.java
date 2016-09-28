import java.io.IOException;
import java.io.DataInput;
import java.io.DataOutput;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.IntWritable;

public class AvgMaxMinWritable implements Writable
    {
	private IntWritable counter;
	private IntWritable pop;
	private IntWritable avg;
	private IntWritable max;
	private IntWritable min;

	/*************************************/
	
	public AvgMaxMinWritable()
	{
	    counter = new IntWritable(0);
	    pop = new IntWritable(0);
	    avg = new IntWritable(0);
	    max = new IntWritable(0);
	    min = new IntWritable(0);
	}

	public AvgMaxMinWritable(int count,int pop)
	{
	    this.counter = new IntWritable(count);
	    this.pop = new IntWritable(pop);
	    avg = new IntWritable(0);
	    max = new IntWritable(0);
	    min = new IntWritable(0);

	}

	/**
	 * GETTER/SETTER
	 **/
	public void setCounter(int count)
	{
	    this.counter.set(count);
	}

	public void setPop(int pop)
	{
	    this.pop.set(pop);
	}

	public int getCounter()
	{
	    return counter.get();
	}

	public int getPop()
	{
	    return pop.get();
	}

	public int getAvg()
	{
	    return avg.get();
	}

	public int getMax()
	{
	    return max.get();
	}

	public int getMin()
	{
	    return min.get();
	}

	public void setAvg(int value)
	{
	    avg.set(value);
	}

	public void setMax(int value)
	{
	    max.set(value);
	}

	public void setMin(int value)
	{
	    min.set(value);
	}
	
	/*****************/
	
	@Override
	public void write(DataOutput out) throws IOException
	{
	    counter.write(out);
	    pop.write(out);
	    avg.write(out);
	    max.write(out);
	    min.write(out);
	}

	@Override
	public void readFields(DataInput in) throws IOException
	{
	    counter.readFields(in);
	    pop.readFields(in); 
	    avg.readFields(in); 
	    max.readFields(in); 
	    min.readFields(in); 
	}

	@Override
	public String toString()
	{
	    return counter + "\t\t" + avg + "\t" + max + "\t" + min;
	}
    }
