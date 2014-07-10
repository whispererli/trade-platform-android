package com.tradeplatform.user;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.trade_platform.core.R;

public class UserActivity extends Activity {
	private EditText username = null;
	private EditText password = null;
	private String userToken = null;
	private ListView addressListView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		userToken = UserTokenUtil.getUserToken(this);

		if (userToken == null) {
			setContentView(R.layout.activity_login);
			username = (EditText) findViewById(R.id.userNameInput);
			password = (EditText) findViewById(R.id.passwordInput);
		} else {
			setContentView(R.layout.user_profile);

			Spinner gender = (Spinner) findViewById(R.id.UserGenderText);
			ArrayAdapter<CharSequence> adapter = ArrayAdapter
					.createFromResource(this, R.array.gender_choices,
							android.R.layout.simple_spinner_item);
			gender.setAdapter(adapter);

			addressListView = (ListView) findViewById(R.id.addressListView);

			Button submitBtn = (Button) findViewById(R.id.saveProfileButton);
			submitBtn.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					new InsertUpdateUserProfileTask(
							userToken,
							getApplicationContext(),
							(RelativeLayout) findViewById(R.id.userProfileLayout))
							.execute();
				}
			});
			Button addAddrBtn = (Button) findViewById(R.id.addNewAddressButton);
			addAddrBtn.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					Intent intent = new Intent(UserActivity.this,
							AddressDetailsActivity.class);
					startActivity(intent);
				}
			});

			new RetriveProfileTask(userToken, getApplicationContext(),
					(RelativeLayout) findViewById(R.id.userProfileLayout),
					addressListView).execute();
		}
	}

	public void login(View view) throws ClientProtocolException,
			NotFoundException, IOException, JSONException {
		LoginTask runner = new LoginTask(username.getText().toString(),
				password.getText().toString(), this);
		runner.execute();
	}

	public void regist(View view) throws ClientProtocolException,
			NotFoundException, IOException, JSONException,
			InterruptedException, ExecutionException, TimeoutException {
		RegistTask runner = new RegistTask(username.getText().toString(),
				password.getText().toString(), this);
		runner.execute();
	}
}
