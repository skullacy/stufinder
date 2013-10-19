package com.example.stufinder;

import com.google.android.gcm.GCMRegistrar;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.NotificationManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.content.Intent;



public class MainActivity extends Activity implements View.OnClickListener 
{
	@Override
	protected void onResume(){
		super.onResume();
		//Activity 이동시 Notification 삭제
        NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        nm.cancel(1234);
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		ActionBar actionBar = getActionBar();
        actionBar.hide();
		//화면에버튼을 설정
		Button fpbtn = (Button)findViewById(R.id.fp);
		fpbtn.setOnClickListener(this);
		Button findpbtn = (Button)findViewById(R.id.findp);
		findpbtn.setOnClickListener(this);
		Button lostpbtn = (Button)findViewById(R.id.lostp);
		lostpbtn.setOnClickListener(this);
		
	}
	
	
	
	
	@Override
	public void onClick(View v)
	{
		if(v.getId() == R.id.findp)
		{
			Intent intent = new Intent(this, Smap.class);
			int selectp = 0;
			intent.putExtra("selectp", selectp);
			startActivity(intent);
		}
		else if(v.getId() == R.id.lostp)
		{
			Intent intent = new Intent(this, Smap.class);
			int selectp = 1;
			intent.putExtra("selectp", selectp);
			startActivity(intent);
		}
		else if(v.getId() == R.id.fp){
			Intent intent = new Intent(this, Dmap.class);
			startActivity(intent);
		}
		else{
			
		}
		

	}
	
}