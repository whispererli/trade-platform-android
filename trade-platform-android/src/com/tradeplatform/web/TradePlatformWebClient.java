package com.tradeplatform.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tradeplatform.order.UserOrder;
import com.tradeplatform.user.Address;
import com.tradeplatform.user.UserProfile;

public class TradePlatformWebClient {
	private static String baseUrl = "http://10.0.2.2:8000/";

	// private static String baseUrl =
	// "http://ec2-54-89-71-135.compute-1.amazonaws.com:8000/";

	public static String userLogin(String username, String password)
			throws ClientProtocolException, IOException, JSONException {

		JSONObject jsonUser = new JSONObject();
		jsonUser.put("username", username);
		jsonUser.put("password", password);
		HttpPost request = new HttpPost(baseUrl + "api-token-auth/");
		request.addHeader("content-type", "application/json");
		request.setEntity(new StringEntity(jsonUser.toString()));
		DefaultHttpClient httpClient = new DefaultHttpClient();
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

		JSONObject jsonUser = new JSONObject();
		jsonUser.put("username", username);
		jsonUser.put("email", username);
		jsonUser.put("password", password);
		HttpPost request = new HttpPost(baseUrl + "user/");
		request.addHeader("content-type", "application/json");
		request.setEntity(new StringEntity(jsonUser.toString()));
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpResponse response = httpClient.execute(request);
		if (response.getStatusLine().getStatusCode() == 201) {
			return userLogin(username, password);
		} else {
			// extract fail reason
		}
		return null;
	}

	public static String makeOrder(UserOrder order, String token)
			throws JSONException, ClientProtocolException, IOException {

		JSONObject jsonOrderWrapper = new JSONObject();
		// Order details
		JSONObject jsonOrderDetails = new JSONObject();
		jsonOrderDetails.put("expect_date", order.getOrderExpectDate());
		jsonOrderDetails.put("description", order.getOrderDescription());
		jsonOrderDetails.put("expect_price", order.getOrderExpectPrice());
		jsonOrderDetails.put("order_status", "P");
		jsonOrderDetails.put("order_address", order.getOrderExpectPlace());
		jsonOrderDetails.put("product_catalog", order.getCatagoryId());
		jsonOrderWrapper.put("order", jsonOrderDetails);
		// TODO: Extra Order details
		JSONArray jsonOrderExtraDetailsList = new JSONArray();
		jsonOrderWrapper.put("extraInfo", jsonOrderExtraDetailsList);

		HttpPost request = new HttpPost(baseUrl + "makeOrder/");
		StringEntity params = new StringEntity(jsonOrderWrapper.toString());
		request.addHeader("content-type", "application/json");
		request.addHeader("Authorization", "Token " + token);
		request.setEntity(params);
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpResponse response = httpClient.execute(request);
		String orderId = null;
		System.out.println(response.getStatusLine().getStatusCode());
		if (response.getStatusLine().getStatusCode() == 200) {
			orderId = EntityUtils.toString(response.getEntity(), "UTF-8");
			for (String imagePath : order.getListOfImagesPath()) {
				HttpPost addImageRequest = new HttpPost(baseUrl + "orderImage/");
				JSONObject imageJsonRequestObject = new JSONObject();
				imageJsonRequestObject.put("order_id", orderId);
				imageJsonRequestObject.put("path", imagePath);
				StringEntity image = new StringEntity(
						imageJsonRequestObject.toString());
				addImageRequest.addHeader("content-type", "application/json");
				addImageRequest.setEntity(image);
				HttpResponse addImageResponse = httpClient
						.execute(addImageRequest);
				if (addImageResponse.getStatusLine().getStatusCode() != 201) {
					// extract fail reason
				}
			}
		} else {
			// extract fail reason
		}
		return orderId;
	}

