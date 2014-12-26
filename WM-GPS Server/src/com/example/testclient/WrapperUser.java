package com.example.testclient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class WrapperUser implements Serializable
{
	float centerX;
	float centerY;
	float destX;
	float destY;
	List<WrapperLocn> shortestPath = new ArrayList<WrapperLocn>();
	int currentLocnIndex = 0;
	boolean active = false;
	int sessionId = 0;
	
	public WrapperUser() 
	{
		super();
	}

	public float getCenterX() {
		return centerX;
	}

	public void setCenterX(float centerX) {
		this.centerX = centerX;
	}

	public float getCenterY() {
		return centerY;
	}

	public void setCenterY(float centerY) {
		this.centerY = centerY;
	}

	public float getDestX() {
		return destX;
	}

	public void setDestX(float destX) {
		this.destX = destX;
	}

	public float getDestY() {
		return destY;
	}

	public void setDestY(float destY) {
		this.destY = destY;
	}

	public List<WrapperLocn> getShortestPath() {
		return shortestPath;
	}

	public void setShortestPath(List<WrapperLocn> shortestPath) {
		this.shortestPath = shortestPath;
	}

	public int getCurrentLocnIndex() {
		return currentLocnIndex;
	}

	public void setCurrentLocnIndex(int currentLocnIndex) {
		this.currentLocnIndex = currentLocnIndex;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public int getSessionId() {
		return sessionId;
	}

	public void setSessionId(int sessionId) {
		this.sessionId = sessionId;
	}
	
	

}
