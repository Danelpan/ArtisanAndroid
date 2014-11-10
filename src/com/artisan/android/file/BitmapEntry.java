package com.artisan.android.file;

import java.io.File;
import java.util.List;

import android.graphics.Bitmap;

import com.artisan.android.utility.BitmapUtility;


@SuppressWarnings("rawtypes")
class BitmapEntry extends FileEntry<Bitmap>{
	
	public BitmapEntry(List<FileEntry> entries) {
		super(entries);
	}

	@Override
	public void write(File file, Bitmap source) {
		BitmapUtility.bitmap2File(source, file);
	}

	@Override
	public Bitmap read(File file) {
		return BitmapUtility.decodeFromFile(file.getAbsolutePath());
	}

}
