package com.tradeplatform.order;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.trade_platform.core.R;
import com.tradeplatform.user.AddressDetailsActivity;
import com.tradeplatform.user.InsertUpdateUserProfileTask;
import com.tradeplatform.user.LoginTask;
import com.tradeplatform.user.RegistTask;
import com.tradeplatform.user.RetriveProfileTask;
import com.tradeplatform.user.UserTokenUtil;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class OrderDetailActivity extends Activity {
    public static final String ORDER_ID = "order_id";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        final String orderId = intent.getStringExtra(ORDER_ID);

		setContentView(R.layout.order_details);

		ListView orderImages = (ListView) findViewById(R.id.order_details_images);
		ArrayAdapter<Bitmap> adapter = new ArrayAdapter<Bitmap>(this,android.R.layout.simple_list_item_1);
		orderImages.setAdapter(adapter);


		new QuerySingleOrderTask(adapter,this, orderId).execute();
	}
}
