package com.example.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.exception.FriendRelationExistsException;
import com.example.exception.FriendRelationNotFoundException;
import com.example.exception.UserNotFoundException;
import com.example.model.FriendRelation;
import com.example.model.FriendRelationPK;
import com.example.model.User;

/**
 * FriendRelationServiceの実装クラス
 *
 * @author Kazuki Hasegawa
 * @see com.exmaple.service.FriendService
 */
@Service
public class FriendServiceImpl implements FriendService {
	private static final Logger logger = LoggerFactory.getLogger(FriendServiceImpl.class);

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
				logger.error(e.getMessage());
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
		Timestamp now = new Timestamp(new Date().getTime());
		relation.setCreatedAt(now);
		relation.setUpdatedAt(now);
		// 保存
		em.persist(relation);
		return relation;
	}

	@Transactional
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
				logger.error("FriendRealtion not found. Id1: {} Id2: {}", id1, id2);
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
				logger.error("FriendRealtion not found. Id1: {} Id2: {}", id1, id2);
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
		logger.error("FriendRealtion not found. Id1: {} Id2: {}", id1, id2);
		return null;
	}

	@Transactional
	@Override
	public List<String> getRelatingList(String id) {
		TypedQuery<String> query = em.createQuery("select u.id from FriendRelation fr inner join fr.user2 u where fr.pk.id1 = :id and fr.allowed = false", String.class);
		query.setParameter("id", id);
		return query.getResultList();
	}

	@Transactional
	@Override
	public List<String> getRelatedList(String id) {
		TypedQuery<String> query = em.createQuery("select u.id from FriendRelation fr inner join fr.user1 u where fr.pk.id2 = :id and fr.allowed = false", String.class);
		query.setParameter("id", id);
		return query.getResultList();
	}

	@Transactional
	@Override
	public List<User> getFriendList(String id) {
		List<User> users = new ArrayList<User>();
		TypedQuery<User> query = em.createQuery("select u from FriendRelation fr inner join fr.user2 u where fr.pk.id1 = :id and fr.allowed = true", User.class);
		query.setParameter("id", id);
		users.addAll(query.getResultList());
		query = em.createQuery("select u from FriendRelation fr inner join fr.user1 u where fr.pk.id2 = :id and fr.allowed = true", User.class);
		query.setParameter("id", id);
		users.addAll(query.getResultList());
		return users;
	}

	@Transactional
	@Override
	public boolean isFriend(String id1, String id2) {
		FriendRelation relation = getFriendRelation(id1, id2);
		return relation != null && relation.isAllowed();
	}
}
