package com.ic;

import java.util.HashMap;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.util.Tool;

public class WordCountInMapperCombining extends Configured implements Tool
{

	public static class Map extends Mapper<LongWritable, Text, Text, IntWritable>
	{
		public HashMap<Text, IntWritable> mapObj;
		
		public void setUp(Context context)  
		{
			mapObj = new HashMap<Text, IntWritable>();
			
		}
		
		public void map(LongWritable key, Text value, Context context)
		{
			String line = value.toString();
			StringTokenizer tokenizer = new StringTokenizer(line);
			
			while(tokenizer.hasMoreElements())
			{
				Text nextelement = new Text(tokenizer.nextToken());
				if(mapObj.containsKey(nextelement))
				{
					int count = mapObj.get(nextelement).get();					
					mapObj.put(nextelement,  new IntWritable(count++));
				}
				else 
				{
					mapObj.put(nextelement, new IntWritable(1));
				}				
			}					
		}
		
		public void cleanUp(Context context)
		{
			
		}
	}
	  
	
	@Override
	public int run(String[] args) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}
   
}
