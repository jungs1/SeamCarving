package linesSegmentation;

import java.awt.image.BufferedImage;
import java.util.Stack;

import energyFunction.EnergyFunction;

public class SeamCarver {

	private BufferedImage picture;
	private int[][] rgb;
	private double[][] energies;
	int row, col;

	private EnergyFunction energyFunction;

	public SeamCarver(BufferedImage input, EnergyFunction fnc) {
		this.picture = input;
		this.energyFunction = fnc;
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
				energies[i][j] = energyFunction.calcEnergyHelper(rgb, i, j, 1000.0);
			}
		}
	}

	/*
	 * Transpose energy matrix and return the seam
	 */
	public int[][] findSeams(int size) {
		double[][] tp = new double[col][row]; // 551, 130
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
			double[] min = { -1, Double.POSITIVE_INFINITY };
			for (int ic = 0; ic < col; ic++) {
				min[1] = Double.POSITIVE_INFINITY;
				double up = energyCopy[ir - 1][ic];
				if (up < min[1]) {
					min[0] = ic;
					min[1] = up;
				}
				double left = ic == 0 ? Double.POSITIVE_INFINITY : energyCopy[ir - 1][ic - 1];
				if (left < min[1]) {
					min[0] = ic - 1;
					min[1] = left;
				}
				double right = ic == col - 1 ? Double.POSITIVE_INFINITY : energyCopy[ir - 1][ic + 1];
				if (right < min[1]) {
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
