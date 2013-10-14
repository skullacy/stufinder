package com.example.stufinder.util;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;

public class StufinderUtil {
	public static String getDefaultMsg(String phoneNum, int lgselect, String title){
		String whoami = null;
		if(lgselect == 0) whoami = "습득자";
		else if(lgselect == 1) whoami = "분실자";
		return "Stufinder 어플에 등록된 '"+title+"' 물품의 "+whoami+"입니다. 전화번호 "+phoneNum+" 으로 연락드리겠습니다.";
	}
	
	public static String getAccount(Context context){
		
		AccountManager manager = AccountManager.get(context);
		Account[] accounts =  manager.getAccounts();
		for(Account account : accounts) {
			if(account.type.equals("com.google")){		//이러면 구글 계정 구분 가능
				return account.name;
			}
		}
		return null;
	}
}
