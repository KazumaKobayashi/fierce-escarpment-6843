package com.example.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 地図座標用のテーブル
 *
 * @author Kazuki Hasegawa
 */
@Table(name="map_coordinates")
@Entity
public class Coordinate implements Serializable {
	/**
	 * Default serializable Id
	 */
	private static final long serialVersionUID = 1L;

	@JsonIgnore
	@Id
	@Column(name="usre_id")
	private String userId;

	@Column(name="lat")
	private Double lat;

	@Column(name="lng")
	private Double lng;

	@JsonIgnore
	@OneToOne
	@JoinColumn(name="user_id", insertable=false, updatable=false)
	private User user;

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getUserId() {
		return userId;
	}

	public Double getLat() {
		return lat;
	}

	public Double getLng() {
		return lng;
	}

	public User getUser() {
		return user;
	}
}
