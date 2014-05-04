package com.tradeplatform.core;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.trade_platform.core.R;

public class OrderItemAdapter extends ArrayAdapter<OrderItem> {
	private final LayoutInflater mInflater;

	public OrderItemAdapter(Context context) {
		super(context, 0);
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	final public View getView(final int position, View convertView,
			ViewGroup parent) {

		ItemStorage storage;
		View row = convertView;

		if (row == null) {
			row = mInflater.inflate(R.layout.order_general, parent, false);

			storage = new ItemStorage();
			storage.ivOrderImage = (ImageView) row
					.findViewById(R.id.orderImage);
			storage.tvOrderCatagory = (TextView) row
					.findViewById(R.id.orderCatagory);
			storage.tvOrderName = (TextView) row.findViewById(R.id.orderName);
			row.setTag(storage);
		} else {
			storage = (ItemStorage) row.getTag();
		}

		OrderItem entity = getItem(position);
		// need do asynchronously.
		// URL m;
		// InputStream i = null;
		// try {
		// m = new URL(entity.UserIconPath);
		// i = (InputStream) m.getContent();
		// } catch (MalformedURLException e1) {
		// e1.printStackTrace();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }

		// Drawable d = Drawable.createFromStream(i, "src");
		// storage.ivUserIcon.setImageDrawable(d);
		storage.tvOrderCatagory.setText(entity.orderCatagory);
		storage.tvOrderName.setText(entity.orderName);

		return row;
	}
}

class ItemStorage {
	ImageView ivOrderImage;
	TextView tvOrderCatagory;
	TextView tvOrderName;
}
