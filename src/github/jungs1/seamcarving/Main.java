package github.jungs1.seamcarving;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import github.jungs1.seamcarving.energy.AdjFourFunction;

/*
 * 
 * LineDectector dectector = new LineDetector("img.png");
 * List<Reactangle> imgAreas = detector.detectLines();
 * 
 * List<Reactangle> imgAreas = detector.detectPhrases();
 * 
 * 
 */
public class Main {

	static JFrame mainFrame;
	static SeamViewer viewer;

	public static void main(String[] args) throws IOException {
		JFrame frame = new JFrame();
		mainFrame = frame;
		viewer = new SeamViewer();

		installMenue(frame);
		frame.getContentPane().add(viewer, BorderLayout.CENTER);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	static void detectLines(BufferedImage text) {

		/**
		 * offsetX: x pixels away from left boarder of the image offsetY: y pixels away
		 * from top boarder of the image width: width of the divided image
		 */
		viewer.setTextImage(text);
		int offsetY = 0;
		int width = text.getWidth() / 10; // 5%
		int height = text.getHeight();
		// red lines[...offset,offset+len-1]
		// ArrayList<int[]> lines = new ArrayList<int[]>();
		Color seamTreeColor = transp(Color.MAGENTA, 0.0);

		for (int x = 0; x < text.getWidth(); x += width) {
			//			BufferedImage clip = clipImage(text, x, offsetY, width, height);
			BufferedImage clip = RedLine.clipImage(text, x, offsetY, width, height);
			// MaxSeamCarver sc = new MaxSeamCarver(clip);
			SeamCarver sc = new SeamCarver(clip, new AdjFourFunction());
			int[][] seams = sc.findSeams(height);

			viewer.drawSeam(seams, x, offsetY, width, height, seamTreeColor);
			int[][] paths = RedLine.reduceSeams(seams, x);

			viewer.drawSeam(paths, x, offsetY, width, height, Color.RED);

		}

		// lambda express in java
		SwingUtilities.invokeLater(() -> {
			mainFrame.pack();
			mainFrame.setLocationRelativeTo(null);
		});

	}

	static void installMenue(JFrame f) {
		JMenuBar root = new JMenuBar();
		{
			JMenu fileMenu = new JMenu("File");
			JMenu showLine = new JMenu("Show Line");
			root.add(fileMenu);
			root.add(showLine);
			{
				JMenuItem imgOpenMenum = new JMenuItem("open image");
				fileMenu.add(imgOpenMenum);
				imgOpenMenum.addActionListener(e -> processOpenImage());
			}

		}
		f.setJMenuBar(root);
	}

	static void processOpenImage() {
		JFileChooser chooser = new JFileChooser(new File("sample"));
		int retval = chooser.showOpenDialog(mainFrame);
		if (retval == JFileChooser.APPROVE_OPTION) {
			File imgFile = chooser.getSelectedFile();
			try {
				BufferedImage img = ImageIO.read(imgFile);
				detectLines(img);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	static Color transp(Color c, double alpha) {
		int r = c.getRed();
		int g = c.getGreen();
		int b = c.getBlue();
		int a = (int) (c.getAlpha() * alpha);
		return new Color(r, g, b, a);
	}

	static int[][] connect(ArrayList<int[]> lines) {
		ArrayList<int[]> blueLines = new ArrayList<int[]>();
		for (int i = 0; i < lines.size() - 1; i++) {
			//
			int[] src = lines.get(i); //
			//   0   1   2   3   4
			// [12, 13, 14, 13, 12, scol, ecol]
			// int sCol = line.length-2;
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
			// tail point of src
			int srcRow = src[src.length - 3];
			int srcCol = src[src.length - 1]; // tail column index
			// int srcCol = src.length - 1; 
			int endRow = 0;
			int endCol = 0;

			int min = Integer.MAX_VALUE;
			for (int k = i + 1; k < lines.size(); k++) {
				int[] dst = lines.get(k);
				// head point of dst
				int dstRow = dst[0];
				int dstCol = dst[dst.length - 2];
				if (dstCol <= srcCol) {
					continue;
				}
				int dist = Math.abs(dstRow - srcRow) + Math.abs(dstCol - srcCol);
				double slope = Math.abs(1.0 * (srcRow - dstRow) / (srcCol - dstCol));

				if (dist < min && slope < 0.5) {
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
