/**
 * Chad Wyszynski for CPSC 551
 */
package cpsc551.HadoopEncrypt.MapReduce;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Scanner;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Mapper.Context;

import cpsc551.HadoopEncrypt.Encrypter.Encrypter;

/**
 * Descrypts data 
 * @author Chad Wyszynski
 *
 */
public class DecryptionMapper 
	extends Mapper<LongWritable, Text,	LongWritable, Text> 
{
	/**
	 * Performs decryption operations
	 */
	private Encrypter encrypter;
	
	/**
	 * Key used for decryption
	 */
	private SecretKey key;
	
	public DecryptionMapper() throws Exception { };
	
	/**
	 * Creates a EncryptionMapper with a given key
	 * @param key key to use for encryption
	 */
	public DecryptionMapper(SecretKey key)
	{
		this.key = key;
		encrypter = new Encrypter(key);
	}
	
	public DecryptionMapper(Encrypter encrypter)
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
		//parse line input:
		//get key
		//get encrypted data
		Scanner scanner = new Scanner(value.toString());
		scanner.useDelimiter("~");
		scanner.next(); //ignore first part; old mapreduce key
		encrypter = new Encrypter(extractKey(scanner.next())); //encryption key
		String encrypted = scanner.next(); //encrypted data
		
		//TODO find out a better way to pass the key
		context.write(
				key, 
				new Text(
					//"KEY: "
					//+ new BigInteger(1, this.key.getEncoded()).toString() 
					 "ENCRYPTED DATA" + encrypted
				)
		);
	}
	
	/**
	 * Retrieve the encryption key from the input
	 * @param keystring the key as a string
	 * @return the encryption key stored in the input
	 */
	private SecretKey extractKey(String keystring)
	{
		
		return null;
	}
}
