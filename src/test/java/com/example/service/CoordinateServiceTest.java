package com.example.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.example.exception.CoordinateExistsException;
import com.example.exception.CoordinateNotFoundException;
import com.example.exception.EmailExistsException;
import com.example.exception.InvalidEmailException;
import com.example.exception.UserExistsException;
import com.example.exception.UserNotFoundException;
import com.example.model.Coordinate;

import static org.junit.Assert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * CoordinateServiceのテスト
 *
 * @author Kazuki Hasegawa
 * @see com.example.service.CoordinateService
 */
@Transactional
@TransactionConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:testContext.xml")
public class CoordinateServiceTest {
	@Autowired
	private CoordinateService service;
	@Autowired
	private UserService userService;

	private String id = CoordinateServiceTest.class.getName();
	private String email = "example@example.com";
	private String password = "kazumakobayashi";

	@Before
	public void setup() throws CoordinateExistsException, UserNotFoundException, UserExistsException, InvalidEmailException, EmailExistsException {
		// ユーザ作成時点で作成される
		userService.create(id, email, password);
	}

	/**
	 * 座標情報取得テスト
	 */
	@Test
	public void 座標情報を取得する() {
		Coordinate coord = service.getCoordinate(id);
		assertThat(coord, is(notNullValue()));
		assertThat(coord.getUserId(), is(id));
		assertThat(coord.getLat(), is(0.0));
		assertThat(coord.getLng(), is(0.0));
	}

	/**
	 * 座標情報更新テスト
	 *
	 * @throws UserNotFoundException 
	 */
	@Test
	public void 座標情報を更新する() throws UserNotFoundException {
		Coordinate coord = service.getCoordinate(id);
		assertThat(coord.getLat(), is(0.0));
		assertThat(coord.getLng(), is(0.0));

		// 座標を更新
		Double lat = 45.123, lng = 57.438;
		service.update(id, lat, lng);

		// 再取得
		coord = service.getCoordinate(id);
		assertThat(coord.getLat(), is(lat));
		assertThat(coord.getLng(), is(lng));
	}

	/**
	 * ２つのユーザ間のおおよその距離算出テスト
	 *
	 * @throws UserExistsException
	 * @throws InvalidEmailException
	 * @throws EmailExistsException
	 * @throws UserNotFoundException
	 * @throws CoordinateNotFoundException 
	 */
	@Test
	public void 座標間の距離を求める() throws UserExistsException, InvalidEmailException, EmailExistsException, UserNotFoundException, CoordinateNotFoundException {
		String id1 = "kazuma1", email1 = "kazuma1@kazuma.com";
		String id2 = "kazuma2", email2 = "kazuma2@kazuma.com";

		userService.create(id1, email1, password);
		userService.create(id2, email2, password);

		double lat1 = 35.626303, lng1 = 139.339350;
		double lat2 = 35.631364, lng2 = 139.330975;
		service.update(id1, lat1, lng1);
		service.update(id2, lat2, lng2);
		assertThat(Math.floor(service.getDistanceBetween(id1, id2)), is(942.0));
	}

	/**
	 * 座標情報の2重で作成出来るか試みる
	 *
	 * @throws UserNotFoundException
	 * @throws CoordinateExistsException
	 */
	@Test(expected=CoordinateExistsException.class)
	public void 座標情報を再び作成する() throws UserNotFoundException, CoordinateExistsException {
		service.create(id, 0.0, 0.0);
	}

	/**
	 * 存在しないユーザの座標情報を作成するテスト
	 *
	 * @throws UserNotFoundException
	 * @throws CoordinateExistsException
	 */
	@Test(expected=UserNotFoundException.class)
	public void 存在しないユーザIdの座標情報を作成する() throws UserNotFoundException, CoordinateExistsException {
		service.create("kazuma", 0.0, 0.0);
	}

	/**
	 * 存在しないユーザの座標情報を更新するテスト
	 *
	 * @throws UserNotFoundException
	 */
	@Test(expected=UserNotFoundException.class)
	public void 存在しないユーザIdの座標情報を更新する() throws UserNotFoundException {
		service.update("kazuma", 0.0, 0.0);
	}

	/**
	 * ユーザ2の方を作成せずに距離を算出するテスト
	 *
	 * @throws CoordinateNotFoundException
	 * @throws UserNotFoundException
	 * @throws UserExistsException
	 * @throws InvalidEmailException
	 * @throws EmailExistsException
	 */
	@Test(expected=CoordinateNotFoundException.class)
	public void 存在しないユーザ間の距離を求める() throws CoordinateNotFoundException, UserNotFoundException, UserExistsException, InvalidEmailException, EmailExistsException {
		String id1 = "kazuma1", email1 = "kazuma1@kazuma.com";
		String id2 = "kazuma2";

		userService.create(id1, email1, password);

		double lat1 = 35.626303, lng1 = 139.339350;
		service.update(id1, lat1, lng1);
		assertThat(Math.floor(service.getDistanceBetween(id1, id2)), is(942.0));
	}
}
