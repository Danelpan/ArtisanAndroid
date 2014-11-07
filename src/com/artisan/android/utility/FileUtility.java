package com.artisan.android.utility;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import android.os.Environment;
import android.text.TextUtils;

public final class FileUtility {

	private FileUtility() {
	}

	public static final File directory(String path) {
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
		return file;
	}

	public static final File file(String path) throws IOException{
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
		file.createNewFile();
		return file;
	}
	
	public static final synchronized void str2File(String data, File file) {
		str2File(data, file, false);
	}
	
	public static final synchronized void str2File(String data, File file,boolean append) {
		if (TextUtils.isEmpty(data)) {
			throw new NullPointerException("data of String is null");
		}
		FileWriter mWriter = null;
		try {
			mWriter = new FileWriter(file, append);
			mWriter.write(data);
			mWriter.flush();
		} catch (IOException e) {
			LogUtility.d(e);
		} finally {
			StreamUtility.closeStream(mWriter);
		}

	}

	public static final synchronized String file2Str(File file) {
		if (null == file) {
			throw new NullPointerException("File is null");
		}
		FileReader mReader = null;
		try {
			mReader = new FileReader(file);
			char[] cTemp = new char[1024 * 4];
			int i = -1;
			StringBuilder strBuilder = new StringBuilder();
			while ((i = mReader.read(cTemp)) != -1) {
				strBuilder.append(cTemp, 0, i);
			}
			return strBuilder.toString();
		} catch (Exception e) {
			LogUtility.d(e);
		} finally {
			StreamUtility.closeStream(mReader);
		}
		return null;
	}

	public static final synchronized void byte2File(byte[] bs, File file) {
		stream2File(StreamUtility.byte2InputStream(bs), file);
	}

	public static final synchronized void stream2File(InputStream is, File file) {
		BufferedOutputStream out = null;
		BufferedInputStream in = null;
		FileOutputStream outputStream = null;
		try {
			in = new BufferedInputStream(is, 8 * 1024);
			outputStream = new FileOutputStream(file);
			out = new BufferedOutputStream(outputStream, 8 * 1024);
			int b;
			while ((b = in.read()) != -1) {
				out.write(b);
			}
		} catch (Exception e) {
			if (file != null && file.exists()) {
				file.delete();
			}
			file = null;
		} finally {
			StreamUtility.closeStream(out, outputStream, in, is);
		}
	}


	public static final boolean copyFile2File(File from, File to) {
		FileInputStream in = null;
		FileOutputStream out = null;
		try {
			in = new FileInputStream(from);
			out = new FileOutputStream(to);
			byte[] bt = new byte[1024 * 4];
			int count;
			while ((count = in.read(bt)) > 0) {
				out.write(bt, 0, count);
			}
			out.flush();
			return true;
		} catch (IOException e) {
			LogUtility.d(e);
		} finally {
			StreamUtility.closeStream(in, out);
		}
		return false;
	}

	public static final File obatinExternalStorageDirectory(){
		return Environment.getExternalStorageDirectory();
	}
}
