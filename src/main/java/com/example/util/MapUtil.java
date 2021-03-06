package com.example.util;

/**
 * 地図系のUtilクラス
 *
 * @author Kazuki Hasegawa
 */
public final class MapUtil {
	private MapUtil() {}

	/**
	 * 2つの経緯度から距離を求める
	 *
	 * @param lat1
	 * @param lng1
	 * @param lat2
	 * @param lng2
	 * @return
	 */
	public static double getDistanceBetween(double lat1, double lng1, double lat2, double lng2, EllipsoidBody type) {
		double dx = toRadians(lng1 - lng2), dy = toRadians(lat1 - lat2);
		double my = toRadians((lat1 + lat2) / 2.0);
		double w = Math.sqrt(1 - type.getE2() * Math.pow(Math.sin(my), 2));
		double n = type.getA() / w;
		double m = type.getMNum() / w * w * w;
		return Math.sqrt(Math.pow(dy * m, 2) + Math.pow(dx * n * Math.cos(my), 2));
	}

	/**
	 * ラジアンに変換する
	 *
	 * @param value
	 * @return
	 */
	private static double toRadians(double value) {
		return value * Math.PI / 180;
	}

	/**
	 * 楕円体の列挙型
	 *
	 * @author Kazuki Hasegawa
	 */
	public enum EllipsoidBody {
		BESSEL(6377397.155000, 6356079.000000),
		GRS80(6378137.000000, 6356752.314140),
		WGS84(6378137.000000, 6356752.314245);

		// 長半径
		private final double a;
		// 第一離心率の２乗数
		private final double e2;
		// 子午線曲率半径
		private final double mNum;

		private EllipsoidBody(double a, double b) {
			this.a = a;
			double e = Math.sqrt((a * a - b * b) / (a * a));
			this.e2 = e * e;
			this.mNum = this.a * (1 - this.e2);
		}

		private double getA() {
			return a;
		}

		private double getE2() {
			return e2;
		}

		private double getMNum() {
			return mNum;
		}
	}
}
