package com.torch2424.decisions;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Start extends Activity 
{

	//Our Decisions object - If memory leak, It'll be here
	public static DecisionsFile decisions;
	ArrayList<ArrayList<String>> decisionsArray;
	ListView listView;
	
	//How many default remaining Decisions
	private final int REMAINING_COUNT = 5;
	
	//For remaining Decisions, public static, because we can change it
	//Though, I should probably save the value in preferences
	int remainingInt;
	boolean remainingBool;
	boolean fiveBool;
	boolean tenBool;
	SharedPreferences prefs;
	
	//our toast
    Toast toasty;
    
    //Views
    TextView remaining;
    TextView notFound;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start);
		
		//Create our File manager
		decisions = new DecisionsFile(getApplicationContext());
		
		//Initilize listview
		listView = (ListView) findViewById(R.id.listView);
		
		//set up toast
   		toasty = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT);
   		
   		//View that is often updated
   		remaining = (TextView) findViewById(R.id.remaining);
   		notFound = (TextView) findViewById(R.id.notFound);
		
	}
	
	@Override
	public void onResume() 
	{
	    super.onResume();
	    
	    //Calculate the remaining decisions/Initialize
	    reInitialize();
	}
	
	//Easily reInitializes everything for us
	private void reInitialize()
	{
		//Get our decisions
		decisionsArray = decisions.getDecisions();
		
		//Get preferences
		prefs = this.getSharedPreferences("MyPrefs", 0);
   		
   		//Set up our remaining Decisions and remaining bool
   		remainingBool = prefs.getBoolean("REMAININGBOOL", false);
   		fiveBool = prefs.getBoolean("FIVEBOOL", false);
   		tenBool = prefs.getBoolean("TENBOOL", false);
   		
   		if(remainingBool)
   		{
   			remaining.setVisibility(View.GONE);
   		}
   		else
   		{
   			remainingInt = REMAINING_COUNT;
   			//Need to add if we have purchases
   			if(fiveBool) 
   			{
   				remainingInt = remainingInt + 5;
   			}
   			
   			if(tenBool)
   			{
   				remainingInt = remainingInt + 10;
   			}
   			
   			remainingInt = remainingInt - decisionsArray.size();
   			
   			//Need to make sure it isn't less than zero
   			if(remainingInt < 0)
   			{
   				remainingInt = 0;
   			}
		
   			//Update view
   			remaining.setVisibility(View.VISIBLE);
			remaining.setText("Remaining Decisions: " + Integer.toString(remainingInt));
   		}
	    
			//Need to check if we have decisions
	  		if(decisions.isEmpty())
	  		{
	  			//Hide our listview
	  			listView.setVisibility(View.GONE);
	  			//Make sure not found is visible
	  			notFound.setVisibility(View.VISIBLE);
	  			
	  			//Do not alter remainining Int's size
	  		}
	  		else
	  		{
	  			//Hide our Textview
	  			notFound.setVisibility(View.GONE);
	  			
	  			//Set up our listview
	  			listView.setVisibility(View.VISIBLE);
	  			createList();
	  		}
	}
	
	
	private void createList()
	{
		//First we must put all of the names of our questions into an Array
		//decisionsArray = decisions.getDecisions(); - Called in Reinitialize
		String[] questions = new String[decisionsArray.size()];
		
		for(int i = 0; i < decisionsArray.size(); ++i)
		{
			//Put the questions of the given index into the Array
			questions[i] = decisionsArray.get(i).get(0);
		}
		
		//setting up adapter and putting in the list view
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, questions);		
		listView.setAdapter(adapter);
		
		//Our on ItemClick Listener
		OnItemClickListener listclick = new OnItemClickListener() 
		{
		    public void onItemClick(AdapterView<?> parent, View v, int position, long id) 
		    {
		    	//Open the Decisions thingy, and give the index to our next activity
		    	Intent intent = new Intent(getApplicationContext(), Decision.class);
		    	intent.putExtra("INDEX", position);
		    	startActivity(intent);		    	
		    }
		};

		listView.setOnItemClickListener(listclick); 
		
		//NOW OUR LONG CLICK LISTENER
		
				//have to create final alert dialog builder out here for long click
				final AlertDialog.Builder builder = new AlertDialog.Builder(this);
				//create another for delete note
				final AlertDialog.Builder delete = new AlertDialog.Builder(this);
				//listener for long click
				OnItemLongClickListener longClick = new OnItemLongClickListener() {
					  public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) 
					  {
						  	//setting up alert dialog
				            builder.setMessage(R.string.longclick)
				                   .setNegativeButton(R.string.longeditdecisions, new DialogInterface.OnClickListener() 
				                   {
				                	   //Edit Decision
				                       public void onClick(DialogInterface dialog, int id) 
				                       {
				                    	
				                    	   ///Open the Decisions thingy, and give the index to our next activity
				           		    		Intent intent = new Intent(getApplicationContext(), EditDecision.class);
				           		    		intent.putExtra("INDEX", position);
				           		    		startActivity(intent);	
				                       }
				                   })
				                   .setPositiveButton(R.string.longdeletedecisions, new DialogInterface.OnClickListener() 
				                   {
				                       public void onClick(DialogInterface dialog, int id) 
				                       {
				                           delete.setMessage(R.string.deleteconfirm)
				                           .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
				                               public void onClick(DialogInterface dialog, int id) 
				                               {
				                            	   //Yes They Would like to Delete
				                            	   //Remove the array at the position they clicked
				                            	   decisions.removeDecision(position);
				                            	   
				                            	   //Recreate the listview and re-get the array and stuff
				                            	   reInitialize();
				                            	   
				                            	   //If above doesnt work, restart the activity, by starting it, and 
				                            	   //finishing this one
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
				                   });
				            // Create the Alert and return true for the case
				            builder.create();
				            builder.show();
				        	return true;
					  }
					};
					
					listView.setOnItemLongClickListener(longClick);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.start, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.backuprestore) 
		{
			//Open the backup/restore Activity
			Intent intent = new Intent(this, BackupSettings.class);
			startActivity(intent);
			
		}
		else if(id == R.id.add_decision)
		{
			//Open the new Question Activity
			if(remainingInt != 0 || remainingBool)
			{
			Intent intent = new Intent(this, NewQuestion.class);
			startActivity(intent);
			}
			else
			{
				//No more remaining Decisions
				toasty.setText("Unfortunately you have run out of decisions. Please support the developer" +
						", and buy more in the settings menu!");
				toasty.setDuration(Toast.LENGTH_LONG);
				toasty.show();
				toasty.setDuration(Toast.LENGTH_SHORT);
			}
		}
		else if(id == R.id.getmoredecisions)
		{
			//Open the IAP Activity
			Intent intent = new Intent(this, IAP.class);
			startActivity(intent);
		}
		else if(id == R.id.coinflip)
		{
			//Open the Coin Flip Activity
			Intent intent = new Intent(this, CoinFlip.class);
			startActivity(intent);
		}
		else if(id == R.id.guessnumber)
		{
			//Open the Guess A Number Between Activity
			Intent intent = new Intent(this, GuessNumber.class);
			startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}
}
