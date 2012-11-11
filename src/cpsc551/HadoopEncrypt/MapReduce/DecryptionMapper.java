/**
 * Chad Wyszynski for CPSC 551
 */
package cpsc551.HadoopEncrypt.MapReduce;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Scanner;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import cpsc551.HadoopEncrypt.Encrypter.ArrayConverter;
import cpsc551.HadoopEncrypt.Encrypter.Encrypter;

/**
 * Descrypts data 
 * @author Chad Wyszynski
 *
 */
public class DecryptionMapper 
	extends Mapper<LongWritable, Text,	Text, Text> 
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
		//encrypter = new Encrypter(key);
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
		Scanner scanner = new Scanner(value.toString());
		String s = "no exception";
		byte[] encrypted = { };
		String keystring = scanner.next();
		try{
		encrypter = new Encrypter(makeKey(keystring)); //encryption key
		encrypted = encrypter.decrypt(getEncryptedData(scanner));
		}
		catch(Exception e)
		{ 
			s = e.getMessage();
		}

		if(encrypted.length != 0)
			context.write(new Text(), new Text(encrypted));
		else
		{
			String bLength = Integer.toString(keystring.getBytes().length);
			context.write(new Text(bLength), new Text(s));
		}
	}
	
	private byte[] getEncryptedData(Scanner scanner) 
	{
		//make a byte array from the encrypted data
		ArrayList<Byte> encryptedData = new ArrayList<Byte>();
		while(scanner.hasNext())
		{
			int nextVal = Integer.parseInt(scanner.next(), 16);
			encryptedData.add((byte) nextVal);
		}
		
		return ArrayConverter.toByteArray(encryptedData);
	}

	/**
	 * Retrieve the encryption key from the input
	 * @param keystring the key as a string
	 * @return the encryption key stored in the input
	 */
	private SecretKey makeKey(String keystring)
	{
		byte[] encodedKey = new BigInteger(keystring, 16).toByteArray();
		return new SecretKeySpec(encodedKey, "AES");
	}
}
