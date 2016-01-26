package com.Models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by shivani on 10/29/2015.
 */
public class Node {
    public String key;
    public String filePath;
    public long coverage;
    public float specificity;
    public List<Node> children;
    public HashSet<String> url_rules = new HashSet<String>();


    public Node(String _key,String _filePath, long _coverage,float _specificity)
    {
        key=_key;
        filePath=_filePath;
        coverage=_coverage;
        specificity=_specificity;
        children = new ArrayList<Node>();
        url_rules = new HashSet<String>();
    }
}
