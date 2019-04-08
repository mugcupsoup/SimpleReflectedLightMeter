/*
 * ExifInfomation.java
 *
 *  (c) 2013 mugcupsoup@gmail.com
 *
 *  Licensed under the MIT license.
 *  http://www.opensource.org/licenses/mit-license
 */

package com.gmail.mugucupsoup.android.Exif;
import android.annotation.SuppressLint;
import java.io.*;
import java.util.*;


@SuppressLint({ "UseSparseArrays", "UseValueOf" })
public class ExifInfomation implements  ExifConst{
	protected HashMap<Integer,ExifTag> tiffTags,exifTags,gpsTags,tnTags,itpIdfTags;
	protected int HEADER_TIFF=0;
	protected boolean littleEndian = true;
	private ExifThumbnail exifTumb = null;

	private Map<Integer, ExifTag> tiffTagMap =  new HashMap<Integer, ExifTag>() {
		private static final long serialVersionUID = -4740024337119618365L;
		{
			//A
			put(256,new ExifTag(256,"ImageWidth"));
			put(257,new ExifTag(257,"ImageLength"));
			put(258,new ExifTag(258,"BitsPerSample"));
			put(259,new ExifTag(259,"Compression"));
			put(262,new ExifTag(262,"PhotometricInterpretation"));
			put(274,new ExifTag(274,"Orientation"));
			put(277,new ExifTag(277,"SamplesPerPixel"));
			put(284,new ExifTag(284,"PlanarConfiguration"));
			put(530,new ExifTag(530,"YCbCrSubSampling"));
			put(531,new ExifTag(531,"YCbCrPositioning"));
			put(282,new ExifTag(282,"XResolution"));
			put(283,new ExifTag(283,"YResolution"));
			put(296,new ExifTag(296,"ResolutionUnit"));
			//B
			put(273,new ExifTag(273,"StripOffsets"));
			put(278,new ExifTag(278,"RowsPerStrip"));
			put(279,new ExifTag(279,"StripByteCounts"));
			put(513,new ExifTag(513,"JPEGInterchangeFormat"));
			put(514,new ExifTag(514,"JPEGInterchangeFormatLength"));
			//C
			put(301,new ExifTag(301,"TransferFunction"));
			put(318,new ExifTag(318,"WhitePoint"));
			put(319,new ExifTag(319,"PrimaryChromaticities"));
			put(529,new ExifTag(529,"YCbCrCoefficients"));
			put(532,new ExifTag(532,"ReferenceBlackWhite"));
			//D
			put(306,new ExifTag(306,"DateTime"));
			put(270,new ExifTag(270,"ImageDescription"));
			put(271,new ExifTag(271,"Make"));
			put(272,new ExifTag(272,"Model"));
			put(305,new ExifTag(305,"Software"));
			put(315,new ExifTag(315,"Artist"));
			put(33432,new ExifTag(33432,"Copyright"));
			//Exif and GPS
			put(34665,new ExifTag(34665,"ExifIFDPointer"));
			put(34853,new ExifTag(34853,"GPSInfoIFDPointer"));
	    }
	};
	
