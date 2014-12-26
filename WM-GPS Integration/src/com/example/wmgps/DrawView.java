package com.example.wmgps;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.example.testclient.Constants;
import com.example.testclient.FloatHolder;
import com.example.testclient.Misc;
import com.example.testclient.ServerSocketListener;
import com.example.testclient.WrapperConverter;
import com.example.testclient.WrapperUser;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DrawView extends View implements Runnable,Parcelable 
{
	Paint locnPaint = new Paint();
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
	Canvas canvas = null;

    public DrawView(Context context) 
    {
        super(context);
        
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

	@Override
    public void onDraw(Canvas canvas) 
	{
    	//  drawLine (float startX, float startY, float stopX, float stopY, Paint paint)

		try
		{
			synchronized (user) 
			{
		        maxWidth = canvas.getWidth(); 
		        maxHeight = canvas.getHeight(); 
				
				mHandler = new Handler();
		        mHandler.post(mUpdate);
		        
		        if(!user.isSupervisor())
		        {
			        if(!user.isActive())
			        	getLocationInformationFromServer();
			        
			        drawLocationAndPath(canvas);
			        drawUser(canvas);
			        
			        if(!user.isActive())
			        	getShortestPathFromUserSource();
			        
			        drawSourceToDest(canvas);
			        
			        if(!user.isActive() )
			        {
			        	scheduler = Executors.newSingleThreadScheduledExecutor();
			        
			        	scheduler.scheduleAtFixedRate
			        	      (
			    	    		  this, 3, 1, TimeUnit.SECONDS
			        	     );
			        }
			        user.setActive(true);
			       	 // this.postInvalidate();
			        // findDestinnation();
			            
			           /* canvas.drawLine(20, 0, 0, 20, paint);
			            canvas.drawLine(20, 0, 0, 20, paint);
			            canvas.drawLine(20, 0, 0, 20, paint);
			            canvas.drawLine(20, 0, 0, 20, paint);*/
		        }
		        else
		        {
			        drawLocationAndPath(canvas);
			        drawUser(canvas);
			        drawSourceToDest(canvas);
			        boolean isSchedulerEnabled = user.isSupervisorSchedulerEnabled();
			        user.setSupervisorSchedulerEnabled(true);
			        
			        if(!isSchedulerEnabled )
			        {
			        	scheduler = Executors.newSingleThreadScheduledExecutor();
			        
			        	scheduler.scheduleAtFixedRate
			        	      (
			    	    		  this, 3, 1, TimeUnit.SECONDS
			        	     );
			        }
		        }
			}
		}
		catch(Exception ex)
		{
			Log.e("", "Exception ", ex);
		}
    }

	private void getShortestPathFromUserSource()
	{
		ServerSocketListener serverSocketListener = new ServerSocketListener();
		serverSocketListener.addKeys(Constants.VALUES_AVALIABLE, Constants.FIND_SHORTEST_PATH);
		serverSocketListener.addValues(user.getUser());
		serverSocketListener.setResponseNeeded(true);
		serverSocketListener.invoke();
		List outputList = serverSocketListener.getOutputList();
		WrapperConverter.convertWrapperToUser(user, outputList, locations);
	}

	private void getLocationInformationFromServer() 
	{
		ServerSocketListener serverSocketListener = new ServerSocketListener();
		serverSocketListener.addKeys(Constants.VALUES_NOT_AVALIABLE, Constants.FETCH_LOCATION_LIST);
		serverSocketListener.setResponseNeeded(true);
		serverSocketListener.invoke();
		List outputList = serverSocketListener.getOutputList();
		locations = WrapperConverter.convertWrapperToLocn(outputList);
	}

	private void drawSourceToDest(Canvas canvas) 
	{
		FloatHolder tempLocnX = new FloatHolder(user.getCenterX()), tempLocnY = new FloatHolder(user.getCenterY());
		List<Locn> bestFitPath = user.getShortestPath(); 
		
		// Kish - TODO : use the bestFitPath from server
		
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
	}
	
	private void drawUser(Canvas canvas) 
	{
		GPSTracker gps = new GPSTracker(getContext());

        // Check if GPS enabled
        if(gps.canGetLocation()) 
        {
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            /*
            // \n is for new line
            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();*/
        } 
        else 
        {
            // Can't get location.
            // GPS or network is not enabled.
            // Ask user to enable GPS/network in settings.
            gps.showSettingsAlert();
        }
		
		int startLocn = (int) (new Random().nextInt() % locations.size());
		startLocn = (startLocn < 0)?-startLocn:startLocn;
		// startLocn = 4; // Kish - Todo : only for debugging
		
		while(!user.isActive() && locations.get(startLocn).isDisabled())
		{
			startLocn = (int) (new Random().nextInt() % locations.size());
			startLocn = (startLocn < 0)?-startLocn:startLocn;
		}
		if(!user.isActive())
		{
			user.setCenterX(locations.get(startLocn).centerX());
			user.setCenterY(locations.get(startLocn).centerY());
			user.radius = Constants.USER_RADIUS;
		}
		
		canvas.drawCircle(user.getCenterX(), user.getCenterY(), 
				user.radius, userPaint);
		
	}

	private void drawLocationAndPath(Canvas canvas) 
	{
        for(int i = 1; i < locations.size(); i++)
        {
        	if(locations.get(i).isDisabled())
        	{                
	            /*canvas.drawRect(locations.get(i).left-locnTextPaint.measureText(i+"")/2, locations.get(i).top - locnTextPaint.getTextSize(),
	            		locations.get(i).right + locnTextPaint.measureText(i+"")/2, locations.get(i).bottom, disabledLocnPaint);*/
	            canvas.drawRect(locations.get(i), disabledLocnPaint);
	            canvas.drawText(i+"", locations.get(i).left, locations.get(i).bottom ,locnTextPaint); //x=300,y=300  
        	}
        	else
        	{
	            /*canvas.drawRect(locations.get(i).left-locnTextPaint.measureText(i+"")/2, locations.get(i).top - locnTextPaint.getTextSize(),
	            		locations.get(i).right + locnTextPaint.measureText(i+"")/2, locations.get(i).bottom, locnPaint);*/
	             canvas.drawRect(locations.get(i), locnPaint);
	            canvas.drawText(i+"", locations.get(i).left, locations.get(i).bottom ,locnTextPaint); //x=300,y=300
        	}
        	
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
	
	private Runnable mUpdate = new Runnable() {
		   public void run()
		   {
			   if((user.getCenterX() + 2) >= DrawView.maxWidth)
			       mHandler.postDelayed(this, 1000);
			  /* else
				   user.centerX += 2;  */
		    }
		};

	@Override
	public void run() 
	{
		if(!user.isSupervisor())
		{
				float positionToUpdate = (new Random().nextInt() % 100);
				positionToUpdate = (positionToUpdate < 0)?-positionToUpdate:positionToUpdate;
		
				if((this.user.getCenterX() + 20) >= DrawView.maxWidth || (this.user.getCenterX() == this.user.getDestX() && this.user.getCenterY() == this.user.getDestY()))
				{
					this.scheduler.shutdown();
					this.scheduler = null;
					this.user.setActive(false);
					return;
				}
				
				if(user.getShortestPath() != null && user.getShortestPath().size() > (user.getCurrentLocnIndex()) && user.getShortestPath().get(user.getCurrentLocnIndex()) != null)
				{
					Locn locn = user.getShortestPath().get(user.getCurrentLocnIndex());
					if(locn.centerX() == user.getCenterX() && locn.centerY() == user.getCenterY())
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
					if(locn.centerX() == user.getCenterX())
					{
						if(locn.centerY() > user.getCenterY()) // Increment Y Axis
						{
							positionToUpdate = (positionToUpdate < 0)?-positionToUpdate:positionToUpdate;
							positionToUpdate = Math.min(positionToUpdate, locn.centerY() - user.getCenterY());
							user.setCenterY(user.getCenterY() + positionToUpdate);
						}
						else // Decrement Y Axis
						{
							positionToUpdate = (positionToUpdate > 0)?-positionToUpdate:positionToUpdate;
							positionToUpdate = Math.max(positionToUpdate, locn.centerY() - user.getCenterY());
							user.setCenterY(user.getCenterY() + positionToUpdate);
						}
					}
					else if(locn.centerY() == user.getCenterY())
					{
						if(locn.centerX() > user.getCenterX()) // Increment X Axis
						{
							positionToUpdate = (positionToUpdate < 0)?-positionToUpdate:positionToUpdate;
							positionToUpdate = Math.min(positionToUpdate, locn.centerX() - user.getCenterX());
							user.setCenterX(user.getCenterX() + positionToUpdate);
						}
						else // Decrement X Axis
						{
							positionToUpdate = (positionToUpdate > 0)?-positionToUpdate:positionToUpdate;
							positionToUpdate = Math.max(positionToUpdate, locn.centerX() - user.getCenterX());
							user.setCenterX(user.getCenterX() + positionToUpdate);
						}
					}
					else // Diagonal
					{
						float deltaX = locn.centerX() - user.getCenterX();
						float deltaY = locn.centerY() - user.getCenterY();
						float L = 0.2F;
						if( deltaX > -100 && deltaX < 100 && deltaY > -100 && deltaY < 100)
						{
							user.setCenterX(locn.centerX());
							user.setCenterY(locn.centerY());
						}
						else
						{
							user.setCenterX(user.getCenterX() + (L * deltaX));
							user.setCenterY(user.getCenterY() + (L * deltaY));
						}
						
						/*if(user.centerX > user.destX)
							user.centerX = user.destX;
						if(user.centerY > user.destY)
							user.centerY = user.destY;*/
		
						/*if(locn.centerX() > user.centerX && locn.centerX() != user.centerX)
						{
							deltaX = locn.centerX() - user.centerX;
						}
						else if(locn.centerX() < user.centerX)
						{
							deltaX = locn.centerX() - user.centerX;
						}*/
						
					}
					
					 if(locn.centerX() == user.getCenterX() && locn.centerY() == user.getCenterY())
							user.setCurrentLocnIndex(user.getCurrentLocnIndex()+1);
						// user.getShortestPath().remove(0);
				}
				else
				{
					this.scheduler.shutdown();
					this.user.setActive(false);
					this.scheduler = null;
				}
			
			updateServerWithNewUser(user.user);
		}
		else
		{
			user.user = getUserFromSessionId(user.getSessionId()+"");
			if(user.user == null)
			{
				this.scheduler.shutdown();
				this.user.setActive(false);
				this.scheduler = null;
			}
			/*else
			{
		        drawUser(canvas);
			}*/
		}
		
   	 	// DrawView.user.centerX += 20;  
   	 	this.postInvalidate();
	}

	private void updateServerWithNewUser(WrapperUser wrapuser) 
	{
		ServerSocketListener serverSocketListener = new ServerSocketListener();
		serverSocketListener.addKeys(Constants.VALUES_AVALIABLE, Constants.UPDATE_USER_ON_SESSION);
		serverSocketListener.addValues(wrapuser);
		serverSocketListener.setResponseNeeded(false);
		serverSocketListener.invoke();
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

	public void setSupervisor(boolean supervisor) {
		this.supervisor = supervisor;
		
	}

	public boolean isSupervisor() {
		return supervisor;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// dest.writeInt(mData);
	}
}
