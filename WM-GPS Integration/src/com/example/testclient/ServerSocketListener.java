package com.example.testclient;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.os.AsyncTask;
import android.util.Log;

import com.example.testclient.WrapperObject;

import com.example.testclient.Constants;
import com.example.testclient.Misc;

public class ServerSocketListener extends AsyncTask<String, Void, String> 
{
    private List values = new ArrayList();
    private StringBuilder finalkey = new StringBuilder();
	private Socket socket = null;
	private List outputList;
	private boolean responseNeeded = false;
    

    public ServerSocketListener() 
    {
		super();
	}

	protected String doInBackground(String... urls) 
    {    	
		String line = "";
	   try 
	   {
		   if(socket == null)
			   socket = new Socket(Constants.ADDRESS, Constants.PORT);
		   
		   ObjectOutputStream writeObjOutputStream = new ObjectOutputStream(socket.getOutputStream());
		   ObjectInputStream readObjInputStream = null;
		   
		   if(!Misc.isNullTrimmedString(finalkey.toString()))
		   {
               writeObjOutputStream.writeObject(finalkey.toString());
               writeObjOutputStream.flush();
		   }
		   if(!Misc.isNullList(values))
		   {
               writeObjOutputStream.writeObject(values);
               writeObjOutputStream.flush();
		   }
		   if(responseNeeded)
		   {
               Object retreivedObj = null;
               
               try 
               {
        		   readObjInputStream = new ObjectInputStream(socket.getInputStream());
            	   retreivedObj = readObjInputStream.readObject();
               } 
               catch (ClassNotFoundException e) 
               {
            	   e.printStackTrace();
               }
               // Unwrap the Objects in List
               if(retreivedObj != null)
               {
            	   if(retreivedObj instanceof  List)
            	   {
            		   outputList = (List) retreivedObj;
	            	  /* List retreivedList = (List) retreivedObj;
	            	   if(Misc.isNullList(retreivedList))
	            	   {
	            		   for(Object obj : retreivedList)
	            		   {
	            			   if(obj instanceof WrapperObject)
	            			   {
	            				   WrapperObject wrap = (WrapperObject) obj;
	            				   outputList.add(wrap.getObject());
	            			   }
	            		   }
	            	   }*/
            	   }
            	   else if(retreivedObj instanceof String)
            	   {
            		   // Kish - TODO : Do I need this?
            	   }
               }
               
               writeObjOutputStream.close();
               
               if(readObjInputStream != null)
            	   readObjInputStream.close();
		   }
	   } 
	   catch (UnknownHostException e) 
	   {
			Log.e("", "Exception e", e);
			e.printStackTrace();
		} 
	   catch (IOException e) 
	   {
			Log.e("", "Exception e", e);
			e.printStackTrace();
		} 
	   finally
	   {
		   closeConnection();
	   }
	   return line;
    }
	
	public void closeConnection()
	{
		if (socket != null) 
		{
			try 
			{
				socket.close();
			} 
			catch (IOException e) 
			{
				Log.e("", "Exception e", e);
				e.printStackTrace();
			}
			finally
			{
				socket = null;
			}
		}
	}

	public List getValues() {
		return values;
	}
	
	public void addValue(Object obj)
	{
		this.values.add(new WrapperObject(obj));
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public String getKey() {
		return finalkey.toString();
	}

	public void addKey(String key) {
		this.finalkey.append(key);
	}

	public void addValues(Object... objects) 
	{
		if(objects != null && objects.length > 0)
		{
			for(Object obj : objects)
			{
				this.values.add(obj);
			}
		}
	}

	public void addKeys(String... keys) 
	{
		boolean firstTime = Misc.isNullTrimmedString(finalkey.toString())?true:false; 
		
		if(keys != null && keys.length > 0)
		{
			for(String key : keys)
			{
				if(!firstTime)
				{
					finalkey.append(Constants.COLON);
				}
				
				finalkey.append(key);
				firstTime = false;
			}
		}
	}

	public void invoke() 
	{
		try 
		{
			this.execute(new String[]{}).get();
		} 
		catch (InterruptedException e) 
		{
			Log.e("", "Exception Occured while comunicating with server", e);
		} 
		catch (ExecutionException e) 
		{
			Log.e("", "Exception Occured while comunicating with server", e);
		}
	}

	public List getOutputList() 
	{
		return outputList;
	}

	public boolean isResponseNeeded() {
		return responseNeeded;
	}

	public void setResponseNeeded(boolean responseNeeded) {
		this.responseNeeded = responseNeeded;
	}
}
