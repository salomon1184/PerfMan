package com.salomon.perfman.controller;

import java.io.File;

import com.salomon.perfman.model.Phone;
import com.salomon.perfman.util.Config;
import com.salomon.perfman.util.Helper;
import com.salomon.perfman.util.Utility;

public class TextRecorder {
	private final String logPath;
	private final String fileName;

	public String getLogPath() {
		return this.logPath;
	}

	public String getFileName() {
		return this.fileName;
	}

	/**
	 * 采样结果记录，文件格式./Log/20140521/LGE_Nexus4_4.4
	 * .2_00505bd7a2137271/com.quad/cpu_12050635.log
	 * 
	 * @param fileFlag
	 */
	public TextRecorder(Phone phone, String packageName, String fileFlag) {

		this.logPath = Helper.combineStrings(Config.ROOTPATH_LOG,
				Utility.getFormatDate("yyyyMMdd"), File.separator,
				phone.toString(), File.separator, packageName, File.separator
						+ Utility.getFormatDate("HHmmss") + File.separator);
		File rootFile = new File(this.logPath);
		if (!rootFile.exists()) {
			rootFile.mkdirs();
		}

		this.fileName = fileFlag + "_" + ".log";
	}

	public void writePerfDataToFile(String perfData) {
		Helper.writeDataToFile(this.logPath, perfData, this.fileName);
	}
}
