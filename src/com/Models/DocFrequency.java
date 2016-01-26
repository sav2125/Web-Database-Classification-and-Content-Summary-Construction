package com.Models;

/**
 * Created by shivanigupta on 11/1/15.
 */

//Details encapsulated for creating hash map of word with their document frequencies
public class DocFrequency {
    public int docFrequency;
    public int lastProcessedDoc;
    public DocFrequency(int freq,int docNo)
    {
        docFrequency=freq;
        lastProcessedDoc=docNo;
    }
}
