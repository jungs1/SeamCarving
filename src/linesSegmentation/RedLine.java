package linesSegmentation;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;

public class RedLine {

	static BufferedImage clipImage(BufferedImage text, int offsetX, int offsetY, int W, int H) {
		int width = Math.min(W, text.getWidth() - offsetX);
		int height = Math.min(H, text.getHeight() - offsetY);

		BufferedImage clip = new BufferedImage(width, height, text.getType());

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int rgb = text.getRGB(offsetX + x, offsetY + y);
				clip.setRGB(x, y, rgb);
			}
		}
		return clip;
	}

	static int[][] reduceSeams(int[][] seams, int offset) {
		/*
		 * [224, 225, 225, 224, 224, 225, 224, col0, col1] 
		 * [224, 225, 225, 224, 224, 225, 224 
		 * [224, 225, 225, 224, 224, 225, 224
		 *  
		 */
		// reduced seam paths
		ArrayList<int[]> paths = new ArrayList<>();
		// seam that starts with the same index
		ArrayList<int[]> cluster = new ArrayList<>();
		int leadIndex = seams[0][0]; //
		for (int i = 0; i < seams.length; i++) {
			if (seams[i][0] == leadIndex) {
			} else {
				int[] path = findCommonAncestor(cluster);
				path[path.length - 2] = offset; // start column index
				path[path.length - 1] = offset + path.length - 1; // end column index
				paths.add(path);
				cluster.clear();
				leadIndex = seams[i][0];
			}
			cluster.add(seams[i]);
		}

		if (cluster.size() > 0) {
			int[] path = findCommonAncestor(cluster);
			path[path.length - 2] = offset; // start column index
			path[path.length - 1] = offset + path.length - 1; // end column index
			paths.add(path);
		}

		int[][] p = new int[paths.size()][];
		paths.toArray(p);
		return p;
	}

	static int[] findCommonAncestor(ArrayList<int[]> cluster) {

		int width = cluster.get(0).length;
		int col = 0;
		// TODO brute force ! -> binary search
		outer: for (int i = 0; i < width; i++) {
			int compare = cluster.get(0)[i];
			for (int k = 0; k < cluster.size(); k++) {
				if (cluster.get(k)[i] == compare) {
					col = i;
				} else {
					break outer;
				}
			}
		}
		return Arrays.copyOf(cluster.get(cluster.size() / 2), col + 1 + 2);
	}
}
