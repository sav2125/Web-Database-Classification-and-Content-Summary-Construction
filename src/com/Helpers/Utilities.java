package com.Helpers;

import com.Models.Node;
import com.Models.Result;
import com.Models.SearchResult;
import com.google.gson.Gson;
import org.apache.commons.codec.binary.Base64;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by shivani on 10/29/2015.
 */
public class Utilities {

    //get results from bing based on query and populate relevant urls and web total in result
    public static Result getBingResult(String BingAPIKey, String site, String query) throws IOException {

        String q = site + "%20";
        query = query.trim();
        query = query.replaceAll(" ", "%20");
        String bingUrl = Constants.BingUrl + q + query + Constants.BingUrlEnd;


        byte[] accountKeyBytes = Base64.encodeBase64((BingAPIKey + ":" + BingAPIKey).getBytes());
        String accountKeyEnc = new String(accountKeyBytes);
        URL url = new URL(bingUrl);
        URLConnection urlConnection = url.openConnection();
        urlConnection.setRequestProperty("Authorization", "Basic " + accountKeyEnc);
        InputStream inputStream = (InputStream) urlConnection.getContent();
        byte[] contentRaw = new byte[urlConnection.getContentLength()];
        inputStream.read(contentRaw);
        String content = new String(contentRaw);
        SearchResult result = new Gson().fromJson(content, SearchResult.class);
        return result.d.results.get(0);
    }


    //Initialize categorization hierarchial tree and add respective file paths relative to project path to each node
    public static Node initializeCategorizationTree() {
        //Root level node
        Node root = new Node("Root", "root.txt", 0, 0);

        //Nodes for Computer category
        Node computer = new Node("Computers", "computers.txt", 0, 0);
        Node hardware = new Node("Hardware", null, 0, 0);
        Node programming = new Node("Programming", null, 0, 0);
        computer.children.add(hardware);
        computer.children.add(programming);

        //Nodes for Health category
        Node health = new Node("Health", "health.txt", 0, 0);
        Node fitness = new Node("Fitness", null, 0, 0);
        Node disease = new Node("Diseases", null, 0, 0);
        health.children.add(fitness);
        health.children.add(disease);

        //Nodes for Sports category
        Node sports = new Node("Sports", "sports.txt", 0, 0);
        Node basketball = new Node("Basketball", null, 0, 0);
        Node soccer = new Node("Soccer", null, 0, 0);
        sports.children.add(basketball);
        sports.children.add(soccer);

        root.children.add(computer);
        root.children.add(health);
        root.children.add(sports);
        return root;
    }



}


