package linesSegmentation;

import java.util.ArrayList;

public class BlueLine {

	static int[][] connect(ArrayList<int[]> lines) {
		ArrayList<int[]> blueLines = new ArrayList<int[]>();
		for (int i = 0; i < lines.size() - 1; i++) {
			//
			int[] src = lines.get(i);
			// [12, 13, 14, 13, 12, scol, ecol]
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
			int srcRow = src[src.length - 3];
			int srcCol = src[src.length - 1];
			int endRow = 0;
			int endCol = 0;

			int min = Integer.MAX_VALUE;
			for (int k = i + 1; k < lines.size(); k++) {
				int[] dst = lines.get(k);
				int dstRow = dst[0];
				int dstCol = dst[dst.length - 2];
				if (dstCol <= srcCol) {
					continue;
				}
				int dist = Math.abs(dstRow - srcRow) + Math.abs(dstCol - srcCol);
				double slope = Math.abs(1.0 * (srcRow - dstRow) / (srcCol - dstCol));

				if (dist < min && slope <= 0.2) {
					endRow = dstRow;
					endCol = dstCol;
					min = dist;
				}
			}

			if (endRow > 0 && endCol > 0) {
				int[] blue = { srcRow, srcCol, endRow, endCol };
				blueLines.add(blue);
			}

		}
		int[][] bline = new int[blueLines.size()][];
		return blueLines.toArray(bline);
	}

}