	private Map<Integer, ExifTag> exifTagMap =  new HashMap<Integer, ExifTag>() {
		private static final long serialVersionUID = -5600440338715383577L;

		{
			//A
			put(36864,new ExifTag(36864,"ExifVersion"));
			put(40960,new ExifTag(40960,"FlashpixVersion"));
			//B
			put(40961,new ExifTag(40961,"ColorSpace"));
			put(42240,new ExifTag(42240,"Gamma"));
			//C
			put(37121,new ExifTag(37121,"ComponentsConfiguration"));
			put(37122,new ExifTag(37122,"CompressedBitsPerPixel"));
			put(40962,new ExifTag(40962,"PixelXDimension"));
			put(40963,new ExifTag(40963,"PixelYDimension"));
			//D
			put(37500,new ExifTag(37500,"MakerNote"));
			put(37510,new ExifTag(37510,"UserComment"));
			//E
			put(40964,new ExifTag(40964,"RelatedSoundFile"));
			//F
			put(36867,new ExifTag(36867,"DateTimeOriginal"));
			put(36868,new ExifTag(36868,"DateTimeDigitized"));
			put(37520,new ExifTag(37520,"SubSecTime"));
			put(37521,new ExifTag(37521,"SubSecTimeOriginal"));
			put(37522,new ExifTag(37522,"SubSecTimeDigitized"));
			//G
			put(33434,new ExifTag(33434,"ExposureTime"));
			put(33437,new ExifTag(33437,"FNumber"));
			put(34850,new ExifTag(34850,"ExposureProgram"));
			put(34852,new ExifTag(34852,"SpectralSensitivity"));
			put(34855,new ExifTag(34855,"ISOSpeedRatings"));
			put(34856,new ExifTag(34856,"OECF"));
			put(37377,new ExifTag(37377,"ShutterSpeedValue"));
			put(37378,new ExifTag(37378,"ApertureValue"));
			put(37379,new ExifTag(37379,"BrightnessValue"));
			put(37380,new ExifTag(37380,"ExposureBiasValue"));
			put(37381,new ExifTag(37381,"MaxApertureValue"));
			put(37382,new ExifTag(37382,"SubjectDistance"));
			put(37383,new ExifTag(37383,"MeteringMode"));
			put(37384,new ExifTag(37384,"LightSource"));
			put(37385,new ExifTag(37385,"Flash"));
			put(37386,new ExifTag(37386,"FocalLength"));
			put(37396,new ExifTag(37396,"SubjectArea"));
			put(41483,new ExifTag(41483,"FlashEnergy"));
			put(41484,new ExifTag(41484,"SpatialFrequencyResponse"));
			put(41486,new ExifTag(41486,"FocalPlaneXResolution"));
			put(41487,new ExifTag(41487,"FocalPlaneYResolution"));
			put(41488,new ExifTag(41488,"FocalPlaneResolutionUnit"));
			put(41492,new ExifTag(41492,"SubjectLocation"));
			put(41493,new ExifTag(41493,"ExposureIndex"));
			put(41495,new ExifTag(41495,"SensingMethod"));
			put(41728,new ExifTag(41728,"FileSource"));
			put(41729,new ExifTag(41729,"SceneType"));
			put(41730,new ExifTag(41730,"CFAPattern"));
			put(41985,new ExifTag(41985,"CustomRendered"));
			put(41986,new ExifTag(41986,"ExposureMode"));
			put(41987,new ExifTag(41987,"WhiteBalance"));
			put(41988,new ExifTag(41988,"DigitalZoomRatio"));
			put(41989,new ExifTag(41989,"FocalLengthIn35mmFilm"));
			put(41990,new ExifTag(41990,"SceneCaptureType"));
			put(41991,new ExifTag(41991,"GainControl"));
			put(41992,new ExifTag(41992,"Contrast"));
			put(41993,new ExifTag(41993,"Saturation"));
			put(41994,new ExifTag(41994,"Sharpness"));
			put(41995,new ExifTag(41995,"DeviceSettingDescription"));
			put(41996,new ExifTag(41996,"SubjectDistanceRange"));
			//H
			put(42016,new ExifTag(42016,"ImageUniqueID"));
			//Interoperability IFD
			put(40965,new ExifTag(40965,"InteroperabilityIFD"));
			
		}
	};
	
