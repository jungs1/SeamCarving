import java.awt.image.BufferedImage;

//import edu.princeton.cs.algs4.Picture;
//import edu.princeton.cs.algs4.Stack;

public class SeamCarver0 {
    private Picture picture;
    private int[][] rgb;
    private double[][] energies;

    private int row, col;

    private int decW;
    private int decH;

    // create a seam carver object based on the given picture
    public SeamCarver0(BufferedImage picture) {
        if (picture == null) {
            throw new IllegalArgumentException();
        }
        this.row = picture.getWidth();
        this.col = picture.getHeight();

        this.picture = new Picture(picture);

        this.energies = new double[row][col];
        this.rgb = new int[row][col];
        for (int i = 0; i < this.energies.length; i++) {
            for (int j = 0; j < this.energies[0].length; j++) {
                this.rgb[i][j] = this.picture.getRGB(j, i);
            }
        }

        calcEnergy();
    }

    private void calcEnergy() {
        int row = height();
        int col = width();
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (i == 0 || j == 0 || i == row - 1
                        || j == col - 1) {
                    energies[i][j] = 1000.0;
                }
                else {
                    energies[i][j] = this.computeEnergy(i, j);
                }
            }
        }
    }

    private double computeEnergy(int row, int col) {

        int down = rgb[row - 1][col];
        int top = rgb[row + 1][col];

        int right = rgb[row][col + 1];
        int left = rgb[row][col - 1];
        // int down = picture.getRGB(col, row - 1);
        // int top = picture.getRGB(col, row + 1);
        //
        //
        // int right = picture.getRGB(col + 1, row);
        // int left = picture.getRGB(col - 1, row);

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

    //  unit testing (optional)
    public static void main(String[] args) {
    }

    // current picture
    public Picture picture() {
        if (decH > 0 || decW > 0) {

            Picture pic = new Picture(this.width(), this.height());
            for (int i = 0; i < pic.width(); i++) {
                for (int j = 0; j < pic.height(); j++) {
                    pic.setRGB(i, j, this.rgb[j][i]);
                }
            }
            this.picture = pic;
            decH = 0;
            decW = 0;
        }


        return new Picture(this.picture);
    }

    // width of current picture
    public int width() {
        return this.picture.width() - decW;
    }

    // height of current picture
    public int height() {
        return this.picture.height() - decH;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x == -1 || y == -1 || x >= this.width() || y >= this.height()) {
            throw new IllegalArgumentException();
        }
        return this.energies[y][x];
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        // this.picture.

        int row = width();
        int col = height();
        double[][] tp = new double[row][col];
        for (int ir = 0; ir < row; ir++) {
            for (int ic = 0; ic < col; ic++) {
                tp[ir][ic] = energies[ic][ir];
            }
        }
        return findSeam(tp, row, col);
    }


    private int[] findSeam(double[][] energies, int row, int col) {
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
        double minACC = Double.POSITIVE_INFINITY;
        // System.out.println(Arrays.toString(acc[row-1]));
        int minCol = -1;
        for (int ic = 0; ic < col; ic++) {
            if (acc[row - 1][ic] < minACC) {
                minACC = acc[row - 1][ic];
                minCol = ic;
            }
        }


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
        for (int i = 0; i < p.length; i++) {
            p[i] = stack.pop();
        }
        return p;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        int col = this.width();
        int row = this.height();
        return findSeam(this.energies, row, col);
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        int col = width();
        int row = height();
        checkSeam(seam, row, col);
        for (int ic = 0; ic < col; ic++) {
            int irow = seam[ic]; // (i, seam[i]) /git
            for (int k = irow; k < row - 1; k++) {
                rgb[k][ic] = rgb[k + 1][ic];

            }
        }
        decH++;
        // this.picture = copy;
        calcEnergy();

    }

    private void checkSeam(int[] seam, int limit, int length) {
        if (seam == null || seam.length != length) {
            throw new IllegalArgumentException();
        }

        int i = 0;
        for (; i < seam.length - 1; i++) {
            if (seam[i] < 0 || seam[i] >= limit || Math.abs(seam[i] - seam[i + 1]) >= 2) {
                throw new IllegalArgumentException();
            }
        }

        if (seam[i] < 0 || seam[i] >= limit) {
            throw new IllegalArgumentException();
        }
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        //   0  1  2  3  4  5
        // [ 111, 112, 5, 2, 3, 4, 5, 5, -1, 5]
        // (0,111) :[0, 110] 111 [112, ...)
        // image size --;
        // Picture copy = new Picture(this.picture.width() - 1, this.picture.height());
        int row = height();
        int col = width();
        checkSeam(seam, col, row);
        for (int i = 0; i < seam.length; i++) {
            int icol = seam[i]; // (i, seam[i])
            // for (int k = icol; k < col - 1; k++) {
            //
            //     rgb[i][k] = rgb[i][k + 1];
            //
            // }
            // 6
            // . . . . . .
            //     2
            System.arraycopy(rgb[i], icol + 1, rgb[i], icol, col - icol - 1);
        }
        decW++;
        // this.picture = copy;
        calcEnergy();

    }

    private void bfs(double[][] e, int[][] path, double[][] acc, int row, int col,
                     double energy) {

        if (row == e.length) {
            return;
        }

        // double curAcc = energy +

        bfs(e, path, acc, row + 1, col - 1, e[row][col]);
        bfs(e, path, acc, row + 1, col, e[row][col]);
        bfs(e, path, acc, row + 1, col + 1, e[row][col]);
    }

}