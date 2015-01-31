package com.torch2424.decisions;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class EditDecision extends Activity 
{

	//our views
	EditText question;
	ListView listView;
	EditText editDecisions;
	
	//Our array 
	ArrayList<ArrayList<String>> decisionsArray;
	//Our Array Index
	int arrayIndex;
	//Our array of decisions
	String[] answerArray;
	
	//our toast
    Toast toasty;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.editdecision);
		
		//for icon up navigation
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		//Initialize our Views
		question = (EditText) findViewById(R.id.questionedit);
		editDecisions = (EditText) findViewById(R.id.addtext);
		//Initilize listview
		listView = (ListView) findViewById(R.id.listView);
		
		//get our Array Index
		arrayIndex = getIntent().getIntExtra("INDEX", 0);
		
		//Initialize our Array And set up the question
		decisionsArray = Start.decisions.getDecisions();
		question.setText(decisionsArray.get(arrayIndex).get(0));
		
		//set up toast
   		toasty = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT);
		
		//Now set up our listview
		createList();
		
		
	}
	
	//The Add Button
		public void addDecision(View view)
		{
			if(editDecisions.getText().toString().isEmpty())
			{
				//Toast User
				toasty.setText("You need to enter a decision first!");
				toasty.show();
			}
			else
			{
				//Get the added decisions and add it to our array list
				String tempString = editDecisions.getText().toString();
				Start.decisions.addNewDecisionEntry(arrayIndex, tempString);
				
				//Clear the edittext
				editDecisions.setText("");
				
				//Re-get our list
				createList();
				
			}
		}
	
	private void createList()
	{
		//MODIFIED TO EDIT QUESTION NOT ARRAYLIST
		
		
		//First we must put all of the names of our questions into an Array
		decisionsArray = Start.decisions.getDecisions();
		//Minus one for question
		final String[] questions = new String[decisionsArray.get(arrayIndex).size() - 1];
		
		//Start i at 1 to skip question
		for(int i = 1; i < decisionsArray.get(arrayIndex).size(); ++i)
		{
			//Put the questions of the given index into the Array
			questions[i - 1] = decisionsArray.get(arrayIndex).get(i);
		}
		
		//Sort the Decisions
		Arrays.sort(questions);
		
		//setting up adapter and putting in the list view
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, questions);		
		listView.setAdapter(adapter);
		
		//Dialog for removing decision
		final AlertDialog.Builder delete = new AlertDialog.Builder(this);
		
		//Our on ItemClick Listener
		OnItemClickListener listclick = new OnItemClickListener() 
		{
		    public void onItemClick(AdapterView<?> parent, View v, final int position, long id) 
		    {
		    	delete.setMessage(R.string.deleteconfirm)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) 
                    {
                 	   //Yes They Would like to Delete
                    	
                    	//Do not allow the removal of the very last decision
                    	if(questions.length <= 1)
                    	{
                    		//Toast
                			toasty.setText("Can not remove last decision, you must delete the question entirely!");
                			toasty.show();
                    	}
                    	else
                    	{
                    		//remove the entry
                    		Start.decisions.removeDecisionEntry(arrayIndex, position);
                 	   
                    		//Recreate the listview and re-get the array and stuff
                    		createList();
                	   
                    		//Toast User
                    		toasty.setText("Decision removed!");
                    		toasty.show();
                    	}
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
         // Create the Alert and return true for the case
         delete.create();
         delete.show();		    	
		    }
		};

		listView.setOnItemClickListener(listclick); 
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_decision, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		//The Up Button
		if(id == android.R.id.home)
		{
			finish();
		}
		//The save button
				else if(id == R.id.finish)
				{
					//If there is no question
					if(question.getText().toString().isEmpty())
					{
						//Toast User
						toasty.setText("You need to enter a question!");
						toasty.show();
					}
					//We're all good to save
					else
					{
						//Only need to save question since it may be different
						Start.decisions.editDecisionEntry(arrayIndex, 0, question.getText().toString());
						
						//Toast User
						toasty.setText("Decision saved!");
						toasty.show();
						
						//And then Finish
						finish();
					}
				}
		return super.onOptionsItemSelected(item);
	}
}
