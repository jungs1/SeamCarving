package linesSegmentation;

import java.awt.image.BufferedImage;

public class BlurImage {
	public BlurImage() {
	}

	public BufferedImage blurImage(BufferedImage img, int kernelSize) {

		int W = img.getWidth();
		int H = img.getHeight();
		BufferedImage blurred = new BufferedImage(W, H, img.getType());

		int offsetR = kernelSize / 2;
		int offsetC = offsetR;
		int size = kernelSize * kernelSize;
		for (int row = offsetR; row < H - offsetR; row++) {
			for (int col = offsetC; col < W - offsetC; col++) {
				int red = 0;
				int green = 0;
				int blue = 0;
				for (int or = -offsetR; or <= offsetR; or++) {
					for (int oc = -offsetC; oc <= offsetC; oc++) {
						int color = img.getRGB(col + oc, row + or);
						red += ((color >> 16) & 0xFF);
						green += ((color >> 8) & 0xFF);
						blue += color & 0xFF;

					}
				}
				red /= size;
				green /= size;
				blue /= size;

				blurred.setRGB(col, row, (255 << 24) + (red << 16) + (green << 8) + blue);
			}
		}

		return blurred;
	}

	//	private int[][] prepareKernel(int kernelSize) {
	//		int [][] k = new int[kernelSize][kernelSize];
	//		for (int i = 0; i < k.length; i++) {
	//			Arrays.fill(k[i], 1);
	//		}
	//		return k;
	//	}

}
