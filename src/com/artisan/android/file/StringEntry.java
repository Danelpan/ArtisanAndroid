package com.artisan.android.file;

import java.io.File;

import com.artisan.android.utility.FileUtility;


class StringEntry extends FileEntry<String>{

	@Override
	public void write(File file, String source) {
		FileUtility.str2File(source, file);
	}

	@Override
	public String read(File file) {
		return FileUtility.file2Str(file);
	}

}
