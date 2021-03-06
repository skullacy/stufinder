package com.example.stufinder;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.stufinder.util.CommServer;
import com.example.stufinder.util.StufinderInfowindowAdapter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.ImageView;

public class DmapFragment extends Fragment {

	GoogleMap mGoogleMap;
	LatLng loc = new LatLng(36.949437, 127.908089);
	CameraPosition cp = new CameraPosition.Builder().target((loc)).zoom(17)
			.build();

	private Map<Marker, JSONObject> allMarkersData = new HashMap<Marker, JSONObject>();
	private Map<Marker, Bitmap> allInfowindowBitmap = new HashMap<Marker, Bitmap>();
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.piece_map, null);
		
		//구글맵 세팅
		mGoogleMap = ((SupportMapFragment) getFragmentManager()
				.findFragmentById(R.id.map)).getMap();
		mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cp));
		mGoogleMap.getUiSettings().setZoomControlsEnabled(isHidden());
		//Infowindow 커스터마이징
		mGoogleMap.setInfoWindowAdapter(new InfoWindowAdapter(){
			@Override
			public View getInfoWindow(Marker arg0){
				return null;
			}
			
			@Override
			public View getInfoContents(Marker marker){
				LayoutInflater infoInflater = LayoutInflater.from(getActivity());
				View myContentsView = infoInflater.inflate(R.layout.piece_infowindow, null);
				
				ImageView img = (ImageView)myContentsView.findViewById(R.id.stuffimg);
				img.setImageBitmap(allInfowindowBitmap.get(marker));
				return myContentsView;
			}
		});
		
		mGoogleMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			@Override
			public boolean onMarkerClick(Marker marker) {
				Log.e("onMarkerClickEvent", "1111");
				onMarkerSelected(marker);
				return true;
			}
		});
		//dmap 글쓰기 버튼 
		Button btnfind = (Button)v.findViewById(R.id.dmapfindbtn);
		btnfind.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(getActivity(), Smap.class);
				int lgselect = 0;
				intent.putExtra("lgselect", lgselect);
				startActivity(intent);
			}
		});	
		
		Button btnlost = (Button)v.findViewById(R.id.dmaplostbtn);
		btnlost.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), Smap.class);
				int lgselect = 1;
				intent.putExtra("lgselect", lgselect);
				startActivity(intent);
			}
		});
		
		//Infowindow 클릭이벤트
		mGoogleMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
			@Override
			public void onInfoWindowClick(Marker marker) {
				changeActivityByMarker(marker);
			}
		});
		
		return v;
	}
	
	
	
	private Button findViewById(int dmapfindbtn) {
		// TODO Auto-generated method stub
		return null;
	}

	public Map<Marker, JSONObject> addMarker(String commResult) {
		//초기화
		JSONArray jsonarr = null;
		allMarkersData.clear();
		mGoogleMap.clear();
		
		
		try {
			jsonarr = new JSONArray(commResult);
			for (int i = 0; i < jsonarr.length(); i++) {
				JSONObject jsonobj = jsonarr.getJSONObject(i);
				Double lati = jsonobj.getDouble("lati");
				Double longi = jsonobj.getDouble("longi");
				LatLng loca = new LatLng(lati, longi);
				
				IconGenerator mIconGenerator = new IconGenerator(getActivity());
				if(jsonobj.getInt("lgselect") == 0){
					mIconGenerator.setStyle(IconGenerator.STYLE_BLUE);
				}
				else{
					mIconGenerator.setStyle(IconGenerator.STYLE_ORANGE);
				}
				
				Bitmap iconBitmap = mIconGenerator.makeIcon(jsonobj.getString("title"));
				
				MarkerOptions markerOptions = new MarkerOptions();
				markerOptions.icon(BitmapDescriptorFactory
						.fromBitmap(iconBitmap));
				markerOptions.position(loca).title(jsonobj.getString("info"));
				Marker marker = mGoogleMap.addMarker(markerOptions);
				allMarkersData.put(marker, jsonobj);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return allMarkersData;
		
	}
	
	public void onMarkerSelected(Marker marker){
		try {
			if(allMarkersData.get(marker).getString("filename") == "null")
			{
				Bitmap noimg=BitmapFactory.decodeResource(getResources(), R.drawable.noimage);
				allInfowindowBitmap.put(marker, noimg);
				marker.showInfoWindow();
			}
			else
			{
				Log.e("onMarkerSelected", "1111");
				String imagePath = allMarkersData.get(marker).getString("filepath");
				Log.e("imagePath", imagePath);
				CommServer comm = new CommServer();
				comm.setServerUrl(imagePath);
				StufinderInfowindowAdapter iwadt = new StufinderInfowindowAdapter(comm, marker);
				new getImageTask().execute(iwadt);
			}
			
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	public void changeActivityByMarker(Marker marker){
		JSONObject markerdata = allMarkersData.get(marker);
		Intent i = new Intent(getActivity(), DetailActivity.class);
		Log.e("markerdata", markerdata.toString());
		try {
			i.putExtra("title", markerdata.getString("title"));
			i.putExtra("lgselect", markerdata.getString("lgselect"));
			i.putExtra("pos", markerdata.getString("pos"));
			i.putExtra("phone", markerdata.getString("phone"));
			i.putExtra("info", markerdata.getString("info"));
			i.putExtra("date", markerdata.getString("date"));
			i.putExtra("stuff_srl", markerdata.getString("stuff_srl"));
			i.putExtra("gaccount", markerdata.getString("gaccount"));
			i.putExtra("filename", markerdata.getString("filename"));
			i.putExtra("filepath", markerdata.getString("filepath"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		startActivity(i);
		getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
	}
	
	private class getImageTask extends AsyncTask<StufinderInfowindowAdapter, Void, StufinderInfowindowAdapter> {
		protected StufinderInfowindowAdapter doInBackground(StufinderInfowindowAdapter... iwadt) {
			Log.e("doInBackground", "getImageTask");
			try {
				if(allInfowindowBitmap.get(iwadt[0].marker) == null){
					iwadt[0].bitmapBinary = iwadt[0].comm.getImage();
					allInfowindowBitmap.put(iwadt[0].marker, iwadt[0].bitmapBinary);
				}
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return iwadt[0];
		}

		protected void onPostExecute(StufinderInfowindowAdapter iwadt) {
			Log.e("doInBackground", "setImageFromBitmap");
			iwadt.marker.showInfoWindow();
			mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(iwadt.marker.getPosition()), 250, null);
		}
	}



}
