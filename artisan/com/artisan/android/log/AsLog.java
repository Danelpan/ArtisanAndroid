package com.artisan.android.log;

import android.text.TextUtils;
import android.util.Log;

public final class AsLog {

	private static final String TAG = "artisan state";

	private static boolean isLogEnable = true;

	private AsLog() {
	}

	public static final boolean isLogEnable() {
		return isLogEnable;
	}

	public static final void setLogEnable(boolean isEnable) {
		AsLog.isLogEnable = isEnable;
	}

	public static final void d(String msg) {
		d(TAG, msg);
	}

	public static final void d(Throwable throwable) {
		d(Log.getStackTraceString(throwable));
	}

	public static final void d(String tag, String msg) {
		Object[] objects = null;
		d(tag, msg, objects);
	}

	public static final void d(String tag, Throwable throwable) {
		d(tag, Log.getStackTraceString(throwable));
	}

	public static final void d(String tag, String msg, Throwable throwable) {
		d(tag, msg + '\n' + Log.getStackTraceString(throwable));
	}

	public static final void d(String tag, String msg, Object... args) {
		if (!isLogEnable())
			return;
		String result = obtainMessage(msg, args);
		Log.d(tag, result);
	}

	public static final void e(String msg) {
		e(TAG, msg);
	}

	public static final void e(Throwable throwable) {
		e(Log.getStackTraceString(throwable));
	}

	public static final void e(String tag, String msg) {
		Object[] objects = null;
		e(tag, msg, objects);
	}

	public static final void e(String tag, Throwable throwable) {
		e(tag, Log.getStackTraceString(throwable));
	}

	public static final void e(String tag, String msg, Throwable throwable) {
		e(tag, msg + '\n' + Log.getStackTraceString(throwable));
	}

	public static final void e(String tag, String msg, Object... args) {
		if (!isLogEnable())
			return;
		String result = obtainMessage(msg, args);
		Log.e(tag, result);
	}

	public static final void i(String msg) {
		i(TAG, msg);
	}

	public static final void i(Throwable throwable) {
		i(Log.getStackTraceString(throwable));
	}

	public static final void i(String tag, String msg) {
		Object[] objects = null;
		i(tag, msg, objects);
	}

	public static final void i(String tag, Throwable throwable) {
		i(tag, Log.getStackTraceString(throwable));
	}

	public static void i(String tag, String msg, Throwable throwable) {
		i(tag, msg + '\n' + Log.getStackTraceString(throwable));
	}

	public static final void i(String tag, String msg, Object... args) {
		if (!isLogEnable())
			return;
		String result = obtainMessage(msg, args);
		Log.i(tag, result);
	}

	public static final void v(String msg) {
		v(TAG, msg);
	}

	public static final void v(Throwable throwable) {
		v(Log.getStackTraceString(throwable));
	}

	public static final void v(String tag, String msg) {
		Object[] objects = null;
		v(tag, msg, objects);
	}

	public static final void v(String tag, Throwable throwable) {
		v(tag, Log.getStackTraceString(throwable));
	}

	public static final void v(String tag, String msg, Throwable throwable) {
		v(tag, msg + '\n' + Log.getStackTraceString(throwable));
	}

	public static final void v(String tag, String msg, Object... args) {
		if (!isLogEnable())
			return;
		String result = obtainMessage(msg, args);
		Log.v(tag, result);
	}

	public static final void w(String msg) {
		v(TAG, msg);
	}

	public static final void w(Throwable throwable) {
		v(Log.getStackTraceString(throwable));
	}

	public static final void w(String tag, String msg) {
		Object[] objects = null;
		v(tag, msg, objects);
	}

	public static final void w(String tag, Throwable throwable) {
		v(tag, Log.getStackTraceString(throwable));
	}

	public static final void w(String tag, String msg, Throwable throwable) {
		v(tag, msg + '\n' + Log.getStackTraceString(throwable));
	}

	public static final void w(String tag, String msg, Object... args) {
		if (!isLogEnable())
			return;
		String result = obtainMessage(msg, args);
		Log.w(tag, result);
	}

	public static final void wtf(String msg) {
		v(TAG, msg);
	}

	public static final void wtf(Throwable throwable) {
		v(Log.getStackTraceString(throwable));
	}

	public static final void wtf(String tag, String msg) {
		Object[] objects = null;
		v(tag, msg, objects);
	}

	public static final void wtf(String tag, Throwable throwable) {
		v(tag, Log.getStackTraceString(throwable));
	}

	public static final void wtf(String tag, String msg, Throwable throwable) {
		v(tag, msg + '\n' + Log.getStackTraceString(throwable));
	}

	public static final void wtf(String tag, String msg, Object... args) {
		if (!isLogEnable())
			return;
		String result = obtainMessage(msg, args);
		Log.wtf(tag, result);
	}

	private static final String obtainMessage(String msg, Object... args) {
		String message = "";
		if (TextUtils.isEmpty(msg)) {
			message = "";
		} else {
			message = String.format(msg, args);
		}
		return message;
	}
}
