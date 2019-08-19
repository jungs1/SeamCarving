package github.jungs1.seamcarving.energy;

public class VerticalOriented implements EnergyFunction {

	@Override
	public double calcEnergyHelper(int[][] rgb, int row, int col, double oobValue) {
		
		if(row < 2 || row +2 >= rgb.length || col < 1 || col + 1 >= rgb[0].length) {
			return oobValue;
		}
		
		int left = rgb[row][col - 1];
		int right =rgb[row][col + 1];
		
		int down = rgb[row - 1][col];
		int top = rgb[row + 1][col];
		
		int top2 = rgb[row - 2][col];
		int down2 =rgb[row + 2][col];

		double result = this.energyFunction(left, right) +
				this.energyFunction(top, down) +
				this.energyFunction(top2, down2);
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

}
