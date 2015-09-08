package com.popo.dailyopen;

import java.util.LinkedList;

public class OpenDay {

	private static String TAG = "OpenDay";
	private String mDate;
	private LinkedList<Integer> mOpenTimeList;
	private LinkedList<Integer> mCloseTimeList;
	
	public OpenDay(String date){
		mDate = date;
		mOpenTimeList = new LinkedList<Integer>();
		mCloseTimeList = new LinkedList<Integer>();
	}
	
	public void addTime(int openTime, int closeTime){
		mOpenTimeList.add(openTime);
		mCloseTimeList.add(closeTime);
	}
	
	public int getOpenTime(int index){
		return mOpenTimeList.get(index);
	}
	
	public int getCloseTime(int index){
		return mCloseTimeList.get(index);
	}
	
	public int getNumberOfTime(){
		return mCloseTimeList.size();
	}
	
	public String getDate(){
		return mDate;
	}
	
	// get sum monitor on time
	public long getAllMonitorOnTime(){
		int numberOfTime = getNumberOfTime();
		long allMonitorOnTime = 0;
		for (int i = 0 ; i < numberOfTime ; i++){
			long openTime = mOpenTimeList.get(i);
			long closeTime = mCloseTimeList.get(i);
			allMonitorOnTime += closeTime - openTime;
		}
		return allMonitorOnTime;
	}
	
	
}
