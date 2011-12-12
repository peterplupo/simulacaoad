package br.ufrj.dcc.ad201102.engine;

import java.util.Random;

public class Uniform {
	
	private Random random;
	
	public Uniform(long seed) {
		random = new Random(seed);
	}
	
	public Random getRandom() {
		return random;
	}
	
	public double nextRandom() {
		return random.nextDouble();
	}

}
