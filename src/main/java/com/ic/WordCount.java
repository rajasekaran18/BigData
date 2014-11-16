package com.ic;

import java.io.IOException;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.lib.input.*;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class WordCount extends Configured implements Tool
{
	
    public static class Map extends Mapper<LongWritable, Text, Text, IntWritable>
    {
        private final static IntWritable one = new IntWritable(1);        
        private Text word = new Text();
        
        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException
        {
        	String line = value.toString();
        	StringTokenizer tokenizer  = new StringTokenizer(line);
        	
        	while(tokenizer.hasMoreElements())
        	{
        		word.set(tokenizer.nextToken());
        		context.write(word, one);
        	}        	
        } 
        
    }
    
    public static class Reduce extends Reducer<Text, IntWritable, Text, IntWritable>
    {
    	@Override
    	public void reduce(Text key, Iterable<IntWritable> val, Context context) throws IOException, InterruptedException
    	{
    		int sum = 0;
    		Iterator<IntWritable> values = val.iterator();
    		while(values.hasNext())
    		{
    			sum += values.next().get();
    		}
    		context.write(key, new IntWritable(sum));
    	}    	
    }
    
	
    
	@Override
	public int run(String[] args) throws Exception 
	{
		// TODO Auto-generated method stub
		
		Configuration conf =new Configuration();
		Job job = new Job(conf, "Word Count");
		job.setJarByClass(WordCount.class);
		
		job.setInputFormatClass(TextInputFormat.class); 
		TextInputFormat.addInputPath(job, new Path(args[0]));
		
		job.setMapperClass(Map.class);
		job.setReducerClass(Reduce.class);
		
		job.setOutputFormatClass(TextOutputFormat.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		
		TextOutputFormat.setOutputPath(job, new Path(args[1]));
		
		boolean res = job.waitForCompletion(true);
		
		if(res)
			return 0;
		else
			return -1; 	
	}
	
	public static void main(String[] args) throws Exception 
	{
		
		int res = ToolRunner.run(new WordCount(), args);
		System.exit(res);		
	}
	
}
