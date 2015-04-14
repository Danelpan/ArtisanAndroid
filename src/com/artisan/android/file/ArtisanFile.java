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
	
	public final void asyncWrite(String path,String source,IFileWriteListener listener){
		FileEntry<String> entry = new StringEntry(entries);
		addEntries(entry);
		entry.setWriteListener(listener);
		entry.startWrite(new File(path), source);
	}
	
	public final void asyncReadString(String path,IFileReadListener<String> listener){
		FileEntry<String> entry = new StringEntry(entries);
		addEntries(entry);
		entry.setReadListener(listener);
		entry.startRead(new File(path));
	}
	
	public final void asyncWrite(String path,Bitmap source,IFileWriteListener listener){
		FileEntry<Bitmap> entry = new BitmapEntry(entries);
		addEntries(entry);
		entry.setWriteListener(listener);
		entry.startWrite(new File(path), source);
	}
	
	public final void asyncReadBitmap(String path,IFileReadListener<Bitmap> listener){
		FileEntry<Bitmap> entry = new BitmapEntry(entries);
		addEntries(entry);
		entry.setReadListener(listener);
		entry.startRead(new File(path));
	}
	
	public final void syncWrite(String path,String source){
		FileEntry<String> entry = new StringEntry();
		entry.write(new File(path), source);
	}
	
	public final String syncReadString(String path){
		FileEntry<String> entry = new StringEntry();
		return entry.read(new File(path));
	}
	
	public final void syncWrite(String path,Bitmap source){
		FileEntry<Bitmap> entry = new BitmapEntry();
		entry.write(new File(path), source);
	}
	
	public final Bitmap syncReadBitmap(String path){
		FileEntry<Bitmap> entry = new BitmapEntry();
		return entry.read(new File(path));
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
