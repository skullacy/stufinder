package com.example.stufinder;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;

public class sMap extends FragmentActivity implements View.OnClickListener {
	
	GoogleMap mGoogleMap;
	LatLng loc = new LatLng(36.949437, 127.908089);
	CameraPosition cp = new CameraPosition.Builder().target((loc).zoom(16).build());
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.smap);
	    // TODO Auto-generated method stub
	    
	    Button locabtn = (Button)findViewById(R.id.locasave);
		locabtn.setOnClickListener(this);
	}
	
	public void onClick(View v)
	{
		Intent sp = getIntent();
//		int selectp = (Integer)sp.getExtras().get("selectp");
		
		Intent intent = new Intent(this, reg.class);
//		intent.putExtra("selectp", selectp);
//		intent.putExtra("lati", lati);
//		intent.putExtra("longi", longi);
		startActivity(intent); 

	}
}
