package com.salomon.perfman.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.StringTokenizer;

import com.salomon.perfman.model.Phone;

public class AndroidUtility {

	private String aapt;
	private String adb;
	private String commonResPath;

	private static final String BIN_SH = "/bin/sh";

	public AndroidUtility(String machineType) {
		this.initTools(machineType);
	}

	/**
	 * 确定adb文件本地路径，执行adb命令的时候需要
	 */
	private void initTools(String machineType) {
		if (machineType.equals(Config.OSX_OSNAME)) {
			this.commonResPath = Helper.combineStrings(Config.PATH_COMMON_RES,
					"macosx");
			this.adb = Helper.combineStrings(this.commonResPath,
					File.separator, "adb");
			this.aapt = Helper.combineStrings(this.commonResPath,
					File.separator, "aapt");
		} else if (machineType.equals(Config.WIN_OSNAME)) {
			this.commonResPath = Helper.combineStrings(Config.PATH_COMMON_RES,
					"windows");
			this.adb = Helper.combineStrings(this.commonResPath,
					File.separator, "adb.exe");
			// System.out.println(new File(this.adb).getAbsolutePath());
			// System.out.println(Config.ROOTPATH_LOGCONFIG);
			this.aapt = Helper.combineStrings(this.commonResPath,
					File.separator, "aapt.exe");
		} else {
			this.commonResPath = Helper.combineStrings(Config.PATH_COMMON_RES,
					"linux");
			this.adb = Helper.combineStrings(this.commonResPath,
					File.separator, "adb");
			this.aapt = Helper.combineStrings(this.commonResPath,
					File.separator, "aapt");
		}
	}

	/**
	 * 获取本机插入的所有手机的serial number (adb devices).
	 * 
	 * @return
	 */
	private ArrayList<String> getPhoneSerialNum() {
		String cmdGetPhones = this.adb + " devices";
		ArrayList<String> serialNumList = new ArrayList<String>();
		try {
			Process p = Runtime.getRuntime().exec(cmdGetPhones);
			final InputStream inStream = p.getInputStream();

			BufferedReader bReader = new BufferedReader(new InputStreamReader(
					inStream));

			String line2 = null;
			while ((line2 = bReader.readLine()) != null) {
				if ((line2 != null) && line2.contains("device")
						&& !line2.contains("List of devices")) {
					String templine = line2.trim();
					serialNumList.add(templine.substring(0,
							templine.length() - 6));
					// System.out.println("thread" + line2);
				}
			}

			// for (String d : serialNumList) {
			// System.out.println(d);
			// }
			return serialNumList;
		} catch (Exception e) {
			Log.logError("shell-" + cmdGetPhones + "-execution failed. Error:"
					+ e.getMessage());
			return null;
		}
	}