	private Map<Integer, ExifTag> gpsTagMap =  new HashMap<Integer, ExifTag>() {
		private static final long serialVersionUID = 4430608875914635416L;
		{
			put(0,new ExifTag(0,"GPSVersionID"));
			put( 1,new ExifTag(1,"GPSLatitudeRef"));
			put( 2,new ExifTag(2,"GPSLatitude"));
			put( 3,new ExifTag(3,"GPSLongitudeRef"));
			put( 4,new ExifTag(4,"GPSLongitude"));
			put( 5,new ExifTag(5,"GPSAltitudeRef"));
			put( 6,new ExifTag(6,"GPSAltitude"));
			put( 7,new ExifTag(7,"GPSTimeStamp"));
			put( 8,new ExifTag(8,"GPSSatellites"));
			put( 9,new ExifTag(9,"GPSStatus"));
			put(10,new ExifTag(10,"GPSMeasureMode"));
			put(11,new ExifTag(11,"GPSDOP"));
			put(12,new ExifTag(12,"GPSSpeedRef"));
			put(13,new ExifTag(13,"GPSSpeed"));
			put(14,new ExifTag(14,"GPSTrackRef"));
			put(15,new ExifTag(15,"GPSTrack"));
			put(16,new ExifTag(16,"GPSImgDirectionRef"));
			put(17,new ExifTag(17,"GPSImgDirection"));
			put(18,new ExifTag(18,"GPSMapDatum"));
			put(19,new ExifTag(19,"GPSDestLatitudeRef"));
			put(20,new ExifTag(20,"GPSDestLatitude"));
			put(21,new ExifTag(21,"GPSDestLongitudeRef"));
			put(22,new ExifTag(22,"GPSDestLongitude"));
			put(23,new ExifTag(23,"GPSDestBearingRef"));
			put(24,new ExifTag(24,"GPSDestBearing"));
			put(25,new ExifTag(25,"GPSDestDistanceRef"));
			put(26,new ExifTag(26,"GPSDestDistance"));
			put(27,new ExifTag(27,"GPSProcessingMethod"));
			put(28,new ExifTag(28,"GPSAreaInformation"));
			put(29,new ExifTag(29,"GPSDateStamp"));
			put(30,new ExifTag(30,"GPSDifferential"));
		}
	};

	private Map<Integer, ExifTag> iptTagMap =  new HashMap<Integer, ExifTag>() {
		private static final long serialVersionUID = 5047846794862747662L;
		{
			put( 1,new ExifTag(1,"InteroperabilityIndex"));
			put( 2,new ExifTag(2,"InteroperabilityVersion"));
			put( 0x1000,new ExifTag(0x1000,"RelatedImageFileFormat"));
			put( 0x1001,new ExifTag(0x1001,"RelatedImageWidth"));
			put( 0x1002,new ExifTag(0x1002,"RelatedImageLength"));
		}
	};
	
	protected class ExByteArrayInputStream extends ByteArrayInputStream{

		public ExByteArrayInputStream(byte[] buf) {
			super(buf);
		}
		
		public int getPos(){
			return super.pos;
		}
		
		public void seek(int pos){
			this.reset();
			this.skip(pos);
		}
	}
	
	public HashMap<Integer, ExifTag> getTiffTags() {return tiffTags;}
	public HashMap<Integer, ExifTag> getExifTags() {return exifTags;}
	public HashMap<Integer, ExifTag> getGpsTags() {return gpsTags;}
	public HashMap<Integer, ExifTag> getTnTags() {return tnTags;}
	public HashMap<Integer, ExifTag> getItpIdfTags() {return itpIdfTags;}
	
	public ExifTag getByName(String name, HashMap<Integer, ExifTag> tags ){
		for(Map.Entry<Integer, ExifTag> cursor : tags.entrySet()){
			ExifTag et = cursor.getValue();
			if(et.getName().equals(name)){
				return et;
			}
		}
		return null;
	}
	
	public ExifThumbnail getExifTumbnail() {
		return exifTumb;
	}
	
	private int readDByte(ExByteArrayInputStream stream) {
		int dbyte;
		int byte1,byte2;
			
		byte1 = stream.read();
		byte2 = stream.read();
		if(this.littleEndian){
			dbyte = byte2*0x100+byte1;
		}else{
			dbyte = byte1*0x100+byte2;
		}
		return dbyte;
	}
	
	private int readDDByte(ExByteArrayInputStream stream){
		int d1,d2;
		
		d1 = readDByte(stream);
		d2 = readDByte(stream);
		if(this.littleEndian){
			return d2*0x100*0x100 + d1;
		}else{
			return d1*0x100*0x100 + d2;
		}
	}
	
