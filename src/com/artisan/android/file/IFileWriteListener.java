package com.artisan.android.file;

import java.io.File;


public interface IFileWriteListener {
	void onStarted(File file);
	
	void onSucceeded(File file);
	
	void onFailed(File file,Throwable throwable);
}
