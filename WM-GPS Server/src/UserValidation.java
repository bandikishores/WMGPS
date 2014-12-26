import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.testclient.Constants;
import com.example.testclient.Misc;
import com.example.testclient.WrapperUser;


public class UserValidation 
{
	public static Map<Integer, WrapperUser> mapOfUsers = new HashMap<Integer, WrapperUser>();

	public int verifyUser(List outputList) 
	{
		String username = (String) outputList.get(0);
		String password = (String) outputList.get(1);
		
		if((username.equals("a") && password.equals("a"))
				|| (username.equals("b") && password.equals("b")))
		{
			synchronized (ServerSocketListener.currentSessionId) 
			{
				++ServerSocketListener.currentSessionId;
			}
			
			return ServerSocketListener.currentSessionId;
		}
		
		return 0;
	}

	public String isSupervisor(List outputList)
	{
		String username = (String) outputList.get(0);
		if(username.equals("a"))
		{
			WrapperUser user = new WrapperUser();
			user.setSessionId(ServerSocketListener.currentSessionId);
			mapOfUsers.put(ServerSocketListener.currentSessionId, user);
			return Constants.WORKER;
		}
		else
			return Constants.SUPERVISOR;
	}

}
