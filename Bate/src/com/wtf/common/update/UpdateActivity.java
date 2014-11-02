package com.wtf.common.update;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.wtf.common.config.Global;

/**
 * @author wtf
 * 
 */
public class UpdateActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//TODO
		checkVersion();

	}

	public void checkVersion() {
//		if (Global.localVersion < Global.serverVersion) {TODO
		if (true) {
			alertUpdate();
		} else {
			//cheanUpdateFile();
		}

	}

	public void alertUpdate() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("version update")
				.setMessage("find new version,pls to update it")
				.setPositiveButton("Update",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								Intent updteIntent = new Intent(
										UpdateActivity.this,
										UpdateService.class);
								updteIntent.putExtra("appName", "Bate");
								startService(updteIntent);
							}
						})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});

		alert.create().show();

	}
	
	

}
