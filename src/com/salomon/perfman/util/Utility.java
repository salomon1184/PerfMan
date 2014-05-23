package com.salomon.perfman.util;

import java.io.File;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utility {
	public static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private static Calendar calendar = Calendar.getInstance();

	/**
	 * 获得指定日期的前n天
	 * 
	 * @param specifiedDay
	 * @param days
	 * @return
	 */
	public static Date getSpecifiedDayBefore(Date specifiedDay, int days) {
		Calendar c = Calendar.getInstance();
		c.setTime(specifiedDay);
		c.add(Calendar.DAY_OF_YEAR, -days);

		return c.getTime();
	}

	/**
	 * 获得指定日期的后n天
	 * 
	 * @param specifiedDay
	 * @param days
	 * @return
	 */
	public static Date getSpecifiedDayAfter(Date specifiedDay, int days) {
		Calendar c = Calendar.getInstance();

		c.setTime(specifiedDay);
		c.add(Calendar.DAY_OF_YEAR, days);

		return c.getTime();
	}

	/**
	 * 计算两个日期之间相差的天数
	 * 
	 * @param smdate
	 *            较小的时间
	 * @param bdate
	 *            较大的时间
	 * @return 相差天数
	 * @throws ParseException
	 */
	public static long daysBetween(Date smdate, Date bdate) {
		long quot = bdate.getTime() - smdate.getTime();
		quot = quot / 1000 / 60 / 60 / 24;
		return quot;
	}

	public static int getDateDay(Date date) {
		if ((date == null) || date.equals(parseDate("1900-1-1", "yyyy-MM-dd"))) {
			return 0;
		}
		calendar.clear();
		calendar.setTime(date);
		return (calendar.get(Calendar.YEAR) * 10000)
				+ ((calendar.get(Calendar.MONTH) + 1) * 100)
				+ calendar.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 字符串转换到时间格式
	 * 
	 * @param dateStr
	 *            需要转换的字符串
	 * @param formatStr
	 *            需要格式的目标字符串 举例 yyyy-MM-dd HH:mm:ss
	 * @return Date 返回转换后的时间
	 * @throws ParseException
	 *             转换异常
	 */
	public static Date parseDate(String dateStr, String formatStr) {
		if (isEmpty(dateStr)) {
			return null;
		}
		if (isEmpty(formatStr)) {
			formatStr = "yyyy-MM-dd HH:mm:ss";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
		Date retDate = null;
		try {
			retDate = sdf.parse(dateStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retDate;
	}

	public static int getInt(String str) {
		int retValue = 0;
		if (!isEmpty(str)) {
			try {
				retValue = Integer.valueOf(str);
			} catch (Exception e) {
			}
		}
		return retValue;
	}

	/**
	 * 判断字符串是否为空
	 */
	public static boolean isEmpty(String testStr) {
		return (null == testStr) || (testStr.length() == 0)
				|| "null".equalsIgnoreCase(testStr);
	}

	/**
	 * 获得机器类型。 返回至为Windows， Linux， Mac OS X。 在constants类中定义。
	 * 
	 * @throws Exception
	 */
	public final static String getMachineType() {
		String os = System.getProperty("os.name");
		if (os.startsWith(Config.WIN_OSNAME)) {
			return Config.WIN_OSNAME;
		} else if (os.startsWith(Config.LINUX_OSNAME)) {
			return Config.LINUX_OSNAME;
		} else if (os.startsWith(Config.OSX_OSNAME)) {
			return Config.OSX_OSNAME;
		} else {
			return "";
		}

	}

	/**
	 * 根据类名和方法名执行方法
	 * 
	 * @param classPathName
	 *            类全名，包括包名
	 * @param methodName
	 *            调用的方法名称
	 * @param parmTypeList
	 *            调用的方法参数类型列表
	 * @param parmList
	 *            调用的方法参数列表
	 */
	public static Object Invoke(String classPathName, String methodName,
			Class<?>[] parmTypeList, Object[] parmList) throws Exception {

		Object retValue = null;
		Class<?> c = Class.forName(classPathName);
		Object obj = c.newInstance();
		Method method = c.getMethod(methodName, parmTypeList);
		retValue = method.invoke(obj, parmList);
		return retValue;
	}

	/**
	 * 获取格式化后的当前时间
	 * 
	 * @return yyyy-MM-dd HH:mm:ss
	 */
	public static String getFormatDate() {
		return getFormatDate("yyyyMMdd--HHmm");
	}

	/**
	 * 获取格式化后的当前时间
	 * 
	 * @param date
	 *            传入的时间
	 * @return yyyy-MM-dd HH:mm:ss
	 */
	public static String getFormatDate(Date date) {
		return getFormatDate("yyyy-MM-dd HH:mm:ss", date);
	}

	/**
	 * 获取格式化后的当前时间
	 * 
	 * @param pattern
	 *            格式化字符
	 * @return
	 */
	public static String getFormatDate(String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(Calendar.getInstance().getTime());
	}

	/**
	 * 获取格式化后的当前时间
	 * 
	 * @param pattern
	 *            格式化字符
	 * @param date
	 *            传入的时间
	 * @return
	 */
	public static String getFormatDate(String pattern, Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}

	/**
	 * 指定路径，循环创建文件夹，如果已有文件夹，则跳过
	 * 
	 * @param outFileName
	 */
	public static void makeDir(String outFileName) {
		Pattern p = Pattern.compile("[/\\" + File.separator + "]");
		Matcher m = p.matcher(outFileName);
		while (m.find()) {
			int index = m.start();
			String subDir = outFileName.substring(0, index);
			File subDirFile = new File(subDir);
			if (!subDirFile.exists()) {
				subDirFile.mkdir();
			}
		}
	}
}
