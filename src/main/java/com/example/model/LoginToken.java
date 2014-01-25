package com.example.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Version;

/**
 * LoginID発行の状態を確認するテーブル
 *
 * @author Kazuki Hasegawa
 */
@Table(name="login_tokens")
@Entity
public class LoginToken implements Serializable {
	/**
	 * Default serializable Id
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="user_id", nullable=false)
	private String userId;

	@Column(name="login_token", nullable=false, unique=true)
	private String token;

	@Column(name="created_at", nullable=false)
	private Timestamp createdAt;

	@Version
	@Column(name="updated_at", nullable=false)
	private Timestamp updatedAt;

	@OneToOne(cascade=CascadeType.ALL)
	@PrimaryKeyJoinColumn
	private User user;

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getUserId() {
		return userId;
	}

	public String getToken() {
		return token;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public Timestamp getUpdatedAt() {
		return updatedAt;
	}

	public User getUser() {
		return user;
	}
}
