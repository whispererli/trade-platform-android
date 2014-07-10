package com.tradeplatform.user;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.trade_platform.core.R;
import com.tradeplatform.web.TradePlatformWebClient;

public class InsertUpdateUserAddressTask extends
		AsyncTask<String, Void, Boolean> {
	private String token;
	private Context mContext;
	private RelativeLayout relativeLayout;
	private String addressId = null;

	public InsertUpdateUserAddressTask(String token, String addressId,
			Context mContext, RelativeLayout relativeLayout) {
		this.mContext = mContext;
		this.token = token;
		this.relativeLayout = relativeLayout;
		this.addressId = addressId;
	}

	@Override
	protected Boolean doInBackground(String... params) {
		try {
			TextView addr1 = (TextView) relativeLayout
					.findViewById(R.id.newAddressAddr1Text);
			TextView addr2 = (TextView) relativeLayout
					.findViewById(R.id.newAddressAddr2Text);
			TextView city = (TextView) relativeLayout
					.findViewById(R.id.newAddressCityText);
			Spinner country = (Spinner) relativeLayout
					.findViewById(R.id.newAddressCountryText);
			TextView post = (TextView) relativeLayout
					.findViewById(R.id.newAddressZipcodeText);
			if (addressId == null) {
				return TradePlatformWebClient.addNewAddress(new Address(addr1
						.getText().toString(), addr2.getText().toString(), city
						.getText().toString(), country.getSelectedItem()
						.toString(), post.getText().toString()), token);
			} else {
				Address addr = new Address(addr1.getText().toString(), addr2
						.getText().toString(), city.getText().toString(),
						country.getSelectedItem().toString(), post.getText()
								.toString());
				addr.setAddressId(addressId);
				return TradePlatformWebClient.updateAddress(addr, token);
			}

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
		return false;
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