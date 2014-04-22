package com.tradeplatform.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

public class TradePlatformWebClient {
	private static String baseUrl = "http://10.0.2.2:8000/";

	public static String userLogin(String username, String password)
			throws ClientProtocolException, IOException, JSONException {

		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpPost request = new HttpPost(baseUrl + "api-token-auth/");
		JSONObject jsonRequestObject = new JSONObject();
		jsonRequestObject.put("username", username);
		jsonRequestObject.put("password", password);
		StringEntity params = new StringEntity(jsonRequestObject.toString());
		request.addHeader("content-type", "application/json");
		request.setEntity(params);
		HttpResponse response = httpClient.execute(request);
		if (response.getStatusLine().getStatusCode() == 200) {
			InputStream inputStream = response.getEntity().getContent();
			// json is UTF-8 by default
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream, "UTF-8"));
			StringBuilder sb = new StringBuilder();

			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			JSONObject jObject = new JSONObject(sb.toString());
			return jObject.get("token").toString();
		}
		return null;
	}

	public static String userRegist(String username, String password)
			throws JSONException, ClientProtocolException, IOException {
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpPost request = new HttpPost(baseUrl + "user/");
		JSONObject jsonRequestObject = new JSONObject();
		jsonRequestObject.put("username", username);
		jsonRequestObject.put("email", username);
		jsonRequestObject.put("password", password);
		StringEntity params = new StringEntity(jsonRequestObject.toString());
		request.addHeader("content-type", "application/json");
		request.setEntity(params);
		HttpResponse response = httpClient.execute(request);
		if (response.getStatusLine().getStatusCode() == 201) {
			return userLogin(username, password);
		} else {
			// extract fail reason
		}
		return null;
	}
}
