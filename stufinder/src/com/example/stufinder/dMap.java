package com.example.stufinder;

import java.io.UnsupportedEncodingException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.stufinder.util.CommServer;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

public class dMap extends FragmentActivity {
	
	GoogleMap mGoogleMap;
	LatLng loc = new LatLng(36.949437, 127.908089);
	CameraPosition cp = new CameraPosition.Builder().target((loc)).zoom(17).build();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.dmap);
	    // TODO Auto-generated method stub
	    
	    mGoogleMap = ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
	    mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cp));
	    
	    
	    CommServer comm = new CommServer();
		comm.setParam("act", "dispStufinderStuffList");
		
		new ServerCommTask().execute(comm);
		
		
	}
	
	private class ServerCommTask extends AsyncTask<CommServer, Void, String> {
		protected String doInBackground(CommServer... comm) {
			String data = null;
			try {
				data = comm[0].getData();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Log.e("2222", data);
			
			return data;
		}
		
		protected void onPostExecute(String result) {
				JSONArray jsonarr = null;
				try {
					jsonarr = new JSONArray(result.toString());
					for(int i=0; i<jsonarr.length(); i++){
						JSONObject jsonobj = jsonarr.getJSONObject(i);
						
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
		}
	}

}
