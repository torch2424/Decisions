package com.torch2424.decisions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;

import android.content.Context;

public class DecisionsFile implements java.io.Serializable 
{

	//Warning if not used
	private static final long serialVersionUID = 1L;
	//Our 2d Array List
	private ArrayList<ArrayList<String>> DecisionsArray;
	//Our text file
	private File DecisionsSave;
		
	//Our Constructor
	public DecisionsFile(Context context)
	{
		//Get our file in which we shall be using throughout our app lifetime
		DecisionsSave = new File(context.getFilesDir().getAbsolutePath() + "/Decisions.ser");
		
		//Set up our 2d Array, Initialized and reset in getDecisions
		getDecisions();

	}
	
	//Fills our Decisions Array with the decisions file and returns it
	//Suppressed warning since there is no fix to casting our deserialized object
	@SuppressWarnings("unchecked")
	public ArrayList<ArrayList<String>> getDecisions()
	{
		//initialize
		DecisionsArray = null;
		DecisionsArray = new ArrayList<ArrayList<String>>();
		
		//First need to check if it exists
		if(DecisionsSave.exists())
		{
			/*
			 * This reads our files and deserilizes our object, which then sets it to our DecisionsArray
			 */
			FileInputStream fileIn;
			try {
				fileIn = new FileInputStream(DecisionsSave);
				 ObjectInputStream in = new ObjectInputStream(fileIn);
		         DecisionsArray = (ArrayList<ArrayList<String>>) in.readObject();
		         in.close();
		         fileIn.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (StreamCorruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
		}
		return DecisionsArray;
	}
	
	//Fills our Decisions Array with the decisions file and returns it
		//Suppressed warning since there is no fix to casting our deserialized object
		@SuppressWarnings("unchecked")
		public ArrayList<ArrayList<String>> getDecisionsFromDirectory(File file)
		{
			
			//First need to check if it exists
			if(file.exists())
			{
				
				//initialize here in case we cant restore
				DecisionsArray = null;
				DecisionsArray = new ArrayList<ArrayList<String>>();
				
				/*
				 * This reads our files and deserilizes our object, which then sets it to our DecisionsArray
				 */
				FileInputStream fileIn;
				try {
					fileIn = new FileInputStream(file);
					 ObjectInputStream in = new ObjectInputStream(fileIn);
			         DecisionsArray = (ArrayList<ArrayList<String>>) in.readObject();
			         in.close();
			         fileIn.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (StreamCorruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				//Make sure you save the decisions back to the internal
				saveDecisions();
				
				//Everything was gravy return our decisions array
				return DecisionsArray; 
				
			}
			else
			{
				//Check for null if the file doesn't exist
				return null;
			}
		}
	
	//Returns if our Array is empty or not
	public boolean isEmpty()
	{
		if(DecisionsArray.size() <= 0 || DecisionsArray.get(0).size() <= 0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	//Creates/saves a decisions
	public void addNewDecision(String question, ArrayList<String> decisions)
	{
		//Add the array list to our object
		DecisionsArray.add(decisions);
		
		//Place the Question in the front of the question we just created
		DecisionsArray.get(DecisionsArray.size() - 1).add(0, question);
		
		//Now save our array
		saveDecisions();
	}
	
	//Creates/saves a decisions
	public void addNewDecisionEntry(int index, String input)
	{
			//Add the String to the index of our object
			DecisionsArray.get(index).add(input);
			
			//Now save our array
			saveDecisions();
	}
	
	//Edits an entry in arraylist
		public void editDecisionEntry(int question, int index, String input)
		{
				//sets the input of the index to the specified value
				DecisionsArray.get(question).set(index, input);
				
				//Now save our array
				saveDecisions();
		}
	
	//Saves The Decisions File
	public void saveDecisions()
	{
		//Output the object into the DecisionsSave file
		FileOutputStream fileOut;
			try {
				fileOut = new FileOutputStream(DecisionsSave);
				ObjectOutputStream out = new ObjectOutputStream(fileOut);
		         out.writeObject(DecisionsArray);
		         out.close();
		         fileOut.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	//Saves to another directory than the default one
	public void saveDecisionsToDirectory(File file)
	{
		//First we need to make the directory of the file
		file.getParentFile().mkdir();
		
		//Output the object into the DecisionsSave file
				FileOutputStream fileOut;
					try {
						fileOut = new FileOutputStream(file);
						ObjectOutputStream out = new ObjectOutputStream(fileOut);
				         out.writeObject(DecisionsArray);
				         out.close();
				         fileOut.close();
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}	
	}
	
	//Removes a question and its decisions from our object at the specified index and then saves
	public void removeDecision(int index)
	{
		//remoce the array list from our object
		DecisionsArray.remove(index);
		
		//Now save our array
		saveDecisions();
	}
	
		//Removes a question and its decisions from our object at the specified index and then saves
		public void removeDecisionEntry(int question, int index)
		{
			//remove the decision from the specified entry
			//Plus 1 be cause 0
			DecisionsArray.get(question).remove(index + 1);
			
			//Now save our array
			saveDecisions();
		}
		
}
