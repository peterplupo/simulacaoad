package br.ufrj.im.dcc.avaliacaodesempenho.distribuicoes;

import java.util.Random;

/**
 * @version 1.0
 * */
public class Exponencial {
	
	private double taxa;
	private Random numeroAleatorio;

	public Exponencial(double taxa, long semente) {
		
		this.taxa = taxa;
		
		if(this.numeroAleatorio == null) {
			this.numeroAleatorio = new Random(semente);
		}
	}
	
	/* Gerador de numero aleatorio exponencial. */
	public double gerar() {
		return -Math.log(1 - this.numeroAleatorio.nextDouble())/this.taxa;
	}
	
	/* Calcula a esperanca matematica. */
	public double getEsperanca() {
		double esperanca = 1.0/this.taxa;
		return esperanca;
	}
	
	/* Calcula a variancia. */
	public double getVariancia(){
		double variancia = 1.0/(this.taxa * this.taxa);
		return variancia;
	}
	
	

}
