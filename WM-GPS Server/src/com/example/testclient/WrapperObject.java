package com.example.testclient;

import java.io.Serializable;

public class WrapperObject implements Serializable
{
	private Object object = null;

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public WrapperObject() {
		super();
	}

	public WrapperObject(Object object) {
		super();
		this.object = object;
	}

}
