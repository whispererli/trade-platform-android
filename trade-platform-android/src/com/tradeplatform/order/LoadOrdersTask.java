package com.tradeplatform.order;

import java.io.IOException;
import java.util.List;

import org.json.JSONException;

import android.os.AsyncTask;
import android.util.Log;

import com.tradeplatform.web.TradePlatformWebClient;

public class LoadOrdersTask extends AsyncTask<Void, Void, List<UserOrder>> {
	private OrderItemAdapter adapter = null;

	public LoadOrdersTask(OrderItemAdapter adapter) {
		this.adapter = adapter;
	}

	@Override
	protected List<UserOrder> doInBackground(Void... params) {
		List<UserOrder> orders = null;
		try {
			orders = TradePlatformWebClient.queryAllOrders();
		} catch (IOException e) {
			Log.e("LoadOrdersTask", e.getMessage());
		} catch (JSONException e) {
			Log.e("LoadOrdersTask", e.getMessage());
		}
		return orders;
	}

	@Override
	protected void onPostExecute(List<UserOrder> orders) {
		if (orders != null) {
			for (UserOrder order : orders) {
				adapter.add(order);
			}
		}
	}
}