package com.torch2424.decisions;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ShowToast")
public class NewQuestion extends Activity 
{
	//The decisions that the user adds
	ArrayList<String> addedDecisions;
	//decisions list
	TextView decisionList;
	//Decisions editText
	EditText editDecisions;
	//Questions EditText
	EditText editQuestion;
	
	//our toast
    Toast toasty;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newquestion);
		
		//for icon up navigation
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		//Get our views
		decisionList = (TextView) findViewById(R.id.decisionList);
		editDecisions = (EditText) findViewById(R.id.editDecisions);
		editQuestion = (EditText) findViewById(R.id.editQuestion);
		
		//set up toast
   		toasty = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT);
   		
   		//Set up Array List
   		addedDecisions = new ArrayList<String>();
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
			addedDecisions.add(tempString);
			
			//Now add it to our textview
			decisionList.setText(decisionList.getText().toString() + tempString + "\n");
			
			//Clear the edittext
			editDecisions.setText("");
		}
	}
	
	//The Undo Button
		public void undoDecision(View view)
		{
			if(decisionList.getText().toString().isEmpty())
			{
				//Toast User
				toasty.setText("You need to enter a decision!");
				toasty.show();
			}
			else
			{	
				//Remove from textview
				String tempString = decisionList.getText().toString().replace(
				addedDecisions.get(addedDecisions.size() - 1) + "\n", "");
				
				//Remove the added Decision
				addedDecisions.remove(addedDecisions.size() - 1);
				
				//Set the textview
				decisionList.setText(tempString);
			}
		}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_question, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
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
			if(editQuestion.getText().toString().isEmpty())
			{
				//Toast User
				toasty.setText("You need to enter a question!");
				toasty.show();
			}
			//There are no decisions
			else if(decisionList.getText().toString().isEmpty())
			{
				//Toast User
				toasty.setText("You need to enter a decision!");
				toasty.show();
			}
			//We're all good to save
			else
			{
				//Create Decision in our object
				Start.decisions.addNewDecision(editQuestion.getText().toString(), addedDecisions);
				
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
