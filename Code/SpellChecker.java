package Assignment_3;

//SpellChecker.java

//-----------------------------------------
//NAME: Ohm Patel
//STUDENT NUMBER: 7928827
//COURSE: COMP 2140, SECTION: A01
//INSTRUCTOR: Phil Pitura
//ASSIGNMENT: Assignment 3, question 2
//
//REMARKS: To implement a spellchecker using hashtables, and given input files as arguments.
//-----------------------------------------

import java.util.*;
import java.io.*;


public class SpellChecker {
	//------------------------------------------------------
	// SpellChecker
	//
	// PURPOSE: Uses the other classes of Queue and HashTable to implement a spellchecker
	// INPUT PARAMETERS: None.
	// OUTPUT PARAMETERS: prints the misspelled words and lines they are found on
	//------------------------------------------------------
	// Note: The files are taken as arguments and are assumed to be already passed as arguments by the user
	//------------------------------------------------------

	public static void main(String args[]) {
		
		HashTable misSpell; //create table for misspelt words
		HashTable dictionary; //create dictionary
		int lineNum = 0; //keep track of the line number
		Queue wrongWords; //queue of the misspelt words
		String w;
				
        try {
            dictionary = new HashTable(50069); //create dictionary using prime number greater than the number of words in the words.txt file
            misSpell = new HashTable(2017); //make a table for misspelt words assuming that there will be at most 2017 wrong words
            Scanner wordFile = new Scanner(new File(args[0])); //take words.txt as argument 0
            Scanner testFile = new Scanner(new File(args[1])); //take file to check as argument 1
        
            //read through the word.txt file and turn each word to lowercase before adding the dictionary table
            while(wordFile.hasNextLine()) {
                w = wordFile.nextLine();
                w=w.trim();
                w=w.toLowerCase();
                if (w.length()>0) {dictionary.insertWord(w);} //check to see if any lines are blank
            }
            
            //read through test file and for each line, take each word, remove punctuation, make lowercase, add to table
            while(testFile.hasNextLine()) {
                w = testFile.nextLine();
                lineNum++; //increment line number
                w=w.trim();
                w=w.toLowerCase();
                
                //use replace to replace punctuation on other special characters
                w=w.replace(".", "");
                w=w.replace(",", "");
                w=w.replace("!", "");
                w=w.replace("(", "");
                w=w.replace(")", "");       
                w=w.replace(";", "");
                w=w.replace("?", "");
                w=w.replace("\"", "");
                w=w.replace("%", "");
                w=w.replace("_", "");
                w=w.replace(":", "");

                if (w.length()>0) { //check for blank lines
                	String [] testWords = w.split(" "); //split line into words
                	for (int i=0;i<testWords.length;i++) {
                		if ((dictionary.check(testWords[i])==false)&&(testWords[i].length()>0)) { //check if word is already in dictionary or is blank
                			if (misSpell.check(testWords[i])==false) { //check if word is already in misspell table
                				wrongWords = new Queue(); //create a queue of the misspelt words and enqueue the line numbers where they are
                				wrongWords.enqueue(lineNum);
                				misSpell.insertWord(testWords[i], (Object)wrongWords); //add the misspelt word to the table
                			}
                			else { //if word is already in dictionary
                				wrongWords=(Queue)misSpell.getValue(testWords[i]); //simply get the line numbers and add to table
                				wrongWords.enqueue(lineNum);
                			}
                		}
                	}
                }
            }
            
            System.out.println("There are a total of "+misSpell.getItems()+" invalid words."); //print out the results
            misSpell.print();
            System.out.println("End of proccessing.");
            
        } catch(FileNotFoundException e) { //catch invalid file error
            System.out.println("Invalid file.");
        }
		
	}
	
}//SpellChecker

class HashTable {
	//------------------------------------------------------
	// HashTable
	//
	// PURPOSE: Create hashtables to use in creating dictionary and misspelt wprds
	// INPUT PARAMETERS: None.
	// OUTPUT PARAMETERS: None
	//------------------------------------------------------
	
	Node table[];
	int tableSize;
	int items;
	private static int hashingConstant=13; //prime constant for horner's method
	
	//constructor for the hashtable
	HashTable(int size){
		table = new Node[size];
		tableSize = size; //uses size set in main method
		items = 0;
		for (int i = 0; i<tableSize; i++) {
			table[i] = null;
		}
	}
	
	//get number of items in table
	int getItems() {
		return items;
	}
	
	//implementation of horner's method to avoid collision
	int hornerHash(String word) {
		int j;
		int hash = 0;
		for (j=0; j<word.length()-1; j++) {
			hash = (hash+(int)(word.charAt(j))*hashingConstant)%tableSize;
		}
		return ((hash+(int)(word.charAt(word.length()-1)))%tableSize);
	}
	
