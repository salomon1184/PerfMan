package com.salomon.perfman.ui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;

import org.jfree.chart.JFreeChart;

import com.salomon.perfman.controller.Counter;
import com.salomon.perfman.controller.FileSelectionAdapter;
import com.salomon.perfman.controller.TextRecorder;
import com.salomon.perfman.model.PerfData;
import com.salomon.perfman.model.Phone;
import com.salomon.perfman.util.AndroidUtility;
import com.salomon.perfman.util.ParamRetriever;
import com.salomon.perfman.util.Utility;

public class PerfMan {

	private JFrame frmPerfman;
	private JTextField textField_dataLogPath;
	private JTextField textField__jpgFilePath;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PerfMan window = new PerfMan();
					window.frmPerfman.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public PerfMan() {
		this.initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		this.frmPerfman = new JFrame();
		this.frmPerfman.setTitle("PerfMan");
		this.frmPerfman.setBounds(100, 100, 960, 680);
		this.frmPerfman.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frmPerfman.getContentPane().setLayout(null);

		// 左侧控制台面板
		JPanel panel_controller = new JPanel();
		panel_controller.setBorder(new LineBorder(Color.LIGHT_GRAY, 1, true));
		panel_controller.setBounds(10, 11, 256, 619);
		this.frmPerfman.getContentPane().add(panel_controller);

		AndroidUtility utility = new AndroidUtility(Utility.getMachineType());
		ArrayList<Phone> phones = utility.getPhones();
		DefaultComboBoxModel<Phone> phoneBoxModel = new DefaultComboBoxModel<Phone>(
				phones.toArray(new Phone[phones.size()]));
		panel_controller.setLayout(null);

		final JComboBox<Phone> comboBox_phone = new JComboBox<Phone>(
				phoneBoxModel);
		comboBox_phone.setBounds(77, 5, 169, 25);
		panel_controller.add(comboBox_phone);

		JScrollPane scrollPane_console = new JScrollPane();
		scrollPane_console.setBounds(10, 121, 236, 324);
		panel_controller.add(scrollPane_console);

		final JTextArea textArea_result = new JTextArea();
		textArea_result.setFont(textArea_result.getFont().deriveFont(
				textArea_result.getFont().getStyle() | Font.BOLD));
		scrollPane_console.setViewportView(textArea_result);
		// scrollPane_console.add(ConsolePane.getInstance());

		ArrayList<String> processes = new ArrayList<String>();
		processes = ParamRetriever.getParam().getParams("processes");
		DefaultComboBoxModel<String> processbBoxModel = new DefaultComboBoxModel<String>(
				processes.toArray(new String[processes.size()]));
		final JComboBox<String> comboBox_process = new JComboBox<String>(
				processbBoxModel);
		comboBox_process.setBounds(77, 41, 169, 25);
		panel_controller.add(comboBox_process);

		JButton btnStart = new JButton("Start");
		btnStart.setBounds(10, 77, 89, 23);
		panel_controller.add(btnStart);

		JButton btnStop = new JButton("Stop");
		btnStop.setBounds(157, 77, 89, 23);
		panel_controller.add(btnStop);

		JLabel lblPhone = new JLabel("Phone:");
		lblPhone.setBounds(10, 10, 57, 14);
		panel_controller.add(lblPhone);

		JLabel lblProcess = new JLabel("Process:");
		lblProcess.setBounds(10, 46, 57, 14);
		panel_controller.add(lblProcess);

		JLabel lblPath = new JLabel("Data Log Path:");
		lblPath.setBounds(10, 456, 117, 20);
		panel_controller.add(lblPath);

		this.textField_dataLogPath = new JTextField();
		this.textField_dataLogPath.setBounds(10, 487, 153, 20);
		this.textField_dataLogPath.setColumns(10);
		panel_controller.add(this.textField_dataLogPath);

		JButton btnBrowse = new JButton("Browse");
		btnBrowse.setBounds(169, 486, 77, 23);

		btnBrowse.addActionListener(new FileSelectionAdapter(this.frmPerfman,
				this.textField_dataLogPath));
		panel_controller.add(btnBrowse);

		JLabel lblOutputPath = new JLabel("Output Path:");
		lblOutputPath.setBounds(10, 507, 104, 20);
		panel_controller.add(lblOutputPath);

		this.textField__jpgFilePath = new JTextField();
		this.textField__jpgFilePath.setBounds(10, 538, 153, 20);
		this.textField__jpgFilePath.setColumns(10);
		panel_controller.add(this.textField__jpgFilePath);

		JButton btnDatajpg = new JButton("Generate JPG");
		btnDatajpg.setBounds(54, 585, 130, 23);
		btnDatajpg.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				File rootPath = new File(PerfMan.this.textField_dataLogPath
						.getText());
				File[] fileList = rootPath.listFiles(new FileFilter() {

					public boolean accept(File pathname) {
						String tmp = pathname.getName().toLowerCase();
						if (tmp.endsWith(".txt") || tmp.endsWith(".log")) {
							return true;
						}
						return false;
					}
				});

				for (File file : fileList) {
					// cpu_12050635.log
					String[] split = file.getName().split("_");
					LineChartBuilder chartBuilder = new LineChartBuilder(file);
					JFreeChart chart = chartBuilder.createChart(split[0]
							+ "Real-Time", split[0], "value");
					chartBuilder.saveAsFile(chart,
							PerfMan.this.textField__jpgFilePath.getText()
									+ File.separator + file.getName() + ".jpg",
							400, 600);
				}
			}
		});
		panel_controller.add(btnDatajpg);

		JButton btnBrowse_1 = new JButton("Browse");
		btnBrowse_1.setBounds(169, 537, 77, 23);
		btnBrowse_1.addActionListener(new FileSelectionAdapter(this.frmPerfman,
				this.textField__jpgFilePath));
		panel_controller.add(btnBrowse_1);

		// 右侧动态数据展示区
		JPanel panel_dataview = new JPanel();
		panel_dataview.setBorder(new MatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY));
		panel_dataview.setBounds(276, 11, 658, 620);
		this.frmPerfman.getContentPane().add(panel_dataview);
		panel_dataview.setLayout(null);

		final MemRealTimeChart rtcMem = new MemRealTimeChart(
				"Memeory Real-time (Mb)", "Memeory", "value");

		rtcMem.setVisible(true);
		rtcMem.setBounds(10, 10, 650, 200);
		panel_dataview.add(rtcMem);

		final CpuRealTimeChart rtcCpu = new CpuRealTimeChart(
				"CPU Real-time (%)", "CPU", "value");

		rtcCpu.setVisible(true);
		rtcCpu.setBounds(10, 210, 650, 200);
		panel_dataview.add(rtcCpu);

		final TrafficRealTimeChart rtcTraffic = new TrafficRealTimeChart(

		"Traffic Real-time (Kb)", "Traffic", "value");

		rtcTraffic.setVisible(true);
		rtcTraffic.setBounds(10, 410, 650, 200);
		panel_dataview.add(rtcTraffic);

		/**
		 * =================================================================
		 * Region: 事件响应
		 * =================================================================
		 */
		btnStart.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				Phone phone = (Phone) comboBox_phone.getSelectedItem();
				String packName = (String) comboBox_process.getSelectedItem();
				rtcMem.setPhone(phone);
				rtcMem.setPackName(packName);
				rtcMem.setRecorder(new TextRecorder(phone, packName, "memory"));
				rtcCpu.setPhone(phone);
				rtcCpu.setPackName(packName);
				rtcCpu.setRecorder(new TextRecorder(phone, packName, "cpu"));
				rtcTraffic.setPhone(phone);
				rtcTraffic.setPackName(packName);
				rtcTraffic.setRecorder(new TextRecorder(phone, packName,
						"traffic"));

				Thread samplerThread = new Thread(rtcMem);
				rtcMem.reset();
				samplerThread.start();

				Thread cpusamplerThread = new Thread(rtcCpu);

				rtcCpu.reset();
				cpusamplerThread.start();

				Thread TrafficsamplerThread = new Thread(rtcTraffic);
				rtcTraffic.reset();
				TrafficsamplerThread.start();

			}
		});

		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rtcMem.stop();
				rtcCpu.stop();
				rtcTraffic.stop();

				StringBuilder builder = new StringBuilder();

				Counter cpuCounter = new Counter(rtcCpu.getRecorder()
						.getLogPath(), rtcCpu.getRecorder().getFileName());
				PerfData cpuData = cpuCounter.getPerfData();

				builder.append("Cpu avarage: ");
				builder.append(cpuData.getAva());
				builder.append("\n");
				builder.append("Cpu Max: ");
				builder.append(cpuData.getMax());
				builder.append("\n");
				builder.append("Cpu Min: ");
				builder.append(cpuData.getMin());
				builder.append("\n");
				Counter memCounter = new Counter(rtcMem.getRecorder()
						.getLogPath(), rtcMem.getRecorder().getFileName());
				PerfData memData = memCounter.getPerfData();

				builder.append("Memory avarage: ");
				builder.append(memData.getAva());
				builder.append("\n");
				builder.append("Memory Max: ");
				builder.append(memData.getMax());
				builder.append("\n");
				builder.append("Memory Min: ");
				builder.append(memData.getMin());
				builder.append("\n");
				Counter trafficCounter = new Counter(rtcTraffic.getRecorder()
						.getLogPath(), rtcTraffic.getRecorder().getFileName());
				PerfData trafficData = trafficCounter.getPerfData();

				builder.append("Traffic : ");
				builder.append(trafficData.getMax() - rtcTraffic.getStartNum());
				builder.append("\n");

				textArea_result.setText(builder.toString());
			}
		});

		/**
		 * ================================================================= End
		 * Region: 事件响应
		 * =================================================================
		 */
	}
}
