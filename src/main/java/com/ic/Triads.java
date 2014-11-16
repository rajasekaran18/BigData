package com.ic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.lib.input.*;
import org.apache.hadoop.mapreduce.lib.output.*;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class Triads extends Configured implements Tool
{
	public static class DegreeMapper extends Mapper<Object, Text, Text, Text>
	{
		public void map(Object key, Text value, Context context) throws IOException, InterruptedException
		{
			String[] persons = value.toString().split(",");
			System.out.println(persons[0].trim());
			System.out.println(persons[1].trim());
			context.write(new Text(persons[0].trim()), value);
			context.write(new Text(persons[1].trim()), value);
		} 
	}
	
	public static class DegreeReducer extends Reducer<Text, Text, Text, Text>
	{
		
		public void reduce(Text key, Iterable<Text> value, Context context) throws IOException, InterruptedException
		{
			System.out.println("key:"+key.toString()); 
			ArrayList<String> arrayList = new ArrayList<String>();
			
			int count = 0;
			
			Iterator<Text> iterator = value.iterator();
			
			while(iterator.hasNext())
			{
				//Text text = ;
				
				//System.out.println(text);			
				arrayList.add(iterator.next().toString());
			}			
			count = arrayList.size();
					
			for(String content : arrayList)
			{
				System.out.println("content:"+content);
				context.write(new Text(content), new Text(content.toString()+"|d("+key.toString()+")="+count) );
			}
		}		
	}
	
	public static class DegreeIdentityMapper extends Mapper<Object, Text, Text, Text>
	{
		public void map(Object key, Text value, Context context) throws IOException, InterruptedException
		{
			String[] tokens = value.toString().split("\\s");
						
			context.write(new Text(tokens[0]), new Text(tokens[1]));
		}
	}
	
	public static class DegreeIdentityReducer extends Reducer<Text, Text, Text, Text>
	{
		public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException
		{
			String concatenatedData = "";
			
			boolean first = true;
			
			for(Text val: values)
			{
				String[] tokens = val.toString().split("\\|");
				
				if(first)
				{
				   concatenatedData += tokens[0];	
				   first = false;	
				}				
				concatenatedData += "|"+tokens[1];
			} 			
			context.write(key, new Text(concatenatedData) );
		}
	}

	@Override
	public int run(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Configuration conf = new Configuration();
		Job job = new Job(conf, "TriadOne");
		job.setJarByClass(Triads.class);
		
		job.setInputFormatClass(TextInputFormat.class);
		
		job.setMapperClass(DegreeMapper.class);
		job.setReducerClass(DegreeReducer.class);
		
		job.setOutputFormatClass(TextOutputFormat.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		
		Path output1 = new Path(args[1]);
		FileSystem fs1 = FileSystem.get(conf);
		fs1.delete(output1, true);			
					
		TextInputFormat.addInputPath(job, new Path(args[0]));		
		TextOutputFormat.setOutputPath(job, output1);		
		
		boolean res = job.waitForCompletion(true);
		
		
		//Second job
		job = new Job(conf, "TriadTwo");
		job.setJarByClass(Triads.class);
		
		job.setInputFormatClass(TextInputFormat.class);
		
		job.setMapperClass(DegreeIdentityMapper.class);
		job.setReducerClass(DegreeIdentityReducer.class);
		
		job.setOutputFormatClass(TextOutputFormat.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		
 		Path output2 = new Path(args[2]);
 		FileSystem fs2 = FileSystem.get(conf);
		fs2.delete(output2, true);
 		 		
		TextInputFormat.addInputPath(job, new Path(args[1]));		
		TextOutputFormat.setOutputPath(job, output2);	
		
		res = job.waitForCompletion(true);
		
	
	
		
		if(res)
			return 0;
		else
			return -1;
		
	}
	
	public static void main(String[] args) throws Exception {
		
		int res = new ToolRunner().run(new Triads(), args);
		System.exit(res);
	}
}
