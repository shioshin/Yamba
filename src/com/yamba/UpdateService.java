package com.yamba;

import java.util.List;

import winterwell.jtwitter.TwitterException;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class UpdateService extends Service {
	static final String TAG = "UpdaterService";

	static final int DELAY = 60000; // a minute
	private boolean runFlag = false; //
	private Updater updater;
	private YambaApplication application;

	@Override
	public void onCreate() {
		super.onCreate();
		this.application = (YambaApplication) getApplication();
		this.updater = new Updater();
		Log.d(TAG, "onCreated");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		this.application.setServiceRunning(false);
		this.runFlag = false;
		this.updater.interrupt();
		this.updater = null;
		Log.d(TAG, "onDestroyed");
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		this.application.setServiceRunning(true);
		this.runFlag = true;
		this.updater.start();
		Log.d(TAG, "onStarted");
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	private class Updater extends Thread {

		List<winterwell.jtwitter.Status> timeline;

		public Updater() {
			super("UpdaterService-Updater");
		}

		@SuppressWarnings("deprecation")
		public void run() {
			UpdateService updaterService = UpdateService.this;
			while (updaterService.runFlag) {
				Log.d(TAG, "Updater running");
				try {
					// Some work goes here...
					timeline = application.getTwitter().getFriendsTimeline();
					// Loop over the timeline and print it out
					for (winterwell.jtwitter.Status status : timeline) { //
						Log.d(TAG, String.format("%s: %s", status.user.name,
								status.text)); //
					}

					Log.d(TAG, "Updater ran");
					Thread.sleep(DELAY);
				} catch (InterruptedException e) {
					updaterService.runFlag = false;
				} catch (TwitterException e) {
					Log.e(TAG, "Failed to connect to twitter service", e);
				}
			}
		}
	}

}
