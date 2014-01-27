package com.example.service;

import java.util.List;

import com.example.exception.FriendRelationExistsException;
import com.example.exception.FriendRelationNotFoundException;
import com.example.exception.UserNotFoundException;
import com.example.model.FriendRelation;
import com.example.model.User;

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
	public FriendRelation getFriendRelation(String id1, String id2);

	/**
	 * 申請を出しているリストを返す
	 *
	 * @param id
	 * @return
	 */
	public List<User> getRelatingList(String id);

	/**
	 * 申請が出されているリストを返す
	 *
	 * @param id
	 * @return
	 */
	public List<User> getRelatedList(String id);

	/**
	 * フレンド一覧を返す
	 *
	 * @param id
	 * @return
	 */
	public List<User> getFriendList(String id);

	/**
	 * ２ユーザがフレンドかどうかを返す
	 *
	 * @param id1
	 * @param id2
	 * @return
	 */
	public boolean isFriend(String id1, String id2);
}
