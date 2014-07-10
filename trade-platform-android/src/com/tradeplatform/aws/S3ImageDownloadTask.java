package com.tradeplatform.aws;

import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

public class S3ImageDownloadTask extends AsyncTask<Void, Void, Bitmap> {
	private String bucketName = null;
	private String s3Path = null;
	private ImageView view = null;

	public S3ImageDownloadTask(String bucketName,
			String s3Path, ImageView view) {
		this.bucketName = bucketName;
		this.s3Path = s3Path;
		this.view = view;
	}

	@Override
	protected Bitmap doInBackground(Void... params) {
		// TODO Auto-generated method stub
		AmazonS3Client client = new AmazonS3Client(new BasicAWSCredentials(
				AWSCredential.accessKey, AWSCredential.securitKey));
		S3Object object = null;
		try {
			object = client.getObject(new GetObjectRequest(bucketName,
                    s3Path));
		} catch (AmazonServiceException ase) {
			System.out.println("Caught an AmazonServiceException, which"
					+ " means your request made it "
					+ "to Amazon S3, but was rejected with an error response"
					+ " for some reason.");
			System.out.println("Error Message:    " + ase.getMessage());
			System.out.println("HTTP Status Code: " + ase.getStatusCode());
			System.out.println("AWS Error Code:   " + ase.getErrorCode());
			System.out.println("Error Type:       " + ase.getErrorType());
			System.out.println("Request ID:       " + ase.getRequestId());
			return null;
		} catch (AmazonClientException ace) {
			System.out.println("Caught an AmazonClientException, which means"
					+ " the client encountered "
					+ "an internal error while trying to "
					+ "communicate with S3, "
					+ "such as not being able to access the network.");
			System.out.println("Error Message: " + ace.getMessage());
			return null;
		}
		System.setProperty(
				"com.amazonaws.services.s3.disableGetObjectMD5Validation",
				"true");
		S3ObjectInputStream input = object.getObjectContent();
		Bitmap bm = BitmapFactory.decodeStream(input);
		try {
			input.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bm;
	}

	// onPostExecute displays the results of the AsyncTask.
	@Override
	protected void onPostExecute(Bitmap result) {
		if (result != null) {
			view.setImageBitmap(result);
		}
	}
}