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
	
	StuffListAdapter adapter;
	
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
		adapter = new StuffListAdapter(getActivity());
		//setListAdapter(adapter);
		
	}

	private class StuffItem {
		public String title;
		public String pos;
		public String date;
		public String info;
		
		public StuffItem(String title, String pos, String date, String info) {
			this.title = title;
			this.pos = pos;
			this.date = date;
			this.info = info;
		}
	}

	public class StuffListAdapter extends ArrayAdapter<StuffItem> {

		public StuffListAdapter(Context context) {
			super(context, 0);
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.piece_row_leftslide, null);
			}
			TextView title = (TextView) convertView.findViewById(R.id.row_title);
			title.setText(getItem(position).title);
			
			TextView pos = (TextView) convertView.findViewById(R.id.row_pos);
			pos.setText(getItem(position).pos);
			
			TextView date = (TextView) convertView.findViewById(R.id.row_date);
			date.setText(getItem(position).date);
			
			TextView info = (TextView) convertView.findViewById(R.id.row_info);
			info.setText(getItem(position).info);

			return convertView;
		}

	}
	
	public void addStuffList(String commResult){
		JSONArray jsonarr = null;
		try {
			jsonarr = new JSONArray(commResult);
			for (int i = 0; i < jsonarr.length(); i++) {
				JSONObject jsonobj = jsonarr.getJSONObject(i);
				adapter.add(new StuffItem(jsonobj.getString("title"), 
						jsonobj.getString("pos"), 
						jsonobj.getString("date"), 
						jsonobj.getString("info")));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setListAdapter(adapter);
	}

}
