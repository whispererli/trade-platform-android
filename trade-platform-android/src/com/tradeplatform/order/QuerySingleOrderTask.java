package com.tradeplatform.order;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.trade_platform.core.R;
import com.tradeplatform.aws.S3ImageListDownloadTask;
import com.tradeplatform.web.TradePlatformWebClient;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

public class QuerySingleOrderTask extends AsyncTask<Void, Void, UserOrder> {
	private ArrayAdapter<Bitmap> adapter = null;
    private String orderId = null;
    private Context mContext;

	public QuerySingleOrderTask(ArrayAdapter<Bitmap> adapter,Context mContext, String orderId) {
		this.adapter = adapter;
        this.orderId = orderId;
        this.mContext = mContext;
	}

	@Override
	protected UserOrder doInBackground(Void... params) {
		UserOrder order = null;
		try {
			order = TradePlatformWebClient.queryOrder(orderId);
		} catch (IOException e) {
			Log.e("LoadOrdersTask", e.getMessage());
		} catch (JSONException e) {
			Log.e("LoadOrdersTask", e.getMessage());
		}
		return order;
	}

	@Override
	protected void onPostExecute(UserOrder order) {
		if (order != null) {
            //order
            List<String> images = order.getListOfImagesPath();
            if (images != null && images.size() > 0) {
                new S3ImageListDownloadTask("trade-platform-order-images",
                        images, adapter).execute();
            } else {
                adapter.add(BitmapFactory.decodeResource(
                        mContext.getResources(), R.drawable.default_order));
            }
		}
	}
}