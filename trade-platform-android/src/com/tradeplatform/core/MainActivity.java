package com.tradeplatform.core;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.trade_platform.core.R;
import com.tradeplatform.core.RefreshableView.PullToRefreshListener;
import com.tradeplatform.order.LoadOrdersTask;
import com.tradeplatform.order.MakeOrderActivity;
import com.tradeplatform.order.OrderItemAdapter;
import com.tradeplatform.user.UserActivity;

public class MainActivity extends Activity {
	static final int MAKE_ORDER_REQUEST = 2; // The request code
	private static boolean connected = false;

	RefreshableView refreshableView;
	OrderItemAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new Fragment()).commit();
		}

		setContentView(R.layout.activity_main);
		refreshableView = (RefreshableView) findViewById(R.id.refreshable_view);
		ListView ordersView = (ListView) findViewById(R.id.refreshable_list_view);

		adapter = new OrderItemAdapter(getApplicationContext());
		ordersView.setAdapter(adapter);
		new LoadOrdersTask(adapter).execute();
		refreshableView.setOnRefreshListener(new PullToRefreshListener() {
			@Override
			public void onRefresh() {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				refreshableView.finishRefreshing();
			}
		}, 0);
		// Network connectivity
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			connected = true;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
		case R.id.user_login:
			Intent intent = new Intent(this, UserActivity.class);
			startActivity(intent, null);
			return true;
		case R.id.make_order:
			Intent makeOrder = new Intent(this, MakeOrderActivity.class);
			startActivityForResult(makeOrder, MAKE_ORDER_REQUEST, null);
			return true;
		case R.id.action_settings:
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
