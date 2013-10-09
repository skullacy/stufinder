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
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class DmapFragment extends Fragment{
	
	GoogleMap mGoogleMap;
	LatLng loc = new LatLng(36.949437, 127.908089);
	CameraPosition cp = new CameraPosition.Builder().target((loc)).zoom(17).build();
	
	Double lati = null;
	Double longi = null;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View v = inflater.inflate(R.layout.piece_map, null);
		
		mGoogleMap = ((SupportMapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();
		mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cp));
		
		mGoogleMap.setOnMarkerClickListener(new OnMarkerClickListener(){
			@Override
			public boolean onMarkerClick(Marker marker){
				System.out.println(marker.getPosition());
				
					new AlertDialog.Builder(getActivity())
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
		
		CommServer comm = new CommServer();
		comm.setParam("act", "dispStufinderStuffList");
		
		new ServerCommTask().execute(comm);
		return v;
	}
	
	protected void addMarker(LatLng location){
		MarkerOptions markerOptions = new MarkerOptions();
		markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
		markerOptions.position(location); 
		mGoogleMap.addMarker(markerOptions);
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
						lati = jsonobj.getDouble("lati");
						longi = jsonobj.getDouble("longi");
						LatLng loca = new LatLng(lati, longi);
						addMarker(loca);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
		}
	}
	

}
