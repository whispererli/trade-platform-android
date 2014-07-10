package com.tradeplatform.user;

import java.util.ArrayList;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

public class Address {
	private String id;
	private String address1;
	private String address2;
	private String city;
	private String country;
	private String post;
	private static ArrayList<String> countries = null;

	public Address(String address1, String address2, String city,
			String country, String post) {
		this.address1 = address1;
		this.address2 = address2;
		this.city = city;
		this.country = country;
		this.post = post;
	}

	public Address(JSONObject addr) throws JSONException {
		this.id = addr.getString("id");
		this.address1 = addr.getString("address1");
		this.address2 = addr.getString("address2");
		this.city = addr.getString("city");
		this.country = addr.getString("nation");
		this.post = addr.getString("post");
	}

	public String getAddressId() {
		return id;
	}

	public void setAddressId(String id) {
		this.id = id;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String nation) {
		this.country = nation;
	}

	public String getPost() {
		return post;
	}

	public void setPost(String post) {
		this.post = post;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(address1).append(" ");
		sb.append(address2).append("\n");
		sb.append(city).append(", ");
		sb.append(post).append("\n");
		sb.append(country).append(" ");
		return sb.toString();
	}

	public static ArrayList<String> getAvaliableCountries() {
		if (countries == null) {
			Locale[] locales = Locale.getAvailableLocales();
			countries = new ArrayList<String>();
			for (Locale locale : locales) {
				String country = locale.getDisplayCountry();
				if (country.trim().length() > 0 && !countries.contains(country)) {
					countries.add(country);
				}
			}
		}
		return countries;
	}

	public static int getCountryIndex(String countryIn) {
		if (countries == null) {
			Locale[] locales = Locale.getAvailableLocales();
			countries = new ArrayList<String>();
			for (Locale locale : locales) {
				String country = locale.getDisplayCountry();
				if (country.trim().length() > 0 && !countries.contains(country)) {
					countries.add(country);
				}
			}
		}
		for (int i = 0; i < countries.size(); i++) {
			if (countries.get(i).equals(countryIn)) {
				return i;
			}
		}
		return 0;
	}
}
