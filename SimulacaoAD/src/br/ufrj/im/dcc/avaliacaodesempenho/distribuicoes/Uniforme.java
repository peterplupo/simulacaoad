package br.ufrj.im.dcc.avaliacaodesempenho.distribuicoes;

import java.util.Random;

public class Uniforme {
	
	private Random random;
	
	public Uniforme(long seed) {
		random = new Random(seed);
	}
	
	/* Funcao que gera um numero aleatorio uniformemente distribuido entre 0 e (i-1). */
	public int geraUniforme(int i) {
		return random.nextInt(i);
	}
	
	public double geraProbabilidade(){
		return random.nextDouble();
	}

}
