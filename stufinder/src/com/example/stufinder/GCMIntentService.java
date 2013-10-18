package com.example.stufinder;

import java.util.Iterator;

import com.google.android.gcm.GCMBaseIntentService;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class GCMIntentService extends GCMBaseIntentService {
	
	private static final String tag = "GCMIntentService";
	private static final String PROJECT_ID = "674677241398";
	
	public GCMIntentService(){
		this(PROJECT_ID);
	}
	public GCMIntentService(String project_id){
		super(project_id);
	}

	@Override
	protected void onError(Context context, String errorId) {
		// TODO Auto-generated method stub
		 Log.d(tag, "on_error. errorId : "+errorId);
	}

	@Override
	protected void onMessage(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Bundle b = intent.getExtras();
		
		Iterator<String> iterator = b.keySet().iterator();
		while(iterator.hasNext()){
			String key = iterator.next();
			String value = b.get(key).toString();
			Log.e(tag, "onMessage. "+key+" : "+value);
		}
		
	}

	@Override
	protected void onRegistered(Context context, String regId) {
		// TODO Auto-generated method stub
		Log.d(tag, "onRegistered. regId : "+regId);
	}

	@Override
	protected void onUnregistered(Context context, String regId) {
		// TODO Auto-generated method stub
		Log.d(tag, "onUnregistered. regId : "+regId);
	}

}
