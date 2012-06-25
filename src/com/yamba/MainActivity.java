package com.yamba;

import winterwell.jtwitter.Twitter;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener,
		TextWatcher {

	private static final String TAG = "StatusActivity";
	private final int MAX_LENGTH = 140;
	EditText editText;
	Button button;
	TextView textCount;
	Twitter twitter;

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		editText = (EditText) findViewById(R.id.editText);
		button = (Button) findViewById(R.id.buttonUpdate);
		textCount = (TextView) findViewById(R.id.textCount);
		button.setOnClickListener(this);

		textCount.setText(Integer.toString(MAX_LENGTH));
		textCount.setTextColor(Color.GREEN);
		editText.addTextChangedListener(this);

		twitter = new Twitter("student", "password"); //
		twitter.setAPIRootUrl("http://yamba.marakana.com/api");

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.itemServiceStart:
			startService(new Intent(this, UpdateService.class)); //
			break;
		case R.id.itemServiceStop:
			stopService(new Intent(this, UpdateService.class)); //
			break;
		case R.id.itemPrefs:
			startActivity(new Intent(this, PrefsActivity.class));
			break;
		}
		return true;
	}

	@Override
	public void onClick(View v) {
//		getTwitter().setStatus(editText.getText().toString());
		Log.d(TAG, "onClicked");
	}

	@Override
	public void afterTextChanged(Editable editText) {
		int count = MAX_LENGTH - editText.length();
		textCount.setText(Integer.toString(count));
		if (count >= 30) {
			textCount.setTextColor(Color.GREEN);
		} else if ((count < 30) && (count > 10)) {
			textCount.setTextColor(Color.YELLOW);
		} else {
			textCount.setTextColor(Color.RED);
		}

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}

	class PostToTwitter extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... params) {
			try {
				winterwell.jtwitter.Status status = twitter
						.updateStatus(params[0]);
				return status.text;
			} catch (Exception e) {
				Log.e(TAG, e.toString());
				e.printStackTrace();
				return "Failed to post";
			}
		}

		@Override
		protected void onPostExecute(String result) {
			Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}

	}
}