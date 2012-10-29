/**
 * Chad Wyszynski for CPSC 551
 */
package cpsc551.HadoopEncrypt.MapReduce;

import java.io.IOException;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Mapper;

/**
 * Encrypts 
 * @author Chad Wyszynski
 *
 */
public class EncryptionMapper 
	extends Mapper<IntWritable, BytesWritable,	IntWritable, BytesWritable> 
{
	//TODO give this guy the encryption function and block size, setEncrypter?
	
	/**
	 * Generates (block number, encrypted block) key-value pairs
	 * @param key block number
	 * @param value byte string to be encrypted
	 * @param context to write new key values to;
	 */
	public void map(IntWritable key, BytesWritable value, Context context)
			throws IOException, InterruptedException
	{
		byte[] plaintext = value.getBytes();
		for(int i = 0; i < plaintext.length; i++)
			plaintext[i]++;
		context.write(key, new BytesWritable(plaintext));
	}
}
