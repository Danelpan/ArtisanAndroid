package com.artisan.android.test;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.artisan.android.R;
import com.artisan.android.task.ArtisanTask;
import com.artisan.android.task.IArtisanTaskListener;

public class MainActivity extends Activity implements IArtisanTaskListener<Integer> {
	TextView textView;
	int index = 0;

	ArtisanTask task;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		textView = (TextView) findViewById(R.id.tv_text);
		textView.setText(index + "");
		task = new ArtisanTask();
		task.runOnThread(index,this);
	}

	@Override
	public void onStarted(Integer params) {
		Log.e("MainActivity", "task started..." + params);
	}

	@Override
	public Object onBackground(Integer params) {
		Log.e("MainActivity", "task background..." + params);
		try {
			Thread.sleep(1000);
			index++;
		} catch (InterruptedException e) {

		}
		return null;
	}

	@Override
	public void onSucceeded(Integer params, Object result) {
		Log.e("MainActivity", "task succeeded..." + (params - 1));
		textView.setText(index + "");
		task.runOnThread(index,this);
	}

	@Override
	public void onFailed(Integer params, Throwable throwable) {
		Log.e("MainActivity", "task failed..."+params);
		throwable.printStackTrace();
	}

	@Override
	protected void onDestroy() {
		task.destory();
		super.onDestroy();
	}

}
