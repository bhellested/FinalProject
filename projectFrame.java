import javax.swing.*;
import javax.swing.filechooser.FileSystemView;

import com.google.api.services.storage.Storage;
import com.google.cloud.storage.*;
import com.google.common.io.Files;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;

//import com.google.api.Page;
import com.google.api.gax.longrunning.OperationFuture;
import com.google.api.gax.paging.Page;
import com.google.cloud.dataproc.v1.HadoopJob;
import com.google.cloud.dataproc.v1.Job;
import com.google.cloud.dataproc.v1.JobControllerClient;
import com.google.cloud.dataproc.v1.JobControllerSettings;
import com.google.cloud.dataproc.v1.JobMetadata; 
import com.google.cloud.dataproc.v1.JobPlacement;
import com.google.cloud.storage.Blob;
//import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
//import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage.BlobListOption;

import java.nio.ByteBuffer;
import java.nio.file.Paths;

public class projectFrame {

    // declare some things we need
    private static JFrame f = new JFrame("Brian Hellested Final Project");
    private static JButton filesB, loadB, search, topN, back;
    private static JTextField t=new JTextField();
    private static JTextArea fileTxt = new JTextArea("");
    private static ActionListener listener = new Listener();
    final JFileChooser fc = new JFileChooser();
    private static ArrayList<String> fileArray = new ArrayList<String>();
    private static ArrayList<String> localPath = new ArrayList<String>();
    private static String mergedData;
    private static HashMap<String, String> lookup = new HashMap<String, String>();
    //private static  List<Pair> words = new ArrayList<Pair>();
    private static JTable j;
    private static List<Tuple> sorted = new ArrayList<Tuple>();

    private static JLabel label = new JLabel("");

    public static void main(String[] args) {

        // set window object size
        f.setSize(800, 450);
        // GUI.setTitle("Brian Hellested Final Project");
        f.setVisible(true);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        addHome();
        f.setLayout(null);
    }

    public static void addHome() {
        // file selection
        filesB = new JButton("Choose Files!");
        filesB.setBounds(350, 100, 150, 50);
        // add listener to button
        filesB.addActionListener(listener);
        filesB.setActionCommand("chooseFiles");
        // text area
        fileTxt.setBounds(350, 200, 150, 50);

        loadB = new JButton("Load Engine");
        loadB.setBounds(350, 300, 150, 50);
        loadB.addActionListener(listener);
        loadB.setActionCommand("loadEngine");
        // add all of the things to the pane
        f.add(filesB);
        f.add(fileTxt);
        f.add(loadB);
        // pane.add(txtArea1);
    }

    public static void renderSecondPage() {
        f.getContentPane().removeAll();
        f.revalidate();
        f.repaint();
        // add label
        label.setText(
                String.format("<html><body style=\"text-align: justify;  text-justify: inter-word;\">%s</body></html>",
                        "Engine was Loaded & Inverted indices were constructed successfully"));
        label.setBounds(200, 100, 400, 75);

        // add the search and top n buttons
        search = new JButton("Search for Term");
        search.setBounds(350, 200, 150, 50);
        search.addActionListener(listener);
        search.setActionCommand("search");
        topN = new JButton("Top-N");
        topN.setBounds(350, 300, 150, 50);
        topN.addActionListener(listener);
        topN.setActionCommand("topN");

        f.add(search);
        f.add(topN);
        f.add(label);
        f.repaint();
    }