	private int readSignedDDByte(ExByteArrayInputStream stream) {
		int d1, d2;

		d1 = readDByte(stream);
		d2 = readDByte(stream);
		if (this.littleEndian) {
			if (d2 >= 0x100/2*0x100){
				d2 = -d2;
			}
			return d2*0x100*0x100 + d1;
		} else {
			if (d1 >= 0x100 / 2 * 0x100){
				d1 = -d1;
			}
			return d1*0x100*0x100 + d2;
		}
	}
	
	private ExifTag readTiffTag(ExByteArrayInputStream stream,int pos, Map<Integer, ExifTag> map){
		stream.seek(this.HEADER_TIFF + pos);
		return this.readTag(stream,pos,this.HEADER_TIFF, map);
	}

	public static int calcSize(int type, int count) {
		int size = 0;
		switch (type) {
		case TAG_TYPE_BYTE:
		case TAG_TYPE_ASCII:
		case TAG_TYPE_UNDEFINED:
			size = 1;
			break;
		case TAG_TYPE_SHORT:
			size = 2;
			break;
		case TAG_TYPE_LONG:
		case TAG_TYPE_SLONG:
			size = 4;
			break;
		case TAG_TYPE_RATIONAL:
		case TAG_TYPE_SRATIONAL:
			size = 8;
			break;
		}
		return size * count;
	}
	private ExifTag readTag(ExByteArrayInputStream stream,int pos,int header, Map<Integer, ExifTag> map){
		int tag = readDByte(stream);
		//System.out.printf("%d\n",tag);
		
		ExifTag exiftag = this.getExifTag(tag,map);
		
		if(exiftag == null) return null;
		exiftag.setPoint(pos);
		int type = readDByte(stream);
		int count = readDDByte(stream);
		
		int size = calcSize(type, count);
		int offset = 0;
		if(size > 4){
			offset = readDDByte(stream);
			stream.seek(header + offset);
		}
		exiftag.setOffset(offset);

		exiftag = readValues(stream,type,count,exiftag);
		return exiftag;
	}

	private ExifTag getExifTag(int tag, Map<Integer, ExifTag> map) {
		if(map.containsKey(tag)){
			return map.get(tag);
		}
		return null;
	}

	public void readValue(ExByteArrayInputStream stream, int type, Vector<Object> objs, Vector<String> strs) {
		Object obj = null;
		String str = null;
		int i1,i2;
		switch (type) {
		case TAG_TYPE_BYTE:
			obj = new Integer(stream.read());
			str = obj.toString();
			break;
		case TAG_TYPE_ASCII:
			break;
		case TAG_TYPE_SHORT:
			obj = new Integer(readDByte(stream));
			str = obj.toString();
			break;
		case TAG_TYPE_LONG:
			obj = new Integer(readDDByte(stream));
			str = obj.toString();
			break;
		case TAG_TYPE_RATIONAL:
			i1 = readDDByte(stream);
			i2 = readDDByte(stream);
			obj = new Double((double) i1 / (double) i2);
			if (i1 == 0){
				str = "0";
			}else if (i2 == 1){
				str = Integer.toString(i1);
			}else{
				str = Integer.toString(i1) + "/" + Integer.toString(i2);
			}
			break;
		case TAG_TYPE_UNDEFINED:
			break;
		case TAG_TYPE_SLONG:
			obj = new Integer(readSignedDDByte(stream));
			str = obj.toString();
			break;
		case TAG_TYPE_SRATIONAL:
			i1 = readSignedDDByte(stream);
			i2 = readSignedDDByte(stream);
			obj = new Double((double) i1 / (double) i2);
			if (i1 == 0){
				str = "0";
			}else if (i2 == 1){
				str = Integer.toString(i1);
			}else{
				str = Integer.toString(i1) + "/" + Integer.toString(i2);
			}
			break;
		}
		if (obj != null){
			objs.addElement(obj);
		}
		if (str != null){
			strs.addElement(str);
		}
		return;
	}

