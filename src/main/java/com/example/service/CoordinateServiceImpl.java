package com.example.service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.exception.CoordinateExistsException;
import com.example.model.Coordinate;

/**
 * CoordinateServiceの実装クラス
 *
 * @author Kazuki Hasegawa
 */
@Service
public class CoordinateServiceImpl implements CoordinateService {
	@PersistenceContext
	EntityManager em;

	@Transactional
	@Override
	public Coordinate create(String userId, Double lat, Double lng) throws CoordinateExistsException {
		Coordinate coord = em.find(Coordinate.class, userId);
		if (coord != null) {
			throw new CoordinateExistsException("Coordinate already exists. Id: " + userId);
		}
		coord = new Coordinate();
		coord.setUserId(userId);
		coord.setLat(lat);
		coord.setLng(lng);
		em.persist(coord);
		return coord;
	}

	@Override
	@Transactional
	public Coordinate update(String userId, Double lat, Double lng) {
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

	@Override
	public Coordinate getCoordinate(String userId) {
		return em.find(Coordinate.class, userId);
	}
}