	public static List<UserOrder> queryAllOrders()
			throws ClientProtocolException, IOException, JSONException {
		HttpGet request = new HttpGet(baseUrl + "userOrder/");
		DefaultHttpClient httpClient = new DefaultHttpClient();
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
			List<UserOrder> orders = new ArrayList<UserOrder>();
			JSONArray ordersJson = new JSONArray(sb.toString());
			for (int i = 0; i < ordersJson.length(); i++) {
				JSONObject orderJson = ordersJson.getJSONObject(i);
				JSONArray imagesJson = orderJson.getJSONArray("order_images");
				List<String> listOfImagesPath = new ArrayList<String>();
				for (int j = 0; j < imagesJson.length(); j++) {
					listOfImagesPath.add(imagesJson.getJSONObject(j).getString(
							"path"));
				}
				orders.add(new UserOrder(orderJson.getString("id"),
                        orderJson.getString("expect_date"),
						orderJson.getString("description"), orderJson
								.getString("product_catalog"), orderJson
								.getString("expect_price"), new Address(
								orderJson.getJSONObject("order_address"))
								.toString(), listOfImagesPath));

			}
			return orders;
		}
		return null;
	}

    public static UserOrder queryOrder(String order)
            throws ClientProtocolException, IOException, JSONException {
        HttpGet request = new HttpGet(baseUrl + "userOrder/" + order + "/");
        DefaultHttpClient httpClient = new DefaultHttpClient();
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
            JSONObject orderJson = new JSONObject(sb.toString());
            JSONArray imagesJson = orderJson.getJSONArray("order_images");
            List<String> listOfImagesPath = new ArrayList<String>();
            for (int j = 0; j < imagesJson.length(); j++) {
                listOfImagesPath.add(imagesJson.getJSONObject(j).getString("path"));
            }
            return new UserOrder(orderJson.getString("id"),
                    orderJson.getString("expect_date"),
                    orderJson.getString("description"), orderJson
                    .getString("product_catalog"), orderJson
                    .getString("expect_price"), new Address(
                    orderJson.getJSONObject("order_address"))
                    .toString(), listOfImagesPath);
        }
        return null;
    }

	public static boolean InsertOrUpdateUserProfile(String token,
			UserProfile profile) throws ClientProtocolException, IOException,
			JSONException {
		JSONObject jsonProfileDetails = new JSONObject();
		jsonProfileDetails.put("name", profile.getName());
		jsonProfileDetails.put("birthday", profile.getBirthday());
		jsonProfileDetails.put("description", profile.getDescription());
		jsonProfileDetails.put("gender", profile.getGender());
		jsonProfileDetails.put("phone", profile.getPhone());
		jsonProfileDetails.put("profile_image", profile.getImagePath());

		HttpPost request = new HttpPost(baseUrl + "updateInsertProfile/");
		StringEntity params = new StringEntity(jsonProfileDetails.toString());
		request.addHeader("content-type", "application/json");
		request.addHeader("Authorization", "Token " + token);
		request.setEntity(params);

		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpResponse response = httpClient.execute(request);
		if (response.getStatusLine().getStatusCode() == 200
				|| response.getStatusLine().getStatusCode() == 201) {
			return true;
		}
		return false;
	}

	public static UserProfile getUserProfile(String token)
			throws ClientProtocolException, IOException, JSONException {
		HttpGet request = new HttpGet(baseUrl + "selfProfile/");
		request.addHeader("content-type", "application/json");
		request.addHeader("Authorization", "Token " + token);
		DefaultHttpClient httpClient = new DefaultHttpClient();
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
			reader.close();
			JSONArray userProfileListJson = new JSONArray(sb.toString());
			assert (userProfileListJson.length() == 1);
			JSONObject userProfileJson = userProfileListJson.getJSONObject(0);
			// get addresses
			HttpGet addrRequest = new HttpGet(baseUrl + "selfAddress/");
			addrRequest.addHeader("content-type", "application/json");
			addrRequest.addHeader("Authorization", "Token " + token);
			DefaultHttpClient addressHttpClient = new DefaultHttpClient();
			HttpResponse addrResponse = addressHttpClient.execute(addrRequest);
			if (addrResponse.getStatusLine().getStatusCode() == 200) {
				inputStream = addrResponse.getEntity().getContent();
				// json is UTF-8 by default
				reader = new BufferedReader(new InputStreamReader(inputStream,
						"UTF-8"));
				sb = new StringBuilder();

				line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
				JSONArray addrArray = new JSONArray(sb.toString());
				List<Address> userAddrs = new ArrayList<Address>();
				for (int i = 0; i < addrArray.length(); i++) {
					userAddrs.add(new Address(addrArray.getJSONObject(i)));
				}
				return new UserProfile(userProfileJson.getString("name"),
						userProfileJson.getString("birthday"),
						userProfileJson.getString("description"),
						userProfileJson.getString("gender"),
						userProfileJson.getString("phone"),
						userProfileJson.getString("profile_image"), userAddrs);
			}
		}
		return null;
	}

	public static Address getAddress(String addrId)
			throws ClientProtocolException, IOException, JSONException {
		// get addresses
		InputStream inputStream;
		HttpGet addrRequest = new HttpGet(baseUrl + "address/" + addrId);
		DefaultHttpClient addressHttpClient = new DefaultHttpClient();
		HttpResponse addrResponse = addressHttpClient.execute(addrRequest);
		if (addrResponse.getStatusLine().getStatusCode() == 200) {
			inputStream = addrResponse.getEntity().getContent();
			// json is UTF-8 by default
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream, "UTF-8"));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			return new Address(new JSONObject(sb.toString()));
		}
		return null;
	}

	public static boolean addNewAddress(Address addr, String token)
			throws JSONException, ClientProtocolException, IOException {
		// Order details
		JSONObject jsonAddressDetails = new JSONObject();
		jsonAddressDetails.put("address1", addr.getAddress1());
		jsonAddressDetails.put("address2", addr.getAddress2());
		jsonAddressDetails.put("city", addr.getCity());
		jsonAddressDetails.put("nation", addr.getCountry());
		jsonAddressDetails.put("post", addr.getPost());

		HttpPost request = new HttpPost(baseUrl + "updateInsertAddress/");
		StringEntity params = new StringEntity(jsonAddressDetails.toString());
		request.addHeader("content-type", "application/json");
		request.addHeader("Authorization", "Token " + token);
		request.setEntity(params);
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpResponse response = httpClient.execute(request);
		System.out.println(response.getStatusLine().getStatusCode());
		if (response.getStatusLine().getStatusCode() == 200) {
			return true;
		}
		return false;
	}

	public static Boolean updateAddress(Address addr, String token)
			throws JSONException, ClientProtocolException, IOException {
		// Order details
		JSONObject jsonAddressDetails = new JSONObject();
		jsonAddressDetails.put("address1", addr.getAddress1());
		jsonAddressDetails.put("address2", addr.getAddress2());
		jsonAddressDetails.put("city", addr.getCity());
		jsonAddressDetails.put("nation", addr.getCountry());
		jsonAddressDetails.put("post", addr.getPost());
		jsonAddressDetails.put("id", addr.getAddressId());

		HttpPost request = new HttpPost(baseUrl + "updateInsertAddress/");
		StringEntity params = new StringEntity(jsonAddressDetails.toString());
		request.addHeader("content-type", "application/json");
		request.addHeader("Authorization", "Token " + token);
		request.setEntity(params);
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpResponse response = httpClient.execute(request);
		System.out.println(response.getStatusLine().getStatusCode());
		if (response.getStatusLine().getStatusCode() == 200) {
			return true;
		}
		return false;
	}
}
