/**
 * 
 */
package cpsc551.HadoopEncrypt.Encrypter.Test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;

import org.junit.Before;
import org.junit.Test;

import cpsc551.HadoopEncrypt.Encrypter.Blocker;

/**
 * @author nimbus154
 *
 */
public class BlockerTest 
{
	private Blocker blocker;
	private static final byte BLOCK_SIZE = 4;
	
	/**
	 * String that spans multiple blocks
	 */
	private static final String MULTI_BLOCK = 
			"I'm going to be broken into blocks!";

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception 
	{
		blocker = new Blocker(BLOCK_SIZE);
	}
	
	/**
	 * Test method for {@link cpsc551.HadoopEncrypt.Encrypter.Blocker#getBlockSize()}.
	 */
	@Test
	public void testGetBlockSize() 
	{
		Blocker b = new Blocker(10);
		assertEquals(10, b.getBlockSize());
	}

	@Test
	public void testGetBlocks()
	{
		byte[] actual = TestHelper.getBlockFromString(MULTI_BLOCK, blocker);
		
		assertEquals(blocker.getBlockSize(), actual.length);
		
		TestHelper.assertBlocksEqual(MULTI_BLOCK.getBytes(), actual);
	}
	
	/**
	 * Tests behavior of blocker if input stream is smaller than block size.
	 * It should read the remaining bytes; that's all.
	 */
	@Test
	public void testGetBlocksTooSmall()
	{
		String s = "a";
		byte[] actual = TestHelper.getBlockFromString(s, blocker);
		
		assertEquals(s.getBytes().length, actual.length);
		TestHelper.assertBlocksEqual(s.getBytes(), actual);
	}
	
	/**
	 * Tests getting the next block a multi-block sequence
	 */
	@Test
	public void testNextBlock()
	{
		ByteArrayInputStream inputStream = 
				new ByteArrayInputStream
				(
					MULTI_BLOCK.getBytes(Charset.defaultCharset())
				);
		blocker = new Blocker(BLOCK_SIZE, inputStream);
		byte[] first = blocker.next();
		byte[] expected = 
				TestHelper.getByteRange(MULTI_BLOCK.getBytes(), 0, BLOCK_SIZE);
		TestHelper.assertBlocksEqual(expected, first);
		byte[] second = blocker.next();
		expected = TestHelper.getByteRange(MULTI_BLOCK.getBytes(), 
											BLOCK_SIZE,
											BLOCK_SIZE * 2);
		TestHelper.assertBlocksEqual(expected, second);
	}
	
	/**
	 * Tests getting the all blocks in a multi-block sequence
	 */
	@Test
	public void testAllBlocks()
	{
		ByteArrayInputStream inputStream = 
				new ByteArrayInputStream
				(
					MULTI_BLOCK.getBytes(Charset.defaultCharset())
				);
		Blocker blocker = new Blocker(BLOCK_SIZE, inputStream);
		
		byte[] total = new byte[MULTI_BLOCK.getBytes().length];
		byte[] actual = blocker.next();
		
		int lastIndex = 0;
		while(actual != null)
		{
			for(int i = 0; i < actual.length; i++)
				total[lastIndex++] = actual[i];			
			actual = blocker.next();
		}
		assertEquals(MULTI_BLOCK.getBytes().length, total.length);
		TestHelper.assertBlocksEqual(MULTI_BLOCK.getBytes(), total);
	}


}
