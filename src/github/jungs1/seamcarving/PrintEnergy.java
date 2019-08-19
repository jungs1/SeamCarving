package github.jungs1.seamcarving;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class PrintEnergy {
	public static void main(String[] args) throws IOException {

		BufferedImage picture = ImageIO.read(new File("sample/test.png"));
		System.out.printf("image is %d pixels wide by %d pixels high.\n", picture.getWidth(), picture.getHeight());

		SeamCarver sc = new SeamCarver(picture, null);

		System.out.printf("Printing energy calculated for each pixel.\n");

		for (int row = 0; row < sc.getHeight(); row++) {
			for (int col = 0; col < sc.getWidth(); col++) {
				System.out.printf("%9.0f ", sc.energy(row, col));
			}
			System.out.println();
		}

	}

}
