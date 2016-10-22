import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

public class TaggedSortComparator extends  WritableComparator {

protected TaggedSortComparator() {
    super(TaggedKey.class, true);
}   
@SuppressWarnings("rawtypes")
@Override
public int compare(WritableComparable w1, WritableComparable w2) {
    TaggedKey k1 = (TaggedKey)w1;
    TaggedKey k2 = (TaggedKey)w2;
    	if(k1.isCity().get() && k2.isCity().get()){
    			return 0;	
    	}else if(k1.isCity().get()){
    	return -1;
    	}
    		
    	return 1;
}			
}