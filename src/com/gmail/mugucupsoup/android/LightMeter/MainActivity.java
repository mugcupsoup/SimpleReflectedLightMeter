/*
 * MainActivity.java
 *
 *  (c) 2013 mugcupsoup@gmail.com
 *
 *  Licensed under the MIT license.
 *  http://www.opensource.org/licenses/mit-license
 */
package com.gmail.mugucupsoup.android.LightMeter;

import com.gmail.mugcuposup.android.R;
import com.gmail.mugucupsoup.android.Exif.*;

import java.io.IOException;
import java.util.HashMap;

import android.media.AudioManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;

public class MainActivity extends LightMeterActivity implements SurfaceHolder.Callback {

	Camera camera;
	SurfaceView surfaceView;
	SurfaceHolder surfaceHolder;
	boolean previewing = false;
	LayoutInflater controlInflater = null;
	int silentMode;

	final int RESULT_SAVEIMAGE = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.camera_preview);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

		getWindow().setFormat(PixelFormat.UNKNOWN);
		surfaceView = (SurfaceView) findViewById(R.id.camerapreview);
		surfaceHolder = surfaceView.getHolder();
		surfaceHolder.addCallback(this);
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		controlInflater = LayoutInflater.from(getBaseContext());
		View viewControl = controlInflater.inflate(R.layout.camera_control,null);
		LayoutParams layoutParamsControl = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		this.addContentView(viewControl, layoutParamsControl);

		Button buttonTakePicture = (Button) findViewById(R.id.exposure);
		buttonTakePicture.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				AudioManager mgr = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
			    int streamType = AudioManager.STREAM_SYSTEM;
			    mgr.setStreamSolo(streamType, true);
				camera.takePicture(null, null, myPictureCallback_JPG);
			}
		});
		
		showUsageDialog(R.string.usage_explain_preview);
	}

	@SuppressLint("UseValueOf")
	public void readExif(byte[] data) {
		
		ExifInfomation ei = new ExifInfomation();
		try {
			ei.readImage(data,true);
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		}
		
		ExifInfo exifInfo = new ExifInfo();
		String exposureTime=null, fnumber=null;
		int isoRatings=0;
		HashMap<Integer,ExifTag> exifTags = ei.getExifTags();
		if(exifTags!=null){
			ExifTag et;
			et = ei.getByName("ExposureTime",exifTags);
			if(et!=null){
				exposureTime = et.getStrValues().firstElement();
				if(exposureTime != null){
					exifInfo.setExposureTimeStr(exposureTime);
				}
			}
			et = ei.getByName("FNumber",exifTags);
			SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
			boolean usePreferenceFnumber = pref.getBoolean("checkbox_fnumber_preference", false);
			if(et!=null && !usePreferenceFnumber){
				fnumber = et.getStrValues().firstElement();
				if(fnumber != null){
					exifInfo.setFNumberStr(fnumber);
				}
			}else{
				exifInfo.setFNumberStr(pref.getString("list_fnumber_preference",getString(R.string.default_fnumber)));
				exifInfo.setHasFnumber(false);
			}
			
			et = ei.getByName("ISOSpeedRatings",exifTags);
			if(et!=null){
				String ISOSpeedRatings = et.getStrValues().firstElement();
				if(ISOSpeedRatings != null){
					isoRatings = new Integer(ISOSpeedRatings).intValue();
					exifInfo.setIsoValue(isoRatings);
				}
			}			
			
			ExifThumbnail ethumb = ei.getExifTumbnail();
			if(ethumb!=null){
				byte[] imagedata = ethumb.getImageBytes();
				exifInfo.setThumbBytes(imagedata);// for show in ResultView
			}

		}
		
		Intent intent = new Intent(this,com.gmail.mugucupsoup.android.LightMeter.ResultActivity.class);
		intent.putExtra("ExposureTimeInfo", exifInfo);
		startActivity(intent);
	}

	PictureCallback myPictureCallback_JPG = new PictureCallback() {
		@Override
		public void onPictureTaken(byte[] arg0, Camera arg1) {
			AudioManager mgr = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
		    int streamType = AudioManager.STREAM_SYSTEM;
		    mgr.setStreamSolo(streamType, false);
			readExif(arg0);
			camera.startPreview();
		}
	};

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		if (previewing) {
			camera.stopPreview();
			previewing = false;
		}

		if (camera != null) {
			try {
				camera.setPreviewDisplay(surfaceHolder);
				camera.startPreview();
				previewing = true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		camera = Camera.open();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		camera.stopPreview();
		camera.release();
		camera = null;
		previewing = false;
	}
	
	@Override
	public void onRestart(){
		super.onRestart();
		showUsageDialog(R.string.usage_explain_preview);
	}

}
