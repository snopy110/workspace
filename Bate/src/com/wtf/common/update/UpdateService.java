/**
 * Service.update version
 */
package com.wtf.common.update;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.wtf.common.Constant;

/**
 * @author wtf
 * 
 */
public class UpdateService extends Service {

	private String appName = "";

	private AsyncTask<String, Integer, String> updateAsyncTask = new UpdateAsyncTask(
			this, appName);

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Service#onBind(android.content.Intent)
	 */
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "UpdateService:onBind", Toast.LENGTH_SHORT).show();
		Log.d(Constant.TAG_DEBUG, "UpdateService:onBind");
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
//		Toast.makeText(this, "UpdateService:onCreate", Toast.LENGTH_SHORT)
//				.show();
		Log.d(Constant.TAG_DEBUG, "UpdateService:onCreate");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
//		Toast.makeText(this, "UpdateService:onStartCommand", Toast.LENGTH_SHORT)
//				.show();
		Log.d(Constant.TAG_DEBUG, "UpdateService:onStartCommand");

		// 获取传值
		appName = intent.getStringExtra("appName");
		
		//start down by asyctask
		updateAsyncTask.execute("start");
		
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	@Deprecated
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
//		Toast.makeText(this, "UpdateService:onStart", Toast.LENGTH_SHORT)
//				.show();
		Log.d(Constant.TAG_DEBUG, "onStart");
	}

}
