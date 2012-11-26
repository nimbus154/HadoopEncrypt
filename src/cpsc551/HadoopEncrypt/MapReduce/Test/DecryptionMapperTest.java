/**
 * for CSPSC 551 by Chad Wyszynski 
 */
package cpsc551.HadoopEncrypt.MapReduce.Test;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.List;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.apache.hadoop.mrunit.types.Pair;

import cpsc551.HadoopEncrypt.Encrypter.Encrypter;
import cpsc551.HadoopEncrypt.MapReduce.DecryptionMapper;
import cpsc551.HadoopEncrypt.MapReduce.EncryptionMapper;

/**
 * Tests the DecryptionMapper
 * @author Chad Wyszynski
 *
 */
public class DecryptionMapperTest {

	/**
	 * Test method for {@link cpsc551.HadoopEncrypt.MapReduce.EncryptionMapper#map(org.apache.hadoop.io.IntWritable, org.apache.hadoop.io.BytesWritable, org.apache.hadoop.mapreduce.Mapper.Context)}.
	 * If EncryptionMapper is not working properly, this may not work properly
	 */
	@Test
	public void testMapIntWritableBytesWritableContext() throws Exception {
		String original = "secret";
		Encrypter encrypter = new Encrypter("password".toCharArray());
		//Run Encryption Mapper		
		List<Pair<LongWritable, BytesWritable>> result = 
				new MapDriver<LongWritable, Text, LongWritable, BytesWritable>()
					.withMapper(new EncryptionMapper(encrypter))
					.withInput(new LongWritable(0), new Text(original))
					.run();
		
		byte[] encryptedBytes = result.get(0).getSecond().getBytes();
		String encrypted = new String(encryptedBytes);
		assertFalse(original == encrypted);
		
		byte[] toDecrypt = new byte[encrypter.getBlockSize()];
		//Hadoop adds a few bytes to the end, they must be stripped for testing
		//Surprisingly, this is a non-issue in production
		for(int i = 0; i < toDecrypt.length; i ++)
			toDecrypt[i] = encryptedBytes[i];
		String s = "";
		for(byte b : toDecrypt)
		{
			s += String.format("%02x", b) + " ";
		}
		Text input = new Text("123	" + s);
		//Now, test DecryptionMapper
		new MapDriver<LongWritable, Text, Text, Text>()
			.withMapper(new DecryptionMapper(encrypter))
			.withInput(new LongWritable(0), input)
			.withOutput(new Text(), new Text(original))
			.runTest();
	}
}
