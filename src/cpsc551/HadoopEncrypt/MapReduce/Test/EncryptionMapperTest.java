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

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * Test method for {@link cpsc551.HadoopEncrypt.MapReduce.EncryptionMapper#map(org.apache.hadoop.io.IntWritable, org.apache.hadoop.io.BytesWritable, org.apache.hadoop.mapreduce.Mapper.Context)}.
	 */
	@Test
	public void testMapIntWritableBytesWritableContext() throws Exception {
		String plaintext = "123456";
		Encrypter e = new Encrypter("asdf".toCharArray());
		List<Pair<LongWritable, BytesWritable>> result = 
				new MapDriver<LongWritable, Text, LongWritable, BytesWritable>()
					.withMapper(new EncryptionMapper(e))
					.withInput(new LongWritable(1), new Text(plaintext))
					.run();
		String encrypted = result.get(0).getSecond().toString();
		assertThat(plaintext, IsNot.not(IsEqual.equalTo(encrypted)));
		assertFalse(result.get(0).getSecond().getBytes().length == 0);
	}
}
