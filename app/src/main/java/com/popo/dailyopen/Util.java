package com.popo.dailyopen;

import java.util.concurrent.TimeUnit;

public class Util {
	
	public static long getHours(long milliseconds){
		return milliseconds / (1000 * 60 *60);
		
	}
	
	public static long getMinutes(long milliseconds){
		long leftMilliseconds = milliseconds - (getHours(milliseconds) * 1000 * 60 * 60);
		return leftMilliseconds / (1000 * 60);
	}
	
	public static long getSeconds(long milliseconds){
		long leftMilliseconds = milliseconds - (getHours(milliseconds) * 1000 * 60 * 60) - (getMinutes(milliseconds) * 1000 * 60);
		return TimeUnit.MILLISECONDS.toSeconds(leftMilliseconds);
	}
	
	
}
