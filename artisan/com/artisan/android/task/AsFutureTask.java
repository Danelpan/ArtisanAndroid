package com.artisan.android.task;

import java.util.concurrent.CancellationException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

import android.os.Handler;
import android.os.Message;

import com.artisan.android.log.AsLog;

class AsFutureTask extends FutureTask<Object> {
	private static final int MESSAGE_POST_RESULT = 0x1;
	private static final int MESSAGE_POST_FAIL = 0x2;
	private AsTaskCallable mCallable;
	private long mTimeout = -1;
	private TimeoutRunnable mTimeoutRunnable;
	private final InternalHandler mInternalHandler = new InternalHandler();
	private volatile boolean isTimeout = false;
	private final AtomicBoolean mCancelled = new AtomicBoolean();

	protected AsFutureTask(AsTaskCallable callable) {
		this(callable, -1);
	}

	protected AsFutureTask(AsTaskCallable callable, long timeout) {
		super(callable);
		this.mCallable = callable;
		this.mTimeout = timeout;
		if (timeout > 0) {
			mTimeoutRunnable = new TimeoutRunnable();
		}
	}

	@Override
	protected void done() {
		if (!isTimeout) {
			if (isCancelled()) {
				return;
			}
			Object result = null;
			int code = MESSAGE_POST_FAIL;
			try {
				result = get();
				code = MESSAGE_POST_RESULT;
			} catch (Exception e) {
				AsLog.d(e);
				result = e;
			}
			if (isCancelled()) {
				return;
			}
			postResult(code, result);
		}
	}

	public boolean cancel() {
		boolean isSuccessed = super.cancel(true);
		if (isSuccessed) {
			mCancelled.set(true);
			CancellationException e = new CancellationException("Task had cancelled");
			postResult(MESSAGE_POST_FAIL, e);
		}else{
			String message = String.format("Task id:%s cancel fail.", toString());
			AsLog.d(message);
		}
		return isSuccessed;
	}

	public final boolean isCancelled() {
		return mCancelled.get();
	}

	@Override
	public void run() {
		if (null != mTimeoutRunnable) {
			mInternalHandler.postDelayed(mTimeoutRunnable, mTimeout);
		}
		super.run();
	}

	private void postResult(int what, Object result) {
		if (null != mTimeoutRunnable) {
			mInternalHandler.removeCallbacks(mTimeoutRunnable);
		}
		Message msg = new Message();
		msg.what = what;
		msg.obj = result;
		mInternalHandler.sendMessage(msg);
	}

	private class InternalHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MESSAGE_POST_RESULT:
				mCallable.onSucceded(msg.obj);
				break;
			case MESSAGE_POST_FAIL:
				mCallable.onFailed((Throwable) msg.obj);
				break;
			default:
				break;
			}
		}
	}

	private class TimeoutRunnable implements Runnable {

		@Override
		public void run() {
			if (isDone()) {
				return;
			}
			if (isCancelled()) {
				return;
			}
			isTimeout = true;
			TimeoutException e = new TimeoutException("Task had timeout.");
			postResult(MESSAGE_POST_FAIL, e);
		}
	}
}
