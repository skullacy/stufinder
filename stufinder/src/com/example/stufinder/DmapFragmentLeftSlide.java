package com.example.stufinder;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.stufinder.util.StufinderUtil;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DmapFragmentLeftSlide extends ListFragment{
	
	StuffListAdapter adapter;
	
	//인터페이스를 통하여 Activity의 참조 얻어오기
	OnListSelectedListener mListener;
	
	//실제 인터페이스의 액티비티 참조는 onAttach사이클에서 얻어진다.
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		try{
			mListener = (OnListSelectedListener) activity;
		}
		catch(ClassCastException e){
			throw new ClassCastException(activity.toString() + " must implement OnArticleSelectedListener");
		}
	}
	
	
	//액티비티로 해당 이벤트를 발생시킨 후, 마커 클릭시 실행될 코드를 실행한다.
	public interface OnListSelectedListener{
		public void onListSelected(Marker marker);
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.piece_listfragment, null);
		return v;
	}
	
	

	public void onActivityCreated(Bundle savedInstanceState) {
		Log.e("count", "onActivityCreated");
		super.onActivityCreated(savedInstanceState);
		adapter = new StuffListAdapter(getActivity());
	}
	
	
	
	@Override
	public void onListItemClick(ListView lv, View v, int position, long id) {
		Marker marker = adapter.getItem(position).marker;
		mListener.onListSelected(marker);
	}

	private class StuffItem {
		public String title;
		public String pos;
		public String date;
		public String lgselect;
//		public String info;
		public Marker marker;
		public String imageUrl;
		public Boolean ismine;
		public JSONObject jsonobj;
		
		public StuffItem(JSONObject jsonobj, Marker marker) {
			try{
				this.title = jsonobj.getString("title");
				this.pos = jsonobj.getString("pos");
				this.date = jsonobj.getString("date");
				this.lgselect = jsonobj.getString("lgselect");
				this.marker = marker;
				this.imageUrl = jsonobj.getString("filepath");
				
				this.jsonobj = jsonobj;
				
				if(StufinderUtil.getAccount(getActivity()).equals(jsonobj.getString("gaccount"))) 
					this.ismine = true;
				else 
					this.ismine = false;
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}
	}

	public class StuffListAdapter extends ArrayAdapter<StuffItem> {

		public StuffListAdapter(Context context) {
			super(context, 0);
		}

		public View getView(final int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.piece_row_leftslide, null);
			}
			TextView title = (TextView) convertView.findViewById(R.id.row_title);
			title.setText(getItem(position).title);
			
			TextView pos = (TextView) convertView.findViewById(R.id.row_pos);
			pos.setText(getItem(position).pos);
			
			TextView date = (TextView) convertView.findViewById(R.id.row_date);
			date.setText(getItem(position).date);
			
			System.out.println(getItem(position).lgselect);
			
			RelativeLayout rm = (RelativeLayout)convertView.findViewById(R.id.findlostlist);
			if(Integer.parseInt(getItem(position).lgselect)==0)
			{
				rm.setBackgroundResource(R.color.list_zerolost);
			}
			else 
				rm.setBackgroundResource(R.color.list_onefind);

//			TextView info = (TextView) convertView.findViewById(R.id.row_info);
//			info.setText(getItem(position).info);
			
			ImageView image = (ImageView) convertView.findViewById(R.id.row_image);
			UrlImageViewHelper.setUrlDrawable(image, getItem(position).imageUrl, R.drawable.noimage);
			
			//수정버튼 표시
			final Button modifyBtn = (Button) convertView.findViewById(R.id.modify);
			Log.e(getItem(position).title, getItem(position).ismine.toString());
			if(getItem(position).ismine){
				modifyBtn.setVisibility(View.VISIBLE);
				modifyBtn.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent i = new Intent(getActivity(), Reg.class);
						Iterator<String> iter = getItem(position).jsonobj.keys();
						while(iter.hasNext()){
							String key = iter.next();
							try{
								i.putExtra(key, getItem(position).jsonobj.getString(key));
							}catch(Exception e){
								e.printStackTrace();
							}
						}
						i.putExtra("type", "modify");
						
						
						startActivity(i);
					}
				});
			}

			return convertView;
		}

	}
	
	public void addStuffList(Map<Marker, JSONObject> allMarkersData){
		//초기화
		adapter.clear();
		Iterator iterator = allMarkersData.entrySet().iterator();
		
		
		while (iterator.hasNext()){
			Map.Entry mapEntry = (Map.Entry) iterator.next();
			JSONObject jsonobj = (JSONObject) mapEntry.getValue();
				adapter.add(new StuffItem(jsonobj, (Marker) mapEntry.getKey()));
		}
		setListAdapter(adapter);
	}
}
