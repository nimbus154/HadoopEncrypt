/**
 * for CSPSC 551 by Chad Wyszynski 
 */
package cpsc551.HadoopEncrypt.MapReduce.Test;

import static org.junit.Assert.*;

import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNot;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

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

	private SecretKey key;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		key = KeyGenerator.getInstance("AES").generateKey();
	}

	/**
	 * Test method for {@link cpsc551.HadoopEncrypt.MapReduce.EncryptionMapper#map(org.apache.hadoop.io.IntWritable, org.apache.hadoop.io.BytesWritable, org.apache.hadoop.mapreduce.Mapper.Context)}.
	 * If EncryptionMapper is not working properly, this may not work properly
	 */
	@Test
	public void testMapIntWritableBytesWritableContext() throws Exception {
		byte[] input = {0, 1, 2, 3, 4};		
		//Run Encryption Mapper		
		List<Pair<IntWritable, BytesWritable>> result = 
				new MapDriver<IntWritable, BytesWritable, IntWritable, BytesWritable>()
					.withMapper(new EncryptionMapper(key))
					.withInput(new IntWritable(1), new BytesWritable(input))
					.run();
		
		//TODO - figure out where the 8 mysterious bytes come from
		byte[] encrypted = result.get(0).getSecond().getBytes();
		for(byte b : encrypted)
			System.out.print(b + " ");
		byte[] test = new byte[16];
		
		for(int i = 0; i < 16; i ++)
			test[i] = encrypted[i];
		
		//Now, test DecryptionMapper
		new MapDriver<IntWritable, BytesWritable, IntWritable, BytesWritable>()
					.withMapper(new DecryptionMapper(key))
					.withInput(new IntWritable(1), new BytesWritable(test))
					.withOutput(new IntWritable(1), new BytesWritable(input))
					.runTest();
	}
}
