import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.BooleanWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;

public class TaggedValue implements Writable{

	private Text city;
	private BooleanWritable regOrCity;
	
	public TaggedValue()
	{
		this.city = new Text();
		this.regOrCity = new BooleanWritable();
	}
	
	public TaggedValue(Text c,BooleanWritable city)
	{
		this.city = c;
		this.regOrCity = city;
	}
	
	public BooleanWritable isCity()
	{
		return this.regOrCity;
	}
	
	public Text getCity()
	{
		return this.city;
	}
	
	public String toString()
	{
		if(this.isCity().get())
			return this.city.toString();
		else
			return this.city.toString().split(",")[2];
	}
	
	public String getCode()
	{
		if(this.isCity().get())
			return ((this.city.toString().split(",")[0])+this.city.toString().split(",")[3]).toUpperCase();
		else
			return ((this.city.toString().split(",")[0])+this.city.toString().split(",")[1]).toUpperCase();
	}
	
	public boolean isFromRegion(TaggedValue value)
	{
		if(value.isCity().get() || !this.isCity().get())
			return false;
		else
		{
			String[] splitCity = this.city.toString().split(",");
			String[] splitReg = value.getCity().toString().split(",");
			String codereg = splitReg[0].toLowerCase();
			String codeCity = splitCity[0].toLowerCase();
			int citynb = Integer.parseInt(splitCity[3]);
			int regnb = Integer.parseInt(splitReg[1]);
			
			return codereg.equals(codeCity) && citynb == regnb;
		}
	}
	
	public void readFields(DataInput in) throws IOException {
		// TODO Auto-generated method stub
		city.readFields(in);
		regOrCity.readFields(in);
	}

	public void write(DataOutput out) throws IOException {
		// TODO Auto-generated method stub
		city.write(out);
		regOrCity.write(out);
	}

}
