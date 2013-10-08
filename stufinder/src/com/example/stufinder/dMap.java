package com.example.stufinder;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

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
	}

}
