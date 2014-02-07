package com.example.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * グループモデル
 * 
 * @author Kazuki Hasegawa
 * @author Kazuma Kobayashi
 */
@Table(name="groups")
@Entity
public class Group implements Serializable{
	/**
	 * Defalut serializable Id
	 */
	private static final long serialVersionUID = 1L;

	public static final int GROUP_NAME_MAX_LENGTH = 63;

	@JsonProperty("id")
	@Id
    @GeneratedValue
    @Column(name="id")
    private Integer id;

	@JsonProperty("name")
	@Column(name="name", nullable=false, length=GROUP_NAME_MAX_LENGTH)
	private String groupname;
	
	@JsonProperty("owner")
	@Column(name = "owner", length=User.USER_ID_MAX_LENGTH)
	private String owner;
	
	public void setId(Integer id) {
		this.id = id;
	}

	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}
	
	public void setOwner(String owner){
		this.owner = owner;
	}

	public Integer getId(){
		return id;
	}
	
	public String getGroupname(){
		return groupname;
	}
	
	public String getOwner(){
		return owner;
	}
}
