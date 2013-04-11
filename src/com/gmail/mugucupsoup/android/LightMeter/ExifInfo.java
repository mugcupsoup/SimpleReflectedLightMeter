/*
 * ExifInfo.java
 *
 *  (c) 2013 mugcupsoup@gmail.com
 *
 *  Licensed under the MIT license.
 *  http://www.opensource.org/licenses/mit-license
 */

package com.gmail.mugucupsoup.android.LightMeter;

import android.annotation.SuppressLint;
import java.io.Serializable;

@SuppressLint("UseValueOf")
public class ExifInfo implements Serializable{
	private static final long serialVersionUID = 3988263042079491967L;
	private double EvValue=0;
	private double FNumber=0;// 1.4 2.0 2.8 ...
	private double ExposureTime=0;//sec
	private int ISOSpeedRatings=0;// 100 200 400
	private byte[] thumbnailBytes = null;
	private String FNumberStr=null;
	private String ExposureTimeStr=null;
	boolean hasFnumber = true;

	public ExifInfo(){
		
	}
	
	public byte[] getThumbBytes() {
		return this.thumbnailBytes;
	}
	public void setThumbBytes(byte[] thumbnailBytes) {
		this.thumbnailBytes = thumbnailBytes;
	}

	public double getFNumber() {
		return this.FNumber;
	}
	
	public double getExposureTime() {
		return this.ExposureTime;
	}
	
	public int getIsoValue() {
		return this.ISOSpeedRatings;
	}
	
	public void setIsoValue(int isoValue) {
		this.ISOSpeedRatings = isoValue;
		this.calcEv();
	}
	
	public double getEvValue() {
		return EvValue;
	}

	public String getFNumberStr() {
		return FNumberStr;
	}

	public void setFNumberStr(String fNumberStr) {
		this.FNumberStr = fNumberStr;
		this.calcEv();
	}

	public String getExposureTimeStr() {
		return this.ExposureTimeStr;
	}

	public void setExposureTimeStr(String exposureTimeStr) {
		this.ExposureTimeStr = exposureTimeStr;
		this.calcEv();
	}
	
	public boolean isHasFnumber() {
		return this.hasFnumber;
	}

	public void setHasFnumber(boolean hasFnumber) {
		this.hasFnumber = hasFnumber;
	}

	private void calcEv() {
		if(this.ExposureTimeStr == null || this.FNumberStr == null || this.ISOSpeedRatings == 0) return;
		this.ExposureTime = this.getValueFromRational(this.ExposureTimeStr);
		this.FNumber = this.getValueFromRational(this.FNumberStr);		
		this.EvValue = (Math.log(Math.pow(this.FNumber, 2.0)) / Math.log(2.0)) - (Math.log(this.ExposureTime) / Math.log(2.0)) - (Math.log(this.ISOSpeedRatings/100.0) / Math.log(2.0));
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
	
}
