package com.tradeplatform.user;

import java.util.List;

public class UserProfile {
	private String name;
	private String birthday;
	private String description;
	private String gender;
	private String phone;
	private String imagePath;
	List<Address> addresses;

	public UserProfile(String name, String birthday, String description,
			String gender, String phone, String imagePath,
			List<Address> addresses) {
		this.name = name;
		this.birthday = birthday;
		this.description = description;
		this.gender = gender;
		this.phone = phone;
		this.imagePath = imagePath;
		this.addresses = addresses;
	}

	public List<Address> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<Address> addresses) {
		this.addresses = addresses;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
}
