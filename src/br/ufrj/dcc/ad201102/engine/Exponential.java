package br.ufrj.dcc.ad201102.engine;

import java.util.Random;

/**
 * @version 1.0
 * */
public class Exponential {
	
	private double rate;
	private Random random;

	public Exponential(double rate, long seed) {
		
		this.rate = rate;
		
		if(random == null) {
			random = new Random(seed);
		}
	}
	
	/* Gerador de numero aleatorio exponencial. */
	public double nextRandom() {
		return -Math.log(1 - random.nextDouble())/rate;
	}
	
	/* Calcula a esperanca matematica. */
	public double getMean() {
		return 1.0/rate;
	}
	
	/* Calcula a variancia. */
	public double getVariance(){
		return 1.0/(rate * rate);
	}
	
	

}
