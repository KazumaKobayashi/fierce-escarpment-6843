package com.example.service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.exception.FriendRelationExistsException;
import com.example.exception.FriendRelationNotFoundException;
import com.example.exception.UserNotFoundException;
import com.example.model.FriendRelation;
import com.example.model.FriendRelationPK;

/**
 * FriendRelationServiceの実装クラス
 *
 * @author Kazuki Hasegawa
 * @see com.exmaple.service.FriendService
 */
@Service
public class FriendServiceImpl implements FriendService {
	@Autowired
	private UserService userService;

	@PersistenceContext
	EntityManager em;

	@Transactional
	@Override
	public FriendRelation create(String id1, String id2) throws FriendRelationExistsException, UserNotFoundException {
		// ユーザの存在確認
		if (userService.getUser(id1) == null) {
			throw new UserNotFoundException("User not found. Id: " + id1);
		}
		if (userService.getUser(id2) == null) {
			throw new UserNotFoundException("User not found. Id: " + id2);
		}

		// プライマリキーの作成
		FriendRelationPK pk = new FriendRelationPK();
		// 存在確認(申請先がすでに出しているか）
		pk.setId1(id2);
		pk.setId2(id1);
		FriendRelation relation = em.find(FriendRelation.class, pk);
		if (relation != null) {
			try {
				// 更新する
				return allow(id2, id1);
			} catch (FriendRelationNotFoundException e) {
				// 起こりえるはずがないが一応
				// TODO: ロガー
				e.printStackTrace();
			}
		}
		// 存在確認
		pk.setId1(id1);
		pk.setId2(id2);
		relation = em.find(FriendRelation.class, pk);
		// 存在確認
		if (relation != null) {
			throw new FriendRelationExistsException("FriendRelation already exists.");
		}
		
		// リレーションの作成
		relation = new FriendRelation();
		relation.setPk(pk);
		relation.setAllowed(false);
		// 保存
		em.persist(relation);
		return relation;
	}

	@Override
	public FriendRelation allow(String id1, String id2) throws FriendRelationNotFoundException {
		FriendRelationPK pk = new FriendRelationPK();
		pk.setId1(id1);
		pk.setId2(id2);
		FriendRelation relation =  em.find(FriendRelation.class, pk);
		// 存在確認
		if (relation == null) {
			// Idを交換して探してみる
			pk.setId1(id2);
			pk.setId2(id1);
			relation = em.find(FriendRelation.class, pk);
			if (relation == null) {
				// それでもないなら例外
				throw new FriendRelationNotFoundException("FriendRelation not found.");
			}
		}

		// 許可
		relation.setAllowed(true);

		// 保存　
		em.persist(relation);
		return relation;
	}

	@Transactional
	@Override
	public void forbid(String id1, String id2) throws FriendRelationNotFoundException {
		FriendRelationPK pk = new FriendRelationPK();
		pk.setId1(id1);
		pk.setId2(id2);
		FriendRelation relation =  em.find(FriendRelation.class, pk);
		// 存在確認
		if (relation == null) {
			// Idを交換して探してみる
			pk.setId1(id2);
			pk.setId2(id1);
			relation = em.find(FriendRelation.class, pk);
			if (relation == null) {
				// それでもないなら例外
				throw new FriendRelationNotFoundException("FriendRelation not found.");
			}
		}

		// 削除
		em.remove(relation);
	}

	@Transactional
	@Override
	public FriendRelation getFriendRelation(String id1, String id2) {
		FriendRelationPK pk = new FriendRelationPK();
		pk.setId1(id1);
		pk.setId2(id2);
		FriendRelation relation =  em.find(FriendRelation.class, pk);
		if (relation != null) {
			return relation;
		}
		// idを逆にして取得してみる
		pk.setId1(id2);
		pk.setId2(id1);
		relation = em.find(FriendRelation.class, pk);
		// 実質にrelationを返せば良いが明示的に記述しておく
		if (relation != null) {
			return relation;
		}
		return null;
	}

}
