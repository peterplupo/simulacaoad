package br.ufrj.dcc.ad2001102.simulacao;

import java.util.Random;

public class ExponentialRandom {
	
	private double rate;
	private static Random normalRandom;

	public ExponentialRandom(double rate, long seed) {
		this.rate = rate;
		if (normalRandom == null) {
			normalRandom = new Random(seed);
		}
	}

	public double nextDouble() {
		return -Math.log(1 - normalRandom.nextDouble()) / rate;
	}

	public double getExpectedMean() {
		return 1 / rate;
	}

	public double getExpectedVariance() {
		return 1 / (rate * rate);
	}
}