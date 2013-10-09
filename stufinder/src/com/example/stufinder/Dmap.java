package com.example.stufinder;

import java.io.UnsupportedEncodingException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.stufinder.util.CommServer;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

public class Dmap extends FragmentActivity {
	
	GoogleMap mGoogleMap;
	
	LatLng loc = new LatLng(36.949437, 127.908089);
	CameraPosition cp = new CameraPosition.Builder().target((loc)).zoom(17).build();
	
	Double lati = null;
	Double longi = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_dmap);
	    // TODO Auto-generated method stub
	    
	    mGoogleMap = ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
	    mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cp));
	   
		mGoogleMap.setOnMarkerClickListener(new OnMarkerClickListener(){
			@Override
			public boolean onMarkerClick(Marker marker){
				System.out.println(marker.getPosition());
				
					new AlertDialog.Builder(Dmap.this)
	                .setTitle("Ahmedabad")
	                .setPositiveButton("OK",
	                        new DialogInterface.OnClickListener() {

	                            @Override
	                            public void onClick(
	                                    DialogInterface dialog,
	                                    int which) {
	                                // TODO Auto-generated method stub

	                            }
	                        }).show();
			return false;
			}
			
		});
	    
	    
	    
		
		
	}
	

}
