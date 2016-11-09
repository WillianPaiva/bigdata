import org.apache.hadoop.conf.Configuration;
import java.util.List;
import java.util.ArrayList;

public class Util
{
    public boolean equals(List<Point2DWritable> list,Configuration conf)
    {
	int k = Integer.parseInt(conf.get("k"));
	for(int i=0;i<k;i++)
	    {
		if(!list.contains(new Point2DWritable(conf.get("k"+i))))
		    {
			return false;
		    }
	    }

	return true;	
    }
}
