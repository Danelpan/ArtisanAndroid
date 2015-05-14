package com.artisan.android.task;

import java.util.concurrent.Callable;

import android.os.Bundle;

class AsTaskCallable implements Callable<Object> {
	
	private IAsTaskListener mAsTaskListener;
	private int mkey;
	private Bundle mBundle;
	public AsTaskCallable(int key,Bundle bundle,IAsTaskListener listener){
		this.mkey = key;
		this.mBundle = bundle;
		this.mAsTaskListener = listener;
		onStarted();
	}
	
	public void onStarted(){
		mAsTaskListener.onAyncTaskStarted(mkey,mBundle);
	}
	
	@Override
	public Object call() throws Exception {
		return mAsTaskListener.onAyncTaskBackground(mkey,mBundle);
	}
	
	public void onSucceded(Object result){
		mAsTaskListener.onAyncTaskSucceeded(mkey,mBundle, result);
	}
	
	public void onFailed(Throwable throwable){
		mAsTaskListener.onAyncTaskFailed(mkey,mBundle, throwable);
	}
	
}
