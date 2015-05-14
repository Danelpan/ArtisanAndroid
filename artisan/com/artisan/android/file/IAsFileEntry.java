package com.artisan.android.file;

import java.io.File;

public interface IAsFileEntry<T> {
	public void onWrite(File file, T t);

	public T onRead(File file);
}
