package com.example.service;

import com.example.model.Group;

/**
 * グループ関係のサービス
 * 
 * @author Kazuma Kobayashi
 *
 */
public interface GroupService{
	/**
	 * グループの作成
	 * 
	 * @param name
	 * @return
	 */
	public Group create(String name);
	
	/**
	 *グループの情報を更新する
	 * 
	 * @param id
	 * @return
	 */
	public Group update(Integer id,String name);
}