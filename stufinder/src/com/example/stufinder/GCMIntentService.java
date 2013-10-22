package com.example.stufinder;

import java.util.Iterator;

import com.google.android.gcm.GCMBaseIntentService;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
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
		String title = "Stufinder";
		String msg = "메세지가 도착했습니다.";
		String ticker = "Stufinder 알림";
		
		Iterator<String> iterator = b.keySet().iterator();
		while(iterator.hasNext()){
			String key = iterator.next();
			String value = b.get(key).toString();
			Log.e(tag, "onMessage. "+key+" : "+value);
			
			if(key.equals("msg")){
				msg = value;
			}
			else if(key.equals("title")){
				title = value;
			}
		}
		
		NotificationManager nm = (NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);
		Intent exeActivity = new Intent(context, IntroActivity.class);
		
		exeActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, exeActivity, PendingIntent.FLAG_UPDATE_CURRENT);
		
		
		
		
		Notification notification = new Notification(android.R.drawable.ic_input_add, ticker, System.currentTimeMillis());
		
		notification.setLatestEventInfo(context, title, msg, pendingIntent);
		notification.defaults = Notification.DEFAULT_SOUND;
		nm.notify(1234, notification);
		
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
