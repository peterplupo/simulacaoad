package br.ufrj.im.dcc.avaliacaodesempenho.estrutura;

import java.util.ArrayList;

/**
 * Classe refere-se ao publisher existente no sistema.
 * 
 * @version 1.0
 * */
public class Publisher {
	
	private ArrayList<Bloco> blocosSistema;
	private double instanteUploadPublisher;
	private int qtdBlocos;

	public Publisher(int qtdBlocos) {
		if(this.blocosSistema == null) {
			this.blocosSistema = new ArrayList<Bloco>();
			
			criaBlocos(qtdBlocos, this.blocosSistema);
			this.qtdBlocos = qtdBlocos;
		}
	}

	/* Cria os blocos que circularao no sistema. */
	private void criaBlocos(int qtdBlocos, ArrayList<Bloco> blocosSistema) {
		
		for(int i = 1; i < qtdBlocos+1; i++) {
			Bloco bloco =  new Bloco(i);
			blocosSistema.add(bloco);
		}
	}
	
	
	/* GETTER e SETTER. */
	public double getInstanteUploadPublisher() {
		return instanteUploadPublisher;
	}

	public void setInstanteUploadPublisher(double instanteUploadPublisher) {
		this.instanteUploadPublisher = instanteUploadPublisher;
	}
	
	public ArrayList<Bloco> getBlocosSistema() {
		return blocosSistema;
	}

	public int getQtdBlocos() {
		return qtdBlocos;
	}

	public void setQtdBlocos(int qtdBlocos) {
		this.qtdBlocos = qtdBlocos;
	}

}
