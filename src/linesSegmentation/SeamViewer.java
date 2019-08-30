package linesSegmentation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import energyFunction.AdjFourFunction;

public class SeamViewer extends JComponent {

	// private SeamViewConfig config;
	// rendering configuration
	// 
	private boolean showBackground;
	
	private boolean showBlur;

	private BufferedImage originImage;
	private BufferedImage bgImage;
	private BufferedImage seamImg;

	private Rectangle area;
	private int[][] seams; // List<int[][]> seams;
	private int[][] auxLines;
	private int color = Color.RED.getRGB();

//	private boolean bgRequired = true;

	public SeamViewer() {
		this(500, 500);
	}

	public SeamViewer(int w, int h) {
		showBackground = true;
		setPreferredSize(new Dimension(w, h));
		setBackground(Color.WHITE);
		this.setOpaque(true);
	}

	public void setBlurImage(Boolean showBlur) {
		this.showBlur = showBlur;
		this.seamImg = new BufferedImage(bgImage.getWidth(), bgImage.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		if (showBlur) {
			BlurImage newImage = new BlurImage();
			BufferedImage  img = newImage.blurImage(this.bgImage, 3);
			this.bgImage = img;
			renderSeam(this.bgImage);
		} else {
			this.bgImage = this.originImage;
			
			renderSeam(this.bgImage);
		}
		repaint();
	}
	

	private void setTextImage(BufferedImage text) {
		if (text == null) {
			return;
		}
		this.originImage = text;
		this.bgImage = text;
				
		this.seamImg = new BufferedImage(bgImage.getWidth(), bgImage.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		this.setPreferredSize(new Dimension(bgImage.getWidth(), bgImage.getHeight()));
	}

	public void setSeamImageOnly(Boolean showBG) {
		this.showBackground = showBG;
		this.repaint();
		/*
		if (text == null) {
			return;
		}
		this.img = null;
		this.seamImg = new BufferedImage(text.getWidth(), text.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		this.setPreferredSize(new Dimension(text.getWidth(), text.getHeight()));
		*/
	}

	@Override
	protected void paintComponent(Graphics g) {
		if (showBackground && bgImage != null) {
			g.drawImage(bgImage, 0, 0, null);
		}
		
		if (showBlur) {
			;
		}
		if (seamImg != null) {
			g.drawImage(seamImg, 0, 0, null);
//			if (auxLines != null) {
//				renderAux(g);
//			}
		}

		if (bgImage == null && seamImg == null) {
			welcomeScreen(g);
		}
		/*
		if (img != null) {
			g.drawImage(seamImg, 0, 0, null);
		} else if (img == null && seamImg != null) {
		} else {
		}
		if (auxLines != null) {
			renderAux(g);
		}
		*/

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

//	public void drawSeam(int[][] seams, int x, int y, int width, int height, Color color) {
//		renderSeam(seams, x, y, width, height, color);
//		// repaint();
//	}

	void drawSeam(int[][] seams, int x, int y, int width, int height, Color color) {
		int offsetX = x;
		int offsetY = y;
		Color c = color;
		int rgb = c.getRGB();
		for (int i = 0; i < seams.length; i++) {
			int[] seam = seams[i];
			for (int k = 0; k < seam.length - 2; k++) {
				seamImg.setRGB(offsetX + k, offsetY + seam[k], rgb);
			}
		}
	}

	public void drawAux(int[][] auxLines) {
		this.auxLines = auxLines;
	}

	private void renderAux(Graphics g) {
		g.setColor(Color.RED);
		for (int i = 0; i < auxLines.length; i++) {
			int[] line = auxLines[i]; // { r0, c0, r1, c1};
			g.drawLine(line[1], line[0], line[3], line[2]);
		}
	}
	
	public void updateText(BufferedImage text) {
		setTextImage(text);
		renderSeam(this.bgImage);
	}
	private void renderSeam(BufferedImage text) {
		//renderSeam(text);
		
		int offsetY = 0;
		int width = text.getWidth() / 10; // 5%
		int height = text.getHeight();
		ArrayList<int[]> lines = new ArrayList<int[]>();

		Color seamTreeColor = transp(Color.MAGENTA, 0.0);

		for (int x = 0; x < text.getWidth(); x += width) {
			BufferedImage clip = RedLine.clipImage(text, x, offsetY, width, height);
			SeamCarver sc = new SeamCarver(clip, new AdjFourFunction());

			int[][] seams = sc.findSeams(height);
			this.drawSeam(seams, x, offsetY, width, height, seamTreeColor);

			int[][] paths = RedLine.reduceSeams(seams, x);
			this.drawSeam(paths, x, offsetY, width, height, Color.RED);
			// change int[][] paths to ArrayList<int[][]>
			for (int i = 0; i < paths.length; i++) {
				lines.add(paths[i]);
			}
			int[][] blueLine = BlueLine.connect(lines);
			this.drawAux(blueLine);

		}
		
		List<Double> slopes = new ArrayList<>();
		double sum = 0;
		for(int [] p : lines) {
			int x1 = p[0];
			int y1 = p[p.length-2];
			int x2 = p[p.length-3];
			int y2 = p[p.length-1];
			if (x2 - x1 == 0) {
				System.out.println("stophere");
			}
			double slope= 1.0*(x2-x1)/(y2-y1);
			sum += slope;
			slopes.add(slope);
		}
		
		System.out.printf("avg sloes: %.5f\n", sum/slopes.size());
	
		this.repaint();
	}
	
	static Color transp(Color c, double alpha) {
		int r = c.getRed();
		int g = c.getGreen();
		int b = c.getBlue();
		int a = (int) (c.getAlpha() * alpha);
		return new Color(r, g, b, a);
	}
}
