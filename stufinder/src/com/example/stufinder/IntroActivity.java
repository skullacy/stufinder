package com.example.stufinder;

import java.io.UnsupportedEncodingException;

import org.json.JSONObject;


import com.example.stufinder.util.CommServer;
import com.example.stufinder.util.StufinderUtil;
import com.google.android.gcm.GCMRegistrar;

import android.app.ActionBar;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

public class IntroActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    
		super.onCreate(savedInstanceState);
	    ActionBar actionBar = getActionBar();
        actionBar.hide();
        
	    setContentView(R.layout.activity_intro);
	    
	    //GCM Push 초기화 및 등록설정
  		GCMRegistrar.checkDevice(this);
  		GCMRegistrar.checkManifest(this);
  		final String regId = GCMRegistrar.getRegistrationId(this);
  		if("".equals(regId))
  			GCMRegistrar.register(this, "674677241398");
  		else
  			Log.e("================", regId);
	    
  		Log.e("pushid", regId);
  		final String gaccount = StufinderUtil.getAccount(this);
  		CommServer comm = new CommServer();
  		comm.setParam("act", "procStufinderCheckPushID");
  		comm.setParam("pushid", regId);
  		comm.setParam("gaccount", gaccount);
  		
  		new CheckPushTask().execute(comm); 
  		
	    
	
	}
	
	private class CheckPushTask extends AsyncTask<CommServer, Void, String> {
		protected String doInBackground(CommServer... comm) {
			String data = null;
			try {
				data = comm[0].getData();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return data;
		}

		protected void onPostExecute(String result) {
			Log.e("getPushId", result);
			try{
				JSONObject jsonobj = new JSONObject(result);
				Log.e("message", jsonobj.getString("message"));
				
				
				Intent i = new Intent(IntroActivity.this, MainActivity.class);
				startActivity(i);
				finish();
			}
			catch(Exception e){
				e.printStackTrace();
			}
			
			
		}
	}

}
