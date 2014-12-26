package com.example.wmgps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

class RetrieveFeedTask extends AsyncTask<String, Void, String> {

    private Exception exception;

    protected String doInBackground(String... urls) 
    {
    	
		String result = "";
	    // Execute the request
	    HttpResponse response = null;
        try 
        {
    		String URL = //"http://ma-vm243.asia.manh.com:22800/scopeRF/wm/inventory/ui/RFLogin.jsflps"; // DevIntg
    				"http://blrlw3645.asia.manh.com:10000/scopeRF/wm/inventory/ui/RFLogin.jsflps"; // Ramesh
    				 // "http://blrlw3126.asia.manh.com:10000/scopeRF/wm/inventory/ui/RFLogin.jsflps"; // Kishore
    				// "http://ma-vm243.asia.manh.com:22001/scopeRF/wm/inventory/ui/RFLogin.jsflps";
    		//URL = "http://www.google.com";
        	HttpClient httpclient = new DefaultHttpClient();

		    // Prepare a request object
		    HttpPost httpget = new HttpPost(URL); 
		    Header header = new BasicHeader("Content-type", "application/x-www-form-urlencoded; charset=utf-8");
		    httpget.addHeader(header);
		    
		    httpget.addHeader("Authorization", "Basic "+Base64.encodeToString("rat#1:rat".getBytes(),Base64.NO_WRAP));
		    
		    Header header1 = new BasicHeader("User-Agent", "TextRenderer/1.0 (CLS: 5.0.8 ; OS:Windows)");
		    httpget.addHeader(header1);
		    
        	 response = httpclient.execute(httpget);
		        // Examine the response status
		        Log.i("Kishore ",response.getStatusLine().toString());

		        // Get hold of the response entity
		        HttpEntity entity = response.getEntity();
		        // If the response does not enclose an entity, there is no need
		        // to worry about connection release

		        if (entity != null) {

		            // A Simple JSON Response Read
		            InputStream instream = entity.getContent();
		             result= convertStreamToString(instream);
		            // now you have the string representation of the HTML request
		            instream.close();
		        }
            
        } catch (Exception e) {
        	Log.e("Exception ", e.getMessage());
	    	
	    	Log.e("Exception ", e.getLocalizedMessage());
	    	
	    	Log.e("Exception ", e.getStackTrace().toString());
        }
        return result;
    }

    protected void onPostExecute(HttpResponse feed) {
        // TODO: check this.exception 
        // TODO: do something with the feed
    }
    
    private static String convertStreamToString(InputStream is) {
	    /*
	     * To convert the InputStream to String we use the BufferedReader.readLine()
	     * method. We iterate until the BufferedReader return null which means
	     * there's no more data to read. Each line will appended to a StringBuilder
	     * and returned as String.
	     */
	    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	    StringBuilder sb = new StringBuilder();

	    String line = null;
	    try {
	        while ((line = reader.readLine()) != null) {
	            sb.append(line + "\n");
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            is.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    return sb.toString();
	}
}
