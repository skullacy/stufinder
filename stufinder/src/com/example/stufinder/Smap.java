package com.example.stufinder;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;

public class Smap extends FragmentActivity implements View.OnClickListener {
	LatLng loc = new LatLng(36.949437, 127.908089);
	Double lati = loc.latitude;
	Double longi = loc.longitude;
	GoogleMap mGoogleMap;
	
	CameraPosition cp = new CameraPosition.Builder().target((loc)).zoom(17).build();
	MarkerOptions marker = new MarkerOptions().position(loc);
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_smap);
	    
	    ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
	    // TODO Auto-generated method stub
	    
	    mGoogleMap = ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
	    mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cp));
	    mGoogleMap.addMarker(marker);
	    
	    //cray added 클릭했을때 마커등록과 화면 이동 
	    mGoogleMap.setOnMapClickListener(new OnMapClickListener(){
	    	@Override
	    	public void onMapClick(LatLng latLng){
	    		MarkerOptions markerOptions = new MarkerOptions();
	    		markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
	    		markerOptions.position(latLng);
	    		
	    		mGoogleMap.clear();
	    		mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
	    		mGoogleMap.addMarker(markerOptions);
	    		
	    		//마커의 위경도 받아오기
	    	
	    		loc = markerOptions.getPosition();
	    		System.out.println(loc);
	    		lati = loc.latitude;
	    		longi = loc.longitude;
//	    		System.out.println(lati);
	    		
	    		//마커 위경도 받아오기 종료
	    	
	    	}
	    });
	
	  //cray ended 클릭했을때 마커등록과 화면 이동  종료
	    
	    
	    mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
	    
	    Button locabtn = (Button)findViewById(R.id.locasave);
		locabtn.setOnClickListener(this);
	}
	
	public void onClick(View v)
	{
		Intent sp = getIntent();
		int selectp = (Integer)sp.getExtras().get("selectp");
		
		Intent intent = new Intent(this, Reg.class);
		intent.putExtra("selectp", selectp);
		System.out.println(lati);
		intent.putExtra("lati", lati);
		intent.putExtra("longi", longi);
		startActivity(intent); 
		finish();

	}
}
