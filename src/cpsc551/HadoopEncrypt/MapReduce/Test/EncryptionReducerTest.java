/**
 * for CPSC 551 by Chad Wyszynski
 */
package cpsc551.HadoopEncrypt.MapReduce.Test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNot;

import java.io.IOException;
import java.util.List;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mrunit.ReduceDriver;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.apache.hadoop.mrunit.types.Pair;

import cpsc551.HadoopEncrypt.Encrypter.Encrypter;
import cpsc551.HadoopEncrypt.MapReduce.EncryptionMapper;
import cpsc551.HadoopEncrypt.MapReduce.EncryptionReducer;

/**
 * Tests EncryptionReducer
 * @author Chad Wyszynski
 *
 */
public class EncryptionReducerTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	  @Test
	  public void testConcatenateBytes() throws IOException, 
	  InterruptedException {
		  //TODO set up reducer test
		  new ReduceDriver<IntWritable, BytesWritable, IntWritable, BytesWritable>()
	      .withReducer(new EncryptionReducer())
	      .withInputKey(new Text("1950"))
	      .withInputValues(Arrays.asList(new IntWritable(10), new IntWritable(5)))
	      .withOutput(new Text("1950"), new IntWritable(10))
	      .runTest();
	  }


}
