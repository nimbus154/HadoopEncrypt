/**
 * 
 */
package cpsc551.HadoopEncrypt.Encrypter;

import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

/**
 * Performs encryption and decryption operations
 * @author Chad Wyszynski
 *
 */
public class Encrypter 
{
	private Cipher encrypter;
	private Cipher decrypter;
	
	/**
	 * Creates a new Encrypter to perform encryption and decryption operations
	 * @param key key to be used with symmetric cipher
	 */
	public Encrypter(SecretKey key) throws Exception
	{
		encrypter = Cipher.getInstance("AES");
		decrypter = Cipher.getInstance("AES");
		encrypter.init(Cipher.ENCRYPT_MODE, key);
		decrypter.init(Cipher.DECRYPT_MODE, key);
	}
	
	/**
	 * Initializes an encrypter to use a user-supplied key
	 * @param key encryption key
	 * @throws Exception key and algorithm-related exceptions
	 */
	public Encrypter(char[] key) throws Exception
	{	
		String algorithm = "PBEWithMD5AndDES";
		
		//Create factory for generating keys
		SecretKeyFactory factory = SecretKeyFactory.getInstance(algorithm);
		
		//Generate key
		PBEKeySpec pwKey = new PBEKeySpec(key);
		SecretKey secretKey = factory.generateSecret(pwKey);
		
		//set up params: salt, # iterations
		PBEParameterSpec pbeParamSpec = 
				new PBEParameterSpec("12345678".getBytes(), 20); 
		
		//Initialize ciphers
		encrypter = Cipher.getInstance(algorithm);
		decrypter = Cipher.getInstance(algorithm);
		encrypter.init(Cipher.ENCRYPT_MODE, secretKey, pbeParamSpec);
		decrypter.init(Cipher.DECRYPT_MODE, secretKey, pbeParamSpec);
	}	
	
	/**
	 * Encrypts a string
	 * @param s string to encrypt
	 * @return encrypted version of string
	 */
	public byte[] encrypt(byte[] bytes)
	{
		try
		{			
			return encrypter.doFinal(bytes);
		}
		catch(Exception e) {}; //TODO exception catching?
		return null;
	}
	
	/**
	 * Decrypts an encrypted string string
	 * @param encrypted byte sequence to decrypt
	 * @return decrypted version of string
	 */
	public byte[] decrypt(byte[] encrypted)
	{
		try
		{
			return decrypter.doFinal(encrypted);
		}
	    catch (javax.crypto.BadPaddingException e) 
	    {
	    	byte[] b = {1};//TODO exception catching?
	    	return b;
	    } 
		catch (IllegalBlockSizeException e) 
		{
			byte[] b = {2};
	    	return b;
	    } 
	}

	/**
	 * Access block size of underlying cipher
	 * @return block size of underlying cipher
	 */
	public int getBlockSize()
	{
		return encrypter.getBlockSize();
	}
}
