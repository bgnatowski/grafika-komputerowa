package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class DDALineDrawingApp extends JFrame {
	private DDALineDrawingPanel lineDrawingPanel;
	private JTextField startXField, startYField, endXField, endYField;

	public DDALineDrawingApp() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800, 800);

		lineDrawingPanel = new DDALineDrawingPanel(50,50,250, 350);

		JPanel controlPanel = new JPanel();
		controlPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

		addZoomControl(controlPanel);
		addLineWidthControl(controlPanel);
		addAntialiasingControl(controlPanel);
		addColorPickerControl(controlPanel);
		addCoordinatesInput(controlPanel);

		add(controlPanel, BorderLayout.SOUTH);
		add(lineDrawingPanel, BorderLayout.CENTER);

		// Dodaj obsługę scrolla myszy
		lineDrawingPanel.addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				if (e.getWheelRotation() < 0) {
					// Przybliżanie
					lineDrawingPanel.zoomIn();
				} else {
					// Oddalanie
					lineDrawingPanel.zoomOut();
				}
			}
		});

		setVisible(true);
	}

	private void addZoomControl(JPanel controlPanel) {
		JSlider zoomSlider = new JSlider(JSlider.HORIZONTAL, 1, 500, 1); // Zmniejszenie zakresu zoomu
		zoomSlider.addChangeListener(e -> {
			int zoomLevel = zoomSlider.getValue();
			double zoomFactor = zoomLevel / 100.0; // Dostosowanie skali
			lineDrawingPanel.setZoom(zoomFactor);
		});
		controlPanel.add(new JLabel("Zoom:"));
		controlPanel.add(zoomSlider);
	}

	private void addLineWidthControl(JPanel controlPanel) {
		JSlider lineWidthSlider = new JSlider(JSlider.HORIZONTAL, 1, 10, 1);
		lineWidthSlider.addChangeListener(e -> {
			int newLineWidth = lineWidthSlider.getValue();
			lineDrawingPanel.setLineWidth(newLineWidth);
		});
		controlPanel.add(new JLabel("Line Width:"));
		controlPanel.add(lineWidthSlider);
	}

	private void addAntialiasingControl(JPanel controlPanel) {
		JCheckBox antialiasingCheckbox = new JCheckBox("Antialiasing");
		antialiasingCheckbox.addItemListener(e -> {
			boolean isAntialiasingEnabled = antialiasingCheckbox.isSelected();
			lineDrawingPanel.setAntialiasing(isAntialiasingEnabled);
		});
		controlPanel.add(antialiasingCheckbox);
	}

	private void addColorPickerControl(JPanel controlPanel) {
		JButton colorButton = new JButton("Choose Line Color");
		colorButton.addActionListener(e -> {
			Color newColor = JColorChooser.showDialog(this, "Choose Line Color", lineDrawingPanel.getLineColor());
			lineDrawingPanel.setLineColor(newColor);
		});
		controlPanel.add(colorButton);
	}

	private void addCoordinatesInput(JPanel controlPanel) {
		startXField = new JTextField(4);
		startYField = new JTextField(4);
		endXField = new JTextField(4);
		endYField = new JTextField(4);

		JButton drawButton = new JButton("Rysuj");
		drawButton.addActionListener(e -> {
			try {
				int startX = Integer.parseInt(startXField.getText());
				int startY = Integer.parseInt(startYField.getText());
				int endX = Integer.parseInt(endXField.getText());
				int endY = Integer.parseInt(endYField.getText());
				lineDrawingPanel.setLineCoordinates(startX, startY, endX, endY);
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(this, "Nieprawidłowe współrzędne odcinka.");
			}
		});

		controlPanel.add(new JLabel("Początek X:"));
		controlPanel.add(startXField);
		controlPanel.add(new JLabel("Początek Y:"));
		controlPanel.add(startYField);
		controlPanel.add(new JLabel("Koniec X:"));
		controlPanel.add(endXField);
		controlPanel.add(new JLabel("Koniec Y:"));
		controlPanel.add(endYField);
		controlPanel.add(drawButton);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			new DDALineDrawingApp();
		});
	}
}
