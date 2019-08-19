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
	private int [][] seams ;
	private int[][] auxLines;
	private int color = Color.RED.getRGB();

	
	public SeamViewer(BufferedImage img) {
		this.img = img;
		
		// hint!
		this.setPreferredSize(new Dimension(img.getWidth(), img.getHeight()));
	}
	
	
	@Override
	protected void paintComponent(Graphics g) {
		int W = getWidth();
		int H = getHeight();
		
		if ( seams != null) {
			renderSeams(g);
		}
		
		
		g.drawImage(img, 0, 0, null);
		
		if (auxLines != null) {
			renderAuxLines(g);
		}
		// 666, 1024
		int y = 622;
		int x = 999;
				
		g.setColor(Color.GREEN.darker());
		g.drawLine(0, y, 4000, y);
		g.drawLine(x, 0, x, 4000);
		
	}

	public void drawSeam(int [][] seams, int x, int y, int width, int height) {
		this.seams = seams;
		this.area = new Rectangle(x, y, width, height);
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

	void renderSeams(Graphics g) {
		int offsetX = area.x;
		int offsetY = area.y;
		for (int i = 0; i < seams.length; i++) {
			int [] seam = seams[i];
			for(int k = 0 ; k < seam.length - 2; k++) {
				// g.drawLine(offsetX + k, offsetY + seam[k], offsetX + k, offsetY + seam[k]);
				img.setRGB(offsetX + k, offsetY + seam[k], color);
				// x + 0, x+1, x+2
				
			}
		}
		
	}
	
}
