package us.malfeasant.spectralyzer;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;

public class Main {
	private static final int RATE = 8000;
	private static final int SAMPLES = 200;
	
	private static final float[] FREQ = {
//		55, 110, 220, 440, 880, 1760, 3520	// octaves of A
		350, 440, 480, 620,	// busy/ring/dial
		697, 770, 852, 941, 1209, 1336, 1477, 1633,	// DTMF
//		261.63f, 277.18f, 293.66f, 311.13f, 329.63f, 349.23f, 369.99f, 392.00f, 415.30f, 440.00f, 466.16f, 493.88f	// Middle C scale
		};
	
	private static final Band[] bands = new Band[FREQ.length];
	private static final JSlider[] sliders = new JSlider[FREQ.length];
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> buildGUI());
	}
	
	private static void buildGUI() {
		JFrame frame = new JFrame("Spectralyzer");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Box hBox = Box.createHorizontalBox();
		
		// set up reference waveforms
		for (int b = 0; b < FREQ.length; ++b) {
			float step = (float) (2 * Math.PI * FREQ[b] / RATE);
			bands[b] = new Band(step, SAMPLES);
			
			Box vBox = Box.createVerticalBox();
			sliders[b] = new JSlider(JSlider.VERTICAL);
			sliders[b].setEnabled(false);
			vBox.add(sliders[b]);
			JLabel label = new JLabel(Float.toString(FREQ[b]));
			vBox.add(label);
			hBox.add(vBox);
		}
		
		frame.add(hBox);
		frame.pack();
		frame.setVisible(true);
	}
}
