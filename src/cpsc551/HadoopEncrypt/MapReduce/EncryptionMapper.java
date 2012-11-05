/**
 * Chad Wyszynski for CPSC 551
 */
package cpsc551.HadoopEncrypt.MapReduce;

import java.io.IOException;
import java.math.BigInteger;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

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
		this(KeyGenerator.getInstance("AES").generateKey());
	};
	
	/**
	 * Creates a EncryptionMapper with a given key
	 * @param key key to use for encryption
	 */
	public EncryptionMapper(SecretKey key)
	{
		this.key = key;
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
		//write encryption key (!) and encrypted data to file
		//TODO find out a better way to pass the key
		context.write(
				key, 
				new Text(
					"~"
					+ new BigInteger(1, this.key.getEncoded()).toString(16) 
					+ "~" + new String(encrypter.encrypt(value.getBytes()))
				)
		);
	}
}
