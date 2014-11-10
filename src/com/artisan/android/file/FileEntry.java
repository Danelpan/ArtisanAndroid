package com.artisan.android.file;

import java.io.File;
import java.util.List;

import com.artisan.android.task.ArtisanTask;
import com.artisan.android.task.IArtisanTaskListener;
import com.artisan.android.utility.LogUtility;


@SuppressWarnings("rawtypes")
abstract class FileEntry<T>{
	
	public static final int KEY_READ = 0x1;
	public static final int KEY_WRITE = 0x2;
	
	private IFileReadListener<T> readListener;
	private IFileWriteListener writeListener;
	
	private ArtisanTask task = new ArtisanTask();
	
	private List<FileEntry> entries;
	
	public FileEntry(List<FileEntry> entries){
		this.entries = entries;
	}
	
	public IFileReadListener<T> getReadListener() {
		return readListener;
	}

	public void setReadListener(IFileReadListener<T> readListener) {
		this.readListener = readListener;
	}

	public IFileWriteListener getWriteListener() {
		return writeListener;
	}

	public void setWriteListener(IFileWriteListener writeListener) {
		this.writeListener = writeListener;
	}

	public void startWrite(File file ,T source){
		start(KEY_WRITE, file, source);
	}
	
	public void startRead(File file){
		start(KEY_READ,file,null);
	}
	
	public void start(int key,File file ,T source){
		ProcessFile processFile = new ProcessFile(file, source);
		task.runOnThreadPool(key, processFile);
	}
	
	public void destory(){
		task.destory();
	}
	
	class ProcessFile implements IArtisanTaskListener<Integer>{
		private T source;
		private File file;
		public ProcessFile(File file ,T source){
			this.file = file;
			this.source = source;
		}
		
		@Override
		public void onStarted(Integer params) {
			LogUtility.d("File process start...");
			switch (params) {
			case KEY_READ:
				if (null != readListener) {
					readListener.onStarted(file);
				}
				break;
			case KEY_WRITE:
				if (null != writeListener) {
					writeListener.onStarted(file);
				}
				break;
			default:
				break;
			}
		}

		@Override
		public Object onBackground(Integer params) {
			LogUtility.d("File process doing...");
			switch (params) {
			case KEY_READ:
				T result = read(file);
				return result;
			case KEY_WRITE:
				write(file, source);
				break;
			default:
				break;
			}
			return null;
		}

		@SuppressWarnings("unchecked")
		@Override
		public void onSucceeded(Integer params, Object result) {
			entries.remove(FileEntry.this);
			LogUtility.d("File process success...");
			switch (params) {
			case KEY_READ:
				if (null != readListener) {
					readListener.onSucceeded(file,(T) result);
				}
				break;
			case KEY_WRITE:
				if (null != writeListener) {
					writeListener.onSucceeded(file);
				}
				break;
			default:
				break;
			}
		}

		@Override
		public void onFailed(Integer params, Throwable throwable) {
			entries.remove(FileEntry.this);
			LogUtility.d("File process fail...",throwable);
			switch (params) {
			case KEY_READ:
				if (null != readListener) {
					readListener.onFailed(file,throwable);
				}
				break;
			case KEY_WRITE:
				if (null != writeListener) {
					writeListener.onFailed(file,throwable);
				}
				break;
			default:
				break;
			}
		}
	}
	
	public abstract void write(File file,T source);
	public abstract T read(File file);
}
