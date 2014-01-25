package com.example.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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

	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="id1", referencedColumnName="id", insertable=false, updatable=false)
	private User user1;

	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="id2", referencedColumnName="id", insertable=false, updatable=false)
	private User user2;

	public void setPk(FriendRelationPK pk) {
		this.pk = pk;
	}

	public void setAllowed(boolean allowed) {
		this.allowed = allowed;
	}

	public void setUser1(User user) {
		this.user1 = user;
	}

	public void setUser2(User user) {
		this.user2 = user;
	}

	public FriendRelationPK getPk() {
		return pk;
	}

	public boolean isAllowed() {
		return allowed;
	}

	public User getUser1() {
		return user1;
	}

	public User getUser2() {
		return allowed ? user2 : null;
	}
}
