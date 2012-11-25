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

import org.apache.hadoop.conf.Configuration;
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
	extends Mapper<LongWritable, Text,	LongWritable, Text> 
{
	/**
	 * Performs decryption operations
	 */
	private Encrypter decrypter;
	
	/**
	 * Generic constructor. This is the constructor called by Hadoop
	 * @throws Exception
	 */
	public DecryptionMapper() throws Exception { };
	
	/**
	 * Creates a EncryptionMapper with a given key
	 * @param key key to use for encryption
	 * @throws Exception encryption related exceptions
	 */
	public DecryptionMapper(SecretKey key) throws Exception
	{
		decrypter = new Encrypter(key);
	}
	
	public DecryptionMapper(Encrypter decrypter)
	{
		this.decrypter = decrypter;
	}
	
	/**
	 * Generates (block number, encrypted block) key-value pairs
	 * @param key block number
	 * @param value byte string to be encrypted
	 * @param context to write new key values to;
	 */
	public void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException
	{//TODO make notes on what the keys and values are
		//Get encryption key
		Configuration conf = context.getConfiguration();
		String encryptionKey = conf.get("encryptionKey");
		
		//retrieve bytes from value parameter
		Scanner scanner = new Scanner(value.toString());
		//set default values in case of failure
		byte[] decrypted = { };
		LongWritable charNumber = new LongWritable(-1);	
		try
		{
			charNumber = getCharNumber(scanner);
			decrypter = new Encrypter(encryptionKey.toCharArray());
			decrypted = decrypter.decrypt(getEncryptedData(scanner));
		}
		catch(Exception e)	{ } //empty catch b/c error values already set
		
		context.write(charNumber, new Text(decrypted + "\n"));
	}
	
	/**
	 * Returns the character number associated with encrypted text.
	 * @precondition scanner has not yet read any bytes
	 * @postcondition scanner has advanced one place; pointing to
	 * encrypted data
	 * @param scanner refers to map's value parameter
	 * @return the character number associated with encrypted text.
	 * Character number is the character position of the encrypted text
	 * in the original file. 
	 */
	private LongWritable getCharNumber(Scanner scanner)
	{
		long parsed = -1;
		try
		{
			parsed = Long.parseLong(scanner.next());
		}
		catch(NumberFormatException e)	{ } //error val already set
		
		return new LongWritable(parsed);
	}
	
	/**
	 * Retrieves all encrypted bytes from scanner
	 * @precondition scanner has already moved past first element in value
	 * (which is the charNumber, not encrypted text)
	 * @param scanner points to space-delimited sequence of encrypted bytes
	 * @return all encrypted bytes
	 */
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
}
