package github.jungs1.seamcarving;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

public class SeamViewer extends JComponent {

	private BufferedImage img;
	private BufferedImage seamImg;

	private Rectangle area;
	private int[][] seams; // List<int[][]> seams;
	private int[][] auxLines;
	private int color = Color.RED.getRGB();

	private boolean bgRequired = true;

	public SeamViewer() {
		setPreferredSize(new Dimension(500, 500));
	}

	public void setTextImage(BufferedImage text) {
		if (text == null) {
			return;
		}
		this.img = text;
		this.seamImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		this.setPreferredSize(new Dimension(img.getWidth(), img.getHeight()));
	}

	@Override
	protected void paintComponent(Graphics g) {
		if (img != null) {
			g.drawImage(img, 0, 0, null);
			g.drawImage(seamImg, 0, 0, null);
		} else {
			welcomeScreen(g);
		}
		if (auxLines != null) {
			renderAux(g);
		}

	}

	void welcomeScreen(Graphics g) {
		int W = getWidth();
		int H = getHeight();

		String msg = "open image on the menu";
		g.setFont(new Font("Courier New", Font.BOLD, 28));
		FontMetrics fm = g.getFontMetrics();
		int fWidth = fm.stringWidth(msg);
		int fHeight = fm.getHeight();
		g.drawString(msg, (W - fWidth) / 2, (H - fHeight) / 2);
	}

	public void drawSeam(int[][] seams, int x, int y, int width, int height, Color color) {
		renderSeam(seams, x, y, width, height, color);
		repaint();
	}

	void renderSeam(int[][] seams, int x, int y, int width, int height, Color color) {
		int offsetX = x;
		int offsetY = y;
		Color c = color;
		int rgb = c.getRGB();
		for (int i = 0; i < seams.length; i++) {
			int[] seam = seams[i];
			for (int k = 0; k < seam.length - 2; k++) {
				seamImg.setRGB(offsetX + k, offsetY + seam[k] - 1, rgb);

				seamImg.setRGB(offsetX + k, offsetY + seam[k], rgb);

			}
		}
	}

	public void drawAux(int[][] auxLines) {
		this.auxLines = auxLines;
		this.repaint();
	}

	private void renderAux(Graphics g) {
		g.setColor(Color.BLUE);
		for (int i = 0; i < auxLines.length; i++) {
			int[] line = auxLines[i]; // { r0, c0, r1, c1};
			g.drawLine(line[1], line[0], line[3], line[2]);
		}
	}

}
