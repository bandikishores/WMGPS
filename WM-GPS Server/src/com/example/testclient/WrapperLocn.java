package com.example.testclient;

import java.io.Serializable;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class WrapperLocn implements Serializable
{
	public boolean disabled = false;
	int locnNumber = 0;
	public List<WrapperLocn> linksToLocations = new LinkedList<WrapperLocn>();
	float left =0, top =0, right =  0, bottom = 0, centerX = 0, centerY = 0;
	
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

	public WrapperLocn()
	{
		super();
	}
	
	public float getLeft() {
		return left;
	}

	public void setLeft(float left) {
		this.left = left;
	}

	public float getTop() {
		return top;
	}

	public void setTop(float top) {
		this.top = top;
	}

	public float getRight() {
		return right;
	}

	public void setRight(float right) {
		this.right = right;
	}

	public float getBottom() {
		return bottom;
	}

	public void setBottom(float bottom) {
		this.bottom = bottom;
	}

	public WrapperLocn(int locnNumber, float left, float top,
			float right, float bottom, boolean disabled) {
		super();
		this.disabled = disabled;
		this.locnNumber = locnNumber;
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
		setCenterY((top + bottom)/2);
		setCenterX((left + right)/2);
	}

	public WrapperLocn(int locnNumber, float left, float top,
			float right, float bottom) {
		super();
		this.locnNumber = locnNumber;
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
		setCenterY((top + bottom)/2);
		setCenterX((left + right)/2);
	}

	public WrapperLocn(float left, float top, float right, float bottom) 
	{
		super();
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
		setCenterX((top + bottom)/2);
		setCenterY((left + right)/2);
	}

	public WrapperLocn(float left, float top, float right, float bottom, boolean disabled)
	{
		this.disabled = disabled;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean isDisabled) {
		this.disabled = isDisabled;
	}

	public List<WrapperLocn> getLinksToLocations() {
		return linksToLocations;
	}

	public void setLinksToLocations(List<WrapperLocn> linksToLocations) {
		this.linksToLocations = linksToLocations;
	}
	
	@Override
	public String toString()
	{
		return ""+left+""+top+""+right+""+bottom;
	}

	public int getLocnNumber() {
		return locnNumber;
	}

	public void setLocnNumber(int locnNumber) {
		this.locnNumber = locnNumber;
	}

	public WrapperLocn findLocationForCoOrdinates(FloatHolder tempLocnX,
			FloatHolder tempLocnY) 
	{
		if(this.getCenterX() == tempLocnX.getValue() && this.getCenterY() == tempLocnY.getValue())
			return this;
		else
			return null;
	}

	public WrapperLocn findLocationForCoOrdinates(float destX, float destY) 
	{
		if(this.getCenterX() == destX && this.getCenterY() == destY)
			return this;
		else
			return null;
	}
	
}
