package com.tradeplatform.order;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.trade_platform.core.R;
import com.tradeplatform.aws.S3ImageDownloadTask;

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

		// Drawable d = Drawable.createFromStream(i, "src");
		// storage.ivUserIcon.setImageDrawable(d);
		storage.tvOrderCatagory.setText(entity.orderCatagory);
		storage.tvOrderName.setText(entity.orderName);
		new S3ImageDownloadTask(entity.orderImageBucket, entity.orderImagePath,
				storage.ivOrderImage).execute();
		return row;
	}
}

class ItemStorage {
	ImageView ivOrderImage;
	TextView tvOrderCatagory;
	TextView tvOrderName;
}
