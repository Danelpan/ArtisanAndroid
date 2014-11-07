package com.artisan.android.file;

import java.io.File;

public interface IFileReadListener<Result> {
	void onStarted();
	
	void onSucceeded(File file,Result result);
	
	void onFailed(Throwable throwable);
}
