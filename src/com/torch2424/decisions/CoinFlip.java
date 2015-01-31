package com.torch2424.decisions;

import java.util.Random;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CoinFlip extends Activity 
{
	
	//our views
	TextView title;
	TextView answer;
	Button decideButton;
	
	//Our array of decisions, will never change
	String[] answerArray = {"Heads", "Tails"};
	
	//Roulette
	Handler handler;
	//Time Counter
	long timeCounter;
	//Our random to choose our answer
	Random ran;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.coinflip);
		
		//for icon up navigation
		getActionBar().setDisplayHomeAsUpEnabled(true);
				
		//Initialize our Views
		title = (TextView) findViewById(R.id.questionTitle);
		answer = (TextView) findViewById(R.id.answer);
		decideButton = (Button) findViewById(R.id.decideButton);
		
		//Set up our Handler
		handler = new Handler();
				
		//Random
		ran = new Random();
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
		    	    	 answer.setText(answerArray[ran.nextInt(answerArray.length)]);
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
		public void flip(View view)
		{
			//Start our roulette thread
			timeCounter = 0;
			handler.postDelayed(roulette, timeCounter);
		}	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.coin_flip, menu);
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
		return super.onOptionsItemSelected(item);
	}
}
