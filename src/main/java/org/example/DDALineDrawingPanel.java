package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

class DDALineDrawingPanel extends JPanel {
	private int x1, y1, x2, y2;
	private double zoom = 1.0;
	private int lineWidth = 1;
	private boolean antialiasing = false;
	private Color lineColor = Color.BLACK;

	private AffineTransform transform = new AffineTransform();

	public DDALineDrawingPanel(int x1, int y1, int x2, int y2) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}
	public void zoomIn() {
		zoom *= 1.1; // Przybliż o 10%
		transform.setToScale(zoom, zoom);
		repaint();
	}

	public void zoomOut() {
		zoom /= 1.1; // Oddal o 10%
		transform.setToScale(zoom, zoom);
		repaint();
	}

	public void setZoom(double zoom) {
		this.zoom = zoom;
		repaint();
	}

	public void setLineWidth(int lineWidth) {
		this.lineWidth = lineWidth;
		repaint();
	}

	public void setAntialiasing(boolean antialiasing) {
		this.antialiasing = antialiasing;
		repaint();
	}

	public void setLineColor(Color lineColor) {
		this.lineColor = lineColor;
		repaint();
	}

	public Color getLineColor() {
		return lineColor;
	}

	public void moveLine(int deltaX, int deltaY) {
		x1 += deltaX;
		x2 += deltaX;
		y1 += deltaY;
		y2 += deltaY;
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, antialiasing ? RenderingHints.VALUE_ANTIALIAS_ON : RenderingHints.VALUE_ANTIALIAS_OFF);
		g2d.setColor(lineColor);
		g2d.setStroke(new BasicStroke(lineWidth));

		int scaledX1 = (int) (x1 * zoom);
		int scaledY1 = (int) (y1 * zoom);
		int scaledX2 = (int) (x2 * zoom);
		int scaledY2 = (int) (y2 * zoom);

		drawDDALine(g2d, scaledX1, scaledY1, scaledX2, scaledY2);
	}

	public void setLineCoordinates(int startX, int startY, int endX, int endY) {
		x1 = startX;
		y1 = startY;
		x2 = endX;
		y2 = endY;
		repaint();
	}

	public void drawDDALine(Graphics2D g2d, int x1, int y1, int x2, int y2) {
		int dx = x2 - x1;
		int dy = y2 - y1;
		int steps = Math.max(Math.abs(dx), Math.abs(dy));
		double xIncrement = (double) dx / steps;
		double yIncrement = (double) dy / steps;
		double x = x1;
		double y = y1;

		for (int i = 0; i <= steps; i++) {
			int xPixel = (int) Math.round(x);
			int yPixel = (int) Math.round(y);
			g2d.drawLine(xPixel, yPixel, xPixel, yPixel);
			x += xIncrement;
			y += yIncrement;
		}
	}
}