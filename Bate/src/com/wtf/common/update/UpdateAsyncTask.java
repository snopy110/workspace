package com.wtf.common.update;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.wtf.R;
import com.wtf.bate.startup.MainActivity;
import com.wtf.common.Constant;
import com.wtf.common.config.Global;

public class UpdateAsyncTask extends AsyncTask<String, Integer, String> {

	private Context context;
	private NotificationManager updateNotificationManager = null;
	private Notification updateNotification = null;
	private PendingIntent updatePendingIntent = null;
	private Intent updateIntent = null;

	private File updateFile = null;
	private File updateDir = null;

	private int downloadCount = 0;
	private int currentSize = 0;
	private long totalSize = 0;
	private int updateTotalSize = 0;
	
	private String appName = "";
	
	private int result = 0;

	public UpdateAsyncTask(Context context,String appName) {

		this.context = context;
		this.appName = appName;
//		this.updateNotificationManager = updateNotificationManager;
//		this.updateNotification = updateNotification;
//		this.updatePendingIntent = updatePendingIntent;

	}

	@Override
	protected void onPreExecute() {
		// do it in main thread
		super.onPreExecute();

		// �����ļ�
		if (android.os.Environment.MEDIA_MOUNTED.equals(android.os.Environment
				.getExternalStorageState())) {
			updateDir = new File(Environment.getExternalStorageDirectory(),
					Global.downloadVersionDir);
			updateFile = new File(updateDir.getPath() + appName +".apk");
		}

		this.updateNotificationManager = (NotificationManager) context
				.getSystemService(context.NOTIFICATION_SERVICE);
		this.updateNotification = new Notification();

		// �������ع����У����֪ͨ�����ص�������
		updateIntent = new Intent(context, MainActivity.class);
		updatePendingIntent = PendingIntent.getActivity(context, 0,
				updateIntent, 0);
		// ����֪ͨ����ʾ����
		updateNotification.icon = R.drawable.ic_launcher;
		updateNotification.tickerText = "��ʼ����";
		updateNotification.setLatestEventInfo(context, "BateӦ������", "0%",
				updatePendingIntent);
		// ����֪ͨ
		updateNotificationManager.notify(0, updateNotification);
		
		Toast.makeText(context, "onPreExecute", Toast.LENGTH_SHORT).show();

	}

	@Override
	protected String doInBackground(String... params) {
		// do it in sub thread
		Log.d(Constant.TAG_DEBUG, "doInBackground----start");

		try {
			// ����Ȩ��;
			if (!updateDir.exists()) {
				boolean mkdirs = updateDir.mkdirs();
				Log.d(Constant.TAG_DEBUG, "updateDir:" + updateDir);
				Log.d(Constant.TAG_DEBUG, "mkdirs:" + mkdirs);
			}
			if (!updateFile.exists()) {
				boolean createNewFile = updateFile.createNewFile();
				Log.d(Constant.TAG_DEBUG, "updateFile:" + updateFile);
				Log.d(Constant.TAG_DEBUG, "createNewFile:" + createNewFile);
			}
			// ���غ�������QQΪ����
			long downloadSize = downloadUpdateFile();
			if (downloadSize > 0) {
				// ���سɹ�
				result = 1;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		Log.d(Constant.TAG_DEBUG, "doInBackground----end");
		return null;
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		// do it in main thread
		super.onProgressUpdate(values);

		updateNotification.setLatestEventInfo(context, "��������", (int) totalSize
				* 100 / updateTotalSize + "%", updatePendingIntent);
		updateNotificationManager.notify(0, updateNotification);
		
		Toast.makeText(context, "onProgressUpdate", Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onPostExecute(String result) {
		// do it in main thread
		super.onPostExecute(result);
		
		 switch(this.result){
         case 1:
             //�����װPendingIntent
             Uri uri = Uri.fromFile(updateFile);
             Intent installIntent = new Intent(Intent.ACTION_VIEW);
             installIntent.setDataAndType(uri, "application/vnd.android.package-archive");
             updatePendingIntent = PendingIntent.getActivity(context, 0, installIntent, 0);
             
             updateNotification.defaults = Notification.DEFAULT_SOUND;//�������� 
             updateNotification.setLatestEventInfo(context, "�Ϻ�����", "�������,�����װ��", updatePendingIntent);
             updateNotificationManager.notify(0, updateNotification);
             
             //ֹͣ����TODO
//           stopSelf();
             break;
         case 0:
             //����ʧ��
             updateNotification.setLatestEventInfo(context, "�Ϻ�����", "�������,�����װ��", updatePendingIntent);
             updateNotificationManager.notify(0, updateNotification);
             break;
         default:
        	//ֹͣ����TODO
//             stopSelf();
     }
	}

	@Override
	protected void onCancelled() {
		// TODO Auto-generated method stub
		super.onCancelled();
	}

	@SuppressLint("NewApi")
	@Override
	protected void onCancelled(String result) {
		// TODO Auto-generated method stub
		super.onCancelled(result);
	}

	public long downloadUpdateFile() throws Exception {

		HttpURLConnection httpConnection = null;
		InputStream is = null;
		FileOutputStream fos = null;

		try {
			URL url = new URL(Constant.DOWNLOADURL);
			httpConnection = (HttpURLConnection) url.openConnection();
			httpConnection
					.setRequestProperty("User-Agent", "PacificHttpClient");
			if (currentSize > 0) {
				httpConnection.setRequestProperty("RANGE", "bytes="
						+ currentSize + "-");
			}
			httpConnection.setConnectTimeout(10000);
			httpConnection.setReadTimeout(20000);
			updateTotalSize = httpConnection.getContentLength();
			if (httpConnection.getResponseCode() == 404) {
				throw new Exception("fail!");
			}
			is = httpConnection.getInputStream();
			fos = new FileOutputStream(updateFile, false);
			byte buffer[] = new byte[4096];
			int readsize = 0;
			while ((readsize = is.read(buffer)) > 0) {
				fos.write(buffer, 0, readsize);
				totalSize += readsize;
				// Ϊ�˷�ֹƵ����֪ͨ����Ӧ�óԽ����ٷֱ�����10��֪ͨһ��
				if ((downloadCount == 0)
						|| (int) (totalSize * 100 / updateTotalSize) - 10 > downloadCount) {
					downloadCount += 10;

					// TODO update UI
					publishProgress(downloadCount);
				}
			}
		} finally {
			if (httpConnection != null) {
				httpConnection.disconnect();
			}
			if (is != null) {
				is.close();
			}
			if (fos != null) {
				fos.close();
			}
		}
		return totalSize;
	}

}
