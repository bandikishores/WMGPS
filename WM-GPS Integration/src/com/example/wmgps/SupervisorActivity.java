package com.example.wmgps;

import java.util.ArrayList;
import java.util.List;

import com.example.testclient.Constants;
import com.example.testclient.Misc;
import com.example.testclient.ServerSocketListener;
import com.example.testclient.WrapperConverter;

import de.greenrobot.event.EventBus;
import android.support.v7.app.ActionBarActivity;
import android.text.DynamicLayout;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class SupervisorActivity extends ActionBarActivity 
{
	public static Circle supervisorUser = new Circle();
	public static List listOfSessions = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_supervisor);
				
		listOfSessions = getSessionListFromServer();
	     
	     // savedInstanceState.putString(Constants.NO_OF_USERS, noOfUsers);
	     ScrollView scrollView=new ScrollView(this);
	    	LinearLayout linearlayout = new LinearLayout(this);
	    	linearlayout.setOrientation(LinearLayout.VERTICAL);
	        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
	                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
	        
	        if(listOfSessions.size() == 0)
	        {
	        	TextView textView = new TextView(this);
		    	textView.setId(123987456);
		    	textView.setLayoutParams(new LinearLayout.LayoutParams( LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1f));
		    	textView.setText(" No Users found to be Active ");
		    	
		    	linearlayout.addView(textView, layoutParams);
	        }
	        
	     for(int i = 0; i < listOfSessions.size(); i++)
	     {
	    	TextView textView = new TextView(this);
	    	textView.setId((i+200)*4);
	    	textView.setLayoutParams(new LinearLayout.LayoutParams( LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1f));
	    	textView.setText("User "+ (i+1) +" Details : \n");
	    	
			Button button = new Button(this);
	    	button.setId(i);
	    	button.setLayoutParams(new LinearLayout.LayoutParams( LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1f));
	    	// button.setWidth(100);
	    	button.setImeOptions(EditorInfo.IME_ACTION_NEXT);
	    	button.setInputType(InputType.TYPE_CLASS_NUMBER);
	    	button.setKeyListener(DigitsKeyListener.getInstance());
	    	// button.setMaxLines(1);
	    	button.setText("View User "+ (i+1) + " Route");
	    	
	    	EditText editText = new EditText(this);
	    	editText.setId((i+200)*2);
	    	editText.setLayoutParams(new LinearLayout.LayoutParams( LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1f));
	    	editText.setHint("Enter Message For User "+ (i+1));
	    	
			Button messageButton = new Button(this);
			messageButton.setId((i+200)*3);
			messageButton.setLayoutParams(new LinearLayout.LayoutParams( LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1f));
	    	// button.setWidth(100);
			messageButton.setImeOptions(EditorInfo.IME_ACTION_NEXT);
	    	messageButton.setInputType(InputType.TYPE_CLASS_NUMBER);
	    	messageButton.setKeyListener(DigitsKeyListener.getInstance());
	    	// button.setMaxLines(1);
	    	messageButton.setText("Send Message To User "+ (i+1) );
	    	messageButton.setOnClickListener(new View.OnClickListener() 
	    	{
	            public void onClick(View view) 
	            {	            	
	            	try
	            	{
	            		int index = (view.getId()/3) - 200;
	            		String sessionId = listOfSessions.get(index) + "";
	            		String message = ((EditText) findViewById((index+200)*2)).getText().toString();
	            		
	            		if(Misc.isNullTrimmedString(message) || Misc.isNullTrimmedString(sessionId))
	            			return;
	            		
	            		sendMessageToUser(sessionId, message);
	            		((EditText) findViewById((index+200)*2)).setHint("Enter Message For User "+ (index+1));
		            	return;
	            	}
	            	catch (Exception e) 
	            	{
						Log.e("", " Object ", e);
					}
	            }
	        });
	    	
	    	DrawView workerDrawView = new DrawView(this);
	    	workerDrawView.setBackgroundColor(Color.rgb(202, 153, 61));
	    	supervisorUser.addUserDrawView(workerDrawView);
	    	
	    	final Context context = this;
	    	
	    	button.setOnClickListener(new View.OnClickListener() 
	    	{
	            public void onClick(View view) 
	            {
	                // Toast.makeText(DynamicLayout.this, "Button clicked index = " , Toast.LENGTH_SHORT).show();
	            	
	            	// DrawView workerDrawView = new DrawView();
	        		// workerDrawView.setBackgroundColor(Color.GRAY);
	            	
	            	try
	            	{
		            	// DrawView userView = supervisorUser.getUserDrawViews().get(view.getId());
		            	
	            		String sessionId = listOfSessions.get(view.getId()) + "";
		            	Intent intent = new Intent(getApplicationContext(), UserActivity.class);
		            	intent.putExtra(Constants.SESSION_FROM_SUPERVISOR, sessionId);
		            	// intent.putExtra("UserView", userView);
		            	// EventBus.getDefault().postSticky(userView); // java.lang.NoClassDefFoundError: de.greenrobot.event.EventBus
		            	startActivity(intent);
	            	}
	            	catch (Exception e) 
	            	{
						Log.e("", " Object ", e);
					}
	            }
	        });
	    	linearlayout.addView(textView, layoutParams);
	    	linearlayout.addView(button, layoutParams);
	    	linearlayout.addView(editText, layoutParams);
	    	linearlayout.addView(messageButton, layoutParams);
	     }
	     scrollView.addView(linearlayout);
	     setContentView(scrollView);
                
      /*  TableLayout tableLayout = new TableLayout(this);
        tableLayout.setStretchAllColumns(true);
        int noOfRows = 1;
        for (int i = 0; i < noOfRows; i++) 
        {
            tableLayout.addView(createOneFullRow(editText));
        }
        
       // tableLayout.addView(createLeftCells(editText));
        // setContentView(drawView);   
                    
        drawView.setSupervisor(true);
    	for(int i = 0; i < Constants.NO_OF_USERS; i++)
    	{
    		DrawView workerDrawView = new DrawView(this);
    		// workerDrawView.setBackgroundColor(Color.GRAY);
    		drawView.setBackgroundColor(Color.rgb(202, 153, 61));
    		drawView.user.addUserDrawView(workerDrawView);
    		workerDrawView.user.setSupervisorView(drawView);
    	}
    	*/
		
		// savedInstanceState.
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		listOfSessions = getSessionListFromServer();
	     
	     // savedInstanceState.putString(Constants.NO_OF_USERS, noOfUsers);
	     ScrollView scrollView=new ScrollView(this);
	    	LinearLayout linearlayout = new LinearLayout(this);
	    	linearlayout.setOrientation(LinearLayout.VERTICAL);
	        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
	                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
	        
	        if(listOfSessions.size() == 0)
	        {
	        	TextView textView = new TextView(this);
		    	textView.setId(123987456);
		    	textView.setLayoutParams(new LinearLayout.LayoutParams( LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1f));
		    	textView.setText(" No Users found to be Active ");
		    	
		    	linearlayout.addView(textView, layoutParams);
	        }
	        
	     for(int i = 0; i < listOfSessions.size(); i++)
	     {
	    	TextView textView = new TextView(this);
	    	textView.setId((i+200)*4);
	    	textView.setLayoutParams(new LinearLayout.LayoutParams( LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1f));
	    	textView.setText("User "+ (i+1) +" Details : \n");
	    	
			Button button = new Button(this);
	    	button.setId(i);
	    	button.setLayoutParams(new LinearLayout.LayoutParams( LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1f));
	    	// button.setWidth(100);
	    	button.setImeOptions(EditorInfo.IME_ACTION_NEXT);
	    	button.setInputType(InputType.TYPE_CLASS_NUMBER);
	    	button.setKeyListener(DigitsKeyListener.getInstance());
	    	// button.setMaxLines(1);
	    	button.setText("View User "+ (i+1) + " Route");
	    	
	    	EditText editText = new EditText(this);
	    	editText.setId((i+200)*2);
	    	editText.setLayoutParams(new LinearLayout.LayoutParams( LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1f));
	    	editText.setHint("Enter Message For User "+ (i+1));
	    	
			Button messageButton = new Button(this);
			messageButton.setId((i+200)*3);
			messageButton.setLayoutParams(new LinearLayout.LayoutParams( LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1f));
	    	// button.setWidth(100);
			messageButton.setImeOptions(EditorInfo.IME_ACTION_NEXT);
	    	messageButton.setInputType(InputType.TYPE_CLASS_NUMBER);
	    	messageButton.setKeyListener(DigitsKeyListener.getInstance());
	    	// button.setMaxLines(1);
	    	messageButton.setText("Send Message To User "+ (i+1) );
	    	messageButton.setOnClickListener(new View.OnClickListener() 
	    	{
	            public void onClick(View view) 
	            {	            	
	            	try
	            	{
	            		int index = (view.getId()/3) - 200;
	            		String sessionId = listOfSessions.get(index) + "";
	            		String message = ((EditText) findViewById((index+200)*2)).getText().toString();
	            		
	            		if(Misc.isNullTrimmedString(message) || Misc.isNullTrimmedString(sessionId))
	            			return;
	            		
	            		sendMessageToUser(sessionId, message);
	            		((EditText) findViewById((index+200)*2)).setHint("Enter Message For User "+ (index+1));
		            	return;
	            	}
	            	catch (Exception e) 
	            	{
						Log.e("", " Object ", e);
					}
	            }
	        });
	    	
	    	DrawView workerDrawView = new DrawView(this);
	    	workerDrawView.setBackgroundColor(Color.rgb(202, 153, 61));
	    	supervisorUser.addUserDrawView(workerDrawView);
	    	
	    	final Context context = this;
	    	
	    	button.setOnClickListener(new View.OnClickListener() 
	    	{
	            public void onClick(View view) 
	            {
	                // Toast.makeText(DynamicLayout.this, "Button clicked index = " , Toast.LENGTH_SHORT).show();
	            	
	            	// DrawView workerDrawView = new DrawView();
	        		// workerDrawView.setBackgroundColor(Color.GRAY);
	            	
	            	try
	            	{
		            	// DrawView userView = supervisorUser.getUserDrawViews().get(view.getId());
		            	
	            		String sessionId = listOfSessions.get(view.getId()) + "";
		            	Intent intent = new Intent(getApplicationContext(), UserActivity.class);
		            	intent.putExtra(Constants.SESSION_FROM_SUPERVISOR, sessionId);
		            	// intent.putExtra("UserView", userView);
		            	// EventBus.getDefault().postSticky(userView); // java.lang.NoClassDefFoundError: de.greenrobot.event.EventBus
		            	startActivity(intent);
	            	}
	            	catch (Exception e) 
	            	{
						Log.e("", " Object ", e);
					}
	            }
	        });
	    	linearlayout.addView(textView, layoutParams);
	    	linearlayout.addView(button, layoutParams);
	    	linearlayout.addView(editText, layoutParams);
	    	linearlayout.addView(messageButton, layoutParams);
	     }
	     scrollView.addView(linearlayout);
	     setContentView(scrollView);
		
		return;
	}
	

    public List getSessionListFromServer() 
    {
    	ServerSocketListener serverSocketListener = new ServerSocketListener();
		serverSocketListener.addKeys(Constants.VALUES_NOT_AVALIABLE, Constants.GET_LIST_OF_SESSIONS);
		serverSocketListener.setResponseNeeded(true);
		serverSocketListener.invoke();
		List outputList = serverSocketListener.getOutputList();
		return outputList;
	}
    
    public void sendMessageToUser(String sessionId, String message) 
    {
    	ServerSocketListener serverSocketListener = new ServerSocketListener();
		serverSocketListener.addKeys(Constants.VALUES_AVALIABLE, Constants.SEND_MESSAGE_TO_USER);
		serverSocketListener.addValues(sessionId, message);
		serverSocketListener.setResponseNeeded(false);
		serverSocketListener.invoke();
	}


	private TableRow createLeftCells(EditText editText ) 
    {
        TableRow tableRow = new TableRow(this);
        tableRow.setPadding(0, 10, 0, 0);
        tableRow.addView(editText);
        return tableRow;
    }

    private TableRow createOneFullRow(EditText editText ) 
    {
        TableRow tableRow = new TableRow(this);
        tableRow.setPadding(0, 10, 0, 0);
        tableRow.addView(editText);
        return tableRow;
    }    

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.supervisor, menu);
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
}
