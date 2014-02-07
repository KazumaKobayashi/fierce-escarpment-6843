package com.example.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * ユーザとグループの参加テーブル
 *
 * @author Kazuki Hasegawa
 */
@Table(name="joins")
@Entity
public class Join {
	@EmbeddedId
	private JoinPK pk;

	@Column(name="flag")
	private boolean flag;
	
	@OneToOne
	@JoinColumn(name="user_id", insertable=false, updatable=false)
	private User user;

	@OneToOne
	@JoinColumn(name="group_id", insertable=false, updatable=false)
	private Group group;
	
	public void setPk(JoinPK pk) {
		this.pk = pk;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public void setFlag(boolean flag){
		this.flag = flag;
	}
	
	public JoinPK getPk() {
		return pk;
	}

	public User getUser() {
		return user;
	}

	public Group getGroup() {
		return group;
	}
}
