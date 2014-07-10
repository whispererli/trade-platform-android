package com.tradeplatform.user;

import java.io.IOException;

import org.json.JSONException;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.trade_platform.core.R;
import com.tradeplatform.web.TradePlatformWebClient;

public class LoginTask extends AsyncTask<String, Void, String> {
	private String username;
	private String password;
	private Context mContext;

	LoginTask(String username, String password, Context activityHandler) {
		this.username = username;
		this.password = password;
		this.mContext = activityHandler;
	}

	@Override
	protected String doInBackground(String... params) {
		try {
			return TradePlatformWebClient.userLogin(username, password);
		} catch (IOException e) {
			Log.e("LoginTask", e.getMessage());
		} catch (JSONException e) {
			Log.e("LoginTask", e.getMessage());
		}
		return null;
	}

	// onPostExecute displays the results of the AsyncTask.
	@Override
	protected void onPostExecute(String result) {
		if (result != null) {
			SharedPreferences settings = mContext.getSharedPreferences(
					mContext.getString(R.string.shared_preferences),
					Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = settings.edit();
			editor.putString(mContext.getString(R.string.user_token), result);
			editor.commit();
			Toast.makeText(mContext, R.string.login_success, Toast.LENGTH_SHORT)
					.show();
			((Activity) mContext).finish();
		} else {
			Toast.makeText(mContext.getApplicationContext(),
					R.string.login_error, Toast.LENGTH_SHORT).show();
		}
	}
}