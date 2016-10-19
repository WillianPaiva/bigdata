import org.apache.hadoop.mapreduce.Partitioner;

public class TaggedPartitionner extends Partitioner<TaggedKey, TaggedValue>{

	@Override
	public int getPartition(TaggedKey key, TaggedValue value, int numPartitions) {
		// TODO Auto-generated method stub
		if(numPartitions == 0)
			return 0;
		
		int alpha = key.getKey().toString().toLowerCase().charAt(0) - 96;
		
		return alpha % numPartitions;
	}

}
