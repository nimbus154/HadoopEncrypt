/**
 * for CPSC 551 by Chad Wyszynski
 */
package cpsc551.HadoopEncrypt.MapReduce;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * Runs the Hadoop encryption MapReduce job
 * @author Chad Wyszynski
 *
 */
public class EncryptionDriver extends Configured implements Tool {
	
	/**
	 * Print usage instructions for the class
	 */
	private void printUsage()
	{
	      System.err.printf("Usage: %s [generic options] " 
	    		  + "<encrypt|decrypt> <input> <output>\n",
	          getClass().getSimpleName());
	      ToolRunner.printGenericCommandUsage(System.err);
	}

	  @Override
	  public int run(String[] args) throws Exception {
		//check command line arguments
	    if (args.length != 3) {
	    	printUsage();
	      return -1;
	    }
	    
	    //create a new Hadoop job, set all options
	    Job job = new Job(getConf(), "HadoopEncrypt");
	    job.setJarByClass(getClass());

	    FileInputFormat.addInputPath(job, new Path(args[1]));
	    FileOutputFormat.setOutputPath(job, new Path(args[2]));
	    
	    if(args[0].equals("encrypt"))
	    	job.setMapperClass(EncryptionMapper.class);
	    else if(args[0].equals("decrypt"))
	    	job.setMapperClass(DecryptionMapper.class);
	    else
	    	throw new IllegalArgumentException(
	    			"Must specify either encrypt or decrypt");
	    
	    job.setCombinerClass(EncryptionReducer.class);
	    job.setReducerClass(EncryptionReducer.class);
	    
	    job.setOutputKeyClass(Text.class);
	    job.setOutputValueClass(BytesWritable.class);
	    
	    return job.waitForCompletion(true) ? 0 : 1;
	  }
	  
	  /**
	   * Main entry point for encryption
	   * @param args
	   * @throws Exception
	   */
	  public static void main(String[] args) throws Exception {
	    int exitCode = ToolRunner.run(new EncryptionDriver(), args);
	    System.exit(exitCode);
	  }
	}
