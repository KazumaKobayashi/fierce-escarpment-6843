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
}