    public static void dispaySearchPage() {
        f.getContentPane().removeAll();
        f.revalidate();
        f.repaint();
        // add label
        label.setText(
                String.format("<html><body style=\"text-align: center;  text-justify: center;\">%s</body></html>",
                        "Enter your Search Term"));
        label.setBounds(350, 100, 150, 75);
        
        t.setBounds(350, 200, 150, 75);

        back = new JButton("Back");
        back.setBounds(0, 0, 150, 30);
        back.addActionListener(listener);
        back.setActionCommand("back");

        search = new JButton("Search");
        search.setBounds(350, 300, 150, 50);
        search.addActionListener(listener);
        search.setActionCommand("doSearch");
        
        f.add(back);
        f.add(label);
        f.add(search);
        f.add(t);
    }
    public static void displaySearchResults(List<String> arr){//display actual search results
    	
    	JPanel panel = new JPanel();
    	panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    	    JPanel p = new JPanel(new BorderLayout());
    	    
    	    ArrayList<String> names=new ArrayList<String>();
        	names.add("Search Term:");
        	names.add("total");
        	for (String s : localPath) {
        		names.add(s);
        	}
        
        String[][] data= new String[1][names.size()];
        //System.out.println("colums: "+names.size());
        data[0][0]=t.getText();
        data[0][1]=arr.get(0);
        int i=2;
        for(String s:localPath) {
        	int index=arr.indexOf(s);
        	if (index!=-1) {
        		data[0][i]=arr.get(index+1);
        	}
        	i++;
        }
        j = new JTable(data, names.toArray());
        
        
            
    	    JScrollPane scroll = new JScrollPane(j);
    	    scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    	    p.add(scroll, BorderLayout.CENTER);
    	    JButton but = new JButton("OK");
    	    p.add(but, BorderLayout.SOUTH);
    	    but.addActionListener(new ActionListener(){
    	        public void actionPerformed(ActionEvent e){
    	            panel.add(new JLabel("Some random text"));
    	            scroll.revalidate();
    	            p.repaint();p.revalidate();
    	        }
    	    });
    	    JFrame frame = new JFrame();
    	    frame.setSize(800,200);
    	    frame.setVisible(true);
    	    frame.add(p);
    	    //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	    frame.setLocationRelativeTo(null);
    	
    	
    }

    public static void dispayTopNPage() {
        f.getContentPane().removeAll();
        f.revalidate();
        f.repaint();
        // add label
        label.setText(
                String.format("<html><body style=\"text-align: center;  text-justify: center;\">%s</body></html>",
                        "Enter your N value"));
        label.setBounds(350, 100, 150, 75);
        
        t.setBounds(350, 200, 150, 75);

        back = new JButton("Back");
        back.setBounds(0, 0, 150, 30);
        back.addActionListener(listener);
        back.setActionCommand("back");
        search = new JButton("Search");
        search.setBounds(350, 300, 150, 50);
        search.addActionListener(listener);
        search.setActionCommand("doTopN");
        f.add(back);
        f.add(label);
        f.add(search);
        f.add(t);
    }

    public static void dispayTopNResults() {
    	//this is creating a popup jTable window to display the results
    	JPanel panel = new JPanel();
    	panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    	    JPanel p = new JPanel(new BorderLayout());
    	    
    	    String[][] data= new String[Integer.parseInt(t.getText())][3];
	          for(int i=0;i<Integer.parseInt(t.getText());i++) {
	          	data[i][0]=sorted.get(i).word;
	          	data[i][1]=sorted.get(i).count.toString();
	          	data[i][2]=""+(i+1);
	          }
    	    String[] headers= {"word","count","rank"};
            j = new JTable(data, headers);
            
    	    JScrollPane scroll = new JScrollPane(j);
    	    scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    	    p.add(scroll, BorderLayout.CENTER);
    	    JButton but = new JButton("OK");
    	    p.add(but, BorderLayout.SOUTH);
    	    but.addActionListener(new ActionListener(){
    	        public void actionPerformed(ActionEvent e){
    	            panel.add(new JLabel("Some random text"));
    	            scroll.revalidate();
    	            p.repaint();p.revalidate();
    	        }
    	    });
    	    JFrame frame = new JFrame();
    	    frame.setSize(400,600);
    	    frame.setVisible(true);
    	    frame.add(p);
    	    //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	    frame.setLocationRelativeTo(null);
    }  

