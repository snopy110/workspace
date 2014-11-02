/**
 * start
 */
package com.wtf.bate.startup;

import java.util.Iterator;
import java.util.Random;
import java.util.TreeSet;

import android.animation.LayoutTransition;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wtf.R;
import com.wtf.bate.sensor.SensorLogActivity;
import com.wtf.common.update.UpdateActivity;
import com.wtf.common.update.UpdateService;
import com.wtf.common.update.UpdateUtils;
import com.wtf.opengl.OpenGlActivity;

/**
 * @author wtf
 *
 */
@SuppressLint("NewApi")
public class MainActivity extends Activity {

	private final int DCB_RED_MAX = 33;
	private final int DCB_RED_LENGTH = 6;
	private final int DCB_BLUE_MAX = 16;
	private final int DCB_BLUE_LENGTH = 1;
	private final int CHILD_INDEX = 0;

	private Button button_add = null;
	private TextView sub = null;
	private LayoutTransition mTransitioner = null;
	private ViewGroup container = null;
	private LinearLayout llayout = null;
	private Animation animPushUpIn = null;
	private Animation animZoomEnter = null;
	private Animation anim2 = null;
	private Animation anim3 = null;
	private Animation anim4 = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		llayout = (LinearLayout) findViewById(R.id.master_layout);
		button_add = (Button) findViewById(R.id.button_add);

		button_add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				addBate();

			}
		});

		initEvironment();

	}

	private void initEvironment() {
		mTransitioner = new LayoutTransition();
		animPushUpIn = AnimationUtils.loadAnimation(this, R.anim.push_up_in);
		animZoomEnter = AnimationUtils.loadAnimation(this, R.anim.zoom_enter);
		
//		onAnimation();
		addBate();
	}

	private void addBate() {

		container = new LinearLayout(this);
		LinearLayout.LayoutParams subLayoutParams = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

		subLayoutParams.setMargins(5, 5, 5, 5);
		layoutParams.setMargins(10, 10, 10, 10);

		getRedBall(subLayoutParams, layoutParams);

		getBlueBall(subLayoutParams, layoutParams);
		

		llayout.addView(container, CHILD_INDEX);

	}

	private static TreeSet<Integer> getRandom(int max,
			TreeSet<Integer> treeSet, int size) {

		int count = 0;

		if (!treeSet.isEmpty())
			treeSet.clear();

		while (size > treeSet.size()) {

			Random random = new Random();
			int radomInt = random.nextInt(max) + 1;

			treeSet.add(radomInt);
			count++;

		}

		System.out.println("Count is " + count);

		return treeSet;
	}

	private void getRedBall(LinearLayout.LayoutParams subLayoutParams,
			LinearLayout.LayoutParams layoutParams) {

		TreeSet<Integer> dcbRedTreeSet = new TreeSet<Integer>();
		getRandom(DCB_RED_MAX, dcbRedTreeSet, DCB_RED_LENGTH);

		for (Iterator<Integer> iterator = dcbRedTreeSet.iterator(); iterator
				.hasNext();) {
			Integer integer = (Integer) iterator.next();

			sub = new TextView(this);
			sub.setLayoutParams(subLayoutParams);
			sub.setText(getBallString(integer));
			sub.setTextSize(24);
			sub.setTextColor(Color.RED);
			sub.startAnimation(animPushUpIn);

			container.setLayoutParams(layoutParams);
//			container.setOrientation(LinearLayout.HORIZONTAL);
			container.addView(sub);

		}

	}

	private void getBlueBall(LinearLayout.LayoutParams subLayoutParams,
			LinearLayout.LayoutParams layoutParams) {

		TreeSet<Integer> dcbRedTreeSet = new TreeSet<Integer>();
		getRandom(DCB_BLUE_MAX, dcbRedTreeSet, DCB_BLUE_LENGTH);

		for (Iterator<Integer> iterator = dcbRedTreeSet.iterator(); iterator
				.hasNext();) {
			Integer integer = (Integer) iterator.next();

			sub = new TextView(this);
			sub.setLayoutParams(subLayoutParams);
			sub.setText(getBallString(integer));
			sub.setTextSize(24);
			sub.setTextColor(Color.BLUE);
			sub.startAnimation(animZoomEnter);

			container.setLayoutParams(layoutParams);
//			container.setOrientation(LinearLayout.HORIZONTAL);
			container.addView(sub);
		}

	}

	@SuppressLint("NewApi")
	private void onAnimation() {
		long duration = 500;

		mTransitioner.setStagger(LayoutTransition.CHANGE_APPEARING, 30);
		mTransitioner.setStagger(LayoutTransition.CHANGE_DISAPPEARING, 30);

		mTransitioner.setDuration(duration);

		LayoutAnimation.setupCustomAnimations(mTransitioner, this);
		
		llayout.setLayoutTransition(mTransitioner);
	}

	private String getBallString(Integer l) {
		String message = String.valueOf(l);

		if (message.length() == 1) {
			message = "0" + message;
		}

		return message;

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
//		MenuItem item = menu.getItem(0);
//		Intent intent = new Intent();
//		intent.setClass(this, UpdateActivity.class);
//		item.setIntent(intent);
		return true;
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		
		Intent intent = new Intent();
		
		switch (item.getItemId()) {
		case R.id.menu_update_version:
			UpdateUtils.alertUpdate(this, UpdateService.class);
//			alertUpdate();
			break;
			
		case R.id.menu_sensor_log:
			
			intent.setClass(this, SensorLogActivity.class);
			
			startActivity(intent);
			
			break;
			
		case R.id.menu_opengl_cube:
			
			intent.setClass(this, OpenGlActivity.class);
			
			startActivity(intent);
			break;

		default:
			break;
		}
		
		return super.onMenuItemSelected(featureId, item);
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
										MainActivity.this,
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
