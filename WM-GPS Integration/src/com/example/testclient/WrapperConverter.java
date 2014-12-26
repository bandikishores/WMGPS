package com.example.testclient;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.example.wmgps.Circle;
import com.example.wmgps.Locn;

public class WrapperConverter
{
	public static List<Locn> convertWrapperToLocn(List<WrapperLocn> wrappers)
	{
		List<Locn> locnList = new ArrayList<Locn>();
		
		if(!Misc.isNullList(wrappers))
		{
			try
			{
				for(int i = 0; i < wrappers.size(); i++)
				{
					WrapperLocn locnWrap = wrappers.get(i);
					Locn locn = new Locn(locnWrap.getLeft(), locnWrap.getTop(), locnWrap.getRight(), locnWrap.getBottom(), locnWrap.isDisabled());
					locnList.add(locn);
				}
			}
			catch(Exception e)
			{
				Log.e("", "Exception ", e);
			}
		}
		
		convertWrapperLinks(locnList, wrappers);
		
		return locnList;
	}

	private static void convertWrapperLinks(List<Locn> locnList, List<WrapperLocn> wrapperLinks) 
	{
		if(!Misc.isNullList(wrapperLinks))
		{
			for(WrapperLocn locnWrap : wrapperLinks)
			{
				int parentLocnNumber = locnWrap.getLocnNumber();
				Locn parentLocn = locnList.get(parentLocnNumber);
				for(int i = 0; i < locnWrap.getLinksToLocations().size(); i++)
				{
					int childLocnNumber = locnWrap.getLinksToLocations().get(i).getLocnNumber();
					parentLocn.getLinksToLocations().add(locnList.get(childLocnNumber));
					/*int indexLink = locnWrap.getLinksToLocations().get(i).getLocnNumber();
					locnList.get(i).getLinksToLocations().add(locnList.get(indexLink));*/
				}
			}
		}
	}

	public static void convertWrapperToUser(Circle user, List outputList, List<Locn> locations) 
	{
		if(!Misc.isNullList(outputList))
		{
			synchronized (user) 
			{
				WrapperUser userWrap = (WrapperUser) outputList.get(0);
				user.setUser(userWrap);
				List<WrapperLocn> wrapperLocns = userWrap.getShortestPath();
				if(!Misc.isNullList(wrapperLocns))
				{
					for(WrapperLocn wraplocn : wrapperLocns)
					{
						user.getShortestPath().add(locations.get(wraplocn.getLocnNumber()));
					}
				}	
			}
		}
	}

}
