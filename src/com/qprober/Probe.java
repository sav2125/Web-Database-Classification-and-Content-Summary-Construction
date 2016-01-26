package com.qprober;

import com.Helpers.Utilities;
import com.Models.Node;
import com.TrainedQueries.QueryProcessor;

/**
 * Created by shivani on 10/29/2015.
 */
public class Probe {
    public static void main(String args[]) {

        //Declare variables
        float t_es = 0;
        float t_ec = 0;
        String site;
        String BingApiKey;


        //Validation on format of arguments passed
        if (args.length < 3) {
            System.out.println("Not enough parameters specified. Please specify the parameters in the following order:");
            System.out.println("BingApiKey t_es t_ec host");
            return;
        }

        BingApiKey = args[0];

        try {
            t_es = Float.parseFloat(args[1]);
            t_ec = Float.parseFloat(args[2]);
        } catch (NumberFormatException e) {
            System.out.println("For t_es and t_ec value please specify a decimal number.");
            return;
        }

        site = args[3];

        System.out.println("Classifying:");

        //initialize herarchial tree
        Node root=Utilities.initializeCategorizationTree();

        //recursively read file from each node and fire queries to find specificty, coverage and content summaries
        QueryProcessor fireQuery = new QueryProcessor();
        fireQuery.parseTree(root,BingApiKey,site,t_es,t_ec);

        //print results
        System.out.println("Result:");
        printHierarchialResult(root);
        System.out.println("Category:");
        printCategory(root,t_ec,t_es);


    }

    private static void printHierarchialResult(Node root) {

        if(root!=null) {
            if (!root.key.equals("Root")) {
                System.out.println("Coverage of the category " + root.key + " :" + root.coverage);
                System.out.println("Specificity of the category " + root.key + " :" + root.specificity);
            }
            for (int i = 0; i < root.children.size(); i++) {
                    printHierarchialResult(root.children.get(i));
                }
        }

    }

    public static void printCategory(Node root,float t_ec,float t_es)
    {
        if(root!=null) {
            if (root.key.equals("Root"))
                System.out.print(root.key + "/");
            else if(root.coverage>t_ec && root.specificity>t_es)
                System.out.print(root.key + "/");
            for (int i = 0; i < root.children.size(); i++)
                printCategory(root.children.get(i), t_ec, t_es);
        }


    }
}
