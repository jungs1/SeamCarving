package linesSegmentation;

import java.awt.image.BufferedImage;
import java.util.Stack;

public class MaxSeamCarver {

	private BufferedImage picture;
	private int[][] rgb;
	private double[][] energies;
	int row, col;

	public MaxSeamCarver(BufferedImage input) {
		this.picture = input;
		this.row = input.getHeight();
		this.col = input.getWidth();

		this.rgb = new int[row][col];
		this.energies = new double[row][col];

		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				this.rgb[i][j] = input.getRGB(j, i);
			}
		}

		calcEnergy();
	}

	private void calcEnergy() {
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				if (i == 0 || j == 0 || i == row - 1 || j == col - 1) {
					energies[i][j] = 0;
				} else {
					energies[i][j] = this.calcEnergyHelper(rgb, i, j);
				}
			}
		}
	}

	private double calcEnergyHelper(int[][] rgb, int row, int col) {
		int down = rgb[row - 1][col];
		int top = rgb[row + 1][col];
		int left = rgb[row][col - 1];
		int right = rgb[row][col + 1];

		double result = this.energyFunction(left, right) + this.energyFunction(top, down);
		return Math.sqrt(result);
	}

	private double energyFunction(int rgb1, int rgb2) {
		int r1 = (rgb1 >> 16) & 0xFF;
		int g1 = (rgb1 >> 8) & 0xFF;
		int b1 = (rgb1 >> 0) & 0xFF;
		int r2 = (rgb2 >> 16) & 0xFF;
		int g2 = (rgb2 >> 8) & 0xFF;
		int b2 = (rgb2 >> 0) & 0xFF;
		return Math.pow(r1 - r2, 2) + Math.pow(g1 - g2, 2) + Math.pow(b1 - b2, 2);
	}

	public int[][] findSeams(int size) {
		double[][] tp = new double[col][row];
		for (int ir = 0; ir < row; ir++) {
			for (int ic = 0; ic < col; ic++) {
				tp[ic][ir] = energies[ir][ic];
			}
		}
		int[][] seams = seam(tp, col, row, size);
		return seams;

	}

	private int[][] seam(double[][] energies, int row, int col, int K) {
		int[][] path = new int[row][col];
		double[][] energyCopy = new double[row][col];

		System.arraycopy(energies[0], 0, energyCopy[0], 0, col);

		for (int ir = 1; ir < row; ir++) {

			double[] min = { -1, Double.NEGATIVE_INFINITY };
			for (int ic = 0; ic < col; ic++) {
				min[1] = Double.NEGATIVE_INFINITY;
				double up = energyCopy[ir - 1][ic];
				if (up > min[1]) {
					min[0] = ic;
					min[1] = up;
				}
				double left = ic == 0 ? Double.NEGATIVE_INFINITY : energyCopy[ir - 1][ic - 1];
				if (left > min[1]) {
					min[0] = ic - 1;
					min[1] = left;
				}
				double right = ic == col - 1 ? Double.NEGATIVE_INFINITY : energyCopy[ir - 1][ic + 1];
				if (right > min[1]) {
					min[0] = ic + 1;
					min[1] = right;
				}
				energyCopy[ir][ic] = min[1] + energies[ir][ic];
				path[ir][ic] = (int) min[0];
			}
		}

		double[] lastrow = energyCopy[row - 1];
		double[][] rowdata = new double[lastrow.length][2];
		for (int i = 0; i < rowdata.length; i++) {
			rowdata[i][0] = lastrow[i];
			rowdata[i][1] = i;
		}

		int[][] topK = new int[K][];
		for (int i = 0; i < topK.length; i++) {
			int minCol = (int) rowdata[i][1];
			Stack<Integer> stack = new Stack<Integer>();
			int irow = row - 1;
			while (irow >= 0) {
				stack.push(minCol);
				minCol = path[irow][minCol];
				irow -= 1;
			}

			int[] p = new int[row];
			for (int k = 0; k < p.length; k++) {
				p[k] = stack.pop();
			}
			topK[i] = p;
		}

		return topK;
	}
}
