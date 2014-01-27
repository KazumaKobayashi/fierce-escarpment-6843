package com.example.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

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

	public void setPk(FriendRelationPK pk) {
		this.pk = pk;
	}

	public void setAllowed(boolean allowed) {
		this.allowed = allowed;
	}

	public FriendRelationPK getPk() {
		return pk;
	}

	public boolean isAllowed() {
		return allowed;
	}
}