	public ExifTag readValues(ExByteArrayInputStream stream, int type, int count,ExifTag exiftag) {
		Vector<Object> values = new Vector<Object>();
		Vector<String> strValues = new Vector<String>();
		String str = new String("");
		switch (type) {
		case TAG_TYPE_ASCII:
			for (int i = 0; i < count; i++) {
				char c = (char) stream.read();
				if (c == '\0') {
					values.addElement((Object) str);
					strValues.addElement(str);
					str = new String("");
				} else{
					str += c;
				}
			}
			if (!str.equals("")) {
				values.addElement((Object) str);
				strValues.addElement(str);
			}
			break;

		case TAG_TYPE_UNDEFINED:
			for (int i = 0; i < count; i++) {
				int b = stream.read();
				str += (char) b;
				values.addElement(new Byte((byte) b));
			}
			if (!str.equals(""))
				strValues.addElement(str);
			break;

		case TAG_TYPE_BYTE:
		case TAG_TYPE_SHORT:
		case TAG_TYPE_LONG:
		case TAG_TYPE_RATIONAL:
		case TAG_TYPE_SLONG:
		case TAG_TYPE_SRATIONAL:
			for (int i = 0; i < count; i++) {
				readValue(stream, type, values, strValues);
			}
			break;
		}
		
		exiftag.setValues(values);
		exiftag.setStrValues(strValues);
		return exiftag;
	}
	
	//for android camera jpeg byte[]
	public boolean readImage(byte data[], boolean read_thumbnail) throws IOException{
		//initialize
		this.exifTags = new HashMap<Integer, ExifTag>();
		this.tiffTags = new HashMap<Integer, ExifTag>();
		this.gpsTags = new HashMap<Integer, ExifTag>();
		this.itpIdfTags = new HashMap<Integer, ExifTag>();
		
		int tiffheadpos = 0; // TIFF HEADER POSITION
		int applength = 0; // APP1 LENGTH
		boolean compress = false;// jpeg compress
		
		ExByteArrayInputStream memstream = new ExByteArrayInputStream(data);
		
		int byte1 = memstream.read();
		int byte2 = memstream.read();
		
		if(byte1 == 0xff && byte2 == 0xd8){// jpeg commpress
			byte1 = memstream.read();
			byte2 = memstream.read();
			compress = true;
			if( byte1 == 0xff && byte2 == 0xe1 ){ // APP1
				byte1 = memstream.read();
				byte2 = memstream.read();
				applength = byte1 * 256 + byte2;
				String str = new String("");
				for(int i=0 ; i < 4 ; i++){
					str += (char)memstream.read();
				}
				if(!str.equals("Exif")){// exit if it isn't Exif
					memstream.close();
					return false;
				}
				while(true){
					byte1 = memstream.read();
					if(byte1 != '\0') break;
				}
				byte2 = memstream.read();
				tiffheadpos = memstream.getPos()-2;
			}else{
				memstream.close();
				return false;
			}
		}
		
		this.HEADER_TIFF = tiffheadpos;// Tiff header position
		
		if ( byte1 == 0x49 && byte2 == 0x49 ){ // INTEL
			this.littleEndian = true;
		}else if ( byte1 == 0x4d && byte2 == 0x4d ){ // MOTOROLA
			this.littleEndian = false;
		}else{
			memstream.close();
			return false;
		}
		
		int id = this.readDByte(memstream);
		if( id != 0x002a ){ // fixed value
			memstream.close();
			return false;
		}
		
		//Tiff IFD
		int offset = this.readDDByte(memstream);
		readIfd(memstream, tiffheadpos, offset, this.tiffTags, this.tiffTagMap);
		
		//exif IFD
		int exifPointer = getPointer(this.tiffTags,34665);
		if(exifPointer > 0){
			readIfd(memstream, tiffheadpos, exifPointer, this.exifTags,this.exifTagMap);
		}
		
		//Interoperability IFD
		int itpidfpointer = getPointer(this.exifTags,40965);
		if(itpidfpointer > 0){
			readIfd(memstream, tiffheadpos, itpidfpointer, this.itpIdfTags,this.iptTagMap);
		}
		
		//gps IFD
		int GPSPointer = getPointer(this.tiffTags,34853);
		if(GPSPointer > 0){
			readIfd(memstream, tiffheadpos, GPSPointer, this.gpsTags,this.gpsTagMap);
		}
		
		//Thumbnail
		if (read_thumbnail) {
			boolean thumb_compress = false;
			int thumbnailtype = THUMB_JPEG;

			memstream.seek(tiffheadpos + offset);
			int num = this.readDByte(memstream);
			memstream.seek(tiffheadpos + offset + 2 + num * 12);
			int nextIFDPointer = this.readDDByte(memstream);

			int thumb_head = 0, thumb_length = 0;
			if (nextIFDPointer != 0) {
				this.tnTags = new HashMap<Integer, ExifTag>();
				readIfd(memstream, tiffheadpos, nextIFDPointer, this.tnTags, this.tiffTagMap);

				int tnend = 0;
				if (compress) {
					tnend = applength + 2;
				} else {
					tnend = getPointer(this.tiffTags, 273);// StripOffsets
				}
				if (this.tnTags.containsKey(513)
						&& this.tnTags.containsKey(514)) {
					thumb_compress = true;
					thumbnailtype = THUMB_JPEG;
					thumb_head = tiffheadpos + getPointer(this.tnTags, 513);// JPEGInterchangeFormat
					thumb_length = getPointer(this.tnTags, 514);// JPEGInterchangeFormatLength
				} else {
					thumb_compress = false;
					thumbnailtype = THUMB_TIFF;
					thumb_head = tiffheadpos + nextIFDPointer;
					thumb_length = tnend - thumb_head;
				}
			}
			if( thumb_head > 0 && thumb_length > 0){
				exifTumb = new ExifThumbnail(thumb_compress, thumbnailtype,	thumb_head, thumb_length);
				exifTumb.setTnTags(this.tnTags);
				exifTumb.readImage(memstream);
			}
		}
		
		memstream.close();
		return true;
	}
	
