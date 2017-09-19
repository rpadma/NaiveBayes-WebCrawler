Implemented Naïve Bayes algorithm using Hadoop Framework to train my model and a Java program to evaluate test results based on trained data. Paintings are classified into 5 class (Abstract Art, Fine Art, Pop Art, Street Art, Modern Art) based on the Keywords associated with Painting.
As part of data collection, I had written a web crawler for the website https://www.saatchiart.com/ using Scrapy python framework to collect 400k Painting data.

Note :

NaiveBayes.java - four parameter are needed to pass as command line arguments

first -  contain the input data 
second - unique keyword are generated
third -  consolidate summary for each category is generated
fourth - word count for each keyword based on category/class 

Example : 
first parameter  - /home/cloudera/Artdup
second parameter -/home/cloudera/Artdup_unique
third parameter  -/home/cloudera/Artdup_output
fourth parameter  -/home/cloudera/Artdup_wordcount


NaiveBayesTest.java -Three parameter are needed to pass as command line arguments

1) parameter - path of file which contain category and summarized data  
2) parameter - path of file contain keyword count for each catefory 
3) parameter - path of file containing test data.


findDuplicate - Java code to find duplicate painting urls.

Scrapy_Python_code - Web Crawler program (using Scrapy framework) to get  almost all the painting data from website.

