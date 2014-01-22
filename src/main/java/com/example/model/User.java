package com.example.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

/**
 * ユーザモデル
 *
 * @author Kazuki Hasegawa
 */
@Table(name="users")
@Entity
public class User implements Serializable {
	/**
	 * Default serializable Id
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@JsonIgnore
	@Column(name="user_id")
	private String id;

	@Column(name="username", nullable=false)
	private String username;

	@Column(name="password", nullable=false)
	@JsonIgnore
	private String password;

	@JsonIgnore
	@Column(name="created_at", nullable=false)
	private Timestamp createdAt;

	@JsonIgnore
	@Version
	@Column(name="updated_at", nullable=false)
	private Timestamp updatedAt;

	@JsonUnwrapped
	@OneToOne
	@JoinColumn(name="user_id", insertable=false, updatable=false)
	private Coordinate coord;

	@JsonUnwrapped
	@OneToOne
	@JoinColumn(name="user_id", insertable=false, updatable=false)
	private LoginToken token;

	public void setId(String id) {
		this.id = id;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

	public void setCoord(Coordinate coord) {
		this.coord = coord;
	}

	public void setToken(LoginToken token) {
		this.token = token;
	}

	public String getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public Timestamp getUpdatedAt() {
		return updatedAt;
	}

	public Coordinate getCoord() {
		return coord;
	}

	public LoginToken getToken() {
		return token;
	}
}