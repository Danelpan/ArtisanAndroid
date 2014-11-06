package com.artisan.android.task;

public interface IArtisanTaskListener<Params>{
	void onStarted(Params params);
	
	Object onBackground(Params params);
	
	void onSucceeded(Params params,Object result);
	
	void onFailed(Params params,Throwable throwable);
}
