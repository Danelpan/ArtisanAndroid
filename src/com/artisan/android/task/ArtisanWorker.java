package com.artisan.android.task;

import java.util.concurrent.CancellationException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import android.os.Handler;
import android.os.Message;


class ArtisanWorker extends FutureTask<Object> {
	private static final int MESSAGE_POST_RESULT = 0x1;
	private static final int MESSAGE_POST_FAIL = 0x2;
	private ArtisanCallable<?> callable;
	private long timeout = -1;
	
	private InternalHandler handler = new InternalHandler();
	
	private volatile boolean isTimeout = false;
	private volatile boolean destory = false;

	protected ArtisanWorker(ArtisanCallable<?> callable) {
		this(callable, -1);
	}

	protected ArtisanWorker(ArtisanCallable<?> callable, long timeout) {
		super(callable);
		this.callable = callable;
		this.timeout = timeout;
	}

	@Override
	protected void done() {
		if (destory) {
			return;
		}
		
		if (!isTimeout) {
			if (isCancelled()) {
				postResult(MESSAGE_POST_FAIL, new CancellationException());
				return;
			}
			try {
				Object result = get();
				postResult(MESSAGE_POST_RESULT, result);
			} catch (Exception e) {
				postResult(MESSAGE_POST_FAIL, e);
			}
		}
	}

	private void postResult(int what, Object result) {
		Message msg = new Message();
		msg.what = what;
		msg.obj = result;
		handler.sendMessage(msg);
	}

	public void destory(boolean mayDestory){
		this.destory = mayDestory;
	}
	
	public boolean isDestory(){
		return destory;
	}
	
	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		return super.cancel(mayInterruptIfRunning);
	}

	@Override
	public void run() {
		if (destory) {
			return;
		}
		
		if (timeout>0) {
			TimeoutRunnable runnable = new TimeoutRunnable();
			new Thread(runnable).start();
		}
		super.run();
	}

	private class InternalHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MESSAGE_POST_RESULT:
				destory = true;
				callable.onSucceded(msg.obj);
				break;
			case MESSAGE_POST_FAIL:
				destory = true;
				callable.onFailed((Throwable) msg.obj);
				break;
			default:
				break;
			}
		}
	}
	
	private class TimeoutRunnable implements Runnable{

		@Override
		public void run() {
			try {
				get(timeout, TimeUnit.MILLISECONDS);
			} catch (Exception e) {
				if (destory) {
					return;
				}
				
				if (isDone()) {
					return;
				}
				
				if (e instanceof TimeoutException) {
					isTimeout = true;
				}
				
				destory = true;
				
				cancel(true);
				postResult(MESSAGE_POST_FAIL, e);
			}
		}
	}
}
