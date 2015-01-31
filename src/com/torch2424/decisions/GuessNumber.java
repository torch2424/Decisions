package com.torch2424.decisions;

import java.util.Random;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class GuessNumber extends Activity 
{
	
	//our views
		TextView title;
		TextView answer;
		Button decideButton;
		EditText minNumber;
		EditText maxNumber;
		
		//Our array of decisions, will never change
		String[] answerArray;
		
		//Roulette
		Handler handler;
		//Time Counter
		long timeCounter;
		//Our random to choose our answer
		Random ran;
		
		//our toast
	    Toast toasty;
	    
	    //min and max ints
	    int min;
	    int max;
		
		

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.guess_number);
		
		//for icon up navigation
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		//Initialize our Views
		title = (TextView) findViewById(R.id.questionTitle);
		answer = (TextView) findViewById(R.id.answer);
		decideButton = (Button) findViewById(R.id.decideButton);
		minNumber = (EditText) findViewById(R.id.minnumber);
		maxNumber = (EditText) findViewById(R.id.maxnumber);
				
		//Set up our Handler
		handler = new Handler();
						
		//Random
		ran = new Random();
		
		//set up toast
   		toasty = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT);
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
			public void decide(View view)
			{
				//To make sure user isn't trying to break everything
				if(!isInteger(minNumber.getText().toString()) || !isInteger(maxNumber.getText().toString()))
				{
					//If the min number or max number is not a number
					toasty.setText("Please only enter integers into the fields!");
					toasty.show();
				}
				else if(Integer.valueOf(minNumber.getText().toString()) > Integer.valueOf(maxNumber.getText().toString()))
				{
					//Min is greater than max
					toasty.setText("The minimum number can not be larger than the maximum number!");
					toasty.show();
				}
				else if(Integer.valueOf(maxNumber.getText().toString()) > 9999)
				{
					toasty.setText("For memory purposes, the max number can not be greater than 9999!");
					toasty.show();
				}
				else
				{
					//We need to fill up out answer array
					//Intitialize our min and max, and then the array
					min = Integer.valueOf(minNumber.getText().toString());
					max = Integer.valueOf(maxNumber.getText().toString());
					answerArray = new String[max - min + 1];
					for(int i = min; i <= max; i++)
					{
						answerArray[i - min] = Integer.toString(i);
					}
					
					//Start our roulette thread
					timeCounter = 0;
					handler.postDelayed(roulette, timeCounter);
				}
			}	
			
			//Got from stack to check if string is int
			//http://stackoverflow.com/questions/5439529/determine-if-a-string-is-an-integer-in-java
			public static boolean isInteger(String s) 
			{
			    try 
			    { 
			        Integer.parseInt(s); 
			    } 
			    catch(NumberFormatException e) 
			    { 
			        return false; 
			    }
			    // only got here if we didn't return false
			    return true;
			}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.guess_number, menu);
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
