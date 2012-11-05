/**
 * for CPSC 551 by Chad Wyszynski
 */
package cpsc551.HadoopEncrypt.MapReduce;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
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
public class DecryptionDriver extends Configured implements Tool {
	
	/**
	 * Print usage instructions for the class
	 */
	private void printUsage()
	{
	      System.err.printf("Usage: %s [generic options] <input> <output>\n",
	          getClass().getSimpleName());
	      ToolRunner.printGenericCommandUsage(System.err);
	}

	  @Override
	  public int run(String[] args) throws Exception {
		//check command line arguments
	    if (args.length != 2) {
	    	printUsage();
	      return -1;
	    }
	    
	    //create a new Hadoop job, set all options
	    Job job = new Job(getConf(), "HadoopDecrypt");
	    job.setJarByClass(getClass());

	    FileInputFormat.addInputPath(job, new Path(args[0]));
	    FileOutputFormat.setOutputPath(job, new Path(args[1]));
	    job.setMapperClass(DecryptionMapper.class);
	    job.setCombinerClass(DecryptionReducer.class);
	    job.setReducerClass(DecryptionReducer.class);
	    
	    job.setOutputKeyClass(Text.class);
	    job.setOutputValueClass(Text.class);
	    
	    return job.waitForCompletion(true) ? 0 : 1;
	  }
	  
	  /**
	   * Main entry point
	   * @param args
	   * @throws Exception
	   */
	  public static void main(String[] args) throws Exception {
	    int exitCode = ToolRunner.run(new DecryptionDriver(), args);
	    System.exit(exitCode);
	  }
	}
