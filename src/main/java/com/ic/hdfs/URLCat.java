package com.ic.hdfs;

import java.io.InputStream;
import java.net.URL;

import org.apache.hadoop.fs.FsUrlStreamHandlerFactory;
import org.apache.hadoop.io.IOUtils;

public class URLCat 
{

	static
	{
	   URL.setURLStreamHandlerFactory(new FsUrlStreamHandlerFactory());	
	}
	
	public static void main(String[] args) 
	{
		InputStream in  = null;
		try{
			System.out.println("BEGIN");
			
			in = new URL(args[0]).openStream();
			IOUtils.copyBytes(in, System.out, 4096, false);
			System.out.println(in);
		}catch(Exception ex)
		{
			System.out.println(ex.getMessage());
			IOUtils.closeStream(in);
		}
		
	}

}
