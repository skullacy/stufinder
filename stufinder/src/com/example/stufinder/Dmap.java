package com.example.stufinder;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;


import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;
import com.actionbarsherlock.view.Window;
import com.actionbarsherlock.widget.SearchView;
import com.example.stufinder.util.CommServer;
import com.example.stufinder.util.StufinderUtil;
import com.google.android.gms.maps.model.Marker;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class Dmap extends SlidingFragmentActivity implements DmapFragmentLeftSlide.OnListSelectedListener,
ActionBar.OnNavigationListener, SearchView.OnQueryTextListener {
	
	SlidingMenu sm;
	private Fragment dMapFragment;
	private ListFragment dMapFragmentLeftMenu;
	static String commResult;
	
	public static final int STUFF_FILTER_ALL = 0;
	public static final int STUFF_FILTER_MINE = 1;
	public static final int STUFF_FILTER_LOST = 2;
	public static final int STUFF_FILTER_GET = 3;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.Theme_Sherlock);
	    super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
	    
	    if (savedInstanceState != null)
	    	dMapFragment = getSupportFragmentManager().getFragment(savedInstanceState, "dMapFragment");
		if (dMapFragment == null)
			dMapFragment = new DmapFragment();	
		
		dMapFragmentLeftMenu = new DmapFragmentLeftSlide();
		
		// 상위 fragment 설정
		setContentView(R.layout.fragment_dmap);
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.content_frame, dMapFragment)
		.commit();
		
		// 하위 fragment 설정(슬라이드메뉴 - 왼쪽)
		setBehindContentView(R.layout.fragment_dmap_leftmenu);
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.menu_frame, dMapFragmentLeftMenu)
		.commit();
		
		//슬라이드메뉴 연결
		sm = getSlidingMenu();
 		sm.setShadowWidthRes(R.dimen.shadow_width);
 		sm.setShadowDrawable(R.drawable.shadow);
 		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
 		sm.setFadeDegree(0.35f);
 		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
 		
 		setSlidingActionBarEnabled(false);
 		
 		
 		//액션바
 		//getSupportActionBar().setDisplayHomeAsUpEnabled(true);
 		
 		getStuffList();
 		
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		getSupportFragmentManager().putFragment(outState, "dMapFragment", dMapFragment);
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
    	//getSupportMenuInflater().inflate(R.menu.mainbar, menu);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
//        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setIcon(R.drawable.showlist);
		
        Context context = getSupportActionBar().getThemedContext();
        
        List<String> items = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.locations)));
        
        ArrayAdapter<String> list = new ArrayAdapter<String>(context, R.layout.sherlock_spinner_item, items);
        list.setDropDownViewResource(R.layout.sherlock_spinner_dropdown_item);
        
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        actionBar.setListNavigationCallbacks(list, this);
        
        SearchView searchView = new SearchView(context);
        searchView.setQueryHint("Search");
        searchView.setOnQueryTextListener(this);
        menu.add("Search")
        	.setIcon(R.drawable.abs__ic_search)
        	.setActionView(searchView)
        	.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
      
        return super.onCreateOptionsMenu(menu);
    }
	
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
    	Log.e("item_idx", String.valueOf(item.getItemId()));
		
		switch(item.getItemId()){
		case android.R.id.home:
			sm.toggle();
			break;
			
		default:
			return false; 
		}
    	
		return super.onOptionsItemSelected(item);
    }
	
	public void getStuffList(){
 		CommServer comm = new CommServer();
		comm.setParam("act", "dispStufinderStuffList");
		getStuff(comm);
	}
	
	public void getStuffList(String searchQuery){
		CommServer comm = new CommServer();
		comm.setParam("act", "dispStufinderStuffList");
		comm.setParam("search_keyword", searchQuery);
		getStuff(comm);
	}
	
	public void getStuffList(int fType){
		Log.e("ftype", String.valueOf(fType));
		CommServer comm = new CommServer();
		comm.setParam("act", "dispStufinderStuffList");
		comm.setParam("ftype", String.valueOf(fType));
		getStuff(comm);
	}
	
	public void getStuffList(int fType, String gaccount){
		CommServer comm = new CommServer();
		comm.setParam("act", "dispStufinderStuffList");
		comm.setParam("ftype", String.valueOf(fType));
		comm.setParam("gaccount", gaccount);
		getStuff(comm);
	}
	
	public void getStuff(CommServer comm){
		setSupportProgressBarIndeterminateVisibility(true);
		new ServerCommTask().execute(comm);
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
			return data;
		}

		protected void onPostExecute(String result) {
			Map<Marker, JSONObject> allMarkersData = new HashMap<Marker, JSONObject>();
			commResult = result;
			allMarkersData = ((DmapFragment) dMapFragment).addMarker(result);
			((DmapFragmentLeftSlide) dMapFragmentLeftMenu).addStuffList(allMarkersData);
			setSupportProgressBarIndeterminateVisibility(false);
		}
	}

	@Override
	public void onListSelected(Marker marker) {
		Log.e("onListSelected", "1111");
		//((DmapFragment) dMapFragment).onMarkerSelected(marker);
		//sm.toggle();
		((DmapFragment) dMapFragment).changeActivityByMarker(marker);
	}
	
	/**
	 * @여기 아래부터는 액션바용 클래스, 인터페이스 , 메소드
	 */
	
	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		// TODO Auto-generated method stub
		Log.e("필터링 시작", String.valueOf(itemPosition));
		switch (itemPosition) {
		case STUFF_FILTER_ALL:
			getStuffList();
			break;
		case STUFF_FILTER_MINE:
			String gaccount = StufinderUtil.getAccount(this);
			getStuffList(STUFF_FILTER_MINE, gaccount);
			break;
		case STUFF_FILTER_LOST:
			getStuffList(STUFF_FILTER_LOST);
			break;
		case STUFF_FILTER_GET:
			getStuffList(STUFF_FILTER_GET);
			break;

		default:
			break;
		}
		
		return false;
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		// TODO Auto-generated method stub
		Log.e("onQueryTextSubmit", query);
		getStuffList(query);
		return false;
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		if(newText == null || newText.isEmpty()){
			getStuffList();
		}
		// TODO Auto-generated method stub
		Log.e("onQueryTextChange", newText);
		return false;
	}

	
	
	
	


}
