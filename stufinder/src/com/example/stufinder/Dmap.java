package com.example.stufinder;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public class Dmap extends SlidingFragmentActivity {
	
	private Fragment dMapFragment;

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    if (savedInstanceState != null)
	    	dMapFragment = getSupportFragmentManager().getFragment(savedInstanceState, "dMapFragment");
		if (dMapFragment == null)
			dMapFragment = new DmapFragment();	
		
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
		.replace(R.id.menu_frame, new DmapFragmentLeftSlide())
		.commit();
		
		//슬라이드메뉴 연결
		SlidingMenu sm = getSlidingMenu();
 		sm.setShadowWidthRes(R.dimen.shadow_width);
 		sm.setShadowDrawable(R.drawable.shadow);
 		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
 		sm.setFadeDegree(0.35f);
 		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		
		
	}
	
	

}
