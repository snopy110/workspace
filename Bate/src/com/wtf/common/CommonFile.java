package com.wtf.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.hardware.Sensor;
import android.os.Environment;
import android.util.Log;

import com.wtf.R;
import com.wtf.common.config.Global;

public class CommonFile {

	private static CommonFile commonFile = null;
	private File fileDir = null;
	private File fileName = null;
	private FileOutputStream fos = null;

	private String strDir = Global.commonLogDir;
	private String strName = Global.commonLogName;
	private String strType = Global.fileTypeTxt;

	private CommonFile() {

	}

	public static CommonFile getSensorLogfile() {

		if (commonFile == null) {

			commonFile = new CommonFile();

		}

		return commonFile;

	}

	public static CommonFile getSensorLogfile(String dir, String name,
			String type) {
		commonFile = getSensorLogfile();

		commonFile.strDir = dir;
		commonFile.strName = name;
		commonFile.strType = type;

		return commonFile;

	}

	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	public void preWirte() throws IOException {

		if (android.os.Environment.MEDIA_MOUNTED.equals(android.os.Environment
				.getExternalStorageState())) {

			fileDir = new File(Environment.getExternalStorageDirectory(),strDir);
			fileName = new File(fileDir.getPath() + strName + strType);

			if (!fileDir.exists()) {
				boolean mkdirs = fileDir.mkdirs();
				Log.d(Constant.TAG_DEBUG, "strDir:" + fileDir);
				Log.d(Constant.TAG_DEBUG, "mkdirs:" + mkdirs);
				Log.d(Constant.TAG_DEBUG, "path:" + fileDir.getPath());
			}

			if (!fileName.exists()) {
				boolean createNewFile = fileName.createNewFile();
				Log.d(Constant.TAG_DEBUG, "createNewFile:" + strDir + strName
						+ strType);
				Log.d(Constant.TAG_DEBUG, "createNewFile:" + createNewFile);
				Log.d(Constant.TAG_DEBUG, "path:" + fileName.getPath());

			}

		}

	}
	
	/**
	 * 
	 * @throws FileNotFoundException
	 */
	public void preRead() throws FileNotFoundException{
		fileDir = new File(Environment.getExternalStorageDirectory(),strDir);
		fileName = new File(fileDir.getPath()+ strName + strType);
		
		if (!fileDir.exists() || !fileName.exists()) {
			throw new FileNotFoundException();
		}
	}

	/**
	 * 
	 * @param sensorList
	 * @return true/false
	 * @throws IOException
	 */
	public void wirte(List<Sensor> sensorList) throws IOException {
		System.out.print("preWirte..........");
		preWirte();
		

		fos = new FileOutputStream(fileName, true);
		StringBuffer buffer = new StringBuffer(sensorList.size());
		for (Iterator iterator = sensorList.iterator(); iterator.hasNext();) {
			Sensor sensor = (Sensor) iterator.next();

			buffer.append(Constant.SENSORLOGTITLE + sensor.getName());

			fos.write(buffer.toString().getBytes());
		}

		fos.flush();
		fos.close();
		System.out.print("have preWirted..........");

	}

	public List<String> read() throws IOException {
		
		preRead();
		
		FileInputStream fis = new FileInputStream(fileName);
		
		byte[] buffer = new byte[1024];
		List<String> result = new ArrayList<String>();
		
		while (fis.read(buffer)!= -1) {
			
			result.add(new String(buffer));
			buffer = new byte[1024];
		}
		
		return result;

	}

}
