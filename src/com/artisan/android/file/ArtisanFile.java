package com.artisan.android.file;

import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.graphics.Bitmap;

@SuppressWarnings("rawtypes")
public final class ArtisanFile {
	
	List<FileEntry> entries = Collections.synchronizedList(new LinkedList<FileEntry>());
	
	public static final ArtisanFile getInstance(){
		return new ArtisanFile();
	}
	
	public final void writeStr(String path,String source,IFileWriteListener listener){
		StringEntry entry = new StringEntry(entries);
		addEntries(entry);
		entry.setWriteListener(listener);
		entry.startWrite(new File(path), source);
	}
	
	public final void readStr(String path,IFileReadListener<String> listener){
		StringEntry entry = new StringEntry(entries);
		addEntries(entry);
		entry.setReadListener(listener);
		entry.startRead(new File(path));
	}
	
	public final void writeBmp(String path,Bitmap source,IFileWriteListener listener){
		BitmapEntry entry = new BitmapEntry(entries);
		addEntries(entry);
		entry.setWriteListener(listener);
		entry.startWrite(new File(path), source);
	}
	
	public final void readBmp(String path,IFileReadListener<Bitmap> listener){
		BitmapEntry entry = new BitmapEntry(entries);
		addEntries(entry);
		entry.setReadListener(listener);
		entry.startRead(new File(path));
	}
	
	public final void destory(){
		for (FileEntry entry : entries) {
			entry.destory();
		}
		entries.clear();
	}
	
	private final synchronized void addEntries(FileEntry entry){
		entries.add(entry);
	}
	
	private ArtisanFile(){}
}
