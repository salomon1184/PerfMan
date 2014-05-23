package com.salomon.perfman.util;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * @summary Log相关
 * @author defu.li
 * 
 */
public class Log {
	/**
	 * @summary 输出相关信息
	 * @param str
	 */
	public static void log(String str) {
		Logger logger = Logger.getLogger(Log.class);
		PropertyConfigurator.configure(Config.ROOTPATH_LOGCONFIG);
		logger.info(str);
	}

	/**
	 * @summary 输出错误信息
	 * @param error
	 */
	public static void logError(Throwable error) {

		System.err.println(Utility.getFormatDate("yyyy-MM-dd HH:mm:ss")
				+ " - 错误信息：" + error.getMessage());
	}

	/**
	 * @summary 输出错误信息
	 * @param error
	 */
	public static void logError(String error) {
		System.err.println(Utility.getFormatDate("yyyy-MM-dd HH:mm:ss") + " - "
				+ error);
	}

	public static void logFatal(String error) {
		System.err.println(Utility.getFormatDate("yyyy-MM-dd HH:mm:ss") + " - "
				+ error);
	}
}
