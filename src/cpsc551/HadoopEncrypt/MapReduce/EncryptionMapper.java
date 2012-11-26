/**
 * Chad Wyszynski for CPSC 551
 */
package cpsc551.HadoopEncrypt.MapReduce;

import java.io.IOException;

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
	
	public EncryptionMapper()
	{ 
		encrypter = null;
	};
	
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
		if(encrypter == null)
		{
			Configuration conf = context.getConfiguration();
			encrypter = createEncrypter(
							conf.get("encryptionKey").toCharArray());
		}
		context.write
		(
			key, 
			new BytesWritable(encrypter.encrypt(value.toString().getBytes()))
		);
	}
	
	/**
	 * Creates an encrypter from a given key
	 * @param encryptionKey encryption key to use for encryption/decryption
	 * @return encrypter or null
	 */
	private Encrypter createEncrypter(char[] encryptionKey)
	{
		Encrypter e = null;
		try
		{
			e = new Encrypter(encryptionKey);
		}
		catch(Exception exception) {}
		return e;
	}
}
