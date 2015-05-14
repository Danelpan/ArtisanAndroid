package com.artisan.android.task;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import android.annotation.SuppressLint;
import android.os.Bundle;

public final class AsAsyncTask {
	
	private Map<Integer, AsFutureTask> mAsFutureTasks;
	private Executor mDefaultTaskExecutor = null;
	private int mDefaultTaskKey = 0x1;
	@SuppressLint("UseSparseArrays")
	private AsAsyncTask(Builder builder){
		mDefaultTaskExecutor = builder.defaultTaskExecutor;
		mDefaultTaskKey = builder.defaultTaskKey;
		mAsFutureTasks = Collections.synchronizedMap(new HashMap<Integer, AsFutureTask>());
	}
	
	public final int doAsyncTask(IAsTaskListener listener){
		return doAsyncTask(mDefaultTaskKey,listener);
	}
	
	public final  int doAsyncTask(int key,IAsTaskListener listener){
		return doAsyncTask(key,null, listener);
	}
	
	public final int doAsyncTask(Bundle bundle,IAsTaskListener listener){
		return doAsyncTask(0x1,bundle, listener);
	}
	
	public final int doAsyncTask(int key,Bundle bundle,IAsTaskListener listener){
		return doAsyncTask(key,bundle,-1L, listener);
	}
	
	public final int doAsyncTask(int key,Bundle bundle,long timeout,IAsTaskListener listener){
		AsFutureTask task = initializeAsyncTask(key,bundle, timeout, listener);
		mDefaultTaskExecutor.execute(task);
		return task.hashCode();
	}
	
	public synchronized final void cancelAllTasks(){
		Iterator<Entry<Integer, AsFutureTask>> iterator = mAsFutureTasks.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<Integer, AsFutureTask> entry = (Map.Entry<Integer, AsFutureTask>) iterator.next();
			AsFutureTask task = entry.getValue();
			task.cancel();
		}
		mAsFutureTasks.clear();
	}
	
	public final void cancel(int key){
		AsFutureTask task = mAsFutureTasks.get(key);
		if (null != task) {
			task.cancel();
			mAsFutureTasks.remove(key);
		}
	}
	
	private final AsFutureTask initializeAsyncTask(int key,Bundle bundle,long timeout,IAsTaskListener listener){
		SimpleAsAsyncTask asyncTask = new SimpleAsAsyncTask(listener);
		AsTaskCallable callable = new AsTaskCallable(key,bundle, asyncTask);
		AsFutureTask asFutureTask = new AsFutureTask(callable,timeout);
		asyncTask.setAsFutureTask(asFutureTask);
		return asFutureTask;
	}
	
	public static class Builder{
		private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
		private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
		private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
		private static final int KEEP_ALIVE = 1;
		private BlockingQueue<Runnable> sPoolWorkQueue = new LinkedBlockingQueue<Runnable>(128);
		private ThreadFactory sThreadFactory = new ThreadFactory() {
			private final AtomicInteger mCount = new AtomicInteger(1);

			public Thread newThread(Runnable r) {
				return new Thread(r, "AsAsyncTask #" + mCount.getAndIncrement());
			}
		};
		private Executor defaultTaskExecutor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE, TimeUnit.SECONDS,sPoolWorkQueue, sThreadFactory);

		private int defaultTaskKey = 0x1; 
		public Builder taskExecutor(Executor executor){
			this.defaultTaskExecutor = executor;
			return this;
		}
		
		public Builder taskDefaultKey(int key){
			defaultTaskKey = key;
			return this;
		}
		
		public AsAsyncTask build(){
			return new AsAsyncTask(this);
		}
	}
	
	private class SimpleAsAsyncTask implements IAsTaskListener{
		
		private IAsTaskListener mAsTaskListener;
		private AsFutureTask mAsFutureTask;
		private int mHashCode;
		
		public SimpleAsAsyncTask(IAsTaskListener asTaskListener) {
			super();
			this.mAsTaskListener = asTaskListener;
		}

		public void setAsFutureTask(AsFutureTask asFutureTask) {
			this.mAsFutureTask = asFutureTask;
			this.mHashCode = mAsFutureTask.hashCode();
			mAsFutureTasks.put(mHashCode, mAsFutureTask);
		}

		@Override
		public void onAyncTaskStarted(int key, Bundle bundle) {
			if (null != mAsTaskListener) {
				mAsTaskListener.onAyncTaskStarted(key, bundle);
			}
		}

		@Override
		public Object onAyncTaskBackground(int key, Bundle bundle) {
			if (null != mAsTaskListener) {
				return mAsTaskListener.onAyncTaskBackground(key, bundle);
			}
			return null;
		}

		@Override
		public void onAyncTaskSucceeded(int key, Bundle bundle, Object result) {
			if (null != mAsTaskListener) {
				mAsTaskListener.onAyncTaskSucceeded(key, bundle, result);
			}
			mAsFutureTasks.remove(mHashCode);
		}

		@Override
		public void onAyncTaskFailed(int key, Bundle bundle, Throwable throwable) {
			if (null != mAsTaskListener) {
				mAsTaskListener.onAyncTaskFailed(key, bundle, throwable);
			}
			mAsFutureTasks.remove(mHashCode);
		}
	}
}
