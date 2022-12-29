package me.hvkcoder.hadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import java.io.IOException;
import java.util.StringTokenizer;


/**
 * MapReduce 实现 WordCount
 *
 * @author h_vk
 * @since 2022/12/29
 */
public class MapReduce {
	public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
		// 创建配置对象
		Configuration conf = new Configuration();
		// 创建 Job 对象
		Job job = Job.getInstance(conf);
		// 指定 Job 主类
		job.setJarByClass(MapReduce.class);
		// 指定 Job 名称
		job.setJobName("WordCount");

		// 指定 Job 输入路径和输出路径
		TextInputFormat.addInputPath(job, new Path("/tmp/data.txt"));
		TextOutputFormat.setOutputPath(job, new Path("/tmp/output_mapreduce"));

		// 指定 Job Mapper 类和输出 Key，Value 类
		job.setMapperClass(WordCountMapper.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(IntWritable.class);

		// 指定 Job Reducer 类
		job.setReducerClass(WordCountReducer.class);

		// 提交任务等待完成
		job.waitForCompletion(true);
	}

	/**
	 * WordCount Mapper 操作
	 * Mapper<KEYIN, VALUEIN, KEYOUT, VALUEOUT>
	 */
	private static class WordCountMapper extends Mapper<Object, Text, Text, IntWritable> {

		private static final IntWritable one = new IntWritable(0);
		private final Text word = new Text();

		@Override
		protected void map(Object key, Text value, Mapper<Object, Text, Text, IntWritable>.Context context) throws IOException, InterruptedException {
			StringTokenizer stringTokenizer = new StringTokenizer(value.toString());
			while (stringTokenizer.hasMoreTokens()) {
				word.set(stringTokenizer.nextToken());
				context.write(word, one);
			}
		}
	}

	/**
	 * WordCount Reducer 操作
	 * Reducer<KEYIN,VALUEIN,KEYOUT,VALUEOUT>
	 */
	private static class WordCountReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
		
		@Override
		protected void reduce(Text key, Iterable<IntWritable> values, Reducer<Text, IntWritable, Text, IntWritable>.Context context) throws IOException, InterruptedException {
			int sum = 0;
			for (IntWritable value : values) {
				sum += value.get();
			}
			context.write(key, new IntWritable(sum));
		}
	}
}
