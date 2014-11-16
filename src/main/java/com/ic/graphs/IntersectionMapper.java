package com.ic.graphs;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.mapreduce.lib.input.*;
import org.apache.hadoop.mapreduce.lib.output.*;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.io.*;

public class IntersectionMapper extends Mapper<Text, Text, Text, Text>
{
	Text graphid = new Text();
	
	Text srcDestPair = new Text();
	
	
   public void map(Text key, Text value, Context context) throws IOException, InterruptedException
   {
	    
	   StringTokenizer tokenizer = new StringTokenizer(value.toString());
	   
	   while(tokenizer.hasMoreTokens())
	   {
		   
		   graphid.set(tokenizer.nextToken());
		   
		   srcDestPair.set(tokenizer.nextToken()+":"+tokenizer.nextToken());
		   
	   }
	   
	   context.write(srcDestPair, graphid);
	   
   }
	
}
