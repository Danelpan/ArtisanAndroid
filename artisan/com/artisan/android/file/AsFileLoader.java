package com.artisan.android.file;

import java.io.File;

import android.graphics.Bitmap;

import com.artisan.android.file.entries.BitmapFileEntry;
import com.artisan.android.file.entries.StringFileEntry;
import com.artisan.android.task.AsAsyncTask;

public final class AsFileLoader {
	
	private AsAsyncTask mAsyncTask;
	
	public static final AsFileLoader getInstance(){
		return new AsFileLoader();
	}
	
	public final int asyncWriteString(String path,String source,IFileWriteListener listener){
		IAsFileEntry<String> entry = new StringFileEntry();
		File file = new File(path);
		AsInternalHandler<String> internalHandler = new AsInternalHandler<String>(entry, file , source);
		internalHandler.setWriteListener(listener);
		return mAsyncTask.doAsyncTask(AsInternalHandler.KEY_WRITE, internalHandler);
	}
	
	public final int asyncReadString(String path,IFileReadListener<String> listener){
		IAsFileEntry<String> entry = new StringFileEntry();
		File file = new File(path);
		AsInternalHandler<String> internalHandler = new AsInternalHandler<String>(entry, file , null);
		internalHandler.setReadListener(listener);
		return mAsyncTask.doAsyncTask(AsInternalHandler.KEY_READ, internalHandler);
	}
	
	public final int asyncWriteBitmap(String path,Bitmap source,IFileWriteListener listener){
		IAsFileEntry<Bitmap> entry = new BitmapFileEntry();
		File file = new File(path);
		AsInternalHandler<Bitmap> internalHandler = new AsInternalHandler<Bitmap>(entry, file , source);
		internalHandler.setWriteListener(listener);
		return mAsyncTask.doAsyncTask(AsInternalHandler.KEY_WRITE, internalHandler);
	}
	
	public final int asyncReadBitmap(String path,IFileReadListener<Bitmap> listener){
		IAsFileEntry<Bitmap> entry = new BitmapFileEntry();
		File file = new File(path);
		AsInternalHandler<Bitmap> internalHandler = new AsInternalHandler<Bitmap>(entry, file , null);
		internalHandler.setReadListener(listener);
		return mAsyncTask.doAsyncTask(AsInternalHandler.KEY_READ, internalHandler);
	}
	
	public final void syncWriteString(String path,String source){
		IAsFileEntry<String> entry = new StringFileEntry();
		entry.onWrite(new File(path), source);
	}
	
	public final String syncReadString(String path){
		IAsFileEntry<String> entry = new StringFileEntry();
		return entry.onRead(new File(path));
	}
	
	public final void syncWriteBitmap(String path,Bitmap source){
		IAsFileEntry<Bitmap> entry = new BitmapFileEntry();
		entry.onWrite(new File(path), source);
	}
	
	public final Bitmap syncReadBitmap(String path){
		IAsFileEntry<Bitmap> entry = new BitmapFileEntry();
		return entry.onRead(new File(path));
	}
	
	public final void cancel(int id){
		mAsyncTask.cancel(id);
	}
	
	private AsFileLoader(){
		mAsyncTask = new AsAsyncTask.Builder().build();
	}
}
