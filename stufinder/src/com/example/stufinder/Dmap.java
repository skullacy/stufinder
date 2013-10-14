package com.example.stufinder;

import java.io.UnsupportedEncodingException;


import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.example.stufinder.util.CommServer;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;

public class Dmap extends SlidingFragmentActivity {
	
	SlidingMenu sm;
	private Fragment dMapFragment;
	private ListFragment dMapFragmentLeftMenu;
	static String commResult;

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
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
 		
 		//액션바
 		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
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
    	getSupportMenuInflater().inflate(R.menu.mainbar, menu);
        ActionBar actionBar = getSupportActionBar();
        getActionBar().setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
		
		switch(item.getItemId()){
		case R.id.item1:
			sm.toggle();
			break;
			
		case R.id.item2:
			break;
		
		case R.id.item3:
			break;
			
		default:
			return false; 
		}
    	
		return super.onOptionsItemSelected(item);
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
			commResult = result;
			((DmapFragment) dMapFragment).addMarker(result);
			((DmapFragmentLeftSlide) dMapFragmentLeftMenu).addStuffList(result);
		}
	}

}
