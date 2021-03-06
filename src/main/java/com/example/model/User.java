package com.example.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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

	public static final int USER_ID_MAX_LENGTH = 63;
	public static final int USER_NAME_MAX_LENGTH = 63;
	public static final int EMAIL_MAX_LENGTH = 127;

	@JsonProperty("id")
	@Id
	@Column(name="id", length=USER_ID_MAX_LENGTH)
	private String id;

	@JsonProperty("email")
	@Column(name="email", unique=true, nullable=false, length=EMAIL_MAX_LENGTH)
	private String email;

	@JsonProperty("name")
	@Column(name="name", nullable=false, length=USER_NAME_MAX_LENGTH)
	private String username;

	@JsonIgnore
	@Column(name="password", nullable=false)
	private String password;

	@JsonIgnore
	@Column(name="created_at", nullable=false)
	private Timestamp createdAt;

	@JsonIgnore
	@Version
	@Column(name="updated_at", nullable=false)
	private Timestamp updatedAt;

	@JsonUnwrapped
	@OneToOne(mappedBy="user")
	private Coordinate coord;

	@JsonIgnore
	@OneToOne(mappedBy="user")
	private LoginToken token;

	@JsonIgnore
	@OneToMany(mappedBy="user1")
	private List<FriendRelation> relatingList;

	@JsonIgnore
	@OneToMany(mappedBy="user2")
	private List<FriendRelation> relatedList;

	public void setId(String id) {
		this.id = id;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public void setRelatingList(List<FriendRelation> relatingList) {
		this.relatingList = relatingList;
	}

	public void setRelatedList(List<FriendRelation> relatedList) {
		this.relatedList = relatedList;
	}

	public String getId() {
		return id;
	}

	public String getEmail() {
		return email;
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

	public List<FriendRelation> getRelatingList() {
		return relatingList;
	}

	public List<FriendRelation> getRelatedList() {
		return relatedList;
	}
}