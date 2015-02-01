import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * Implementation of Julia set using Hadoop MapReduce
 * ============================================================================
 * 
 */
public class Julia {

	// some parameters
	private static Complex COMPLEX;

	// some constants
	private static int SIZE;
	private static final int ITERS = 256;

	private static final double xmin = -2.0;
	private static final double ymin = -2.0;
	private static final double width = 4.0;
	private static final double height = 4.0;

	// return number of iterations to check z is in the Julia set of COMPLEX
	public static int julia(Complex c, Complex z, int iters) {
		for (int t = 0; t < iters; t++) {
			if (z.abs() > 2.0)
				return t;
			z = z.times(z).plus(c);
		}
		return iters - 1;
	}

	/**
	 * The mapper.
	 * 
	 */
	public static class JuliaMapper extends
			Mapper<Object, Text, Text, IntWritable> {

		public void map(Object key, Text value, Context context)
				throws IOException, InterruptedException {
			String[] parts = value.toString().split(" ");
			Long i = Long.parseLong(parts[0]);
			Long j = Long.parseLong(parts[1]);

			double x = xmin + i * width / SIZE;
			double y = ymin + j * height / SIZE;
			Complex z = new Complex(x, y);
			int t = julia(COMPLEX, z, ITERS);

			context.write(new Text(i + " " + j), new IntWritable(t));
		}
	}

	/**
	 * The reducer.
	 * 
	 */
	public static class JuliaReducer extends
			Reducer<Text, IntWritable, Text, IntWritable> {

		public void reduce(Text key, Iterable<IntWritable> values,
				Context context) throws IOException, InterruptedException {
			context.write(key, new IntWritable((255 - values.iterator().next()
					.get()) % 256));
		}
	}

	/**
	 * The main function
	 */
	public static void main(String[] args) throws Exception {

		if (args.length != 3) {
			System.out
					.println("usage : hadoop jar target/julia-0.0.1-SNAPSHOT.jar Julia <a> <b> <size>");
			System.out.println("complex : a+ib");
			System.exit(0);
		}
		double real = Double.parseDouble(args[0]);
		double imag = Double.parseDouble(args[1]);
		COMPLEX = new Complex(real, imag);
		SIZE = Integer.parseInt(args[2]);

		// Create configuration
		Configuration conf = new Configuration();

		// Create job
		Job job = Job.getInstance(conf);

		// HDFS
		FileSystem fs = FileSystem.get(conf);

		job.setJarByClass(Julia.class);

		// Setup MapReduce
		job.setMapperClass(JuliaMapper.class);
		job.setReducerClass(JuliaReducer.class);

		// Specify key / value for the mapper
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(IntWritable.class);
		// Specify key / value for the reducer
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);

		// Input
		FileInputFormat.addInputPath(job, new Path("input"));

		// Output
		Path output = new Path("output");
		if (fs.exists(output)) {
			fs.delete(output, true);
		}
		FileOutputFormat.setOutputPath(job, output);

		// Execute job and wait for its completion
		job.waitForCompletion(true);

	}
}