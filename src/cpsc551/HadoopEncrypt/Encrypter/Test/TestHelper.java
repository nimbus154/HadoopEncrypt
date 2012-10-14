/**
 * 
 */
package cpsc551.HadoopEncrypt.Encrypter.Test;

import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNot;

import cpsc551.HadoopEncrypt.Encrypter.Blocker;
import cpsc551.HadoopEncrypt.Encrypter.Encrypter;

/**
 * Helper methods for Encrypter unit tests
 * @author Chad Wyszynski
 *
 */
public class TestHelper 
{
	/**
	 * Helper method for comparing the results of a blocker method 
	 * @param expected expected bytes
	 * @param actual actual bytes
	 */
	public static void assertBlocksNotEqual(byte[] expected, byte[] actual)
	{
		assertTrue(expected.length != 0);
		assertTrue(actual.length != 0);
		assertTrue(actual != null);
		for(int i = 0; i < Math.min(expected.length, actual.length); i++)
			assertThat(expected, IsNot.not(IsEqual.equalTo(actual)));
	}
	
	/**
	 * Helper method for comparing the results of a blocker method 
	 * @param expected expected bytes
	 * @param actual actual bytes
	 */
	public static void assertBlocksEqual(byte[] expected, byte[] actual)
	{
		assertTrue(expected.length != 0);
		assertTrue(actual.length != 0);
		assertTrue(actual != null);
		for(int i = 0; i < Math.min(expected.length, actual.length); i++)
			assertEquals(expected[i], actual[i]);
	}
	
	/**
	 * Gets a block-size byte array from string
	 * @param s string from which to get byte array
	 * @param blocker used to retrieve block
	 * @return byte first block of bytes from s, or null on IOException
	 */
	public static byte[] getBlockFromString(String s, Blocker blocker)
	{
		ByteArrayInputStream inputStream = 
				new ByteArrayInputStream(s.getBytes(Charset.defaultCharset()));
		
		byte[] actual = null;
		try
		{
			actual = blocker.getBlocks(inputStream);
		}
		catch(IOException e) {}
		return actual;
	}
	
	public static void assertEncryptDecrypt(byte[] block, Encrypter encrypter)
	{
		byte[] encrypted = encrypter.encrypt(block);
		assertThat(block, IsNot.not(IsEqual.equalTo(encrypted)));
		
		byte[] decrypted = encrypter.decrypt(encrypted);
		assertBlocksEqual(block, decrypted);
	}
	/**
	 * Helper method for encrypting/decrypting first block of a string
	 * @param INPUT string for which to test encryption/decrpytion
	 * @param blocker blocker to get first block
	 * @param encrypter used for encryption and decryption
	 */
	public static void assertEncryptDecryptFirstBlock(final String INPUT, 
														Blocker blocker,
														Encrypter encrypter)
	{
		TestHelper.assertEncryptDecrypt
		(
				TestHelper.getBlockFromString(INPUT, blocker),
				encrypter
		);
	}
	
	/**
	 * Return a subset of an array
	 * @param array array from which to extract a subset
	 * @param start starting index
	 * @param end ending index-- not inclusive
	 * @return array of elements in range start to end
	 */
	public static byte[] getByteRange(byte[] array, int start, int end)
	{
		byte[] subset = new byte[end - start];
		int j = 0;
		for(int i = start; i < end; i++)
			subset[j++] = array[i];
		return subset;
	}
	
	/**
	 * Concatenates an array list into a large byte array
	 * @param list list whose members to concatenate
	 * @return a byte array of all members in list
	 */
	public static byte[] concatenateByteList(ArrayList<byte[]> list)
	{
		int size = 0;
		for(byte[] b : list)
			size += b.length;
		byte[] concatenated = new byte[size];
		int currentIndex = 0;
		for(byte[] b : list)
		{
			for(int i = 0; i < b.length; i++)
				concatenated[currentIndex++] = b[i];
		}
		
		return concatenated;
	}
}
