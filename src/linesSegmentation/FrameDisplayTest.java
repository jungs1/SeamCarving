package linesSegmentation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class FrameDisplayTest implements ActionListener {
	/*
	 * Creating an object of JFrame instead of extending it 
	 * has no side effects.
	 */
	private JFrame frame;
	private JPanel panel, panel1;
	private JTextField tfield;
	private JButton nextButton, backButton;

	public FrameDisplayTest() {
		frame = new JFrame("Frame Display Test");
		// If you running your program from cmd, this line lets it comes
		// out of cmd when you click the top-right  RED Button.
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		panel = new JPanel();
		panel1 = new JPanel();

		tfield = new JTextField(10);

		nextButton = new JButton("NEXT");
		backButton = new JButton("BACK");
		nextButton.addActionListener(this);
		backButton.addActionListener(this);

		panel.add(tfield);
		panel.add(nextButton);
		panel1.add(backButton);

		frame.setContentPane(panel);
		frame.setSize(220, 220);
		frame.setVisible(true);
	}

	public void actionPerformed(ActionEvent ae) {
		JButton button = (JButton) ae.getSource();
		if (tfield.getText().length() > 0) {
			if (button == nextButton) {
				/*
				 * this will remove the first panel 
				 * and add the new panel to the frame.
				 */
				frame.remove(panel);
				frame.setContentPane(panel1);
			} else if (button == backButton) {
				frame.remove(panel1);
				frame.setContentPane(panel);
			}
			frame.validate();
			frame.repaint(); // prefer to write this always.
		}
	}

	public static void main(String[] args) {
		/*
		 * This is the most important part ofyour GUI app, never forget 
		 * to schedule a job for your event dispatcher thread : 
		 * by calling the function, method or constructor, responsible
		 * for creating and displaying your GUI.
		 */
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new FrameDisplayTest();
			}
		});
	}
}