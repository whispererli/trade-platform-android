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

public class RetriveAddressTask extends AsyncTask<String, Void, Address> {
	private String addressId;
	private Context mContext;
	private RelativeLayout addressDetails;

	public RetriveAddressTask(String addressId, Context mContext,
			RelativeLayout addressDetails) {
		this.mContext = mContext;
		this.addressId = addressId;
		this.addressDetails = addressDetails;
	}

	@Override
	protected Address doInBackground(String... params) {
		try {
			return TradePlatformWebClient.getAddress(addressId);
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
	protected void onPostExecute(Address address) {
		if (address != null) {
			EditText addr1 = (EditText) addressDetails
					.findViewById(R.id.newAddressAddr1Text);
			addr1.setText(address.getAddress1());
			EditText addr2 = (EditText) addressDetails
					.findViewById(R.id.newAddressAddr2Text);
			addr2.setText(address.getAddress2());
			EditText city = (EditText) addressDetails
					.findViewById(R.id.newAddressCityText);
			city.setText(address.getCity());
			Spinner country = (Spinner) addressDetails
					.findViewById(R.id.newAddressCountryText);
			country.setSelection(Address.getCountryIndex(address.getCountry()));
			EditText zipCode = (EditText) addressDetails
					.findViewById(R.id.newAddressZipcodeText);
			zipCode.setText(address.getPost());

		} else {
			Toast.makeText(mContext, R.string.profile_empty, Toast.LENGTH_SHORT)
					.show();
		}
	}
}