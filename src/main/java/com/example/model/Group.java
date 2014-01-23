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
 */
@Table(name="group")
@Entity
public class Group implements Serializable{
	/**
	 * Defalut serializable Id
	 */
	private static final long serialVersionUID = 1L;

	@JsonProperty("id")
	@Id
    @GeneratedValue
    private Integer id;
	
	@Column(name="groupname", nullable=false)
	private String groupname;
	
	public void setId(Integer id) {
		this.id = id;
	}

	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}

	public Integer getId(){
		return id;
	}
	
	public String getGroupname(){
		return groupname;
	}
	
}
