/**
 * Chad Wyszynski for CPSC 551
 */
package cpsc551.HadoopEncrypt.MapReduce;

import java.io.IOException;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Reducer;

/**
 * Concatenates encrypted bytes into a file
 * @author Chad Wyszynski
 * Input key    - EncryptionMapper receives lines of text as input. LongWritable
 * 			     refers to the number of the first character in the line.
 * Input value  - encrypted bytes of text
 * Output key   - same as input key
 * Output value - encrypted file
 */
public class EncryptionReducer extends
		Reducer<LongWritable, BytesWritable, LongWritable, BytesWritable> 
{
	public EncryptionReducer() { } ;
	
	/**
	 * Generates a file of encrypted bytes
	 * @param key file line number
	 * @param value encrypted line
	 * @param context to write new key values to;
	 */
	@Override
	public void reduce(LongWritable key, Iterable<BytesWritable> value, Context context)
			throws IOException, InterruptedException
	{
		//simple write encrypted data to file
		for(BytesWritable b : value)
			context.write(key, b);		
	}
}
