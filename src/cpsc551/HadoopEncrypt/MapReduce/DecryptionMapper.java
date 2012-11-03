/**
 * Chad Wyszynski for CPSC 551
 */
package cpsc551.HadoopEncrypt.MapReduce;

import java.io.IOException;

import javax.crypto.SecretKey;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Mapper;

import cpsc551.HadoopEncrypt.Encrypter.Encrypter;

/**
 * Descrypts data 
 * @author Chad Wyszynski
 *
 */
public class DecryptionMapper 
	extends Mapper<IntWritable, BytesWritable,	IntWritable, BytesWritable> 
{
	//TODO give this guy the encryption function and block size, setEncrypter?
	
	private Encrypter encrypter;

	/**
	 * Creates a DecryptionMapper with a given ken
	 * @param key key to use for decryption
	 */
	public DecryptionMapper(SecretKey key)
	{
		encrypter = new Encrypter(key);
	}
	
	/**
	 * Generates (block number, encrypted block) key-value pairs
	 * @param key block number
	 * @param value byte string to be encrypted
	 * @param context to write new key values to;
	 */
	public void map(IntWritable key, BytesWritable value, Context context)
			throws IOException, InterruptedException
	{
		context.write(
				key, 
				new BytesWritable(encrypter.decrypt(value.getBytes()))
		);
	}
}
