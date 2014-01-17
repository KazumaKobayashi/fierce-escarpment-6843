/**
 * 
 */
package com.example.jackson;

/**
 * @author Kazuki Hasegawa
 */
public class User {
	private String name;
	private Double lat;
	private Double lng;

	public void setName(String name) {
		this.name = name;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}

	public String getName() {
		return name;
	}

	public Double getLat() {
		return lat;
	}

	public Double getLng() {
		return lng;
	}
}
