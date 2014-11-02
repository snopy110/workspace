package com.wtf.bate.sensor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.text.AndroidCharacter;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.wtf.R;
import com.wtf.common.CommonFile;
import com.wtf.common.CommonUtiles;
import com.wtf.common.Constant;
import com.wtf.common.config.Global;
import com.wtf.common.update.UpdateUtils;

public class SensorLogActivity extends Activity {

	private SensorManager sensorManager = null;

	CommonFile sensorLogFile = null;

	ListView listView = null;

	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		listView = new ListView(this);
		
		setContentView(listView);

		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

		List<Sensor> sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);

		sensorLogFile = CommonFile.getSensorLogfile(Global.sensorLogDir,
				Global.sensorLogName, Global.fileTypeTxt);
		try {
	
			sensorLogFile.wirte(sensorList);

		} catch (IOException e) {
			Log.d(Constant.TAG_DEBUG, "sensorLogFile to wirte false!!!");


			CommonUtiles.ShowAlterDailogByStr(this,
					R.string.alter_title_write_false, e.toString());

			e.printStackTrace();
		}

	}

	@Override
	protected void onResume() {
		super.onResume();

		try {
			List<String> list = sensorLogFile.read();

			listView.setAdapter(new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, list));

		} catch (IOException e) {
			Log.d(Constant.TAG_DEBUG, "sensorLogFile to read false!!!");
			Toast.makeText(this, "sensorLogFile to read false!!!",
					Toast.LENGTH_SHORT).show();
			CommonUtiles.ShowAlterDailogByStr(this,
					R.string.alter_title_read_false, e.toString());

			e.printStackTrace();
		}

	}
}
