/**
 * Tests Blocker and Encrypter's integrated functions
 */
package cpsc551.HadoopEncrypt.Encrypter.Test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

import javax.crypto.KeyGenerator;

import org.junit.Before;
import org.junit.Test;

import cpsc551.HadoopEncrypt.Encrypter.Blocker;
import cpsc551.HadoopEncrypt.Encrypter.Encrypter;

/**
 * Tests the integration of Blocker and Encrypter
 * @author Chad Wyszynski
 *
 */
public class IntegrationTest {

	private Encrypter encrypter;
	private Blocker blocker;
	/**
	 * A string that constitutes more than one encryption block
	 */
	private final String MULTI_BLOCK =
			"angelheaded hipsters burning for the "
			+ " ancient heavenly connection to the starry dynamo in the "
			+ "machienry of night";
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception 
	{
		encrypter = 
				new Encrypter(KeyGenerator.getInstance("AES").generateKey());
		blocker = new Blocker(encrypter.getBlockSize());
	}
	
	/**
	 * Tests basic encryption 
	 */
	@Test
	public void testBasicEncryptDecrypt() 
	{
		TestHelper.assertEncryptDecryptFirstBlock
		(
			MULTI_BLOCK,
			blocker,
			encrypter
	    );
	}
	
	/**
	 * Test blocking, encryption and decryption with a string smaller than
	 * the block size
	 */
	@Test
	public void testSmallerThanBlockSize()
	{
		final String INPUT = "a";
		assertTrue(INPUT.getBytes().length < blocker.getBlockSize());
		TestHelper.assertEncryptDecryptFirstBlock(INPUT, blocker, encrypter);
	}
	
	/**
	 * Tests encryption/decryption of an entire string
	 */
	@Test
	public void testWholeString()
	{
		ByteArrayInputStream inputStream = 
				new ByteArrayInputStream(MULTI_BLOCK.getBytes());
		blocker = new Blocker(encrypter.getBlockSize(), inputStream);
		
		ArrayList<byte[]> transformedBlocks = new ArrayList<byte[]>();
		byte[] actual = blocker.next();
		while(actual != null)
		{
			transformedBlocks.add(encrypter.encrypt(actual));						
			actual = blocker.next();
		}
		byte[] encrypted = TestHelper.concatenateByteList(transformedBlocks);
		TestHelper.assertBlocksNotEqual(MULTI_BLOCK.getBytes(), encrypted);

		inputStream = new ByteArrayInputStream(encrypted);
		blocker = new Blocker(encrypter.getBlockSize(), inputStream);
		transformedBlocks.clear();
		actual = blocker.next();
		while(actual != null)
		{
			transformedBlocks.add(encrypter.decrypt(actual));						
			actual = blocker.next();			 
		}
		
		byte[] decrypted = TestHelper.concatenateByteList(transformedBlocks);
		TestHelper.assertBlocksNotEqual(MULTI_BLOCK.getBytes(), decrypted);
		
		//TODO test suite for all tests
	}
}
