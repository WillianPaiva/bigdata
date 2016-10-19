import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.BooleanWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;


public class TaggedKey implements WritableComparable<TaggedKey>{

	private Text key;
	private BooleanWritable isCity;
	
	public TaggedKey()
	{
		key = new Text();
		isCity = new BooleanWritable();
	}
	
	public TaggedKey(String text,boolean c)
	{
		key = new Text(text);
		isCity = new BooleanWritable(c);
	}
	
	public Text getKey()
	{
		return key;
	}
	
	public BooleanWritable isCity()
	{
		return this.isCity;
	}
	
	public void readFields(DataInput in) throws IOException {
		// TODO Auto-generated method stub
		key.readFields(in);
		isCity.readFields(in);
	}

	public void write(DataOutput out) throws IOException {
		// TODO Auto-generated method stub
		key.write(out);
		isCity.write(out);
	}

	public int compareTo(TaggedKey o) {
		// TODO Auto-generated method stub
		return key.compareTo(o.getKey());
	}
	
	public String toString()
	{
		return key.toString();
	}

}