	private boolean getAvailability(String serialNum) {

		String cmdgetManufacturer = this.adb + " -s " + serialNum
				+ " get-state";
		boolean availability = false;
		try {
			Process p = Runtime.getRuntime().exec(cmdgetManufacturer);

			final InputStream inStream = p.getInputStream();

			BufferedReader bReader = new BufferedReader(new InputStreamReader(
					inStream));

			// String line2 = null;
			String line2 = null;
			while ((line2 = bReader.readLine()) != null) {
				if ((line2 != null) && line2.contains("device")) {

					return true;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return availability;
	}

	private String getManufacturer(String serialNum) {
		String cmdgetManufacturer = this.adb + " -s " + serialNum
				+ " shell getprop ro.product.manufacturer";
		String manufacturer = null;

		try {
			Process p = Runtime.getRuntime().exec(cmdgetManufacturer);

			final InputStream inStream = p.getInputStream();

			BufferedReader bReader = new BufferedReader(new InputStreamReader(
					inStream));

			manufacturer = bReader.readLine().toString();
			manufacturer.replaceAll("\\||[\\s]+", "");

		} catch (Exception e) {
			e.printStackTrace();
		}

		return manufacturer;
	}

	private String getModel(String serialNum) {
		String cmdgetManufacturer = this.adb + " -s " + serialNum
				+ " shell getprop ro.product.model";
		String model = null;

		try {
			Process p = Runtime.getRuntime().exec(cmdgetManufacturer);

			final InputStream inStream = p.getInputStream();

			BufferedReader bReader = new BufferedReader(new InputStreamReader(
					inStream));

			model = bReader.readLine().toString();
			model = model.replaceAll("\\||[\\s]+", "");

		} catch (Exception e) {
			e.printStackTrace();
		}

		return model;
	}

	private String getRelease(String serialNum) {
		String cmdgetManufacturer = this.adb + " -s " + serialNum
				+ " shell getprop ro.build.version.release";
		String release = null;

		try {
			Process p = Runtime.getRuntime().exec(cmdgetManufacturer);

			final InputStream inStream = p.getInputStream();

			BufferedReader bReader = new BufferedReader(new InputStreamReader(
					inStream));

			release = bReader.readLine().toString();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return release;
	}

	/***
	 * 获取手机信息
	 * 
	 * @return
	 */
	public ArrayList<Phone> getPhones() {
		ArrayList<Phone> phoneList = new ArrayList<Phone>();
		String cmdStartServer = this.adb + " start-server";
		if (!executeCommand(cmdStartServer, "start adb server")) {
			return null;
		}

		ArrayList<String> serialNumList = this.getPhoneSerialNum();
		for (String serialNum : serialNumList) {
			Phone phone = new Phone();
			phone.setId(serialNum);
			phone.setAvailability(this.getAvailability(serialNum));
			phone.setManufacturer(this.getManufacturer(serialNum));
			phone.setModel(this.getModel(serialNum));
			phone.setOsVersion(this.getRelease(serialNum));
			phoneList.add(phone);
			Log.log("检测到手机： " + phone.toString());
		}

		return phoneList;
	}

	public static void main(String[] args) {

		AndroidUtility ultility = new AndroidUtility(Utility.getMachineType());
		// ultility.getApkInfo("C:\\Users\\defus_000\\Desktop\\Quad_V2.4_release_20130924_1843.apk");
		// try {

		// String result =
		// ultility.getMemInfoContent(ultil ity.getPhones().get(0),
		// "com.quad");
		// Log.log(ultility.getMemInfoContent(ultility.getPhones().get(0),
		// "com.quad"));
		// Log.log(ultility.getCpuInfoContent(ultility.getPhones().get(0),
		// "com.quad"));

		// Log.log(Float.toString(ultility.getCpuInfoValue(ultility.getPhones()
		// .get(0), "com.quad")));
		// String uid = ultility.getTrafficInfoContent(
		// ultility.getPhones().get(0), "com.quad");
		long total = ultility.getTrafficValue(ultility.getPhones().get(0),
				"com.quad");

		Log.log(Long.toString(total));
		// String file =
		// "C:\\Users\\defus_000\\Desktop\\METP\\Log\\monkey\\com.quad\\20140414--1252\\LGE_Nexus4_4.4.2_00505bd7a2137271\\monkey_0.log";
		// Log.logError(ultility.searchMonkeyError(file, 0));
		// Log.logError(result);
		// File testFile = new File(System.getProperty("user.dir")
		// + File.separator + "res" + File.separator
		// + "myLog4j.properties");
		//
		// System.out.println(testFile.getAbsolutePath());
		// ultility.searchMonkeyError("D:\\Workspaces_Eclipse\\METP\\Log\\monkey\\com.quad\\20131015--1102\\samsung-GT-N7000-4.1.2-364790339AB2819F\\monkey_0.log");
	}

	public long getMemInfoValue(Phone phone, String packageName) {
		String dumpInfo = this.getMemInfoContent(phone, packageName);
		if ((dumpInfo == "") || dumpInfo.isEmpty()) {
			return 0;
		}
		String[] split = dumpInfo.split("    ");
		// for (String string : split) {
		// Log.log(string);
		// }
		return Long.parseLong(split[3]) / 1024;
	}

	public float getCpuInfoValue(Phone phone, String packageName) {
		String dumpInfo = this.getCpuInfoContent(phone, packageName);
		if ((dumpInfo == "") || dumpInfo.isEmpty()) {
			return 0;
		}
		String[] split = dumpInfo.split("%");
		// for (String string : split) {
		// Log.log(string);
		// }
		return Float.parseFloat(split[0].trim());
	}

	public long getTrafficValue(Phone phone, String packageName) {
		long total = 0;
		String dumpInfo = this.getTrafficInfoContent(phone, packageName);
		if ((dumpInfo == "") || dumpInfo.isEmpty()) {
			return 0;
		}

		String[] splits = dumpInfo.split("\n");
		for (String s : splits) {
			String[] datas = s.split(" ");
			total += Long.parseLong(datas[5]) + Long.parseLong(datas[7]);
		}

		return total / 1024;
	}

	public String getMemInfoContent(Phone phone, String packageName) {
		Process process = null;
		String cmd = Helper.combineStrings(this.adb, " -s ", phone.getId(),
				"shell dumpsys meminfo ", packageName);
		try {
			process = Runtime.getRuntime().exec(cmd);
			if (process != null) {
				InputStream ins = process.getInputStream();
				InputStreamReader reader = new InputStreamReader(ins);
				BufferedReader buffer = new BufferedReader(reader);

				String line = "";
				StringBuilder sb = new StringBuilder(line);
				while ((line = buffer.readLine()) != null) {
					if (line.contains("TOTAL")) {
						sb.append(line);
						sb.append("\n");
					}
				}
				/**
				 * native dalvik other total size: 4572 3527 N/A 8099 allocated:
				 * 4113 2684 N/A 6797 free: 406 843 N/A 1249 (Pss): 1775 3572
				 * 3953 9300 (shared dirty): 1448 4020 4792 10260 (priv dirty):
				 * 1652 1308 708 3668 此行其实就是memoryinfo 读取出来的值
				 * 其中size是需要的内存，而allocated是分配了的内存
				 * ，对应的2列分别是native和dalvik，当总数也就是total这一列超过单个程序内存的最大限制时
				 * ，OOM就很有可能会出现了。 多数时候，发生OOM
				 * 都是在做一些跟图片相关的操作，以下提出一些建议尽量可以减少这种情况的发生
				 */
				process.destroy();
				return sb.toString();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "";
	}

	public String getCpuInfoContent(Phone phone, String packageName) {
		Process process = null;
		String cmd = Helper.combineStrings(this.adb, " -s ", phone.getId(),
				"shell dumpsys cpuinfo ", packageName);
		try {
			process = Runtime.getRuntime().exec(cmd);
			if (process != null) {
				InputStream ins = process.getInputStream();
				InputStreamReader reader = new InputStreamReader(ins);
				BufferedReader buffer = new BufferedReader(reader);

				String line = "";
				StringBuilder sb = new StringBuilder(line);
				while ((line = buffer.readLine()) != null) {
					if (line.contains(packageName)) {
						sb.append(line);
						sb.append("\n");
					}
				}

				process.destroy();
				return sb.toString();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "";
	}

	public String getTrafficInfoContent(Phone phone, String packageName) {
		Process process = null;
		String cmd = Helper.combineStrings(this.adb, " -s ", phone.getId(),
				"shell cat /proc/net/xt_qtaguid/stats ");
		int uid = this.getUid(phone, packageName);
		try {
			process = Runtime.getRuntime().exec(cmd);
			if (process != null) {
				InputStream ins = process.getInputStream();
				InputStreamReader reader = new InputStreamReader(ins);
				BufferedReader buffer = new BufferedReader(reader);

				String line = "";
				StringBuilder sb = new StringBuilder(line);

				/**
				 *   
				 */
				while ((line = buffer.readLine()) != null) {
					String[] splits = line.split(" ");
					// 57 wlan0 0x0 10204 1 8951 18 4928 27 8951 18 0 0 0 0 4928
					// 27 0 0 0 0
					// 58 wlan0 0x0 10206 0 137502 2331 238665 4304 137502 2331
					// 0 0 0 0 238665 4304 0 0 0 0

					// 59 wlan0 0x0 10206 1 206312 382 64565 592 204904 366 0 0
					// 1408 16 64565 592 0 0 0
					if (!line.contains("wlan")) {
						continue;
					}

					if (Integer.parseInt(splits[3]) == uid) {
						sb.append(line);
						sb.append("\n");
					}
				}

				process.destroy();
				return sb.toString();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "";
	}

	/**
	 * 根据apk安装包获取卸载时所需得包名。
	 * 
	 * @param apkPath
	 *            apk安装包所在路径。
	 * @return
	 */
	public String getPackageName(String apkPath) {
		String packageName = "";
		String apptCommand = Helper.combineStrings(this.aapt, " dump xmltree ",
				apkPath, " AndroidManifest.xml");
		try {
			Process p = Runtime.getRuntime().exec(apptCommand);
			InputStream stdoutStream = new BufferedInputStream(
					p.getInputStream());
			StringBuffer buffer = new StringBuffer();
			for (;;) {
				int c = stdoutStream.read();
				if (c == -1) {
					break;
				}
				buffer.append((char) c);
			}
			String outputText = buffer.toString();
			stdoutStream.close();

			StringTokenizer tokenizer = new StringTokenizer(outputText, "\n");
			while (tokenizer.hasMoreTokens()) {
				String line = tokenizer.nextToken().trim();
				int packageNamePosition = line.indexOf("package=");
				int packageNameEndPostion = line.indexOf(" (Raw",
						packageNamePosition);
				if ((packageNamePosition > 0) && (packageNameEndPostion > 0)) {
					packageName = line.substring(packageNamePosition + 9,
							packageNameEndPostion - 1);
					break;
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return packageName;
	}

	/**
	 * 删除设备中的/data/local/tmp/logcat和/data/local/tmp/screenshots/
	 * 
	 * @param targetPhone
	 * @return
	 */
	public void cleanDevice(Phone targetPhone) {
		String cmd = Helper.combineStrings(this.adb, " -s ",
				targetPhone.getId(), " shell rm -r ");
		executeCommand(cmd + Config.DEVICE_PATH_LOGCAT, "clean logcat");
		executeCommand(cmd + Config.DEVICE_PATH_SCREENSHOTS,
				"clean screenshots");
	}

	/**
	 * 清楚手机logcat缓存信息
	 * 
	 * @param targetPhone
	 * @return
	 */
	public boolean logcatClear(Phone targetPhone) {
		String cmd = Helper.combineStrings(this.adb, " -s ",
				targetPhone.getId(), " logcat -c");
		return executeCommand(cmd, "Phone--" + targetPhone.toString()
				+ "--clear logcat");
	}

	/**
	 * 将logcat输出重定向到文件中（PC文件）
	 * 
	 * @param targetPhone
	 * @param logfile
	 * @return
	 */
	public boolean logcatDump(Phone targetPhone, String logfile) {
		String cmd = Helper.combineStrings(this.adb, " -s ",
				targetPhone.getId(), " logcat -d -v time");
		return executeCommandWithLog(cmd, "Phone--" + targetPhone.toString()
				+ "dump logcat info to file" + logfile, new File(logfile));
	}

	/**
	 * 拉取手机中logcat文件到PC文件夹
	 * 
	 * @param targetPhone
	 * @param targetFolder
	 * @return
	 */
	public boolean pullLogcat(Phone targetPhone, String targetFolder) {
		String cmd = Helper.combineStrings(this.adb, " -s ",
				targetPhone.getId(), " pull ", Config.DEVICE_PATH_LOGCAT, " ",
				targetFolder);
		return executeCommand(cmd, "Phone--" + targetPhone.toString()
				+ " Pull logcat to folder: " + targetFolder);
	}

	/**
	 * 拉取手机中截图文件到PC文件夹
	 * 
	 * @param targetPhone
	 * @param targetFolder
	 * @return
	 */
	public boolean pullScreenShot(Phone targetPhone, String targetFolder) {
		String cmd = Helper.combineStrings(this.adb, " -s ",
				targetPhone.getId(), " pull ", Config.DEVICE_PATH_SCREENSHOTS,
				" ", targetFolder);
		return executeCommand(cmd, "Phone--" + targetPhone.toString()
				+ " Pull screenshots to folder: " + targetFolder);
	}

	/**
	 * 安装apk
	 * 
	 * @param targetPhone
	 * @param apkFilePath
	 * @return
	 */
	public boolean installApk(Phone targetPhone, String apkFilePath) {
		// String packageName = getPackageName(apkFilePath);

		String installCommand = Helper.combineStrings(this.adb, " -s ",
				targetPhone.getId(), " install -r ", apkFilePath);

		return executeCommand(installCommand,
				"Phone--" + targetPhone.toString() + "--Install apk ");
	}

	/**
	 * 由包名获取进程id
	 * 
	 * @param targetPhone
	 * @param processName
	 * @return
	 */
	public int getPid(Phone targetPhone, String processName) {

		String cmd = Helper.combineStrings(this.adb, " -s ",
				targetPhone.getId(), " shell ps c");
		int pid = 0;

		try {
			Process process = Runtime.getRuntime().exec(cmd);

			final InputStream inStream = process.getInputStream();

			BufferedReader bReader = new BufferedReader(new InputStreamReader(
					inStream));

			String line2 = null;
			// Log.log("process details:");
			while ((line2 = bReader.readLine()) != null) {

				if (line2.contains(processName)) {
					String[] splits = line2.split("\\||[\\s]+");
					// Log.log("--" + line2);
					pid = Integer.parseInt(splits[1]);
				}
			}

		} catch (Exception e) {
			Log.log("shell-" + cmd + "-FAIL. ERROR MESSAGE:" + e.getMessage());
		}

		return pid;
	}

	/**
	 * 由包名获取Uid
	 * 
	 * @param targetPhone
	 * @param processName
	 * @return
	 */
	public int getUid(Phone targetPhone, String processName) {
		String cmd = Helper.combineStrings(this.adb, " -s ",
				targetPhone.getId(), " shell dumpsys package ", processName);
		int pid = 0;

		try {
			Process process = Runtime.getRuntime().exec(cmd);

			final InputStream inStream = process.getInputStream();

			BufferedReader bReader = new BufferedReader(new InputStreamReader(
					inStream));

			String line2 = null;
			// Log.log("process details:");
			/**
			 * Packages: Package [com.quad] (64e98ca8): userId=10206 gids=[1028,
			 * 1015, 3003, 3002] pkg=Package{651a3340 com.quad}
			 * codePath=/data/app/com.quad-1.apk
			 * resourcePath=/data/app/com.quad-1.apk
			 * nativeLibraryPath=/data/app-lib/com.quad-1 versionCode=323
			 * targetSdk=9 versionName=3.2.3
			 */
			while ((line2 = bReader.readLine()) != null) {

				if (line2.contains("userId=")) {
					String[] splits = line2.split(" ");
					// Log.log("--" + line2);
					pid = Integer.parseInt(splits[4].substring(7));
				}
			}

		} catch (Exception e) {
			Log.log("shell-" + cmd + "-FAIL. ERROR MESSAGE:" + e.getMessage());
		}

		return pid;
	}

	/**
	 * 根据进程id，杀掉手机进程
	 * 
	 * @param targetPhone
	 * @param pid
	 * @return
	 */
	public boolean killProcessById(Phone targetPhone, int pid) {
		String cmd = Helper.combineStrings(this.adb, " -s ",
				targetPhone.getId(), " shell kill -9 ", Integer.toString(pid));

		return executeCommand(cmd, "kill process: " + pid);
	}

	/**
	 * 
	 * @param targetPhone
	 * @param packageName
	 * @return
	 */
	public boolean killProcessByPackageName(Phone targetPhone,
			String packageName) {
		Log.log("Kill process: " + packageName);
		return this.killProcessById(targetPhone,
				this.getPid(targetPhone, packageName));
	}

	/**
	 * 执行monkey
	 * 
	 * @param targetPhone
	 * @param packageName
	 * @param logfile
	 * @return
	 */
	public boolean runMonkey(Phone targetPhone, String packageName,
			String logfile) {
		// "adb -s jdfkljsfklj wait-for-device  shell monkey -p comn.quad  --bugreport  --ignore-timeouts  --ignore-security-exceptions "
		// +
		// "  --monitor-native-crashes  --kill-process-after-error --pct-syskeys 0   --pct-touch 80 --throttle 500  -v -v 400000 > monkeypath"
		String cmd = Helper
				.combineStrings(
						this.adb,
						" -s ",
						targetPhone.getId(),
						" wait-for-device  shell monkey -p ",
						packageName,
						"  --bugreport  --ignore-timeouts  --ignore-security-exceptions  --monitor-native-crashes  --kill-process-after-error ",
						"--pct-syskeys 0   --pct-touch 80 --throttle 500  -v -v 400000 ");
		// Log.log("monkey command: " + cmd);
		Log.log("手机--" + targetPhone.toString() + "--Moneky 开始执行");
		return executeCommandWithLog(cmd, "手机" + targetPhone.toString()
				+ "--Run monkey", new File(logfile));
	}

	/**
	 * kill monkey process of the target phone
	 * 
	 * @param targetPhone
	 * @return
	 */
	public boolean killMonkey(Phone targetPhone) {
		return this.killProcessByPackageName(targetPhone,
				"com.android.commands.monkey");
	}

	/**
	 * 倒叙搜索logcat文件，查找崩溃信息
	 * 
	 * @param monkeyLogFile
	 * @return
	 */
	public String searchMonkeyError(String monkeyLogFile) {
		// FIXME 这里的搜索性能和搜索关键字都需要调整
		String strError = "";
		try {
			RandomAccessFile raf = new RandomAccessFile(monkeyLogFile, "r");
			long length = raf.length();
			long start = raf.getFilePointer();
			long nextEnd = (start + length) - 1;

			raf.seek(nextEnd);
			while (nextEnd > start) {
				String line = raf.readLine();

				if ((line == null) || (line.length() == 0)) {
					nextEnd = nextEnd - 50;
					if (nextEnd > 0) {
						raf.seek(nextEnd);
					}
					continue;
				} else {
					nextEnd = nextEnd - line.length();
					if (nextEnd > 0) {
						raf.seek(nextEnd);

						nextEnd = nextEnd + line.length();
						if (line.contains("CRASH:")) {
							// monkeyLogFile.seek(nextIndex);
							// String packageLine =
							// monkeyLogFile.readLine();
							// monkeyLogFile.seek(nextIndex +
							// packageLine.length());
							raf.seek(nextEnd - line.length());
							String ErrorLine = raf.readLine();
							// || line.contains("new native crash detected")
							// || line.contains("native_crash_")
							// || line.contains("unexpected power cycle")
							String[] packageSplit = line.split(":");
							strError = "	CRASH	"
									+ packageSplit[1].split("\\(")[0] + "	"
									+ ErrorLine.split(":")[1];
							break;
						} else if (line.contains("not responding:")) {
							strError = "	ANR	" + "	" + line;
							break;
						} else if (line.contains("new native crash detected")
								|| line.contains("native_crash_")) {
							strError = "	NATIVE_CRASH	" + "	" + line;
							break;
						} else if (line.contains("unexpected power cycle")) {
							strError = "	POWER_CYCLE	" + "	" + line;
							break;
						}

					}
				}

			}

			raf.close();
		} catch (Exception e) {

			e.printStackTrace();
		}
		if (!strError.isEmpty()) {
			Log.log(strError);
		}
		return strError;
	}

	/**
	 * 搜索logcat文件，查找崩溃信息
	 * 
	 * @param monkeyLogFile
	 * @return
	 */
	public String searchMonkeyError(String monkeyFile, long startIndex) {
		String strError = "";
		try {
			RandomAccessFile monkeyLogFile = new RandomAccessFile(monkeyFile,
					"r");
			long start = monkeyLogFile.getFilePointer();
			long length = start + monkeyLogFile.length();
			// Log.logError("length" + length);
			long nextIndex = start + startIndex;
			monkeyLogFile.seek(nextIndex);
			while (nextIndex < length) {
				String line = monkeyLogFile.readLine();

				if (line == null) {
					monkeyLogFile.close();
					return strError;
				}

				nextIndex = nextIndex + line.length();
				if (nextIndex < length) {
					if (line.contains("CRASH:")) {
						// monkeyLogFile.seek(nextIndex);
						// String packageLine = monkeyLogFile.readLine();
						// monkeyLogFile.seek(nextIndex + packageLine.length());

						/*
						 * / CRASH: com.quad (pid 4478)
						 * 
						 * // Short Msg: net.sqlcipher.database.SQLiteException
						 * // Long Msg: net.sqlcipher.database.SQLiteException:
						 * no such column: topic_desc: , while compiling: SELECT
						 * chat_id, chat_type, input_type, input_text,
						 * emotion_tab_position, topic_desc, publisher_type FROM
						 * chat_cache WHERE chat_id = 30357 And chat_type = 1 //
						 * Build Label:
						 */
						String ErrorLine = monkeyLogFile.readLine();
						ErrorLine = monkeyLogFile.readLine();// FIXBUG--这里必须readLine两次，才能获取下一行，中间有个空白行。
						// ErrorLine = monkeyLogFile.readLine();
						// || line.contains("new native crash detected")
						// || line.contains("native_crash_")
						// || line.contains("unexpected power cycle")
						String[] packageSplit = line.split(":");
						strError = "	CRASH	" + packageSplit[1].split("\\(")[0]
								+ "	" + ErrorLine.split(":")[1];
						break;
					} else if (line.contains("not responding:")) {
						strError = "	ANR	" + "	" + line;
						break;
					} else if (line.contains("new native crash detected")
							|| line.contains("native_crash_")) {
						strError = "	NATIVE_CRASH	" + "	" + line;
						break;
					} else if (line.contains("unexpected power cycle")) {
						strError = "	POWER_CYCLE	" + "	" + line;
						break;
					}
				}
			}

			monkeyLogFile.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return strError;
	}

	public static boolean executeCommand(String strCommand, String strComment) {
		int result = 0;

		try {
			Process process = Runtime.getRuntime().exec(strCommand);

			final InputStream inStream = process.getInputStream();
			final InputStream errorStream = process.getErrorStream();
			new Thread() {
				@Override
				public void run() {
					BufferedReader bReader = new BufferedReader(
							new InputStreamReader(inStream));
					try {
						String line2 = null;
						while ((line2 = bReader.readLine()) != null) {
							if (line2 != null) {
							}
						}
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						try {
							inStream.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}.start();

			new Thread() {
				@Override
				public void run() {

					BufferedReader bReader = new BufferedReader(
							new InputStreamReader(errorStream));
					try {
						String line2 = null;
						while ((line2 = bReader.readLine()) != null) {
							if (line2 != null) {
							}
						}
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						try {
							errorStream.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}.start();

			result = process.waitFor();
			process.destroy();

			if (result == 0) {
				Log.log(strComment + " success!");
				return true;
			} else {
				Log.logError(strComment + " fail!");
				return false;
			}
		} catch (Exception e) {
			Log.logError(strComment + "-fail or be cancelled! Error message："
					+ e.getMessage());

			return false;
		}
	}

	/**
	 * 执行shell 命令，将执行log保存在文件中
	 * 
	 * @param strCommand
	 *            shell 命令
	 * @param logfile
	 *            log文件（必须指定路径）。
	 * @return
	 */
	public static boolean executeCommandWithLog(String strCommand,
			String strComment, File logfile) {
		int result = 0;
		try {
			Process process = Runtime.getRuntime().exec(strCommand);

			final InputStream inStream = process.getInputStream();
			final InputStream errorStream = process.getErrorStream();
			final File logFile = logfile;
			new Thread() {
				@Override
				public void run() {

					try {
						Helper.writeInputStreamToFile(inStream, logFile);
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						try {
							inStream.close();
						} catch (Exception e2) {
							e2.printStackTrace();
						}

					}
				}
			}.start();

			new Thread() {
				@Override
				public void run() {

					BufferedReader bReader = new BufferedReader(
							new InputStreamReader(errorStream));
					Log.log(Helper.inputStreamToString(errorStream));
					try {
						errorStream.close();
						bReader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}.start();

			process.waitFor();
			process.destroy();

			if (result == 0) {
				Log.log(strComment + " success!");
				return true;

			} else {
				Log.logError(strComment + " fail!");
				return false;
			}

		} catch (Exception e) {
			Log.logError(strComment + "-FAIL OR BE CANCELLED. ERROR MESSAGE："
					+ e.getMessage());

			return false;
		}
	}
}
