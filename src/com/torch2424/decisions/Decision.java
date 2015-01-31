package com.torch2424.decisions;

import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Decision extends Activity 
{

	//our views
	TextView title;
	TextView answer;
	Button decideButton;
	//Our array 
	ArrayList<ArrayList<String>> decisionsArray;
	//Our Array Index
	int arrayIndex;
	//Our array of decisions
	String[] answerArray;
	//Handler to run our roulette style deciding
	Handler handler;
	//Time Counter
	long timeCounter;
	//Our random to choose our answer
	Random ran;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.decision);
		
		//for icon up navigation
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		//Initialize our Views
		title = (TextView) findViewById(R.id.questionTitle);
		answer = (TextView) findViewById(R.id.answer);
		decideButton = (Button) findViewById(R.id.decideButton);
		
		//Initialize our Array
		decisionsArray = Start.decisions.getDecisions();
		
		//get our Array Index
		arrayIndex = getIntent().getIntExtra("INDEX", 0);
		
		//Set the question Title
		//Need to set first index to index of listview, transfer through intent
		title.setText(decisionsArray.get(arrayIndex).get(0));
		
		//Set up our Handler
		handler = new Handler();
		
		//Random
		ran = new Random();
		
		//Other stuff done in on resume
	}
	
	@Override
	public void onResume() 
	{
	    super.onResume();
	    //Need this to redo answer textview and re-set our arrays and things
	    
	    //Reset our Array - For edit decision
	  	decisionsArray = Start.decisions.getDecisions();
	    
	    //Reset our answer
	    answer.setText(this.getResources().getString(R.string.pressdecide));
	    
	    //Reset our Array, and fill it up -1 because questions
	    answerArray = new String[decisionsArray.get(arrayIndex).size() - 1];
	    
	    //Start at 1 because question
	    for(int ii = 1; ii < answerArray.length; ++ii)
	    {
	    	answerArray[ii - 1] = decisionsArray.get(arrayIndex).get(ii);
	    }
	    
	    //Reset the question title, since people could edit it in edit decisions
	    title.setText(decisionsArray.get(arrayIndex).get(0));
	}
	
	//Runnable for our handler
	Runnable roulette = new Runnable()
	{
	    public void run() 
	    {
	    	//Run on Ui Thread so we can edit the answer textview
	    	 runOnUiThread(new Runnable()
	    	 {
	    	      @Override
	    	      public void run() 
	    	      {
	    	         //UI
	    	    	  //This is setting the text our answer to a randomly selected string from the specified array
	    	    	  //Minus one for the question, plus one since 0 is the question index
	    	    	 answer.setText(decisionsArray.get(arrayIndex).
	    	    			 get(ran.nextInt(decisionsArray.get(arrayIndex).size() - 1) + 1));
	    	      }
 
	    	 });
	    	 
	    	 //Non UI
	    	 
	    	 //Add onehundred seconds to our delay
	    	 timeCounter += 20;
	    	 
	    	 //If it's not over a second delay, call again
	    	 if(timeCounter <= 200)
	    	 {
	    		 handler.postDelayed(roulette, timeCounter);
	    	 }
	    	 else
	    	 {
	    		 //Cancel all calls
	    		 handler.removeCallbacks(roulette);
	    		 answer.setText(answer.getText() + " - Final Decision");
	    	 }
	    }
	};
	
	//The Decide Button function
	public void decide(View view)
	{
		//Start our roulette thread
		timeCounter = 0;
		handler.postDelayed(roulette, timeCounter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.decision, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.editdecisions) 
		{
			///Open the Decisions thingy, and give the index to our next activity
	    		Intent intent = new Intent(getApplicationContext(), EditDecision.class);
	    		intent.putExtra("INDEX", arrayIndex);
	    		startActivity(intent);	
		}
		//The Up Button
		if(id == android.R.id.home)
		{
			finish();
		}
		return super.onOptionsItemSelected(item);
	}
}
