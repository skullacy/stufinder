package com.example.stufinder.util;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;

public class StufinderUtil {
	public static String getDefaultMsg(String phoneNum, int lgselect, String title){
		String whoami = null;
		if(lgselect == 0) whoami = "������";
		else if(lgselect == 1) whoami = "�н���";
		return "Stufinder ���ÿ� ��ϵ� '"+title+"' ��ǰ�� "+whoami+"�Դϴ�. ��ȭ��ȣ "+phoneNum+" ���� �����帮�ڽ��ϴ�.";
	}
	
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
