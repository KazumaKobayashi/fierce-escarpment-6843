package com.example.service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.exception.CoordinateExistsException;
import com.example.exception.CoordinateNotFoundException;
import com.example.exception.UserNotFoundException;
import com.example.model.Coordinate;
import com.example.model.User;
import com.example.util.MapUtil;

/**
 * CoordinateServiceの実装クラス
 *
 * @author Kazuki Hasegawa
 * @see com.example.service.CoordinateService
 */
@Service
public class CoordinateServiceImpl implements CoordinateService {
	@Autowired
	private UserService userService;

	@PersistenceContext
	EntityManager em;

	@Transactional
	@Override
	public Coordinate create(String userId, Double lat, Double lng) throws UserNotFoundException, CoordinateExistsException {
		User user = userService.getUser(userId);
		if (user == null) {
			throw new UserNotFoundException("User not found. Id: " + userId);
		}
		Coordinate coord = em.find(Coordinate.class, userId);
		if (coord != null) {
			throw new CoordinateExistsException("Coordinate already exists. Id: " + userId);
		}
		coord = new Coordinate();
		coord.setUserId(userId);
		coord.setLat(lat);
		coord.setLng(lng);
		coord.setUser(user);
		// ユーザ側も設定
		user.setCoord(coord);
		em.persist(coord);
		return coord;
	}

	@Transactional
	@Override
	public Coordinate update(String userId, Double lat, Double lng) throws UserNotFoundException {
		Coordinate coord = em.find(Coordinate.class, userId);
		if (coord != null) {
			coord.setLat(lat);
			coord.setLng(lng);
			em.persist(coord);
		} else {
			// ないなら新規作成しちゃおう
			try {
				coord = create(userId, lat, lng);
			} catch (CoordinateExistsException e) {
				// 起こりえるわけがないけど念の為に
				// TODO: ロガーの追加
				e.printStackTrace();
			}
		}
		return coord;
	}

	@Transactional
	@Override
	public Coordinate getCoordinate(String userId) {
		return em.find(Coordinate.class, userId);
	}

	@Transactional
	@Override
	public double getDistanceBetween(String userId1, String userId2) throws CoordinateNotFoundException {
		Coordinate coord1 = getCoordinate(userId1);
		Coordinate coord2 = getCoordinate(userId2);
		if (coord1 == null || coord2 == null) {
			throw new CoordinateNotFoundException("座標情報が見つかりません。");
		}
		return MapUtil.getDistanceBetween(coord1.getLat(), coord1.getLng(), coord2.getLat(), coord2.getLng(), MapUtil.EllipsoidBody.GRS80);
	}
}
