package com.example.stufinder;

import java.io.UnsupportedEncodingException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.stufinder.util.CommServer;
import com.example.stufinder.util.StufinderInfowindowAdapter;
import com.example.stufinder.util.StufinderUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;

import android.app.ActionBar;
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
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class DetailActivity extends Activity {
	
	protected int stuff_srl;
	protected String gaccount;
	protected AlertDialog.Builder alertDialog;
	protected ProgressDialog dialog;
	
	protected ImageView IV_stuffimg;
	
	protected ReplyAdapter replyAdapter;
	protected ListView replyList;
	protected TextView replyContent;
	
	protected PullToRefreshScrollView mPullRefreshScrollView;
	protected ScrollView mScrollView;
	protected int reply_page = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_detail);
	    
	    ActionBar actionBar = getActionBar();
	    actionBar.hide();
	    
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
	    
	    
	    //����ó ��ư Ŭ���� �߰��ൿ ���ø޴� ��Ÿ����
	    BT_phone.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final String[] acts = new String[] {"��ȭ�ɱ�", "�޼��� ������"};
				new AlertDialog.Builder(DetailActivity.this).setTitle("����")
					.setItems(acts, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							switch (which) {
							case 0:
								Log.e("dial", BT_phone.getText().toString());
								Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+BT_phone.getText()));
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
					.setNeutralButton("�ݱ�", null).show();
			}
		});
	    
	    //���� ��ü ����, Ŭ���̺�Ʈ ����
	    replyContent = (TextView) findViewById(R.id.content_reply);
	    Button replySend = (Button) findViewById(R.id.sendreply);
	    replySend.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(replyContent.getText().toString().length() < 1){
					alertDialog = new AlertDialog.Builder(DetailActivity.this);
					alertDialog.setTitle("").setMessage("����� �Է��ϼ���").setNeutralButton("Ȯ��", null).show();
				}
				
				CommServer comm = new CommServer();
				comm.setParam("stuff_srl", String.valueOf(stuff_srl));
				comm.setParam("gaccount", gaccount);
				comm.setParam("content", replyContent.getText().toString());
				comm.setParam("act", "procStufinderInsertStuffReply");
				dialog = new ProgressDialog(DetailActivity.this);
				dialog.setTitle("");
				dialog.setMessage("������ ������Դϴ�.");
				dialog.show();
				new InsertReplyTask().execute(comm);
				
			}
		});
	    
	    //���� �ҷ�����
	    replyAdapter = new ReplyAdapter(this);
	    replyList = (ListView) findViewById(R.id.replyList);
	    
	    //��ũ�Ѻ� Pulltorefresh ����
	    mPullRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.detail_scroll);
	    mPullRefreshScrollView.setMode(Mode.PULL_FROM_END);
	    mPullRefreshScrollView.setOnRefreshListener(new OnRefreshListener<ScrollView>() {
	    	
			@Override
			public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
				CommServer comm = new CommServer();
			    comm.setParam("act", "dispStufinderStuffReplyList");
			    comm.setParam("stuff_srl", String.valueOf(stuff_srl));
			    comm.setParam("gaccount", gaccount);
			    new getReplyTask().execute(comm);
			}
		});
	    
	    mScrollView = mPullRefreshScrollView.getRefreshableView();
	    
	    //���� ���� ����
	    CommServer comm = new CommServer();
	    comm.setParam("act", "dispStufinderStuffReplyList");
	    comm.setParam("stuff_srl", String.valueOf(stuff_srl));
	    comm.setParam("gaccount", gaccount);
	    new getReplyTask().execute(comm);
	    
	    
	    
	}
	
	private void setReplyCount(int count){
		TextView replyCount = (TextView) findViewById(R.id.reply_count);
		
		replyCount.setText(String.format("%d ���� ������ �ֽ��ϴ�.", count));
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
			Log.e("page_count", String.valueOf(reply_page));
			String data = null;
			comm[0].setParam("page", String.valueOf(++reply_page));
			try {
				data = comm[0].getData();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return data;
		}

		protected void onPostExecute(String result) {
			Log.e("reply list", result);
			JSONObject resultObj = null;
			JSONArray jsonarr = null;
			try {
				resultObj = new JSONObject(result);
				setReplyCount(resultObj.getInt("total_count"));
				Log.e("total_page", String.valueOf(resultObj.getInt("total_page")));
				Log.e("reply_page", String.valueOf(reply_page));
				if(resultObj.getInt("total_page") >= reply_page){
					jsonarr = resultObj.getJSONArray("data");
					for (int i = 0; i < jsonarr.length(); i++) {
						JSONObject jsonobj = jsonarr.getJSONObject(i);
						Log.e("jsonobj", jsonobj.toString());
						replyAdapter.add(new ReplyItem(jsonobj.getString("gaccount"), jsonobj.getString("content"), jsonobj.getString("regdate")));
					}
					replyList.setAdapter(replyAdapter);
					StufinderUtil.setListViewHeight(replyList);
				}
				else{
					Toast.makeText(DetailActivity.this, "����� �� �������� �ʽ��ϴ�.", Toast.LENGTH_SHORT).show();
					reply_page--;
				}
				
				
				mPullRefreshScrollView.onRefreshComplete();
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
			WindowManager wm = (WindowManager)getSystemService(WINDOW_SERVICE);
		    Display display = wm.getDefaultDisplay();
		    int width = display.getWidth();
		    System.out.println(width);
		    Bitmap resizebitmap = Bitmap.createScaledBitmap(bitmap,width,width,true);
			IV_stuffimg.setImageBitmap(resizebitmap);
			
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
