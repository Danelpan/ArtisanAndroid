package com.artisan.android.file.entries;

import java.io.File;

import com.artisan.android.file.IAsFileEntry;
import com.artisan.android.utility.FileUtility;

public class StringFileEntry implements IAsFileEntry<String>{
	
	@Override
	public void onWrite(File file, String source) {
		FileUtility.str2File(source, file);
	}

	@Override
	public String onRead(File file) {
		return FileUtility.file2Str(file);
	}

}
