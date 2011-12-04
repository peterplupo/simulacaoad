package br.ufrj.im.dcc.avaliacaodesempenho.estrutura;

import java.util.ArrayList;

/**
 * Classe que representa um peer no sistema.
 * 
 * @version 1.0
 * */
public class Peer {
	
	private ArrayList<Bloco> blocosPeer;
	private double instanteEntradaSistema;
	private double instanteSaidaSistema;     
	private double instanteUploadPeer;   
	private boolean seed = false;
	
	public Peer() {
		
		if(blocosPeer == null) {
			blocosPeer = new ArrayList<Bloco>();
		}
		
	}
	
	/* Adiciona um novo bloco ao conjunto de blocos do peer. */
	public void addBloco(Bloco bloco){
		this.blocosPeer.add(bloco.getChaveBloco()-1, bloco);
	}
	
	/* Devolve a assinatura do peer. Refere-se a quantidade de blocos que um peer possui. */
	public ArrayList<Bloco> assinaturaPeer(){
		return blocosPeer;
	}
	
	/* GETTER e SETTER. */
	public double getInstanteUploadPeer() {
		return instanteUploadPeer;
	}

	public void setInstanteUploadPeer(double instanteUploadPeer) {
		this.instanteUploadPeer = instanteUploadPeer;
	}

	public double getInstanteSaidaSistema() {
		return instanteSaidaSistema;
	}

	public void setInstanteSaidaSistema(double instanteSaidaSistema) {
		this.instanteSaidaSistema = instanteSaidaSistema;
	}

	public ArrayList<Bloco> getBlocosPeer() {
		return blocosPeer;
	}
	
	public double getInstanteEntradaSistema() {
		return instanteEntradaSistema;
	}

	public void setInstanteEntradaSistema(double instanteEntradaSistema) {
		this.instanteEntradaSistema = instanteEntradaSistema;
	}

	public boolean isSeed() {
		return seed;
	}

	public void setSeed(boolean seed) {
		this.seed = seed;
	}
	
}