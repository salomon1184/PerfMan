package com.salomon.perfman.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class ConsolePane extends JScrollPane {

	private JTextPane textPane = new JTextPane();

	private static ConsolePane console = null;

	public static synchronized ConsolePane getInstance() {
		if (console == null) {
			console = new ConsolePane();
		}
		return console;
	}

	private ConsolePane() {

		this.setViewportView(this.textPane);

		this.textPane.setEditable(false);
		this.setPreferredSize(new Dimension(640, 120));

		// Create reader threads
		this.redirectSystemStreams();
	}

	private void updateTextPane(final String text) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Document doc = ConsolePane.this.textPane.getDocument();

				try {

					doc.insertString(doc.getLength(), text, null);

				} catch (BadLocationException e) {
					throw new RuntimeException(e);
				}
				ConsolePane.this.textPane.setCaretPosition(doc.getLength() - 1);
			}
		});
	}

	private void updateErrorTextPane(final String text) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {

				StyledDocument doc = (StyledDocument) ConsolePane.this.textPane
						.getDocument();
				try {
					Style style = doc.addStyle("StyleName", null);

					Color foreground = Color.RED;
					// Foreground color
					StyleConstants.setForeground(style, foreground);
					doc.insertString(doc.getLength(), text, style);
				} catch (BadLocationException e) {
					throw new RuntimeException(e);
				}
				ConsolePane.this.textPane.setCaretPosition(doc.getLength() - 1);
			}
		});
	}

	private void redirectSystemStreams() {
		OutputStream out = new OutputStream() {

			@Override
			public void write(final int b) throws IOException {
				ConsolePane.this.updateTextPane(String.valueOf((char) b));
			}

			@Override
			public void write(byte[] b, int off, int len) throws IOException {
				ConsolePane.this.updateTextPane(new String(b, off, len));
			}

			@Override
			public void write(byte[] b) throws IOException {
				this.write(b, 0, b.length);
			}
		};

		OutputStream errorOut = new OutputStream() {

			@Override
			public void write(final int b) throws IOException {
				ConsolePane.this.updateErrorTextPane(String.valueOf((char) b));
			}

			@Override
			public void write(byte[] b, int off, int len) throws IOException {
				ConsolePane.this.updateErrorTextPane(new String(b, off, len));
			}

			@Override
			public void write(byte[] b) throws IOException {
				this.write(b, 0, b.length);
			}
		};
		System.setOut(new PrintStream(out, true));
		System.setErr(new PrintStream(errorOut, true));
	}
}
