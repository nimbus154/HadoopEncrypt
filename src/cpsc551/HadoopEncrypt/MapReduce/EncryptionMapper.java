/**
 * Chad Wyszynski for CPSC 551
 */
package cpsc551.HadoopEncrypt.MapReduce;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Mapper;

import cpsc551.HadoopEncrypt.Encrypter.Encrypter;

/**
 * Encrypts 
 * @author Chad Wyszynski
 *
 */
public class EncryptionMapper 
	extends Mapper<IntWritable, BytesWritable,	IntWritable, BytesWritable> 
{
	//TODO give this guy the encryption function and block size, setEncrypter?
	
	private Encrypter encrypter;
	
	public EncryptionMapper() throws NoSuchAlgorithmException
	{
		//TODO save/reuse key
		encrypter = new Encrypter(KeyGenerator.getInstance("AES").generateKey()); 
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
				new BytesWritable(encrypter.encrypt(value.getBytes()))
		);
	}
}
