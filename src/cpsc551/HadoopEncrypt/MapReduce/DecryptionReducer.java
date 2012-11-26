/**
 * 
 */
package cpsc551.HadoopEncrypt.MapReduce;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/**
 * Groups all decrypted data; assembles it as original file
 * @author Chad Wyszynski
 * Input key - Blank line of text
 * Input value - Decrypted text
 * Output key - blank text
 * Output value - Decrypted file
 */
public class DecryptionReducer extends
		Reducer<Text, Text,	Text, Text> 
{
	public DecryptionReducer() { } ;
	
	/**
	 * Generates a file of plain text
	 * @param key file line number
	 * @param value encrypted line
	 * @param context to write new key values to;
	 */
	@Override
	public void reduce(Text key, Iterable<Text> value, Context context)
			throws IOException, InterruptedException
	{
		//simple write encrypted data to file
		for(Text decrypted : value)
			context.write(new Text(), decrypted);		
	}
}
