package com.tradeplatform.user;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.trade_platform.core.R;
import com.tradeplatform.web.TradePlatformWebClient;

public class InsertUpdateUserProfileTask extends
		AsyncTask<String, Void, Boolean> {
	private String token;
	private Context mContext;
	private RelativeLayout relativeLayout;

	public InsertUpdateUserProfileTask(String token, Context mContext,
			RelativeLayout relativeLayout) {
		this.mContext = mContext;
		this.token = token;
		this.relativeLayout = relativeLayout;
	}

	@Override
	protected Boolean doInBackground(String... params) {
		try {
			EditText etUserName = (EditText) relativeLayout
					.findViewById(R.id.UserNameText);
			Spinner etUserGender = (Spinner) relativeLayout
					.findViewById(R.id.UserGenderText);
			String gender = "F";
			if (etUserGender.getSelectedItem().equals("男")) {
				gender = "M";
			}
			EditText etBirthday = (EditText) relativeLayout
					.findViewById(R.id.BirthdayText);
			EditText etUserCellPhone = (EditText) relativeLayout
					.findViewById(R.id.UserCellPhoneText);
			EditText etSelfDescription = (EditText) relativeLayout
					.findViewById(R.id.SelfDescriptionText);
			return TradePlatformWebClient.InsertOrUpdateUserProfile(token,
					new UserProfile(etUserName.getText().toString(), etBirthday
							.getText().toString(), etSelfDescription.getText()
							.toString(), gender, etUserCellPhone.getText()
							.toString(), null, null));

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	// onPostExecute displays the results of the AsyncTask.
	@Override
	protected void onPostExecute(Boolean result) {
		if (result) {
			Toast.makeText(mContext, R.string.update_success,
					Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(mContext, R.string.update_failed, Toast.LENGTH_SHORT)
					.show();
		}
	}
}