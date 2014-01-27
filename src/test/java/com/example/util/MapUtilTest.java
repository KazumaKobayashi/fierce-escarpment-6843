package com.example.util;

import org.junit.Test;
import org.junit.runners.JUnit4;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * MapUtilのテスト
 *
 * @author Kazuki Hasegawa
 * @see com.example.util.MapUtil
 */
@RunWith(JUnit4.class)
public class MapUtilTest {

	/**
	 * 距離算出テスト(Bessel楕円体)
	 */
	@Test
	public void おおよその距離を求める_ベッセル楕円体() {
		// 東京工科大学
		double lat1 = 35.626303, lng1 = 139.339350;
		// 八王子みなみ野駅
		double lat2 = 35.631364, lng2 = 139.330975;
		double distanceBetween = MapUtil.getDistanceBetween(lat1, lng1, lat2, lng2, MapUtil.EllipsoidBody.BESSEL);
		assertThat(Math.floor(distanceBetween), is(942.0));
	}

	/**
	 * 距離算出テスト(GRS80)
	 */
	@Test
	public void おおよその距離を求める_GRS80() {
		// 東京工科大学
		double lat1 = 35.626303, lng1 = 139.339350;
		// 八王子みなみ野駅
		double lat2 = 35.631364, lng2 = 139.330975;
		double distanceBetween = MapUtil.getDistanceBetween(lat1, lng1, lat2, lng2, MapUtil.EllipsoidBody.GRS80);
		assertThat(Math.floor(distanceBetween), is(942.0));
	}

	/**
	 * 距離算出テスト(GRS80)
	 */
	@Test
	public void おおよその距離を求める_WGS84() {
		// 東京工科大学
		double lat1 = 35.626303, lng1 = 139.339350;
		// 八王子みなみ野駅
		double lat2 = 35.631364, lng2 = 139.330975;
		double distanceBetween = MapUtil.getDistanceBetween(lat1, lng1, lat2, lng2, MapUtil.EllipsoidBody.WGS84);
		assertThat(Math.floor(distanceBetween), is(942.0));
	}
}
