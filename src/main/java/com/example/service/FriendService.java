package com.example.service;

import com.example.exception.FriendRelationExistsException;
import com.example.exception.FriendRelationNotFoundException;
import com.example.exception.UserNotFoundException;
import com.example.model.FriendRelation;

/**
 * フレンド関係のサービスクラス
 * id1とid2の逆は許容しない
 *
 * @author Kazuki Hasegawa
 */
public interface FriendService {
	/**
	 * フレンド関係の作成
	 *
	 * @param id1
	 * @param id2
	 */
	public FriendRelation create(String id1, String id2) throws FriendRelationExistsException, UserNotFoundException;

	/**
	 * フレンド関係の更新
	 * 基本的には、フレンド申請の許可のみを行う
	 *
	 * @param id1
	 * @param id2
	 */
	public FriendRelation allow(String id1, String id2) throws FriendRelationNotFoundException;

	/**
	 * フレンド関係の却下
	 *
	 * @param id1
	 * @param id2
	 */
	public void forbid(String id1, String id2) throws FriendRelationNotFoundException;

	/**
	 * フレンド関係の取得
	 *
	 * @param id1
	 * @param id2
	 * @return
	 * @throws FriendRelationNotFoundException
	 */
	public FriendRelation getFriendRelation(String id1, String id2) throws FriendRelationNotFoundException;
}
