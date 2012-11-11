/**
 * 
 */
package cpsc551.HadoopEncrypt.MapReduce;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/**
 * @author nimbus154
 *
 */
public class DecryptionReducer extends
		Reducer<Text, Text,	Text, Text> 
{
	public DecryptionReducer() { } ;
	
	/**
	 * Generates a file of unencrypted text
	 * @param key file line number
	 * @param value encrypted line
	 * @param context to write new key values to;
	 */
	@Override
	public void reduce(Text key, Iterable<Text> value, Context context)
			throws IOException, InterruptedException
	{
		//simple write encrypted data to file
		for(Text decrypted : value)
			context.write(key, decrypted);		
	}
}