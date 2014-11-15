package us.malfeasant.spectralyzer;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Band {
	private static final Executor exec = Executors.newCachedThreadPool();
	
	private final float[] sin;
	private final float[] cos;
	
	public Band(float step, int samples) {
		// set up reference waveforms
		sin = new float[samples];
		cos = new float[samples];
		for (int s = 0; s < samples; ++s) {
			sin[s] = (float) (Math.sin(step * s));
			cos[s] = (float) (Math.cos(step * s));
		}
		
	}
}
