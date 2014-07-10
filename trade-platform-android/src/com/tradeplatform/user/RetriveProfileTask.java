package com.tradeplatform.user;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.trade_platform.core.R;
import com.tradeplatform.core.AndroidBugUtils;
import com.tradeplatform.web.TradePlatformWebClient;

public class RetriveProfileTask extends AsyncTask<String, Void, UserProfile> {
	private String token;
	private Context mContext;
	private RelativeLayout relativeLayout;
	private ListView addressListView;

	public RetriveProfileTask(String token, Context mContext,
			RelativeLayout relativeLayout, ListView addressListView) {
		this.mContext = mContext;
		this.token = token;
		this.relativeLayout = relativeLayout;
		this.addressListView = addressListView;
	}

	@Override
	protected UserProfile doInBackground(String... params) {
		try {
			return TradePlatformWebClient.getUserProfile(token);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	protected void onPostExecute(UserProfile userProfile) {
		if (userProfile != null) {
			EditText etUserName = (EditText) relativeLayout
					.findViewById(R.id.UserNameText);
			etUserName.setText(userProfile.getName());
			Spinner etUserGender = (Spinner) relativeLayout
					.findViewById(R.id.UserGenderText);
			if (userProfile.getGender().equals("M")) {
				etUserGender.setSelection(0);
			} else {
				etUserGender.setSelection(1);
			}

			EditText etBirthday = (EditText) relativeLayout
					.findViewById(R.id.BirthdayText);
			etBirthday.setText(userProfile.getBirthday());
			EditText etUserCellPhone = (EditText) relativeLayout
					.findViewById(R.id.UserCellPhoneText);
			etUserCellPhone.setText(userProfile.getPhone());
			EditText etSelfDescription = (EditText) relativeLayout
					.findViewById(R.id.SelfDescriptionText);
			etSelfDescription.setText(userProfile.getDescription());

			AddressItemAdapter addressListAdapter = new AddressItemAdapter(
					mContext);

			if (userProfile.getAddresses() != null) {
				for (Address addr : userProfile.getAddresses()) {
					addressListAdapter.add(addr);
				}
			}
			addressListView.setAdapter(addressListAdapter);
			AndroidBugUtils.setListViewHeightBasedOnChildren(addressListView);

		} else {
			Toast.makeText(mContext, R.string.profile_empty, Toast.LENGTH_SHORT)
					.show();
		}
	}
}