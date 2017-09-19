package org.myorg;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;

import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Reducer.Context;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.myorg.NaiveBayesU.Map;
import org.myorg.NaiveBayesU.Reduce;
import org.myorg.PageRank.Map2;
import org.myorg.PageRank.Reduce2;

public class NaiveBayes extends Configured implements Tool {


	public static void main( String[] args) throws  Exception {
	      int pag  = ToolRunner.run( new NaiveBayes(), args);
	      System .exit(pag);
	   }

	@Override
	public int run(String[] arg0) throws Exception {
		
		
		// job to find the unique vocabulary count.
		
		  Job job  = Job .getInstance(getConf(), " Step1 ");
	      job.setJarByClass( this .getClass());
	      FileInputFormat.addInputPaths(job,  arg0[0]);
 	      FileOutputFormat.setOutputPath(job,  new Path(arg0[1]));
	      job.setMapperClass( Map .class);
	      job.setReducerClass( Reduce .class);
	      job.setOutputKeyClass( Text .class);
	      job.setOutputValueClass( IntWritable .class);
          job.waitForCompletion( true);
	      

		  // passing vocabulary count to second job 
		  
          Configuration newConf = getConf(); 
		  Path pathObj = new Path(arg0[1]+"/part-r-00000");   
		 // LineNumberReader  lnr = new LineNumberReader(new FileReader(new File(arg0[1]+"/part-r-00000")));
		  //lnr.skip(Long.MAX_VALUE);
		  //Long value=(long) (lnr.getLineNumber()+1);
		  //System.out.println("Uniques words"+lnr.getLineNumber()); 
		  //Add 1 because line index starts at 0
		  // Finally, the LineNumberReader object should be closed to prevent resource leak
		  //lnr.close();
		  FileSystem fileSys = pathObj.getFileSystem(newConf);
		  Long Uniqueword = (long) IOUtils.readLines(fileSys.open(pathObj)).size();
		  newConf.setLong("numberOfuniqueword",Uniqueword);
		  System.out.println("number of Uniqueword:"+Uniqueword);
	
		  // calculating number of keyword per category/class/ type 
		  
		  Job job1  = Job.getInstance(getConf(), " Step2 ");
	      job1.setJarByClass( this .getClass());
	      FileInputFormat.addInputPaths(job1,  arg0[0]);
   	      FileOutputFormat.setOutputPath(job1,  new Path(arg0[2]));
	      job1.setMapperClass( Map1 .class);
	      job1.setReducerClass( Reduce1 .class);
	      job1.setOutputKeyClass( Text .class);
	      job1.setOutputValueClass( Text .class);
	      job1.waitForCompletion( true);
	      
		
		// calculating number of each keyword occurence in a particular class/ category 
		
	      Job job2  = Job .getInstance(getConf(), " Step3 ");
	      job2.setJarByClass( this .getClass());
	      FileInputFormat.addInputPaths(job2,  arg0[0]);
	      FileOutputFormat.setOutputPath(job2,  new Path(arg0[3]));
	      job2.setMapperClass( Map2 .class);
	      job2.setReducerClass( Reduce2 .class);
	      job2.setOutputKeyClass( Text .class);
	      job2.setOutputValueClass( IntWritable .class);

	      return job2.waitForCompletion( true)  ? 0 : 1;
		
	}


	   public static class Map extends Mapper<LongWritable, Text, Text, IntWritable> {
		   public void map(LongWritable offset, Text lineText, Context context)
		       throws IOException, InterruptedException {
			   final IntWritable one  = new IntWritable( 1);
			   Text word =new Text();   
			   String line =lineText.toString().replace(" ", "");
			   String[] splitarray = line.split(",");
			  // System.out.println(splitarray[1]);
			  if(splitarray.length>0)
			  {
			   String Keywords=splitarray[1].toString().toLowerCase();
			   String Key=Keywords.replace("|","##");
			    for(String temp:Key.split("##"))
			   {
				  
				   context.write(new Text(temp.trim()),one);
			   }
			   
			    }
			  else
			  {
				  return;
			  }
		
		   }
		  }


	   
		  public static class Reduce extends Reducer<Text, IntWritable, Text, IntWritable> {
		   @Override
		   public void reduce(Text key, Iterable<IntWritable> counts, Context context)
		       throws IOException, InterruptedException {

			         context.write(key,  new IntWritable(1));
			      // ignoring the duplicate occurences 
		   }
		   
		  }
	
		   public static class Map1 extends Mapper<LongWritable, Text, Text, Text> {
			   public void map(LongWritable offset, Text lineText, Context context)
			       throws IOException, InterruptedException {
				   final IntWritable one  = new IntWritable( 1);
				   Text word =new Text();   
				   String line =lineText.toString();
				   String[] splitarray = line.split(",");
				   Long Vocabulary =Long.valueOf( context.getConfiguration().get("numberOfuniqueword"));
				   int count=0;
				   
				  // System.out.println(splitarray[1]);
				  if(splitarray.length>0)
				  {
				   String Keywords=splitarray[1].toString().toLowerCase();
				   String Key=Keywords.replace("|","##");
				    for(String temp:Key.split("##"))
				   {
					  count=count+1;
				    	
					 //  context.write(new Text(temp.trim()),one);
				   }
				   
				    }
				  else
				  {
					count=0; 
				  }
				  String MapLine=splitarray[0]+"###1"+"###"+count+"###"+Vocabulary;
				  context.write(new Text(splitarray[0]),new Text(MapLine));
				  
			   }
			  }


		   
			  public static class Reduce1 extends Reducer<Text, Text, Text, Text> {
			   @Override
			   public void reduce(Text key, Iterable<Text> counts, Context context)
			       throws IOException, InterruptedException {
				   
			      Long csum  = (long) 0;
			      Long wsum=(long) 0;
			      Long v=(long) 0;
			         for ( Text count  : counts) {
			        	String [] str= count.toString().split("###");
			        	
			        	csum=csum+ Long.valueOf(str[1]);
			        	wsum=wsum+ Long.valueOf(str[2]);
			        	v=Long.valueOf(str[3]);
			        	 
			        	 //sum  += count.get();                                       // get the total number of occurrence of a word
			         }
			         
			         String values=csum+"####"+wsum+"####"+v;
			         context.write(key,  new Text(values));
			    
			   }
			   
			  }

			  
			   public static class Map2 extends Mapper<LongWritable, Text, Text, IntWritable> {
				   public void map(LongWritable offset, Text lineText, Context context)
				       throws IOException, InterruptedException {
					   final IntWritable one  = new IntWritable( 1);
					   Text word =new Text();   
					   String line =lineText.toString().replace(" ", "");
					   String[] splitarray = line.split(",");
					  // System.out.println(splitarray[1]);
					  if(splitarray.length>0)
					  {
					   String Keywords=splitarray[1].toString().toLowerCase();
					   String Key=Keywords.replace("|","##");
					   String classname=splitarray[0];
					    for(String temp:Key.split("##"))
					   {
						  
						   context.write(new Text(classname+"###"+temp.trim()),one);
					   }
					   
					    }
					  else
					  {
						  return;
					  }
				
				   }
				  }


			   
				  public static class Reduce2 extends Reducer<Text, IntWritable, Text, IntWritable> {
				   @Override
				   public void reduce(Text key, Iterable<IntWritable> counts, Context context)
				       throws IOException, InterruptedException {

					   
					      int sum  = 0;
					         for ( IntWritable count  : counts) {
					            sum  += count.get();                                       // get the total number of occurrence of a word
					         }
					         context.write(key,  new IntWritable(sum));
					    
					   }

				   }	  
			  
			  
}
