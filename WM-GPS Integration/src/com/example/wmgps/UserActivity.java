package com.example.wmgps;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.example.testclient.Constants;
import com.example.testclient.Misc;
import com.example.testclient.ServerSocketListener;
import com.example.testclient.WrapperConverter;
import com.example.testclient.WrapperUser;

import de.greenrobot.event.EventBus;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class UserActivity extends ActionBarActivity 
{
	DrawView drawView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user);
		
		Intent intent = getIntent();
		
		// drawView = (DrawView) EventBus.getDefault().removeStickyEvent(DrawView.class);// intent.getSerializableExtra("UserView");
		String session = intent.getStringExtra(Constants.SESSION_FROM_SUPERVISOR);
		drawView = new DrawView(this);
		drawView.setBackgroundColor(Color.rgb(202, 153, 61));
		if(!Misc.isNullTrimmedString(session) && Misc.isInt(session))
		{
			drawView.user.setSessionId(Integer.parseInt(session));
			drawView.user.setSupervisor(true);
			drawView.user.setRadius(Constants.USER_RADIUS);
			drawView.locations = getLocationInformationFromServer();
			drawView.user.user = getUserFromSessionId(session);
			drawView.user.setSupervisorSchedulerEnabled(false);
			
			if(drawView.user.user == null)
			{
				onBackPressed();
				return;
			}
			getShortestPathFromUserSource(drawView.user, drawView.locations);
			setContentView(drawView);
		}
		else
		{
			session = intent.getStringExtra(Constants.SESSION_ID);
			if(!Misc.isNullTrimmedString(session) && Misc.isInt(session))
				drawView.user.setSessionId(Integer.parseInt(session));
	        setContentView(drawView);
		}	
	        // drawView.setBackgroundColor(Color.rgb(96, 159, 96));
			// File drawableFile = new File(getApplicationContext().getFilesDir().getAbsolutePath()+"/background.png");
	
		  /*  if(Drawable.createFromPath(drawableFile.getAbsolutePath())!=null)
		    	drawView.setBackground(Drawable.createFromPath(drawableFile.getAbsolutePath()));
		    else*/
		   

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user, menu);
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

	private List<Locn> getLocationInformationFromServer() 
	{
		List<Locn> locations = new ArrayList<Locn>();
		ServerSocketListener serverSocketListener = new ServerSocketListener();
		serverSocketListener.addKeys(Constants.VALUES_NOT_AVALIABLE, Constants.FETCH_LOCATION_LIST);
		serverSocketListener.setResponseNeeded(true);
		serverSocketListener.invoke();
		List outputList = serverSocketListener.getOutputList();
		locations = WrapperConverter.convertWrapperToLocn(outputList);
		return locations;
	}

	private void getShortestPathFromUserSource(Circle user, List<Locn> locations)
	{
		ServerSocketListener serverSocketListener = new ServerSocketListener();
		serverSocketListener.addKeys(Constants.VALUES_AVALIABLE, Constants.FIND_SHORTEST_PATH);
		serverSocketListener.addValues(user.getUser());
		serverSocketListener.setResponseNeeded(true);
		serverSocketListener.invoke();
		List outputList = serverSocketListener.getOutputList();
		WrapperConverter.convertWrapperToUser(user, outputList, locations);
	}

	private WrapperUser getUserFromSessionId(String session) 
	{
		ServerSocketListener serverSocketListener = new ServerSocketListener();
		serverSocketListener.addKeys(Constants.VALUES_AVALIABLE, Constants.GET_USER_FROM_SESSION_ID);
		serverSocketListener.addValues(session);
		serverSocketListener.setResponseNeeded(true);
		serverSocketListener.invoke();
		List outputList = serverSocketListener.getOutputList();

		if(!Misc.isNullList(outputList))
			return (WrapperUser) outputList.get(0);
		else
			return null;
	}
	
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		if(drawView != null && drawView.user != null && !drawView.user.isSupervisor())
		{
			ServerSocketListener serverSocketListener = new ServerSocketListener();
			serverSocketListener.addKeys(Constants.VALUES_AVALIABLE, Constants.REMOVE_USER_BY_SESSION_ID);
			serverSocketListener.addValues(drawView.user.getSessionId()+"");
			serverSocketListener.setResponseNeeded(false);
			serverSocketListener.invoke();
		}
		
		return;
	}
}
