package com.artisan.android.file;

import java.io.File;

public interface IFileReadListener<Result> {
	void onStarted(File file);
	
	void onSucceeded(File file,Result result);
	
	void onFailed(File file,Throwable throwable);
}
