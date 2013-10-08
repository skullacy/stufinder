package com.example.stufinder.util;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;

public class getGoogleAccount {
	public static String getAccount(Context context){
		
		AccountManager manager = AccountManager.get(context);
		Account[] accounts =  manager.getAccounts();
		for(Account account : accounts) {
			if(account.type.equals("com.google")){		//�̷��� ���� ���� ���� ����
				return account.name;
			}
		}
		return null;
	}
	
}
