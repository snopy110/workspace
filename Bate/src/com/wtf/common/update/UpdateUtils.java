/**
 * 
 */
package com.wtf.common.update;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;

import com.wtf.common.Constant;
import com.wtf.common.config.Global;

/**
 * @author hp
 * 
 */
public class UpdateUtils {
	private static Context context = null;
	

	public static void alertUpdate(Context context, final Class<?> cls) {
		setContext(context);

		AlertDialog.Builder alert = new AlertDialog.Builder(context);

		alert.setTitle("version update")
				.setMessage("find new version,pls to update it")
				.setPositiveButton("Update",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								Log.d(Constant.TAG_DEBUG, "alertUpdate:Update");
								Intent updteIntent = new Intent(getContext(),
										cls);
								updteIntent.putExtra("appName", "Bate");
								getContext().startService(updteIntent);
								Log.d(Constant.TAG_DEBUG,
										"alertUpdate:Update--to start service");
							}
						})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								Log.d(Constant.TAG_DEBUG, "alertUpdate:Cancel");
								dialog.dismiss();
							}
						});

		alert.create().show();

	}

	/*
	 * ��ʼ��ȫ�ֱ��� ʵ�ʹ��������������serverVersion�ӷ������˻�ȡ����������������activity��ִ��
	 */
	public static void initGlobal(Context context) {
		setContext(context);

		try {
			Global.localVersion = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionCode; // ���ñ��ذ汾��
			Global.serverVersion = 1;// �ٶ��������汾Ϊ2�����ذ汾Ĭ����1
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	

	private static Context getContext() {
		return UpdateUtils.context;
	}

	private static void setContext(Context context) {
		UpdateUtils.context = context;
	}	
}
