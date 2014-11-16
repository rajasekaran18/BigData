package com.ic.fs;

import java.io.IOException;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.VLongWritable;


public class SequenceFileWriter {
	
	 private static final String[] DATA = {
		    "One, two, buckle my shoe",
		    "Three, four, shut the door",
		    "Five, six, pick up sticks",
		    "Seven, eight, lay them straight",
		    "Nine, ten, a big fat hen"
		  };

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
       String uri = args[0];
      
       Configuration conf = new Configuration();
       
       Path path = new Path(uri);
       
       FileSystem fs = FileSystem.get(URI.create(uri), conf);
	
       IntWritable key = new IntWritable();
       Text value = new Text();
       SequenceFile.Writer writer = null;
       try{
       writer =  SequenceFile.createWriter(fs, conf, path, key.getClass(), value.getClass());
       
       for(int i =0; i < 100; i++)
       {
    	   key.set(100 - i);
    	   value.set(DATA[i % DATA.length]);
    	   
    	   writer.append(key, value);
       }
       }finally
       {
    	   IOUtils.closeStream(writer);
       }
    
		
	}
	
	

}
