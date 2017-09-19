/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stringcount;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author Rohit
 */
public class findduplicate {
 
    
    public static void main(String args[]) throws FileNotFoundException, IOException
    {
         String line;
        String filepath,filepath1;
        filepath ="G:/Web_app/Stringcount/quotes-street-art_main.txt";
        
        filepath1 ="G:/Web_app/Stringcount/quotes-street-art_main_output.txt";
       
        BufferedReader list = new BufferedReader( new FileReader( filepath ) );
         boolean hasDuplicate = false;
         Set<String> lines = new HashSet<String>();
         
while ( (line = list.readLine()) != null && !hasDuplicate )
    {
        lines.add(line);
    
    
    }

    
 
     list.close();
     
     
     
     BufferedWriter out = new BufferedWriter(new FileWriter(filepath1));
Iterator it =lines.iterator(); // why capital "M"?
while(it.hasNext()) {
    out.write(it.next()+"\n");
}
out.close();
     
         }
    
        

}
