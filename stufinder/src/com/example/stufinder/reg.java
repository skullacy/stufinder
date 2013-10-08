package com.example.stufinder;

import java.net.URL;
import java.net.URLEncoder;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;


import android.util.Log;
import android.view.View;
import android.content.Intent;


public class reg extends Activity implements View.OnClickListener{
	private final String SERVER_ADDRESS = "http://skullacytest.cafe24.com/xe";
	
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
		PickDate.setText(String.format("%04d³â %02d¿ù %02dÀÏ", mYear, mMonth+1, mDay));    
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
			
		}

//    	Intent intent = new Intent(this, dMap.class);
//		startActivity(intent);
	}


