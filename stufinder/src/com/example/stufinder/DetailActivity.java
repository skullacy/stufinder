package com.example.stufinder;

import com.example.stufinder.util.StufinderUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_detail);
	    
	    final Intent intent = getIntent();
	    
	    TextView TV_title = (TextView) findViewById(R.id.title);
	    TV_title.setText(intent.getExtras().getString("title"));
	    
	    TextView TV_pos = (TextView) findViewById(R.id.pos);
	    TV_pos.setText(intent.getExtras().getString("pos"));
	    
	    final Button BT_phone = (Button) findViewById(R.id.phone);
	    BT_phone.setText(intent.getExtras().getString("phone"));
	    
	    TextView TV_date = (TextView) findViewById(R.id.date);
	    TV_date.setText(intent.getExtras().getString("date"));
	    
	    TextView TV_info = (TextView) findViewById(R.id.info);
	    TV_info.setText(intent.getExtras().getString("info"));
	    
	    //비트맵파일 ByteArray로 수신 후 변경
	    byte[] bitmapstream = intent.getByteArrayExtra("bitmapstream");
	    Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapstream, 0, bitmapstream.length);
	    
	    ImageView IV_stuffimg = (ImageView) findViewById(R.id.stuffimg);
	    if(bitmap != null){
	    	IV_stuffimg.setImageBitmap(bitmap);
	    }
	    
	    //연락처 버튼 클릭시 추가행동 선택메뉴 나타나기
	    BT_phone.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final String[] acts = new String[] {"전화걸기", "메세지 보내기"};
				new AlertDialog.Builder(DetailActivity.this).setTitle("선택")
					.setItems(acts, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							switch (which) {
							case 0:
								Log.e("dial", BT_phone.getText().toString());
								Intent i = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+BT_phone.getText()));
								startActivity(i);
								break;

							case 1:
								Log.e("message", StufinderUtil.getDefaultMsg(
										intent.getExtras().getString("phone"), 
										intent.getExtras().getInt("lgselect"), 
										intent.getExtras().getString("title")));
								SmsManager smsManager = SmsManager.getDefault();
								String smsNumber = intent.getExtras().getString("phone");
								String smsText = StufinderUtil.getDefaultMsg(
										intent.getExtras().getString("phone"), 
										intent.getExtras().getInt("lgselect"), 
										intent.getExtras().getString("title"));
								Intent in = new Intent(Intent.ACTION_SENDTO);
								in.setData(Uri.parse("sms:"+BT_phone.getText()));
								in.putExtra("sms_body", smsText);
								startActivity(in);
								//smsManager.sendTextMessage(smsNumber, null, smsText, null, null);
								break;
							}
						}
					})
					.setNeutralButton("닫기", null).show();
			}
		});
	    
	    
	    // TODO Auto-generated method stub
	}

}
