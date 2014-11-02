package com.wtf.opengl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.WindowManager;

import com.wtf.R;


public class OpenGlActivity extends Activity {
	
	static int width = 0;
	static int height = 0;
	
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// ��������������
		mGLSurfaceView = new TouchSurfaceView(this);
		mGLSurfaceView.requestFocus();
		mGLSurfaceView.setFocusableInTouchMode(true);
		mGLSurfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
		
		setContentView(mGLSurfaceView);
		
		WindowManager wm = (WindowManager) getSystemService(this.WINDOW_SERVICE);
		
		Point outSize = new Point();
		wm.getDefaultDisplay().getSize(outSize);
		this.width = outSize.x;
		this.height = outSize.y;
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		mGLSurfaceView.onResume();
	}

	@Override
	protected void onPause() {
		// Ideally a game should implement onResume() and onPause()
		// to take appropriate action when the activity looses focus
		super.onPause();
		mGLSurfaceView.onPause();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		((TouchSurfaceView) mGLSurfaceView).onDestroy();
		mGLSurfaceView = null;
		System.gc();
		
		
	}
	

	private GLSurfaceView mGLSurfaceView;
	
	
	/**
	 * ���ӵĻ���
	 */
	class TouchSurfaceView extends GLSurfaceView {
		
		public int getDirect1() {
			return direct1;
		}

		public int getDirect2() {
			return direct2;
		}
		// ����1�ķ���
		public int direct1 = 0;
		// ����2�ķ���
		public int direct2 = 0;
		
		private Timer timer = new Timer();
		myTimerTask task = new myTimerTask(true);;
		
		/**
		 * ���ӻ����Ĺ���������
		 * 
		 * @param context �����ģ���ǰActivity��
		 */
		public TouchSurfaceView(Context context) {
			super(context);
			mRenderer = new CubeRenderer(context);
			this.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
			setRenderer(mRenderer);
			setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

		}
		
		/**
		 * ����ʱ�Ĵ���
		 */
		public void onDestroy(){
			timer.cancel();
			timer = null;
		}

		@Override
		public boolean onTouchEvent(MotionEvent e) {

			switch (e.getAction()) {
			// down��ʱ������Ч��
			case MotionEvent.ACTION_DOWN:
				if (!task.isRun() && !task.isEndRun() && task.isFirstRun()) {
					// ������ӵķ���
					Double directRandom1 = Math.random()*3+1;
					Double directRandom2 = Math.random()*3+1;
					direct1 = directRandom1.intValue();
					direct2 = directRandom2.intValue();
					
					// ����1������2������ͬʱ��x�����ƶ�
					while (direct1 == 2 && direct1 == direct2) {
						
						Double temp = Math.random()*3+1;
						direct2 = temp.intValue();
					}
					
					// ��ʼ�Ƕ�
					Double angle1 = Math.random()*46+45;
					mRenderer.mAngle1 = angle1.floatValue();
					Double angle2 = Math.random()*46+45;
					mRenderer.mAngle2 = angle2.floatValue();
					
					// ������ʱ��
					task.setRun(true);
					task.setEndRun(false);
					if (task.isFirstRun()){
						timer.schedule(task, 100, 100);
						task.setFirstRun(false);
					}
					break;
				}
				
			case MotionEvent.ACTION_UP:
				
				// �رն�ʱ��
				task.endInit();
				break;

			}
			return true;
		}
		/**
		 * ��ʱ���������ࡣ
		 * 
		 * @author renyuming
		 * @version 1.0
		 */
		private class myTimerTask extends TimerTask{
			private boolean run = false;
			private boolean firstRun = true;
			private float moveY = 0.0f;
			private float move1x = 0.0f;
			private float move1y = 0.0f;

			private float move2x = 0.0f;
			private float move2y = 0.0f;

			private boolean move1Dir = false;
			private boolean move2Dir = true;



			private boolean endRun = false;
			private Double m2AngleX90 = 0.0;
			private Double m1AngleX90 = 0.0;
			private float x1Dif90	= 0.0f;
			private float x2Dif90 = 0.0f;
			
			private float endMove = 2.0f;
			private float BaseAngle = 75.0f;
			private static final String TAG = "OpenGlActivity---myTimerTask";
			
			public float getMove1x() {
				return move1x;
			}
			public float getMove1y() {
				return move1y;
			}			
			
			public float getMove2x() {
				return move2x;
			}	
			
			public float getMove2y() {
				return move2y;
			}			
			
			public float getMoveY() {
				return moveY;
			}

			public void setFirstRun(boolean firstRun) {
				this.firstRun = firstRun;
			}

			public boolean isFirstRun() {
				return firstRun;
			}

			public boolean isRun() {
				return run;
			}

			public void setRun(boolean run) {
				this.run = run;
			}

			public myTimerTask(boolean firstRun) {
				this.firstRun = firstRun;
			}
			
			public boolean isEndRun() {
				return endRun;
			}

			public void setEndRun(boolean endRun) {
				this.endRun = endRun;
			}		
			
			public void run() {
				
				// ���еĳ���
				if (isRun()) {
					
					// ÿ������ĽǶ�
					long random1 = Math.round(Math.random()*79+21);
					long random2 = Math.round(Math.random()*79+21);
					while (random1 == random2) {
						random2 = Math.round(Math.random()*59+1);
					}
					
					// �����ǶȲ��ܹ���
					if (mRenderer.mAngle1 > (360 * 20)) {
						mRenderer.mAngle1 = mRenderer.mAngle1 - (360 * 10);
					} else {
						mRenderer.mAngle1 += (BaseAngle + random1);
					}
					if (mRenderer.mAngle2 > (360 * 20)) {
						mRenderer.mAngle2 = mRenderer.mAngle2 - (360 * 10);
					}else {
						mRenderer.mAngle2 += (BaseAngle + random2);
					}
					
					// Y������
					if (moveY < 0.5f) {
						moveY += 0.0f;
					}else if (moveY >= 0.5f) {
						moveY -= 0.0f;
					}
					
					Log.d("move", "move2Dir:" + String.valueOf(move2Dir));
					Log.d("move", "move2x:" + String.valueOf(move2x));
					
					float max_move_1_3_xiangxian_Direct1 = 0.9f;
					float min_move_1_3_xiangxian_Direct1 = -0.4f;
					float max_move_x_xiangxian_Direct1 = 1.35f;
					float min_move_x_xiangxian_Direct1 = -0.4f;
					float max_move_y_xiangxian_Direct1 = 0.9f;
					float min_move_y_xiangxian_Direct1 = -0.9f;
					float max_move_2_4_xiangxian_Direct1 = 0.85f;
					float min_move_2_4_xiangxian_Direct1 = -0.35f;
					
					float max_move_1_3_xiangxian_Direct2 = 0.35f;
					float min_move_1_3_xiangxian_Direct2 = -0.80f;
					float max_move_x_xiangxian_Direct2 = 0.35f;
					float min_move_x_xiangxian_Direct2 = -1.4f;
					float max_move_y_xiangxian_Direct2 = 0.9f;
					float min_move_y_xiangxian_Direct2 = -0.9f;
					float max_move_2_4_xiangxian_Direct2 = 0.35f;
					float min_move_2_4_xiangxian_Direct2 = -0.85f;
					
					
					// д�Ϸ����ƶ�
					switch (direct2) {
					case 1: // 1,3�����ƶ�
						
						if (move2x > max_move_1_3_xiangxian_Direct2){
							move2Dir = false;
						}else if (move2x < min_move_1_3_xiangxian_Direct2) {
							move2Dir = true;
						}
						
						if (move2Dir) {
							
							move2x += 0.1f;
							move2y = move2x;
						}else {
							
							move2x -= 0.1f;
							move2y = move2x;
						}
						break;
					case 2://x�᷽�򣬺����ƶ�
						
						if (move2x > max_move_x_xiangxian_Direct2){
							move2Dir = false;
						}else if (move2x < min_move_x_xiangxian_Direct2) {
							move2Dir = true;
						}
						
						if (move2Dir) {
							
							move2x += 0.1f;
						}else {
							
							move2x -= 0.1f;
						}					
						break;
					case 3:// y�����·����ƶ�
						
						if (move2y > max_move_y_xiangxian_Direct2) {
							move2Dir = false;
						}else if (move2y <= min_move_y_xiangxian_Direct2) {
							move2Dir = true;
						}
						if (move2Dir){
							move2y += 0.1f;
						}else {
							move2y -= 0.1f;
						}
						break;
					case 4:// 2,4�����ƶ�
						
						if (move2x > max_move_2_4_xiangxian_Direct2){
							move2Dir = false;
						}else if (move2x < min_move_2_4_xiangxian_Direct2) {
							move2Dir = true;
						}
						
						if (move2Dir) {
							
							move2x += 0.1f;
							move2y = -move2x;
						}else {
							
							move2x -= 0.1f;
							move2y = -move2x;
						}						
						break;

					default:
						break;
					}
					
					
					switch (direct1) {
					case 1: // 1,3�����ƶ�
						if (move1x >= max_move_1_3_xiangxian_Direct1) {
							move1Dir = false;
						}else if (move1x <= min_move_1_3_xiangxian_Direct1){
							move1Dir = true;
						}
						if (move1Dir) {
							
							move1x += 0.1f;
							move1y = move1x;
						}else {
							
							move1x -= 0.1f;
							move1y = move1x;
						}
						
						break;
					case 2://x�᷽�򣬺����ƶ�
						if (move1x >= max_move_x_xiangxian_Direct1) {
							move1Dir = false;
						}else if (move1x <= min_move_x_xiangxian_Direct1){
							move1Dir = true;
						}
						if (move1Dir) {
							
							move1x += 0.1f;
						}else {
							
							move1x -= 0.1f;
						}
						
						break;
					case 3:// y�����·����ƶ�
						if (move1y > max_move_y_xiangxian_Direct1) {
							move1Dir = false;
						}else if (move1y <= min_move_y_xiangxian_Direct1) {
							move1Dir = true;
						}
						if (move1Dir){
							move1y += 0.1f;
						}else {
							move1y -= 0.1f;
						}
						
						break;
					case 4:// 2,4�����ƶ�
						if (move1x >= max_move_2_4_xiangxian_Direct1) {
							move1Dir = false;
						}else if (move1x <= min_move_2_4_xiangxian_Direct1){
							move1Dir = true;
						}
						if (move1Dir) {
							
							move1x += 0.1f;
							move1y = -move1x;
						}else {
							
							move1x -= 0.1f;
							move1y = -move1x;
						}
						
						break;

					default:
						break;
					}
					// ���󻭲�����
					requestRender();
				}else{
					
					// ֹͣ�ĳ��ϣ����е�������
					if (endRun) {
						
						// ȡ��ֹͣʱ�ĽǶȣ������Ԥ��ϣ���ﵽ�ĽǶ�
						if (m1AngleX90.compareTo(new Double(0.0)) == 0) {
							m1AngleX90 = (Math.rint(mRenderer.mAngle1/90)*90);
							Double mAngle1 = Math.rint(mRenderer.mAngle1);
							x1Dif90 = m1AngleX90.floatValue() - mAngle1.floatValue();
						}
						if (m2AngleX90.compareTo(new Double(0.0)) == 0) {
							m2AngleX90 = (Math.rint(mRenderer.mAngle2/90)*90);
							Double mAngle2 = Math.rint(mRenderer.mAngle2);
							x2Dif90 = m2AngleX90.floatValue() - mAngle2.floatValue();
						}
						// ���е���
						endRun = adjustXYAxis();

						// ���󻭲�����
						requestRender();
					}else {
						
						// ���������Ժ�Ĵ����õ����Ľ�������ص�ǰһ���档
						
						if (direct1 == direct2 && direct2 == 0) {
							return;
						}
						Double circuit1_90 = Math.rint(mRenderer.mAngle1/90);
						Double circuit2_90 = Math.rint(mRenderer.mAngle2/90);
						StringBuffer sb = new StringBuffer();
						
						Log.d("data", "=====================================");
						Log.d("data", "mAngle1:" + mRenderer.mAngle1);
						Log.d("data", "mAngle2:" + mRenderer.mAngle2);
						Log.d("data", "direct1:" + direct1);
						Log.d("data", "direct2:" + direct2);
						Log.d("data", "circuit1_90:" + circuit1_90);
						Log.d("data", "circuit2_90:" + circuit2_90);
						Log.d("data", "=====================================");

						getDiceResult(circuit1_90, sb, direct1);
						getDiceResult(circuit2_90, sb, direct2);
						
						Log.d("data", sb.toString());
						direct1 = 0;
						direct2 = 0;
						
						onPause();
						try {
							Thread.sleep(3000);
						} catch (InterruptedException e) {
							Log.d(TAG, e.getMessage());
						}
						Intent intent = getIntent();
						intent.putExtra("data", sb.toString());
						setResult(RESULT_OK, intent);
						finish();
					}
				}
				
			}

			/**
			 * �õ����ӵĽ����
			 * @param dice ����ID
			 * @param sb   ���
			 * @param direct ���ӵķ���
			 */
			private void getDiceResult(Double dice, StringBuffer sb, int direct) {
				if (!sb.toString().equalsIgnoreCase("")) {
					sb.append("<br>");
				}
				switch (direct) {
				case 1:
					//����ת
					int round1 = dice.intValue()%4;
					switch (round1) {
					case 1:
						sb.append("DICE:5#");
						break;
					case 2:
						sb.append("DICE:1#");
						break;
					case 3:
						sb.append("DICE:2#");
						break;
					case 0:
						sb.append("DICE:6#");
						break;								
					default:
						sb.append("DICE:0#");
						break;
					}
					break;
				case 2:
					//����ת
					int round2 = dice.intValue()%4;
					switch (round2) {
					case 1:
						sb.append("DICE:2#");
						break;
					case 2:
						sb.append("DICE:1#");
						break;
					case 3:
						sb.append("DICE:5#");
						break;
					case 0:
						sb.append("DICE:6#");
						break;								
					default:
						sb.append("DICE:0#");
						break;
					}							
					break;
				case 3:
					//����ת
					int round3 = dice.intValue()%4;
					switch (round3) {
					case 1:
						sb.append("DICE:4#");
						break;
					case 2:
						sb.append("DICE:1#");
						break;
					case 3:
						sb.append("DICE:3#");
						break;
					case 0:
						sb.append("DICE:6#");
						break;								
					default:
						sb.append("DICE:0#");
						break;
					}	
					break;
				case 4:
					//����ת
					int round4 = dice.intValue()%4;
					switch (round4) {
					case 1:
						sb.append("DICE:3#");
						break;
					case 2:
						sb.append("DICE:1#");
						break;
					case 3:
						sb.append("DICE:4#");
						break;
					case 0:
						sb.append("DICE:6#");
						break;								
					default:
						sb.append("DICE:0#");
						break;
					}
					break;

				default:
					break;
				}
			}
			
			/**
			 * ֹͣ��Ч�Ĵ���
			 */
			public void endInit(){
				
				run = false;
				endRun = true;
				m1AngleX90 = 0.0;
				m2AngleX90 = 0.0;
				
				// ֹͣ��ʱ�򣬽Ƕ�С��90���ʱ������ı�Ƕ�ֵ��
				if (mRenderer.mAngle1 < 90){
					Double d1 = Math.random()*179+1;
					mRenderer.mAngle1 += d1.floatValue();
				}
				
				if (mRenderer.mAngle2 < 90){
					Double d2 = Math.random()*179+1;
					mRenderer.mAngle2 += d2.floatValue();
				}
			}
			/**
			 * x,y��ĵ�������
			 * 
			 * @return true:��Ҫ��������/false:����Ҫ����
			 */
			private boolean adjustXYAxis() {
				// x,yƽ�����
				boolean result  = true;
				
				if (mRenderer.mAngle1 != (m1AngleX90.floatValue())) {
					if (Math.abs(x1Dif90) > 15.0f) {
						// ����15��ĳ��Ͻ���΢��
						if (x1Dif90 > 0){
							// �ڴ�ֵ��ĳ��ϣ���ǰֵС��
							mRenderer.mAngle1 += endMove;
							x1Dif90 -= endMove;
						}else {
							// �ڴ�ֵС�ĳ��ϣ���ǰֵ��
							mRenderer.mAngle1 -= endMove;
							x1Dif90 += endMove;
						}
					}else {
						// ������15��ĳ���һ�ε�����λ
						if (x1Dif90 > 0){
							// �ڴ�ֵ��ĳ��ϣ���ǰֵС��
							mRenderer.mAngle1 += x1Dif90;
							x1Dif90 -= x1Dif90;
						}else {
							// �ڴ�ֵС�ĳ��ϣ���ǰֵ��
							mRenderer.mAngle1 += x1Dif90;
							x1Dif90 -= x1Dif90;
						}
					}
				}

				if (mRenderer.mAngle2 != (m2AngleX90.floatValue())) {
					if (Math.abs(x2Dif90) > 15.0f) {
						// ����15��ĳ��Ͻ���΢��
						if (x2Dif90 > 0){
							// �ڴ�ֵ��ĳ��ϣ���ǰֵС��
							mRenderer.mAngle2 += endMove;
							x2Dif90 -= endMove;
						}else {
							// �ڴ�ֵС�ĳ��ϣ���ǰֵ��
							mRenderer.mAngle2 -= endMove;
							x2Dif90 += endMove;
						}
					}else {
						// ������15��ĳ���һ�ε�����λ
						if (x2Dif90 > 0){
							// �ڴ�ֵ��ĳ��ϣ���ǰֵС��
							mRenderer.mAngle2 += x2Dif90;
							x2Dif90 -= x2Dif90;
						}else {
							// �ڴ�ֵС�ĳ��ϣ���ǰֵ��
							mRenderer.mAngle2 += x2Dif90;
							x2Dif90 -= x2Dif90;
						}
					}
				}
				
				if (x2Dif90 == x1Dif90 && x2Dif90 == 0.0f) {
					result = false;
				}
				
				return result;
			}
			
			
		}

		/**
		 * Render a cube.
		 */
		private class CubeRenderer implements GLSurfaceView.Renderer {
			
			public CubeRenderer(Context context) {
				mContext = context;
				mCube = new Cube();
			}

			public void onDrawFrame(GL10 gl) {

				gl.glTexEnvx(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE,
						GL10.GL_MODULATE);
				gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

				gl.glMatrixMode(GL10.GL_PROJECTION);
				gl.glLoadIdentity();
				gl.glTranslatef(-0.5f, task.getMoveY(), 0.0f);
				
				switch (direct1) {
				case 1:
					gl.glTranslatef(task.getMove1x(), task.getMove1y(), 0.0f);
					gl.glRotatef(mAngle1, 1, 0, 0);//����ת
					break;
				case 2:
					gl.glTranslatef(task.getMove1x(), task.getMove1y(), 0.0f);
					gl.glRotatef(mAngle1, -1, 0, 0);//����ת
					break;
				case 3:
					gl.glTranslatef(task.getMove1x(), task.getMove1y(), 0.0f);
					gl.glRotatef(mAngle1, 0, 1, 0);//����ת
					break;
				case 4:
					gl.glTranslatef(task.getMove1x(), task.getMove1y(), 0.0f);
					gl.glRotatef(mAngle1, 0, -1, 0);//����ת
				default:
					break;
				}

				gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
				gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
				mCube.draw(gl);
				gl.glLoadIdentity();
				gl.glTranslatef(0.5f, task.getMoveY(), 0.0f);
				
				switch (direct2) {
				case 1:
					gl.glTranslatef(task.getMove2x(), task.getMove2y(), 0.0f);
					gl.glRotatef(mAngle2, 1, 0, 0);//����ת
					break;
				case 2:
					gl.glTranslatef(task.getMove2x(), task.getMove2y(), 0.0f);
					gl.glRotatef(mAngle2, -1, 0, 0);//����ת
					break;
				case 3:
					gl.glTranslatef(task.getMove2x(), task.getMove2y(), 0.0f);
					gl.glRotatef(mAngle2, 0, 1, 0);//����ת
					break;
				case 4:
					gl.glTranslatef(task.getMove2x(), task.getMove2y(), 0.0f);
					gl.glRotatef(mAngle2, 0, -1, 0);//����ת
					break;
				default:
					break;
				}

				gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
				gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
				mCube.draw(gl);

			}

			// ����Ļ �ı�ʱ�򣬱����ֻ������ã���������ã���onSurfaceCreated ������onSurfaceChanged ��
			public void onSurfaceChanged(GL10 gl, int width, int height) {

//				gl.glViewport(0, 0, 320, 320);
				
				gl.glViewport(0, 0, OpenGlActivity.width, OpenGlActivity.height);
			}

			// ����Ļ �ı�ʱ�򣬱����ֻ������ã���������ã���onSurfaceCreated ������onSurfaceChanged ��
			public void onSurfaceCreated(GL10 gl, EGLConfig config) {
				
				// �������� �Լ�д�ķ���
				loadTexture(gl, 1, R.drawable.cube_front);
				loadTexture(gl, 2, R.drawable.cube_back);
				loadTexture(gl, 3, R.drawable.cube_left);
				loadTexture(gl, 4, R.drawable.cube_right);
				loadTexture(gl, 5, R.drawable.cube_top);
				loadTexture(gl, 6, R.drawable.cube_bottom);

				gl.glDisable(GL10.GL_DITHER);
				gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
				gl.glEnable(GL10.GL_CULL_FACE);
				gl.glShadeModel(GL10.GL_SMOOTH);
				gl.glEnable(GL10.GL_DEPTH_TEST);
				
				// Ĭ����ת
				gl.glRotatef(10.0f, 1.0f, 1.0f, 0);

			}

			public void loadTexture(GL10 gl, int no, int drawableId) {

				// ��������
				int[] textures = new int[1];
				switch (no) {
				// �����ɰ�����
				case 1:
					gl.glGenTextures(1, textures, 0);
					Cube.mTextureID_front = textures[0];
					gl.glBindTexture(GL10.GL_TEXTURE_2D, Cube.mTextureID_front);
					break;
				case 2:
					gl.glGenTextures(1, textures, 0);
					Cube.mTextureID__back = textures[0];
					gl.glBindTexture(GL10.GL_TEXTURE_2D, Cube.mTextureID__back);
					break;
				case 3:
					gl.glGenTextures(1, textures, 0);
					Cube.mTextureID_left = textures[0];
					gl.glBindTexture(GL10.GL_TEXTURE_2D, Cube.mTextureID_left);
					break;
				case 4:
					gl.glGenTextures(1, textures, 0);
					Cube.mTextureID_right = textures[0];
					gl.glBindTexture(GL10.GL_TEXTURE_2D, Cube.mTextureID_right);
					break;
				case 5:
					gl.glGenTextures(1, textures, 0);
					Cube.mTextureID_top = textures[0];
					gl.glBindTexture(GL10.GL_TEXTURE_2D, Cube.mTextureID_top);
					break;
				case 6:
					gl.glGenTextures(1, textures, 0);
					Cube.mTextureID_bottom = textures[0];
					gl.glBindTexture(GL10.GL_TEXTURE_2D, Cube.mTextureID_bottom);
					break;
				}

				gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
						GL10.GL_NEAREST);
				gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,
						GL10.GL_LINEAR);
				gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,
						GL10.GL_CLAMP_TO_EDGE);
				gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,
						GL10.GL_CLAMP_TO_EDGE);
				gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE,
						GL10.GL_REPLACE);

				InputStream is = mContext.getResources()
						.openRawResource(drawableId);
				Bitmap bitmapTmp;

				try {
					bitmapTmp = BitmapFactory.decodeStream(is);
				} finally {
					try {
						is.close();
					} catch (IOException e) {
						// Ignore.
					}
				}

				GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmapTmp, 0);
				bitmapTmp.recycle();

			}
			
			private Cube mCube;

			private Context mContext;

			public float mAngle1;

			public float mAngle2;


		}

		private CubeRenderer mRenderer;

	}
}


