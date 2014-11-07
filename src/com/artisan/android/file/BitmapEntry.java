package com.artisan.android.file;

import java.io.File;

import android.graphics.Bitmap;

import com.artisan.android.utility.BitmapUtility;


class BitmapEntry extends FileEntry<Bitmap>{
	
	@Override
	public void write(File file, Bitmap source) {
		BitmapUtility.bitmap2File(source, file);
	}

	@Override
	public Bitmap read(File file) {
		return BitmapUtility.decodeFromFile(file.getAbsolutePath());
	}

}
