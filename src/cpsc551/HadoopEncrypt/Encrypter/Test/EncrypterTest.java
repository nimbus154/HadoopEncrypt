/**
 * 
 */
package cpsc551.HadoopEncrypt.Encrypter.Test;

import static org.junit.Assert.*;

import javax.crypto.KeyGenerator;

import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNot;
import org.junit.Before;
import org.junit.Test;

import cpsc551.HadoopEncrypt.Encrypter.Encrypter;

/**
 * @author nimbus154
 *
 */
public class EncrypterTest 
{
	Encrypter e;

	/**
	 * Builds a new encrypter for each test
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception 
	{
		try
		{
			e = new Encrypter(KeyGenerator.getInstance("AES").generateKey());
		}
		catch(Exception e) {};
	}

	/**
	 * Tests basic encryption
	 */
	@Test
	public void testEncrypt() 
	{
		String original = "A secret";
		
		byte[] encrypted = e.encrypt(original.getBytes());
		assertThat(original.getBytes(), IsNot.not(IsEqual.equalTo(encrypted)));
	}
	
	/**
	 * Tests basic encryption
	 */
	@Test
	public void testDecrypt() 
	{
		String original = "A secret";
		byte[] decrypted = e.decrypt(e.encrypt(original.getBytes()));
		assertEquals(original, new String(decrypted));
	}
	
	/**
	 * Tests password-based encryption
	 */
	@Test
	public void testPBE() throws Exception
	{
		char[] key = "Key".toCharArray();
		String original = "Secret";
		Encrypter pbEncrytper = new Encrypter(key);
		
		byte[] encrypted = pbEncrytper.encrypt(original.getBytes());
		byte[] decrypted = pbEncrytper.decrypt(encrypted);
		assertEquals(original, new String(decrypted));
	}
}
