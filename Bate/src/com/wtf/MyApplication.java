/**
 * application
 */
package com.wtf;

import android.app.Application;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

import com.wtf.common.Constant;
import com.wtf.common.config.Global;

/**
 * @author wtf
 *
 */
public class MyApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();

		try {
			 Global.localVersion = getApplicationContext().getPackageManager().getPackageInfo(
					getPackageName(), 0).versionCode;
			 //TODO
			 Global.serverVersion = 2;
			 
		} catch (NameNotFoundException e) {
			Log.e(Constant.TAG_ERROR, "Application Name Not Found Exception");
			e.printStackTrace();
		}

	}

}
