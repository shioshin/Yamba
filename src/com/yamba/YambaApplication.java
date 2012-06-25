package com.yamba;

import winterwell.jtwitter.Twitter;
import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

public class YambaApplication extends Application implements
		OnSharedPreferenceChangeListener {
	private static final String TAG = YambaApplication.class.getSimpleName();
	public Twitter twitter; //
	private SharedPreferences prefs;

	@Override
	public void onCreate() {
		super.onCreate();
		this.prefs = PreferenceManager.getDefaultSharedPreferences(this);
		this.prefs.registerOnSharedPreferenceChangeListener(this);
		Log.i(TAG, "onCreated");
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		Log.i(TAG, "onTerminated");
	}

	@SuppressWarnings("deprecation")
	public synchronized Twitter getTwitter() {
		if (twitter == null) {
			String username, password, url;
			username = prefs.getString("username", "");
			password = prefs.getString("password", "");
			url = prefs.getString("url", "http://yamba.marakana.com/api");

			if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)
					&& !TextUtils.isEmpty(url)) {

				this.twitter = new Twitter(username, password);
				this.twitter.setAPIRootUrl(url);
			}
		}
		return this.twitter;
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		this.twitter = null;
	}

}
