import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.Counter;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.util.StringUtils;

public class invertedIndex {

  public static class TokenizerMapper
       extends Mapper<Object, Text, Text, Text>{

    static enum CountersEnum { INPUT_WORDS }
    private Text word = new Text();
    private Text count = new Text();
    private Set<String> patternsToSkip = new HashSet<String>();
    private Configuration conf;
    private BufferedReader fis;

    public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
      String line = value.toString().toLowerCase();
    
      StringTokenizer itr = new StringTokenizer(line);
      String file = ((FileSplit) context.getInputSplit()).getPath().getName();
      //iterate over line
      while (itr.hasMoreTokens()) {
    	  //get rid of anything not A-Z
        word.set(itr.nextToken().replaceAll("[^a-zA-Z]", "").toLowerCase());
        if(!word.toString().equals("")) {
        	context.write(word, new Text(file));
        }
      }
    }
  }

  public static class IntSumReducer
       extends Reducer<Text,Text,Text,Text> {
    private Text result = new Text();

    public void reduce(Text key, Iterable<Text> values,
                       Context context
                       ) throws IOException, InterruptedException {
      int sum=0;
      Iterator<Text> itr = values.iterator();
      HashMap<String, Integer> map=new HashMap<String, Integer>();
      while(itr.hasNext()) {
    	  String value= itr.next().toString();
    	  if(map.containsKey(value)) {
    		  //increment count for that doc
    		  map.put(value, map.get(value)+1);
    		  sum++;
    	  }
    	  else {
    		  //add doc to map
    		  map.put(value, 1);
    		  sum++;
    	  }
      }
      String total = ""+sum + " ";
      for (Map.Entry x : map.entrySet()) {
    	  String keys=(String)x.getKey();//filename
    	  int value=(int)x.getValue();//count
    	  
    	  total=total.concat(keys + ":" + value + " ");
    }
    context.write(key,new Text(total));
    }
  }

  public static void main(String[] args) throws Exception {
//	long start = System.currentTimeMillis();
    Configuration conf = new Configuration();
    Job job = Job.getInstance(conf, "invertedIndex");
    job.setJarByClass(invertedIndex.class);
    job.setMapperClass(TokenizerMapper.class);
    //job.setCombinerClass(IntSumReducer.class);
    job.setReducerClass(IntSumReducer.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(Text.class);
    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));
    
    job.waitForCompletion(true);
//    long end = System.currentTimeMillis();
//    System.out.println("Time before execution: " + start);
//    System.out.println("Time after execution: " + end);
//    System.out.println("Total execution time: " + (start-end));
//    System.exit(0);
  }
}