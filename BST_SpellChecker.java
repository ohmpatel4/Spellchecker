package Assignment_4;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class BST_SpellChecker {

	public static void main(String args[]) {
		
		BST dictionary = new BST();
		BST misSpell = new BST();
		int lineNum = 0; //keep track of the line number
		Queue wrongWords; //queue of the misspelt words
		String w;
		String s;

        try {
            
            Scanner wordFile = new Scanner(new File(args[0])); //take words.txt as argument 0
            Scanner testFile = new Scanner(new File(args[1])); //take file to check as argument 1

        
            while(wordFile.hasNextLine()) {
                w = wordFile.nextLine();
                w=w.trim();
                w=w.toLowerCase();
                if (w.length()>0) {dictionary.treeInsert(w);} //check to see if any lines are blank
            } 
            
            
            while(testFile.hasNextLine()) {
                s = testFile.nextLine();
                lineNum++; //increment line number
                s=s.trim();
                s=s.toLowerCase();
                
                //use replace to replace punctuation on other special characters
                s=s.replace(".", "");
                s=s.replace(",", "");
                s=s.replace("!", "");
                s=s.replace("(", "");
                s=s.replace(")", "");       
                s=s.replace(";", "");
                s=s.replace("?", "");
                s=s.replace("\"", "");
                s=s.replace("%", "");
                s=s.replace("_", "");
                s=s.replace(":", "");
                
                if (s.length()>0) { //check for blank lines
                	String [] testWords = s.split(" "); //split line into words
                	for (int i=0;i<testWords.length;i++) {
                		if ((dictionary.contains(testWords[i])==false)&&(testWords[i].length()>0)) { //check if word is already in dictionary or is blank
                			if (misSpell.contains(testWords[i])==false) { //check if word is already in misspell table
                				wrongWords = new Queue(); //create a queue of the misspelt words and enqueue the line numbers where they are
                				wrongWords.enqueue(lineNum);
                				misSpell.treeInsert(testWords[i]); //add the misspelt word to the table
                			}
                		}
                	}
                }
                
                
            }
            misSpell.print();
                
        } catch(FileNotFoundException e) { //catch invalid file error
            System.out.println("Invalid file.");
        }
		
	}
	
}

class BST {
	
	public static Node root;
	
	class Node {
        String item;    
        Node left;   
        Node right;   
        Node(String str) {
            item = str;
        }
    } //Node
	
	void treeInsert(String word) {
        if ( root == null ) {
            // The tree is empty.  Set root to point to a new node containing
            // the new item.  This becomes the only node in the tree.
        root = new Node( word );
        return;
    }
        Node runner;  // Runs down the tree to find a place for newItem.
    runner = root;   // Start at the root.
    while (true) {
        if ( word.compareTo(runner.item) < 0 ) {
                // Since the new item is less than the item in runner,
                // it belongs in the left subtree of runner.  If there
                // is an open space at runner.left, add a new node there.
                // Otherwise, advance runner down one level to the left.
            if ( runner.left == null ) {
                runner.left = new Node( word );
                return;  // New item has been added to the tree.
            }
            else
                runner = runner.left;
        }
        else {
                // Since the new item is greater than or equal to the item in
                // runner it belongs in the right subtree of runner.  If there
                // is an open space at runner.right, add a new node there.
                // Otherwise, advance runner down one level to the right.
            if ( runner.right == null ) {
                runner.right = new Node( word );
                return;  // New item has been added to the tree.
            }
            else
                runner = runner.right;
        }
    } // end while
	}  // end treeInsert()
	
	
    boolean treeContains( Node root, String item ) {
        if ( root == null ) {
                // Tree is empty, so it certainly doesn't contain item.
            return false;
        }
        else if ( item.equals(root.item) ) {
                // Yes, the item has been found in the root node.
            return true;
        }
        else if ( item.compareTo(root.item) < 0 ) {
                // If the item occurs, it must be in the left subtree.
            return treeContains( root.left, item );
        }
        else {
                // If the item occurs, it must be in the right subtree.
            return treeContains( root.right, item );
        }
    }  // end treeContains()
    
    boolean contains(String i) { return treeContains(root,i); }
	
    void printBST(Node node) {
        if ( node != null ) {
        	printBST(node.left);             // Print items in left subtree.
            System.out.println("" + node.item);  // Print item in the node.
            printBST(node.right);            // Print items in the right subtree.
        }
    
    }
    
    void print() { printBST(root); }
}

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

