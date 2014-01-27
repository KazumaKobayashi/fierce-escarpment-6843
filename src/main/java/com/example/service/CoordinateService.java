package com.example.service;

import com.example.exception.CoordinateExistsException;
import com.example.exception.CoordinateNotFoundException;
import com.example.exception.UserNotFoundException;
import com.example.model.Coordinate;

/**
 * 地図座標用のサービス
 *
 * @author Kazuki Hasegawa
 */
public interface CoordinateService {
	/**
	 * 座標情報の作成
	 *
	 * @param userId
	 * @param lat
	 * @param lng
	 * @return
	 * @throws CoordinateExistsException 
	 */
	public Coordinate create(String userId, Double lat, Double lng) throws UserNotFoundException, CoordinateExistsException;

	/**
	 * 座標情報を更新
	 *
	 * @param userId
	 * @param lat
	 * @param lng
	 * @return
	 * @throws UserNotFoundException
	 */
	public Coordinate update(String userId, Double lat, Double lng) throws UserNotFoundException;

	/**
	 * 座標を取得
	 *
	 * @param userId
	 * @return
	 */
	public Coordinate getCoordinate(String userId);

	/**
	 * 2点間の距離をメートルで求める
	 *
	 * @return
	 * @throws CoordinateNotFoundException
	 */
	public double getDistanceBetween(String userId1, String userId2) throws CoordinateNotFoundException;
}
