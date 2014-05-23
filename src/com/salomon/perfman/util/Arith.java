package com.salomon.perfman.util;

import java.math.BigDecimal;

/**
 * 由于Java的简单类型不能够精确的对浮点数进行运算，这个工具类提供精 确的浮点数运算，包括加减乘除和四舍五入 直接加减乘除的话,在多运行几遍就会出问题的.
 * 以后的运算建议都用这个类中的方法吧.
 * 
 * 测试中进行计算的数据多为float型,所以本类的运算函数都是基于float型的
 * 
 * @author <a href="mailto:defu.li@renren-inc.com">defu</a> Nov 2, 2012
 */

public class Arith {

	// 默认除法运算精度
	private static final int DEF_DIV_SCALE = 10;

	// 这个类不能实例化
	public Arith() {

	}

	/**
	 * 提供精确的float加法运算。
	 * 
	 * @param v1
	 *            被加数
	 * @param v2
	 *            加数
	 * @return 两个参数的和
	 * @author <a href="mailto:defu.li@renren-inc.com">salomon</a> Nov 2, 2012
	 */
	public static float floatAdd(float v1, float v2) {
		BigDecimal b1 = new BigDecimal(Float.toString(v1));
		BigDecimal b2 = new BigDecimal(Float.toString(v2));
		return b1.add(b2).floatValue();
	}

	/**
	 * 提供精确的long加法运算
	 * 
	 * @param v1
	 * @param v2
	 * @return 两个long型参数的和
	 * @author <a href="mailto:defu.li@renren-inc.com">salomon</a> Nov 2, 2012
	 */
	public static long longAdd(long v1, long v2) {
		BigDecimal b1 = new BigDecimal(Long.toString(v1));
		BigDecimal b2 = new BigDecimal(Long.toString(v2));
		return b1.add(b2).longValue();
	}

	/**
	 * 提供精确的减法运算。
	 * 
	 * @param v1
	 *            被减数
	 * @param v2
	 *            减数
	 * @return 两个参数的差
	 * @author <a href="mailto:defu.li@renren-inc.com">salomon</a> Nov 2, 2012
	 */
	public static float floatSub(float v1, float v2) {
		BigDecimal b1 = new BigDecimal(Float.toString(v1));
		BigDecimal b2 = new BigDecimal(Float.toString(v2));
		return b1.subtract(b2).floatValue();
	}

	/**
	 * 提供精确的long减法运算。
	 * 
	 * @param v1
	 *            被减数
	 * @param v2
	 *            减数
	 * @return 两个参数的差
	 * @author <a href="mailto:defu.li@renren-inc.com">salomon</a> Nov 2, 2012
	 */
	public static float longSub(long v1, long v2) {
		BigDecimal b1 = new BigDecimal(Long.toString(v1));
		BigDecimal b2 = new BigDecimal(Long.toString(v2));
		return b1.subtract(b2).longValue();
	}

	/**
	 * 提供精确的float乘法运算。
	 * 
	 * @param v1
	 *            被乘数
	 * @param v2
	 *            乘数
	 * @return 两个参数的积
	 * @author <a href="mailto:defu.li@renren-inc.com">salomon</a> Nov 2, 2012
	 */
	public static float floatMul(float v1, float v2) {
		BigDecimal b1 = new BigDecimal(Float.toString(v1));
		BigDecimal b2 = new BigDecimal(Float.toString(v2));
		return b1.multiply(b2).floatValue();
	}

	/**
	 * 提供精确的long乘法运算。
	 * 
	 * @param v1
	 *            被乘数
	 * @param v2
	 *            乘数
	 * @return 两个参数的积
	 * @author <a href="mailto:defu.li@renren-inc.com">salomon</a> Nov 2, 2012
	 */
	public static float longMul(long v1, long v2) {
		BigDecimal b1 = new BigDecimal(Long.toString(v1));
		BigDecimal b2 = new BigDecimal(Long.toString(v2));
		return b1.multiply(b2).longValue();
	}

	/**
	 * 提供（相对）精确的float除法运算，当发生除不尽的情况时，精确到 小数点以后10位，以后的数字四舍五入。
	 * 
	 * @param v1
	 *            被除数
	 * @param v2
	 *            除数
	 * @return 两个参数的商
	 * @author <a href="mailto:defu.li@renren-inc.com">salomon</a> Nov 2, 2012
	 */

	public static float floatDiv(float v1, float v2) {
		return Arith.floatDiv(v1, v2, Arith.DEF_DIV_SCALE);

	}

	/**
	 * 提供（相对）精确的long除法运算，当发生除不尽的情况时，精确到 小数点以后10位，以后的数字四舍五入。
	 * 
	 * @param v1
	 *            被除数
	 * @param v2
	 *            除数
	 * @return 两个参数的商
	 * @author <a href="mailto:defu.li@renren-inc.com">salomon</a> Nov 2, 2012
	 */

	public static float longDiv(long v1, long v2) {
		return Arith.longDiv(v1, v2, Arith.DEF_DIV_SCALE);

	}

	/**
	 * 提供（相对）精确的float除法运算。当发生除不尽的情况时，由scale参数指定精度，以后的数字四舍五入。
	 * 
	 * @param v1
	 *            被除数
	 * @param v2
	 *            除数
	 * @param scale
	 *            表示表示需要精确到小数点以后几位。
	 * @return 两个参数的商
	 * @author <a href="mailto:defu.li@renren-inc.com">salomon</a> Nov 2, 2012
	 */

	public static float floatDiv(float v1, float v2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}

		BigDecimal b1 = new BigDecimal(Float.toString(v1));
		BigDecimal b2 = new BigDecimal(Float.toString(v2));

		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).floatValue();

	}

	/**
	 * 提供（相对）精确的long除法运算。当发生除不尽的情况时，由scale参数指定精度，以后的数字四舍五入。
	 * 
	 * @param v1
	 *            被除数
	 * @param v2
	 *            除数
	 * @param scale
	 *            表示表示需要精确到小数点以后几位。
	 * @return 两个参数的商
	 * @author <a href="mailto:defu.li@renren-inc.com">salomon</a> Nov 2, 2012
	 */

	public static float longDiv(long v1, long v2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}

		BigDecimal b1 = new BigDecimal(Long.toString(v1));
		BigDecimal b2 = new BigDecimal(Long.toString(v2));

		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).floatValue();

	}

	/**
	 * 针对float提供精确的小数位四舍五入处理。
	 * 
	 * @param v
	 *            需要四舍五入的数字
	 * @param scale
	 *            小数点后保留几位
	 * @return 四舍五入后的结果
	 * @author <a href="mailto:defu.li@renren-inc.com">salomon</a> Nov 2, 2012
	 */

	public static float floatRound(float v, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");

		}

		BigDecimal b = new BigDecimal(Float.toString(v));
		BigDecimal one = new BigDecimal("1");

		return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).floatValue();
	}

	/**
	 * 针对long提供精确的小数位四舍五入处理。
	 * 
	 * @param v
	 *            需要四舍五入的数字
	 * @param scale
	 *            小数点后保留几位
	 * @return 四舍五入后的结果
	 * @author <a href="mailto:defu.li@renren-inc.com">salomon</a> Nov 2, 2012
	 */

	public static long longRound(long v, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");

		}

		BigDecimal b = new BigDecimal(Long.toString(v));
		BigDecimal one = new BigDecimal("1");

		return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).longValue();
	}

};
