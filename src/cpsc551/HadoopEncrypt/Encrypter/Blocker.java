/**
 * 
 */
package cpsc551.HadoopEncrypt.Encrypter;

import java.util.List;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Breaks a string or file into blocks to be encrypted
 * @author nimbus154
 *
 */
public class Blocker 
{
	/**
	 * Size of blocks
	 */
	private int blockSize;
	
	/**
	 * Stream of blocks from which to read
	 */
	private InputStream inputStream;
	
	/**
	 * Instantiates a blocker for a given block size
	 * @param blockSize size of blocks
	 */
	public Blocker(int blockSize)
	{
		this.blockSize = blockSize; 
		this.inputStream = null;
	}
	
	/**
	 * Instantiates a blocker for a given block size and input stream.
	 * @postcondition next() can be called
	 * @param blockSize size of blocks
	 */
	public Blocker(int blockSize, InputStream inputStream)
	{
		this.blockSize = blockSize;
		this.inputStream = inputStream;
	}
	
	/**
	 * Returns block size associated with a blocker instance
	 * @return block size associated with a blocker instance
	 */
	public int getBlockSize()
	{
		return this.blockSize;
	}
	
	/**
	 * Returns the next block-sized number of bytes from the byte stream 
	 * @param rawInput byte stream reader of input to be broken into blocks
	 * @postcondition rawInput's internal pointer will advance blockSize bytes
	 * 					 or to eof
	 * @throws IOException if rawInput refers to an invalid stream
	 * @return a block-sized number of bytes
	 */
	public byte[] getBlocks(InputStream rawInput) throws IOException
	{
		List<Byte> bytes = new ArrayList<Byte>();	
		for(int i = 0; i < blockSize; i++)
		{
			int nextByte = rawInput.read();
			
			if(nextByte != -1)
				bytes.add((byte)nextByte);
			else
				break;
		}
		
		return toByteArray(bytes);
	}
	
	/**
	 * Wrapper for handling byte and Byte
	 * @param list list to convert to primitive
	 * @return array of primitive bytes
	 */
	private byte[] toByteArray(List<Byte> list)
	{
		byte[] b = new byte[list.size()];
		for(int i = 0; i < list.size(); i++)
			b[i] = list.get(i);
		return b;
	}

	/**
	 * Sets the internal input stream; for getting blocks
	 * @postcondition next() can be called
	 * @param inputStream stream for which to retrieve blocks
	 */
	public void setInputStream(InputStream inputStream)
	{
		this.inputStream = inputStream;
	}
	
	/**
	 * Returns the next block in the internal input stream
	 * @return next block in internal input stream or null if at eof
	 */
	public byte[] next()
	{
		byte[] next = null;
		try
		{
			next = getBlocks(inputStream);
			if(next.length == 0)
				next = null;
		}
		catch(IOException e)
		{
			return null;
		}
		
		
		return next;
	}
}
