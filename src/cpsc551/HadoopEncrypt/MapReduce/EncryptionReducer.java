/**
 * Chad Wyszynski for CPSC 551
 */
package cpsc551.HadoopEncrypt.MapReduce;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/**
 * Concatenates encrypted bytes into a file
 * @author Chad Wyszynski
 *
 */
public class EncryptionReducer extends
		Reducer<LongWritable, Text, LongWritable, Text> 
{
	public EncryptionReducer() { } ;
	
	/**
	 * Generates a file of encrypted bytes
	 * @param key file line number
	 * @param value encrypted line
	 * @param context to write new key values to;
	 */
	@Override
	public void reduce(LongWritable key, Iterable<Text> value, Context context)
			throws IOException, InterruptedException
	{
		//simple write encrypted data to file
		for(Text text : value)
			context.write(key, text);		
	}
}
