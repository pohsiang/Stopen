package com.popo.dailyopen;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.LinkedList;

public class DailyOpenListAdapter extends BaseAdapter {

	private static String TAG = "DailyOpenListAdapter";
	private LinkedList<OpenDay> mOpenDayList;
	private Context mContext;
    private int mTodayViewReadCount = 0;
	public DailyOpenListAdapter(Context context, LinkedList<OpenDay> opendaylist){
		mOpenDayList = opendaylist;
		mContext = context;
	}
	
	@Override
	public int getCount() {
		return mOpenDayList.size();
	}

	@Override
	public Object getItem(int position) {
		return mOpenDayList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		OpenDayView opendayview = new OpenDayView(mContext, mOpenDayList.get(position));
        Log.v(TAG, "getView");
		return opendayview.getView();
	}
}
