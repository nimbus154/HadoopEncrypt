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
import cpsc551.HadoopEncrypt.MapReduce.EncryptionMapper;

/**
 * Tests the EncryptionMapper
 * @author Chad Wyszynski
 *
 */
public class EncryptionMapperTest {

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
	 */
	@Test
	public void testMapIntWritableBytesWritableContext() throws Exception {
		Encrypter e = new Encrypter(key);
		byte[] plaintext = {0, 1, 2, 3, 4, 5, 6, 7};	
		BytesWritable input = new BytesWritable(plaintext);
		List<Pair<IntWritable, BytesWritable>> result = 
				new MapDriver<IntWritable, BytesWritable, IntWritable, BytesWritable>()
					.withMapper(new EncryptionMapper(e))
					.withInput(new IntWritable(1), input)
					.run();
		byte[] encrypted = result.get(0).getSecond().getBytes();
		for(int i = 0; i < plaintext.length; i++)
			assertThat(plaintext[i], IsNot.not(IsEqual.equalTo(encrypted[i])));
		
		
		byte[] expected = e.encrypt(input.getBytes());		
		assertEquals(expected.length, encrypted.length);
		
	}
}
