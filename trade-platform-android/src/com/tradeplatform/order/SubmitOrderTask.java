package com.tradeplatform.order;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.trade_platform.core.R;
import com.tradeplatform.aws.S3FileListUploadTask;
import com.tradeplatform.web.TradePlatformWebClient;

public class SubmitOrderTask extends AsyncTask<String, Void, String> {
	private Context context = null;
	private UserOrder order = null;
	private String token;

	public SubmitOrderTask(UserOrder order, Context context, String token) {
		this.order = order;
		this.token = token;
		this.context = context;
	}

	@Override
	protected String doInBackground(String... params) {
		List<String> imageKeys = S3FileListUploadTask.uploadFileList(order
				.getListOfImagesPath());
		try {
			TradePlatformWebClient.makeOrder(order, token);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPostExecute(String result) {
		if (result != null) {
			Toast.makeText(context, R.string.regist_success, Toast.LENGTH_SHORT)
					.show();
		} else {
			Toast.makeText(context, R.string.regist_error, Toast.LENGTH_SHORT)
					.show();
		}
	}
}
