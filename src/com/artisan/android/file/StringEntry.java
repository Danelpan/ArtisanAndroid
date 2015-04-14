package com.artisan.android.file;

import java.io.File;
import java.util.List;

import com.artisan.android.utility.FileUtility;


@SuppressWarnings("rawtypes")
class StringEntry extends FileEntry<String>{

	public StringEntry(){}
	
	public StringEntry(List<FileEntry> entries) {
		super(entries);
	}

	@Override
	public void write(File file, String source) {
		FileUtility.str2File(source, file);
	}

	@Override
	public String read(File file) {
		return FileUtility.file2Str(file);
	}

}
