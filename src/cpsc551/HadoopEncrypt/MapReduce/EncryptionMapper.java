/**
 * Chad Wyszynski for CPSC 551
 */
package cpsc551.HadoopEncrypt.MapReduce;

import java.io.IOException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import cpsc551.HadoopEncrypt.Encrypter.Encrypter;

/**
 * Encrypts 
 * @author Chad Wyszynski
 *
 */
public class EncryptionMapper 
	extends Mapper<LongWritable, Text,	LongWritable, BytesWritable> 
{	
	private Encrypter encrypter;
	
	public EncryptionMapper() throws Exception
	{ 
		this(KeyGenerator.getInstance("AES").generateKey());
	};
	
	/**
	 * Creates a EncryptionMapper with a given key
	 * @param key key to use for encryption
	 */
	public EncryptionMapper(SecretKey key)
	{
		try{
		encrypter = new Encrypter(key);
		}catch(Exception e) {}
	}
	
	public EncryptionMapper(Encrypter encrypter)
	{
		this.encrypter = encrypter;
	}
	
	/**
	 * Generates (block number, encrypted block) key-value pairs
	 * @param key block number
	 * @param value byte string to be encrypted
	 * @param context to write new key values to;
	 */
	public void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException
	{	
		Configuration conf = context.getConfiguration();
		String encryptionKey = conf.get("encryptionKey");
		try 
		{
			encrypter = new Encrypter(encryptionKey.toCharArray());
		}
		catch(Exception e) //encryption initialization errors
		{
			context.write(new LongWritable(-1), new BytesWritable());
		}
		context.write
		(
			key, 
			new BytesWritable(encrypter.encrypt(value.toString().getBytes()))
		);
	}
}
