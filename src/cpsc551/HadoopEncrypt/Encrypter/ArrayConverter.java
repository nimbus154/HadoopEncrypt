/**
 * 
 */
package cpsc551.HadoopEncrypt.Encrypter;

import java.util.List;

/**
 * @author nimbus154
 *
 */
public class ArrayConverter 
{
	/**
	 * Wrapper for handling byte and Byte
	 * @param list list to convert to primitive
	 * @return array of primitive bytes
	 */
	public static byte[] toByteArray(List<Byte> list)
	{
		byte[] b = new byte[list.size()];
		for(int i = 0; i < list.size(); i++)
			b[i] = list.get(i);
		return b;
	}
}
