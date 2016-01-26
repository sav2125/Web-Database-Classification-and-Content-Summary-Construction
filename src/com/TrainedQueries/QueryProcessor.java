package com.TrainedQueries;

import com.Helpers.GetWordsLynx;
import com.Helpers.Utilities;
import com.Models.DocFrequency;
import com.Models.Node;
import com.Models.Result;

import javax.swing.text.html.HTMLDocument;
import java.io.*;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by shivani on 10/29/2015.
 */
public class QueryProcessor {


   public void parseTree(Node root, String BingAPIKey,String site,float t_es,float t_ec) {
       Node root1=root;

       Queue<Node> queue = new LinkedBlockingQueue<Node>();
       queue.add(root);
       while(!queue.isEmpty())
       {


           Node front = queue.remove();

           GetWordsLynx.document = new  TreeMap();

           if(front.filePath!=null) {
               //File file = new File(front.filePath);
               BufferedReader br = null;
               //total to count all documents returned for subcategories of given category
               long total = 0;

               try {
                   InputStream is = getClass().getResourceAsStream(front.filePath);
                   InputStreamReader isr = new InputStreamReader(is);

                   //br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                   br = new BufferedReader(isr);
                   String line = null;

                   Node child = front.children.get(0);

                   //for each line in the categoryization rules file
                   while ((line = br.readLine()) != null) {

                       String category = line.substring(0, line.indexOf(" "));
                       String query = line.substring(line.indexOf(" ") + 1);
                       if (child.key != category)
                           child = findNodeWithCategory(front, category);
                       Result result = Utilities.getBingResult(BingAPIKey, site, query);

                       //add url_rules to hash map for building content summaries later
                       int count=0;
                       int countRoot=0;
                       int j=0;
                       while(count<4 || countRoot <4)
                       {
                           if (j >= result.Web.size())
                               break;
                               if (!front.url_rules.contains(result.Web.get(j).Url) && count<4 && !front.key.equals("Root")) {

                                   front.url_rules.add(result.Web.get(j).Url);
                                   count++;
                               }
                           if (!root1.url_rules.contains(result.Web.get(j).Url)&& countRoot<4) {

                               root1.url_rules.add(result.Web.get(j).Url);
                               countRoot++;
                           }
                               j++;
                       }

                       child.coverage += result.WebTotal;
                       if(result.WebTotal>t_ec)
                       total += result.WebTotal;


                   }

                    //for each child assign the specificity and coverage, also add the node to queue if above threshold
                   for (int i = 0; i < front.children.size(); i++) {
                       front.children.get(i).specificity = ((float) front.children.get(i).coverage) / total;
                       System.out.println("Specificity for category "+front.children.get(i).key+" is:"+front.children.get(i).specificity);
                       System.out.println("Coverage for category "+front.children.get(i).key+" is:"+front.children.get(i).coverage);
                       if(front.children.get(i).specificity > t_es && front.children.get(i).coverage > t_ec)
                       {
                           queue.add(front.children.get(i));
                       }
                   }

                   //Extract content summaries for current category node
                   if(front.coverage > t_ec && front.specificity > t_es)
                   {
                       System.out.println("Extracting content summary for"+ front.key);
                       GetWordsLynx.currentDocNo=1;
                       Iterator iterator = front.url_rules.iterator();
                       while(iterator.hasNext())
                       {
                           String url= (String)iterator.next();
                           System.out.println("Getting page:" + url);
                           GetWordsLynx.runLynx(url);
                           GetWordsLynx.currentDocNo++;
                       }

                       //write the content summary to file
                       String file_name = front.key + "-" + site + ".txt";
                       File fout = new File(file_name);
                       FileOutputStream fos = new FileOutputStream(fout);
                       BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
                       for(Map.Entry<String,DocFrequency> entry : GetWordsLynx.document.entrySet())
                       {
                           String key = entry.getKey();
                           DocFrequency val = entry.getValue();
                           bw.write(key+"#"+val.docFrequency);
                           bw.newLine();
                       }

                       bw.flush();
                       bw.close();
                   }


               } catch (FileNotFoundException e) {
                   e.printStackTrace();
               } catch (IOException e) {
                   e.printStackTrace();
               }
           }


       }

       //extract and write content summary for root
       GetWordsLynx.currentDocNo=1;
       Iterator iterator = root1.url_rules.iterator();
       while(iterator.hasNext())
       {
           String url= (String)iterator.next();
           System.out.println("Getting page:" + url);
           GetWordsLynx.runLynx(url);
           GetWordsLynx.currentDocNo++;
       }
       //write content summaries output to file for root node
       String file_name = root1.key + "-" + site + ".txt";
       File fout = new File(file_name);
       FileOutputStream fos = null;
       try {
           fos = new FileOutputStream(fout);

       BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
       for(Map.Entry<String,DocFrequency> entry : GetWordsLynx.document.entrySet())
       {
           String key = entry.getKey();
           DocFrequency val = entry.getValue();

               bw.write(key+"#"+val.docFrequency);
               bw.newLine();

       }
       bw.flush();
       bw.close();

   } catch (FileNotFoundException e) {
        e.printStackTrace();
    } catch (IOException e) {
           e.printStackTrace();
       }


   }

    //find node which has same key as ccategory in the rules files.
    Node findNodeWithCategory(Node p,String category)
    {
        for(int i=0;i<p.children.size();i++)
        {
            if(p.children.get(i).key.equals(category))
                return p.children.get(i);
        }
        return null;
    }

}