	private void readIfd(ExByteArrayInputStream memstream, int tiffheadpos, int pointer, HashMap<Integer, ExifTag> outTags,Map<Integer, ExifTag> map) {
		memstream.seek(tiffheadpos + pointer);
		int num = this.readDByte(memstream);
		for(int i=0 ; i < num ; i++){
			ExifTag exifTag = this.readTiffTag(memstream, pointer + 2 + i * 12, map);
			if(exifTag!= null){
				outTags.put(exifTag.getCode(), exifTag);
			}
		}
	}

	private int getPointer(HashMap<Integer, ExifTag> tags, int i) {
		if(tags.containsKey(i)){
			Object obj =tags.get(i).getValues().firstElement();
			if(obj != null){
				return ((Integer)obj).intValue();
			}
		}
		return 0;
	}
	
	private void print(HashMap<Integer, ExifTag> tags) {
		for(Map.Entry<Integer, ExifTag> cursor : tags.entrySet()){
			ExifTag et = cursor.getValue();
			System.out.println(et.getName() + ":" + et.getStrValues().firstElement());
		}
	}

	//for test
	public static void main(String args[]) {
		try {
			File file = new File(args[0]);
			long len = file.length();
			
	        byte data[] = new byte[(int) len];
			FileInputStream f=new FileInputStream(args[0]);
			f.read(data);
			f.close();
			ExifInfomation ei = new ExifInfomation();
			ei.readImage(data,true);
			System.out.println("##Tiff IFD");
			ei.print(ei.getTiffTags());
			System.out.println("##Exif IFD");
			ei.print(ei.getExifTags());
			System.out.println("##GPS IFD");
			ei.print(ei.getGpsTags());
			System.out.println("##Interoperability IFD");
			ei.print(ei.getItpIdfTags());
			ei.getExifTumbnail().writeImage("test.jpg");
		} catch (FileNotFoundException e) {
			System.out.print(e.getMessage());
			System.exit(1);
		} catch (IOException e) {
			System.out.print(e.getMessage());
			System.exit(1); 
		}
		System.exit(0);
	}
}
