package com.example.stufinder;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;


import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;
import com.actionbarsherlock.widget.SearchView;
import com.example.stufinder.util.CommServer;
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
import android.widget.TextView;
import android.widget.Toast;

public class Dmap extends SlidingFragmentActivity implements DmapFragmentLeftSlide.OnListSelectedListener,
SearchView.OnQueryTextListener {
	
	SlidingMenu sm;
	private Fragment dMapFragment;
	private ListFragment dMapFragmentLeftMenu;
	static String commResult;
	
	//�˻��� ���� ����
	private static final String[] COLUMNS = {
	        BaseColumns._ID,
	        SearchManager.SUGGEST_COLUMN_TEXT_1,
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    if (savedInstanceState != null)
	    	dMapFragment = getSupportFragmentManager().getFragment(savedInstanceState, "dMapFragment");
		if (dMapFragment == null)
			dMapFragment = new DmapFragment();	
		
		dMapFragmentLeftMenu = new DmapFragmentLeftSlide();
		
		// ���� fragment ����
		setContentView(R.layout.fragment_dmap);
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.content_frame, dMapFragment)
		.commit();
		
		// ���� fragment ����(�����̵�޴� - ����)
		setBehindContentView(R.layout.fragment_dmap_leftmenu);
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.menu_frame, dMapFragmentLeftMenu)
		.commit();
		
		//�����̵�޴� ����
		sm = getSlidingMenu();
 		sm.setShadowWidthRes(R.dimen.shadow_width);
 		sm.setShadowDrawable(R.drawable.shadow);
 		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
 		sm.setFadeDegree(0.35f);
 		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
 		
 		setSlidingActionBarEnabled(false);
 		
 		
 		//�׼ǹ�
 		//getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
 		CommServer comm = new CommServer();
		comm.setParam("act", "dispStufinderStuffList");

		new ServerCommTask().execute(comm);
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		getSupportFragmentManager().putFragment(outState, "dMapFragment", dMapFragment);
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//    	getSupportMenuInflater().inflate(R.menu.mainbar, menu);
//        ActionBar actionBar = getSupportActionBar();
//        getActionBar().setDisplayShowHomeEnabled(false);
//        actionBar.setDisplayShowTitleEnabled(false);
		
        menu.add("Search")
            .setIcon(R.drawable.abs__ic_search)
            .setActionView(R.layout.collapsible_edittext)
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
        
        SubMenu subMenu = menu.addSubMenu("Filter");
        subMenu.add("���");
        subMenu.add("�� ����");
        subMenu.add("�н�Ǯ");
        subMenu.add("���湰");
        
        MenuItem subMenuItem = subMenu.getItem();
        subMenuItem.setIcon(R.drawable.ic_launcher);
        subMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        
        

        return super.onCreateOptionsMenu(menu);
    }
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //This uses the imported MenuItem from ActionBarSherlock
        Toast.makeText(this, "Got click: " + item.getItemId(), Toast.LENGTH_SHORT).show();
        return true;
    }
    
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item){
//    	Log.e("item_idx", String.valueOf(item.getItemId()));
//		
//		switch(item.getItemId()){
//		case R.id.item1:
//			sm.toggle();
//			break;
//			
//		case R.id.item2:
//			break;
//		
//		case R.id.item3:
//			break;
//			
//		default:
//			return false; 
//		}
//    	
//		return super.onOptionsItemSelected(item);
//    }
    
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
			Map<Marker, JSONObject> allMarkersData = new HashMap<Marker, JSONObject>();
			commResult = result;
			allMarkersData = ((DmapFragment) dMapFragment).addMarker(result);
			((DmapFragmentLeftSlide) dMapFragmentLeftMenu).addStuffList(allMarkersData);
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
	 * @���� �Ʒ����ʹ� �׼ǹٿ� Ŭ����, �������̽� , �޼ҵ�
	 */

	@Override
	public boolean onQueryTextSubmit(String query) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		// TODO Auto-generated method stub
		return false;
	}
	

}
