package com.eufh.drohne.business.service.impl;

import com.google.maps.model.LatLng;

public class OrderLocation {
	private int orderID;
	private String address;
	private LatLng latlng;
	public OrderLocation(int orderID, String address, LatLng latlng) {
		super();
		this.orderID = orderID;
		this.address = address;
		this.latlng = latlng;
	}
	public OrderLocation(int orderID, String address) {
		super();
		this.orderID = orderID;
		this.address = address;
	}
	public LatLng getLatlng() {
		return latlng;
	}
	public void setLatlng(LatLng latlng) {
		this.latlng = latlng;
	}
	public int getOrderID() {
		return orderID;
	}
	public String getAddress() {
		return address;
	}
}
