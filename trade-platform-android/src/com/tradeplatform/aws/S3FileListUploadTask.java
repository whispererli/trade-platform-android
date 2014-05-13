package com.tradeplatform.aws;

import java.io.File;
import java.util.List;

import android.os.AsyncTask;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.event.ProgressEvent;
import com.amazonaws.event.ProgressListener;
import com.amazonaws.services.s3.transfer.MultipleFileUpload;
import com.amazonaws.services.s3.transfer.TransferManager;

public class S3FileListUploadTask extends AsyncTask<Void, Void, Void> {
	private String bucketName = null;
	private String virtualDirectoryKeyPrefix = null;
	private File directory = null;
	private List<File> files = null;

	public S3FileListUploadTask(String bucketName,
			String virtualDirectoryKeyPrefix, File directory, List<File> files) {
		this.bucketName = bucketName;
		this.virtualDirectoryKeyPrefix = virtualDirectoryKeyPrefix;
		this.directory = directory;
		this.files = files;
	}

	@Override
	protected Void doInBackground(Void... params) {
		// TODO Auto-generated method stub
		DefaultAWSCredentialsProviderChain credentialProviderChain = new DefaultAWSCredentialsProviderChain();
		TransferManager tx = new TransferManager(new BasicAWSCredentials());
		// credentialProviderChain.getCredentials());
		MultipleFileUpload fileUpload = tx.uploadFileList(bucketName,
				virtualDirectoryKeyPrefix, directory, files);

		// You can poll your transfer's status to check its progress
		if (fileUpload.isDone() == false) {
			System.out.println("Transfer: " + fileUpload.getDescription());
			System.out.println("  - State: " + fileUpload.getState());
			System.out.println("  - Progress: "
					+ fileUpload.getProgress().getBytesTransferred());
		}

		// Transfers also allow you to set a <code>ProgressListener</code>
		// to
		// receive
		// asynchronous notifications about your transfer's progress.
		fileUpload.addProgressListener(new ProgressListener() {

			@Override
			public void progressChanged(ProgressEvent arg0) {
				// TODO Auto-generated method stub

			}

		});
		// Or you can block the current thread and wait for your transfer to
		// to complete. If the transfer fails, this method will throw an
		// AmazonClientException or AmazonServiceException detailing the
		// reason.
		try {
			fileUpload.waitForCompletion();
		} catch (AmazonServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AmazonClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// After the upload is complete, call shutdownNow to release the
		// resources.
		tx.shutdownNow();
		return null;
	}
}