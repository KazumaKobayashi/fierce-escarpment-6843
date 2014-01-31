package com.example.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang3.StringUtils;

/**
 * JoinのPrimary Keyクラス
 *
 * @author Kazuki Hasegawa
 */
@Embeddable
public class JoinPK implements Serializable {
	/**
	 * Default serializable id
	 */
	private static final long serialVersionUID = 1L;

	@Column(name="user_id")
	private String userId;

	@Column(name="group_id")
	private Integer groupId;

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public String getUserId() {
		return userId;
	}

	public Integer getGroupId() {
		return groupId;
	}
	/**
	 * 比較関数
	 * 作成しないと出来ないので
	 *
	 * @param object
	 * @return
	 */
	public boolean equals(Object object) {
		if (object == this) {
			return true;
		}
		if (!(object instanceof JoinPK)) {
			return false;
		}
		JoinPK pk = (JoinPK) object;
		return StringUtils.equals(userId, pk.userId)
				&& (groupId == pk.groupId);
	}

	/**
	 * ハッシュコード生成
	 */
	public int hashCode() {
		final int id = 53;
		int hashCode = 19;
		hashCode = hashCode * id + userId.hashCode();
		hashCode = hashCode * id + groupId.hashCode();
		return hashCode;
	}
}
