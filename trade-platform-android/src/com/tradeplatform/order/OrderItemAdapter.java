package com.tradeplatform.order;

import java.util.List;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.trade_platform.core.R;
import com.tradeplatform.aws.S3ImageDownloadTask;

public class OrderItemAdapter extends ArrayAdapter<UserOrder> {
	private final LayoutInflater mInflater;
	Context context;

	public OrderItemAdapter(Context context) {
		super(context, 0);
		this.context = context;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	final public View getView(final int position, View convertView,
			ViewGroup parent) {

		OrderViewGroup orderView;
		View row = convertView;

		if (row == null) {
			row = mInflater.inflate(R.layout.order_general, parent, false);

			orderView = new OrderViewGroup();
			orderView.ivOrderImage = (ImageView) row
					.findViewById(R.id.orderImage);
			orderView.tvOrderCatagory = (TextView) row
					.findViewById(R.id.orderCatagory);
			orderView.tvOrderDesc = (TextView) row
					.findViewById(R.id.orderDescription);
			orderView.tvOrderExpPrice = (TextView) row
					.findViewById(R.id.orderExpectPrice);
			orderView.tvOrderExpPlace = (TextView) row
					.findViewById(R.id.orderExpectPlace);
			orderView.tvOrderExpDate = (TextView) row
					.findViewById(R.id.orderExpectDate);
			row.setTag(R.layout.order_general,orderView);
		} else {
			orderView = (OrderViewGroup) row.getTag(R.layout.order_general);
		}

		UserOrder order = getItem(position);

		orderView.tvOrderCatagory.setText(OrderCatagories.getName(order
				.getCatagoryId()));
		orderView.tvOrderDesc.setText(order.getOrderDescription());
		orderView.tvOrderExpPrice.setText(order.getOrderExpectPrice());
		orderView.tvOrderExpPlace.setText(order.getOrderExpectPlace());
		orderView.tvOrderExpDate.setText(order.getOrderExpectDate());

        row.setTag(R.string.order_id, order.getOrderId());

		List<String> images = order.getListOfImagesPath();
		if (images != null && images.size() > 0) {
			new S3ImageDownloadTask("trade-platform-order-images",
					images.get(0), orderView.ivOrderImage).execute();
		} else {
			orderView.ivOrderImage.setImageBitmap(BitmapFactory.decodeResource(
					context.getResources(), R.drawable.default_order));
		}
		return row;
	}
}

class OrderViewGroup {
	TextView tvOrderExpDate;
	TextView tvOrderExpPlace;
	TextView tvOrderExpPrice;
	ImageView ivOrderImage;
	TextView tvOrderCatagory;
	TextView tvOrderDesc;
}
