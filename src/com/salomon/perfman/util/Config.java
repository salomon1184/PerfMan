package com.salomon.perfman.util;

import java.io.File;

public class Config {

	public final static String WIN_OSNAME = "Windows";
	public final static String LINUX_OSNAME = "Linux";
	public final static String OSX_OSNAME = "Mac OS X";
	public final static String DEVICE_PATH_TMP = "/data/local/tmp";
	public final static String DEVICE_PATH_LOGCAT = "/data/local/tmp/logcat";
	public final static String DEVICE_PATH_SCREENSHOTS = "/data/local/tmp/screenshots";
	public static final String PATH_COMMON_RES = Helper.combineStrings(
			System.getProperty("user.dir"), File.separator, "res",
			File.separator);
	public static final String ROOTPATH_LOG = Helper.combineStrings(
			System.getProperty("user.dir"), File.separator, "Log",
			File.separator);
	public static final String PATH_PARAMS_XML = Helper.combineStrings(
			PATH_COMMON_RES, "params.xml");
	public static final String ROOTPATH_LOGCONFIG = Helper.combineStrings(
			PATH_COMMON_RES, "myLog4j.properties");
}
