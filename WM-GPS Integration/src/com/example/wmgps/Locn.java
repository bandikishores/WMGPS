package com.example.wmgps;

import java.io.Serializable;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import com.example.testclient.FloatHolder;

import android.graphics.RectF;

public class Locn extends RectF implements Serializable
{
	public boolean disabled = false;
	public List<Locn> linksToLocations = new LinkedList<Locn>();
	
	public Locn()
	{
		super();
	}
	
	public Locn(float left, float top, float right, float bottom)
	{
		super(left, top, right, bottom);
	}
	
	public Locn(float left, float top, float right, float bottom, boolean disabled)
	{
		super(left, top, right, bottom);
		this.disabled = disabled;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean isDisabled) {
		this.disabled = isDisabled;
	}

	public List<Locn> getLinksToLocations() {
		return linksToLocations;
	}

	public void setLinksToLocations(List<Locn> linksToLocations) {
		this.linksToLocations = linksToLocations;
	}

	@Override
	public boolean equals(Object obj)
	{
		Locn tempLocn = (Locn) obj;
		if(this.top == tempLocn.top && this.bottom == tempLocn.bottom &&
				this.left == tempLocn.left && this.right == tempLocn.right)
			return true;
		else 
			return false;
	}

	public Locn findLocationForCoOrdinates(FloatHolder tempLocnX,
			FloatHolder tempLocnY) 
	{
		if(this.centerX() == tempLocnX.getValue() && this.centerY() == tempLocnY.getValue())
			return this;
		else
			return null;
	}

	public Locn findLocationForCoOrdinates(float destX, float destY) 
	{
		if(this.centerX() == destX && this.centerY() == destY)
			return this;
		else
			return null;
	}
	
}
