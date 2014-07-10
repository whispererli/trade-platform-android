package com.tradeplatform.user;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.trade_platform.core.R;

public class AddressItemAdapter extends ArrayAdapter<Address> {
	private final LayoutInflater mInflater;
	private final Context mContext;

	public AddressItemAdapter(Context context) {
		super(context, 0);
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mContext = context;
	}

	@Override
	final public View getView(final int position, View convertView,
			ViewGroup parent) {

		View row = convertView;
		TextView tvAddress;
		Button tvDel = null;
		if (row == null) {
			row = mInflater.inflate(R.layout.address_general, parent, false);

			tvAddress = (TextView) row.findViewById(R.id.userAddress);
            row.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(mContext,
							AddressDetailsActivity.class);
					intent.putExtra(AddressDetailsActivity.ADDRESS_ID, v
							.getTag(R.id.deleteAddressButton).toString());
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					mContext.startActivity(intent, null);
				}
			});
			tvDel = (Button) row.findViewById(R.id.deleteAddressButton);
			tvDel.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

				}
			});
			row.setTag(R.id.userAddress,tvAddress);

		} else {
			tvAddress = (TextView) row.getTag(R.id.userAddress);
		}

		Address addr = getItem(position);
		tvAddress.setText(addr.toString());
        row.setTag(R.id.deleteAddressButton,addr.getAddressId());
		tvDel.setTag(addr.getAddressId());

		return row;
	}
}
