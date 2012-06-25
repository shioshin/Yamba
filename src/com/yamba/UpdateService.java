package com.yamba;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class UpdateService extends Service {
	static final String TAG = "UpdaterService";

	static final int DELAY = 60000; // a minute
	private boolean runFlag = false; //
	private Updater updater;

	@Override
	public void onCreate() {
		super.onCreate();
		this.updater = new Updater();
		Log.d(TAG, "onCreated");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		this.runFlag = false;
		this.updater.interrupt();
		this.updater = null;
		Log.d(TAG, "onDestroyed");
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		this.runFlag = true;
		this.updater.start();
		Log.d(TAG, "onStarted");
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	private class Updater extends Thread {
		public Updater() {
			super("UpdaterService-Updater");
		}

		public void run() {
			UpdateService updaterService = UpdateService.this;
			while (updaterService.runFlag) {
				Log.d(TAG, "Updater running");
				try {
					// Some work goes here...
					Log.d(TAG, "Updater ran");
					Thread.sleep(DELAY);
				} catch (InterruptedException e) {
					updaterService.runFlag = false;
				}
			}
		}
	}

}
