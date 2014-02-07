package com.example.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang3.StringUtils;

/**
 * FriendRelationテーブルのPrimary Keyクラス
 *
 * @author Kazuki Hasegawa
 */
@Embeddable
public class FriendRelationPK implements Serializable {
	/**
	 * Default serializable id
	 */
	private static final long serialVersionUID = 1L;

	@Column(name="id1", length=User.USER_ID_MAX_LENGTH)
	private String id1;

	@Column(name="id2", length=User.USER_ID_MAX_LENGTH)
	private String id2;

	public void setId1(String id1) {
		this.id1 = id1;
	}

	public void setId2(String id2) {
		this.id2 = id2;
	}

	public String getId1() {
		return id1;
	}

	public String getId2() {
		return id2;
	}

	public boolean equals(Object object) {
		if (object == this) {
			return true;
		}
		if (!(object instanceof FriendRelationPK)) {
			return false;
		}
		FriendRelationPK pk = (FriendRelationPK) object;
		return StringUtils.equals(id1, pk.id1)
				&& StringUtils.equals(id2, pk.id2);
	}

	public int hashCode() {
		final int id = 37;
		int hashCode = 31;
		hashCode = hashCode * id + id1.hashCode();
		hashCode = hashCode * id + id2.hashCode();
		return hashCode;
	}
}
