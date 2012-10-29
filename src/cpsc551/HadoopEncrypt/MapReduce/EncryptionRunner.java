/**
 * for CPSC 551 by Chad Wyszynski
 */
package cpsc551.HadoopEncrypt.MapReduce;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * Runs the Hadoop encryption MapReduce job
 * @author Chad Wyszynski
 *
 */
public class EncryptionRunner 
{

	/**
	 * @param args 0: input filename, 1: output file name
	 */
	public static void main(String[] args) throws Exception
	{
	    if (args.length != 2) 
	    {
	        System.err.println("Usage: HadoopEncrypt <input path> <output path>");
	        System.exit(-1);
	      }
	      
	      Job job = new Job();
	      job.setJarByClass(EncryptionRunner.class);
	      job.setJobName("Max temperature");

	      FileInputFormat.addInputPath(job, new Path(args[0]));
	      FileOutputFormat.setOutputPath(job, new Path(args[1]));
	      
	      job.setMapperClass(EncryptionMapper.class);
	      job.setReducerClass(EncryptionReducer.class);

	      job.setOutputKeyClass(IntWritable.class);
	      job.setOutputValueClass(BytesWritable.class);
	      
	      System.exit(job.waitForCompletion(true) ? 0 : 1);
	}

}