	//insert a missplet word into the table after applying horner's method
	void insertWord(String word, Object val) {
		int hash;
		Node newWord;
		hash = hornerHash(word);
		newWord = new Node(word,val,table[hash]);
		table[hash]=newWord;
		items++;
	}
	
		void insertWord(String word) {
		insertWord(word, null);
	}
	
	//return the word value in table
	Object getValue(String word) {
		int hash;
		Node entry;
		Object val = null;
		hash = hornerHash(word);
		entry = table[hash];
		while (entry!=null) {
			if (word.equals(entry.key)) { //check if word is in table
				val = entry.value; //give value to the word
				break;
			}
			entry=entry.nextVal; //go to next value on table
		}
		return val;
	}
	
	//check if the word already exists in table
	boolean check(String word) {
		boolean exists = false; //set to false initially
		int hash;
		Node entry;
		hash=hornerHash(word);
		entry = table[hash];
		while (entry!=null) {
			if (word.equals(entry.key)) {
				exists=true; //if word is in table then set to true and break
				break;
			}
			entry=entry.nextVal; //go to next value on table
		}	
		return exists; //return boolean 
	}
	
	//prints the invalid words found on the lines
	void print() {
		Node entry;
		int i;
		Queue q;
		for (i=0; i<tableSize; i++) {
			entry = table[i];
			while(entry!=null){
				//for each entry in table, if it is not blank, print it out
				if (entry.value!=null) {
					System.out.print("Invalid word \""+entry.key+"\" found on lines "); //get the word from the table
					q = (Queue) entry.value; //get the lines where the word is misspelt
					q.print();
					
				}
			entry = entry.nextVal;
			}
		}
	}
	
	
	//Node class to create nodes of words and values
	class Node {
		
		String key; //key is the word
		Object value; //value is the line number
		Node nextVal;

		//constructor for Node
		Node(String k, Object val, Node next){
			key=k;
			value=val;
			nextVal=next;
		}
		
		public Object getKey() { //get word
			return key;
		}
		
		public Object getValue() { //get lines
			return value;
		}
		
		public Object getNextVal() { //get next line
			return nextVal;
		}

	}//Node
	
}//HashTable



class Queue {
	//------------------------------------------------------
	// Queue
	//
	// PURPOSE: Implement a queue to store the line numbers in the table
	// INPUT PARAMETERS: None.
	// OUTPUT PARAMETERS: None.
	//------------------------------------------------------
	// Note that most of this class's code is based on Assignment 2 linked list implementation
	//------------------------------------------------------
	queue head;
	queue tail;
	
	
	//Constructor for the queue
	Queue(){
		head = null;
		tail = null;
	}
	
	//Constructor for queue
	Queue(int q){
		queue newNode = new queue(q);
		head = newNode;
		tail = newNode;
	}
	
	//check if the list is empty by checking for tail
	boolean isEmpty() {
		return (tail==null);
	}
	
	//enqueue a new node, or in this case a word, to the end of table
	void enqueue(int q) {
		queue newNode = new queue(q); //create new node
		if(isEmpty()) {
			head=tail=newNode; //if empty then add to front
		}
		else {
			tail.nextLine=newNode; //else add next
			tail = newNode;
		}
	}
	
	//return the top value of the queue
	int top() {
		int line =-1;
		if (!isEmpty()) {
			line = head.getLine(); //if not empty then get top value
		}
		else {
			System.out.println("Queue is empty!");
		}
		return line;
	}
	
	//remove first node from queue
	int deque() {
		int line =-1;
		if (!isEmpty()) {
			line = head.getLine(); //if not empty then move pointer to next line and return previous line
			head = head.getNextLine();
			if(head==null) { //set to null to remove node
				tail=null;
			}
		}
		else {
			System.out.println("Queue is empty!");
		}
		return line;
	}
	
	//print the queue in an organized manner in the form
	//lines: 1 5 7 2 12 35 67 75 ...
	void print() {
		queue num;
		num = head;
		while (num!=null) {
			System.out.print(num.getLine()); //get next value of the line
			if (num.getNextLine()!=null) { //so long as the queue is not empty, print spaces to separate the line numbers
				System.out.print(" ");
			}
			else { //if empty, then print nothing and go to next line
				System.out.println("");
			}
			num=num.nextLine;
		}
	}
	
	//queue constructor
	private class queue{
		private int line;
		private queue nextLine;
		
		//queue constructor for line numbers
		queue(int l){
			line = l;
			nextLine=null;
		}
		
		public int getLine() { //get line number
			return line;
		}	

		public queue getNextLine() { //get next line number
			return nextLine;
		}
		
	}//queue
	
}//Queue