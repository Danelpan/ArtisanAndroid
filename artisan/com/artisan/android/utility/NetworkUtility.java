package com.artisan.android.utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public final class NetworkUtility {

	private NetworkUtility() {}

	public static final boolean isNetworkOnline(Context context){
		if(null == context){
			throw new NullPointerException();
		}
		ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);   
		if (null == cm) {
			return false;
		} else {
			NetworkInfo[] info = cm.getAllNetworkInfo();
			if (null == info || info.length<=0) {
				return false;
			}
			for (int i = 0; i < info.length; i++) {
				if (info[i].getState() == NetworkInfo.State.CONNECTED) {
					return true;
				}
			}
		}
		return false;   
	}

	public static final NetworkInfo getNetworkInfo(Context context){
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo;
	}
	
	public static final boolean isWifiNetworkOnline(Context context){
		if(null == context){
			throw new NullPointerException();
		}
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if(null == info){
			return false;
		}else{
			if(info.isAvailable()){
				return true;
			}
		}
		return false;
	}

	public static final boolean isMobileNetworkOnline(Context context){
		if(null == context){
			throw new NullPointerException();
		}
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if(null == info){
			return false;
		}else{
			if(info.isAvailable()){
				return true;
			}
		}
		return false;
	}
}
