package com.tradeplatform.core;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.trade_platform.core.R;
import com.tradeplatform.web.TradePlatformWebClient;

public class LoginActivity extends Activity {
	private EditText username = null;
	private EditText password = null;
	private TextView attempts;
	int counter = 3;
	public static final String USER_TOKEN = "userToken"; // The request code

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		String userToken = intent.getStringExtra(USER_TOKEN);
		if (userToken == null) {
			setContentView(R.layout.activity_login);
			username = (EditText) findViewById(R.id.userNameInput);
			password = (EditText) findViewById(R.id.passwordInput);
			attempts = (TextView) findViewById(R.id.attemptReminderNum);
			attempts.setText(Integer.toString(counter));
		} else {
			setContentView(R.layout.user_profile);
		}
	}

	public void login(View view) throws ClientProtocolException,
			NotFoundException, IOException, JSONException {
		LoginTask runner = new LoginTask();
		runner.execute();
	}

	public void regist(View view) throws ClientProtocolException,
			NotFoundException, IOException, JSONException,
			InterruptedException, ExecutionException, TimeoutException {
		RegistTask runner = new RegistTask();
		runner.execute();
	}

	private class RegistTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... params) {
			try {
				return TradePlatformWebClient.userRegist(username.getText()
						.toString(), password.getText().toString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(String result) {
			if (result != null) {
				Toast.makeText(getApplicationContext(),
						R.string.regist_success, Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getApplicationContext(), R.string.regist_error,
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	private class LoginTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... params) {
			try {
				return TradePlatformWebClient.userLogin(username.getText()
						.toString(), password.getText().toString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(String result) {
			if (result != null) {
				Toast.makeText(getApplicationContext(), R.string.login_success,
						Toast.LENGTH_SHORT).show();
				Intent _result = new Intent();
				_result.putExtra(USER_TOKEN, result);
				setResult(Activity.RESULT_OK, _result);
				finish();
			} else {
				Toast.makeText(getApplicationContext(), R.string.login_error,
						Toast.LENGTH_SHORT).show();
				attempts.setBackgroundColor(Color.RED);
				counter--;
				attempts.setText(Integer.toString(counter));
				if (counter == 0) {
					setResult(Activity.RESULT_CANCELED, null);
					finish();
				}
			}
		}
	}
}
