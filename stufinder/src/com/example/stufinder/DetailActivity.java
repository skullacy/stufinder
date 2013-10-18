package com.example.stufinder;

import java.io.UnsupportedEncodingException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.stufinder.util.CommServer;
import com.example.stufinder.util.StufinderInfowindowAdapter;
import com.example.stufinder.util.StufinderUtil;
import com.google.android.gms.maps.CameraUpdateFactory;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class DetailActivity extends Activity {
	
	public int stuff_srl;
	public String gaccount;
	AlertDialog.Builder alertDialog;
	ProgressDialog dialog;
	
	ImageView IV_stuffimg;
	
	ReplyAdapter replyAdapter;
	ListView replyList;
	TextView replyContent;

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_detail);
	    
	    final Intent intent = getIntent();
	    stuff_srl = intent.getExtras().getInt("stuff_srl");
	    gaccount = intent.getExtras().getString("gaccount");
	    
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
	    
	    String filepath = intent.getExtras().getString("filepath");
	    CommServer imgcomm = new CommServer();
	    imgcomm.setServerUrl(filepath);
	    IV_stuffimg = (ImageView) findViewById(R.id.stuffimg);
	    new getImageTask().execute(imgcomm);
	    
	    
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
	    
	    //리플 객체 생성, 클릭이벤트 연결
	    replyContent = (TextView) findViewById(R.id.content_reply);
	    Button replySend = (Button) findViewById(R.id.sendreply);
	    replySend.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(replyContent.getText().toString().length() < 1){
					alertDialog = new AlertDialog.Builder(DetailActivity.this);
					alertDialog.setTitle("").setMessage("댓글을 입력하세요").setNeutralButton("확인", null).show();
				}
				
				CommServer comm = new CommServer();
				comm.setParam("stuff_srl", String.valueOf(stuff_srl));
				comm.setParam("gaccount", gaccount);
				comm.setParam("content", replyContent.getText().toString());
				comm.setParam("act", "procStufinderInsertStuffReply");
				dialog = new ProgressDialog(DetailActivity.this);
				dialog.setTitle("");
				dialog.setMessage("서버에 등록중입니다.");
				dialog.show();
				new InsertReplyTask().execute(comm);
				
			}
		});
	    
	    //리플 불러오기
	    replyAdapter = new ReplyAdapter(this);
	    replyList = (ListView) findViewById(R.id.replyList);
	    
	    CommServer comm = new CommServer();
	    comm.setParam("act", "dispStufinderStuffReplyList");
	    comm.setParam("stuff_srl", String.valueOf(stuff_srl));
	    comm.setParam("gaccount", gaccount);
	    new getReplyTask().execute(comm);
	    
	    
	    
	    
	}
	
	private class InsertReplyTask extends AsyncTask<CommServer, Void, String> {
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
			dialog.dismiss();
			JSONObject jsonobj;
			try {
				jsonobj = new JSONObject(result);
				replyAdapter.insert(new ReplyItem(jsonobj.getString("gaccount"), jsonobj.getString("content"), jsonobj.getString("regdate")), 0);
				replyList.setAdapter(replyAdapter);
				StufinderUtil.setListViewHeight(replyList);
				replyContent.setText(null);
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	private class getReplyTask extends AsyncTask<CommServer, Void, String> {
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
			JSONArray jsonarr = null;
			try {
				jsonarr = new JSONArray(result);
				for (int i = 0; i < jsonarr.length(); i++) {
					JSONObject jsonobj = jsonarr.getJSONObject(i);
					Log.e("jsonobj", jsonobj.toString());
					replyAdapter.add(new ReplyItem(jsonobj.getString("gaccount"), jsonobj.getString("content"), jsonobj.getString("regdate")));
				}
				replyList.setAdapter(replyAdapter);
				StufinderUtil.setListViewHeight(replyList);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private class getImageTask extends AsyncTask<CommServer, Void, Bitmap> {
		protected Bitmap doInBackground(CommServer... comm) {
			Bitmap bitmap = null;
			Log.e("doInBackground", "getImageTask");
			try {
				bitmap = comm[0].getImage();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return bitmap;
		}

		protected void onPostExecute(Bitmap bitmap) {
			IV_stuffimg.setImageBitmap(bitmap);
		}
	}
	
	private class ReplyItem {
		public String author;
		public String content;
		public String regdate;
		public ReplyItem(String author, String content, String regdate) {
			this.author = author;
			this.content = content;
			this.regdate = regdate;
		}
	}

	public class ReplyAdapter extends ArrayAdapter<ReplyItem> {

		public ReplyAdapter(Context context) {
			super(context, 0);
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(DetailActivity.this).inflate(R.layout.piece_row_reply, null);
			}
			TextView gaccount = (TextView) convertView.findViewById(R.id.row_gaccount);
			TextView regdate = (TextView) convertView.findViewById(R.id.row_regdate);
			TextView content = (TextView) convertView.findViewById(R.id.row_content);
			
			gaccount.setText(getItem(position).author);
			regdate.setText(getItem(position).regdate);
			content.setText(getItem(position).content);

			return convertView;
		}

	}
	

}
