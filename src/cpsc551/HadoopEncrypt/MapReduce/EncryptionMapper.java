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
 * Encrypts lines of text extracted from files
 * @author Chad Wyszynski
 * Input key    - EncryptionMapper receives lines of text as input. LongWritable
 * 			      refers to the number of the first character in the line.
 * Input value  - text of line to encrypt
 * Output key   - same as input key
 * Output value - encrypted bytes
 */
public class EncryptionMapper 
	extends Mapper<LongWritable, Text,	LongWritable, BytesWritable> 
{	
	/**
	 * Performs encryption operations
	 */
	private Encrypter encrypter;
	
	/**
	 * Default constructor. Called by Hadoop MapReduce.
	 */
	public EncryptionMapper()
	{ 
		//encrypter will be initialized with user-supplied key
		encrypter = null; 
	};
	
	/**
	 * Initializes EncryptionMapper with an encrypter.
	 * Useful for testing, but doesn't work in production.
	 * @param encrypter encrypter to use for encryption operations
	 */
	public EncryptionMapper(Encrypter encrypter)
	{
		this.encrypter = encrypter;
	}
	
	/**
	 * Encrypts lines of text
	 * @param key position of first character in line
	 * @param value plain text to be encrypted
	 * @param context to write new key values to;
	 */
	public void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException
	{	
		if(encrypter == null) //production this will always be true
		{
			//Extract user-supplied encryption key
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
