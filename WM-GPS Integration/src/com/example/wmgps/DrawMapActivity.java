package com.example.wmgps;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.example.testclient.FloatHolder;

import android.support.v7.app.ActionBarActivity;
import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class DrawMapActivity extends ActionBarActivity  implements Runnable
{
	/*Paint locnPaint = new Paint();
	Paint disabledLocnPaint = new Paint();
	Paint locnTextPaint = new Paint();
	Paint pathPaint = new Paint();
	Paint userPaint = new Paint();
	Paint destPaint = new Paint();
	List<Locn> locations = new ArrayList<Locn>();
	Circle user = new Circle();
	boolean supervisor = false;
    
	ScheduledExecutorService scheduler = null;
	private Handler mHandler;
	
	public static int maxWidth = 0;// 720
	public static int maxHeight = 0;// 1184
	Canvas canvas = new Canvas();*/
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_draw_map);
		/*	
		// Size
        locnPaint.setStrokeWidth(50);
        disabledLocnPaint.setStrokeWidth(50);
        pathPaint.setStrokeWidth(7);
        destPaint.setStrokeWidth(10);
        
        // Style
        locnPaint.setStyle(Style.FILL);
        disabledLocnPaint.setStyle(Style.FILL);
		userPaint.setStyle(Paint.Style.FILL); 
		
        locnTextPaint.setTextSize(50);
       // userPaint.setStrokeWidth(7);
		FontMetrics fm = new FontMetrics();
		disabledLocnPaint.setTextAlign(Paint.Align.CENTER);
		disabledLocnPaint.getFontMetrics(fm);  
		locnPaint.setTextAlign(Paint.Align.CENTER);
		locnPaint.getFontMetrics(fm); 
		
		if(isSupervisor())
		{
			user.setSupervisor(true);
		}
        
		fomulateColors();
        createLocations();
        
        maxWidth = canvas.getWidth(); 
        maxHeight = canvas.getHeight(); 
		
		mHandler = new Handler();
        mHandler.post(mUpdate);
        
        findConnection();
        drawPath(canvas);
    	 
        for(int i = 1; i < locations.size(); i++)
        {
        	if(locations.get(i).isDisabled())
        	{                
	            canvas.drawRect(locations.get(i).left-locnTextPaint.measureText(i+"")/2, locations.get(i).top - locnTextPaint.getTextSize(),
	            		locations.get(i).right + locnTextPaint.measureText(i+"")/2, locations.get(i).bottom, disabledLocnPaint);
	            canvas.drawRect(locations.get(i), disabledLocnPaint);
	            canvas.drawText(i+"", locations.get(i).left, locations.get(i).bottom ,locnTextPaint); //x=300,y=300  
        	}
        	else
        	{
	            canvas.drawRect(locations.get(i).left-locnTextPaint.measureText(i+"")/2, locations.get(i).top - locnTextPaint.getTextSize(),
	            		locations.get(i).right + locnTextPaint.measureText(i+"")/2, locations.get(i).bottom, locnPaint);
	             canvas.drawRect(locations.get(i), locnPaint);
	            canvas.drawText(i+"", locations.get(i).left, locations.get(i).bottom ,locnTextPaint); //x=300,y=300
        	}
        }
        drawUser(canvas);
        
        findDestination(canvas);
        
        if(!user.isActive() )
        {
        	scheduler = Executors.newSingleThreadScheduledExecutor();
        
        	scheduler.scheduleAtFixedRate
        	      (
    	    		  this, 3, 1, TimeUnit.SECONDS
        	     );
        }
        user.setActive(true);
	*/}
