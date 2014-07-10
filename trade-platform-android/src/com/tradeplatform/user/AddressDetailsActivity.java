package com.tradeplatform.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.trade_platform.core.R;

public class AddressDetailsActivity extends Activity {

	public static final String ADDRESS_ID = "addressId"; // The request code

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		String userToken = UserTokenUtil.getUserToken(this);
		final String addressId = intent.getStringExtra(ADDRESS_ID);

		if (userToken != null) {
			setContentView(R.layout.address_details);
			Spinner country = (Spinner) findViewById(R.id.newAddressCountryText);
			ArrayAdapter adapter = new ArrayAdapter(this,
					android.R.layout.simple_spinner_item,
					Address.getAvaliableCountries());
			country.setAdapter(adapter);
			TextView addressDetailsLable = (TextView) findViewById(R.id.addressDetailsLable);
			if (addressId != null) {
				addressDetailsLable.setText(getString(R.string.tipEditAddress));
				new RetriveAddressTask(
						addressId,
						this,
						(RelativeLayout) findViewById(R.id.addressDetailsLayout))
						.execute();
			}
			Button submit = (Button) findViewById(R.id.saveAddressButton);
			submit.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					new InsertUpdateUserAddressTask(

							UserTokenUtil
									.getUserToken(AddressDetailsActivity.this),
							addressId,
							getApplicationContext(),
							(RelativeLayout) findViewById(R.id.addressDetailsLayout))
							.execute();

				}
			});

		}
	}
}
