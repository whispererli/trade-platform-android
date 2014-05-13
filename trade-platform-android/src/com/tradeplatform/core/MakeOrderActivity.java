package com.tradeplatform.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.trade_platform.core.R;

public class MakeOrderActivity extends Activity {

	public static final String USER_TOKEN = "userToken"; // The request code
	public static final String EXTRA_RES_ID = "pos"; // The request code
	private static final int SELECT_PICTURE = 1; // The request code
	private static final int SELECT_CAMERA = 2; // The request code
	private EditText expectDateSelector;
	private ImageButton addImage;
	private GridView mGridview;
	// private DateFormat dateFormat = new
	// SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final String tradePlatformImagePath = Environment
			.getExternalStorageDirectory().getAbsolutePath() + "/OrderImages/";
	private List<String> listOfImagesPath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		String userToken = intent.getStringExtra(USER_TOKEN);
		setContentView(R.layout.make_order);
		expectDateSelector = (EditText) findViewById(R.id.expectDate);
		addImage = (ImageButton) findViewById(R.id.add_image);

		mGridview = (GridView) findViewById(R.id.add_images_grid);
		listOfImagesPath = new ArrayList<String>();
		if (listOfImagesPath != null) {
			mGridview.setAdapter(new ImageAdapter(this, listOfImagesPath));
		}
		mGridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				Intent intent = new Intent(MakeOrderActivity.this,
						ImageViewActivity.class);
				intent.putExtra(EXTRA_RES_ID, listOfImagesPath.get((int) id));
				startActivity(intent);
			}
		});
		if (userToken != null) {
			init();
		} else {
			// TODO: make the user to login
			init();
		}
	}

	private void init() {
		// setup expect date selector.
		// expectDateSelector.setInputType(InputType.TYPE_NULL);
		expectDateSelector
				.setOnFocusChangeListener(new View.OnFocusChangeListener() {

					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						// TODO Auto-generated method stub
						if (hasFocus) {
							Calendar c = Calendar.getInstance();
							new DatePickerDialog(MakeOrderActivity.this,
									new DatePickerDialog.OnDateSetListener() {

										@Override
										public void onDateSet(DatePicker view,
												int year, int monthOfYear,
												int dayOfMonth) {
											// TODO Auto-generated method stub
											expectDateSelector.setText(year
													+ "/" + (monthOfYear + 1)
													+ "/" + dayOfMonth);
										}
									}, c.get(Calendar.YEAR), c
											.get(Calendar.MONTH), c
											.get(Calendar.DAY_OF_MONTH)).show();

						}
					}
				});

		expectDateSelector.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Calendar c = Calendar.getInstance();
				new DatePickerDialog(MakeOrderActivity.this,
						new DatePickerDialog.OnDateSetListener() {

							@Override
							public void onDateSet(DatePicker view, int year,
									int monthOfYear, int dayOfMonth) {
								// TODO Auto-generated method stub
								expectDateSelector.setText(year + "/"
										+ (monthOfYear + 1) + "/" + dayOfMonth);
							}
						}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c
								.get(Calendar.DAY_OF_MONTH)).show();

			}
		});
		addImage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// set up image selector.
				CharSequence[] items = { "相册", "相机" };
				new AlertDialog.Builder(MakeOrderActivity.this)
						.setTitle("选择图片来源")
						.setItems(items, new OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								if (which == 0) {
									Intent intent = new Intent(
											Intent.ACTION_GET_CONTENT);
									intent.addCategory(Intent.CATEGORY_OPENABLE);
									intent.setType("image/*");
									startActivityForResult(Intent
											.createChooser(intent, "选择图片"),
											SELECT_PICTURE);
								} else {
									Intent intent = new Intent(
											MediaStore.ACTION_IMAGE_CAPTURE);
									startActivityForResult(intent,
											SELECT_CAMERA);
								}
							}
						}).create().show();
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == SELECT_PICTURE) {
				// 选择图片
				Uri uri = data.getData();
				if (listOfImagesPath != null) {
					listOfImagesPath.add(getPath(uri));
					mGridview.setAdapter(new ImageAdapter(this,
							listOfImagesPath));
				}
			} else {
				Bundle extras = data.getExtras();
				Bitmap thePic = extras.getParcelable("data");
				// String imgcurTime = dateFormat.format(new Date());
				File imageDirectory = new File(tradePlatformImagePath);
				imageDirectory.mkdirs();
				String fileName = UUID.randomUUID().toString() + ".jpg";
				try {
					File imageFile = new File(tradePlatformImagePath, fileName);
					imageFile.createNewFile();
					FileOutputStream out = new FileOutputStream(imageFile,
							false);
					/* 采用压缩转档方法 */
					thePic.compress(Bitmap.CompressFormat.JPEG, 80, out);
					out.close();
					if (listOfImagesPath != null) {
						listOfImagesPath.add(tradePlatformImagePath + fileName);
						mGridview.setAdapter(new ImageAdapter(this,
								listOfImagesPath));
					}
				} catch (FileNotFoundException e) {
					e.getMessage();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		} else {
			Toast.makeText(this, "请重新选择图片", Toast.LENGTH_SHORT).show();
		}

	}

	public String getPath(Uri uri) {
		final String docId = DocumentsContract.getDocumentId(uri);
		final String[] split = docId.split(":");

		Uri contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

		final String selection = "_id=?";
		final String[] selectionArgs = new String[] { split[1] };

		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = { column };

		try {
			cursor = getContentResolver().query(contentUri, projection,
					selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst()) {
				final int index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}
}