package com.artisan.android.file;

import java.io.File;


public interface IFileWriteListener {
	void onStarted();
	
	void onSucceeded(File file);
	
	void onFailed(Throwable throwable);
}
