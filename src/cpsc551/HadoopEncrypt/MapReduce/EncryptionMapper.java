/**
 * Chad Wyszynski for CPSC 551
 */
package cpsc551.HadoopEncrypt.MapReduce;

import java.io.IOException;
import java.math.BigInteger;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.IntWritable;
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
	extends Mapper<LongWritable, Text,	LongWritable, Text> 
{
	//TODO give this guy the encryption function and block size, setEncrypter?
	
	private Encrypter encrypter;
	private SecretKey key;
	
	public EncryptionMapper() throws Exception
	{ 
		key = KeyGenerator.getInstance("AES").generateKey();
		encrypter = new Encrypter(key);
	};
	
	/**
	 * Creates a EncryptionMapper with a given key
	 * @param key key to use for encryption
	 */
	public EncryptionMapper(SecretKey key)
	{
		encrypter = new Encrypter(key);
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
		context.write(
				key, 
				new Text("Key: " 
							+ new BigInteger(1, this.key.getEncoded()).toString() 
							+ "\n" + encrypter.encrypt(value.getBytes()))
		);
	}
}
