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
import com.example.model.Coordinate;

import static org.junit.Assert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

@Transactional
@TransactionConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:testContext.xml")
public class CoordinateServiceTest {
	@Autowired
	private CoordinateService service;

	private String id = CoordinateServiceTest.class.getName();

	@Before
	public void setup() throws CoordinateExistsException {
		service.create(id, 0.0, 0.0);
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

	@Test
	public void 座標情報を更新する() {
		Coordinate coord = service.getCoordinate(id);
		assertThat(coord.getLat(), is(0.0));
		assertThat(coord.getLng(), is(0.0));

		// 座標を更新
		Double lat = 45.123, lng = 57.438;
		service.update(id, lat, lng);

		// 再取得
		coord = service.getCoordinate(id);
		assertThat(coord.getLat(), is(lat));
		assertThat(coord.getLng(), is(lng));;
	}
}
