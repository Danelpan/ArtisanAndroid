package com.artisan.android.file;

import java.io.File;

import android.os.Bundle;

import com.artisan.android.task.IAsTaskListener;

public class AsInternalHandler<T> implements IAsTaskListener{
	
	public static final int KEY_READ = 0x1;
	public static final int KEY_WRITE = 0x2;
	
	private IFileReadListener<T> mReadListener = new IFileReadListener<T>() {

		@Override
		public void onStarted(File file) {
			
		}

		@Override
		public void onSucceeded(File file, T result) {
			
		}

		@Override
		public void onFailed(File file, Throwable throwable) {
			
		}
	};
	private IFileWriteListener mWriteListener = new IFileWriteListener() {
		
		@Override
		public void onSucceeded(File file) {
			
		}
		
		@Override
		public void onStarted(File file) {
			
		}
		
		@Override
		public void onFailed(File file, Throwable throwable) {
			
		}
	};
	
	private IAsFileEntry<T> mAsFileEntry;
	private File mFile;
	private T mSource;
	public AsInternalHandler(IAsFileEntry<T> entry,File file,T source){
		this.mAsFileEntry = entry;
		this.mFile = file;
		this.mSource = source;
	}
	
	public void setReadListener(IFileReadListener<T> readListener) {
		this.mReadListener = readListener;
	}

	public void setWriteListener(IFileWriteListener writeListener) {
		this.mWriteListener = writeListener;
	}

	@Override
	public void onAyncTaskStarted(int key, Bundle bundle) {
		switch (key) {
		case KEY_READ:
			mReadListener.onStarted(mFile);
			break;
		case KEY_WRITE:
			mWriteListener.onStarted(mFile);
			break;
		default:
			break;
		}
	}

	@Override
	public Object onAyncTaskBackground(int key, Bundle bundle) {
		switch (key) {
		case KEY_READ:
			T result = mAsFileEntry.onRead(mFile);
			return result;
		case KEY_WRITE:
			mAsFileEntry.onWrite(mFile, mSource);
			break;
		default:
			break;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onAyncTaskSucceeded(int key, Bundle bundle, Object result) {
		switch (key) {
		case KEY_READ:
			if (null != mReadListener) {
				mReadListener.onSucceeded(mFile,(T) result);
			}
			break;
		case KEY_WRITE:
			if (null != mWriteListener) {
				mWriteListener.onSucceeded(mFile);
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onAyncTaskFailed(int key, Bundle bundle, Throwable throwable) {
		switch (key) {
		case KEY_READ:
			if (null != mReadListener) {
				mReadListener.onFailed(mFile,throwable);
			}
			break;
		case KEY_WRITE:
			if (null != mWriteListener) {
				mWriteListener.onFailed(mFile,throwable);
			}
			break;
		default:
			break;
		}
		
	}
}
