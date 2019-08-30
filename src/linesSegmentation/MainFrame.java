package linesSegmentation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import energyFunction.AdjFourFunction;

/*
 * 
 * LineDectector dectector = new LineDetector("img.png");
 * List<Reactangle> imgAreas = detector.detectLines();
 * 
 * List<Reactangle> imgAreas = detector.detectPhrases();
 * 
 * Bootstrap.java
 */
public class MainFrame {
	static ArrayList<BufferedImage> images;

	static JFrame myFrame;
	static SeamViewer viewer;

	public static void main(String[] args) throws IOException {

		images = new ArrayList<BufferedImage>();
		JFrame frame = new JFrame();

		myFrame = frame;
		viewer = new SeamViewer();
		// myFrame.getContentPane().setBackground(Color.YELLOW);
		// viewer.setBackground(bg);
		installMenue(frame);

		frame.getContentPane().add(viewer, BorderLayout.CENTER);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	static void installMenue(JFrame f) {
		JMenuBar root = new JMenuBar();
		{
			JMenu fileMenu = new JMenu("File");
			JMenu showLine = new JMenu("Show Lines Only");
			root.add(fileMenu);
			root.add(showLine);
			{
				JMenuItem imgOpenMenum = new JMenuItem("open image");
				fileMenu.add(imgOpenMenum);
				imgOpenMenum.addActionListener(e -> processOpenImage());
			}
			{
				//				JMenuItem redLine = new JMenuItem("show red line only");
				//				showLine.add(redLine);
				//				redLine.addActionListener(l -> showLinesOnly());
				JCheckBoxMenuItem cbBg = new JCheckBoxMenuItem("show seam only");
				cbBg.addItemListener(e -> showLinesOnly(e));
				showLine.add(cbBg);
			}
			{
				JMenu energyFn = new JMenu("Energy Functions");
				root.add(energyFn);
				{
					JMenuItem fourAjdMenu = new JMenuItem("Four Adj");
					energyFn.add(fourAjdMenu);
				}
			}
			{
				JMenu processing = new JMenu("PreProcessing");
				root.add(processing);
				{
					JMenuItem blurring = new JMenuItem("Blur");
					processing.add(blurring);
					blurring.addActionListener(e -> processBlurring(true));
					
					JMenuItem clear = new JMenuItem("Clear");
					processing.add(clear);
					clear.addActionListener(e -> processBlurring(false));
				}
						
			}

		}
		f.setJMenuBar(root);
	}

	static void processBlurring(boolean enableBlur) {
		viewer.setBlurImage(enableBlur);
	}

	// 1. save image
	// 2. make seeamImg public static in SeamViewer class
	private static void showLinesOnly(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {
			//			viewer.setBackground(Color.WHITE);
			viewer.setSeamImageOnly(false);
		} else {
			viewer.setSeamImageOnly(true);
		}
		//		Rectangle r = myFrame.getBounds();
		//		int W = r.width;
		//		int H = r.height;
		//
		//		BufferedImage img = images.get(0);
		//		SeamViewer lineViewer = new SeamViewer(W, H);
		//		lineViewer.setSeamImageOnly();
		//		myFrame.getContentPane().remove(viewer);
		//		viewer = lineViewer;
		//		myFrame.getContentPane().add(viewer);
		//		myFrame.getContentPane().revalidate();
		//		viewer.setBackground(Color.WHITE);
		//		viewer.setSeamImageOnly();
		// myFrame.setContentPane(lineViewer);

		// myFrame.repaint();
	}

	private static void processOpenImage() {
		JFileChooser chooser = new JFileChooser(new File("test"));
		int retval = chooser.showOpenDialog(myFrame);
		if (retval == JFileChooser.APPROVE_OPTION) {
			File imgFile = chooser.getSelectedFile();
			try {
				BufferedImage img = ImageIO.read(imgFile);
				viewer.updateText(img);
			
				SwingUtilities.invokeLater(() -> {
					myFrame.pack();
					myFrame.setLocationRelativeTo(null);
				});
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		

	}

}