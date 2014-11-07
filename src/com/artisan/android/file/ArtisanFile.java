package com.artisan.android.file;

import java.io.File;

import android.graphics.Bitmap;

public final class ArtisanFile {
	
	public static final void writeStr(String path,String source,IFileWriteListener listener){
		StringEntry entry = new StringEntry();
		entry.setWriteListener(listener);
		entry.startWrite(new File(path), source);
	}
	
	public static final void readStr(String path,IFileReadListener<String> listener){
		StringEntry entry = new StringEntry();
		entry.setReadListener(listener);
		entry.startRead(new File(path));
	}
	
	public static final void writeBmp(String path,Bitmap source,IFileWriteListener listener){
		BitmapEntry entry = new BitmapEntry();
		entry.setWriteListener(listener);
		entry.startWrite(new File(path), source);
	}
	
	public static final void readBmp(String path,IFileReadListener<Bitmap> listener){
		BitmapEntry entry = new BitmapEntry();
		entry.setReadListener(listener);
		entry.startRead(new File(path));
	}
	
}
