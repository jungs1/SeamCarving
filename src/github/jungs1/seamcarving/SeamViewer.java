package github.jungs1.seamcarving;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

public class SeamViewer extends JComponent {

	BufferedImage img;
	
	private Rectangle area;
	private int [][] seams ; // List<int[][]> seams;
	private int[][] auxLines;
	private int color = Color.RED.getRGB();

	private boolean bgRequired = true;

	private BufferedImage seamImg;
	
	public SeamViewer(BufferedImage img) {
		this.img = img;
		this.seamImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		
		// hint!
		this.setPreferredSize(new Dimension(img.getWidth(), img.getHeight()));
	}
	
	
	@Override
	protected void paintComponent(Graphics g) {
		int W = getWidth();
		int H = getHeight();
		
		g.drawImage(img, 0, 0, null);
		g.drawImage(seamImg, 0, 0, null);
		
//		if ( seams != null) {
//			// renderSeams(g);
//			renderSeams2(g);
//		}
		
		
		
		if (auxLines != null) {
			renderAuxLines(g);
		}
		// 666, 1024
//		int y = 622;
//		int x = 999;
//				
//		g.setColor(Color.GREEN.darker());
//		g.drawLine(0, y, 4000, y);
//		g.drawLine(x, 0, x, 4000);
		
	}

	public void drawSeam(int [][] seams, int x, int y, int width, int height, Color color, boolean thickess) {
//		this.seams = seams;
//		this.area = new Rectangle(x, y, width, height);
		renderSeams2(seams, x, y, width, height,color, thickess);
		repaint();
	}	
	
	public void drawAux(int[][] auxLines) {
		this.auxLines = auxLines;
		this.repaint();
	}
	
	private void renderAuxLines(Graphics g) {
		g.setColor(Color.BLUE);
		for (int i = 0; i < auxLines.length; i++) {
			int [] line = auxLines[i]; // { r0, c0, r1, c1};
			g.drawLine(line[1], line[0],line[3], line[2]);
		}
	}

	void renderSeams2(int [][] seams, int x, int y, int width, int height, Color color, boolean thickness) {
		int offsetX = x;
		int offsetY =y;
//		Color color = Color.RED;
//		g.setColor(color);
		Color c = color;
		int rgb = c.getRGB();
		// Graphics g = seamImg.getGraphics(); //
//		seamG.setColor(c);
//		seamG.fillRect(x, y, 10, 10);
		for (int i = 0; i < seams.length; i++) {
			int [] seam = seams[i];
			for(int k = 0 ; k < seam.length - 2; k++) {
				seamImg.setRGB(offsetX + k, offsetY + seam[k] - 1, rgb);
//				if(thickness && offsetY + seam[k] - 1 >= 0) {
//					seamImg.setRGB(offsetX + k, offsetY + seam[k] - 1, rgb);
//				}
				seamImg.setRGB(offsetX + k, offsetY + seam[k], rgb);
//				if (thickness && offsetY + seam[k] + 1 < seamImg.getHeight()) {
//					seamImg.setRGB(offsetX + k, offsetY + seam[k] + 1, rgb);
//				}
//				img.setRGB(offsetX + k, offsetY + seam[k], color);
//				img.setRGB(offsetX + k, offsetY + seam[k], color);
			}
		}
	}
	
	
	void renderSeams(Graphics g) {
		int offsetX = area.x;
		int offsetY = area.y;
		Color c =transp(Color.RED, 0.2);
		int color = c.getRGB();
		for (int i = 0; i < seams.length; i++) {
			int [] seam = seams[i];
			for(int k = 0 ; k < seam.length - 2; k++) {
				// g.drawLine(offsetX + k, offsetY + seam[k], offsetX + k, offsetY + seam[k]);
				img.setRGB(offsetX + k, offsetY + seam[k], color);
				
				// x + 0, x+1, x+2
				
			}
		}
		int black = Color.BLACK.getRGB();
		for(int i = 0 ; i < area.height; i++) {			
			g.setColor(Color.BLACK);
			img.setRGB(offsetX+area.width-2, i, black);
			// g.drawLine(offsetX+area.width-10, 0, offsetX+area.width-10, area.height);
		}
		
		
	}


	private Color transp(Color c, double alpha) {
		int r = c.getRed();
		int g = c.getGreen();
		int b = c.getBlue();
		int a = (int) (c.getAlpha() * alpha);
		return new Color(r, g, b, a);
	}
	
}
