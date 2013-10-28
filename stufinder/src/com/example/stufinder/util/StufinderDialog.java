package com.example.stufinder.util;

import com.example.stufinder.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

public class StufinderDialog implements View.OnClickListener{
	
	Context context;
	Bundle bundle;
	
	AlertDialog.Builder builder;
	AlertDialog alertDialog;
	
	public StufinderDialog(Context context, Bundle bundle){
		this.context = context;
		this.bundle = bundle;
	}
	
	public Dialog getDialog(){
		
		
		Context mContext = this.context;
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.messagedialog, null);
		
		Button btn = (Button)view.findViewById(R.id.callbtn);
		Button btns = (Button)view.findViewById(R.id.smsbtn);
		Button btnc = (Button)view.findViewById(R.id.closebtn);
		
		btn.setOnClickListener(this);
		btns.setOnClickListener(this);
		btnc.setOnClickListener(this);
		
		builder = new AlertDialog.Builder(mContext);
		builder.setView(view);
		
		alertDialog = builder.create();
		
		return alertDialog;
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.callbtn:
			Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+bundle.getString("phone")));
			context.startActivity(i);
			break;
		case R.id.smsbtn:
			String smsText = StufinderUtil.getDefaultMsg(
					bundle.getString("phone"), 
					bundle.getInt("lgselect"), 
					bundle.getString("title"));
			Intent in = new Intent(Intent.ACTION_SENDTO);
			in.setData(Uri.parse("sms:"+bundle.getString("phone")));
			in.putExtra("sms_body", smsText);
			context.startActivity(in);
		default:
			alertDialog.dismiss();
			break;
		}
	}
	
	
	
}
