package com.example.stufinder;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.stufinder.util.CommServer;
import com.example.stufinder.util.StufinderUtil;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.view.View.OnClickListener;

import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

public class Reg extends Activity implements View.OnClickListener {

	private static final String TEMP_PHOTO_FILE = "temp.jpg";
	private static final int REQ_CODE_PICK_IMAGE = 0;
	Bitmap selectedImage;

	private Button PickDate;
	EditText phoneEdit;
	EditText posEdit;
	EditText infoEdit;
	EditText writep;
	
	String stuff_srl;
	Boolean ismodify;
	Double lati;
	Double longi;
	int lgselect;

	Button saveBtn;
	private int mYear;
	private int mMonth;
	private int mDay;
	static final int DATE_DIALOG_ID = 0;

	public ProgressDialog dialog;
	AlertDialog.Builder alertDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reg);
		
		ActionBar actionBar = getActionBar();
		actionBar.hide();
		
		Log.e("222222222222", "129837189243");
		writep = (EditText) findViewById(R.id.writep);
		phoneEdit = (EditText) findViewById(R.id.phonet);
		// 전화번호 형식으로 자동입력 (미완성)
		phoneEdit.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
		
		posEdit = (EditText) findViewById(R.id.post);
		infoEdit = (EditText) findViewById(R.id.infot);
		((Button) findViewById(R.id.saved)).setOnClickListener(this);
		Button photoUp = (Button) findViewById(R.id.photoUpdate);
		
		PickDate = (Button) findViewById(R.id.dateb);
		Log.e("before intent", "before intent");
		Intent intent = getIntent();
		
		
		
		
		ismodify = intent.getExtras().containsKey("type");
		//Log.e("intent type", intent.getExtras().getString("type").toString());
		if(ismodify){
			lati = Double.parseDouble(intent.getExtras().getString("lati"));
			longi = Double.parseDouble(intent.getExtras().getString("longi"));
			lgselect = Integer.parseInt(intent.getExtras().getString("lgselect"));
			
			posEdit.setText(intent.getExtras().getString("pos"));
			phoneEdit.setText(intent.getExtras().getString("phone"));
			PickDate.setText(intent.getExtras().getString("date"));
			infoEdit.setText(intent.getExtras().getString("info"));
			writep.setText(intent.getExtras().getString("title"));
			stuff_srl = intent.getExtras().getString("stuff_srl");
		}
		else{
			lati = intent.getExtras().getDouble("lati");
			longi = intent.getExtras().getDouble("longi");
			lgselect = intent.getExtras().getInt("lgselect");
			
			// 현재 핸드폰 전화번호 자동입력
			TelephonyManager telManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
			phoneEdit.setText(telManager.getLine1Number());
			
			
			final Calendar c = Calendar.getInstance();
			mYear = c.get(Calendar.YEAR);
			mMonth = c.get(Calendar.MONTH);
			mDay = c.get(Calendar.DAY_OF_MONTH);
			updateDisplay();
		}

		photoUp.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(
						Intent.ACTION_GET_CONTENT,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				intent.setType("image/*");
				intent.putExtra("crop", "true");
				intent.putExtra("aspectX", 1);
				intent.putExtra("aspectY", 1);
				intent.putExtra("scale", true);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, getTempUri());
				intent.putExtra("outputFormat",
						Bitmap.CompressFormat.JPEG.toString());
				startActivityForResult(intent, REQ_CODE_PICK_IMAGE);

			}

			private Uri getTempUri() {
				return Uri.fromFile(getTempFile());
			}

			// 외장메모리에 임시 이미지 파일을 생성하여 그 파일의 경로를 반환
			private File getTempFile() {
				if (isSDCARDMOUNTED()) {
					File f = new File(
							Environment.getExternalStorageDirectory(),
							TEMP_PHOTO_FILE);
					try {
						f.createNewFile();
					} catch (IOException e) {

					}
					return f;
				} else
					return null;
			}

			// SD카드가 마운트 되어 있는지 확인
			private boolean isSDCARDMOUNTED() {
				String status = Environment.getExternalStorageState();
				if (status.equals(Environment.MEDIA_MOUNTED))
					return true;

				return false;
			}

		});

		
		PickDate.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				showDialog(DATE_DIALOG_ID);

			}
		});

		
	}

	private void updateDisplay() {
		PickDate.setText(String.format("%04d년 %02d월 %02d일", mYear, mMonth + 1,
				mDay));
	}

	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			//
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			updateDisplay();
		}

	};

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,
					mDay);
		}
		return null;
	}

	public void setCommSubmit(boolean sendPush) {
		// TODO Auto-generated method stub
		String pos = posEdit.getText().toString();
		String phone = phoneEdit.getText().toString();
		String date = PickDate.getText().toString();
		String info = infoEdit.getText().toString();
		String category = writep.getText().toString();

		

		Log.e("onClick", "aaaa");

		CommServer comm = new CommServer();
		comm.setParam("act", "procStufinderInsertData");
		if(ismodify){
			comm.setParam("stuff_srl", stuff_srl);
		}
		comm.setParam("title", category);
		comm.setParam("pos", pos);
		comm.setParam("phone", phone);
		comm.setParam("lgselect", String.valueOf(lgselect));
		comm.setParam("info", info);
		comm.setParam("date", date);
		comm.setParam("lati", Double.toString(lati));
		comm.setParam("longi", Double.toString(longi));
		comm.setParam("gaccount", StufinderUtil.getAccount(this));
		
		//푸시 보내기 설정 파라미터
		comm.setParam("sendPush", sendPush ? "Y" : "N");

		if (selectedImage != null) {
			comm.insertImage(selectedImage);
		}

		dialog = new ProgressDialog(Reg.this);
		dialog.setTitle("");
		dialog.setMessage("서버에 등록중입니다.");
		dialog.show();

		new ServerCommTask().execute(comm);
	}

	@Override
	public void onClick(View v) {

		new AlertDialog.Builder(this)
				.setTitle("도움 요청하기")
				.setMessage("주변의 가까운 사람들에게 알림을 보내겠습니까?")
				.setPositiveButton("예", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface selfdialog, int which) {
						// TODO Auto-generated method stub
						setCommSubmit(true);
					}
				})
				.setNegativeButton("아니오", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface selfdialog,
							int which) {
						// TODO Auto-generated method stub
						setCommSubmit(false);
					}
				}).show();

	}

	// 액티비티로 복귀하였을때 이미지 세팅ㅋㅋㅋㅋ
	protected void onActivityResult(int requestCode, int resultCode,
			Intent imageData) {
		super.onActivityResult(requestCode, resultCode, imageData);

		switch (requestCode) {
		case REQ_CODE_PICK_IMAGE: {
			if (resultCode == RESULT_OK) {
				if (imageData != null) {
					String filePath = Environment.getExternalStorageDirectory()
							+ "/temp.jpg";
					// 로그캣으로 경로 확인
					System.out.println("path" + filePath);
					// temp.jpg파일을 Bitmap으로 디코딩/////
					selectedImage = BitmapFactory.decodeFile(filePath);
					ImageView _image = (ImageView) findViewById(R.id.imageView);
					_image.setImageBitmap(selectedImage);
				}
			}
			break;
		}

		}

	}

	private class ServerCommTask extends AsyncTask<CommServer, Void, String> {
		protected String doInBackground(CommServer... comm) {
			String data = null;
			Log.e("1111", "1111");
			try {
				data = comm[0].getData();
				Log.e("doInBackground", data);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return data;
		}

		protected void onPostExecute(String result) {
			JSONObject jsonobj;
			dialog.dismiss();
			try {
				jsonobj = new JSONObject(result.toString());
				if (jsonobj.getInt("error") == 0) {
					Log.e("insert Account", "success regist");
					Intent intent = new Intent(Reg.this, Dmap.class);
					startActivity(intent);
					finish();
					overridePendingTransition(android.R.anim.fade_in,
							android.R.anim.fade_out);
				} else {
					Log.e("1111", jsonobj.toString());
					alertDialog = new AlertDialog.Builder(Reg.this);
					alertDialog.setTitle("")
							.setMessage(jsonobj.getString("message"))
							.setNeutralButton("확인", null).show();
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
}
