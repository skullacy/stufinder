package com.example.stufinder.util;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;

public class getGoogleAccount {
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
