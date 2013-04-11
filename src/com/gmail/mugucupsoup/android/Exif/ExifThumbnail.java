/*
 * ExifThumbnail.java
 *
 *  (c) 2013 mugcupsoup@gmail.com
 *
 *  Licensed under the MIT license.
 *  http://www.opensource.org/licenses/mit-license
 */
package com.gmail.mugucupsoup.android.Exif;

import java.io.*;
import java.util.*;

public class ExifThumbnail extends ExifInfomation {	
	private boolean thumbnail_compress = false;
	private int thumbnailtype = THUMB_JPEG;
	private int thumb_head;
	private int thumb_len;
	private byte[] imageBytes = null;
	
	public byte[] getImageBytes() {
		return imageBytes;
	}
	
	public int getThumbnailtype() {
		return thumbnailtype;
	}
	
	public void setTiffTags(HashMap<Integer, ExifTag> tiffTags) {
		this.tiffTags = tiffTags;
	}

	public void setExifTags(HashMap<Integer, ExifTag> exifTags) {
		this.exifTags = exifTags;
	}

	public void setGpsTags(HashMap<Integer, ExifTag> gpsTags) {
		this.gpsTags = gpsTags;
	}

	public void setTnTags(HashMap<Integer, ExifTag> tnTags) {
		this.tnTags = tnTags;
	}
	
	public ExifThumbnail(boolean thumbnail_compress, int thumbnailtype, int tnhead,int tnlen) {
		super();
		this.thumbnail_compress = thumbnail_compress;
		this.thumbnailtype = thumbnailtype;
		this.thumb_head = tnhead;
		this.thumb_len = tnlen;
	}

	public void setHedderTiff(int pos){
		HEADER_TIFF = pos;
	}

	private void writeHeader(ByteArrayOutputStream stream) {
		if (this.littleEndian) {
			stream.write(0x49);
			stream.write(0x49);
		} else {
			stream.write(0x4d);
			stream.write(0x4d);
		}

		writeDByte(stream,0x002a);
		writeDDByte(stream,0x00000008);

		return;
	}


	private void writeDDByte(ByteArrayOutputStream stream, int i) {
		int b1 = i / (0x100 * 0x100);
		int b2 = i - (b1 * 0x100 * 0x100);
		if (this.littleEndian) {
			writeDByte(stream, b2);
			writeDByte(stream, b1);
		} else {
			writeDByte(stream, b1);
			writeDByte(stream, b2);
		}
		return;
	}

	private void writeDByte(ByteArrayOutputStream stream, int i) {
		byte b1 = (byte) (i / 0x100);
		byte b2 = (byte) (i - (int) b1 * 0x100);
		if (this.littleEndian) {
			stream.write(b2);
			stream.write(b1);
		} else {
			stream.write(b1);
			stream.write(b2);
		}
		return;
	}

	public boolean writeImage(String filename) throws IOException {
		if(imageBytes != null){
			FileOutputStream fos = new FileOutputStream(filename);
			fos.write(imageBytes);
			fos.close();
			return true;
		}
		return false;
	}
	
	public byte[] readImage(ExByteArrayInputStream memstream) throws IOException {
		if(this.thumb_head > 0 && this.thumb_len > 0){
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			if (!thumbnail_compress) this.writeHeader(baos);
			memstream.seek(thumb_head);
			byte b;
			for (int i = 0; i < this.thumb_len; i++) {
				b = (byte) memstream.read();
				baos.write(b);				
			}
			imageBytes = baos.toByteArray();
			baos.close();
		}
		return imageBytes;
	}

}
