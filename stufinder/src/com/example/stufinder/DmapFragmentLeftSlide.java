package com.example.stufinder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DmapFragmentLeftSlide extends ListFragment{
	
	SampleAdapter adapter;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.e("count", "oncreateview");
		return inflater.inflate(R.layout.piece_listfragment, null);
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		Log.e("count", "onActivityCreated");
		super.onActivityCreated(savedInstanceState);
		
//		for (int i = 0; i < 20; i++) {
//			adapter.add(new SampleItem("Sample List", android.R.drawable.ic_menu_search));
//		}
		adapter = new SampleAdapter(getActivity());
		//setListAdapter(adapter);
		
	}

	private class SampleItem {
		public String tag;
		public int iconRes;
		public SampleItem(String tag, int iconRes) {
			this.tag = tag; 
			this.iconRes = iconRes;
		}
	}

	public class SampleAdapter extends ArrayAdapter<SampleItem> {

		public SampleAdapter(Context context) {
			super(context, 0);
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.piece_row, null);
			}
			ImageView icon = (ImageView) convertView.findViewById(R.id.row_icon);
			icon.setImageResource(getItem(position).iconRes);
			TextView title = (TextView) convertView.findViewById(R.id.row_title);
			title.setText(getItem(position).tag);

			return convertView;
		}

	}
	
	public void addStuffList(String commResult){
		JSONArray jsonarr = null;
		try {
			jsonarr = new JSONArray(commResult);
			for (int i = 0; i < jsonarr.length(); i++) {
				JSONObject jsonobj = jsonarr.getJSONObject(i);
				adapter.add(new SampleItem(jsonobj.getString("info").toString(), android.R.drawable.ic_menu_search));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setListAdapter(adapter);
	}

}
