package com.example.stufinder.util;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

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
	
	public static void setListViewHeight(ListView listView){
		ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
	}
}
