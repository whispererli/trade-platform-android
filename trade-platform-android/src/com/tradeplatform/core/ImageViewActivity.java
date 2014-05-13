package com.tradeplatform.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

public class ImageViewActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent i = getIntent();
		String filePath = i.getStringExtra(MakeOrderActivity.EXTRA_RES_ID);

		ImageView imageView = new ImageView(getApplicationContext());
		// imageView
		FileInputStream fs = null;
		Bitmap bm = null;
		try {
			fs = new FileInputStream(new File(filePath));

			if (fs != null) {
				bm = BitmapFactory.decodeFileDescriptor(fs.getFD());
				imageView.setImageBitmap(bm);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fs != null) {
				try {
					fs.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		if (bm != null) {
			imageView.setImageBitmap(bm);
		}

		setContentView(imageView);
	}
}