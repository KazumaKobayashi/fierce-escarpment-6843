package com.example.service;

import com.example.exception.GroupNotFoundException;
import com.example.jackson.Response;
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
	public Group create(String userId,String name);
	
	/**
	 *グループの情報を更新する
	 *
	 * @param id
	 * @param name
	 * @return
	 * @throws GroupNotFoundException 
	 */
	public Response update(Integer id,String name) throws GroupNotFoundException;
	
	/**
	 * グループの情報を取得する
	 *
	 * @param id
	 * @return
	 * @throws GroupNotFoundException 
	 */
	public Group getGroup(Integer id) throws GroupNotFoundException;
	
	/**
	 * グループにユーザーを追加する
	 * 
	 * @param username
	 * @param id
	 * @return
	 * @throws GroupNotFoundException
	 */
	public Group join(String userId, Integer id) throws GroupNotFoundException;
	/**
	 * 
	 * @param userId
	 * @param name
	 * @return
	 */
	public Response regist(String userid,String name);
}