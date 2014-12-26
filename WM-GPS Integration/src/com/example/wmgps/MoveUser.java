package com.example.wmgps;

import android.view.View;

public class MoveUser extends Thread 
{
	View view = null;
	
	 public MoveUser(DrawView drawView) {
		this.view = drawView;
	}

	public void run() 
     {
    	 /*if((DrawView.user.centerX + 2) >= DrawView.maxWidth)
    		 DrawView.scheduler.shutdown();
    	 
    	 DrawView.user.centerX += 2;  */ 
     }

}
