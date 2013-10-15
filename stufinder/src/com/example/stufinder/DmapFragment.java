package com.example.stufinder;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
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

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
		
		//���۸� ����
		mGoogleMap = ((SupportMapFragment) getFragmentManager()
				.findFragmentById(R.id.map)).getMap();
		mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cp));
		//Infowindow Ŀ���͸���¡
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
				try {
					String imagePath = allMarkersData.get(marker).getString("filepath");
					CommServer comm = new CommServer();
					comm.setServerUrl(imagePath);
					StufinderInfowindowAdapter iwadt = new StufinderInfowindowAdapter(comm, marker);
					new getImageTask().execute(iwadt);
					
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				return true;
			}
		});
		
		//Infowindow Ŭ���̺�Ʈ
		mGoogleMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
			@Override
			public void onInfoWindowClick(Marker marker) {
				System.out.println(marker.getPosition());
				JSONObject markerdata = allMarkersData.get(marker);
				Intent i = new Intent(getActivity(), DetailActivity.class);
				Log.e("markerdata", markerdata.toString());
				try {
					i.putExtra("title", markerdata.getString("title"));
					i.putExtra("lgselect", markerdata.getInt("lgselect"));
					i.putExtra("pos", markerdata.getString("pos"));
					i.putExtra("phone", markerdata.getString("phone"));
					i.putExtra("info", markerdata.getString("info"));
					i.putExtra("date", markerdata.getString("date"));
					i.putExtra("stuff_srl", markerdata.getInt("stuff_srl"));
					i.putExtra("gaccount", markerdata.getString("gaccount"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//100kb�̻��� ��� bitmap��ü�� ������ transaction ���� �߻��ϹǷ�, bytearray stream��ü�� ��ȯ �� ������.
				Bitmap bitmap = allInfowindowBitmap.get(marker);
				if(bitmap != null){
					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					bitmap.compress(CompressFormat.JPEG, 100, stream);
					byte[] bitmapstream = stream.toByteArray();
					i.putExtra("bitmapstream", bitmapstream);
					Log.e("intent", i.toString());
				}
				
				startActivity(i);
			}
			
		});

		return v;
	}

	public void addMarker(String commResult) {
		JSONArray jsonarr = null;
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