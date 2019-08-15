package github.jungs1.linesegment;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Main {

	public static void main(String[] args) throws IOException {
		BufferedImage text = ImageIO.read(new File("sample/in02.jpg"));
		
		SeamCarver sc = new SeamCarver(text);
		int [][] seams = sc.findSeams(300);
		
		renderSeam(text, seams, Color.RED);
		
		
		show(text);
		
		// : seams.length
		// seam[i].length : width
	}

	static void show(BufferedImage text) {
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		ImageIcon icon = new ImageIcon(text);
		f.getContentPane().add(new JLabel(icon), BorderLayout.CENTER);
		f.pack();
		f.setVisible(true);
		
	}

	static void saveTo(BufferedImage img, String fname) {
		
	}

	static void renderSeam(BufferedImage text, int[][] seams, Color red) {
		/*
		 *           width == col == x
		 *    +-----------------------> X
		 *    |
		 *    |
		 *    |
		 *    V
		 *    Y
		 */
		int width = text.getWidth();
		int height = text.getHeight();
		for (int i = 0; i < seams.length; i++) {
			int[] seam = seams[i];
			for (int k = 0; k < width; k++) {
				text.setRGB(k, seam[k], 0xFF0000);
			}
		}
	}
}
