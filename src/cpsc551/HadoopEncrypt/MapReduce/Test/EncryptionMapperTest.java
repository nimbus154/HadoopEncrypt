/**
 * for CSPSC 551 by Chad Wyszynski 
 */
package cpsc551.HadoopEncrypt.MapReduce.Test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;

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
	public void testMapIntWritableBytesWritableContext() {
		byte[] testIn = {0, 1, 2, 3, 4, 5, 6, 7};
		byte[] exOut = {1, 2, 3, 4, 5, 6, 7, 8};
		new MapDriver<IntWritable, BytesWritable, IntWritable, BytesWritable>()
			.withMapper(new EncryptionMapper())
			.withInput(new IntWritable(1), new BytesWritable(testIn))
			.withOutput(new IntWritable(1), new BytesWritable(exOut))
			.runTest();			
	}
}
