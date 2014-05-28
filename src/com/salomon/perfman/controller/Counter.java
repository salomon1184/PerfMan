package com.salomon.perfman.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import com.salomon.perfman.model.PerfData;
import com.salomon.perfman.util.Helper;

public class Counter {

	private final String logPath;
	private final String logFile;

	public Counter(String path, String file) {
		this.logPath = path;
		this.logFile = file;
	}

	public PerfData getPerfData() {
		float total = 0;
		long index = 1;
		PerfData data = new PerfData();

		FileReader reader;
		try {
			reader = new FileReader(this.logPath + File.separator
					+ this.logFile);
			BufferedReader bReader = new BufferedReader(reader);
			String line = bReader.readLine();
			String[] ss = line.split(Helper.SEPARTORFLAG);
			data.setMin(Float.parseFloat(ss[1]));
			total += Float.parseFloat(ss[1]);
			while ((line = bReader.readLine()) != null) {
				String[] splits = line.split(Helper.SEPARTORFLAG);
				float temp = Float.parseFloat(splits[1]);
				total += temp;

				if (data.getMin() > temp) {
					data.setMin(temp);
				}

				if (data.getMax() < temp) {
					data.setMax(temp);
				}

				index++;
			}

			data.setAva(total / index);

			reader.close();
			return data;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}
}