/*
	private void createLocations() // locations additions
    {
    	locations.add(new Locn(0, 0, 0, 0, true)); // 0
    	locations.add(new Locn(30, 30, 80, 80)); // 1
    	locations.add(new Locn(550, 30, 600, 80)); // 2
    	locations.add(new Locn(250, 500, 300, 550)); // 3
    	locations.add(new Locn(550, 600, 600, 650)); // 4
    	locations.add(new Locn(250, 750, 300, 800, true)); // 5
    	locations.add(new Locn(30, 900, 80, 950)); // 6
    	locations.add(new Locn(550, 900, 600, 950)); // 7
    } 
	
	private void fomulateColors() 
    {
        // locnPaint.setColor(Color.BLUE);
        // disabledLocnPaint.setColor(Color.RED);
        // pathPaint.setColor(Color.BLACK);
        // locnTextPaint.setColor(Color.CYAN);
        userPaint.setColor(Color.MAGENTA);
        // destPaint.setColor(Color.YELLOW);
        
        locnPaint.setColor(Color.BLUE);
        pathPaint.setColor(Color.GRAY);
        disabledLocnPaint.setColor(Color.rgb(255, 12, 62));
        destPaint.setColor(Color.rgb(128, 255, 0));
        locnTextPaint.setColor(Color.rgb(102, 255, 255));
	}

	private void drawSourceToDest(Canvas canvas) 
	{
		FloatHolder tempLocnX = new FloatHolder(user.centerX), tempLocnY = new FloatHolder(user.centerY);
		List<Locn> bestFitPath = user.getShortestPath(); 
		
		if(!user.isActive() && (bestFitPath == null || bestFitPath.size() == 0))
			bestFitPath = findBestFitLocationAlg(tempLocnX, tempLocnY);
		
		if(bestFitPath != null && bestFitPath.size() > 1)
		{
			user.shortestPath = bestFitPath;
			drawBestFitAlg(bestFitPath, canvas);
		}
	}

	private void drawBestFitAlg(List<Locn> bestFitPath, Canvas canvas) 
	{
		try
		{
			for(int i = 0; i < bestFitPath.size() - 1; i++)
			{
				 canvas.drawLine(bestFitPath.get(i).centerX(), 
						 bestFitPath.get(i).centerY(), 
						 bestFitPath.get(i+1).centerX(), 
						 bestFitPath.get(i+1).centerY()
						 , destPaint);
			}
		}
		catch(Exception e)
		{
			Log.e("Exception ", e.getMessage());
	    	
	    	Log.e("Exception ", e.getLocalizedMessage());
	    	
	    	Log.e("Exception ", e.getStackTrace().toString());
	    	
		}
	}*/
	
	

	private void drawUser(Canvas canvas) 
	{
		/*int startLocn = (int) (new Random().nextInt() % 8);
		startLocn = (startLocn < 0)?-startLocn:startLocn;
		
		if(scheduler == null)
		{
			user.centerX = locations.get(startLocn).centerX();
			user.centerY = locations.get(startLocn).centerY();
			user.radius = 20;
		}
		else
		{
			// Do Nothing
		}*/
		
		/*GPSTracker gps = new GPSTracker(DrawMapActivity.this);

        // Check if GPS enabled
        if(gps.canGetLocation()) 
        {
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            // \n is for new line
            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        } 
        else 
        {
            // Can't get location.
            // GPS or network is not enabled.
            // Ask user to enable GPS/network in settings.
            gps.showSettingsAlert();
        }*/
		
		/*int startLocn = (int) (new Random().nextInt() % locations.size());
		startLocn = (startLocn < 0)?-startLocn:startLocn;
		
		while(!user.isActive() && locations.get(startLocn).isDisabled())
		{
			startLocn = (int) (new Random().nextInt() % locations.size());
			startLocn = (startLocn < 0)?-startLocn:startLocn;
		}
		if(!user.isActive())
		{
			user.centerX = locations.get(startLocn).centerX();
			user.centerY = locations.get(startLocn).centerY();
			user.radius = 20;
		}
		
		canvas.drawCircle(user.centerX, user.centerY, 
				user.radius, userPaint);*/
		
	}



	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

	/*private Runnable mUpdate = new Runnable() {
		   public void run()
		   {
			   if((user.centerX + 2) >= DrawView.maxWidth)
			       mHandler.postDelayed(this, 1000);
		    }
		};*/
