package com.example.stufinder;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.example.stufinder.util.getGoogleAccount;

import android.os.Bundle;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Button;
import android.content.Intent;
import android.widget.Spinner;



public class MainActivity extends Activity implements View.OnClickListener 
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
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
			Intent intent = new Intent(this, sMap.class);
			int selectp = 0;
			intent.putExtra("selectp", selectp);
			startActivity(intent);
		}
		else if(v.getId() == R.id.lostp)
		{
			Intent intent = new Intent(this, sMap.class);
			int selectp = 1;
			intent.putExtra("selectp", selectp);
			startActivity(intent);
		}
		else if(v.getId() == R.id.fp){
			Intent intent = new Intent(this, dMap.class);
			startActivity(intent);
		}
		else{
			
		}
		

	}
	
}