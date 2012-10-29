/**
 * Chad Wyszynski for CPSC 551
 */
package cpsc551.HadoopEncrypt.MapReduce;

import java.io.IOException;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Reducer;

/**
 * Concatenates encrypted bytes into a file
 * @author Chad Wyszynski
 *
 */
public class EncryptionReducer extends
		Reducer<IntWritable, BytesWritable,	IntWritable, BytesWritable> 
{
	/**
	 * Generates a file of encrypted bytes
	 * @param key block number
	 * @param value encrypted block-sized bytes
	 * @param context to write new key values to;
	 */
	public void reduce(IntWritable key, BytesWritable value, Context context)
			throws IOException, InterruptedException
	{
		context.write(key, value);
	}
}
