/*
 * ResultActivity.java
 *
 *  (c) 2013 mugcupsoup@gmail.com
 *
 *  Licensed under the MIT license.
 *  http://www.opensource.org/licenses/mit-license
 */

package com.gmail.mugucupsoup.android.LightMeter;

import com.gmail.mugcuposup.android.R;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.*;

@SuppressLint("UseValueOf")
public class ResultActivity extends LightMeterActivity{
	
	String result;
	ExifInfo exifInfo;
	boolean isok=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

		final Context context = this;

		Button buttonTakePicture = (Button) findViewById(R.id.back);
		buttonTakePicture.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				backToPreview();
			}
		});

		Button buttonOpenSettings = (Button) findViewById(R.id.settings);
		buttonOpenSettings.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(context,com.gmail.mugucupsoup.android.LightMeter.LightMeterPreferenceActivety.class);
				startActivity(intent);
			}
		});

        Intent intent = getIntent();
        exifInfo = (ExifInfo)intent.getSerializableExtra("ExposureTimeInfo");
        
        if(exifInfo.getEvValue() == 0){
        	AlertDialog.Builder alert = new AlertDialog.Builder(this);  
            alert.setTitle("Error");  
            alert.setMessage(getString(R.string.error_no_exifinformation));
            alert.setPositiveButton("OK", null);
            alert.show();
            return;
        }else{
        	isok = true;
            showExifInfo();
            showValues();
        }
        
        showUsageDialog(R.string.usage_explain_result);
	}
	
	private void backToPreview() {
		//Intent intent = new Intent(this,com.gmail.mugucupsoup.android.LightMeter.MainActivity.class);
		//startActivity(intent);
		finish();
	}
	
	private void showValues() {
		if(!isok) return;
		
		//ev
        TextView textEv = (TextView) findViewById(R.id.textEv);
        textEv.setText(Double.toString(Math.round(exifInfo.getEvValue()*10)/10.0));
        
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
		//iso
        String isoStr = pref.getString("list_iso_preference",getString(R.string.default_iso));
        TextView textIso = (TextView) findViewById(R.id.textIso);
        textIso.setText(isoStr);

        String fnumberStr = pref.getString("calc_fnumber_preference",getString(R.string.default_calc_fnumber));
        double fnumber = this.getValueFromRational(fnumberStr);	
        //fnumber
        TextView textF = (TextView) findViewById(R.id.textF);
        textF.setText(Double.toString(fnumber));
        
        String exposureStr = calcExposureTime(exifInfo.getEvValue(),new Integer(isoStr).intValue(),fnumber);
        //exposure time
        TextView textT = (TextView) findViewById(R.id.textT);
        textT.setText(exposureStr);
	}

	private String calcExposureTime(double evValue, int isoValue, double fnumber) {

		double tmp = (Math.log(fnumber*fnumber) / Math.log(2.0)) - (Math.log(isoValue/100.0) / Math.log(2.0)) - evValue;
		tmp = tmp*Math.log(2.0);
		double exposureTime = Math.pow(Math.E,tmp);
		
		//find near value from exposure time array
		String[] exposureArr = getResources().getStringArray(R.array.exposure_array);
		
		double exposureMax = getValueFromRational(exposureArr[0]);
		double exposureMin = getValueFromRational(exposureArr[exposureArr.length-1]);
		String outofrange = "out of range";
		if(exposureTime > exposureMax || exposureTime < exposureMin){
			return outofrange;
		}
		double diff = 99999.0;
		int i=0;
		for(i=0;i < exposureArr.length; i++){
			double exposureValue = getValueFromRational(exposureArr[i]);;
			double tmpDiff = Math.abs(exposureValue - exposureTime);
			if(tmpDiff >= diff){
				return exposureArr[i-1];
			}else{
				diff = tmpDiff;
			}
		}
		return exposureArr[i-1];
	}

	private double getValueFromRational(String str) {
		if(str == null) return 0;
		String[] numArr = str.split("/");
		if(numArr.length == 1){
			return  new Double(numArr[0]).doubleValue();
		}else{
			return  new Double(numArr[0]).doubleValue()/new Double(numArr[1]).doubleValue();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if(!isok) return super.dispatchKeyEvent(event);
		int action = event.getAction();
		int keyCode = event.getKeyCode();
		switch (keyCode) {
		case KeyEvent.KEYCODE_VOLUME_UP:
			if (action == KeyEvent.ACTION_UP) {
				this.changeFnumber(1);
			}
			return true;
		case KeyEvent.KEYCODE_VOLUME_DOWN:
			if (action == KeyEvent.ACTION_DOWN) {
				this.changeFnumber(-1);
			}
			return true;
		default:
			return super.dispatchKeyEvent(event);
		}
	}
	
	private void changeFnumber(int l) {
		if(l==0) return;
		
		//read preference
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        String fnumberStr = pref.getString("calc_fnumber_preference",getString(R.string.default_calc_fnumber));
        
		//find near value
		String[] fnumberArr = getResources().getStringArray(R.array.fnumber_list_value_preference);
		int now=0;
		for(int i=0;i < fnumberArr.length; i++){
			if(fnumberArr[i].equals(fnumberStr)){
				now = i;
				break;
			}
		}
		
		String nextStr = null;
		if(now == 0){
			if(l < 0){
				return;
			}else{
				nextStr = fnumberArr[now+1];
			}
		}else if(now == (fnumberArr.length-1)){
			if(l > 0){
				return;
			}else{
				nextStr = fnumberArr[now-1];
			}
		}else{
			if(l > 0){
				nextStr = fnumberArr[now+1];
			}else{
				nextStr = fnumberArr[now-1];
			}
		}
		
		//save preference
		SharedPreferences.Editor editor = pref.edit();
		editor.putString("calc_fnumber_preference", nextStr);
		editor.commit();
				
		this.showValues();
	}

	private void showExifInfo(){
        if(!isok) return;
        TextView textExifInfo = (TextView) findViewById(R.id.textExifInfo);
        
        byte[] imageBytes = exifInfo.getThumbBytes();
        if(imageBytes!=null){
        	//draw thumbnail
	        Drawable image = this.getDrwableFromBytes(imageBytes);
			SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
	        String sizeStr = pref.getString("thumbnail_size_preference",getString(R.string.default_thumbnail_size));
	        String[] numArr = sizeStr.split("/");
	        image.setBounds(0, 0, new Integer(numArr[0]).intValue(), new Integer(numArr[1]).intValue());
	        textExifInfo.setCompoundDrawables(image, null, null, null);
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append(getString(R.string.exif_information));
        sb.append("\n");
        if(exifInfo.getExposureTimeStr() != null){
        	sb.append(getString(R.string.exposure_time));
        	sb.append(": ");
        	sb.append(exifInfo.getExposureTimeStr());
        	sb.append("\n");
        }
        
        if(exifInfo.getFNumberStr() != null){
        	sb.append(getString(R.string.fnumber));
        	sb.append(": ");
        	sb.append(exifInfo.getFNumberStr());
        	if(!exifInfo.isHasFnumber()){
        		sb.append(getString(R.string.using_preference));
        	}
        	sb.append("\n");
        }
        
        if(exifInfo.getIsoValue() > 0){
        	sb.append(getString(R.string.iso_speed_ratings));
        	sb.append(": ");
        	sb.append(exifInfo.getIsoValue());
        	sb.append("\n");
        }
        textExifInfo.setText(sb.toString());
	}

	private Drawable getDrwableFromBytes(byte[] imageBytes) {
		if (imageBytes != null) {
			return new BitmapDrawable(BitmapFactory.decodeByteArray(imageBytes,	0, imageBytes.length));
		} else {
			return null;
		}
	}
	
	@Override
	public void onRestart(){
		super.onRestart();
		showUsageDialog(R.string.usage_explain_result);
		this.showExifInfo();
		this.showValues();
	}
}
