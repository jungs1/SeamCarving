package github.jungs1.seamcarving;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Main {

	public static void main(String[] args) throws IOException {
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		BufferedImage text = ImageIO.read(new File("sample/in01.jpg"));
		SeamViewer viewer = new SeamViewer(text);
		
		f.getContentPane().add(viewer, BorderLayout.CENTER);
		f.setLocationRelativeTo(null);
		
		// 
		int offsetX = 200;
		int offsetY = 0;
		int width = 100;
		int height = text.getHeight();
		
		f.pack();
		f.setVisible(true);
		
		int nPathes = 0;
		ArrayList<int []> lines = new ArrayList<int[]>();
		for(int ox = 0; ox < text.getWidth() ; ox += width) {
			BufferedImage clip = clipImage(text, ox, offsetY, width, height);
			
			SeamCarver sc = new SeamCarver(clip);
			int [][] seams = sc.findSeams(height);
			
			//1. burte forc e method array
			
			/*
			 * [a, ... x]
			 * [b, ... z]
			 * [c, ....m]
			 * 
			 */
			int [][] pathes = reduceSeams(seams, ox);
			for (int i = 0; i < pathes.length; i++) {
				lines.add(pathes[i]); // [ 23, 43, 54, 2, 4332, 434,3..]
			}
			// lines.addAll(Arrays.asList(pathes));
			nPathes += pathes.length;
			viewer.renderSeam(pathes, ox, offsetY, width, height);
			
			// viewer.renderSeam(seams, ox, offsetY, width, height);
			// printSeams(seams);
//			try {
//				Thread.sleep(500);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
			
		}
		System.out.println("# of pathes: " + lines.size());
		int [][] blueLines = connect(lines);
		viewer.renderAuxLines(blueLines);
		
		// renderSeam(text, seams, Color.RED);
		
		
		// show(text);
		
		
		
		// : seams.length
		// seam[i].length : width
	}

	static int[][] connect(ArrayList<int[]> lines) {
		ArrayList<int[]> blueLines = new ArrayList<int[]>();
		for (int i = 0; i < lines.size() - 1; i++) {
			//
			int[] src = lines.get(i); //
			//  0    1   2   3  4 
			// [12, 13, 14, 13, 12, scol, ecol]
			// int sCol = line.length-2;
			
			// tail point of src
			int srcRow = src[src.length-3];
			int srcCol = src.length-1; // tail column index
			int endRow = 0;
			int endCol = 0;
			int min = Integer.MAX_VALUE;
			for(int k = i+1 ; k < lines.size(); k++) {
				int [] dst = lines.get(k);
				// head point of dst
				int dstRow = dst[0];
				int dstCol = dst[dst.length-2];
				int dist = Math.abs(dstRow - srcRow) + Math.abs(dstCol - srcCol);
				if(dist < min) {
					endRow = dstRow;
					endCol = dstCol;
					min = dist;
				}
			}
			
			int [] blue = {srcRow, srcCol, endRow, endCol};
			blueLines.add(blue);
			
		}
		
		int [][] bline = new int[blueLines.size()][];
		return blueLines.toArray(bline);
	}

	static int[][] reduceSeams(int[][] seams, int offset) {
		/*
		 *  [224, 225, 225, 224, 224, 225, 224, col0, col1]
			[224, 225, 225, 224, 224, 225, 224 
			[224, 225, 225, 224, 224, 225, 224 
			
			[261, 262, 263, 262, 261, 261, 260, 259, 259, 258, 257, 257, 256, 255, 254, 253, 252, 251, 250, 249, 248, 247, 246, 246, 246, 247, 247, 246, 246, 246, 246, 245, 244, 245, 244, 245, 245, 245, 244, 245, 246, 247, 246, 245, 246, 245, 245, 245, 245, 244, 245, 244, 243, 243, 242, 241, 240, 239, 238, 237]
			[261, 262, 263, 262, 261, 261, 260, 259, 259, 258, 257, 257, 256, 255, 254, 253, 252, 251, 250, 249, 248, 247, 246, 246, 246, 247, 247, 246, 246, 246, 246, 245, 244, 245, 244, 245, 245, 245, 244, 245, 246, 247, 246, 245, 246, 245, 245, 245, 245, 244, 245, 244, 243, 243, 243, 242, 241, 240, 239, 238]

		 */
		ArrayList<int[]> pathes = new ArrayList<>();
		ArrayList<int[]> cluster = new ArrayList<>();
		int leadIndex = seams[0][0]; // 
		for (int i = 0; i < seams.length; i++) {
			if(seams[i][0] == leadIndex) {
				// cluster.add(seams[i]);
				// System.out.println(cluster.toString());
			} else {
				/*
				 * cluster -> 
				 */
				int[] path = findCommonAncestor(cluster);
				path[path.length-2] = offset; // start column index
				path[path.length-1] = offset + path.length - 1; //   end column index
				pathes.add(path);
				cluster.clear();
				leadIndex = seams[i][0];
			}
			cluster.add(seams[i]);
		}
		
		int [][] p = new int[pathes.size()][];
		pathes.toArray(p);
		return p;
	}

	private static int[] findCommonAncestor(ArrayList<int[]> cluster) {
		// TODO Auto-generated method stub
		/*
		 *          col   i
		 *      [33, 34, 45 ]
		 *   i  [33, 34, 47]
		 *      [33, 34, 47.]
		 */
		int width = cluster.get(0).length;
		int col = 0;
		// TODO brute force ! -> binary search
		outer: for (int i = 0; i < width; i++) {
			int compare = cluster.get(0)[i];
			for (int k = 0; k < cluster.size(); k++) {
				if(cluster.get(k)[i] == compare) {
					col = i;
				} else {
					break outer;
				}
			}
		}
		
		
		return Arrays.copyOf(cluster.get(cluster.size() / 2), col+1 + 2);
	}

	static void printSeams(int[][] seams) {
		for (int i = 0; i < seams.length; i++) {
			System.out.println(Arrays.toString(seams[i]));
		}
		
	}

	static BufferedImage clipImage(BufferedImage text, int offsetX, int offsetY, int W, int H) {
		// 200 x 180
		int width = Math.min(W, text.getWidth() - offsetX);
		int height = Math.min(H, text.getHeight() - offsetY);
		
		BufferedImage clip = new BufferedImage(width, height, text.getType());
		
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height ; y++) {
				int rgb = text.getRGB(offsetX + x, offsetY + y);
				clip.setRGB(x, y, rgb);
			}
		}
		return clip;
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