    public static void doHadoopJob(String projectId, String region, String clusterName)
    	      throws IOException, InterruptedException {
    	String myEndpoint = String.format("%s-dataproc.googleapis.com:443", region);

        // Configure the settings for the job controller client.
        JobControllerSettings jobControllerSettings =
            JobControllerSettings.newBuilder().setEndpoint(myEndpoint).build();

        // Create a job controller client with the configured settings. Using a try-with-resources
        // closes the client,
        // but this can also be done manually with the .close() method.
        try (JobControllerClient jobControllerClient =
            JobControllerClient.create(jobControllerSettings)) {

          // Configure cluster placement for the job.
          JobPlacement jobPlacement = JobPlacement.newBuilder().setClusterName(clusterName).build();

          // Configure Hadoop job settings. The HadoopFS query is set here.
          ArrayList<String> args=new ArrayList<String>();
          args.add("invertedIndex");
          args.add("gs://hellested_final_project/inputData");
          args.add("gs://hellested_final_project/output");
          HadoopJob hadoopJob =
              HadoopJob.newBuilder()
                  .setMainClass("gs://hellested_final_project/invertedIndex.jar")
                  .addAllArgs(args)
                  .setMainJarFileUri("gs://hellested_final_project/invertedIndex.jar")
                  .build();

          Job job = Job.newBuilder().setPlacement(jobPlacement).setHadoopJob(hadoopJob).build();

          // Submit an asynchronous request to execute the job.
          OperationFuture<Job, JobMetadata> submitJobAsOperationAsyncRequest =
              jobControllerClient.submitJobAsOperationAsync(projectId, region, job);

          Job response = submitJobAsOperationAsyncRequest.get();

          // Print output from Google Cloud Storage.
          Matcher matches =
              Pattern.compile("gs://(.*?)/(.*)").matcher(response.getDriverOutputResourceUri());
          matches.matches();

          com.google.cloud.storage.Storage storage = StorageOptions.getDefaultInstance().getService();
          Blob blob = storage.get(matches.group(1), String.format("%s.000000000", matches.group(2)));

          System.out.println(
              String.format("Job finished successfully: %s", new String(blob.getContent())));

        } catch (ExecutionException e) {
        	
        	
          // If the job does not complete successfully, print the error message.
          System.err.println(String.format("submitHadoopFSJob: %s ", e.getMessage()));
        }
      }
    
