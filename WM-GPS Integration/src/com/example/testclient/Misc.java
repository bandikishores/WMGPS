package com.example.testclient;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class Misc {
	public static final String EMPTY_STRING = "";
	
	public static boolean isNullTrimmedString(String s)
	{
		return ((s == null) || (s.trim().length() == 0) || ("null".equals(s)));
	}
	 public static boolean isNullArray(Object [] arr) {
         if (arr == null || arr.length == 0) return true;
            for (int i = 0; i < arr.length; i++) {
                if (arr[i] != null) return false;
            }
            return true;
    }

    public static boolean isNullArray( String [] arr ) {
         if (arr == null || arr.length == 0) return true;
            for (int i = 0; i < arr.length; i++) {
                if (!Misc.isNullString(arr[i])) return false;
            }
            return true;
    }

    public static boolean isNullList(Collection lookupList) {
        if(lookupList == null || lookupList.size() == 0) return true;
        for (Iterator iterator = lookupList.iterator(); iterator.hasNext();) {
                if(iterator.next() != null) return false;
         }
         return true;
    }

    public static boolean isNullMap(Map map) {
		if(map == null || map.size() == 0) 
			return true;
		else        
			return false;    
	}
    
    public static boolean isNullString(String s)
	{
		return ((s == null) || (s.length() == 0) || "null".equals(s));
	}
    
    public static boolean isInt(String s)
	{
		boolean valid = true;

		try
		{
			int intl = Integer.parseInt( s );
		}
		catch( NumberFormatException ex )
		{
			valid = false;
		}

		return valid;
	}
}
