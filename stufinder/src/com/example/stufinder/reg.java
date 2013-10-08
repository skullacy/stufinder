package com.example.stufinder;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;

import java.util.Calendar;



import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.net.Uri;
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

import android.util.Log;
import android.view.View;
import android.content.Intent;


public class reg extends Activity implements View.OnClickListener{
	private final String SERVER_ADDRESS = "http://skullacytest.cafe24.com/xe";
	
	private static final String TEMP_PHOTO_FILE = "temp.jpg";
	private static final int REQ_CODE_PICK_IMAGE = 0;
	Bitmap selectedImage;
	
	private Button      PickDate;
	EditText phoneEdit;
	EditText posEdit;
	EditText infoEdit;
	EditText writep;
	
	Button saveBtn;
	private int         mYear;    
	private int         mMonth;    
	private int         mDay;    
	static final int DATE_DIALOG_ID = 0;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.reg);
	    
	    writep = (EditText)findViewById(R.id.writep);
	    phoneEdit = (EditText)findViewById(R.id.phonet);
	    posEdit = (EditText)findViewById(R.id.post);
	    infoEdit = (EditText)findViewById(R.id.infot);
	    ((Button)findViewById(R.id.saved)).setOnClickListener(this);
	    
	    Button photoUp = (Button)findViewById(R.id.photoUpdate);
	    photoUp.setOnClickListener(new OnClickListener(){
	    	
	    	public void onClick(View v){
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
	    	
	    	 private Uri getTempUri(){
	 	    	return Uri.fromFile(getTempFile());
	 	    }
	 	    //����޸𸮿� �ӽ� �̹��� ������ �����Ͽ� �� ������ ��θ� ��ȯ
	 	    private File getTempFile(){
	 	    	if(isSDCARDMOUNTED()){
	 	    		File f = new File(Environment.getExternalStorageDirectory(),
	 	    				TEMP_PHOTO_FILE);
	 	    		try{
	 	    			f.createNewFile();
	 	    		}catch (IOException e){
	 	    			
	 	    		}
	 	    		return f;
	 	    	}else
	 	    		return null;
	 	    }
	 	    //SDī�尡 ����Ʈ �Ǿ� �ִ��� Ȯ��
	 	    private boolean isSDCARDMOUNTED(){
	 	    	String status = Environment.getExternalStorageState();
	 	    	if(status.equals(Environment.MEDIA_MOUNTED))
	 	    		return true;
	 	    	
	 	    	return false;
	 	    }
		   
	    });

	   

	    PickDate = (Button)findViewById(R.id.dateb); 
	    PickDate.setOnClickListener(new View.OnClickListener()
	    {                         
	         
			public void onClick(View v) {                
				showDialog(DATE_DIALOG_ID);
				
			}        
		});
	   
	    final Calendar c = Calendar.getInstance();                 
		mYear = c.get(Calendar.YEAR);        
		mMonth = c.get(Calendar.MONTH);        
		mDay = c.get(Calendar.DAY_OF_MONTH);                 
		updateDisplay();    
		}        
	
		private void updateDisplay()   
		{
		PickDate.setText(String.format("%04d�� %02d�� %02d��", mYear, mMonth+1, mDay));    
		}
	
		private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener()
		{                                 
		            
			public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) {                    
				//                     
				mYear = year;                    
				mMonth = monthOfYear;                    
				mDay = dayOfMonth;                    
				updateDisplay();                
				}
          
			};     
		
		@Override    
		protected Dialog onCreateDialog(int id) 
		{        
			switch(id)
			{        
			case DATE_DIALOG_ID:            
				return new DatePickerDialog(this, mDateSetListener, mYear, mMonth, mDay);        
			}        
			return null;    
		}
		
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(this, dMap.class);
			startActivity(intent);
		}
		
		
		
    	
	
//��Ƽ��Ƽ�� �����Ͽ����� �̹��� ���ä�������
	protected void onActivityResult(int requestCode, int resultCode, Intent imageData){
		super.onActivityResult(requestCode, resultCode, imageData);
		
		switch(requestCode){
	    	case REQ_CODE_PICK_IMAGE:
	    	{
	    		if(resultCode == RESULT_OK)
	    		{
	    			if(imageData != null)
	    			{
	    				String filePath = Environment.getExternalStorageDirectory() + "/temp.jpg";
	    				//�α�Ĺ���� ��� Ȯ��
	    				System.out.println("path" + filePath);
	    				//temp.jpg������ Bitmap���� ���ڵ�/////
	    				selectedImage = BitmapFactory.decodeFile(filePath);
	    				ImageView _image = (ImageView)findViewById(R.id.imageView);
	    				_image.setImageBitmap(selectedImage);
	    			}
	    		}
	    	break;
	    	}
		
		}
		
	}
}
