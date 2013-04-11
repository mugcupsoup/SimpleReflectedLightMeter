/*
 * ExifTag.java
 *
 *  (c) 2013 mugcupsoup@gmail.com
 *
 *  Licensed under the MIT license.
 *  http://www.opensource.org/licenses/mit-license
 */

package com.gmail.mugucupsoup.android.Exif;
import java.util.*;

public class ExifTag implements ExifConst{
	private final String SEPARATER = ",";
	private int code;
	private String name;
	private int type;
	private Vector<Object> values = new Vector<Object>();
	private Vector<String> strValues = new Vector<String>();
	private int count;
	private int offset;
	private int point;

	public Vector<Object> getValues() {
		return values;
	}

	public void setValues(Vector<Object> values) {
		this.values = values;
	}

	public Vector<String> getStrValues() {
		return strValues;
	}

	public void setStrValues(Vector<String> strValues) {
		this.strValues = strValues;
	}

	public ExifTag(int code,String name) {
		this.code = code;
		this.name = name;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public int getType() {
		return type;
	}
	
	public String strValuesToString() {
		String str = new String("");
		if(strValues.isEmpty()) return str;
		for (Enumeration<String> e = strValues.elements(); e.hasMoreElements();) {
			str += e.nextElement();
			if (e.hasMoreElements()){
				str += this.SEPARATER;
			}
		}
		return str;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}
	
}
