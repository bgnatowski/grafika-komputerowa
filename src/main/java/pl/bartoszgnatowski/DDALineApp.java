package pl.bartoszgnatowski;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DDALineApp extends JFrame {
	private DDAPainter ddaPainter;
	private JTextField startXField, startYField, endXField, endYField;
	public DDALineApp() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(600, 600);

		ddaPainter = new DDAPainter(50, 50, 200, 200);

		JPanel controlPanel = new JPanel();
		controlPanel.setLayout(new GridLayout(2, 1));

		JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT));

		addCoordinatesInput(row2);
		addLineWidthControl(row1);
		addAntialiasingControl(row1);
		addColorPickerControl(row1);

		controlPanel.add(row1);
		controlPanel.add(row2);
		ddaPainter.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				double zoom = ddaPainter.getZoom();
				if (e.getButton() == MouseEvent.BUTTON1) {
					zoom *= 1.1;
					ddaPainter.setZoom(zoom);
				} else if (e.getButton() == MouseEvent.BUTTON3) {
					zoom /= 1.1;
					ddaPainter.setZoom(zoom);
				}
			}
		});

		add(controlPanel, BorderLayout.NORTH);
		add(ddaPainter, BorderLayout.CENTER);

		setVisible(true);
	}

	private void addLineWidthControl(JPanel controlPanel) {
		JSlider lineWidthSlider = new JSlider(JSlider.HORIZONTAL, 1, 10, 1);
		lineWidthSlider.addChangeListener(e -> {
			int newLineWidth = lineWidthSlider.getValue();
			ddaPainter.setLineWidth(newLineWidth);
		});
		controlPanel.add(new JLabel("Grubość linii:"));
		controlPanel.add(lineWidthSlider);
	}

	private void addAntialiasingControl(JPanel controlPanel) {
		JCheckBox antialiasingCheckbox = new JCheckBox("Antyaliasing");
		antialiasingCheckbox.addItemListener(e -> {
			boolean isAntialiasingEnabled = antialiasingCheckbox.isSelected();
			ddaPainter.setAntialiasing(isAntialiasingEnabled);
		});
		controlPanel.add(antialiasingCheckbox);
	}

	private void addColorPickerControl(JPanel controlPanel) {
		JButton colorButton = new JButton("Kolor");
		colorButton.addActionListener(e -> {
			Color newColor = JColorChooser.showDialog(this, "Wybór koloru", ddaPainter.getLineColor());
			ddaPainter.setLineColor(newColor);
		});
		controlPanel.add(colorButton);
	}

	private void addCoordinatesInput(JPanel controlPanel) {
		startXField = new JTextField("50", 4);
		startYField = new JTextField("50", 4);
		endXField = new JTextField("200", 4);
		endYField = new JTextField("200", 4);

		JButton drawButton = getDrawButton();

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

	private JButton getDrawButton() {
		JButton drawButton = new JButton("Rysuj");
		drawButton.addActionListener(e -> {
			try {
				int startX = Integer.parseInt(startXField.getText());
				int startY = Integer.parseInt(startYField.getText());
				int endX = Integer.parseInt(endXField.getText());
				int endY = Integer.parseInt(endYField.getText());

				if (isValidCoordinates(startX, startY) && isValidCoordinates(endX, endY)) {
					ddaPainter.setLineCoordinates(startX, startY, endX, endY);
				} else {
					JOptionPane.showMessageDialog(this, "Nieprawidłowe współrzędne odcinka.");
				}
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(this, "Nieprawidłowe współrzędne odcinka.");
			}
		});
		return drawButton;
	}

	private boolean isValidCoordinates(int x, int y) {
		return x >= 0 && x <= getWidth() && y >= 0 && y <= getHeight();
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(DDALineApp::new);
	}
}
