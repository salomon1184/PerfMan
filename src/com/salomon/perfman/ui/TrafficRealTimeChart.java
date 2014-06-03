package com.salomon.perfman.ui;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import com.salomon.perfman.controller.TextRecorder;
import com.salomon.perfman.model.Phone;
import com.salomon.perfman.util.AndroidUtility;
import com.salomon.perfman.util.Utility;

public class TrafficRealTimeChart extends ChartPanel implements Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static TimeSeries timeSeries;
	private final long value = 0;
	boolean makeStop = false;
	private final AndroidUtility utility;
	private Phone phone;
	private String packName;
	private TextRecorder recorder;
	private float startNum;

	public Phone getPhone() {
		return this.phone;
	}

	public void setPhone(Phone phone) {
		this.phone = phone;
	}

	public String getPackName() {
		return this.packName;
	}

	public void setPackName(String packName) {
		this.packName = packName;
	}

	public void setRecorder(TextRecorder recorder) {
		this.recorder = recorder;
	}

	public void setStartNum(float startNum) {
		this.startNum = startNum;
	}

	public TextRecorder getRecorder() {
		return this.recorder;
	}

	public float getStartNum() {
		return this.startNum;
	}

	public TrafficRealTimeChart(String chartContent, String title,
			String yaxisName) {
		super(createChart(chartContent, title, yaxisName));
		this.utility = new AndroidUtility(Utility.getMachineType());
	}

	@SuppressWarnings("deprecation")
	private static JFreeChart createChart(String chartContent, String title,
			String yaxisName) {
		// 创建时序图对象
		timeSeries = new TimeSeries(chartContent, Millisecond.class);
		TimeSeriesCollection timeseriescollection = new TimeSeriesCollection(
				timeSeries);
		JFreeChart jfreechart = ChartFactory.createTimeSeriesChart(title,
				"Time(Seconds)", yaxisName, timeseriescollection, true, true,
				false);
		XYPlot xyplot = jfreechart.getXYPlot();
		// 纵坐标设定
		ValueAxis valueaxis = xyplot.getDomainAxis();
		// 自动设置数据轴数据范围
		valueaxis.setAutoRange(true);
		// 数据轴固定数据范围 30s
		valueaxis.setFixedAutoRange(30000D);

		valueaxis = xyplot.getRangeAxis();
		// valueaxis.setRange(0.0D,200D);

		return jfreechart;
	}

	public void run() {
		while (true) {
			try {
				if (this.makeStop == true) {
					return;
				}

				// /FIXME 更换为Traffic监控值
				long trafficData = this.utility.getTrafficValue(this.phone,
						this.packName);
				timeSeries.add(new Millisecond(), trafficData - this.startNum);
				this.recorder.writePerfDataToFile(Long.toString(trafficData));

				Thread.sleep(3000);
			} catch (InterruptedException e) {
			}
		}

	}

	public void reset() {
		this.makeStop = false;
	}

	public void stop() {
		this.makeStop = true;
		timeSeries.clear();
	}
}
