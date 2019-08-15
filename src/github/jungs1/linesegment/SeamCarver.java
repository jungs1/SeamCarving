package github.jungs1.linesegment;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Stack;

public class SeamCarver {

	private BufferedImage picture;
	private int[][] rgb;
	private double[][] energies;

	public SeamCarver(BufferedImage input) {
		this.picture = input;
		int row = input.getHeight();
		int col = input.getWidth();
		
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
		int row = picture.getHeight();
        int col = picture.getWidth();
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (i == 0 || j == 0 || i == row - 1
                        || j == col - 1) {
                    energies[i][j] = 1000.0;
                }
                else {
                    energies[i][j] = this.computeEnergy(rgb, i, j);
                }
            }
        }
	}
	
	private double computeEnergy(int [][] rgb, int row, int col) {

        int down = rgb[row - 1][col];
        int top = rgb[row + 1][col];

        int right = rgb[row][col + 1];
        int left = rgb[row][col - 1];

        double result = this.energeFn(left, right) + this.energeFn(top, down);

        return Math.sqrt(result);
    }
	
	private double energeFn(int c1, int c2) {
        int r1 = (c1 >> 16) & 0xFF;
        int g1 = (c1 >> 8) & 0xFF;
        int b1 = (c1 >> 0) & 0xFF;

        int r2 = (c2 >> 16) & 0xFF;
        int g2 = (c2 >> 8) & 0xFF;
        int b2 = (c2 >> 0) & 0xFF;

        return Math.pow(r1 - r2, 2) + Math.pow(g1 - g2, 2) + Math.pow(b1 - b2, 2);
    }

	
	public int[][] findSeams(int size) {
		int row = picture.getHeight(); // 130
		int col = picture.getWidth(); // 551
		System.out.printf("row: %d, col: %d\n", row, col);
		System.out.printf("energies) row: %d, col: %d\n", energies.length, energies[0].length);
        double[][] tp = new double[col][row]; // 551, 130
        for (int ir = 0; ir < row; ir++) {
            for (int ic = 0; ic < col; ic++) {
                tp[ic][ir] = energies[ir][ic];
            }
        }
        // TODO K까 1인 꼉우만 
        return seam(tp, col, row, size);
    }
	
	private int[][] seam(double [][] energies, int row, int col, int K) {
		
        int[][] path = new int[row][col]; // 3000, (1, 4):0 <- (3)
        double[][] acc = new double[row][col];

        System.arraycopy(energies[0], 0, acc[0], 0, col);


        for (int ir = 1; ir < row; ir++) {
            /*
                (ir-1, ic-1)   (ir-1, ic)  (ir-1, ic+1)
                            \      |       /
                                (ir, ic)
             */
            double[] min = { -1, Double.POSITIVE_INFINITY };
            for (int ic = 0; ic < col; ic++) {
                min[1] = Double.POSITIVE_INFINITY;
                double left = ic == 0 ? Double.POSITIVE_INFINITY : acc[ir - 1][ic - 1];
                if (left < min[1]) {
                    min[0] = ic - 1;
                    min[1] = left;
                }
                double up = acc[ir - 1][ic];
                if (up < min[1]) {
                    min[0] = ic;
                    min[1] = up;
                }
                double right = ic == col - 1 ? Double.POSITIVE_INFINITY : acc[ir - 1][ic + 1];
                if (right < min[1]) {
                    min[0] = ic + 1;
                    min[1] = right;
                }

                acc[ir][ic] = min[1] + energies[ir][ic];
                path[ir][ic] = (int) min[0];
            }
        }
        
        
        /*
         * 
         * 0
         * 1
         * 2
         * 3
         * .
         * ..
         * n 0:  2122 438, 1214 433, 129 4323, 4312, 0, 0, 0, 0
         *   1:     0   1     2   3    4   
         *       4    3    1     2
         *  => 129  433  438  1214  ... ... ..
         *  
         *  [
         *    [438, 5]
         *    ..
         *    .
         *    [129, 12]
         *    
         *    
         *    [129, 12]
         *    
         *    ..
         *    [438, 5]
         *     
         *     [4, 3, 1]
         */
        double [] lastrow = acc[row-1];
        
        double [][] rowdata = new double[lastrow.length][2];
        for (int i = 0; i < rowdata.length; i++) {
			// copy acc
        	rowdata[i][0] = lastrow[i];
        	rowdata[i][1] = i;
		}
        
        Arrays.sort(rowdata, new Comparator<>() {
			@Override
			public int compare(double[] a, double[] b) {
//				if ( a[0] > b[0]) return 1;
//				else if ( a[0] < b[0] ) return -1;
//				else return 0;
				
				return a[0] > b[0] ?  +1 : ( a[0] < b[0] ? -1 : 0) ;
				
				//1,2,34,5
				//5
			}
        });
        int [][] topK = new int[K][];
        for(int i = 0 ; i < topK.length ; i++) {
        	int minCol = (int)rowdata[i][1];
        	
        	Stack<Integer> stack = new Stack<Integer>();
		    		// 굳이 스택을 안써도 됨!
		    int irow = row - 1;
		    while (irow >= 0) {
		        // System.out.println(irow +", " + minCol);
		        stack.push(minCol);
		        minCol = path[irow][minCol];
		        irow -= 1;
		    }
		
		    int[] p = new int[row];
		    for (int k = 0; k < p.length; k++) {
		        p[k] = stack.pop();
		    }
		    topK[i] = p;
		    // System.out.println(Arrays.toString(p));
        }
        
        return topK;
	}

}
