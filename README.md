# Web-Database-Classification-and-Content-Summary-Construction

Team members

Shivani Gupta (uni : sg3296)
Siddharth Aman Varshney (uni : sav2125)

Clear description of the internal design of our project :-

Our JAVA code consists of following :-
It consists of 4 packages which are :-
->Helpers
->Models
->TrainedQueries
->qprober
Helpers package consists of the following classes :-
->Constants :- This class consists of two constants. We define bing url that we need to query databases.
 
->GetWordsLynx :- To extract the text of a page, we use this java script provided to us. We declare a treemap 'document' here which stores the token and its document frequency across the database.

->Utilities :- This class consists of method 'getBingResult' which is used for parsing the data fetched from the bing api into our data structures for processing. The second method 'initializeCategorizationTree' initialises our data structure for storing the information fetched from bing api. We create a n-ary tree data structure. Root node consists of information of 'Root' as explained in project. Children of root consists of information about 'Health' , 'Computer', 'Sports' categories. Similarly 'Health' node's children consists of information about 'Fitness' and 'Disease'. Similar implementation occurs for 'Computer' and 'Sports' child nodes.

Models package consists of the following classes :-

->DocFrequency
->Node
->Result
->Root
->SearchResult
->Webs
The above 6 classes specify the data structures which are used for parsing the data fetched from the bing api into our data structures for further processing. 

TrainedQueries package consists of the following class :-
->QueryProcessor :- This class contains the whole logic of  Web Database Classification and Content Summary Construction. First we process each rule of Root.txt line by line. We collect the query and get results from bing api. We then store at most 4 url's returned by Bing. We calculate specificity and coverage[1] for the current node in queue as well. If these values are greater than threshold, we add their children for further processing. If the node's specificity and coverage are greater than threshold values then we process stored url's with the help of GetWordLynx.java . We store document frequencies of every token. Finally we print token and its document frequency.

qprober package consists of the following class :-
->Probe.java :- This class consists of main method. First the tree structure is initialised here. We then call QueryProcessor method. Finally we print the classification.

References :
[1] Alpa Jain, Panagiotis Ipeirotis and Luis Gravano, “Building Query Optimizers for Information Extraction:
The SQoUT Project
”, Newsletter, ACM SIGMOD Record archive,Volume 37 Issue 4, December 2008,Pages 28-34
