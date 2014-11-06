package com.artisan.android.task;

import java.util.concurrent.Callable;

class ArtisanCallable<Params> implements Callable<Object> {
	
	private IArtisanTaskListener<Params> listener;
	private Params params;
	public ArtisanCallable(Params params,IArtisanTaskListener<Params> listener){
		this.params = params;
		this.listener = listener;
	}
	
	public void onStarted(){
		listener.onStarted(params);
	}
	
	@Override
	public Object call() throws Exception {
		return listener.onBackground(params);
	}
	
	public void onSucceded(Object result){
		listener.onSucceeded(params, result);
	}
	
	public void onFailed(Throwable throwable){
		listener.onFailed(params, throwable);
	}
	
}