    private static class Listener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("chooseFiles")) {

                JFileChooser fc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
                fc.setMultiSelectionEnabled(true);
                fc.setFileSelectionMode(JFileChooser.FILES_ONLY);

                int r = fc.showOpenDialog(f);
                if (r == JFileChooser.APPROVE_OPTION) {
                    // create an array of files chosen. these get uploaded to bucket.
                    File[] files = fc.getSelectedFiles();
                    for (File f : files) {// multiple files is not working correctly so user must add each individually
                        fileArray.add(f.getAbsolutePath());
                        fileTxt.append(f.getName() + '\n');
                        localPath.add(f.getName());

                    }
                } else {
                    System.out.println("the user has exited the file dialog");
                }


            }
            if (e.getActionCommand().equals("loadEngine")) {
                // here we are uploading each file and then submitting hadoop job on the cluster

                // upload files
                //modified from https://cloud.google.com/storage/docs/samples/storage-upload-file#storage_upload_file-java
                int i=0;
                String projectId = "finalprojectbrianhellested";
                String bucketName = "hellested_final_project";
                com.google.cloud.storage.Storage storage =  com.google.cloud.storage.StorageOptions.newBuilder().setProjectId(projectId).build().getService();
            	for(String s : fileArray) {
                    BlobId blobId = BlobId.of(bucketName, "inputData/" + localPath.get(i));
                    i++;
                   BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
                   //storage.create(blobInfo, Files.readAllBytes(s));
                    try {
						storage.create(blobInfo, java.nio.file.Files.readAllBytes(Paths.get(s)));
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
                }
                    
                // submit job
            	
                    try {
						doHadoopJob("finalprojectbrianhellested","us-central1","cluster-887b");
					} catch (IOException | InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

                // download results
                    
                ByteArrayOutputStream bStream= new ByteArrayOutputStream();
                mergedData="";
                Page<Blob> blobs = storage.list(bucketName, BlobListOption.currentDirectory(),
                	     BlobListOption.prefix("output/"));
            	 Iterator<Blob> blobIterator = blobs.iterateAll().iterator();
            	 while (blobIterator.hasNext()) {
            	   Blob blob = blobIterator.next();
            	   blob.downloadTo(bStream);
            	   mergedData+=bStream.toString();
            	   //System.out.println(blob.toString()); this was just metadata
            	   // do something with the blob
            	 }
            	 BufferedWriter out = null;
                 try {
 					out = new BufferedWriter(new FileWriter("test.txt"));
 					out.write(mergedData);
 				} catch (IOException e1) {
 					// TODO Auto-generated catch block
 					e1.printStackTrace();
 				} finally {
 					try {
 						out.close();
 					} catch (IOException e1) {
 						// TODO Auto-generated catch block
 						e1.printStackTrace();
 					}
 				}
                 //at this point all information is stored in mergedData string.
                 //based on what user wants i should put into a, array to sort for top n or hashTable for search.
                 BufferedReader bufReader = new BufferedReader(new StringReader(mergedData));
                 String line=null;
                 try {
					while( (line=bufReader.readLine()) != null )
					 {
						//System.out.println(line);
						String[] split=line.split("\t",2);
						lookup.put(split[0],split[1]);//HashMap for Search
						
						//sorted.add(new Tuple(Integer.parseInt(split[1].substring(0,split[1].indexOf(" "))),split[0]));
					 }
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
                 for (Map.Entry x : lookup.entrySet()) {
                	 String key=x.getKey().toString();
                	 String value=x.getValue().toString();
                	 int freq=Integer.parseInt(value.substring(0,value.indexOf(" ")));
						sorted.add(new Tuple(freq,key));

               }
                Collections.sort(sorted);
                
                // change to next page
                renderSecondPage();
            }
            if (e.getActionCommand().equals("search")) {//page where user inputs search term
                
                dispaySearchPage();
            }
            if (e.getActionCommand().equals("doSearch")) {//page to view actual search results
                //get search results and render page with them
            	if(!lookup.containsKey(t.getText())) {
            		//System.out.println("sorry " + t.getText()+" was not found" );
            		label.setText(
                            String.format("<html><body style=\"text-align: center;  text-justify: center;\">%s</body></html>",
                                    "That lookup was not found. Please Try again"));
            		return;
            		
            	}else {
            		label.setText(
                            String.format("<html><body style=\"text-align: center;  text-justify: center;\">%s</body></html>",
                                    "Enter Your Search Term"));
            		String s=lookup.get(t.getText());
            		s=s.replaceAll(":", " ");
            		//System.out.println("key: "+t.getText() + " Value: "+ s);
            		String[] split=s.split(" ");
            		List<String> toReturn =
            	            new ArrayList<String>(Arrays.asList(split));
//            		for(String str : toReturn) {
//            			System.out.println(str);
//            		}
            		displaySearchResults(toReturn);
            	}
            }
            if (e.getActionCommand().equals("topN")) {//page to input N value
                dispayTopNPage();
            }
            if (e.getActionCommand().equals("doTopN")) { // page to actually view top n results
                //get top n results and render page with them
            	//validate topN input (must be integer)
            	String temp=t.getText();
            	int result=1;
            	try {
                    result = Integer.parseInt(temp);
                } catch (NumberFormatException nfe) {
                    label.setText(
                            String.format("<html><body style=\"text-align: center;  text-justify: center;\">%s</body></html>",
                                    "That was not a valid number"));
                }
            	if(result<=0) {
            		label.setText(
                            String.format("<html><body style=\"text-align: center;  text-justify: center;\">%s</body></html>",
                                    "Please enter a number greater than 0"));
            		return;
            	}
            	else {
            		dispayTopNResults();
            	}                
            }
            if (e.getActionCommand().equals("back")) { // handles all back buttons
                if(back.getText().equals("Back to Top N")){
                	t.setText("");
                    dispayTopNPage();
                }
                else if(back.getText().equals("Back to Search")){
                	t.setText("");
                    dispaySearchPage();
                }
                else if(back.getText().equals("Back")){
                	t.setText("");
                    renderSecondPage();
                }
            }


        }
    }
    private static class Tuple implements Comparable<Tuple> {
        private Integer count;
        private String word;

        public Tuple(int count, String word) {
            this.count = count;
            this.word = word;
        }

        @Override
        public int compareTo(Tuple o) {
            return (o.count).compareTo(this.count);
        }
        public String toString() {
            return word + " " + count;
        }
    }
}
