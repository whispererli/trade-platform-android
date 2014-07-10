package com.tradeplatform.order;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class OrderImageListAdapter extends BaseAdapter {
	private static final int PADDING = 8;
	private static final int WIDTH = 90;
	private static final int HEIGHT = 90;
	private Context mContext;
	private List<String> imagePicList;

	public OrderImageListAdapter(Context c, List<String> imagePicList) {
		mContext = c;
		this.imagePicList = imagePicList;
	}

	@Override
	public int getCount() {
		return imagePicList.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	// Will get called to provide the ID that
	// is passed to OnItemClickListener.onItemClick()
	@Override
	public long getItemId(int position) {
		return position;
	}

	// create a new ImageView for each item referenced by the Adapter
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageView;

		if (convertView == null) {
			imageView = new ImageView(mContext);
			imageView.setLayoutParams(new GridView.LayoutParams(WIDTH, HEIGHT));
			imageView.setPadding(PADDING, PADDING, PADDING, PADDING);
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			FileInputStream fs = null;
			Bitmap bm;
			try {
				fs = new FileInputStream(new File(imagePicList.get(position)
						.toString()));

				if (fs != null) {
					BitmapFactory.Options bfOptions = new BitmapFactory.Options();
					bfOptions.inDither = false; // Disable Dithering mode
					bfOptions.inPurgeable = true; // Tell to gc that whether it
													// needs free
													// memory, the Bitmap can be
													// cleared
					bfOptions.inInputShareable = true; // Which kind of
														// reference will be
														// used to recover the
														// Bitmap data
														// after being clear,
														// when it will
														// be used in the future
					bfOptions.inTempStorage = new byte[32 * 1024];
					bm = BitmapFactory.decodeFileDescriptor(fs.getFD(), null,
							bfOptions);
					imageView.setImageBitmap(bm);
					imageView.setId(position);
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
		} else {
			imageView = (ImageView) convertView;
		}
		return imageView;
	}

}
