package com.artisan.android.file.entries;

import java.io.File;

import android.graphics.Bitmap;

import com.artisan.android.file.IAsFileEntry;
import com.artisan.android.utility.BitmapUtility;

public class BitmapFileEntry implements IAsFileEntry<Bitmap>{

	@Override
	public void onWrite(File file, Bitmap source) {
		BitmapUtility.bitmap2File(source, file);
	}

	@Override
	public Bitmap onRead(File file) {
		return BitmapUtility.decodeFromFile(file.getAbsolutePath());
	}

}
