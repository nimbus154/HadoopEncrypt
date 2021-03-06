/**
 * Chad Wyszynski for CPSC 551
 */
package cpsc551.HadoopEncrypt.MapReduce;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import cpsc551.HadoopEncrypt.Encrypter.ArrayConverter;
import cpsc551.HadoopEncrypt.Encrypter.Encrypter;

/**
 * Decrypts data encrypted by Encryption Mapper
 * @author Chad Wyszynski
 * Input key    - DecryptionMapper receives lines of text as input. LongWritable
 * 			      refers to the number of the first character in the line.
 * Input value  - text of line
 * Output key   - blank text
 * Output value - decrypted text
 */
public class DecryptionMapper 
	extends Mapper<LongWritable, Text,	Text, Text> 
{
	/**
	 * Performs decryption operations
	 */
	private Encrypter decrypter;
	
	/**
	 * Generic constructor. This is the constructor called by Hadoop
	 * @throws Exception
	 */
	public DecryptionMapper() throws Exception 
	{ 
		decrypter = null;
	};
	
	/**
	 * Initializes mapper with an encrypter.
	 * Useful for testing.
	 * @param decrypter encrypter to user for mapping
	 */
	public DecryptionMapper(Encrypter decrypter)
	{
		this.decrypter = decrypter;
	}
	
	/**
	 * Decrypts data
	 * @param key character number in file
	 * @param value encrypted data
	 * @param context to write new key values to;
	 * OUTPUT: blank key, decrypted data
	 */
	public void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException
	{
		if(decrypter == null)
		{
			Configuration conf = context.getConfiguration();
			decrypter = createEncrypter(
							conf.get("encryptionKey").toCharArray());
		}
		//set default values in case of failure
		byte[] decrypted = { };
		
		//retrieve bytes from value parameter
		Scanner scanner = new Scanner(value.toString());
		getCharNumber(scanner); //extract old key, not used for calculations
		try
		{
			decrypted = decrypter.decrypt(getEncryptedData(scanner));
		}
		catch(Exception e)	{ } //empty catch b/c error values already set
		context.write(new Text(), new Text(decrypted));
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
			int nextVal = Integer.parseInt(scanner.next(), 16); //parse as hex
			encryptedData.add((byte) nextVal);
		}
		
		return ArrayConverter.toByteArray(encryptedData);
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
