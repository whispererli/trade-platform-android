package com.tradeplatform.order;

import java.util.List;

public class UserOrder {
    private String orderId = null;
    private String orderExpectDate = null;
    private String orderDescription = null;
    private String catagoryId = null;
    private String orderExpectPlace = null;
    private String orderExpectPrice = null;
    private List<String> listOfImagesPath = null;

    public UserOrder(String orderId, String orderExpectDate, String orderDescription,
                     String catagoryId, String orderExpectPrice,
                     String orderExpectPlace, List<String> listOfImagesPath) {

        this.orderExpectDate = orderExpectDate;
        this.orderDescription = orderDescription;
        this.catagoryId = catagoryId;
        this.orderExpectPlace = orderExpectPlace;
        this.orderExpectPrice = orderExpectPrice;
        this.listOfImagesPath = listOfImagesPath;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
	public String getOrderExpectDate() {
		return orderExpectDate;
	}

	public void setOrderExpectDate(String orderExpectDate) {
		this.orderExpectDate = orderExpectDate;
	}

	public String getOrderDescription() {
		return orderDescription;
	}

	public void setOrderDescription(String orderDescription) {
		this.orderDescription = orderDescription;
	}

	public String getCatagoryId() {
		return catagoryId;
	}

	public void setCatagoryId(String catagoryId) {
		this.catagoryId = catagoryId;
	}

	public String getOrderExpectPlace() {
		return orderExpectPlace;
	}

	public void setOrderExpectPlace(String orderExpectPlace) {
		this.orderExpectPlace = orderExpectPlace;
	}

	public String getOrderExpectPrice() {
		return orderExpectPrice;
	}

	public void setOrderExpectPrice(String orderExpectPrice) {
		this.orderExpectPrice = orderExpectPrice;
	}

	public List<String> getListOfImagesPath() {
		return listOfImagesPath;
	}

	public void setListOfImagesPath(List<String> listOfImagesPath) {
		this.listOfImagesPath = listOfImagesPath;
	}
}
