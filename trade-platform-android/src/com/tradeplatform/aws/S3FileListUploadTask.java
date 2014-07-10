package com.tradeplatform.aws;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;
import com.amazonaws.services.s3.transfer.model.UploadResult;

public class S3FileListUploadTask {
	public static final String S3_TEST_BUCKET = "trade-platform-order-images";
	public static final String S3_TEST_PATH = "/";

	public static List<String> uploadFileList(List<String> files) {
		List<String> imageKeys = new ArrayList<String>();
		// TODO credentialProviderChain.getCredentials());
		TransferManager tx = new TransferManager(new BasicAWSCredentials(
				AWSCredential.accessKey, AWSCredential.securitKey));
		for (String file : files) {
			try {
				String virtualDirectoryKeyPrefix = S3_TEST_PATH
						+ UUID.randomUUID().toString();
				Upload fileUpload = tx.upload(S3_TEST_BUCKET,
						virtualDirectoryKeyPrefix, new File(file));

				UploadResult result = fileUpload.waitForUploadResult();
				imageKeys.add(result.getKey());
			} catch (AmazonServiceException ase) {
				System.out
						.println("Caught an AmazonServiceException, which"
								+ " means your request made it "
								+ "to Amazon S3, but was rejected with an error response"
								+ " for some reason.");
				System.out.println("Error Message:    " + ase.getMessage());
				System.out.println("HTTP Status Code: " + ase.getStatusCode());
				System.out.println("AWS Error Code:   " + ase.getErrorCode());
				System.out.println("Error Type:       " + ase.getErrorType());
				System.out.println("Request ID:       " + ase.getRequestId());
				return imageKeys;
			} catch (AmazonClientException ace) {
				System.out
						.println("Caught an AmazonClientException, which means"
								+ " the client encountered "
								+ "an internal error while trying to "
								+ "communicate with S3, "
								+ "such as not being able to access the network.");
				System.out.println("Error Message: " + ace.getMessage());
				return imageKeys;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return imageKeys;
			}

		}
		// After the upload is complete, call shutdownNow to release the
		// resources.
		tx.shutdownNow();
		return imageKeys;
	}
}