package com.Helpers;
import com.Models.DocFrequency;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.TreeMap;


public class GetWordsLynx {

    //document contains list of all words with their document frequency for a given category
    public static TreeMap<String,DocFrequency> document;
    public static int currentDocNo=0;

    public static void runLynx(String url) {
        int buffersize = 40000;
        StringBuffer buffer = new StringBuffer(buffersize);

        try {

            String cmdline[] = {"/usr/bin/lynx", "--dump", url };
            //String cmdline[] = {"C:\\cygwin64\\bin\\lynx.exe", "--dump", url };
            Process p = Runtime.getRuntime().exec(cmdline);
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            char[] cbuf = new char[1];

            while (stdInput.read(cbuf, 0, 1) != -1 || stdError.read(cbuf, 0, 1) != -1) {
                buffer.append(cbuf);
            }
            p.waitFor();
            stdInput.close();
            stdError.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }


        // Remove the References at the end of the dump
        int end = buffer.indexOf("\nReferences\n");

        if (end == -1) {
            end = buffer.length();
        }


        // Remove everything inside [   ] and do not write more than two consecutive spaces
        boolean recording = true;
        boolean wrotespace = false;
        StringBuffer output = new StringBuffer(end);

        for (int i = 0; i < end; i++) {
            if (recording) {
                if (buffer.charAt(i) == '[') {
                    recording = false;
                    if (!wrotespace) {
                        output.append(' ');
                        wrotespace = true;
                    }
                    continue;
                } else {
                    if (Character.isLetter(buffer.charAt(i)) && buffer.charAt(i)<128) {
                        output.append(Character.toLowerCase(buffer.charAt(i)));
                        wrotespace = false;
                    } else {
                        if (!wrotespace) {
                            output.append(' ');
                            wrotespace = true;
                        }
                    }
                }
            } else {
                if (buffer.charAt(i) == ']') {
                    recording = true;
                    continue;
                }
            }
        }

        //get tokens and add to tree map alongwith their frequency.\
        StringTokenizer st = new StringTokenizer(output.toString());
        while (st.hasMoreTokens()) {
            String tok = st.nextToken();
            if(!document.containsKey(tok)) {
                document.put(tok,new DocFrequency(1,currentDocNo));
            }
            else
            {
               DocFrequency doc = document.get(tok);
                //check if the document has been already processed
                if(doc.lastProcessedDoc<currentDocNo) {
                    doc.lastProcessedDoc=currentDocNo;
                    doc.docFrequency++;

                }
            }
        }
    }


}