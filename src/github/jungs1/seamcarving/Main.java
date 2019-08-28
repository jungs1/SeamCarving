package github.jungs1.seamcarving;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import github.jungs1.seamcarving.energy.AdjFourFunction;
import github.jungs1.seamcarving.energy.VerticalOriented;

public class Main {

	public static void main(String[] args) throws IOException {
		JFrame f = new JFrame();

		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		BufferedImage text = ImageIO.read(new File("sample/in01.jpg"));
		
//		BlurImage blur = new BlurImage();
//		BufferedImage blurImage = blur.blurImage(text, 19);
//		SeamViewer viewer = new SeamViewer(blurImage);
		SeamViewer viewer = new SeamViewer(text);

		f.getContentPane().add(viewer, BorderLayout.CENTER);
		f.setLocationRelativeTo(null);

		/**
		 * offsetX: x pixels away from left boarder of the image offsetY: y pixels away
		 * from top boarder of the image width: width of the divided image
		 */
		// int offsetX = 200;
		int offsetY = 0;
		int width = text.getWidth()/10; // 5%
		int height = text.getHeight();

		f.pack();
		f.setVisible(true);

		// red lines[...offset,offset+len-1]
		ArrayList<int[]> lines = new ArrayList<int[]>();
		Color seamTreeColor = transp(Color.MAGENTA, 0.0);
		
		for (int x = 0; x < text.getWidth(); x += width) {
			//			BufferedImage clip = clipImage(text, x, offsetY, width, height);
			BufferedImage clip = RedLine.clipImage(text, x, offsetY, width, height);
			// MaxSeamCarver sc = new MaxSeamCarver(clip);
			SeamCarver sc = new SeamCarver(clip, new AdjFourFunction());
			int[][] seams = sc.findSeams(height);
			System.out.println("seam lenth: " + seams.length);
			/*
			 * 1. Brute Force Method using array
			 * reduceSeams removes the weird tree edges 
			 */
			//			int[][] paths = reduceSeams(seams, x);
			// int[][] paths = RedLine.reduceSeams(seams, x);

//			for (int i = 0; i < paths.length; i++) {
//				lines.add(paths[i]); // [ 23, 43, 54, 2, 4332, 434,3..]
//			}
			// lines.addAll(Arrays.asList(pathes));
			
			
			viewer.drawSeam(seams,x, offsetY, width, height, seamTreeColor, false);
			int[][] paths = RedLine.reduceSeams(seams, x);
			System.out.println("common length : " + paths.length);
			
			viewer.drawSeam(paths, x, offsetY, width, height, Color.RED, false);

			// viewer.renderSeam(seams, x, offsetY, width, height);
			// printSeams(seams);
			//			try {
			//				Thread.sleep(500);
			//			} catch (InterruptedException e) {
			//				e.printStackTrace();
			//			}

		}
//		int[][] blueLines = connect(lines);
//		viewer.drawAux(blueLines);

		// renderSeam(text, seams, Color.RED);

		// show(text);

		// : seams.length
		// seam[i].length : width
	}
	
	static Color transp(Color c, double alpha) {
		int r = c.getRed();
		int g = c.getGreen();
		int b = c.getBlue();
		int a = (int) (c.getAlpha() * alpha);
		return new Color(r, g, b, a);
	}

	static int[][] connect(ArrayList<int[]> lines) {
		ArrayList<int[]> blueLines = new ArrayList<int[]>();
		for (int i = 0; i < lines.size() -1; i++) {
			//
			int[] src = lines.get(i); //
			//   0   1   2   3   4
			// [12, 13, 14, 13, 12, scol, ecol]
			// int sCol = line.length-2;
			/**
			 *          width(col)
			 *     +-----------------> X
			 *     |
			 * h   |
			 * row |             (srcRow, srcCOL) =  (3,7), row:3, col:7
			 *     |
			 *     v
			 *     Y
			 */
			// tail point of src
			int srcRow = src[src.length - 3];
			int srcCol = src[src.length - 1]; // tail column index
			// int srcCol = src.length - 1; 
			int endRow = 0;
			int endCol = 0;
			
			int min = Integer.MAX_VALUE;
			for (int k = i + 1; k < lines.size(); k++) {
				int[] dst = lines.get(k);
				// head point of dst
				int dstRow = dst[0];
				int dstCol = dst[dst.length - 2];
				if (dstCol <= srcCol) {
					continue;
				}
				int dist = Math.abs(dstRow - srcRow) + Math.abs(dstCol - srcCol);
				double slope = Math.abs(1.0*(srcRow -dstRow) / (srcCol - dstCol));
				
//				if ( slope > 5 ) {
//					System.out.printf("(%d, %d) -> (%d, %d) %.3f, d: %d\n", srcRow, srcCol, dstRow, dstCol, slope, dist);
//				}
				if (dist < min && slope < 0.5) {
					endRow = dstRow;
					endCol = dstCol;
					min = dist;
					// System.out.printf("* new (%d, %d) -> (%d, %d) %.3f, d: %d\n", srcRow, srcCol, dstRow, dstCol, slope, dist);
				}
			}

			if ( endRow > 0 && endCol > 0) {
				int[] blue = { srcRow, srcCol, endRow, endCol };
				blueLines.add(blue);				
			}

		}
		int[][] bline = new int[blueLines.size()][];
		return blueLines.toArray(bline);
	}

	//	static void show(BufferedImage text) {
	//		JFrame f = new JFrame();
	//		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	//
	//		ImageIcon icon = new ImageIcon(text);
	//		f.getContentPane().add(new JLabel(icon), BorderLayout.CENTER);
	//		f.pack();
	//		f.setVisible(true);
	//
	//	}

	//	static void saveTo(BufferedImage img, String fname) {
	//
	//	}

	//	static void renderSeam(BufferedImage text, int[][] seams, Color red) {
	//		/*
	//		 * width == col == x +-----------------------> X | | | V Y
	//		 */
	//		int width = text.getWidth();
	//		int height = text.getHeight();
	//		for (int i = 0; i < seams.length; i++) {
	//			int[] seam = seams[i];
	//			for (int k = 0; k < width; k++) {
	//				text.setRGB(k, seam[k], 0xFF0000);
	//			}
	//		}
	//	}
}
