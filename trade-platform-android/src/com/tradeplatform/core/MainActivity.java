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

public class MainActivity extends Activity {
	static final int USER_LOGIN_REQUEST = 1; // The request code
	static final int MAKE_ORDER_REQUEST = 2; // The request code
	private static String userToken = null;
	private static boolean connected = false;

	RefreshableView refreshableView;
	ListView listView;
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
		listView = (ListView) findViewById(R.id.refreshable_list_view);

		adapter = new OrderItemAdapter(getApplicationContext());
		listView.setAdapter(adapter);
		loadOrderData();
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

	private void loadOrderData() {
		OrderItem se = new OrderItem();
		se.orderImagePath = "http://www.gravatar.com/avatar/c566fc0ef34130a6f1ed4df9cbb68b94?s=128&d=identicon&r=PG";
		se.orderCatagory = "电子产品";
		se.orderName = "Mac book pro";
		adapter.add(se);
		se = new OrderItem();
		se.orderImagePath = "http://www.gravatar.com/avatar/defff916430126c28dd317fc9ca15a9c?s=128&d=identicon&r=PG";
		se.orderCatagory = "食品";
		se.orderName = "好巴食";
		adapter.add(se);
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
			Intent intent = new Intent(this, LoginActivity.class);
			intent.putExtra(LoginActivity.USER_TOKEN, userToken);
			startActivityForResult(intent, USER_LOGIN_REQUEST, null);
			return true;
		case R.id.make_order:
			Intent makeOrder = new Intent(this, MakeOrder.class);
			makeOrder.putExtra(LoginActivity.USER_TOKEN, userToken);
			startActivityForResult(makeOrder, MAKE_ORDER_REQUEST, null);
			return true;
		case R.id.action_settings:
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == USER_LOGIN_REQUEST) {
			if (resultCode == RESULT_OK) {
				userToken = data.getStringExtra(LoginActivity.USER_TOKEN);
			}
		}
	}
}
