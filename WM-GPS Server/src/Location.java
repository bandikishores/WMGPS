import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.example.testclient.FloatHolder;
import com.example.testclient.Misc;
import com.example.testclient.WrapperLocn;
import com.example.testclient.WrapperUser;


public class Location 
{
	public static List<WrapperLocn> locations = new ArrayList<WrapperLocn>();
	
	public static List<WrapperLocn> fetchLocations() 
	{
		if(Misc.isNullList(locations))
		{
			createLocations();
			createLinks();
		}
		return locations;
	}
	
	public static void createLocations()
	{
    	locations.add(new WrapperLocn(0, 0, 0, 0, 0, true)); // 0
    	locations.add(new WrapperLocn(1, 30, 30, 80, 80)); // 1
    	locations.add(new WrapperLocn(2, 550, 30, 600, 80)); // 2
    	locations.add(new WrapperLocn(3, 250, 500, 300, 550, true)); // 3
    	locations.add(new WrapperLocn(4, 550, 600, 600, 650)); // 4
    	locations.add(new WrapperLocn(5, 250, 750, 300, 800)); // 5
    	locations.add(new WrapperLocn(6, 30, 900, 80, 950)); // 6
    	locations.add(new WrapperLocn(7, 550, 900, 600, 950)); // 7
	}
	
	private static void createLinks() 
	{
		locations.get(1).getLinksToLocations().add(locations.get(2));
		locations.get(1).getLinksToLocations().add(locations.get(3));
		locations.get(1).getLinksToLocations().add(locations.get(6));
		locations.get(2).getLinksToLocations().add(locations.get(1));
		locations.get(2).getLinksToLocations().add(locations.get(5));
		locations.get(3).getLinksToLocations().add(locations.get(1));
		locations.get(3).getLinksToLocations().add(locations.get(6));
		locations.get(3).getLinksToLocations().add(locations.get(5));
		locations.get(4).getLinksToLocations().add(locations.get(7));
		locations.get(5).getLinksToLocations().add(locations.get(3));
		locations.get(5).getLinksToLocations().add(locations.get(2));
		locations.get(5).getLinksToLocations().add(locations.get(7));
		locations.get(6).getLinksToLocations().add(locations.get(1));
		locations.get(6).getLinksToLocations().add(locations.get(3));
		locations.get(6).getLinksToLocations().add(locations.get(7));
		locations.get(7).getLinksToLocations().add(locations.get(4));
		locations.get(7).getLinksToLocations().add(locations.get(5));
		locations.get(7).getLinksToLocations().add(locations.get(6));
	}

	public void findDestination(WrapperUser user) 
	{
		int destLocn = (int) (new Random().nextInt() % locations.size());
		destLocn = (destLocn < 0)?-destLocn:destLocn;
		// destLocn = 2; // Kish - Todo : remove only for debugging
		
		float destX = locations.get(destLocn).getCenterX();
		float destY = locations.get(destLocn).getCenterY();
		
		while(!user.isActive()
				&& (destX == user.getCenterX() && destY == user.getCenterY() || locations.get(destLocn).isDisabled()))
		{
			destLocn = (int) (new Random().nextInt() % locations.size());
			destLocn = (destLocn < 0)?-destLocn:destLocn;
			
			destX = locations.get(destLocn).getCenterX();
			destY = locations.get(destLocn).getCenterY();
		}
		
		if(!user.isActive()) // if(scheduler == null)
		{
			user.setDestX(destX);
			user.setDestY(destY);
		}
	}

	public void findBestFitLocationAlg(WrapperUser user) 
	{
		List<WrapperLocn> visitedLocns = new ArrayList<WrapperLocn>();
		List<WrapperLocn> rejectedLocns = new ArrayList<WrapperLocn>();
		List<WrapperLocn> minShortestLocns = new ArrayList<WrapperLocn>();
		WrapperLocn currentLocn = null;
		WrapperLocn destLocn = null;
		
		FloatHolder minShortestDist = new FloatHolder(9999);
		
		for(int i = 1; i < locations.size(); i++)
        {
			if(currentLocn == null)
				currentLocn = locations.get(i).findLocationForCoOrdinates(user.getCenterX(), user.getCenterY());
			if(destLocn == null)
				destLocn = locations.get(i).findLocationForCoOrdinates(user.getDestX(), user.getDestY());
        }
		if(currentLocn == null || destLocn == null)
			return;
		
		// minShortestLocns.add(currentLocn);
		visitedLocns.add(currentLocn);
		TraverseAllPaths(currentLocn, destLocn, visitedLocns, rejectedLocns, minShortestLocns, minShortestDist);
		user.setShortestPath(minShortestLocns);
	}

	private boolean TraverseAllPaths(WrapperLocn currentLocn, WrapperLocn destLocn, List<WrapperLocn> visitedLocns, List<WrapperLocn> rejectedLocns, List<WrapperLocn> minShortestLocns, FloatHolder minShortestDist) 
	{
		if(currentLocn == destLocn) 
			return true;
		
		for(int i = 0; i < currentLocn.getLinksToLocations().size(); i++)
		// while((childLocn = FetchOptimalChild(currentLocn, destLocn, visitedLocns, rejectedLocns)) != null)
		{
			WrapperLocn childLocn = currentLocn.getLinksToLocations().get(i);
			if(!visitedLocns.contains(childLocn) && !rejectedLocns.contains(childLocn) && !childLocn.isDisabled())
			{
				visitedLocns.add(childLocn);
				if(TraverseAllPaths(childLocn, destLocn, visitedLocns, rejectedLocns, minShortestLocns, minShortestDist))
				{
					// try calculating this distance for each recursive logic instead of finding at the end. 
					// but again performance hit would depend on the number of hits of successful finding path.  
					FloatHolder shortestDist = new FloatHolder(findShortestDist(visitedLocns));
					if(minShortestDist.getValue() > shortestDist.getValue())
					{
						minShortestDist.setValue(shortestDist.getValue());
						minShortestLocns.clear();
						minShortestLocns.addAll(visitedLocns);
					}
					visitedLocns.remove(childLocn);
				}
				else
				{
					visitedLocns.remove(childLocn);
					// need this because i don't end up searching already visited paths.
					// But again, can't use VisitedLocn's because that will have my entire Path captured.
					// rejectedLocns.add(childLocn); 
				}
			}
		}
		return false;
	}
		
	private float findShortestDist(List<WrapperLocn> visitedLocns) 
	{
		float minDist = 0;
		for(int i = 0; i < visitedLocns.size() - 1; i++)
		{
			float xCenter = visitedLocns.get(i).getCenterX();
			float yCenter = visitedLocns.get(i).getCenterY();
			
			float xCenter1 = visitedLocns.get(i+1).getCenterX();
			float yCenter1 = visitedLocns.get(i+1).getCenterY();
			
			minDist += getDistanceBetweenPoints(xCenter, yCenter, 
					xCenter1, yCenter1);
		}
		return minDist;
	}
	
	private float getDistanceBetweenPoints(float x1, float y1, float x2, float y2)
	{
		return (float) Math.sqrt(Math.pow((x2-x1), 2) + Math.pow((y2-y1), 2));
	}
}
