package com.wtf.common;

import com.wtf.R;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

public class CommonUtiles {
	
	public static void ShowAlterDailogByStr(Context context,int title, final String str){
		
		Builder alter = new Builder(context);
		
		alter.setTitle(context.getString(title))
		.setMessage(str)
		.setPositiveButton(context.getString(R.string.button_confirm), new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		}).show();
		
		
		
	}

}
