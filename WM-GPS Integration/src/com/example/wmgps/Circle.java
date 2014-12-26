package com.example.wmgps;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.example.testclient.WrapperUser;

public class Circle implements Serializable {
	float radius;
	List<Locn> shortestPath = new ArrayList<Locn>();
	List<DrawView> userDrawViews = new ArrayList<DrawView>();
	DrawView supervisorView = null;
	boolean supervisor = false;
	boolean supervisorSchedulerEnabled = false;
	WrapperUser user = new WrapperUser();

    public Circle() {
		super();
	}

	Circle(float centerX, float centerY, float radius) {
        this.radius = radius;
        this.user.setCenterX(centerX);
        this.user.setCenterY(centerY);
    }

    @Override
    public String toString() {
        return "Circle[" + user.getCenterX() + ", " + user.getCenterY() + ", " + radius + "]";
    }
    
    public void setDestination(float destX, float destY)
    {
    	this.user.setDestX(destX);
    	this.user.setDestY(destY);
    }

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

	public float getCenterX() {
		return user.getCenterX();
	}

	public void setCenterX(float centerX) {
		this.user.setCenterX(centerX);
	}

	public float getCenterY() {
		return this.user.getCenterY();
	}

	public void setCenterY(float centerY) {
		this.user.setCenterY(centerY);
	}

	public float getDestX() {
		return this.user.getDestX();
	}

	public void setDestX(float destX) {
		this.user.setDestX(destX);
	}

	public float getDestY() {
		return this.user.getDestY();
	}

	public void setDestY(float destY) {
		this.user.setDestY(destY);
	}

	public List<Locn> getShortestPath() {
		return shortestPath;
	}

	public void setShortestPath(List<Locn> shortestPath) {
		this.shortestPath = shortestPath;
	}

	public int getCurrentLocnIndex() {
		return this.user.getCurrentLocnIndex();
	}

	public void setCurrentLocnIndex(int currentLocnIndex) {
		this.user.setCurrentLocnIndex(currentLocnIndex);
	}

	public boolean isActive() {
		return this.user.isActive();
	}

	public void setActive(boolean isActive) {
		this.user.setActive(isActive);
	}

	public List<DrawView> getUserDrawViews() {
		return userDrawViews;
	}

	public void setUserDrawViews(List<DrawView> userDrawViews) {
		this.userDrawViews = userDrawViews;
	}
	
	public void addUserDrawView(DrawView drawView)
	{
		this.userDrawViews.add(drawView);
	}

	public boolean isSupervisor() {
		return supervisor;
	}

	public void setSupervisor(boolean isSupervisor) {
		this.supervisor = isSupervisor;
	}

	public DrawView getSupervisorView() {
		return supervisorView;
	}

	public void setSupervisorView(DrawView supervisorView) {
		this.supervisorView = supervisorView;
	}

	public int getSessionId() {
		return user.getSessionId();
	}

	public void setSessionId(int sessionId) {
		this.user.setSessionId(sessionId);
	}

	public WrapperUser getUser() {
		return user;
	}

	public void setUser(WrapperUser user) {
		this.user = user;
	}

	public boolean isSupervisorSchedulerEnabled() {
		return supervisorSchedulerEnabled;
	}

	public void setSupervisorSchedulerEnabled(boolean supervisorSchedulerEnabled) {
		this.supervisorSchedulerEnabled = supervisorSchedulerEnabled;
	}

}
