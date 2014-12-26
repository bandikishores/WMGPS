import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.testclient.Constants;
import com.example.testclient.WrapperLocn;
import com.example.testclient.Misc;
import com.example.testclient.WrapperObject;
import com.example.testclient.WrapperUser;

public class ServerSocketListener {
	
	public ServerSocketListener()
	{
		Location.fetchLocations();
	}
	
	private static ServerSocket serverSocket;
	private String message;
	public static List<String> listOfSessions = new ArrayList<String>();
	public static Map<String, String> mapOfMessages = new HashMap<String, String>();
	public static Integer currentSessionId = 0;
		  
    public static void main(String[] args) 
    {
        new ServerSocketListener().createConnection();
    }
    
    private void createConnection() 
    {
    	try 
        {
            serverSocket = new ServerSocket(Constants.PORT);  //Server socket
        } 
        catch (IOException e) 
        {
            System.out.println("Could not listen on port: 6543");
        }
  
        System.out.println("Server started. Listening to the port 6543");
        
        while (true) 
        {
            try 
            {
            	Socket clientSocket = serverSocket.accept();   //accept the client connection
            	ObjectInputStream readObjInputStream = new ObjectInputStream(clientSocket.getInputStream());
                
                String str = Misc.EMPTY_STRING;
                try 
                {
					str = (String) readObjInputStream.readObject();
				} 
                catch (ClassNotFoundException e) 
                {
					System.out.println(e);
					closeConnection(clientSocket);
					continue;
				}
                
                if(!Misc.isNullTrimmedString(str))
                {
                	String[] keys = str.split(":");
        			List outputList = new ArrayList();
        			
                	if(!Misc.isNullArray(keys))
                	{
                		if(Constants.VALUES_AVALIABLE.equals(keys[0]))
                		{
                			Object retreivedObj = null;
                            
                            try 
                            {
                         	   retreivedObj = readObjInputStream.readObject();
                            } 
                            catch (ClassNotFoundException e) 
                            {
                         	   e.printStackTrace();
                            }
                            
                            // Unwrap the Objects in List
                            if(retreivedObj != null)
                            {
                         	   if(retreivedObj instanceof List)
                         	   {
                        		   outputList = (List) retreivedObj;
                         		  /*List retreivedList = (List) outputList;
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
                		}

                        ObjectOutputStream writeObjOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
                        List inputToHost = processInputs(keys, outputList);
                        writeObjOutputStream.writeObject(inputToHost);
                  	    writeObjOutputStream.flush();
                        writeObjOutputStream.close();
                	}
                	else
                	{
                    	closeConnection(clientSocket);
                    	continue;
                	}
                }
                else
                {
                	closeConnection(clientSocket);
                	continue;
                }
                
                /*inputStreamReader = new InputStreamReader(clientSocket.getInputStream());
                bufferedReader = new BufferedReader(inputStreamReader); //get the client message
                message = bufferedReader.readLine();
                
                if(message != null && message.equals("getCount"))
                {
	                outputStreamReader = new OutputStreamWriter(clientSocket.getOutputStream());
	                bufferedWriter = new BufferedWriter(outputStreamReader);
	                bufferedWriter.write("Number Of Pings"+(++numberOfPings));
	                bufferedWriter.flush();
                }
                bufferedReader.close();
                if(outputStreamReader != null)
                	outputStreamReader.close();*/
                
                closeConnection(clientSocket);
            } 
            catch (IOException ex) 
            {
                System.out.println("Problem in message reading");
                System.out.println(ex);
                ex.printStackTrace();
            }
        }
	}

	private List processInputs(String[] keys, List outputList) 
	{
		List inputToHost = new ArrayList();
		for(int i = 1; i < keys.length; i++)
		{
			if(keys[i].equals(Constants.VALIDATE_USER))
			{
				int sessionId = new UserValidation().verifyUser(outputList);
				
				if(sessionId > 0)
				{
					String userType = new UserValidation().isSupervisor(outputList);
					if(Constants.WORKER.equals(userType))
						listOfSessions.add(sessionId+"");
					
					inputToHost.add(sessionId+"");
					inputToHost.add(userType);
				}
			}
			else if(keys[i].equals(Constants.FETCH_LOCATION_LIST))
			{
				inputToHost = Location.fetchLocations();
			}
			else if(keys[i].equals(Constants.FIND_SHORTEST_PATH))
			{
				WrapperUser user = (WrapperUser) outputList.get(0);
				Location loc = new Location();
				loc.findDestination(user);
				loc.findBestFitLocationAlg(user);
				inputToHost.add(user);
			}
			else if(keys[i].equals(Constants.GET_LIST_OF_SESSIONS))
			{
				inputToHost = listOfSessions;
			}
			else if(keys[i].equals(Constants.GET_USER_FROM_SESSION_ID))
			{
				String session = (String) outputList.get(0);
				Integer userId = Integer.parseInt(session);
				if(UserValidation.mapOfUsers.containsKey(userId))
					inputToHost.add(UserValidation.mapOfUsers.get(userId));
			}
			else if(keys[i].equals(Constants.UPDATE_USER_ON_SESSION))
			{
				WrapperUser wrapuser = (WrapperUser) outputList.get(0);
				UserValidation.mapOfUsers.put(wrapuser.getSessionId(), wrapuser);
			}
			else if(keys[i].equals(Constants.REMOVE_USER_BY_SESSION_ID))
			{
				String session = (String) outputList.get(0);
				Integer userId = Integer.parseInt(session);
				if(UserValidation.mapOfUsers.containsKey(userId))
				{
					UserValidation.mapOfUsers.remove(userId);
					listOfSessions.remove(userId+"");
					mapOfMessages.remove(userId+"");
				}
			}
			else if(keys[i].equals(Constants.SEND_MESSAGE_TO_USER))
			{
				String session = (String) outputList.get(0);
				String message = (String) outputList.get(1);
				
				String existingMessages = mapOfMessages.get(session);
				if(Misc.isNullTrimmedString(existingMessages))
					existingMessages = message;
				else
				{
					existingMessages = existingMessages + " \n " + message; 
				}
				
				mapOfMessages.put(session, existingMessages);
			}
			else if(keys[i].equals(Constants.GET_MESSAGES_FROM_SESSION_ID))
			{
				String session = (String) outputList.get(0);
				
				String existingMessages = mapOfMessages.remove(session);
				
				if(Misc.isNullTrimmedString(existingMessages))
					existingMessages = Misc.EMPTY_STRING;
				
				inputToHost.add(existingMessages);
			}
		}
		return inputToHost;
	}

	public List addValues(List inputToHost, Object... objects) 
	{
		if(objects != null && objects.length > 0)
		{
			for(Object obj : objects)
			{
				inputToHost.add(obj);
			}
		}
		return inputToHost;
	}

	private static void closeConnection(Socket clientSocket)
    {
    	try 
    	{
			clientSocket.close();
		} 
    	catch (IOException e) 
    	{
			System.out.println(e);
		}
    }

}
