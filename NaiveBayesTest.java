/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stringcount;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author Rohit
 */
public class NaiveBayesTest {
   
    public static void main(String[] args) {

      //  Map<String, String> TestData = 
         
		 // args[0]  contain summarized data for each category 
		 
      HashMap<String,String>ClassData=readClassData(args[0]);   //"G:/Web_app/Stringcount/Artdup_o/part-r-00000"
	  
	  //agrs[1]  contain the keyword count per category 
	  
      HashMap<String,String>WordData=readClassData(args[1]);    //"G:/Web_app/Stringcount/Artdup_wc/part-r-00000"  
	  
	  // Input test data
      readFileData(arg[2],ClassData,WordData);                  //"G:/Web_app/Stringcount/TestData/Test_abstract.txt"

        
       for (String key : ClassData.keySet()) {

            System.out.println(key + " = " + ClassData.get(key));
        }
    }
    
    
    public static HashMap<String,String> readClassData(String filepath)
    {
        
       HashMap<String, String> TestData = new HashMap<>();
        try {
             BufferedReader list = new BufferedReader( new FileReader( filepath) );
     
         String line;
         while((line = list.readLine())!= null)
        {
        String[] words = line.split("\t");
        TestData.put(words[0].trim(),words[1].trim());
      
        }
     list.close();
     
     
      
}catch (IOException ex) {
    System.out.println(ex.getMessage().toString());
        } 
        
       
        return TestData;
        
    }

    public static void readFileData(String filename,HashMap<String,String> clsdata,HashMap<String,String> worddata) {
    
        System.out.println("clsData"+clsdata.size());
        
        try {
             BufferedReader list = new BufferedReader( new FileReader( filename ) );
     
        String line =" ";
        long svalue;
        double rsum;
        double max;
        long classnum=0;
        String mkey = null;
         while((line = list.readLine())!= null)
        {        
          String[] words = line.split(",");
          String[] keyword=words[1].toString().trim().replace("|","##").split("##");
          max=0;
          for(String key : clsdata.keySet()) {
           rsum=0;
           for(String w:keyword)
            {
            String[] Cv=clsdata.get(key).toString().split("####");
            String Searchkey=key+"####"+w.trim();
            String Searchvalue=worddata.get(Searchkey);
           
            if(Searchvalue==null)
            {
               svalue=1;   
            }
            else
            {
            svalue=Integer.valueOf(Searchvalue)+1;
            }
			// sum of total number of keyword in that class and total  vocabulary in all the categories   
            Long denominator=Long.valueOf(Cv[1])+Long.valueOf(Cv[2]);
            double result=Double.valueOf((double)svalue/(double)denominator);   // no of occurence of particular keyword in that category / denominator    
            rsum=rsum+result;
            classnum=((Long.valueOf(Cv[0])));             //   category probability ignoreing the denominator as its same.
             
        }
        
        rsum=rsum*((double)(classnum));     // multiplying category prob ignoreing denominator as its same for all categories
        if(rsum>max)
        {
            max=rsum;
            mkey=key;                      // return the category with highest value      
        }    
            
        }
        System.out.println(mkey+" "+max);
        
        }

         
}catch (IOException ex) {
    System.out.println(ex.getMessage().toString());
        } 
        
}
}

