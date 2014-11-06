package com.artisan.android.utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.UUID;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public final class DeviceUtility {

	private DeviceUtility() {}

	/**
	 * (randomly generated number) UUID string
	 * @return
	 */
	public final static String obtainUUID() {
		String uuid = UUID.randomUUID().toString();
		return uuid;
	}

	/**
	 * Obtain application's version name,<br>
	 * android:versionName="1.0.0"
	 * @param context
	 * @return
	 */
	public static final String obtainVersionName(Context context) {
		try {
			PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return pi.versionName;
		} catch (NameNotFoundException e) {
			LogUtility.d(e);
		}
		return "unknown";
	}

	/**
	 * Obtain application's version code,<br>
	 * android:versionCode="1"
	 * @param context
	 * @return
	 */
	public static final int obtainVersionCode(Context context) {
		try {
			PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return pi.versionCode;
		} catch (NameNotFoundException e) {
			LogUtility.d(e);
		}
		return 0;
	}

	/**
	 * Returns the unique device ID,<br> 
	 * for example, the IMEI for GSM and the MEID or ESN for CDMA phones.<br>
	 * Return null if device ID is not available. 
	 * <p>
	 * Requires Permission:{@link android.Manifest.permission#READ_PHONE_STATE READ_PHONE_STATE}
	 * @param context
	 * @return
	 */
	public static final String obtainDeviceId(Context context) {
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getDeviceId();
	}

	/**
	 * Obtain current mobile phone screen pixels' size,such as :
	 * <pre>
	 * int size[] = DeviceUtility.obtainDeviceSize(context);
	 * int width = size[0];
	 * int height = size[1];
	 * </pre>
	 * @param context
	 * @return
	 */
	public static final int[] obtainDeviceSize(Context context) {
		int[] size = new int[2];
		DisplayMetrics dm = new DisplayMetrics();
		WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		manager.getDefaultDisplay().getMetrics(dm);
		int width = dm.widthPixels;
		int height = dm.heightPixels;
		size[0] = width;
		size[1] = height;
		return size;
	}

	/**
	 * Obtain CPU usage,example 0.5.
	 * @return
	 */
	public static final float obtainCUPUsage() {
		int totalUsage = 0;
		try {
			java.lang.Process p = Runtime.getRuntime().exec("top -m 15 -d 1 -n 1");
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line = reader.readLine();
			while (line != null) {
				if (!TextUtils.isEmpty(line)) {
					break;
				}
				line = reader.readLine();
			}
			if (!TextUtils.isEmpty(line)) {
				String[] items = line.split(",");
				if (null != items && items.length > 0) {
					for (String item : items) {
						if (!TextUtils.isEmpty(item)) {
							item = item.trim();
							String usage = item.split(" ")[1];
							usage = usage.substring(0, usage.length() - 1);
							int rate = Integer.valueOf(usage);
							totalUsage += rate;
						}
					}
				}
			}
			p.waitFor();
		} catch (Exception e) {
			LogUtility.d(e);
		}
		return totalUsage / 100f;
	}

	/**
	 * Obtain free memory size,unit is MB
	 * @param context
	 * @return
	 */
	public static final long obtainFreeMemory(Context context) {
		MemoryInfo mi = new MemoryInfo();
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		activityManager.getMemoryInfo(mi);
		long availableMegs = mi.availMem / 1024 / 1024;;
		return availableMegs;
	}

	/**
	 * Obtain free external storage directory size,unit is MB
	 * @param context
	 * @deprecated
	 * @return
	 */
	public static final long obtainFreeExternalStorageDirectory() {
		File path = Environment.getExternalStorageDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		long size = blockSize * availableBlocks / 1024 / 1024;
		return size;
	}

	/**
	 * The name of the instruction set (CPU type + ABI convention) of native code.
	 * @return
	 */
	public static final String obtainCpu() {
		String cpu = android.os.Build.CPU_ABI;
		if(TextUtils.isEmpty(cpu)){
			cpu = "";
		}
		return cpu;
	}
	
	/**
	 * Returns the ISO country code equivalent of the current registered
	 * operator's MCC (Mobile Country Code).
	 * <pre>
	 * Availability: Only when user is registered to a network. Result may be
	 * unreliable on CDMA networks to determine if on a CDMA network).
	 * </pre>
	 * <p>
	 * Requires Permission:{@link android.Manifest.permission#READ_PHONE_STATE READ_PHONE_STATE}
	 * @return
	 */
	public static final String obtainNetworkCountryIso(Context context) {
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String country = tm.getNetworkCountryIso();
		if(country == null){
			country = "";
		}
		return country;
	}
	
	/**
	 * The user-visible version string. E.g., "1.0" or "3.4b5".
	 * @return
	 */
	public static final String obtainOsVersion() {
		String os_version = android.os.Build.VERSION.RELEASE;
		if(os_version == null){
			os_version = "";
		}
		return os_version;
	}
	
	/**
	 * The user-visible version string. E.g., "1.0" or "3.4b5".
	 * @return
	 */
	public static final String obtainDeviceModel() {
		String model = android.os.Build.MODEL;
		if(model == null){
			model = "";
		}
		return model;
	}
	
	/**
	 * Returns the ID of this TimeZone, such as America/Los_Angeles, GMT-08:00 or UTC.
	 * @return
	 */
	public static final String obtainTimeZone() {
		String zone = null;
		try {
			zone = Calendar.getInstance().getTimeZone().getID();
		} catch (Exception e) {
			LogUtility.d(e);
		}
		if(zone == null){
			zone = "";
		}
		return zone;
	}
	
	/**
	 * The manufacturer of the product/hardware. 
	 * @return
	 */
	public static final String obtainManufacturer() {
		String manufacturer = android.os.Build.MANUFACTURER;
		if(manufacturer == null){
			manufacturer = "";
		}
		return manufacturer;
	}
	
	/**
	 * The brand (e.g., carrier) the software is customized for, if any. 
	 * @return
	 */
	public static final String obtainDeviceBrand() {
		String brand = android.os.Build.BRAND;
		if(brand == null){
			brand = "";
		}
		return brand;
	}
	
	/**
	 * The user-visible SDK version of the framework; its possible
	 * values are defined in {@link Build.VERSION_CODES}.
	 * @return
	 */
	public static final int obtainSystemSDKInt() {
		return android.os.Build.VERSION.SDK_INT;
	}
	
	/**
	 * Return the MAC address of the WLAN interface macAddress <br>
	 * the MAC address in {@code XX:XX:XX:XX:XX:XX}
	 * @return
	 */
	public static final String obtainDeviceMac(Context context){
		String macAddress = null;
		WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = (null == wm  ? null : wm.getConnectionInfo());
		if (null != info) {
			macAddress = info.getMacAddress();
		}
		if(macAddress == null){
			macAddress = "";
		}
		return macAddress;
	}
	
	/**
	 * Return wifi ip address
	 * @param context
	 * @return
	 */
	public static final String obtainDeviceIp(Context context){
		String IPAddress = null;
		WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = (null == wm ? null : wm.getConnectionInfo());
		if (null != info) {
			IPAddress  = Integer.toString(info.getIpAddress());
		}
		if(IPAddress == null){
			IPAddress = "";
		}
		return IPAddress;
	}
	
	/**
	 * Returns the unique subscriber ID, for example, the IMSI for a GSM phone.
	 * Return null if it is unavailable.
	 * <p>
	 * Requires Permission:{@link android.Manifest.permission#READ_PHONE_STATE READ_PHONE_STATE}
	 * @return 
	 */
	public static String obtainIMSI(Context context) {
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String imsi = tm.getSubscriberId();
		if(imsi ==null){
			imsi = "";
		}
		return imsi; 
	}
}
