package com.example.wmgps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.testclient.Constants;
import com.example.testclient.Misc;
import com.example.testclient.ServerSocketListener;
import com.example.wmgps.R;
import com.example.wmgps.R.id;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		((TextView) findViewById(R.id.Hidden1)).setVisibility(View.GONE);
		((TextView) findViewById(R.id.Hidden2)).setVisibility(View.GONE);
		((TextView) findViewById(R.id.Hidden3)).setVisibility(View.GONE);
		((TextView) findViewById(R.id.Hidden4)).setVisibility(View.GONE);
		((TextView) findViewById(R.id.Hidden5)).setVisibility(View.GONE);
		((TextView) findViewById(R.id.Hidden6)).setVisibility(View.GONE);
		((TextView) findViewById(R.id.Hidden7)).setVisibility(View.GONE);
		((TextView) findViewById(R.id.Hidden8)).setVisibility(View.GONE);
		((TextView) findViewById(R.id.Hidden9)).setVisibility(View.GONE);
		((TextView) findViewById(R.id.Hidden10)).setVisibility(View.GONE);
		((TextView) findViewById(R.id.Hidden11)).setVisibility(View.GONE);
		((TextView) findViewById(R.id.Hidden11)).setText(Misc.EMPTY_STRING);
		((TextView) findViewById(R.id.ErrorText)).setVisibility(View.GONE);
		((TextView) findViewById(R.id.ErrorText)).setTextColor(Color.RED);
		((TextView) findViewById(R.id.ErrorText)).setText("Unknown Error Occured");
		((EditText) findViewById(R.id.edit_message)).setVisibility(View.GONE);
		((EditText) findViewById(R.id.edit_message1)).setVisibility(View.GONE);
		((TextView) findViewById(R.id.userNameText)).setVisibility(View.GONE);
		((EditText) findViewById(R.id.userNameEdit)).setVisibility(View.GONE);
		((TextView) findViewById(R.id.passwordText)).setVisibility(View.GONE);
		((EditText) findViewById(R.id.passwordEdit)).setVisibility(View.GONE);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
	}
	
	/** Called when the user clicks the Send button */
	public void sendMessage(View view) 
	{
		/*Intent intent = new Intent(this, DisplayMessageActivity.class);
		EditText editText = (EditText) findViewById(R.id.edit_message);
		String message = editText.getText().toString();
		intent.putExtra(EXTRA_MESSAGE, message);
		startActivity(intent);*/
		
		/*EditText editText = (EditText) findViewById(R.id.edit_message);
		String message = editText.getText().toString();
		
		TextView output = (TextView) findViewById(R.id.welcome);
		output.setText(message);*/
			
			/*Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL));
			startActivity(browserIntent);
			
			*/
		TextView hiddenText = (TextView) findViewById(R.id.Hidden11);
		if(Misc.isNullTrimmedString(hiddenText.getText().toString()))
		{
			TextView output = (TextView) findViewById(R.id.welcome1);
			boolean without_connection = true;
			Map<String, ArrayList<String>> typeAndValueOfTag = new LinkedHashMap<String, ArrayList<String>>(); // Insertion order must be followed
			
			String response = Misc.EMPTY_STRING;
			String welcome1String = "*******************Connected*************************";
			    try 
			    {
			    	
			    	response = new RetrieveFeedTask().execute(new String[]{}).get();
	
			    } catch (Exception e) {
			    	response = Misc.EMPTY_STRING;
			    	Log.e("Exception ", e.getMessage());
			    	
			    	Log.e("Exception ", e.getLocalizedMessage());
			    	
			    	Log.e("Exception ", e.getStackTrace().toString());
			    	
			    }
			    
			    
			if(Misc.isNullTrimmedString(response))
			{
				// welcome1String = "***Loading Static, couldn't connect***";
				
				response = "{\"window\":{\"elements\":[{\"type\":\"hidden\",\"name\":\"sourceAction\"},{\"type\":\"label\",\"value\":\"Warehouse Management\"},"+
			    "{\"type\":\"break\"},{\"type\":\"label\",\"value\":\"--------- ----------\"},{\"type\":\"break\"}, {\"type\":\"label\",\"value\":\" User ID: \"},"+
			    "{\"type\":\"entry\",\"name\":\"j_username\",\"value\":\"\",\"maxLength\":15,\"dispLength\":10,"+
			    "\"setHidden\":[{\"hiddenName\":\"sourceAction\",\"hiddenValue\":\"username\"}],\"focus\":true},"+
			    "{\"type\":\"break\"},{\"type\":\"label\",\"value\":\"Password: \"},"+
			    "{\"type\":\"entry\",\"name\":\"j_password\",\"value\":\"\",\"hideInput\":true,\"maxLength\":14,\"dispLength\":10,\"focus\":false},"+
			    "{\"type\":\"keybinding\",\"value\":\"CTRL-X\",\"URL\":\"\",\"keyDescription\":\"CTRL-X Exit\"},"+
			    "{\"type\":\"keybinding\",\"value\":\"CTRL-L\",\"URL\":\"/scopeRF/RFLogin/RFLegal.jsp\",\"keyDescription\":\"CTRL-L License Agreement\"}]"+
			    		",\"submit\":\"/scopeRF/RFLogin/ProcessLogin.jsp\"}}";
			}
			    
			StringBuffer finalEncodedString = new StringBuffer(Misc.EMPTY_STRING);
	
			int currentOutputTag = R.id.Hidden1;
			int currentInputTag = R.id.edit_message;
			
			    // Renderer::decodeConfig()
			    if(!Misc.isNullTrimmedString(response))
			    {
				    try 
				    {
						JSONObject jsonObj = new JSONObject(response);
						
						for (Iterator iterator = jsonObj.keys(); iterator.hasNext();) 
						{
							String name = (String) iterator.next();
							
							JSONObject jsonObj1 = jsonObj.getJSONObject(name);
							
							for (Iterator iterator1 = jsonObj1.keys(); iterator1.hasNext();) 
							{
								String name1 = (String) iterator1.next();
								
								if("elements".equals(name1))
								{
									JSONArray jsonArr = jsonObj1.getJSONArray(name1);
									boolean userName = false;
									boolean password = false;
									for (int i = 0; i < jsonArr.length(); i++) 
									{
				                        JSONObject temp = jsonArr.getJSONObject(i);
				                        String initialValue = null;
				                        String initialName = null;
				                        String initialLabel = null;
				                        
				                        if (!temp.isNull("name") && temp.getString("name").equals("j_password")) 
		                    			{
		                    				initialName = "j_password";
		                    			}
				                        else if (!temp.isNull("name") && temp.getString("name").equals("j_username")) 
		                    			{
		                    				initialName = "j_username";
		                    			}
				                        
				                        for (Iterator iterator2 = temp.keys(); iterator2.hasNext();) 
										{
					                        String tempName = (String)iterator2.next();
					                        String tempValue = (String)temp.getString(tempName);
					                        
					                        if(tempName.equals("type"))
					                        {
					                        	if(tempValue.equals("label") || tempValue.equals("entry"))
					                        	{
					                        		initialLabel = tempValue;
					                        	}
					                        }
					                        else if(tempName.equals("value"))
					                        {
					                        	initialValue = tempValue;
					                        }
					                        
					                       /* if(tempName.equals("label"))
					                        {
					                        	if(Misc.isNullTrimmedString(initialValue))
					                        		initialLabel = tempName;
					                        	else
					                        	{
					                        		typeAndValueOfTag.put("output",initialValue);
					                        		initialValue = Misc.EMPTY_STRING;
					                        		continue;
					                        	}
					                        }
					                        else if(tempName.equals("value"))
					                        {
					                        	if(Misc.isNullTrimmedString(initialName) && Misc.isNullTrimmedString(initialLabel))
					                        	{
					                        		initialValue = tempValue;
					                        	}
					                        	else if(!Misc.isNullTrimmedString(initialLabel) && initialLabel.equals("label"))
					                        	{
					                        		typeAndValueOfTag.put("output",tempValue);
					                        		initialValue = Misc.EMPTY_STRING;
					                        		continue;
					                        	}
					                        	else if(!Misc.isNullTrimmedString(initialLabel) && initialLabel.equals("entry"))
					                        	{
					                        		if(!Misc.isNullTrimmedString(initialName) && initialName.equals("name"))
					                        			typeAndValueOfTag.put("input",tempValue);
					                        		else
					                        			initialValue = tempValue;
					                        	}
					                        }
					                        else if(tempName.equals("entry"))
					                        {
					                        	if(Misc.isNullTrimmedString(initialValue) || Misc.isNullTrimmedString(initialLabel))
					                        	{
					                        		initialName = tempName;
					                        	}
					                        	else
					                        		typeAndValueOfTag.put("input",initialValue);
					                        	
					                        	finalEncodedString.append("  " + tempName + "  " + tempValue + " ----- ");
					                        }*/
										}
				                        
				                        if(initialLabel != null)
				                        {
			                    			if(initialLabel.equals("label"))
			                    			{
			                    				if(initialValue.equals(" User ID: "))
			                    				{
			                    					TextView output1 = (TextView) findViewById(R.id.userNameText);
				                    				output1.setText(initialValue);
				                    				output1.setVisibility(View.VISIBLE);
				                    				userName = true;
				                    				password = false;
			                    				}
			                    				else if(initialValue.equals("Password: "))
			                    				{
			                    					TextView output1 = (TextView) findViewById(R.id.passwordText);
				                    				output1.setText(initialValue);
				                    				output1.setVisibility(View.VISIBLE);
				                    				password = true;
				                    				userName = false;
			                    				}
			                    				else
			                    				{
				                    				TextView output1 = (TextView) findViewById(currentOutputTag++);
				                    				output1.setText(initialValue);
				                    				output1.setVisibility(View.VISIBLE);
				                    				userName = false;
				                    				password = false;
			                    				}
			                    			}
			                    			else if(initialLabel.equals("entry"))
			                    			{
			                    				if("j_password".equals(initialName))
			                    				{
				                    				EditText input = ((EditText) findViewById(R.id.passwordEdit));
				                    				input.setVisibility(View.VISIBLE);
				                    				if(Misc.isNullTrimmedString(initialValue))
				                    				{
				                    					input.setText("");
				                    					input.setHint("Please enter password");
				                    				}
			                    				}
			                    				else if("j_username".equals(initialName))
			                    				{
				                    				EditText input = ((EditText) findViewById(R.id.userNameEdit));
				                    				input.setVisibility(View.VISIBLE);
				                    				if(Misc.isNullTrimmedString(initialValue))
				                    				{
				                    					input.setText("");
				                    					input.setHint("Please enter username");
				                    				}
			                    				}
			                    				else
			                    				{
			                    					EditText input = ((EditText) findViewById(currentInputTag++));
				                    				input.setVisibility(View.VISIBLE);
				                    				if(Misc.isNullTrimmedString(initialValue))
				                    				{
				                    					input.setText("");
				                    					input.setHint("Please enter value");
				                    				}
				                    				else
				                    					input.setText(initialValue);
			                    				}
			                    			}
				                        }
				                        /*if(initialLabel != null)
				                        {
				                        	List<String> listOfValues;
				                        	if(typeAndValueOfTag.containsKey(initialLabel))
				                        	{
				                        		listOfValues = typeAndValueOfTag.get(initialLabel);
				                        		listOfValues.add(initialValue);
				                        	}
				                        	typeAndValueOfTag.put(initialLabel,initialValue);
				                        }*/
									}
								}
								else
								{
									// Here, just the URL would get processed. So don't use.
									// output.setText("  " + name + "  " + jsonObj1.getString(name1));
								}
							}
						} 
						
						TextView welcome1 = (TextView) findViewById(R.id.welcome1);
						welcome1.setText(welcome1String);
						
						// processUIDisplay(typeAndValueOfTag);
	                    
						/*if(finalEncodedString.length() == 0)
							finalEncodedString.append("Could not process");
						
						output.setText(finalEncodedString.toString());
						
						TextView output2 = (TextView) findViewById(R.id.Hidden1);
						output2.setText("Hidden one is enabled");
						output2.setVisibility(View.VISIBLE);*/
						
					} catch (JSONException e) {
				    	Log.e("Exception ", e.getMessage());
				    	
				    	Log.e("Exception ", e.getLocalizedMessage());
				    	
				    	Log.e("Exception ", e.getStackTrace().toString());
					}
			    }
			    
			    hiddenText.setText("validated user");
	
	
	
	/* {"window":{"elements":[{"type":"hidden","name":"sourceAction"},{"type":"label","value":"Warehouse Management"},
			    {"type":"break"},{"type":"label","value":"--------- ----------"},{"type":"break"}, {"type":"label","value":" User ID: "},
			    {"type":"entry","name":"j_username","value":"","maxLength":15,"dispLength":10,
			    "setHidden":[{"hiddenName":"sourceAction","hiddenValue":"username"}],"focus":true},
			    {"type":"break"},{"type":"label","value":"Password: "},
			    {"type":"entry","name":"j_password","value":"","hideInput":true,"maxLength":14,"dispLength":10,"focus":false},
			    {"type":"keybinding","value":"CTRL-X","URL":"","keyDescription":"CTRL-X Exit"},
			    {"type":"keybinding","value":"CTRL-L","URL":"/scopeRF/RFLogin/RFLegal.jsp","keyDescription":"CTRL-L License Agreement"}]
			    		,"submit":"/scopeRF/RFLogin/ProcessLogin.jsp"}}*/
		}
		else
		{
			List outputList = verifyUser();

			if(!Misc.isNullList(outputList))
			{
				String sessionId = (String) outputList.get(0);
				String userType = (String) outputList.get(1);
				
				Intent intent = new Intent(this, MenuActivity.class);
				if(!Misc.isNullTrimmedString(sessionId) && !Misc.isNullTrimmedString(userType) && Integer.parseInt(sessionId) > 0)
				{
					if(userType.equals(Constants.WORKER))
					{
						MenuActivity.sessionId = Misc.EMPTY_STRING;
						TextView error = (TextView) findViewById(R.id.ErrorText);
						error.setText(Misc.EMPTY_STRING);
						error.setVisibility(View.GONE);
					    intent.putExtra(Constants.USER_TYPE, Constants.WORKER);
					    intent.putExtra(Constants.SESSION_ID, sessionId);
					}
					else
					{
						TextView error = (TextView) findViewById(R.id.ErrorText);
						error.setText(Misc.EMPTY_STRING);
						error.setVisibility(View.GONE);
					    intent.putExtra(Constants.USER_TYPE, Constants.SUPERVISOR);
					    intent.putExtra(Constants.SESSION_ID, sessionId);
					}
				}
				// goto Maps screen
			    // Get the message from the intent
			    startActivity(intent);
			}
			else
			{
				
				TextView error = (TextView) findViewById(R.id.ErrorText);
				error.setText("Invalid Username/Password");
				error.setVisibility(View.VISIBLE);
			}
		}
		return;

	}
	
	private List verifyUser() 
	{
		String userName = ((EditText) findViewById(R.id.userNameEdit)).getText().toString();
		String password = ((EditText) findViewById(R.id.passwordEdit)).getText().toString();
		
		ServerSocketListener serverSocketListener = new ServerSocketListener();
		serverSocketListener.addKeys(Constants.VALUES_AVALIABLE, Constants.VALIDATE_USER);
		serverSocketListener.addValues(userName, password);
		serverSocketListener.setResponseNeeded(true);
		serverSocketListener.invoke();
		List outputList = serverSocketListener.getOutputList();
		return outputList;
	}

	private void processUIDisplay(Map<String, String> typeAndValueOfTag) 
	{
		int currentOutputTag = R.id.Hidden1;
		int currentInputTag = R.id.edit_message;
		
		for (Iterator<String> iterator = (Iterator<String>) typeAndValueOfTag.keySet(); iterator.hasNext();) 
		{
			String inputType = iterator.next();
			String inputValue = typeAndValueOfTag.get(inputType);
			
			if(inputType.equals("output"))
			{
				TextView output = (TextView) findViewById(currentOutputTag++);
				output.setText(inputValue);
				output.setVisibility(View.VISIBLE);
			}
			else if(inputType.equals("input"))
			{
				EditText input = ((EditText) findViewById(currentInputTag++));
				input.setVisibility(View.VISIBLE);
				input.setFocusable(true);
				if(Misc.isNullTrimmedString(inputValue))
					input.setHint("Please enter value");
				else
					input.setText(inputValue);
			}
		}
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
	

	
		/*DefaultHttpClient httpclient = new DefaultHttpClient();
	    HttpGet httpget = new HttpGet(yourURL);
	    HttpResponse response = httpclient.execute(httpget);
	    in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
	    StringBuffer sb = new StringBuffer("");
	    String line = "";
	    String NL = System.getProperty("line.separator");
	    while ((line = in.readLine()) != null) {                    
	        sb.append(line + NL);
	    }
	    in.close();
	    String result = sb.toString();
	    Log.v("My Response :: ", result);*/
	
	/*TextView output = (TextView) findViewById(R.id.welcome);
	try {
	    output.append("starting\n");
	    RestClient client = new  RestClient("http://10.0.0.188:8084/Servlet_1/servlet1");
	
	    try {
	        client.Execute(RequestMethod.GET);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	
	    output.append("after execute\n");
	
	    String response = client.getResponse();
	    output.append("class - " + response  +  "\n" );
	    output.append(response);
	    output.append("done\n");
	}
	catch (Exception ex) {
	    output.append("error: " + ex.getMessage() + "\n" + ex.toString() +  "\n");
	}*/
}
