package us.malfeasant.spectralyzer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;

public class Main {
	private static final int RATE = 8000;
	private static final int SAMPLES = 200;
	
	private static final float[] FREQ = {
//		1070, 1270, 2025, 2225, 	// bell 103
//		55, 110, 220, 440, 880, 1760, 3520	// octaves of A
//		350, 440, 480, 620,	// busy/ring/dial
		697, 770, 852, 941, 1209, 1336, 1477, 1633,	// DTMF
//		 261.63f,  277.18f,  293.66f,  311.13f,  329.63f,  349.23f,  369.99f,  392.00f,  415.30f,  440.00f,  466.16f,  493.88f,	// Middle C scale
//		 523.26f,  554.36f,  587.32f,  622.26f,  659.26f,  698.46f,  739.98f,  784.00f,  830.60f,  880.00f,  932.32f,  987.76f,	// C5 scale
//		1046.52f, 1108.72f, 1174.64f, 1244.52f, 1318.52f, 1396.92f, 1479.96f, 1568.00f, 1661.20f, 1760.00f, 1864.64f, 1975.52f,	// C6 scale
		};
	
	private static final Band[] bands = new Band[FREQ.length];
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> buildGUI());
	}
	
	private static void buildGUI() {
		JFrame frame = new JFrame("Spectralyzer");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Box hBox = Box.createHorizontalBox();
		for (int b = 0; b < FREQ.length; ++b) {
			Box vBox = Box.createVerticalBox();
			JSlider slider = new JSlider(JSlider.VERTICAL);
			slider.setEnabled(false);	// using this for output only
			vBox.add(slider);
			vBox.add(new JLabel(Float.toString(FREQ[b])));
			hBox.add(vBox);
			
			float step = (float) (2 * Math.PI * FREQ[b] / RATE);
			bands[b] = new Band(step, SAMPLES, slider);
		}
		
		beginRecording();
		
		frame.add(hBox);
		frame.pack();
		frame.setVisible(true);
	}
	
	private static void beginRecording() {
		AudioFormat format = new AudioFormat(RATE, 8, 1, true, false);
		DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
		
		try {
			TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info);
			line.open();
			line.start();
			
			new Thread(new Runnable() {
				@Override
				public void run() {
					byte[] buffer = new byte[SAMPLES];
					while (!Thread.interrupted()) {
						line.read(buffer, 0, buffer.length);
						float[] asFloat = new float[SAMPLES];
						for (int i=0; i < SAMPLES; ++i) {
							asFloat[i] = buffer[i] / 128f;
						}
//						System.out.println(asFloat[0]);
						for (Band b : bands) {
							b.enqueue(asFloat);
						}
					}
				}
			}).start();
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
