package com.salomon.perfman.ui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import com.salomon.perfman.util.Utility;

public class LineChartBuilder {

	private final File dataLogFile;

	public LineChartBuilder(File dataPath) {
		this.dataLogFile = dataPath;
	}

	@SuppressWarnings("deprecation")
	public JFreeChart createChart(String chartContent, String title,
			String yaxisName) {
		// 创建时序图对象
		TimeSeries timeSeries = new TimeSeries(chartContent, Millisecond.class);
		TimeSeriesCollection timeseriescollection = new TimeSeriesCollection(
				timeSeries);
		JFreeChart jfreechart = ChartFactory.createTimeSeriesChart(title,
				"Time(Seconds)", yaxisName, timeseriescollection, true, true,
				false);

		FileReader fileReader;
		try {
			fileReader = new FileReader(this.dataLogFile);
			BufferedReader bReader = new BufferedReader(fileReader);
			String line = null;
			while ((line = bReader.readLine()) != null) {
				if (line != null) {
					String[] split = line.split("  ");
					Date date = Utility.parseDate(split[0],
							"yyyy-MM-dd hh:mm:ss");
					timeSeries.add(new Millisecond(date),
							Double.parseDouble(split[1]));
				}
			}
			fileReader.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println(e.getMessage());
		}

		XYPlot xyplot = jfreechart.getXYPlot();
		// 纵坐标设定
		ValueAxis valueaxis = xyplot.getDomainAxis();
		// 自动设置数据轴数据范围
		valueaxis.setAutoRange(true);
		// 数据轴固定数据范围 30s
		// valueaxis.setFixedAutoRange(30000D);

		valueaxis = xyplot.getRangeAxis();
		// valueaxis.setRange(0.0D,200D);

		return jfreechart;
	}

	public void saveAsFile(JFreeChart chart, String outPutPath, int height,
			int width) {
		FileOutputStream outputStream = null;
		File outputFile = new File(outPutPath);
		if (!outputFile.getParentFile().exists()) {
			outputFile.getParentFile().mkdirs();
		}

		try {
			outputStream = new FileOutputStream(outputFile);
			ChartUtilities.writeChartAsJPEG(outputStream, chart, width, height);
			outputStream.flush();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

}
