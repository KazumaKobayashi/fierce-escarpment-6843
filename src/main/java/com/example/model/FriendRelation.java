package com.example.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

/**
 * 友達かどうかのクラス
 * プライマリキー以外には、許可されているかどうかのフラグが存在する
 *
 * @author Kazuki Hasegawa
 */
@Table(name="friend_relations")
@Entity
public class FriendRelation {
	@EmbeddedId
	private FriendRelationPK pk;

	@Column(name="allowed", nullable=false)
	private boolean allowed;

	@Column(name="created_at", nullable=false)
	private Timestamp createdAt;

	@Version
	@Column(name="updated_at", nullable=false)
	private Timestamp updatedAt;

	@ManyToOne
	@JoinColumn(name="id1", insertable=false, updatable=false)
	private User user1;

	@ManyToOne
	@JoinColumn(name="id2", insertable=false, updatable=false)
	private User user2;

	public void setPk(FriendRelationPK pk) {
		this.pk = pk;
	}

	public void setAllowed(boolean allowed) {
		this.allowed = allowed;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

	public FriendRelationPK getPk() {
		return pk;
	}

	public boolean isAllowed() {
		return allowed;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public Timestamp getUpdatedAt() {
		return updatedAt;
	}
}