/*
	@SuppressLint("NewApi")
	@Override
	public void run() 
	{
		float positionToUpdate = (new Random().nextInt() % 100);
		positionToUpdate = (positionToUpdate < 0)?-positionToUpdate:positionToUpdate;

		if((this.user.centerX + 20) >= DrawView.maxWidth || (this.user.centerX == this.user.destX && this.user.centerY == this.user.destY))
		{
			this.scheduler.shutdown();
			this.scheduler = null;
			this.user.setActive(false);
			return;
		}
		
		if(user.getShortestPath() != null && user.getShortestPath().size() > (user.getCurrentLocnIndex()) && user.getShortestPath().get(user.getCurrentLocnIndex()) != null)
		{
			Locn locn = user.getShortestPath().get(user.getCurrentLocnIndex());
			if(locn.centerX() == user.centerX && locn.centerY() == user.centerY)
			{
				user.setCurrentLocnIndex(user.getCurrentLocnIndex()+1);
				if(user.getShortestPath() != null && user.getShortestPath().size() > (user.getCurrentLocnIndex()) && user.getShortestPath().get(user.getCurrentLocnIndex()) != null)
				{
					locn = user.getShortestPath().get(user.getCurrentLocnIndex());
					// user.getShortestPath().remove(0);
				}
				else
				{
					this.scheduler.shutdown();
					this.scheduler = null;
					this.user.setActive(false);
					return;
				}
			}
			if(locn.centerX() == user.centerX)
			{
				if(locn.centerY() > user.centerY) // Increment Y Axis
				{
					positionToUpdate = (positionToUpdate < 0)?-positionToUpdate:positionToUpdate;
					positionToUpdate = Math.min(positionToUpdate, locn.centerY() - user.centerY);
					user.centerY += positionToUpdate;
				}
				else // Decrement Y Axis
				{
					positionToUpdate = (positionToUpdate > 0)?-positionToUpdate:positionToUpdate;
					positionToUpdate = Math.max(positionToUpdate, locn.centerY() - user.centerY);
					user.centerY += positionToUpdate;
				}
			}
			else if(locn.centerY() == user.centerY)
			{
				if(locn.centerX() > user.centerX) // Increment X Axis
				{
					positionToUpdate = (positionToUpdate < 0)?-positionToUpdate:positionToUpdate;
					positionToUpdate = Math.min(positionToUpdate, locn.centerX() - user.centerX);
					user.centerX += positionToUpdate;
				}
				else // Decrement X Axis
				{
					positionToUpdate = (positionToUpdate > 0)?-positionToUpdate:positionToUpdate;
					positionToUpdate = Math.max(positionToUpdate, locn.centerX() - user.centerX);
					user.centerX += positionToUpdate;
				}
			}
			else // Diagonal
			{
				float deltaX = locn.centerX() - user.centerX;
				float deltaY = locn.centerY() - user.centerY;
				float L = 0.2F;
				if( deltaX > -100 && deltaX < 100 && deltaY > -100 && deltaY < 100)
				{
					user.centerX = locn.centerX();
					user.centerY = locn.centerY();
				}
				else
				{
					user.centerX = user.centerX + (L * deltaX);
					user.centerY = user.centerY + (L * deltaY);
				}
				
			}
			
			 if(locn.centerX() == user.centerX && locn.centerY() == user.centerY)
					user.setCurrentLocnIndex(user.getCurrentLocnIndex()+1);
				// user.getShortestPath().remove(0);
		}
		else
		{
			this.scheduler.shutdown();
			this.user.setActive(false);
			this.scheduler = null;
		}
		
   	 	// DrawView.user.centerX += 20;  
   	 	this.invalidateOptionsMenu();
	}

	public void setSupervisor(boolean supervisor) {
		this.supervisor = supervisor;
		
	}

	public boolean isSupervisor() {
		return supervisor;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.draw_map, menu);
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
	}*/
	/*
	 * 
	 * 
	private void findConnection() 
	{
		locations.get(1).getLinksToLocations().add(locations.get(2));
		locations.get(1).getLinksToLocations().add(locations.get(3));
		locations.get(1).getLinksToLocations().add(locations.get(6));
		locations.get(2).getLinksToLocations().add(locations.get(1));
		locations.get(2).getLinksToLocations().add(locations.get(5));
		locations.get(3).getLinksToLocations().add(locations.get(1));
		locations.get(3).getLinksToLocations().add(locations.get(6));
		locations.get(3).getLinksToLocations().add(locations.get(5));
		locations.get(4).getLinksToLocations().add(locations.get(7));
		locations.get(5).getLinksToLocations().add(locations.get(3));
		locations.get(5).getLinksToLocations().add(locations.get(2));
		locations.get(5).getLinksToLocations().add(locations.get(7));
		locations.get(6).getLinksToLocations().add(locations.get(1));
		locations.get(6).getLinksToLocations().add(locations.get(3));
		locations.get(6).getLinksToLocations().add(locations.get(7));
		locations.get(7).getLinksToLocations().add(locations.get(4));
		locations.get(7).getLinksToLocations().add(locations.get(5));
		locations.get(7).getLinksToLocations().add(locations.get(6));
	}

	private void drawPath(Canvas canvas) 
	{
		for(int i = 1; i < locations.size(); i++)
        {
			List<Locn> connections = locations.get(i).getLinksToLocations();
			float startX = locations.get(i).centerX();
			float startY = locations.get(i).centerY();
			
			for(int j = 0; j < connections.size(); j++)
			{
				 canvas.drawLine(startX, startY, 
						 connections.get(j).centerX(), connections.get(j).centerY()
						 , pathPaint);
			}
        }
	}
	
	
	
	private void findDestination(Canvas canvas2) 
	{
		int destLocn = (int) (new Random().nextInt() % locations.size());
		destLocn = (destLocn < 0)?-destLocn:destLocn;
		
		float destX = locations.get(destLocn).centerX();
		float destY = locations.get(destLocn).centerY();
		
		while(!user.isActive()
				&& (destX == user.centerX && destY == user.centerY || locations.get(destLocn).isDisabled()))
		{
			destLocn = (int) (new Random().nextInt() % locations.size());
			destLocn = (destLocn < 0)?-destLocn:destLocn;
			
			destX = locations.get(destLocn).centerX();
			destY = locations.get(destLocn).centerY();
		}
		
		if(!user.isActive()) // if(scheduler == null)
		{
			user.destX = destX;
			user.destY = destY;
		}
		
		drawSourceToDest(canvas2);
	}

	private List<Locn> findBestFitLocationAlg(FloatHolder tempLocnX, FloatHolder tempLocnY) 
	{
		List<Locn> visitedLocns = new ArrayList<Locn>();
		List<Locn> rejectedLocns = new ArrayList<Locn>();
		List<Locn> minShortestLocns = new ArrayList<Locn>();
		Locn currentLocn = null;
		Locn destLocn = null;
		
		FloatHolder minShortestDist = new FloatHolder(9999);
		
		for(int i = 1; i < locations.size(); i++)
        {
			if(currentLocn == null)
				currentLocn = locations.get(i).findLocationForCoOrdinates(tempLocnX, tempLocnY);
			if(destLocn == null)
				destLocn = locations.get(i).findLocationForCoOrdinates(user.destX, user.destY);
        }
		if(currentLocn == null || destLocn == null)
			return null;
		
		// minShortestLocns.add(currentLocn);
		visitedLocns.add(currentLocn);
		TraverseAllPaths(currentLocn, destLocn, visitedLocns, rejectedLocns, minShortestLocns, minShortestDist);
		return minShortestLocns;
	}

	private boolean TraverseAllPaths(Locn currentLocn, Locn destLocn, List<Locn> visitedLocns, List<Locn> rejectedLocns, List<Locn> minShortestLocns, FloatHolder minShortestDist) 
	{
		if(currentLocn == destLocn) 
			return true;
		
		for(int i = 0; i < currentLocn.getLinksToLocations().size(); i++)
		// while((childLocn = FetchOptimalChild(currentLocn, destLocn, visitedLocns, rejectedLocns)) != null)
		{
			Locn childLocn = currentLocn.getLinksToLocations().get(i);
			if(!visitedLocns.contains(childLocn) && !rejectedLocns.contains(childLocn) && !childLocn.isDisabled())
			{
				visitedLocns.add(childLocn);
				if(TraverseAllPaths(childLocn, destLocn, visitedLocns, rejectedLocns, minShortestLocns, minShortestDist))
				{
					// try calculating this distance for each recursive logic instead of finding at the end. 
					// but again performance hit would depend on the number of hits of successful finding path.  
					FloatHolder shortestDist = new FloatHolder(findShortestDist(visitedLocns));
					if(minShortestDist.getValue() > shortestDist.getValue())
					{
						minShortestDist.setValue(shortestDist.getValue());
						minShortestLocns.clear();
						minShortestLocns.addAll(visitedLocns);
					}
					visitedLocns.remove(childLocn);
				}
				else
				{
					visitedLocns.remove(childLocn);
					// need this because i don't end up searching already visited paths.
					// But again, can't use VisitedLocn's because that will have my entire Path captured.
					// rejectedLocns.add(childLocn); 
				}
			}
		}
		return false;
	}
		
	private float findShortestDist(List<Locn> visitedLocns) 
	{
		float minDist = 0;
		for(int i = 0; i < visitedLocns.size() - 1; i++)
		{
			minDist += getDistanceBetweenPoints(visitedLocns.get(i).centerX(), visitedLocns.get(i).centerY(), 
					visitedLocns.get(i+1).centerX(), visitedLocns.get(i+1).centerY());
		}
		return minDist;
	}
	
	private float getDistanceBetweenPoints(float x1, float y1, float x2, float y2)
	{
		return (float) Math.sqrt(Math.pow((x2-x1), 2) + Math.pow((y2-y1), 2));
	}*/
}
