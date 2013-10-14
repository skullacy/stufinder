package com.example.stufinder.util;

import com.google.android.gms.maps.model.Marker;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

public class StufinderInfowindowAdapter {
	
	public StufinderInfowindowAdapter(CommServer comm, Marker view){
		this.comm = comm;
		this.marker = view;
	}
	
	public CommServer comm;
	public Marker marker;
	public Bitmap bitmapBinary;
	
}
