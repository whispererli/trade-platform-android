package com.trade_platform.core;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.trade_platform.core.RefreshableView.PullToRefreshListener;

public class MainActivity extends Activity {
	private static final String ANIMALS_TABSTRING = "Animals";
	private static final String FLOWERS_TABSTRING = "Flowers";
	private static final String DROP_DOWN = "Dropdown";
	protected static final String THUMBNAIL_IDS = "thumbNailIDs";

	private ArrayList<Integer> mThumbIdsFlowers = new ArrayList<Integer>(
			Arrays.asList(R.drawable.image1, R.drawable.image2,
					R.drawable.image3, R.drawable.image4, R.drawable.image5,
					R.drawable.image6, R.drawable.image7, R.drawable.image8,
					R.drawable.image9, R.drawable.image10, R.drawable.image11,
					R.drawable.image12));

	private ArrayList<Integer> mThumbIdsAnimals = new ArrayList<Integer>(
			Arrays.asList(R.drawable.sample_1, R.drawable.sample_2,
					R.drawable.sample_3, R.drawable.sample_4,
					R.drawable.sample_5, R.drawable.sample_6,
					R.drawable.sample_7, R.drawable.sample_0));
	
	 RefreshableView refreshableView;  
	    ListView listView;  
	    ArrayAdapter<String> adapter;  
	    String[] items = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L" };  

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new Fragment()).commit();
		}

		final ActionBar tabBar = getActionBar();
		tabBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		tabBar.addTab(tabBar.newTab().setText(DROP_DOWN)
				.setTabListener(new TabListener(new Fragment())));
		
		GridFragment flowerFrag = new GridFragment();
		Bundle args = new Bundle();
		args.putIntegerArrayList(THUMBNAIL_IDS, mThumbIdsFlowers);
		flowerFrag.setArguments(args);
		tabBar.addTab(tabBar.newTab().setText(FLOWERS_TABSTRING)
				.setTabListener(new TabListener(flowerFrag)));

		GridFragment animalFrag = new GridFragment();
		args = new Bundle();
		args.putIntegerArrayList(THUMBNAIL_IDS, mThumbIdsAnimals);
		animalFrag.setArguments(args);
		tabBar.addTab(tabBar.newTab().setText(ANIMALS_TABSTRING)
				.setTabListener(new TabListener(animalFrag)));
		
		
		

		setContentView(R.layout.activity_main);  
        refreshableView = (RefreshableView) findViewById(R.id.refreshable_view);  
        listView = (ListView) findViewById(R.id.refreshable_list_view);  
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);  
        listView.setAdapter(adapter);  
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
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public static class TabListener implements ActionBar.TabListener {
		private final Fragment mFragment;

		public TabListener(Fragment fragment) {
			mFragment = fragment;
		}

		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
		}

		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			if (null != mFragment) {
				ft.replace(R.id.container, mFragment);
			}
		}

		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			if (null != mFragment) {
				ft.remove(mFragment);
			}
		}
	}

}
