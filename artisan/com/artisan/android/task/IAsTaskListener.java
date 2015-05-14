package com.artisan.android.task;

import android.os.Bundle;

public interface IAsTaskListener{
	void onAyncTaskStarted(int key,Bundle bundle);
	
	Object onAyncTaskBackground(int key,Bundle bundle);
	
	void onAyncTaskSucceeded(int key,Bundle bundle,Object result);
	
	void onAyncTaskFailed(int key,Bundle bundle,Throwable throwable);
}
