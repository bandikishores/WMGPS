package com.example.wmgps;

import java.io.File;
import java.util.List;

import com.example.testclient.Constants;
import com.example.testclient.Misc;
import com.example.testclient.ServerSocketListener;
import com.example.testclient.WrapperUser;
import com.example.wmgps.DrawView;
import com.example.wmgps.R;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class MenuActivity extends ActionBarActivity {

public static String sessionId = Misc.EMPTY_STRING;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maps);
		
		
        //DrawView.user = new Circle();
        // Kish - TODO : create a field's on the fly over here.
		
		/*((EditText) findViewById(R.id.userNameEdit)).setVisibility(View.GONE);
		((EditText) findViewById(R.id.userNameEdit)).setText("Enter UserId");
		((Button) findViewById(R.id.switchToUser)).setVisibility(View.GONE);*/
    	
    	((TextView) findViewById(R.id.ErrorTextSupervisor)).setVisibility(View.GONE);
    	((TextView) findViewById(R.id.ErrorTextSupervisor)).setTextColor(Color.RED);
        
        Intent intent = getIntent();
        
        if(Constants.SUPERVISOR.equals(intent.getStringExtra(Constants.USER_TYPE)))
        {
        	// Kish - TODO : create a field's on the fly over here.
			/*((EditText) findViewById(R.id.userNameEdit)).setVisibility(View.VISIBLE);
			((Button) findViewById(R.id.switchToUser)).setVisibility(View.VISIBLE);*/
        	
        	intent = new Intent(this, SupervisorActivity.class);
        	
        	intent.putExtra(Constants.SESSION_ID, getIntent().getStringExtra(Constants.SESSION_ID));
        	sessionId = Misc.EMPTY_STRING;
        	// goto User screen
		    startActivity(intent);
		    
        	/*((TextView) findViewById(R.id.displayNoOfUsers)).setVisibility(View.VISIBLE);
        	((EditText) findViewById(R.id.users)).setVisibility(View.VISIBLE);
        	((Button) findViewById(R.id.switchToSupervisor)).setVisibility(View.VISIBLE);
        	((EditText) findViewById(R.id.users)).setInputType(InputType.TYPE_CLASS_NUMBER);*/
        }
        else
        {
        	intent = new Intent(this, UserActivity.class);
        	
        	intent.putExtra(Constants.SESSION_ID, getIntent().getStringExtra(Constants.SESSION_ID));
        	sessionId = getIntent().getStringExtra(Constants.SESSION_ID);
			// goto User screen
		    startActivity(intent);
        }

    	
    	((TextView) findViewById(R.id.displayNoOfUsers)).setVisibility(View.GONE);
    	((EditText) findViewById(R.id.users)).setVisibility(View.GONE);
    	((Button) findViewById(R.id.switchToSupervisor)).setVisibility(View.GONE);
		((TextView) findViewById(R.id.ErrorTextSupervisor)).setVisibility(View.VISIBLE);
		((TextView) findViewById(R.id.ErrorTextSupervisor)).setTextColor(Color.GREEN);
		
		/*if(!Misc.isNullTrimmedString(sessionId) && Misc.isInt(sessionId) && Integer.parseInt(sessionId) > 0)
		{
			String message = getMessagesFromSession(sessionId);
			if(!Misc.isNullTrimmedString(message))
				((TextView) findViewById(R.id.ErrorTextSupervisor)).setText(message);
			else
				((TextView) findViewById(R.id.ErrorTextSupervisor)).setText("Logged Out!!");
		}
		else
		{		
			((TextView) findViewById(R.id.ErrorTextSupervisor)).setText("Logged Out!!");
		}*/
        
    	LinearLayout ll = new LinearLayout(this);
	   	 ll.setOrientation(LinearLayout.VERTICAL);
	
	   	 TextView tv = new TextView(this);
	   	 tv.setText("MaxWidth " + DrawView.maxWidth + "  MaxHiehgt" + DrawView.maxHeight);
	   	 ll.addView(tv);
	}

	private String getMessagesFromSession(String sessionId2) 
	{
		ServerSocketListener serverSocketListener = new ServerSocketListener();
		serverSocketListener.addKeys(Constants.VALUES_AVALIABLE, Constants.GET_MESSAGES_FROM_SESSION_ID);
		serverSocketListener.addValues(sessionId2);
		serverSocketListener.setResponseNeeded(true);
		serverSocketListener.invoke();
		List outputList = serverSocketListener.getOutputList();
		
		if(!Misc.isNullList(outputList))
			return (String) outputList.get(0);
		else
			return null;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.maps, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		
		if(!Misc.isNullTrimmedString(sessionId) && Misc.isInt(sessionId) && Integer.parseInt(sessionId) > 0)
		{
			String message = getMessagesFromSession(sessionId);
			if(!Misc.isNullTrimmedString(message))
				((TextView) findViewById(R.id.ErrorTextSupervisor)).setText(message);
			else
				((TextView) findViewById(R.id.ErrorTextSupervisor)).setText("Logged Out!!");
		}
		else
		{		
			((TextView) findViewById(R.id.ErrorTextSupervisor)).setText("Logged Out!!");
		}
	}
	
	@Override
	public void onDestroy()
	{
		super.onDestroy();
        //DrawView.user = new Circle();	
	}
	
	@Override
	public void onRestart()
	{
		super.onRestart();
        //DrawView.user = new Circle();	
	}
	
	public void switchToSupervisorMode(View view) 
	{
    	Intent intent = new Intent(this, SupervisorActivity.class);
    	String noOfUsers = ((EditText) findViewById(R.id.users)).getText().toString();
    	
    	if(Misc.isNullTrimmedString(noOfUsers) || !Misc.isInt(noOfUsers))
    	{
    		((TextView) findViewById(R.id.ErrorTextSupervisor)).setVisibility(View.VISIBLE);
    		((TextView) findViewById(R.id.ErrorTextSupervisor)).setText("Invalid number!!");
    		return;
    	}
    	else
    	{
    		intent.putExtra(Constants.NO_OF_USERS, noOfUsers);
    	}
    	
		// goto Supervisor screen
	    startActivity(intent);
	}

}
