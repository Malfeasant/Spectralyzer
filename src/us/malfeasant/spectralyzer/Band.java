package us.malfeasant.spectralyzer;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.swing.JSlider;
import javax.swing.SwingUtilities;

public class Band {
	private static final Executor exec = Executors.newCachedThreadPool();
	
	private final float[] sin;
	private final float[] cos;
	
	private final JSlider slider;
	
	public Band(float step, int samples, JSlider slider) {
		this.slider = slider;
		// set up reference waveforms
		sin = new float[samples];
		cos = new float[samples];
		for (int s = 0; s < samples; ++s) {
			sin[s] = (float) (Math.sin(step * s));
			cos[s] = (float) (Math.cos(step * s));
		}
	}
	
	public void enqueue(float[] buffer) {
		exec.execute(() -> process(buffer));
	}
	private void process(float[] buffer) {
		float sSum = 0;	// sum of sin * incoming for each band
		float cSum = 0;	// sum of cos * incoming for each band
		for (int sample = 0; sample < buffer.length; sample++) {
			sSum += (sin[sample] * (buffer[sample]));
			cSum += (cos[sample] * (buffer[sample]));
		}
		int power = (int) ((sSum * sSum + cSum * cSum) );
		SwingUtilities.invokeLater(() -> slider.setValue(power));
	}
}
